package br.com.frequencia.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRException;
import br.com.frequencia.action.AbstractAction;
import br.com.frequencia.action.TransactionalAction;
import br.com.frequencia.dao.CredenciamentoDAO;
import br.com.frequencia.dao.FrequenciaDAO;
import br.com.frequencia.dao.ParticipanteDAO;
import br.com.frequencia.dao.ParticipanteInscritoDAO;
import br.com.frequencia.dao.TurmaDAO;
import br.com.frequencia.dao.impl.CredenciamentoDAOImpl;
import br.com.frequencia.dao.impl.EventoDAOImpl;
import br.com.frequencia.dao.impl.FrequenciaDAOImpl;
import br.com.frequencia.dao.impl.ParticipanteDAOImpl;
import br.com.frequencia.dao.impl.ParticipanteInscritoDAOImpl;
import br.com.frequencia.dao.impl.TurmaDAOImpl;
import br.com.frequencia.model.Evento;
import br.com.frequencia.model.Frequencia;
import br.com.frequencia.model.Horario;
import br.com.frequencia.model.Participante;
import br.com.frequencia.model.ParticipanteInscrito;
import br.com.frequencia.model.Turma;
import br.com.frequencia.relatorio.bean.RelatorioBean;
import br.com.frequencia.relatorio.dto.AlunoDTO;
import br.com.frequencia.relatorio.dto.CrachaDTO;
import br.com.frequencia.relatorio.dto.CredenciamentoDTO;
import br.com.frequencia.relatorio.dto.EtiquetaDTO;
import br.com.frequencia.relatorio.dto.MapaFrequencia;
import br.com.frequencia.relatorio.dto.OficinaDTO;
import br.com.frequencia.relatorio.util.Constantes;
import br.com.frequencia.relatorio.util.RelatorioUtil;
import br.com.frequencia.ui.combo.ComboBoxItemModel;
import br.com.frequencia.ui.frame.PrincipalFrame;

enum FREQUENCIA {
	PRESENTE("P"), 
	AUSENTE("F"),
	A_COMPARECE(""), 
	PRESENCA_PARCIAL("PP");

	private String status;

	private FREQUENCIA(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}
}

public class RelatoriosController extends PersistenceController {
	private PrincipalFrame frame;
	/**
	 * @TODO Estou fazendo aqui na pressa, levar isto para um arquivo de configuração, de preferencia que o usuário possa alterar Foi
	 *       acordado com Amanda - ESAF que o mínimo de HORAS por TURNO pra considerar a presença é de 3 horas (total possível é 4hs)
	 */
	private final Integer MINIMO_HORAS_FREQUENCIA_TURNO = 3;

