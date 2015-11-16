package br.com.frequencia.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import br.com.frequencia.action.AbstractAction;
import br.com.frequencia.action.TransactionalAction;
import br.com.frequencia.dao.CredenciamentoDAO;
import br.com.frequencia.dao.EventoDAO;
import br.com.frequencia.dao.FrequenciaDAO;
import br.com.frequencia.dao.HorarioDAO;
import br.com.frequencia.dao.InstrutorDAO;
import br.com.frequencia.dao.OrgaoDAO;
import br.com.frequencia.dao.ParticipanteDAO;
import br.com.frequencia.dao.ParticipanteInscritoDAO;
import br.com.frequencia.dao.SalaDAO;
import br.com.frequencia.dao.TurmaDAO;
import br.com.frequencia.dao.impl.CredenciamentoDAOImpl;
import br.com.frequencia.dao.impl.EventoDAOImpl;
import br.com.frequencia.dao.impl.FrequenciaDAOImpl;
import br.com.frequencia.dao.impl.HorarioDAOImpl;
import br.com.frequencia.dao.impl.InstrutorDAOImpl;
import br.com.frequencia.dao.impl.OrgaoDAOImpl;
import br.com.frequencia.dao.impl.ParticipanteDAOImpl;
import br.com.frequencia.dao.impl.ParticipanteInscritoDAOImpl;
import br.com.frequencia.dao.impl.SalaDAOImpl;
import br.com.frequencia.dao.impl.TurmaDAOImpl;
import br.com.frequencia.model.Credenciamento;
import br.com.frequencia.model.Evento;
import br.com.frequencia.model.Frequencia;
import br.com.frequencia.model.Horario;
import br.com.frequencia.model.Instrutor;
import br.com.frequencia.model.Orgao;
import br.com.frequencia.model.Participante;
import br.com.frequencia.model.ParticipanteInscrito;
import br.com.frequencia.model.Sala;
import br.com.frequencia.model.Turma;
import br.com.frequencia.ui.frame.PrincipalFrame;
import br.com.frequencia.util.DateUtil;

import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import com.googlecode.jcsv.writer.CSVWriter;
import com.googlecode.jcsv.writer.internal.CSVWriterBuilder;

public class ImportarExportarController extends PersistenceController {

	private PrincipalFrame frame;
	private static String CAMINHO_ARQUIVO = "C:/Frequencia/Atualizacao.sql";
	private static String MSG_LISTAS_VAZIAS = "Não existem novos participantes para serem exportados!";

	public ImportarExportarController(AbstractController parent, final PrincipalFrame frame) {
		super(parent);
		loadPersistenceContext();
		frame.addWindowListener(this);
		this.frame = frame;
		this.frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);

