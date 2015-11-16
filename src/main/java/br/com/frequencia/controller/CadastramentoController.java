package br.com.frequencia.controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import javax.swing.JOptionPane;

import br.com.frequencia.action.AbstractAction;
import br.com.frequencia.action.TransactionalAction;
import br.com.frequencia.dao.OrgaoDAO;
import br.com.frequencia.dao.ParticipanteDAO;
import br.com.frequencia.dao.impl.OrgaoDAOImpl;
import br.com.frequencia.dao.impl.ParticipanteDAOImpl;
import br.com.frequencia.event.PesquisarNovosParticipantesEvent;
import br.com.frequencia.event.PesquisarParticipanteInscritoEvent;
import br.com.frequencia.model.Participante;
import br.com.frequencia.ui.frame.PrincipalFrame;

/**
 * Define a <code>Controller</code> responsável por manipular a aba de <code>Cadastramento</code>.
 * 
 * @see br.com.frequencia.controller.PersistenceController
 * 
 */
public class CadastramentoController extends PersistenceController {

	private PrincipalFrame frame;
	private IncluirNovoParticipanteController incluirNovoParticipanteController;
	private List<Participante> listaParticipantesNovos;
	private List<Participante> listaParticipanteInscritos;
	private static String CAMINHO_ARQUIVO = "C:/Frequencia/Atualizacao.sql";
	private static String MSG_LISTAS_VAZIAS = "Não existem novos participantes para gerar arquivo!";

