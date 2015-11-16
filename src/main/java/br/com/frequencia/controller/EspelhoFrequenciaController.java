package br.com.frequencia.controller;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JOptionPane;

import br.com.frequencia.action.AbstractAction;
import br.com.frequencia.action.BooleanExpression;
import br.com.frequencia.action.ConditionalAction;
import br.com.frequencia.action.TransactionalAction;
import br.com.frequencia.dao.CredenciamentoDAO;
import br.com.frequencia.dao.FrequenciaDAO;
import br.com.frequencia.dao.ParticipanteDAO;
import br.com.frequencia.dao.impl.CredenciamentoDAOImpl;
import br.com.frequencia.dao.impl.EventoDAOImpl;
import br.com.frequencia.dao.impl.FrequenciaDAOImpl;
import br.com.frequencia.dao.impl.ParticipanteDAOImpl;
import br.com.frequencia.model.Evento;
import br.com.frequencia.model.Frequencia;
import br.com.frequencia.model.Participante;
import br.com.frequencia.ui.frame.PrincipalFrame;

public class EspelhoFrequenciaController extends PersistenceController {

	private PrincipalFrame frame;

	private final Integer MINIMO_HORAS_FREQUENCIA_TURNO = 3;

	public EspelhoFrequenciaController(AbstractController parent, final PrincipalFrame frame) {
		super(parent);
		loadPersistenceContext();
		frame.addWindowListener(this);
		this.frame = frame;
		final Clipboard clipboard = frame.getToolkit().getSystemClipboard();

		registerAction(frame.getBtnCopiarExtrato(),
				ConditionalAction.build().addConditional(new BooleanExpression() {

					public boolean conditional() {
						if (frame.getTextAreaEspelhoFrequencia().getText().isEmpty()) {
							JOptionPane.showMessageDialog(frame, "Não existe conteúdo para ser copiado!", "Espelho Frequência",
									JOptionPane.INFORMATION_MESSAGE);
							
							return false;
						}
						return true;
					}
				}).addAction(TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {

					@Override
					protected void action() {
						StringSelection data;
						data = new StringSelection(frame.getTextAreaEspelhoFrequencia().getText());
						clipboard.setContents(data, data);					

						JOptionPane.showMessageDialog(frame, "Espelho da frequência copiado!\nPara uma melhor formatação, utilize a fonte \"courier new\"", "Espelho Frequência",
								JOptionPane.INFORMATION_MESSAGE);

					}

				})));
				
	
		registerAction(frame.getButtonCarregarEspelhoFrequencia(),
				ConditionalAction.build().addConditional(new BooleanExpression() {

					public boolean conditional() {
						if (frame.getTextFieldInscricaoEspelho().getText().isEmpty()) {
							JOptionPane.showMessageDialog(frame, "Informe o número da Inscrição!", "Espelho Frequência",
									JOptionPane.INFORMATION_MESSAGE);
							
							return false;
						}
						return true;
					}
				}).addAction(TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {

					@Override
					protected void action() {
						/*
						 * // Carrega lista de frequencia. List<AlunoDTO> listaFrequencia = populaMapa(); // calcular
						 * Total Faltas calcularTotalFaltas(listaFrequencia); // Monta crossTable List<CrossTab>
						 * listaCross = montarCrossTab(listaFrequencia);
						 */
						try {
							//frame.getWaitPreviewRelatorios().start();

							/*
							 * @TODO SE A BASE POSSUIR MAIS DE UM EVENTO ISTO SERÁ UM BUG. ATUALMENTE SÓ TEM UM EVENTO   
							 */
							Evento evento = new EventoDAOImpl(getPersistenceContext()).getAll().get(0);
							
							DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
							DateFormat hf = new SimpleDateFormat("HH:mm:ss");
							
							clearTextArea();
							appendTextArea(evento.getNomEvento() + " " + df.format(evento.getDataInicio()) + " a " + df.format(evento.getDataFim()) + "\n");

							ParticipanteDAO partDAO = new ParticipanteDAOImpl(getPersistenceContext());

							Participante participante = partDAO.pesquisarParticipantes(frame.getTextFieldInscricaoEspelho().getText());

							if (participante != null) {
								appendTextArea("\nInscrição: " + participante.getNumInscricao());
								appendTextArea("\nNome     : " + participante.getNomParticipante());
								if (null != participante.getOrgao())
									appendTextArea("\nÓrgão    : " + participante.getOrgao().getNomOrgao() + "\n\n");


								// Esta será incrementada em um dia até atingir a data final do evento
								Calendar datFrequencia = new GregorianCalendar();
								datFrequencia.setTimeInMillis(evento.getDataInicio().getTime());

								// Data inicio do evento
								Calendar datInicioEvento = new GregorianCalendar();
								datInicioEvento.setTimeInMillis(evento.getDataInicio().getTime());
								// Data fim do evento
								Calendar datFimEvento = new GregorianCalendar();
								datFimEvento.setTimeInMillis(evento.getDataFim().getTime());

								// Data corrente do evento
								Calendar datCorrente = Calendar.getInstance();

								FrequenciaDAO freqDAO = new FrequenciaDAOImpl(getPersistenceContext());
								CredenciamentoDAO credDAO = new CredenciamentoDAOImpl(getPersistenceContext());
								do {

									Calendar datHorFreqMANHA_INI = new GregorianCalendar(datFrequencia
											.get(Calendar.YEAR), datFrequencia.get(Calendar.MONTH), datFrequencia
											.get(Calendar.DAY_OF_MONTH), 8, 30);
									Calendar datHorFreqMANHA_FIM = new GregorianCalendar(datFrequencia
											.get(Calendar.YEAR), datFrequencia.get(Calendar.MONTH), datFrequencia
											.get(Calendar.DAY_OF_MONTH), 12, 30);
									Calendar datHorFreqTARDE_INI = new GregorianCalendar(datFrequencia
											.get(Calendar.YEAR), datFrequencia.get(Calendar.MONTH), datFrequencia
											.get(Calendar.DAY_OF_MONTH), 14, 00);
									Calendar datHorFreqTARDE_FIM = new GregorianCalendar(datFrequencia
											.get(Calendar.YEAR), datFrequencia.get(Calendar.MONTH), datFrequencia
											.get(Calendar.DAY_OF_MONTH), 18, 00);

									Integer minutosFrequenciaTurno = 0;
									if (datHorFreqMANHA_INI.before(datCorrente)) {
										
										appendTextArea(df.format(datFrequencia.getTime()) + "\n");
										
										List<Frequencia> frequencias = freqDAO.pesquisarFrequenciasParticipanteData(
												participante.getNumInscricao(), datFrequencia);
										if (null == frequencias || frequencias.isEmpty()) {
											appendTextArea("\tMANHÃ\n\tAUSENTE\n");
											appendTextArea("\tTARDE\n\tAUSENTE\n");
										} else {
											/* DESCOBRIR FREQUENCIA */
											// System.out.println("**************ALGUEM");

											/**
											 * Manhã
											 */
											appendTextArea("\tMANHÃ\n\n");
											boolean manha = true;
											for (Frequencia fre : frequencias) {
												int diffInMin = (int) ((fre.getHorarioSaida().getTimeInMillis() - fre
														.getHorarioEntrada().getTimeInMillis()) / (1000 * 60));

												if (manha && !fre.getHorarioEntrada().before(datHorFreqMANHA_FIM)) {
													manha = false;


													// Primeiro dia a frequência da manhã é por meio do credenciamento, se for credenciado tem frequencia
													if (minutosFrequenciaTurno == 0 && datFrequencia.get(Calendar.DAY_OF_MONTH) == datInicioEvento.get(Calendar.DAY_OF_MONTH)) {
														if (null != credDAO.recuperarCredenciamento(participante.getId())) {
															appendTextArea("\tPRESENTE / CREDENCIADO\n\n");
														}
													} else {
														appendResumoTextArea(datCorrente, datHorFreqMANHA_INI,datHorFreqMANHA_FIM, minutosFrequenciaTurno);
													}
													
													minutosFrequenciaTurno = 0;
													appendTextArea("\tTARDE\n");
													//FIM MANHÃ
												}
												
												appendTextArea(String.format("\t\t%-30s: %8s a %8s (%s)\n",fre.getTurma().getNomDisciplina().substring(0, Math.min(30, fre.getTurma().getNomDisciplina().length())),hf.format(fre.getHorarioEntrada().getTime()),hf.format(fre.getHorarioSaida().getTime()),getMinutosEmHora(diffInMin)));
												minutosFrequenciaTurno += diffInMin;
											}

											//Teve frequência só pela manhã nesse dia para o candidato
											if (manha) {
												appendResumoTextArea(datCorrente, datHorFreqMANHA_INI,datHorFreqMANHA_FIM, minutosFrequenciaTurno);
												minutosFrequenciaTurno = 0;
												appendTextArea("\tTARDE\n");
											}
											appendResumoTextArea(datCorrente, datHorFreqTARDE_INI,datHorFreqTARDE_FIM, minutosFrequenciaTurno);
										}
									}

									// Próxima dia do Evento
									datFrequencia.add(Calendar.DAY_OF_MONTH, 1);
								} while (datFrequencia.before(datFimEvento)
										|| datFrequencia.compareTo(datFimEvento) == 0);

							}
							appendTextArea("FIM ESPELHO DE FREQUÊNCIA");


						} finally {
							//frame.getWaitPreviewRelatorios().stop();
						}
					}

					private void appendResumoTextArea(Calendar datCorrente, Calendar datHorFreqMANHA_INI,
							Calendar datHorFreqMANHA_FIM, Integer minutosFrequenciaTurno) {
						if (minutosFrequenciaTurno >= MINIMO_HORAS_FREQUENCIA_TURNO * 60)
							appendTextArea("\tPRESENTE");
						else if (datHorFreqMANHA_INI.before(datCorrente)
								&& datHorFreqMANHA_FIM.after(datCorrente)
								&& minutosFrequenciaTurno > 0)
							appendTextArea("\tPRESENÇA PARCIAL");
						else
							appendTextArea("\tAUSENTE");
						appendTextArea("("+getMinutosEmHora(minutosFrequenciaTurno)+") \n\n");
					}
					
					private String getMinutosEmHora(Integer minutos) {
						if (minutos == 0) 
							return "00:00"; 
				          
						DecimalFormat df = new DecimalFormat("00" );
						
				        int inteira = minutos / 60;  
				          
				        int resto = minutos % 60;  
				          
				        return df.format(inteira) + ":" + df.format(resto);
					}

					private void clearTextArea() {
						frame.getTextAreaEspelhoFrequencia().setText("");
					}

					private void appendTextArea(String text) {
						frame.getTextAreaEspelhoFrequencia().append(text);
					}
				})));

	}

}