		registerAction(frame.getButtonImportarCSV_Turma_CFC(),
				TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {

					public void action() {
						frame.getLabelAguardeCarregandoDados().setVisible(Boolean.TRUE);
						int DialogReturnValue = frame.getFilePlanilhaTurmaCSV().showOpenDialog(frame);

						if (DialogReturnValue == JFileChooser.APPROVE_OPTION) {
							File fileCSV = frame.getFilePlanilhaTurmaCSV().getSelectedFile();
							System.out.println("Arquivo CSV selecionado: " + fileCSV.getPath());

							try {
								Reader reader = new InputStreamReader(new FileInputStream(fileCSV), "ISO-8859-1");

								CSVReader<String[]> csvParser = CSVReaderBuilder.newDefaultReader(reader);
								List<String[]> data = csvParser.readAll();

								TurmaDAO turmaDAO = new TurmaDAOImpl(getPersistenceContext());

								EventoDAO eventoDAO = new EventoDAOImpl(getPersistenceContext());

								Map<Character, Horario> horariosMap = insereOuRecuperaHorariosPadrao();

								Turma turma = null;
								Sala sala = null;
								Instrutor instrutor = null;
								for (String[] colums : data) {
									System.out.println(Arrays.toString(colums));
									
									// setando id da grade_oficina (SISFIE)
									turma = new Turma(Integer.parseInt(colums[5].trim()));
									turma.setNomDisciplina(colums[0].trim());
									
									instrutor = insereOuRecuperaInstrutor(colums[2].trim());
									turma.setInstrutor(instrutor);
									
									// 01A, por exemplo
									turma.setNomTurma(colums[3].trim());
									
									sala = insereOuRecuperaSala(colums[4].trim(), Integer.parseInt(colums[1].trim()));
									turma.setSala(sala);
									
									// por exemplo 01A, recuperaria o horário "A" do Map
									turma.setHorario(horariosMap.get(colums[6].trim().charAt(colums[6].trim().length() - 1)));
									
									turma.setEvento(eventoDAO.findById(Integer.parseInt(colums[7].trim())));
									
									turmaDAO.save(turma);
								}
								JOptionPane.showMessageDialog(frame, "Turmas importadas com sucesso!", "Importação de Grades",
										JOptionPane.INFORMATION_MESSAGE);
								frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
								refreshTableTurma();
								carregarCombos();
							} catch (FileNotFoundException e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
								frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
							} catch (IOException e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
								frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
							} catch (Exception e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
								frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
							}

						} else {
							System.out.println("Arquivo CSV selecionado: Dialog cancelada pelo usuario");
							frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
						}
					}

					public void posAction() {
						cleanUp();
					}

					/**
					 * Cria os horários de A a I - segunda a tarde até sexta a tarde, cada LETRA um turno.
					 * 
					 * @return
					 */
					private Map<Character, Horario> insereOuRecuperaHorariosPadrao() {
						Map<Character, Horario> horariosMap = new TreeMap<Character, Horario>();

						HorarioDAO horDAO = new HorarioDAOImpl(getPersistenceContext());
						Horario horario = null;

						Calendar hrInicialManha = new GregorianCalendar(0, 0, 0, 8, 30);
						Calendar hrFinalManha = new GregorianCalendar(0, 0, 0, 12, 30);
						Calendar hrInicialTarde = new GregorianCalendar(0, 0, 0, 14, 00);
						Calendar hrFinalTarde = new GregorianCalendar(0, 0, 0, 18, 00);

						// Segunda tarde
						horario = horDAO.findById(1);
						if (horario == null) {
							horario = new Horario(1);
							horario.setDescHorario("A");
							horario.setHrInicial(hrInicialTarde);
							horario.setHrFinal(hrFinalTarde);
							horDAO.save(horario);
						}
						horariosMap.put(horario.getDescHorario().charAt(0), horario);

						// Terça manhã
						horario = horDAO.findById(2);
						if (horario == null) {
							horario = new Horario(2);
							horario.setDescHorario("B");
							horario.setHrInicial(hrInicialManha);
							horario.setHrFinal(hrFinalManha);
							horDAO.save(horario);
						}
						horariosMap.put(horario.getDescHorario().charAt(0), horario);

						// Terça tarde
						horario = horDAO.findById(3);
						if (horario == null) {
							horario = new Horario(3);
							horario.setDescHorario("C");
							horario.setHrInicial(hrInicialTarde);
							horario.setHrFinal(hrFinalTarde);
							horDAO.save(horario);
						}
						horariosMap.put(horario.getDescHorario().charAt(0), horario);

						// Quarta manhã
						horario = horDAO.findById(4);
						if (horario == null) {
							horario = new Horario(4);
							horario.setDescHorario("D");
							horario.setHrInicial(hrInicialManha);
							horario.setHrFinal(hrFinalManha);
							horDAO.save(horario);
						}
						horariosMap.put(horario.getDescHorario().charAt(0), horario);

						// Quarta tarde
						horario = horDAO.findById(5);
						if (horario == null) {
							horario = new Horario(5);
							horario.setDescHorario("E");
							horario.setHrInicial(hrInicialTarde);
							horario.setHrFinal(hrFinalTarde);
							horDAO.save(horario);
						}
						horariosMap.put(horario.getDescHorario().charAt(0), horario);

						// Quinta manhã
						horario = horDAO.findById(6);
						if (horario == null) {
							horario = new Horario(6);
							horario.setDescHorario("F");
							horario.setHrInicial(hrInicialManha);
							horario.setHrFinal(hrFinalManha);
							horDAO.save(horario);
						}
						horariosMap.put(horario.getDescHorario().charAt(0), horario);

						// Quinta tarde
						horario = horDAO.findById(7);
						if (horario == null) {
							horario = new Horario(7);
							horario.setDescHorario("G");
							horario.setHrInicial(hrInicialTarde);
							horario.setHrFinal(hrFinalTarde);
							horDAO.save(horario);
						}
						horariosMap.put(horario.getDescHorario().charAt(0), horario);

						// Sexta manhã
						horario = horDAO.findById(8);
						if (horario == null) {
							horario = new Horario(8);
							horario.setDescHorario("H");
							horario.setHrInicial(hrInicialManha);
							horario.setHrFinal(hrFinalManha);
							horDAO.save(horario);
						}
						horariosMap.put(horario.getDescHorario().charAt(0), horario);

						// Sexta tarde
						horario = horDAO.findById(9);
						if (horario == null) {
							horario = new Horario(9);
							horario.setDescHorario("I");
							horario.setHrInicial(hrInicialTarde);
							horario.setHrFinal(hrFinalTarde);
							horDAO.save(horario);
						}
						horariosMap.put(horario.getDescHorario().charAt(0), horario);

						return horariosMap;
					}

					/**
					 * Com base no nome da sala, verifica se já está cadastrada, senão cadastra-a
					 * 
					 * @param nome
					 * @param nomeCompleto
					 * @return
					 */
					private Sala insereOuRecuperaSala(String nomeCompleto, Integer capacidade) {

						SalaDAO salaDAO = new SalaDAOImpl(getPersistenceContext());

						Sala sala = null;
						List<Sala> listSala = salaDAO.pesquisarSalaByNomeEqual(nomeCompleto);
						if (listSala != null && !listSala.isEmpty()) {
							sala = listSala.get(0);
						} else {
							sala = new Sala();
							sala.setFlgAtivo(true);
							sala.setNomSala(nomeCompleto);
							sala.setNumCapacidade(capacidade);
							salaDAO.save(sala);
						}

						return sala;
					}

					/**
					 * Com base no nome do instruturo, verifica se já está cadastro, senão, cadastra-o
					 * 
					 * @param nome
					 * @return
					 */
					private Instrutor insereOuRecuperaInstrutor(String nome) {
						if (null == nome || "".equals(nome.trim())) {
							nome = "A DEFINIR";
						}
						InstrutorDAO instrutorDAO = new InstrutorDAOImpl(getPersistenceContext());

						Instrutor instrutor = null;
						List<Instrutor> listInstrutor = instrutorDAO.pesquisarInstrutorByNomeEqual(nome);
						if (listInstrutor != null && !listInstrutor.isEmpty()) {
							instrutor = listInstrutor.get(0);
						} else {
							instrutor = new Instrutor();
							instrutor.setFlgAtivo(true);
							instrutor.setNomInstrutor(nome);
							instrutorDAO.save(instrutor);
						}

						return instrutor;
					}
				}));

		registerAction(frame.getButtonImportarCSV_Inscrito_CFC(),
				TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {

					public void action() {
						frame.getLabelAguardeCarregandoDados().setVisible(Boolean.TRUE);
						int DialogReturnValue = frame.getFilePlanilhaInscritosCSV().showOpenDialog(frame);

						if (DialogReturnValue == JFileChooser.APPROVE_OPTION) {
							File fileCSV = frame.getFilePlanilhaInscritosCSV().getSelectedFile();
							System.out.println("Arquivo CSV selecionado: " + fileCSV.getPath());

							try {
								Reader reader = new InputStreamReader(new FileInputStream(fileCSV), "ISO-8859-1");

								CSVReader<String[]> csvParser = CSVReaderBuilder.newDefaultReader(reader);

								List<String[]> data = csvParser.readAll();

								TurmaDAO turmaDAO = new TurmaDAOImpl(getPersistenceContext());
								ParticipanteDAO participanteDAO = new ParticipanteDAOImpl(getPersistenceContext());
								ParticipanteInscritoDAO partInscritoDAO = new ParticipanteInscritoDAOImpl(getPersistenceContext());
								Turma turma = null;
								Participante participante = new Participante();
								ParticipanteInscrito participanteTurma = null;

								/**
								 * @TODO Ver com Gustavo ou Pessoal do Anexo como gerar os 5 primeiros digitos da inscrição
								 * 
								 */
								for (String[] colums : data) {
									System.out.println(Arrays.toString(colums));
									String inscricao = colums[0].trim();

									// Se for o mesmo canditato registra a penas a turma pra ele
									if (!inscricao.equals(participante.getNumInscricao())) {
										participante = new Participante();
										participante.setFlgAlterado(false);
										participante.setFlgRecemCadastrado(false);
										/** @TODO ver com Wesley **/

										/**
										 * @TODO REVER a importação do nome para o crachá, não está CERTO deste jeito 31/05/2013 - A
										 *       informação do nome do crachá não veio da planilha do CFC, acordado com Abadia e Amanda
										 *       (ESAF) que não importaremos este dado. Pegando o primeiro nome então, pra ser o nome do
										 *       Crachá
										 */
										String nomeCracha;
										// nomeCracha não veio no CSV, pegando primeiro nome
										if (colums[3].indexOf(" ") > -1) {
											nomeCracha = colums[3].substring(0, colums[3].indexOf(" "));
										} else {
											nomeCracha = colums[3];
										}

										participante.setNomCracha(nomeCracha);
										participante.setNomParticipante(colums[3]);
										participante.setNumInscricao(inscricao.trim());
										/*
										 * 31/05/2013 - A informação do Órgão não veio da planilha do CFC, acordado com Abadia e Amanda
										 * (ESAF) que não importaremos este dado.
										 */
										participante.setOrgao(insereOuRecuperaOrgao(colums[1], colums[2])); // sigla do órgão
										participanteDAO.save(participante);
									}

									turma = turmaDAO.findById(Integer.parseInt(colums[4].trim()));
									if (null == turma) {
										throw new Exception("TURMA não encontrada: " + colums[4].trim());
									}
									participanteTurma = new ParticipanteInscrito();
									participanteTurma.setParticipante(participante);
									participanteTurma.setTurma(turma);

									partInscritoDAO.save(participanteTurma);

								}
								frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
								JOptionPane.showMessageDialog(frame, "Distribuição dos inscritos nas oficinas importada com sucesso!",
										"Importação de Inscritos", JOptionPane.INFORMATION_MESSAGE);
								refreshTablesParticipantes();
								carregarCombos();
							} catch (FileNotFoundException e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
								frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
							} catch (IOException e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
								frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
							} catch (Exception e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
								frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
							}

						} else {
							frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
							System.out.println("Arquivo CSV selecionado: Dialog cancelada pelo usuario");
						}
					}

				}));

		registerAction(frame.getButtonImportar(), TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {

			public void action() {
				int DialogReturnValue = frame.getFileImportacaoSQL().showOpenDialog(frame);

				if (DialogReturnValue == JFileChooser.APPROVE_OPTION) {
					File fileSQL = frame.getFileImportacaoSQL().getSelectedFile();
					System.out.println("Arquivo SQL selecionado: " + fileSQL.getPath());

					BufferedReader buffReader = null;
					try {
						FileReader reader = new FileReader(fileSQL);
						buffReader = new BufferedReader(reader);
						String linha;
						List<String> listaSQL = new ArrayList<String>();
						while ((linha = buffReader.readLine()) != null) {
							listaSQL.add(linha);
						}
						ParticipanteDAO participanteDAO = new ParticipanteDAOImpl(getPersistenceContext());
						participanteDAO.importarParticipantes(listaSQL);
						JOptionPane.showMessageDialog(frame, "Novos Participantes importados com sucesso!",
								"Importação de Novos Participantes", JOptionPane.INFORMATION_MESSAGE);
						refreshTablesParticipantes();
						carregarCombos();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (buffReader != null)
							try {
								buffReader.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
					}

				} else {
					System.out.println("Arquivo SQL selecionado: Dialog cancelada pelo usuario");
				}
			}
		}));

		registerAction(frame.getButtonExportar(), TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {

			private ParticipanteDAO dao;
			private File file = new File(CAMINHO_ARQUIVO);
			List<Participante> listaParticipantesNovos;
			List<Participante> listaParticipanteInscritos;

			@Override
			public void action() {
				listaParticipanteInscritos = new ArrayList<Participante>();
				listaParticipantesNovos = new ArrayList<Participante>();
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
						refreshTablesParticipantes();
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
					if (participante.getOrgao() != null && participante.getOrgao().getId() != null) {
						orgao = participante.getOrgao().getId().toString();
					}
					pw.println("INSERT INTO PARTICIPANTE (ID_ORGAO,ID_USUARIO,NUM_INSCRICAO,NOM_PARTICIPANTE,NOM_CRACHA,FLG_RECEM_CADASTRADO,FLG_ALTERADO) VALUES ("
							+ orgao
							+ ","
							+ usuario
							+ ",'"
							+ participante.getNumInscricao()
							+ "','"
							+ participante.getNomParticipante()
							+ "','" + participante.getNomCracha() + "'," + 1 + "," + 0 + ");");
					participante.setFlgAlterado(Boolean.FALSE);
					dao.save(participante);
				}
			}
		}));

		registerAction(frame.getButtonImportarCSV_Evento_CFC(),
				TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {

					public void action() {
						frame.getLabelAguardeCarregandoDados().setVisible(Boolean.TRUE);
						int DialogReturnValue = frame.getFilePlanilhaCrachaOrgaoCSV().showOpenDialog(frame);

						if (DialogReturnValue == JFileChooser.APPROVE_OPTION) {
							File fileCSV = frame.getFilePlanilhaCrachaOrgaoCSV().getSelectedFile();
							System.out.println("Arquivo CSV selecionado: " + fileCSV.getPath());

							try {
								Reader reader = new InputStreamReader(new FileInputStream(fileCSV), "ISO-8859-1");
								List<String[]> data = null;
								try {
									CSVReader<String[]> csvParser = CSVReaderBuilder.newDefaultReader(reader);
									data = csvParser.readAll();
									EventoDAO eventoDAO = new EventoDAOImpl(getPersistenceContext());
									if (data != null && !data.isEmpty()) {
										if (data.get(0).length != 5) {
											throw new Exception(
													"Erro na importação (Arquivo não corresponde a um arquivo de Evento válido)");
										}
										for (String[] colums : data) {
											Evento evento = new Evento();
											evento.setId(Integer.parseInt(colums[0].trim()));
											evento.setNomEvento(colums[1].trim());
											evento.setDataInicio(DateUtil.getDataHora(colums[2].trim(), "dd/MM/yyyy"));
											evento.setDataFim(DateUtil.getDataHora(colums[3].trim(), "dd/MM/yyyy"));
											evento.setDescLocalizacaoEvento(colums[4].trim());
											evento.setFlgAtivo(true);
											eventoDAO.save(evento);
										}
									}
								} finally {
									reader.close();
								}
								JOptionPane.showMessageDialog(frame, "Evento importado com sucesso!", "Importação",
										JOptionPane.INFORMATION_MESSAGE);
								carregarCombos();
								frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
							} catch (FileNotFoundException e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
								frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
							} catch (IOException e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
								frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
							} catch (Exception e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
								frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
							}
						}
					}

					@Override
					protected void posAction() {
						cleanUp();
					}
				}));

		registerAction(frame.getButtonGerarPlanilhaCrach(),
				TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {

					public void action() {

						Writer writer = null;
						try {
							writer = new OutputStreamWriter(new FileOutputStream("C:\\Frequencia\\PlanilhaCracha.csv"), Charset
									.forName("ISO-8859-1"));

							CSVWriter<String[]> csvWriter = CSVWriterBuilder.newDefaultWriter(writer);

							ParticipanteDAO participanteDAO = new ParticipanteDAOImpl(getPersistenceContext());
							ParticipanteInscritoDAO participanteInscDAO = new ParticipanteInscritoDAOImpl(getPersistenceContext());

							List<Participante> lista = participanteDAO.getAll();

							List<String> dadosParti = null;
							for (Participante part : lista) {
								List<ParticipanteInscrito> listaPart = participanteInscDAO.pesquisarParticipanteInscritos(part
										.getNumInscricao());

								dadosParti = new ArrayList<String>();
								dadosParti.add(part.getNumInscricao());
								dadosParti.add(part.getNomParticipante());
								dadosParti.add(part.getNomCracha());
								if (part.getOrgao() != null)
									dadosParti.add(part.getOrgao().getNomOrgao());

								for (int i = 0; i < 9; i++) {
									try {
										ParticipanteInscrito parti = listaPart.get(i);
										dadosParti.add(parti.getTurma().getNomDisciplina());
										dadosParti.add(parti.getTurma().getSala().getNomSala());
									} catch (IndexOutOfBoundsException e) {
										dadosParti.add("A ESCOLHER");
										dadosParti.add(" ");
									}

								}

								String[] linhaCSV = new String[9];
								linhaCSV = dadosParti.toArray(linhaCSV);
								System.out.println(Arrays.toString(linhaCSV));
								csvWriter.write(linhaCSV);
							}

							JOptionPane.showMessageDialog(frame, "Planilha para verso de Crachá gerada com sucesso!", "Planilha",
									JOptionPane.INFORMATION_MESSAGE);
							frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
							frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
						} catch (IOException e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
							frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
						} catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
							frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
						} finally {
							if (null != writer)
								try {
									writer.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
						}
					}
				}));

		registerAction(frame.getButtonExportarFrequencia(),
				TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {

					public void action() {

						Writer writer = null;
						try {
							int DialogReturnValue = frame.getPastaExportacaoFrequencia().showSaveDialog(frame);

							if (DialogReturnValue != JFileChooser.APPROVE_OPTION) {
								return;
							}
							SimpleDateFormat formato = new SimpleDateFormat("yyyyMMddHHmmssS");

							writer = new OutputStreamWriter(new FileOutputStream(new File(frame.getPastaExportacaoFrequencia()
									.getSelectedFile(), "ExportacaoFrequencia - " + formato.format(new Date(System.currentTimeMillis()))
									+ ".csv")), Charset.forName("ISO-8859-1"));

							CSVWriter<String[]> csvWriter = CSVWriterBuilder.newDefaultWriter(writer);

							FrequenciaDAO freqDAO = new FrequenciaDAOImpl(getPersistenceContext());
							List<Frequencia> frequencias = freqDAO.pesquisarFrequenciasFinalizadas();

							for (Frequencia f : frequencias) {

								String[] linhaCSV = new String[] {
										f.getTurma().getId().toString(), // Equivale ao id da grade de oficina no sisfie
										f.getParticipante().getNumInscricao().toString(), 
										formato.format(f.getHorarioEntrada().getTime()),
										formato.format(f.getHorarioSaida().getTime()) };
								csvWriter.write(linhaCSV);
							}

							JOptionPane.showMessageDialog(frame, "Frequências desta máquina exportadas com sucesso!", "Planilha",
									JOptionPane.INFORMATION_MESSAGE);
							frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
							frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
						} catch (IOException e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
							frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
						} catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
							frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
						} finally {
							if (null != writer)
								try {
									writer.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
						}

					}

				}));

		registerAction(frame.getButtonImportarFrequencia(),
				TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {

					public void action() {

						Writer writer = null;
						try {
							int dialogReturnValue = frame.getFilesImportacaoFrequencia().showOpenDialog(frame);

							if (dialogReturnValue != JFileChooser.APPROVE_OPTION) {
								return;
							}

							List<Integer> idsTurmasParaRecarregarlistaFrequencia = null;
							
							SimpleDateFormat formato = new SimpleDateFormat("yyyyMMddHHmmssS");
							ParticipanteDAO participanteDAO = new ParticipanteDAOImpl(getPersistenceContext());
							FrequenciaDAO freqDAO = new FrequenciaDAOImpl(getPersistenceContext());
							Frequencia frequencia = null;
							for (File file : frame.getFilesImportacaoFrequencia().getSelectedFiles()) {
								Reader reader = new InputStreamReader(new FileInputStream(file), "ISO-8859-1");
								List<String[]> data = null;
								try {
									CSVReader<String[]> csvParser = CSVReaderBuilder.newDefaultReader(reader);
									data = csvParser.readAll();
								} finally {
									reader.close();
								}
								
								idsTurmasParaRecarregarlistaFrequencia = new ArrayList<Integer>();
								
								for (String[] colums : data) {
									
									Participante participante = new Participante();
									participante = participanteDAO.pesquisarParticipantes(colums[1]);
									
									if (participante != null){
										Calendar horarioEntrada = new GregorianCalendar();
										horarioEntrada.setTime(formato.parse(colums[2]));
										
										if (!freqDAO.existeFrequenciaPartTurmaHorEntrada(participante.getId(),
												Integer.valueOf(colums[0]), horarioEntrada)) {
											Calendar horarioSaida = new GregorianCalendar();
											horarioSaida.setTime(formato.parse(colums[3]));
											
											frequencia = new Frequencia();
											frequencia.setParticipante(participante);
											frequencia.setTurma(new Turma(Integer.valueOf(colums[0])));
											frequencia.setHorarioEntrada(horarioEntrada);
											frequencia.setHorarioSaida(horarioSaida);
											
											freqDAO.save(frequencia);
										} else {
											System.out.println("FREQUENCIA JA EXISTE -> " + Arrays.toString(colums));
										}
										
										idsTurmasParaRecarregarlistaFrequencia.add(Integer.valueOf(colums[0]));
									} else {
										throw new Exception("Participante do arquivo de registro de frequência inexistente na base de dados.");
									}
								}
							}

							JOptionPane.showMessageDialog(frame, "Frequências dos(s) arquivo(s) selecionados importadas com sucesso!",
									"Frequencia", JOptionPane.INFORMATION_MESSAGE);
							frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
							frame.refreshTableFrequencias(freqDAO.pesquisarFrequencias(idsTurmasParaRecarregarlistaFrequencia));
						} catch (FileNotFoundException e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
							frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
						} catch (IOException e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
							frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
						} catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
							frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
						} finally {
							if (null != writer)
								try {
									writer.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
						}
					}

				}));

		registerAction(frame.getBtnExportarCredenciamentos(),
				TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {

					public void action() {

						Writer writer = null;
						try {
							int DialogReturnValue = frame.getPastaExportacaoFrequencia().showSaveDialog(frame);

							if (DialogReturnValue != JFileChooser.APPROVE_OPTION) {
								return;
							}
							writer = new OutputStreamWriter(new FileOutputStream(new File(frame.getPastaExportacaoFrequencia()
									.getSelectedFile(), "ExportacaoCredenciamento - "
									+ new SimpleDateFormat("yyyyMMddHHmm").format(new Date(System.currentTimeMillis())) + ".csv")), Charset
									.forName("ISO-8859-1"));

							CSVWriter<String[]> csvWriter = CSVWriterBuilder.newDefaultWriter(writer);

							CredenciamentoDAO credDAO = new CredenciamentoDAOImpl(getPersistenceContext());
							List<Credenciamento> credenciamentos = credDAO.getAll();

							SimpleDateFormat formato = new SimpleDateFormat("yyyyMMddHHmmssS");
							for (Credenciamento c : credenciamentos) {

								String[] linhaCSV = new String[] { c.getParticipante().getId().toString(),
										c.getEvento().getId().toString(), formato.format(c.getHorarioEntrada().getTime()),
										formato.format(c.getDataCadastro().getTime()) };
								csvWriter.write(linhaCSV);
							}

							JOptionPane.showMessageDialog(frame, "Credenciados desta máquina EXPORTADOS com sucesso!", "Planilha",
									JOptionPane.INFORMATION_MESSAGE);
							frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
							frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
						} catch (IOException e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
							frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
						} catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
							frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
						} finally {
							if (null != writer)
								try {
									writer.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
						}

					}

				}));

		registerAction(frame.getBtnImportarCredenciamentos(),
				TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {

					public void action() {

						Writer writer = null;
						try {
							int dialogReturnValue = frame.getFilesImportacaoFrequencia().showOpenDialog(frame);

							if (dialogReturnValue != JFileChooser.APPROVE_OPTION) {
								return;
							}

							SimpleDateFormat formato = new SimpleDateFormat("yyyyMMddHHmmssS");
							CredenciamentoDAO credDAO = new CredenciamentoDAOImpl(getPersistenceContext());
							Credenciamento credenciamento = null;
							for (File file : frame.getFilesImportacaoFrequencia().getSelectedFiles()) {
								Reader reader = new InputStreamReader(new FileInputStream(file), "ISO-8859-1");
								List<String[]> data = null;
								try {
									CSVReader<String[]> csvParser = CSVReaderBuilder.newDefaultReader(reader);
									data = csvParser.readAll();
								} finally {
									reader.close();
								}

								for (String[] colums : data) {
									credenciamento = credDAO.recuperarCredenciamento(Integer.valueOf(colums[0]));
									if (null == credenciamento) {
										Calendar dataHorarioEntrada = new GregorianCalendar();
										dataHorarioEntrada.setTime(formato.parse(colums[2]));

										Calendar dataHoraCadastro = new GregorianCalendar();
										dataHoraCadastro.setTime(formato.parse(colums[3]));

										credenciamento = new Credenciamento();
										credenciamento.setParticipante(new Participante(Integer.valueOf(colums[0])));
										credenciamento.setEvento(new Evento(Integer.valueOf(colums[1])));
										credenciamento.setHorarioEntrada(dataHorarioEntrada);
										credenciamento.setDataCadastro(dataHoraCadastro.getTime());

										credDAO.save(credenciamento);
									} else {
										System.out.println("FREQUENCIA JA EXISTE -> " + Arrays.toString(colums));
									}
								}

							}

							JOptionPane.showMessageDialog(frame, "Credenciados dos(s) arquivo(s) selecionados IMPORTADOS com sucesso!",
									"Frequencia", JOptionPane.INFORMATION_MESSAGE);
							frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
							frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
						} catch (IOException e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
							frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
						} catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(frame, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
							frame.getLabelAguardeCarregandoDados().setVisible(Boolean.FALSE);
						} finally {
							if (null != writer)
								try {
									writer.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
						}
					}

					@Override
					public void posAction() {
						cleanUp();
					}

				}));

	}

	/**
	 * Com base no nome do Órgão, verifica se já está cadastro, senão, cadastra-o
	 * 
	 * @param nome
	 * @return
	 */
	private Orgao insereOuRecuperaOrgao(String nome, String sigla) {
		OrgaoDAO orgaoDAO = new OrgaoDAOImpl(getPersistenceContext());

		Orgao orgao = null;
		List<Orgao> listOrgao = orgaoDAO.pesquisarOrgaoByNomeEqual(nome);
		if (listOrgao != null && !listOrgao.isEmpty()) {
			orgao = listOrgao.get(0);
		} else {
			orgao = new Orgao();
			orgao.setFlgAtivo(true);
			orgao.setNomOrgao(nome);
			orgao.setSiglaOrgao(sigla);
			orgaoDAO.save(orgao);
		}

		return orgao;
	}

	private void refreshTableTurma() {
		TurmaDAO turmaDAO = new TurmaDAOImpl(getPersistenceContext());
		List<Turma> listaTurma = turmaDAO.getAll();
		frame.getLabelListaTurma().setText("Lista de Turmas:  " + listaTurma.size());
		frame.refreshTableTurmas(listaTurma);
	}

	private void refreshTablesParticipantes() {
		ParticipanteDAO dao = new ParticipanteDAOImpl(getPersistenceContext());

		List<Participante> listaParticipanteInscritos = dao.pesquisarParticipantesInscritos();
		frame.refreshTableInscritos(listaParticipanteInscritos);
		frame.getLabelParticipantesInscritos().setText("Participantes Inscritos:  " + listaParticipanteInscritos.size());

		List<Participante> listaParticipantesNovos = dao.pesquisarParticipantesNovos();
		frame.refreshTableNovosParticipantes(listaParticipantesNovos);
		frame.getLabelNovosParticipantes().setText("Novos Participantes:  " + listaParticipantesNovos.size());
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
		if (frame.getComboBoxOrgao().getItemCount() == 0) {
			OrgaoDAO orgaoDao = new OrgaoDAOImpl(getPersistenceContext());
			frame.setListaOrgao(orgaoDao.getAll());
		}
		if (frame.getComboBoxOrgaoFrequencia().getItemCount() == 0) {
			OrgaoDAO orgaoDao = new OrgaoDAOImpl(getPersistenceContext());
			frame.setListaOrgaoFrequencia(orgaoDao.getAll());
		}

		TurmaDAO turmaDAO = new TurmaDAOImpl(getPersistenceContext());
		List<Turma> listaTurmas = turmaDAO.getAll();

		if (frame.getComboBoxTurmaFrequencia().getItemCount() == 0) {
			frame.setListaTurmaFrequencia(turmaDAO.getAll());
		}
		if (frame.getComboBoxOrgaoCredenciamento().getItemCount() == 0) {
			OrgaoDAO orgaoDao = new OrgaoDAOImpl(getPersistenceContext());
			frame.setListaOrgaoCredenciamento(orgaoDao.getAll());
		}

		frame.setListaTurmaRelCredenciamento(listaTurmas);
		frame.setListaTurmaRelEtiqueta(listaTurmas);
		frame.setListaTurmaMapaFrequencia(listaTurmas);

	}

	/*
	 * Teste regex extração capacidade da sala public static void main(String[] args) {
	 * System.out.println("Sala 01 (35)".replaceAll("(?i)(.*\\()(.+?)(\\))", "$2")); }
	 */
}
