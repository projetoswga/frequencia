package br.com.frequencia.controller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JOptionPane;

import br.com.frequencia.action.AbstractAction;
import br.com.frequencia.action.BooleanExpression;
import br.com.frequencia.action.ConditionalAction;
import br.com.frequencia.action.TransactionalAction;
import br.com.frequencia.dao.FrequenciaDAO;
import br.com.frequencia.dao.OrgaoDAO;
import br.com.frequencia.dao.ParticipanteDAO;
import br.com.frequencia.dao.ParticipanteInscritoDAO;
import br.com.frequencia.dao.TurmaDAO;
import br.com.frequencia.dao.impl.FrequenciaDAOImpl;
import br.com.frequencia.dao.impl.OrgaoDAOImpl;
import br.com.frequencia.dao.impl.ParticipanteDAOImpl;
import br.com.frequencia.dao.impl.ParticipanteInscritoDAOImpl;
import br.com.frequencia.dao.impl.TurmaDAOImpl;
import br.com.frequencia.event.RegistrarFrequenciaEvent;
import br.com.frequencia.model.Frequencia;
import br.com.frequencia.model.Participante;
import br.com.frequencia.model.ParticipanteInscrito;
import br.com.frequencia.model.Turma;
import br.com.frequencia.ui.combo.ComboBoxItemModel;
import br.com.frequencia.ui.frame.PrincipalFrame;
import br.com.frequencia.util.PropertiesUtil;

/**
 * Define a <code>Controller</code> responsável por manipular a aba de <code>Frequencia</code>.
 * 
 * @see br.com.frequencia.controller.PersistenceController
 * 
 */
public class FrequenciaController extends PersistenceController {

	private PrincipalFrame frame;
	public static final String MSG_CONFIRMA_FECHAMENTO_FREQ_TURMA = "Isto irá fechar todas as Frequências que estiverem em aberto.  \n\n  Deseja realmente continuar?";
	public static final String MSG_VALIDACAO_TURMA = "É necessário selecionar uma turma antes de efetuar o registro da frequência!";
	public static final String MSG_VALIDACAO_INSCRICAO = "É necessário informar um número de inscrição antes de efetuar o registro da frequência!";
	public static final String MSG_REGISTRO_NAO_ENCONTRADO = "Participante não cadastrado, Favor realizar o cadastro na Aba Cadastramento!";
	public static final String MSG_ENTRADA = "Entrada registrada com sucesso!";
	public static final String MSG_SAIDA = "Saída registrada com sucesso!";
	public static final String MSG_SALA_LOTADA = "Não Existem vagas para a turma selecionada!";
	public static final String MSG_AGUARDAR_TOLERANCIA = "Favor aguardar limite de tolerância no atraso dos participantes inscritos!";
	private FrequenciaDAO dao;
	private ParticipanteDAO participanteDAO;
	private ParticipanteInscritoDAO participanteInscritoDAO;
	private TurmaDAO turmaDAO;
	private Turma turma;

