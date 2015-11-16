package br.com.frequencia.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import br.com.frequencia.action.AbstractAction;
import br.com.frequencia.dao.EventoDAO;
import br.com.frequencia.dao.FrequenciaDAO;
import br.com.frequencia.dao.HorarioDAO;
import br.com.frequencia.dao.ParticipanteDAO;
import br.com.frequencia.dao.ParticipanteInscritoDAO;
import br.com.frequencia.dao.SalaDAO;
import br.com.frequencia.dao.TurmaDAO;
import br.com.frequencia.dao.impl.EventoDAOImpl;
import br.com.frequencia.dao.impl.FrequenciaDAOImpl;
import br.com.frequencia.dao.impl.HorarioDAOImpl;
import br.com.frequencia.dao.impl.ParticipanteDAOImpl;
import br.com.frequencia.dao.impl.ParticipanteInscritoDAOImpl;
import br.com.frequencia.dao.impl.SalaDAOImpl;
import br.com.frequencia.dao.impl.TurmaDAOImpl;
import br.com.frequencia.event.AbstractEventListener;
import br.com.frequencia.event.CredenciarParticipanteEvent;
import br.com.frequencia.event.EditarParticipanteiInscritoEvent;
import br.com.frequencia.event.IncluirParticipanteEvent;
import br.com.frequencia.event.PesquisarNovosParticipantesEvent;
import br.com.frequencia.event.PesquisarParticipanteInscritoEvent;
import br.com.frequencia.model.Credenciamento;
import br.com.frequencia.model.Evento;
import br.com.frequencia.model.Frequencia;
import br.com.frequencia.model.Horario;
import br.com.frequencia.model.Participante;
import br.com.frequencia.model.ParticipanteInscrito;
import br.com.frequencia.model.Sala;
import br.com.frequencia.model.Turma;
import br.com.frequencia.ui.combo.ComboBoxItemModel;
import br.com.frequencia.ui.frame.PrincipalFrame;
import br.com.frequencia.util.DateUtil;
import br.com.frequencia.util.JPAUtil;

public class PrincipalController extends PersistenceController {

	private PrincipalFrame frame;
	private TurmaController turmaController;
	private CadastramentoController cadastramentoController;
	private IncluirNovoParticipanteController incluirNovoParticipanteController;
	private EditarParticipanteInscritosController editarParticipanteInscritosController;
	private CredenciamentoController credenciamentoController;
	private FrequenciaController frequenciaController;
	private ImportarExportarController importarExportarController;
	private RelatoriosController relatoriosController;
	private EspelhoFrequenciaController espelhoFrequenciaController;

	public PrincipalController() {
		loadPersistenceContext();
		frame = new PrincipalFrame();
		frame.addWindowListener(this);
		frame.setVisible(true);

		turmaController = new TurmaController(this, frame);
		cadastramentoController = new CadastramentoController(this, frame);
		incluirNovoParticipanteController = new IncluirNovoParticipanteController(this, frame);
		editarParticipanteInscritosController = new EditarParticipanteInscritosController(this);
		credenciamentoController = new CredenciamentoController(this, frame);
		frequenciaController = new FrequenciaController(this, frame);
		importarExportarController = new ImportarExportarController(this, frame);
		relatoriosController = new RelatoriosController(this, frame);
		espelhoFrequenciaController = new EspelhoFrequenciaController(this, frame);

		definirAcoesEventosAbaFrequencia();
		definirAcoesEventosAbaTurma();
		definirAcoesEventosAbaCadastramento();
		definirAcoesEventosAbaCredenciamento();
		definirAcoesEventosAbaImportacaoExportacao();
		definirAcoesEventosAbaRelatorios();
		definirAcoesEventosAbaEspelhoFrequencia();

	}


	private void definirAcoesEventosAbaEspelhoFrequencia() {
		registerAction(frame.getButtonGerarRelCredenciamento(), new AbstractAction() {
			public void action() {
				espelhoFrequenciaController.loadPersistenceContext();
			}
		});
	}


	private void definirAcoesEventosAbaRelatorios() {
		registerAction(frame.getButtonGerarRelCredenciamento(), new AbstractAction() {
			public void action() {
				relatoriosController.loadPersistenceContext();
			}
		});
	}