	public RelatoriosController(AbstractController parent, final PrincipalFrame frame) {
		super(parent);
		loadPersistenceContext();
		frame.addWindowListener(this);
		this.frame = frame;

		registerAction(frame.getButtonGerarRelCredenciamento(),
				TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {

					@Override
					protected void action() {

						String nomeArquivoSaidaRelatorio = "RelatorioCredenciamento-"
								+ ((ComboBoxItemModel) frame.getComboBoxTurmaRelCredenciamento().getSelectedItem()).getDescricao();
						List<CredenciamentoDTO> listaCredenciamento = new ArrayList<CredenciamentoDTO>();
						if (frame.getComboBoxTurmaRelCredenciamento().getSelectedIndex() > 0) {
							ParticipanteInscritoDAO piDAO = new ParticipanteInscritoDAOImpl(getPersistenceContext());
							List<ParticipanteInscrito> listaParticipantes = piDAO
									.pesquisarParticipanteInscritosPorTurma(((ComboBoxItemModel) frame.getComboBoxTurmaRelCredenciamento()
											.getSelectedItem()).getId());

							for (ParticipanteInscrito pi : listaParticipantes) {
								String siglaOrgao = "";
								if (null != pi.getParticipante().getOrgao())
									siglaOrgao = pi.getParticipante().getOrgao().getSiglaOrgao();
								listaCredenciamento.add(new CredenciamentoDTO((listaCredenciamento.size() + 1) + "", pi.getParticipante()
										.getNumInscricao(), pi.getParticipante().getNomParticipante(), siglaOrgao));
							}

						} else {
							ParticipanteDAO piDAO = new ParticipanteDAOImpl(getPersistenceContext());
							List<Participante> listaParticipantes = piDAO.getAll();

							for (Participante p : listaParticipantes) {
								String siglaOrgao = "";
								if (null != p.getOrgao())
									siglaOrgao = p.getOrgao().getSiglaOrgao();
								listaCredenciamento.add(new CredenciamentoDTO((listaCredenciamento.size() + 1) + "", p.getNumInscricao(), p
										.getNomParticipante(), siglaOrgao));
							}
						}

						if (null != listaCredenciamento && !listaCredenciamento.isEmpty()) {
							try {

								Evento evento = new EventoDAOImpl(getPersistenceContext()).getAll().get(0);
								
								new RelatorioBean().relatorioCredenciamento(listaCredenciamento, evento, nomeArquivoSaidaRelatorio, Constantes.EXCEL);
								JOptionPane.showMessageDialog(frame, "Relatório de Credenciamento da Turma gerado com sucesso em '"
										+ RelatorioUtil.CAMINHO_SAIDA + nomeArquivoSaidaRelatorio + "." + Constantes.EXCEL + "'",
										TITLE_INFORMACAO, JOptionPane.INFORMATION_MESSAGE);

							} catch (JRException e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
							} catch (IOException e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
							}
						} else {
							JOptionPane.showMessageDialog(frame, "Nenhum registro foi encontrado!", TITLE_INFORMACAO,
									JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}));

		registerAction(frame.getButtonGerarRelEtiquietas(),
				TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {

					@Override
					protected void action() {
						Participante paramParticipante = new Participante();
						paramParticipante.setNumInscricao(frame.getTextFieldInscricaoRelEtiqueta().getText());
						paramParticipante.setNomParticipante(frame.getTextFieldNomeRelEtiqueta().getText());

						List<EtiquetaDTO> listaEtiquetas = new ArrayList<EtiquetaDTO>();
						String nomeArquivoSaidaRelatorio = null;

						nomeArquivoSaidaRelatorio = "RelatorioEtiqueta-"
								+ ((ComboBoxItemModel) frame.getComboBoxTurmaRelEtiquetas().getSelectedItem()).getDescricao();

						ParticipanteDAO partDAO = new ParticipanteDAOImpl(getPersistenceContext());
						List<Participante> listaParticipantes = partDAO.pesquisarParticipantes(((ComboBoxItemModel) frame
								.getComboBoxTurmaRelEtiquetas().getSelectedItem()).getId(), paramParticipante, frame
								.getChckbxApenasNovosParticipantes().isSelected());

						if (null != listaParticipantes && !listaParticipantes.isEmpty()) {

							for (Participante p : listaParticipantes) {
								listaEtiquetas.add(new EtiquetaDTO(p.getNomCracha(), p.getNumInscricao()));
							}

							try {
								new RelatorioBean().etiqueta(listaEtiquetas, nomeArquivoSaidaRelatorio, Constantes.EXCEL);
								JOptionPane.showMessageDialog(frame, "Relatório de Etiquetas da Turma gerado com sucesso em '"
										+ RelatorioUtil.CAMINHO_SAIDA + nomeArquivoSaidaRelatorio + "." + Constantes.EXCEL + "'",
										TITLE_INFORMACAO, JOptionPane.INFORMATION_MESSAGE);
							} catch (JRException e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
							} catch (IOException e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
							}
						} else {
							JOptionPane.showMessageDialog(frame, "Nenhum registro foi encontrado!", TITLE_INFORMACAO,
									JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}));

		registerAction(frame.getButtonGerarRelCracha(),
				TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {

					@Override
					protected void action() {
						Participante paramParticipante = new Participante();
						paramParticipante.setNumInscricao(frame.getTextFieldInscricaoRelEtiqueta().getText());
						paramParticipante.setNomParticipante(frame.getTextFieldNomeRelEtiqueta().getText());

						List<CrachaDTO> listaCrachas = new ArrayList<CrachaDTO>();
						String nomeArquivoSaidaRelatorio = null;

						nomeArquivoSaidaRelatorio = "RelatorioCrachá-"
								+ ((ComboBoxItemModel) frame.getComboBoxTurmaRelEtiquetas().getSelectedItem()).getDescricao();

						ParticipanteDAO partDAO = new ParticipanteDAOImpl(getPersistenceContext());
						ParticipanteInscritoDAO partInscDAO = new ParticipanteInscritoDAOImpl(getPersistenceContext());

						List<Participante> listaParticipantes = partDAO.pesquisarParticipantes(((ComboBoxItemModel) frame
								.getComboBoxTurmaRelEtiquetas().getSelectedItem()).getId(), paramParticipante, frame
								.getChckbxApenasNovosParticipantes().isSelected());

						DateFormat hf = new SimpleDateFormat("HH:mm");

						if (null != listaParticipantes && !listaParticipantes.isEmpty()) {

							CrachaDTO crachaDTO;
							Horario h;
							List<OficinaDTO> listOficinas;
							int q = 0;
							String nomeOrgao;
							for (Participante p : listaParticipantes) {
								nomeOrgao = "";
								if (null != p.getOrgao())
									nomeOrgao = p.getOrgao().getNomOrgao();

								listaCrachas.add(crachaDTO = new CrachaDTO(p.getNumInscricao(), p.getNomParticipante().toUpperCase(),
										nomeOrgao.toUpperCase(), p.getNomCracha().toUpperCase(), null));
								List<ParticipanteInscrito> listPartInscritos = partInscDAO.pesquisarParticipanteInscritos(p);

								if (null != listPartInscritos && !listPartInscritos.isEmpty()) {
									listOficinas = new ArrayList<OficinaDTO>();
									q = 1;
									for (ParticipanteInscrito pi : listPartInscritos) {
										if (++q > 10)
											break;
										h = pi.getTurma().getHorario();
										listOficinas.add(new OficinaDTO("Horário \"" + h.getDescHorario() + "\" das "
												+ hf.format(h.getHrInicial().getTime()) + " às " + hf.format(h.getHrFinal().getTime()), pi
												.getTurma().getNomDisciplina(), pi.getTurma().getSala().getNomSala()));
									}
									crachaDTO.setOficinas(listOficinas);
									System.out.println("listOficinas.SIZE: " + listOficinas.size());
								}
							}

							try {

								new RelatorioBean().crachas(listaCrachas, nomeArquivoSaidaRelatorio, Constantes.PDF);
								JOptionPane.showMessageDialog(frame, "Relatório de Crachás da Turma gerado com sucesso em '"
										+ RelatorioUtil.CAMINHO_SAIDA + nomeArquivoSaidaRelatorio + "." + Constantes.PDF + "'",
										TITLE_INFORMACAO, JOptionPane.INFORMATION_MESSAGE);

							} catch (JRException e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
							} catch (IOException e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
							}
						} else {
							JOptionPane.showMessageDialog(frame, "Nenhum registro foi encontrado!", TITLE_INFORMACAO,
									JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}));
		
		registerAction(frame.getButtonGerarMapaFrequencia(),
				TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {

					@Override
					protected void action() {

						try {
							frame.getWaitPreviewRelatorios().start();

							Evento evento = new EventoDAOImpl(getPersistenceContext()).getAll().get(0);

							String nomeArquivoSaidaRelatorio = "RelatorioMapaFrequencia-"
									+ ((ComboBoxItemModel) frame.getComboBoxTurmaMapaFrequencia().getSelectedItem()).getDescricao();

							ParticipanteDAO partDAO = new ParticipanteDAOImpl(getPersistenceContext());

							List<Participante> listaParticipantes = partDAO.pesquisarParticipantesTurma(((ComboBoxItemModel) frame
									.getComboBoxTurmaMapaFrequencia().getSelectedItem()).getId());

							List<AlunoDTO> listaFrequencia = new ArrayList<AlunoDTO>();

							for (Participante participante : listaParticipantes) {
								String siglaOrgao = "";
								if (null != participante.getOrgao())
									siglaOrgao = participante.getOrgao().getSiglaOrgao();
								AlunoDTO alunoDTO = new AlunoDTO(participante.getNumInscricao(), participante.getNomParticipante()
										.toUpperCase(), siglaOrgao);

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

								DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
								FrequenciaDAO freqDAO = new FrequenciaDAOImpl(getPersistenceContext());
								CredenciamentoDAO credDAO = new CredenciamentoDAOImpl(getPersistenceContext());
								do {
									FREQUENCIA manha = FREQUENCIA.A_COMPARECE;
									FREQUENCIA tarde = FREQUENCIA.A_COMPARECE;

									Calendar datHorFreqMANHA_INI = new GregorianCalendar(datFrequencia.get(Calendar.YEAR), datFrequencia
											.get(Calendar.MONTH), datFrequencia.get(Calendar.DAY_OF_MONTH), 8, 30);
									Calendar datHorFreqMANHA_FIM = new GregorianCalendar(datFrequencia.get(Calendar.YEAR), datFrequencia
											.get(Calendar.MONTH), datFrequencia.get(Calendar.DAY_OF_MONTH), 12, 30);
									Calendar datHorFreqTARDE_INI = new GregorianCalendar(datFrequencia.get(Calendar.YEAR), datFrequencia
											.get(Calendar.MONTH), datFrequencia.get(Calendar.DAY_OF_MONTH), 14, 00);
									Calendar datHorFreqTARDE_FIM = new GregorianCalendar(datFrequencia.get(Calendar.YEAR), datFrequencia
											.get(Calendar.MONTH), datFrequencia.get(Calendar.DAY_OF_MONTH), 18, 00);

									Integer minutosFrequenciaManha = 0;
									Integer minutosFrequenciaTarde = 0;
									if (datHorFreqMANHA_INI.before(datCorrente)) {

										List<Frequencia> frequencias = freqDAO.pesquisarFrequenciasParticipanteData(
												participante.getNumInscricao(), datFrequencia);
										if (null == frequencias || frequencias.isEmpty()) {
											manha = FREQUENCIA.AUSENTE;
											tarde = FREQUENCIA.AUSENTE;
										} else {

											for (Frequencia fre : frequencias) {
												if (fre.getHorarioEntrada().before(datHorFreqMANHA_FIM)) { // até 12 e 30
													/* Manhã */
													int diffInMin = (int) ((fre.getHorarioSaida().getTimeInMillis() - fre
															.getHorarioEntrada().getTimeInMillis()) / (1000 * 60));
													minutosFrequenciaManha += diffInMin;
												} else {
													/* Tarde */
													int diffInMin = (int) ((fre.getHorarioSaida().getTimeInMillis() - fre
															.getHorarioEntrada().getTimeInMillis()) / (1000 * 60));
													minutosFrequenciaTarde += diffInMin;
												}
											}

											if (minutosFrequenciaManha >= MINIMO_HORAS_FREQUENCIA_TURNO * 60)
												manha = FREQUENCIA.PRESENTE;
											else if (datHorFreqMANHA_INI.before(datCorrente) && datHorFreqMANHA_FIM.after(datCorrente)
													&& minutosFrequenciaManha > 0)
												// Está na sala mais a frequencia do turno ainda não fechou
												manha = FREQUENCIA.PRESENCA_PARCIAL; 
											else
												manha = FREQUENCIA.AUSENTE;

											// Total de frequencia deu mais de 180 minutos
											if (minutosFrequenciaTarde >= MINIMO_HORAS_FREQUENCIA_TURNO * 60) 
												tarde = FREQUENCIA.PRESENTE;
											else if (datHorFreqTARDE_INI.before(datCorrente) && datHorFreqTARDE_FIM.after(datCorrente)
													&& minutosFrequenciaTarde > 0)
												// Está na sala mais a frequencia do turno ainda não fechou
												tarde = FREQUENCIA.PRESENCA_PARCIAL; 
											else
												tarde = FREQUENCIA.AUSENTE;
										}

										// No primeiro dia a frequência da manhã é por meio do credenciamento, se for credenciado tem frequencia
										if (datFrequencia.get(Calendar.DAY_OF_MONTH) == datInicioEvento.get(Calendar.DAY_OF_MONTH))
											if (null != credDAO.recuperarCredenciamento(participante.getId())) {
												// Crendenciado, então tem frequencia na manhã do dia do evento
												minutosFrequenciaManha = 240; 
												manha = FREQUENCIA.PRESENTE;
											}
									}

									if (frame.getChckbxExibirMinutos().isSelected()) {
										// EXIBIR os minutos ao invés de F, P ou PP
										alunoDTO.getMapaFrequencia().add(
												new MapaFrequencia(df.format(datFrequencia.getTime()) + " MANHÃ", minutosFrequenciaManha
														.toString()));
										alunoDTO.getMapaFrequencia().add(
												new MapaFrequencia(df.format(datFrequencia.getTime()) + " TARDE", minutosFrequenciaTarde
														.toString()));
									} else {
										alunoDTO.getMapaFrequencia().add(
												new MapaFrequencia(df.format(datFrequencia.getTime()) + " MANHÃ", manha.getStatus()));
										alunoDTO.getMapaFrequencia().add(
												new MapaFrequencia(df.format(datFrequencia.getTime()) + " TARDE", tarde.getStatus()));
									}

									// Próxima dia do Evento
									datFrequencia.add(Calendar.DAY_OF_MONTH, 1);
								} while (datFrequencia.before(datFimEvento) || datFrequencia.compareTo(datFimEvento) == 0);

								listaFrequencia.add(alunoDTO);
							}

							try {
								new RelatorioBean().relatorioMapa(listaFrequencia, evento, nomeArquivoSaidaRelatorio);
								JOptionPane.showMessageDialog(frame, "Relatório de Mapa de Frequência gerado com sucesso em '"
										+ RelatorioUtil.CAMINHO_SAIDA + nomeArquivoSaidaRelatorio + "." + Constantes.EXCEL + "'",
										TITLE_INFORMACAO, JOptionPane.INFORMATION_MESSAGE);
							} catch (JRException e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
							} catch (IOException e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
							}
						} finally {
							frame.getWaitPreviewRelatorios().stop();
						}
					}
				}));

		carregarCombos();
	}

	private void carregarCombos() {
		if (frame.getComboBoxTurmaRelCredenciamento().getItemCount() == 0) {
			TurmaDAO turmaDAO = new TurmaDAOImpl(getPersistenceContext());
			List<Turma> listaTurmas = turmaDAO.getAll();
			frame.setListaTurmaRelCredenciamento(listaTurmas);
			frame.setListaTurmaRelEtiqueta(listaTurmas);
			frame.setListaTurmaMapaFrequencia(listaTurmas);
		}
	}
}
