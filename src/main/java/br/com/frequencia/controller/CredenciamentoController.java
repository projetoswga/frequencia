package br.com.frequencia.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import br.com.frequencia.action.AbstractAction;
import br.com.frequencia.action.BooleanExpression;
import br.com.frequencia.action.ConditionalAction;
import br.com.frequencia.action.TransactionalAction;
import br.com.frequencia.dao.CredenciamentoDAO;
import br.com.frequencia.dao.EventoDAO;
import br.com.frequencia.dao.OrgaoDAO;
import br.com.frequencia.dao.ParticipanteDAO;
import br.com.frequencia.dao.impl.CredenciamentoDAOImpl;
import br.com.frequencia.dao.impl.EventoDAOImpl;
import br.com.frequencia.dao.impl.OrgaoDAOImpl;
import br.com.frequencia.dao.impl.ParticipanteDAOImpl;
import br.com.frequencia.event.CredenciarParticipanteEvent;
import br.com.frequencia.model.Credenciamento;
import br.com.frequencia.model.Evento;
import br.com.frequencia.model.Participante;
import br.com.frequencia.ui.frame.PrincipalFrame;

/**
 * Define a <code>Controller</code> responsável por manipular a aba de <code>Credencimento</code>.
 * 
 * @see br.com.frequencia.controller.PersistenceController
 * 
 */
public class CredenciamentoController extends PersistenceController {

	private PrincipalFrame frame;
	private List<Credenciamento> credenciados;
	private CredenciamentoDAO credenciamentoDAO;
	private static String MSG_PARTICIPANTE_JA_CREDENCIADO = "Participante já credenciado!";
	private static String MSG_PARTICIPANTE_NAO_ENCONTRADO = "Participante não encontrado, favor realizar o cadastro na Aba Cadastramento.";
	private static String MSG_MAIS_DE_UM_PARTICIPANTE_ENCONTRADO = "Mais de um participante foi encontrado, favor especificar mais a pesquisa.";
	private static String MSG_VALIDADCAO = "Informe pelo menos um critério antes de clicar em 'Credenciar'.";
	private static String MSG_EXCLUSAO_REGISTRO = "Nenhum registro selecionado. Selecione um registro da lista para a exclução.";

	public CredenciamentoController(AbstractController parent, final PrincipalFrame frame) {
		super(parent);
		loadPersistenceContext();
		frame.addWindowListener(this);
		this.frame = frame;
		credenciados = new ArrayList<Credenciamento>();
		credenciamentoDAO = new CredenciamentoDAOImpl(getPersistenceContext());
		frame.getLabelMensagemSucessoCredenciamento().setVisible(Boolean.FALSE);

		registerAction(frame.getButtonRegistrarCredenciamento(), ConditionalAction.build().addConditional(new BooleanExpression() {

			/**
			 * Verificando se o usuário informou pelo menos um critério de pesquisa. Do contrário poderá retornar mais de um registro
			 * invalidando o credenciamento.
			 */
			public boolean conditional() {
				Participante participanteCredenciado = frame.getParticipanteCredenciamento();
				if ((participanteCredenciado.getNumInscricao() == null || participanteCredenciado.getNumInscricao().isEmpty())
						&& (participanteCredenciado.getNomParticipante() == null || participanteCredenciado.getNomParticipante().isEmpty())
						&& (participanteCredenciado.getOrgao().getId() == 0)) {
					JOptionPane.showMessageDialog(frame, MSG_VALIDADCAO, TITLE_INFORMACAO, JOptionPane.ERROR_MESSAGE);
					return false;
				}
				return true;
			}
		}).addAction(TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {

			@Override
			public void action() {
				frame.getLabelMensagemSucessoCredenciamento().setVisible(Boolean.FALSE);

				/**
				 * Populando uma lista total com registros da lista de inscritos e da lista de novos participantes
				 */
				Participante participanteCredenciado = frame.getParticipanteCredenciamento();
				ParticipanteDAO participanteDAO = new ParticipanteDAOImpl(getPersistenceContext());
				List<Participante> listaParticipantes = new ArrayList<Participante>();
				listaParticipantes.addAll(participanteDAO.pesquisarTodosParticipantes(participanteCredenciado));

				/**
				 * Verificando a possibilidade de registrar o participante no evento. A pesquisa tem que retornar exatamente 1 registro e
				 * que já não tenha sido credenciado
				 */
				if (listaParticipantes == null || listaParticipantes.isEmpty()) {
					JOptionPane.showMessageDialog(frame, MSG_PARTICIPANTE_NAO_ENCONTRADO, TITLE_ERROR, JOptionPane.ERROR_MESSAGE);
				} else if (listaParticipantes.size() > 1) {
					JOptionPane.showMessageDialog(frame, MSG_MAIS_DE_UM_PARTICIPANTE_ENCONTRADO, TITLE_ERROR, JOptionPane.ERROR_MESSAGE);
				} else {
					/**
					 * Verificando se o participante já está credenciado
					 */
					boolean credenciado = Boolean.FALSE;
					if (credenciados != null) {
						for (Credenciamento cred : credenciados) {
							if (cred.getParticipante().getId().equals(listaParticipantes.get(0).getId())) {
								JOptionPane.showMessageDialog(frame, MSG_PARTICIPANTE_JA_CREDENCIADO, TITLE_ERROR,
										JOptionPane.ERROR_MESSAGE);
								credenciado = Boolean.TRUE;
								break;
							}
						}
					}

					/**
					 * Caso o participante não esteja credenciado
					 */
					if (!credenciado) {
						credenciamentoDAO.save(getCredenciamento(listaParticipantes.get(0)));
						//JOptionPane.showMessageDialog(frame, MSG_SUCESSO, TITLE_INFORMACAO, JOptionPane.INFORMATION_MESSAGE);
						frame.getLabelMensagemSucessoCredenciamento().setVisible(Boolean.TRUE);
					}
				}
			}

			@Override
			public void posAction() {
				cleanUp();
				refreshTableCredenciados();
				fireEvent(new CredenciarParticipanteEvent(credenciados));
			}
		})));