	private void definirAcoesEventosAbaImportacaoExportacao() {
		registerAction(frame.getButtonImportarCSV_Turma_CFC(), new AbstractAction() {
			public void action() {
				importarExportarController.loadPersistenceContext();
			}
		});

		registerAction(frame.getButtonImportarCSV_Inscrito_CFC(), new AbstractAction() {
			public void action() {
				importarExportarController.loadPersistenceContext();
			}
		});

		registerAction(frame.getButtonImportar(), new AbstractAction() {
			public void action() {
				importarExportarController.loadPersistenceContext();
			}
		});
		
		registerAction(frame.getButtonExportar(), new AbstractAction() {
			public void action() {
				importarExportarController.loadPersistenceContext();
			}
		});
		
		registerAction(frame.getButtonImportarFrequencia(), new AbstractAction() {
			public void action() {
				importarExportarController.loadPersistenceContext();
			}
		});
	}

	private void definirAcoesEventosAbaFrequencia() {
		registerAction(frame.getButtonRegistrarFrequencia(), new AbstractAction() {
			public void action() {
				frequenciaController.loadPersistenceContext();
			}
		});

		frame.getComboBoxTurmaFrequencia().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				frame.getTextFieldInscricaoFrequencia().setText("");
				frame.getTextFieldNomeFrequencia().setText("");
				if (frame.getComboBoxOrgaoFrequencia().getItemCount() > 0)
					frame.getComboBoxOrgaoFrequencia().setSelectedIndex(0);
				Integer IdComboBoxTurmaFrequencia = ((ComboBoxItemModel) frame.getComboBoxTurmaFrequencia().getSelectedItem()).getId();

				if (IdComboBoxTurmaFrequencia == 0) {
					frame.getTextFieldInscricaoFrequencia().setEnabled(Boolean.FALSE);
					frame.getTextFieldNomeFrequencia().setEnabled(Boolean.FALSE);
					frame.getComboBoxOrgaoFrequencia().setEnabled(Boolean.FALSE);
					frame.getPanelInformacoesTurma().setVisible(Boolean.FALSE);

					frame.refreshTableFrequencias(new ArrayList<Frequencia>());
					frame.getLabelListaParticipantesFrequencia().setVisible(Boolean.FALSE);
					frame.getLabelVagas().setVisible(Boolean.FALSE);
				} else {
					frame.getTextFieldInscricaoFrequencia().setEnabled(Boolean.TRUE);
					frame.getTextFieldNomeFrequencia().setEnabled(Boolean.TRUE);
					frame.getComboBoxOrgaoFrequencia().setEnabled(Boolean.TRUE);
					frame.getPanelInformacoesTurma().setVisible(Boolean.TRUE);

					TurmaDAO turmaDAO = new TurmaDAOImpl(getPersistenceContext());
					Turma turma = turmaDAO.findById(IdComboBoxTurmaFrequencia);
					if (turma != null) {
						frame.getLabelConteudoCapacidade().setText(turma.getSala().getNumCapacidade().toString());
						frame.getLabelConteudoHorario().setText(turma.getHorario().getDescHorario());
						frame.getLabelConteudoNomeTurma().setText(turma.getNomTurma());
						frame.getLabelConteudoConteudo().setText(turma.getNomDisciplina());
						frame.getLabelConteudoNomeSala().setText(turma.getSala().getNomSala());
	
						if (null != turma.getInstrutor())
							frame.getLabelConteudoInstrutor().setText(turma.getInstrutor().getNomInstrutor());
	
						/**
						 * @TODO Futuramente, substituir por "select count(*)" já trazendo a quantidade do banco, ficará bem mais rápido do que
						 *       trazer toda a lista e pegar o "size". Apesar de que o BD é pequeno.
						 * 
						 */
						ParticipanteInscritoDAO participanteInscritoDAO = new ParticipanteInscritoDAOImpl(getPersistenceContext());
						List<ParticipanteInscrito> listaParticipantesInscritos = participanteInscritoDAO
								.pesquisarParticipanteInscritosPorTurma(turma.getId());
						if (listaParticipantesInscritos != null) {
							frame.getLabelConteudoInscritosTurmaFrequencia()
									.setText(new Integer(listaParticipantesInscritos.size()).toString());
						} else {
							frame.getLabelConteudoInscritosTurmaFrequencia().setText("0");
						}
	
						frame.getTextFieldInscricaoFrequencia().grabFocus();
						
						/** INTERVALO RETIRADO A PEDIDO DO CLIENTE
						frame.getButtonIntervaloTurma().setVisible(false);
						frame.getButtonFimIntervaloTurma().setVisible(false);
						if (turma.getInicioIntervalo() == null) {
							frame.getButtonIntervaloTurma().setVisible(true);
						} else if (turma.getFimIntervalo() == null) {
							frame.getButtonFimIntervaloTurma().setVisible(true);
						}
						*/
	
						List<Frequencia> list = new ArrayList<Frequencia>();

						FrequenciaDAO frequenciaDAO = new FrequenciaDAOImpl(getPersistenceContext());
						list = frequenciaDAO.pesquisarFrequencias(turma.getId());
						if (list != null) {
							for (Frequencia frequencia : list) {
								turmaDAO.findById(frequencia.getTurma().getId());
							}
							frame.refreshTableFrequencias(list);
							frame.getLabelListaParticipantesFrequencia().setVisible(Boolean.TRUE);
							frame.getLabelVagas().setVisible(Boolean.TRUE);
							frame.getLabelListaParticipantesFrequencia().setText(
									"Participantes com registro de frequência:  " + list.size());
							List<ParticipanteInscrito> listaParticipanteInscritosTurma = participanteInscritoDAO.pesquisarParticipanteInscritosPorTurma(turma.getId());
							Integer quantidadeVagas = turma.getSala().getNumCapacidade() - listaParticipanteInscritosTurma.size(); 
							Integer vagasRestantes = 0;
							if (listaParticipanteInscritosTurma != null){
								quantidadeVagas = turma.getSala().getNumCapacidade() - listaParticipanteInscritosTurma.size();
								vagasRestantes = list.size() - listaParticipanteInscritosTurma.size();
								if (vagasRestantes > 0){
									quantidadeVagas -= vagasRestantes;
								}
							}
							frame.getLabelVagas().setText("Vagas não preenchidas:  " + quantidadeVagas);
							frame.getTextFieldInscricaoFrequencia().grabFocus();
						} else {
							frame.refreshTableFrequencias(new ArrayList<Frequencia>());
							frame.getLabelListaParticipantesFrequencia().setVisible(Boolean.FALSE);
							frame.getLabelVagas().setVisible(Boolean.FALSE);
						}
					}
				}
			}
		});

		frame.getListaFrequencia().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				if (event.getClickCount() == 2) {
					Frequencia frequencia = frame.getListaFrequencia().getFrequenciaSelected();
					if (frequencia != null) {
						frame.getComboBoxTurmaFrequencia().setSelectedIndex(frequencia.getTurma().getId());
						frame.getTextFieldInscricaoFrequencia().setText(frequencia.getParticipante().getNumInscricao());
						frame.getTextFieldNomeFrequencia().setText(frequencia.getParticipante().getNomParticipante());
						frame.getComboBoxOrgaoFrequencia().setSelectedIndex(frequencia.getParticipante().getOrgao().getId());
					}
				}
			}
		});
	}

	/**
	 * Ações e eventos da Aba Credenciamento
	 */
	private void definirAcoesEventosAbaCredenciamento() {
		registerAction(frame.getjMenuItemExcluirCredenciamento(), new AbstractAction() {
			public void action() {
				credenciamentoController.loadPersistenceContext();
			}
		});

		registerAction(frame.getButtonRegistrarCredenciamento(), new AbstractAction() {
			public void action() {
				credenciamentoController.loadPersistenceContext();
			}
		});

		registerEventListener(CredenciarParticipanteEvent.class, new AbstractEventListener<CredenciarParticipanteEvent>() {
			public void handleEvent(final CredenciarParticipanteEvent event) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						List<Credenciamento> list = event.getTarget();
						if (list != null) {
							frame.refreshTableCredenciamento(event.getTarget());
						}
					}
				});
			}
		});

		frame.getListaCredenciamento().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					int col = frame.getListaCredenciamento().columnAtPoint(e.getPoint());
					int row = frame.getListaCredenciamento().rowAtPoint(e.getPoint());
					if (col != -1 && row != -1) {
						frame.getListaCredenciamento().setColumnSelectionInterval(col, col);
						frame.getListaCredenciamento().setRowSelectionInterval(row, row);
					}
					frame.getjPopupMenuListaCredenciamento().show(frame.getListaCredenciamento(), e.getX(), e.getY());
				}
			}
		});
	}

	/**
	 * Ações e eventos da Aba Cadastramento
	 */
	private void definirAcoesEventosAbaCadastramento() {
		registerAction(frame.getjMenuItemExcluirNovoParticipante(), new AbstractAction() {
			public void action() {
				cadastramentoController.loadPersistenceContext();
			}
		});

		registerAction(frame.getjMenuItemEditarNovoParticipante(), new AbstractAction() {
			public void action() {
				Participante participante = frame.getListaNovosParticipantes().getParticipanteSelected();
				if (participante != null) {
					incluirNovoParticipanteController.show(participante);
				}
			}
		});

		registerAction(frame.getjMenuItemEditarParticipantesInscritos(), new AbstractAction() {
			public void action() {
				Participante participante = frame.getListaParticipanteInscrito().getParticipanteInscritoSelected();
				if (participante != null) {
					editarParticipanteInscritosController.show(participante);
				}
			}
		});

		registerAction(frame.getButtonPesquisarInscritos(), new AbstractAction() {
			public void action() {
				cadastramentoController.loadPersistenceContext();
			}
		});

		registerAction(frame.getButtonPesquisarNovosParticipantes(), new AbstractAction() {
			public void action() {
				cadastramentoController.loadPersistenceContext();
			}
		});

		registerAction(frame.getButtonGerarArquivo(), new AbstractAction() {
			public void action() {
				cadastramentoController.loadPersistenceContext();
			}
		});

		registerEventListener(PesquisarParticipanteInscritoEvent.class, new AbstractEventListener<PesquisarParticipanteInscritoEvent>() {
			public void handleEvent(final PesquisarParticipanteInscritoEvent event) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						List<Participante> list = event.getTarget();
						if (list != null) {
							frame.refreshTableInscritos(event.getTarget());
							frame.getLabelParticipantesInscritos().setText("Participantes Inscritos:  " + list.size());
						}
					}
				});
			}
		});

		registerEventListener(PesquisarNovosParticipantesEvent.class, new AbstractEventListener<PesquisarNovosParticipantesEvent>() {
			public void handleEvent(final PesquisarNovosParticipantesEvent event) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						List<Participante> list = event.getTarget();
						if (list != null) {
							frame.refreshTableNovosParticipantes(event.getTarget());
							frame.getLabelNovosParticipantes().setText("Novos Participantes:  " + list.size());
						}
					}
				});
			}
		});

		registerEventListener(IncluirParticipanteEvent.class, new AbstractEventListener<IncluirParticipanteEvent>() {
			public void handleEvent(final IncluirParticipanteEvent event) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						refreshTables();
					}
				});
			}
		});

		registerEventListener(EditarParticipanteiInscritoEvent.class, new AbstractEventListener<EditarParticipanteiInscritoEvent>() {
			public void handleEvent(final EditarParticipanteiInscritoEvent event) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						refreshTables();
					}
				});
			}
		});

		frame.getListaParticipanteInscrito().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				if (event.getClickCount() == 2) {
					Participante participanteInscrito = frame.getListaParticipanteInscrito().getParticipanteInscritoSelected();
					if (participanteInscrito != null) {
						editarParticipanteInscritosController.show(participanteInscrito);
					}
				}
			}
		});

		frame.getListaNovosParticipantes().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				if (event.getClickCount() == 2) {
					Participante participante = frame.getListaNovosParticipantes().getParticipanteSelected();
					if (participante != null) {
						incluirNovoParticipanteController.show(participante);
					}
				}
			}
		});

		frame.getListaNovosParticipantes().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					int col = frame.getListaNovosParticipantes().columnAtPoint(e.getPoint());
					int row = frame.getListaNovosParticipantes().rowAtPoint(e.getPoint());
					if (col != -1 && row != -1) {
						frame.getListaNovosParticipantes().setColumnSelectionInterval(col, col);
						frame.getListaNovosParticipantes().setRowSelectionInterval(row, row);
					}
					frame.getjPopupMenuListaNovosParticipantes().show(frame.getListaNovosParticipantes(), e.getX(), e.getY());
				}
			}
		});

		frame.getListaParticipanteInscrito().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					int col = frame.getListaParticipanteInscrito().columnAtPoint(e.getPoint());
					int row = frame.getListaParticipanteInscrito().rowAtPoint(e.getPoint());
					if (col != -1 && row != -1) {
						frame.getListaParticipanteInscrito().setColumnSelectionInterval(col, col);
						frame.getListaParticipanteInscrito().setRowSelectionInterval(row, row);
					}
					frame.getjPopupMenuListaParticipantesInscritos().show(frame.getListaParticipanteInscrito(), e.getX(), e.getY());
				}
			}
		});
	}

	/**
	 * Ações e Eventos da Aba Turma
	 */
	private void definirAcoesEventosAbaTurma() {
		// registerAction(frame.getjMenuItemExcluirTurma(), new AbstractAction() {
		// public void action() {
		// turmaController.loadPersistenceContext();
		// }
		// });

		registerAction(frame.getjMenuItemEditarTurma(), new AbstractAction() {
			public void action() {
				Turma turma = frame.getListaTurma().getTurmaSelected();
				if (turma != null) {
					turmaController.show(turma);
				}
			}
		});

		registerAction(frame.getButtonSalvarTurma(), new AbstractAction() {
			public void action() {
				turmaController.loadPersistenceContext();
			}
		});

		frame.getComboBoxEventoTurma().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				frame.getLabelListaTurma().setText("");
				Integer IdComboBoxEventoTurma = ((ComboBoxItemModel) frame.getComboBoxEventoTurma().getSelectedItem()).getId();
				if (IdComboBoxEventoTurma == 0) {
					frame.getTextFieldCapacidadeSalaTurma().setEnabled(Boolean.FALSE);
					frame.getTextFieldDescricaoHorarioTurma().setEnabled(Boolean.FALSE);
					frame.getTextFieldHorarioFinal().setEnabled(Boolean.FALSE);
					frame.getTextFieldHorarioInicialTurma().setEnabled(Boolean.FALSE);
					frame.getTextFieldNomeSalaTurma().setEnabled(Boolean.FALSE);
					frame.getTextFieldNomeTurma().setEnabled(Boolean.FALSE);
					frame.getComboBoxHorarioTurma().setEnabled(Boolean.FALSE);
					frame.getComboBoxSalaTurma().setEnabled(Boolean.FALSE);
					frame.getComboBoxInstrutorTurma().setEnabled(Boolean.FALSE);
					frame.getCheckBoxSalaAtivoTurma().setEnabled(Boolean.FALSE);
					frame.getTextFieldNomeDisciplina().setEnabled(Boolean.FALSE);
				} else {
					EventoDAO eventoDAO = new EventoDAOImpl(getPersistenceContext());
					Evento evento = eventoDAO.findById(IdComboBoxEventoTurma);
					TurmaDAO turmaDAO = new TurmaDAOImpl(getPersistenceContext());
					frame.refreshTableTurmas(turmaDAO.pesquisarTurmas(evento.getId()));
					habilitarCamposTurma();
				}
			}
		});

		frame.getComboBoxHorarioTurma().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Integer IdComboBoxHorarioTurma = ((ComboBoxItemModel) frame.getComboBoxHorarioTurma().getSelectedItem()).getId();
				if (IdComboBoxHorarioTurma == 0) {
					frame.getTextFieldDescricaoHorarioTurma().setText("");
					frame.getTextFieldHorarioFinal().setText("");
					frame.getTextFieldHorarioInicialTurma().setText("");
				} else {
					HorarioDAO horarioDAO = new HorarioDAOImpl(getPersistenceContext());
					Horario horario = horarioDAO.findById(IdComboBoxHorarioTurma);
					frame.getTextFieldDescricaoHorarioTurma().setText(horario.getDescHorario());
					frame.getTextFieldHorarioFinal().setText(DateUtil.getDataHora(horario.getHrFinal().getTime(), FORMATO_HORA_BANCO));
					frame.getTextFieldHorarioInicialTurma().setText(
							DateUtil.getDataHora(horario.getHrInicial().getTime(), FORMATO_HORA_BANCO));
				}
			}
		});

		frame.getComboBoxSalaTurma().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Integer IdComboBoxSalaTurma = ((ComboBoxItemModel) frame.getComboBoxSalaTurma().getSelectedItem()).getId();
				if (IdComboBoxSalaTurma == 0) {
					frame.getTextFieldCapacidadeSalaTurma().setText("");
					frame.getTextFieldNomeSalaTurma().setText("");
					frame.getCheckBoxSalaAtivoTurma().setSelected(Boolean.FALSE);
				} else {
					SalaDAO salaDAO = new SalaDAOImpl(getPersistenceContext());
					Sala sala = salaDAO.findById(IdComboBoxSalaTurma);
					frame.getTextFieldCapacidadeSalaTurma().setText(sala.getNumCapacidade().toString());
					frame.getTextFieldNomeSalaTurma().setText(sala.getNomSala());
					frame.getCheckBoxSalaAtivoTurma().setSelected(sala.getFlgAtivo());
				}
			}
		});

		frame.getListaTurma().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				if (event.getClickCount() == 2) {
					Turma turma = frame.getListaTurma().getTurmaSelected();
					if (turma != null) {
						habilitarCamposTurma();
						turmaController.show(turma);
					}
				}
			}
		});

		frame.getListaTurma().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					int col = frame.getListaTurma().columnAtPoint(e.getPoint());
					int row = frame.getListaTurma().rowAtPoint(e.getPoint());
					if (col != -1 && row != -1) {
						frame.getListaTurma().setColumnSelectionInterval(col, col);
						frame.getListaTurma().setRowSelectionInterval(row, row);
					}
					frame.getjPopupMenuListaTurmas().show(frame.getListaTurma(), e.getX(), e.getY());
				}
			}
		});
	}

	private void habilitarCamposTurma() {
		frame.getTextFieldCapacidadeSalaTurma().setEnabled(Boolean.TRUE);
		frame.getTextFieldDescricaoHorarioTurma().setEnabled(Boolean.TRUE);
		frame.getTextFieldHorarioFinal().setEnabled(Boolean.TRUE);
		frame.getTextFieldHorarioInicialTurma().setEnabled(Boolean.TRUE);
		frame.getTextFieldNomeSalaTurma().setEnabled(Boolean.TRUE);
		frame.getTextFieldNomeTurma().setEnabled(Boolean.TRUE);
		frame.getComboBoxHorarioTurma().setEnabled(Boolean.TRUE);
		frame.getComboBoxSalaTurma().setEnabled(Boolean.TRUE);
		frame.getComboBoxInstrutorTurma().setEnabled(Boolean.TRUE);
		frame.getCheckBoxSalaAtivoTurma().setEnabled(Boolean.TRUE);
		frame.getTextFieldNomeDisciplina().setEnabled(Boolean.TRUE);
	}

	private void refreshTables() {
		ParticipanteDAO participanteDAO = new ParticipanteDAOImpl(getPersistenceContext());
		List<Participante> listaParticipanteInscritos = participanteDAO.pesquisarParticipantesInscritos();
		frame.refreshTableInscritos(listaParticipanteInscritos);
		frame.getLabelParticipantesInscritos().setText("Participantes Inscritos:  " + listaParticipanteInscritos.size());

		List<Participante> listaParticipantes = participanteDAO.pesquisarParticipantesNovos();
		frame.refreshTableNovosParticipantes(listaParticipantes);
		frame.getLabelNovosParticipantes().setText("Novos Participantes:  " + listaParticipantes.size());
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();
		JPAUtil.closeEntityManagerFactory();
	}


}
