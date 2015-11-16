package br.com.frequencia.controller;

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
import br.com.frequencia.event.IncluirParticipanteEvent;
import br.com.frequencia.model.Credenciamento;
import br.com.frequencia.model.Evento;
import br.com.frequencia.model.Participante;
import br.com.frequencia.ui.frame.IncluirParticipanteFrame;
import br.com.frequencia.ui.frame.PrincipalFrame;
import br.com.frequencia.util.GeradorHashcode;
import br.com.frequencia.validation.ParticipanteValidator;
import br.com.frequencia.validation.Validator;

/**
 * Define a <code>Controller</code> responsável por manipular a inclusão/edição dos Participantes.
 * 
 * @see br.com.frequencia.controller.PersistenceController
 * 
 */
public class IncluirNovoParticipanteController extends PersistenceController {

	private IncluirParticipanteFrame frame;
	private Validator<Participante> validador = new ParticipanteValidator();
	private PrincipalFrame framePrincipal;
	private CredenciamentoDAO credenciamentoDAO;

	public IncluirNovoParticipanteController(AbstractController parent, final PrincipalFrame framePrincipal) {
		super(parent);
		// loadPersistenceContext();
		this.frame = new IncluirParticipanteFrame();
		this.framePrincipal = framePrincipal;
		frame.addWindowListener(this);

		registerAction(frame.getButtonLimpar(), new AbstractAction() {
			public void action() {
				cleanUp();
			}
		});

		registerAction(frame.getButtonSalvarNovoParticipante(), ConditionalAction.build().addConditional(new BooleanExpression() {

			public boolean conditional() {
				Participante participante = frame.getParticipante();
				String msg = validador.validate(participante);
				if (!"".equals(msg == null ? "" : msg)) {
					JOptionPane.showMessageDialog(frame, msg, "Validação", JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
				return true;
			}
		}).addAction(TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {
			private Participante participante;

			@Override
			protected void action() {
				participante = frame.getParticipante();
				OrgaoDAO orgaoDAO = new OrgaoDAOImpl(getPersistenceContext());
				if (participante.getOrgao() != null && participante.getOrgao().getId() != null) {
					participante.setOrgao(orgaoDAO.findById(participante.getOrgao().getId()));
				}
				ParticipanteDAO participanteDAO = new ParticipanteDAOImpl(getPersistenceContext());

				/**
				 * Verifica se já exite o numero de inscrição para outro participante, caso exita ele gera outro. somente em caso de
				 * candidato novo.
				 */
				if (participante.getId() == null) {
					participante.setNumInscricao(GeradorHashcode.gerarHashcode());
					boolean exite = participanteDAO.pesquisarNumInscricao(participante.getNumInscricao());
					while (exite) {
						participante.setNumInscricao(GeradorHashcode.gerarHashcode());
					}
				}

				participanteDAO.save(participante);
				credenciamentoDAO = new CredenciamentoDAOImpl(getPersistenceContext());
				Credenciamento credenciamento = credenciamentoDAO.recuperarCredenciamento(participante.getId());
				credenciamentoDAO.save(getCredenciamento(participante, credenciamento));
				refreshTableCredenciados();
			}

			private Credenciamento getCredenciamento(Participante participante, Credenciamento credenciamento) {
				if (credenciamento == null) {
					credenciamento = new Credenciamento();
				}
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

			public void posAction() {
				cleanUp();
				fireEvent(new IncluirParticipanteEvent(participante));
				JOptionPane.showMessageDialog(frame, MSG_SUCESSO, "Informação", JOptionPane.INFORMATION_MESSAGE);
				frame.setVisible(false);
			}
		})));
	}

	private void refreshTableCredenciados() {
		credenciamentoDAO = new CredenciamentoDAOImpl(getPersistenceContext());
		List<Credenciamento> credenciados = credenciamentoDAO.getAll();
		ParticipanteDAO participanteDAO = new ParticipanteDAOImpl(getPersistenceContext());
		OrgaoDAO orgaoDAO = new OrgaoDAOImpl(getPersistenceContext());
		EventoDAO eventoDAO = new EventoDAOImpl(getPersistenceContext());
		for (Credenciamento credenciamento : credenciados) {
			participanteDAO.findById(credenciamento.getParticipante().getId());
			if (credenciamento.getParticipante() != null && credenciamento.getParticipante().getOrgao() != null
					&& credenciamento.getParticipante().getOrgao().getId() != null) {
				orgaoDAO.findById(credenciamento.getParticipante().getOrgao().getId());
			}
			eventoDAO.findById(credenciamento.getEvento().getId());
		}
		framePrincipal.getLabelListaCredenciados().setText("Participantes Credenciados:  " + credenciados.size());
		framePrincipal.refreshTableCredenciamento(credenciados);
	}

	public void show() {
		try {
			loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
			carregarCombos();
			frame.setTitle("Incluir Novo Participante");
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void carregarCombos() {
		if (frame.getComboBoxOrgao().getItemCount() == 0) {
			OrgaoDAO orgaoDao = new OrgaoDAOImpl(getPersistenceContext());
			frame.setListaOrgao(orgaoDao.getAll());
		}
	}

	public void show(Participante participante) {
		loadPersistenceContext();
		OrgaoDAO orgaoDAO = new OrgaoDAOImpl(getPersistenceContext());
		participante.setOrgao(orgaoDAO.findById(participante.getOrgao().getId()));
		carregarCombos();
		frame.setParticipante(participante);
		frame.setTitle("Editar Participante");
		frame.setVisible(true);
	}

	@Override
	protected void cleanUp() {
		frame.resetForm();
		super.cleanUp();
	}

}