	public CadastramentoController(AbstractController parent, final PrincipalFrame frame) {
		super(parent);
		loadPersistenceContext();
		frame.addWindowListener(this);
		this.frame = frame;
		incluirNovoParticipanteController = new IncluirNovoParticipanteController(this, frame);

		registerAction(frame.getButtonNovoParticipante(), new AbstractAction() {
			public void action() {
				incluirNovoParticipanteController.show();
			}
		});

		registerAction(frame.getButtonGerarArquivo(), TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {

			private ParticipanteDAO dao;
			private File file = new File(CAMINHO_ARQUIVO);

			@Override
			public void action() {
				dao = new ParticipanteDAOImpl(getPersistenceContext());
				listaParticipantesNovos = dao.pesquisarParticipantesNovosGeracaoArquivo();
				listaParticipanteInscritos = dao.pesquisarParticipantesInscritosGeracaoArquivo();
				if ((listaParticipanteInscritos == null || listaParticipanteInscritos.isEmpty())
						&& (listaParticipantesNovos == null || listaParticipantesNovos.isEmpty())) {
					JOptionPane.showMessageDialog(frame, MSG_LISTAS_VAZIAS, TITLE_INFORMACAO, JOptionPane.INFORMATION_MESSAGE);
				} else {
					try {
						file.getParentFile().mkdirs();
						PrintWriter pw = new PrintWriter(file);
						montarScriptNovosParticipantes(pw);
						montarScriptParticipantesInscritos(pw);
						pw.flush();
						pw.close();
						JOptionPane.showMessageDialog(frame, "Arquivo de participantes novos gerado com sucesso em: C:/Frequencia",
								TITLE_INFORMACAO, JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			/**
			 * Gera o script dos participantes inscritos. será realizado apenas update.
			 * 
			 * @param pw
			 */
			private void montarScriptParticipantesInscritos(PrintWriter pw) {
				for (Participante participante : listaParticipanteInscritos) {
					String usuario = "null";
					if (participante.getUsuario() != null && participante.getUsuario().getId() != null) {
						usuario = participante.getUsuario().getId().toString();
					}
					pw.println("UPDATE PARTICIPANTE SET ID_ORGAO = " + participante.getOrgao().getId() + ",ID_USUARIO = " + usuario
							+ ",NUM_INSCRICAO = '" + participante.getNumInscricao() + "',NOM_PARTICIPANTE = '"
							+ participante.getNomParticipante() + "',NOM_CRACHA = '" + participante.getNomCracha()
							+ "',FLG_RECEM_CADASTRADO = " + 0 + ",FLG_ALTERADO = " + 0 + " WHERE ID_PARTICIPANTE = " + participante.getId()
							+ ";");
					participante.setFlgRecemCadastrado(Boolean.FALSE);
					participante.setFlgAlterado(Boolean.FALSE);
					dao.save(participante);
				}
			}

			/**
			 * Gera o script dos participantes novos. será realizado inserts.
			 * 
			 * @param pw
			 */
			private void montarScriptNovosParticipantes(PrintWriter pw) {
				for (Participante participante : listaParticipantesNovos) {
					String usuario = "null";
					if (participante.getUsuario() != null && participante.getUsuario().getId() != null) {
						usuario = participante.getUsuario().getId().toString();
					}
					String orgao = "null";
					if (participante.getOrgao() != null && participante.getOrgao().getId() != null){
						orgao = participante.getOrgao().getId().toString();
					}
					pw.println("INSERT INTO PARTICIPANTE (ID_ORGAO,ID_USUARIO,NUM_INSCRICAO,NOM_PARTICIPANTE,NOM_CRACHA,FLG_RECEM_CADASTRADO,FLG_ALTERADO) VALUES ("
							+ orgao
							+ ","
							+ usuario
							+ ",'"
							+ participante.getNumInscricao()
							+ "','"
							+ participante.getNomParticipante() + "','" + participante.getNomCracha() + "'," + 0 + "," + 0 + ");");
					participante.setFlgRecemCadastrado(Boolean.FALSE);
					participante.setFlgAlterado(Boolean.FALSE);
					dao.save(participante);
				}
			}

			@Override
			public void posAction() {
				cleanUp();
				refreshTables();
				listaParticipantesNovos = null;
			}
		}));

		registerAction(frame.getButtonPesquisarInscritos(), new AbstractAction() {

			@Override
			public void action() {
				ParticipanteDAO dao = new ParticipanteDAOImpl(getPersistenceContext());
				listaParticipanteInscritos = dao.pesquisarParticipantesInscritos(frame.getParticipante());
			}

			@Override
			public void posAction() {
				cleanUp();
				fireEvent(new PesquisarParticipanteInscritoEvent(listaParticipanteInscritos));
				listaParticipanteInscritos = null;
			}
		});

		registerAction(frame.getButtonPesquisarNovosParticipantes(), new AbstractAction() {

			@Override
			public void action() {
				ParticipanteDAO dao = new ParticipanteDAOImpl(getPersistenceContext());
				listaParticipantesNovos = dao.pesquisarParticipantes(frame.getParticipante());
			}

			@Override
			public void posAction() {
				cleanUp();
				fireEvent(new PesquisarNovosParticipantesEvent(listaParticipantesNovos));
				listaParticipantesNovos = null;
			}
		});

		registerAction(frame.getButtonLimparAbaInscricao(), new AbstractAction() {
			public void action() {
				cleanUp();
			}
		});

		registerAction(frame.getjMenuItemExcluirNovoParticipante(),
				TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {

					@Override
					protected void action() {
						if (frame.getListaNovosParticipantes().getParticipanteSelected() != null) {
							ParticipanteDAO dao = new ParticipanteDAOImpl(getPersistenceContext());
							dao.remove(frame.getListaNovosParticipantes().getParticipanteSelected());
						} else {
							JOptionPane.showMessageDialog(frame, MSG_EXCLUSAO_REGISTRO, TITLE_ERROR, JOptionPane.ERROR_MESSAGE);
							return;
						}
					}

					@Override
					public void posAction() {
						cleanUp();
						refreshTables();
						JOptionPane.showMessageDialog(frame, MSG_SUCESSO, TITLE_INFORMACAO, JOptionPane.INFORMATION_MESSAGE);
						fireEvent(new PesquisarNovosParticipantesEvent(listaParticipantesNovos));
					}
				}));

		refreshTables();
		carregarComboOrgao();
	}

	private void refreshTables() {
		ParticipanteDAO dao = new ParticipanteDAOImpl(getPersistenceContext());

		listaParticipanteInscritos = dao.pesquisarParticipantesInscritos();
		frame.refreshTableInscritos(listaParticipanteInscritos);
		frame.getLabelParticipantesInscritos().setText("Participantes Inscritos:  " + listaParticipanteInscritos.size());

		listaParticipantesNovos = dao.pesquisarParticipantesNovos();
		frame.refreshTableNovosParticipantes(listaParticipantesNovos);
		frame.getLabelNovosParticipantes().setText("Novos Participantes:  " + listaParticipantesNovos.size());
	}

	private void carregarComboOrgao() {
		OrgaoDAO orgaoDao = new OrgaoDAOImpl(getPersistenceContext());
		frame.setListaOrgao(orgaoDao.getAll());
	}

	public void loadPersistenceContext() {
		loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
	}

	@Override
	protected void cleanUp() {
		frame.resetAbaInscricao();
		super.cleanUp();
	}

}