	public FrequenciaController(AbstractController parent, final PrincipalFrame frame) {
		super(parent);
		loadPersistenceContext();
		frame.addWindowListener(this);
		this.frame = frame;
		frame.getLabelMensagemSucessoFrequencia().setVisible(Boolean.FALSE);

		registerAction(frame.getButtonRegistrarFrequencia(), ConditionalAction.build().addConditional(new BooleanExpression() {

			public boolean conditional() {
				frame.getLabelMensagemSucessoFrequencia().setVisible(Boolean.FALSE);
				if (((ComboBoxItemModel) frame.getComboBoxTurmaFrequencia().getSelectedItem()).getId() == 0) {
					JOptionPane.showMessageDialog(frame, MSG_VALIDACAO_TURMA, TITLE_ERROR, JOptionPane.ERROR_MESSAGE);
					return false;
				}
				if (frame.getTextFieldInscricaoFrequencia().getText() == null
						|| frame.getTextFieldInscricaoFrequencia().getText().isEmpty()) {
					JOptionPane.showMessageDialog(frame, MSG_VALIDACAO_INSCRICAO, TITLE_ERROR, JOptionPane.ERROR_MESSAGE);
					return false;
				} else {
					participanteDAO = new ParticipanteDAOImpl(getPersistenceContext());
					if (!participanteDAO.pesquisarNumInscricao(frame.getTextFieldInscricaoFrequencia().getText().trim())) {
						JOptionPane.showMessageDialog(frame, MSG_REGISTRO_NAO_ENCONTRADO, TITLE_ERROR, JOptionPane.ERROR_MESSAGE);
						return false;
					}
				}
				return true;
			}
		}).addAction(TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {

			private Frequencia frequencia;
			private Participante participante;
			private ParticipanteInscrito participanteInscrito;
			private List<Frequencia> listaFrequencias;
			private Integer quantidadeRestanteVagas = 0;
			private Integer quantidadeRestanteVagasMenosInscritos = 0;
			private List<Frequencia> listaFrequenciaTurma;
			private List<ParticipanteInscrito> listaParticipanteInscritosTurma;
			private String tolerancia;
			private Calendar calendarTolerancia;
			private List<Frequencia> listaFrequenciaParticipanteTurma;

			@SuppressWarnings("static-access")
			@Override
			protected void action() {
				frequencia = null;
				dao = new FrequenciaDAOImpl(getPersistenceContext());
				turmaDAO = new TurmaDAOImpl(getPersistenceContext());
				participanteInscritoDAO = new ParticipanteInscritoDAOImpl(getPersistenceContext());
				participanteDAO = new ParticipanteDAOImpl(getPersistenceContext());

				recuperarInformacoes();

				if (listaParticipanteInscritosTurma != null && !listaParticipanteInscritosTurma.isEmpty()) {
					if (listaFrequenciaTurma == null) {
						listaFrequenciaTurma = new ArrayList<Frequencia>();
					}

					/**
					 * Quando não exite mais vagas
					 */
					if (quantidadeRestanteVagas == 0) {
						if (participanteInscrito != null && listaFrequenciaParticipanteTurma != null
								&& !listaFrequenciaParticipanteTurma.isEmpty()) {
							verificarEntradaSaida(Boolean.FALSE);
						} else if (participante != null && listaFrequenciaParticipanteTurma != null
								&& !listaFrequenciaParticipanteTurma.isEmpty()) {
							verificarEntradaSaida(Boolean.TRUE);
						} else {
							JOptionPane.showMessageDialog(frame, MSG_SALA_LOTADA, TITLE_ERROR, JOptionPane.ERROR_MESSAGE);
							return;
						}

					} else if (quantidadeRestanteVagas > 0) {
						boolean contem = Boolean.FALSE;
						if (listaParticipanteInscritosTurma != null && participanteInscrito != null) {
							for (ParticipanteInscrito pi : listaParticipanteInscritosTurma) {
								if (pi.getParticipante().getId() == participanteInscrito.getParticipante().getId()) {
									contem = Boolean.TRUE;
									break;
								}
							}
						}
						if (participanteInscrito != null && contem) {
							/**
							 * Quando for o primeiro registro, efetua-se o registro normalmente, do contrario, verifica se é entrada ou
							 * saida pelas variáveis horarioEntrada e horarioSaida da entiade Frequencia
							 */
							if (listaFrequencias == null || listaFrequencias.isEmpty()) {
								carregarFrequenciaComParticipanteInscrito(participanteInscrito);
							} else {
								verificarEntradaSaida(Boolean.FALSE);
							}
						} else {
							/**
							 * Caso ainda exitam vagas mesmo com os participantes já inscritos, o registro é efetuado sem mais verificações.
							 * Do contrario, é verificado o tempo máximo de tolerancia no atraso do participante inscritos de acordo com a
							 * informação obtida pelo arquivo configuracao.properties
							 */
							Calendar atual = Calendar.getInstance();
							if (quantidadeRestanteVagasMenosInscritos > 0) {
								if (listaFrequencias == null || listaFrequencias.isEmpty()) {
									carregarFrequenciaComParticipanteNovo(participante);
								} else {
									verificarEntradaSaida(Boolean.TRUE);
								}
							} else if (calendarTolerancia.get(calendarTolerancia.HOUR) <= atual.get(atual.HOUR)
									&& calendarTolerancia.get(calendarTolerancia.MINUTE) < atual.get(atual.MINUTE)) {
								if (listaFrequencias == null || listaFrequencias.isEmpty()) {
									carregarFrequenciaComParticipanteNovo(participante);
								} else {
									verificarEntradaSaida(Boolean.TRUE);
								}
							} else {
								JOptionPane.showMessageDialog(frame, MSG_AGUARDAR_TOLERANCIA, TITLE_ERROR, JOptionPane.ERROR_MESSAGE);
							}
						}
					}
					/**
					 * Caso não haja nenhum participante inscrito.
					 */
				} else {
					if (quantidadeRestanteVagas > 0) {
						if (listaFrequencias == null || listaFrequencias.isEmpty()) {
							carregarFrequenciaComParticipanteNovo(participante);
						} else {
							verificarEntradaSaida(Boolean.TRUE);
						}
					}
				}
				if (frequencia != null) {
					/*
					Calendar he = frequencia.getHorarioEntrada();
					frequencia.setHorarioEntrada(new GregorianCalendar(he.get(Calendar.YEAR),he.get(Calendar.MONTH),he.get(Calendar.DAY_OF_MONTH),he.get(Calendar.HOUR_OF_DAY),he.get(Calendar.MINUTE),he.get(Calendar.SECOND)));

					Calendar hs = frequencia.getHorarioEntrada();
					frequencia.setHorarioEntrada(new GregorianCalendar(hs.get(Calendar.YEAR),hs.get(Calendar.MONTH),hs.get(Calendar.DAY_OF_MONTH),hs.get(Calendar.HOUR_OF_DAY),hs.get(Calendar.MINUTE),hs.get(Calendar.SECOND)));
					 * 
					 */

					dao.save(frequencia);
					quantidadeRestanteVagas -= 1;
					participante = null;
					participanteInscrito = null;
					listaParticipanteInscritosTurma = null;
				}
			}

			/**
			 * Método responsável por recuperar as informações necessárias para a realização do registro de frequência.
			 * <ul>
			 * <li>Recupera a Turma de acordo com a opção escolhida pelo usuário na tela. Atributo: <code>turma</code></li>
			 * <li>Recupera o participante pelo número de inscrição informado pelo usuário. Atributo: <code>participanteInscrito</code></li>
			 * <li>Recupera os participantes inscritos na turma selecionada. Atributo: <code>listaParticipanteInscritosTurma</code></li>
			 * <li>Recupera as frequencias do participante informado. Atributo: <code>listaFrequencias</code></li>
			 * <li>Recupera os registros de frequencia de toda a turma. Atributo: <code>listaFrequenciaTurma</code></li>
			 * <li>Recuperando o participante novo. Atributo: <code>participante</code></li>
			 * </ul>
			 */
			@SuppressWarnings("static-access")
			private void recuperarInformacoes() {
				turma = turmaDAO.findById(((ComboBoxItemModel) frame.getComboBoxTurmaFrequencia().getSelectedItem()).getId());
				List<ParticipanteInscrito> list = participanteInscritoDAO.pesquisarParticipanteInscritos(frame
						.getTextFieldInscricaoFrequencia().getText());
				if (list != null && !list.isEmpty()) {
					participanteInscrito = list.get(0);
				}
				listaParticipanteInscritosTurma = participanteInscritoDAO.pesquisarParticipanteInscritosPorTurma(turma.getId());
				listaFrequencias = dao.pesquisarFrequencias(frame.getTextFieldInscricaoFrequencia().getText());
				listaFrequenciaTurma = dao.pesquisarFrequencias(turma.getId());
				listaFrequenciaParticipanteTurma = dao.pesquisarFrequenciasParticipanteTurma(frame.getTextFieldInscricaoFrequencia()
						.getText(), turma.getId());
				participante = participanteDAO.pesquisarParticipantes(frame.getTextFieldInscricaoFrequencia().getText());
				tolerancia = PropertiesUtil.getProperty("tolerancia");
				calendarTolerancia = calendarTolerancia.getInstance();

				// Para teste de tolerancia remover depois
				// HorarioDAO horarioDao = new HorarioDAOImpl(getPersistenceContext());
				// turma.getHorario().getHrInicial().setTime(new Date());
				// turma.getHorario().getHrFinal().setTime(new Date());
				// horarioDao.save(turma.getHorario());

				calendarTolerancia.setTime(turma.getHorario().getHrInicial().getTime());
				calendarTolerancia.add(Calendar.HOUR, new Integer(tolerancia.split("\\:")[0]));
				calendarTolerancia.add(Calendar.MINUTE, new Integer(tolerancia.split("\\:")[1]));

				if (listaFrequenciaTurma == null) {
					quantidadeRestanteVagas = turma.getSala().getNumCapacidade();
				} else {
					quantidadeRestanteVagas = turma.getSala().getNumCapacidade() - listaFrequenciaTurma.size();
				}

				if (listaParticipanteInscritosTurma == null) {
					quantidadeRestanteVagasMenosInscritos = turma.getSala().getNumCapacidade();
				} else {
					quantidadeRestanteVagasMenosInscritos = turma.getSala().getNumCapacidade() - listaParticipanteInscritosTurma.size();
				}
			}

			private void verificarEntradaSaida(boolean participanteNovo) {
				frequencia = listaFrequencias.get(listaFrequencias.size() - 1);
				if (frequencia.getHorarioEntrada() != null && frequencia.getHorarioSaida() == null) {
					if (!frequencia.getTurma().getId().equals(turma.getId())) {
						JOptionPane.showMessageDialog(frame, "É necessário registrar a saída do participante na turma "
								+ frequencia.getTurma().getNomTurma(), TITLE_ERROR, JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				frame.getLabelMensagemSucessoFrequencia().setForeground(Color.BLUE);
				if (frequencia.getHorarioEntrada() == null) {
					frequencia.setHorarioEntrada(Calendar.getInstance());
					frame.getLabelMensagemSucessoFrequencia().setText(MSG_ENTRADA);
				} else if (frequencia.getHorarioSaida() == null) {
					frequencia.setHorarioSaida(Calendar.getInstance());
					frame.getLabelMensagemSucessoFrequencia().setText(MSG_SAIDA);
					frame.getLabelMensagemSucessoFrequencia().setForeground(Color.RED);
				} else {
					if (participanteNovo) {
						carregarFrequenciaComParticipanteNovo(participante);
					} else {
						carregarFrequenciaComParticipanteInscrito(participanteInscrito);
					}
				}
				frame.getLabelMensagemSucessoFrequencia().setVisible(Boolean.TRUE);
			}

			private void carregarFrequenciaComParticipanteNovo(Participante participante) {
				frequencia = new Frequencia();
				frequencia.setHorarioEntrada(Calendar.getInstance());
				frequencia.setParticipante(participante);
				frequencia.setTurma(turma);
				frame.getLabelMensagemSucessoFrequencia().setText(MSG_ENTRADA);
				frame.getLabelMensagemSucessoFrequencia().setVisible(Boolean.TRUE);
			}

			private void carregarFrequenciaComParticipanteInscrito(ParticipanteInscrito participanteInscrito) {
				frequencia = new Frequencia();
				frequencia.setHorarioEntrada(Calendar.getInstance());
				frequencia.setParticipante(participanteInscrito.getParticipante());
				frequencia.setTurma(turma);
				frame.getLabelMensagemSucessoFrequencia().setText(MSG_ENTRADA);
				frame.getLabelMensagemSucessoFrequencia().setVisible(Boolean.TRUE);
			}

			@Override
			public void posAction() {
				cleanUp();
				refreshTables();
				fireEvent(new RegistrarFrequenciaEvent(listaFrequencias));
				frequencia = null;
			}
		})));
		
		registerAction(frame.getButtonFinalizarFrequenciaTurma(), ConditionalAction.build().addConditional(new BooleanExpression() {

			public boolean conditional() {
				if (((ComboBoxItemModel) frame.getComboBoxTurmaFrequencia().getSelectedItem()).getId() != 0) {
					int confirm = JOptionPane.showConfirmDialog(frame, MSG_CONFIRMA_FECHAMENTO_FREQ_TURMA, TITLE_CONFIRMACAO, JOptionPane.YES_NO_OPTION);
					return confirm == JOptionPane.YES_OPTION;
				}
				return false;
			}
		}).addAction(TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {

			@Override
			protected void action() {
				FrequenciaDAO freqDAO = new FrequenciaDAOImpl(getPersistenceContext());
				List<Frequencia> frequencias = freqDAO.pesquisarFrequenciasAbertas(((ComboBoxItemModel) frame.getComboBoxTurmaFrequencia().getSelectedItem()).getId());
				for (Frequencia f : frequencias) {
					System.out.println("Fechando frequencia: " + f.getId());
					f.setHorarioSaida(Calendar.getInstance());
					freqDAO.save(f);
				}
				JOptionPane.showMessageDialog(frame, "Frequencias da Turma " + ((ComboBoxItemModel) frame.getComboBoxTurmaFrequencia().getSelectedItem()).getDescricao() + " fechadas com sucesso!",TITLE_INFORMACAO, JOptionPane.INFORMATION_MESSAGE);
			}
			
			@Override
			public void posAction() {
				cleanUp();
				refreshTables();
			}
		})));
		

		registerAction(frame.getButtonLimparFrequencia(), new AbstractAction() {
			public void action() {
				Integer idComboBoxTurmaFrequencia = ((ComboBoxItemModel) frame.getComboBoxTurmaFrequencia().getSelectedItem()).getId();
				if (idComboBoxTurmaFrequencia != 0) {
					turma = turmaDAO.findById(idComboBoxTurmaFrequencia);
					frame.getLabelMensagemSucessoFrequencia().setVisible(Boolean.FALSE);
				}
				refreshTables();
				cleanUp();
			}
		});
		
		registerAction(frame.getButtonIntervaloTurma(), TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {
			public void action() {
				Integer idComboBoxTurmaFrequencia = ((ComboBoxItemModel) frame.getComboBoxTurmaFrequencia().getSelectedItem()).getId();
				if (idComboBoxTurmaFrequencia != 0) {
					turma = turmaDAO.findById(idComboBoxTurmaFrequencia);
					/** @TODO COLUNA DESCONTINUADA */
					//turma.setInicioIntervalo(Calendar.getInstance());
					
					turmaDAO.save(turma);

					frame.getButtonIntervaloTurma().setVisible(false);
					frame.getButtonFimIntervaloTurma().setVisible(true);
					JOptionPane.showMessageDialog(frame, "Intervalo da Turma " + ((ComboBoxItemModel) frame.getComboBoxTurmaFrequencia().getSelectedItem()).getDescricao() + " INICIADO com sucesso!",TITLE_INFORMACAO, JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}));
		
		registerAction(frame.getButtonFimIntervaloTurma(), TransactionalAction.build().persistenceCtxOwner(this).addAction(new AbstractAction() {
			public void action() {
				Integer idComboBoxTurmaFrequencia = ((ComboBoxItemModel) frame.getComboBoxTurmaFrequencia().getSelectedItem()).getId();
				if (idComboBoxTurmaFrequencia != 0) {
					turma = turmaDAO.findById(idComboBoxTurmaFrequencia);
					/** @TODO COLUNA DESCONTINUADA */
					//turma.setFimIntervalo(Calendar.getInstance());
					
					turmaDAO.save(turma);

					frame.getButtonFimIntervaloTurma().setVisible(false);
					JOptionPane.showMessageDialog(frame, "Intervalo da Turma " + ((ComboBoxItemModel) frame.getComboBoxTurmaFrequencia().getSelectedItem()).getDescricao() + " FINALIZADO com sucesso!",TITLE_INFORMACAO, JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}));

		refreshTables();
		carregarCombos();
	}

	private void refreshTables() {
		Integer quantidadeVagas = 0;
		dao = new FrequenciaDAOImpl(getPersistenceContext());
		turmaDAO = new TurmaDAOImpl(getPersistenceContext());
		List<Frequencia> list = new ArrayList<Frequencia>();
		if (turma != null) {
			list = dao.pesquisarFrequencias(turma.getId());
			if (list != null) {
				for (Frequencia frequencia : list) {
					turmaDAO.findById(frequencia.getTurma().getId());
					//quantidadeVagas = frequencia.getTurma().getSala().getNumCapacidade();
				}
				frame.refreshTableFrequencias(list);
				frame.getLabelListaParticipantesFrequencia().setVisible(Boolean.TRUE);
				frame.getLabelVagas().setVisible(Boolean.TRUE);
				frame.getLabelListaParticipantesFrequencia().setText("Participantes com registro de frequência:  " + list.size());
				List<ParticipanteInscrito> listaParticipanteInscritosTurma = participanteInscritoDAO.pesquisarParticipanteInscritosPorTurma(turma.getId());
				Integer vagasRestantes = 0;
				if (listaParticipanteInscritosTurma != null){
					quantidadeVagas = turma.getSala().getNumCapacidade() - listaParticipanteInscritosTurma.size();
					vagasRestantes = list.size() - listaParticipanteInscritosTurma.size();
					if (vagasRestantes > 0){
						quantidadeVagas -= vagasRestantes;
					}
				}
				frame.getLabelVagas().setText("Vagas não preenchidas:  " + (quantidadeVagas));
				frame.getTextFieldInscricaoFrequencia().grabFocus();
			}
		} else {
			// list = dao.pesquisarFrequencias();
			frame.getLabelListaParticipantesFrequencia().setVisible(Boolean.FALSE);
			frame.getLabelVagas().setVisible(Boolean.FALSE);
		}
	}

	private void carregarCombos() {
		if (frame.getComboBoxOrgaoFrequencia().getItemCount() == 0) {
			OrgaoDAO orgaoDao = new OrgaoDAOImpl(getPersistenceContext());
			frame.setListaOrgaoFrequencia(orgaoDao.getAll());
		}
		if (frame.getComboBoxTurmaFrequencia().getItemCount() == 0) {
			TurmaDAO turmaDAO = new TurmaDAOImpl(getPersistenceContext());
			frame.setListaTurmaFrequencia(turmaDAO.getAll());
		}
	}

	public void loadPersistenceContext() {
		loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
	}

	@Override
	protected void cleanUp() {
		frame.resetAbaFrequencia();
		super.cleanUp();
	}

}