		registerAction(frame.getButtonLimparCredenciamento(), new AbstractAction() {
			public void action() {
				frame.getLabelMensagemSucessoCredenciamento().setVisible(Boolean.FALSE);
				cleanUp();
			}
		});

		registerAction(frame.getjMenuItemExcluirCredenciamento(),
				TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {

					@Override
					protected void action() {
						if (frame.getListaCredenciamento().getCredenciadoSelected() != null) {
							credenciamentoDAO.remove(frame.getListaCredenciamento().getCredenciadoSelected());
						} else {
							JOptionPane.showMessageDialog(frame, MSG_EXCLUSAO_REGISTRO, TITLE_ERROR, JOptionPane.ERROR_MESSAGE);
						}
					}

					@Override
					public void posAction() {
						cleanUp();
						refreshTableCredenciados();
						JOptionPane.showMessageDialog(frame, MSG_SUCESSO, TITLE_INFORMACAO, JOptionPane.INFORMATION_MESSAGE);
						fireEvent(new CredenciarParticipanteEvent(credenciados));
					}
				}));

		carregarComboOrgao();
		refreshTableCredenciados();
	}

	public void refreshTableCredenciados() {
		credenciados = credenciamentoDAO.getAll();
		frame.getLabelListaCredenciados().setText("Participantes Credenciados:  " + credenciados.size());
		frame.refreshTableCredenciamento(credenciados);
	}

	private Credenciamento getCredenciamento(Participante participante) {
		Credenciamento credenciamento = new Credenciamento();
		credenciamento.setParticipante(participante);
		credenciamento.setEvento(getEvento());
		Date data = new Date();
		credenciamento.setDataCadastro(data);
		credenciamento.setHorarioEntrada(Calendar.getInstance());
		return credenciamento;
	}

	private Evento getEvento() {
		EventoDAO eventoDAO = new EventoDAOImpl(getPersistenceContext());
		return eventoDAO.pesquisarEventoAtivo();
	}

	private void carregarComboOrgao() {
		OrgaoDAO orgaoDao = new OrgaoDAOImpl(getPersistenceContext());
		frame.setListaOrgaoCredenciamento(orgaoDao.getAll());
	}

	public void loadPersistenceContext() {
		loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
	}

	@Override
	protected void cleanUp() {
		frame.resetAbaCredenciamento();
		super.cleanUp();
	}

}
