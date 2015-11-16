package br.com.frequencia.controller;

import java.util.List;

import javax.swing.JOptionPane;

import br.com.frequencia.action.AbstractAction;
import br.com.frequencia.action.BooleanExpression;
import br.com.frequencia.action.ConditionalAction;
import br.com.frequencia.action.TransactionalAction;
import br.com.frequencia.dao.EventoDAO;
import br.com.frequencia.dao.HorarioDAO;
import br.com.frequencia.dao.InstrutorDAO;
import br.com.frequencia.dao.SalaDAO;
import br.com.frequencia.dao.TurmaDAO;
import br.com.frequencia.dao.impl.EventoDAOImpl;
import br.com.frequencia.dao.impl.HorarioDAOImpl;
import br.com.frequencia.dao.impl.InstrutorDAOImpl;
import br.com.frequencia.dao.impl.SalaDAOImpl;
import br.com.frequencia.dao.impl.TurmaDAOImpl;
import br.com.frequencia.event.IncluirTurmaEvent;
import br.com.frequencia.model.Turma;
import br.com.frequencia.ui.frame.PrincipalFrame;
import br.com.frequencia.validation.TurmaValidator;
import br.com.frequencia.validation.Validator;

/**
 * Define a <code>Controller</code> responsável por manipular a aba de <code>Turma</code>.
 * 
 * @see br.com.frequencia.controller.PersistenceController
 * 
 */
public class TurmaController extends PersistenceController {

	private PrincipalFrame frame;
	private Validator<Turma> validador = new TurmaValidator();
	private List<Turma> listaTurma;

	public TurmaController(AbstractController parent, final PrincipalFrame frame) {
		super(parent);
		this.frame = frame;
		loadPersistenceContext();
		frame.addWindowListener(this);

		registerAction(frame.getButtonLimparTurma(), new AbstractAction() {
			public void action() {
				cleanUp();
			}
		});

		registerAction(frame.getButtonSalvarTurma(), ConditionalAction.build().addConditional(new BooleanExpression() {

			public boolean conditional() {
				if (frame.getComboBoxEventoTurma().getSelectedIndex() == 0){
					JOptionPane.showMessageDialog(frame, "É necessário Selecionar um Evento antes de continuar!", TITLE_VALIDACAO, JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
				if (frame.getComboBoxHorarioTurma().getSelectedIndex() == 0){
					JOptionPane.showMessageDialog(frame, "É necessário Selecionar um Horário antes de continuar!", TITLE_VALIDACAO, JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
				if (frame.getComboBoxSalaTurma().getSelectedIndex() == 0){
					JOptionPane.showMessageDialog(frame, "É necessário Selecionar uma Sala antes de continuar!", TITLE_VALIDACAO, JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
				if (frame.getComboBoxInstrutorTurma().getSelectedIndex() == 0){
					JOptionPane.showMessageDialog(frame, "É necessário Selecionar um Instrutor antes de continuar!", TITLE_VALIDACAO, JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
				Turma turma = frame.getTurma();
				String msg = validador.validate(turma);
				if (!"".equals(msg == null ? "" : msg)) {
					JOptionPane.showMessageDialog(frame, msg, TITLE_VALIDACAO, JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
				return true;
			}
		}).addAction(TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {
			private Turma turma;

			@Override
			protected void action() {
				turma = frame.getTurma();
				HorarioDAO horarioDAO = new HorarioDAOImpl(getPersistenceContext());
				horarioDAO.save(turma.getHorario());
				SalaDAO salaDAO = new SalaDAOImpl(getPersistenceContext());
				salaDAO.save(turma.getSala());
				TurmaDAO dao = new TurmaDAOImpl(getPersistenceContext());
				dao.save(turma);
				EventoDAO eventoDAO = new EventoDAOImpl(getPersistenceContext());
				turma.setEvento(eventoDAO.findById(turma.getEvento().getId()));
				InstrutorDAO instrutorDAO = new InstrutorDAOImpl(getPersistenceContext());
				turma.setInstrutor(instrutorDAO.findById(turma.getInstrutor().getId()));
			}

			public void posAction() {
				cleanUp();
				fireEvent(new IncluirTurmaEvent(turma));
				JOptionPane.showMessageDialog(frame, MSG_SUCESSO, TITLE_INFORMACAO, JOptionPane.INFORMATION_MESSAGE);
				refreshTable();
			}
		})));

		registerAction(frame.getjMenuItemExcluirTurma(),
				TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {

					@Override
					protected void action() {
						if (frame.getListaTurma().getTurmaSelected() != null) {
							TurmaDAO dao = new TurmaDAOImpl(getPersistenceContext());
							dao.remove(frame.getListaTurma().getTurmaSelected());
						} else {
							JOptionPane.showMessageDialog(frame, MSG_EXCLUSAO_REGISTRO, TITLE_ERROR, JOptionPane.ERROR_MESSAGE);
							return;
						}
					}

					@Override
					public void posAction() {
						cleanUp();
						refreshTable();
						JOptionPane.showMessageDialog(frame, MSG_SUCESSO, TITLE_INFORMACAO, JOptionPane.INFORMATION_MESSAGE);
					}
				}));

		carregarCombos();
		refreshTable();
	}

	private void carregarCombos() {
		if (frame.getComboBoxEventoTurma().getItemCount() == 0) {
			EventoDAO eventoDAO = new EventoDAOImpl(getPersistenceContext());
			frame.setListaEvento(eventoDAO.getAll());
		}
		if (frame.getComboBoxHorarioTurma().getItemCount() == 0) {
			HorarioDAO horarioDAO = new HorarioDAOImpl(getPersistenceContext());
			frame.setListaHorario(horarioDAO.getAll());
		}
		if (frame.getComboBoxSalaTurma().getItemCount() == 0) {
			SalaDAO salaDAO = new SalaDAOImpl(getPersistenceContext());
			frame.setListaSala(salaDAO.getAll());
		}
		if (frame.getComboBoxInstrutorTurma().getItemCount() == 0) {
			InstrutorDAO instrutorDAO = new InstrutorDAOImpl(getPersistenceContext());
			frame.setListaInstrutor(instrutorDAO.getAll());
		}
	}

	private void refreshTable() {
		TurmaDAO turmaDAO = new TurmaDAOImpl(getPersistenceContext());
		listaTurma = turmaDAO.getAll();
		frame.getLabelListaTurma().setText("Lista de Turmas:  " + listaTurma.size());
		frame.refreshTableTurmas(listaTurma);
	}

	public void loadPersistenceContext() {
		loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
	}

	public void show(Turma turma) {
		frame.setTurma(turma);
		loadPersistenceContext();
	}

	@Override
	protected void cleanUp() {
		frame.resetAbaTurma();
		super.cleanUp();
	}
}
