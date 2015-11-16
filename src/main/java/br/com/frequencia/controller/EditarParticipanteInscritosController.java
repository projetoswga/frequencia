package br.com.frequencia.controller;

import javax.swing.JOptionPane;

import br.com.frequencia.action.AbstractAction;
import br.com.frequencia.action.BooleanExpression;
import br.com.frequencia.action.ConditionalAction;
import br.com.frequencia.action.TransactionalAction;
import br.com.frequencia.dao.OrgaoDAO;
import br.com.frequencia.dao.ParticipanteDAO;
import br.com.frequencia.dao.impl.OrgaoDAOImpl;
import br.com.frequencia.dao.impl.ParticipanteDAOImpl;
import br.com.frequencia.event.EditarParticipanteiInscritoEvent;
import br.com.frequencia.model.Participante;
import br.com.frequencia.ui.frame.EditarParticipanteInscristoFrame;
import br.com.frequencia.validation.ParticipanteValidator;
import br.com.frequencia.validation.Validator;

/**
 * Define a <code>Controller</code> responsável por manipular a inclusão/edição dos Participantes.
 * 
 * @see br.com.frequencia.controller.PersistenceController
 * 
 */
public class EditarParticipanteInscritosController extends PersistenceController {

	private EditarParticipanteInscristoFrame frame;
	private Validator<Participante> validador = new ParticipanteValidator();

	public EditarParticipanteInscritosController(AbstractController parent) {
		super(parent);
		this.frame = new EditarParticipanteInscristoFrame();
		frame.addWindowListener(this);

		registerAction(frame.getButtonLimpar(), new AbstractAction() {
			public void action() {
				cleanUp();
			}
		});

		registerAction(frame.getButtonEditarParticipanteInscritos(), ConditionalAction.build().addConditional(new BooleanExpression() {

			public boolean conditional() {
				Participante participanteInscrito = frame.getParticipanteInscrito();
				String msg = validador.validate(participanteInscrito);
				if (!"".equals(msg == null ? "" : msg)) {
					JOptionPane.showMessageDialog(frame, msg, "Validação", JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
				return true;
			}
		}).addAction(TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {
			private Participante participanteInscrito;

			@Override
			protected void action() {
				participanteInscrito = frame.getParticipanteInscrito();
				OrgaoDAO orgaoDAO = new OrgaoDAOImpl(getPersistenceContext());
				if (null != participanteInscrito.getOrgao())
					participanteInscrito.setOrgao(orgaoDAO.findById(participanteInscrito.getOrgao().getId()));
				ParticipanteDAO participanteDAO = new ParticipanteDAOImpl(getPersistenceContext());
				participanteDAO.save(participanteInscrito);
			}

			public void posAction() {
				cleanUp();
				fireEvent(new EditarParticipanteiInscritoEvent(participanteInscrito));
				JOptionPane.showMessageDialog(frame, MSG_SUCESSO, "Informação", JOptionPane.INFORMATION_MESSAGE);
				frame.setVisible(false);
			}
		})));
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

	public void show(Participante participanteInscrito) {
		loadPersistenceContext();
		OrgaoDAO orgaoDAO = new OrgaoDAOImpl(getPersistenceContext());
		if (participanteInscrito.getOrgao() != null)
			participanteInscrito.setOrgao(orgaoDAO.findById(participanteInscrito.getOrgao().getId()));
		carregarCombos();
		frame.setParticipanteInscrito(participanteInscrito);
		frame.setTitle("Editar Participante");
		frame.setVisible(true);
	}

	@Override
	protected void cleanUp() {
		frame.resetForm();
		super.cleanUp();
	}

}
