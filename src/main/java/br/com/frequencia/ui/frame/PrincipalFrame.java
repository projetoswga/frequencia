package br.com.frequencia.ui.frame;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.MaskFormatter;

import br.com.frequencia.controller.PersistenceController;
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
import br.com.frequencia.ui.combo.AutoCompletion;
import br.com.frequencia.ui.combo.ComboBoxItemModel;
import br.com.frequencia.ui.table.CredenciamentoTable;
import br.com.frequencia.ui.table.FrequenciaTable;
import br.com.frequencia.ui.table.NovosParticipantesTable;
import br.com.frequencia.ui.table.ParticipanteInscritoTable;
import br.com.frequencia.ui.table.TurmaTable;
import br.com.frequencia.util.DateUtil;
import br.com.frequencia.util.WaitPreview;

public class PrincipalFrame extends JFrame {

	private static final long serialVersionUID = 8542162005600227482L;
	private JTabbedPane addAbas = new JTabbedPane(JTabbedPane.TOP);
	private Container abaFrequencia = new Container();
	private Container abaTurma = new Container();
	private Container abaImportaExportar = new Container();
	private Container abaCadastramento = new Container();
	private Container abaCredenciamento = new Container();
	private Container abaRelatorios = new Container();
	private Container abaEspelhoFrequencia = new Container();
	private JPanel panelInformacoesTurma = new JPanel();
	private JPanel panelHorarioTurma = new JPanel();
	private JPanel panelSalaTurma = new JPanel();
	private JCheckBox checkBoxSalaAtivoTurma = new JCheckBox("Ativa?");
	private JCheckBox chckbxApenasNovosParticipantes = new JCheckBox("Apenas novos Participantes?");
	private JTextField textFieldNomeTurma = new JTextField();
	private JTextField textFieldInscricao = new JTextField();
	private JTextField textFieldNomParticipante = new JTextField();
	private JTextField textFieldNomeCredenciamento = new JTextField();
	private JTextField textFieldInscricaoCredenciamento;
	private JTextField textFieldDescricaoHorarioTurma = new JTextField();
	private JTextField textFieldHorarioInicialTurma = new JTextField();
	private JTextField textFieldHorarioFinal = new JTextField();
	private JTextField textFieldNomeSalaTurma = new JTextField();
	private JTextField textFieldInscricaoFrequencia;
	private JTextField textFieldNomeFrequencia = new JTextField();
	private JButton buttonSalvarTurma = new JButton("Salvar");
	private JButton buttonLimparTurma = new JButton("Limpar");
	private JButton buttonPesquisarInscritos = new JButton("Pesquisar Inscritos");
	private JButton buttonNovoParticipante = new JButton("Novo");
	private JButton buttonPesquisarNovosParticipantes = new JButton("Pesquisar Novos");
	private JButton buttonLimparAbaInscricao = new JButton("Limpar");
	private JButton buttonLimparCredenciamento = new JButton("Limpar");
	private JButton buttonRegistrarCredenciamento = new JButton("Credenciar");
	private JButton buttonRegistrarFrequencia = new JButton("Registrar");
	private JButton buttonFinalizarFrequenciaTurma = new JButton("Fechar Frequência Turma");
	private JButton buttonLimparFrequencia = new JButton("Limpar");
	private JButton buttonGerarArquivo = new JButton("Gerar Arquivo");
	private JButton buttonImportarCSV_Turma_CFC = new JButton("CSV - Turmas", createImageIcon("/imagens/document-excel-icon.png"));
	private JButton buttonImportarCSV_Inscrito_CFC = new JButton("CSV - Participantes",
			createImageIcon("/imagens/document-excel-icon.png"));
	private JButton buttonImportarCSV_Evento_CFC = new JButton("CSV - Evento",
			createImageIcon("/imagens/document-excel-icon.png"));
	private JButton buttonImportar = new JButton("Importar Novos Particip.", createImageIcon("/imagens/file-import-icon.png"));
	private JButton buttonExportar = new JButton("Exportar Novos Particip.", createImageIcon("/imagens/file-export-icon.png"));
	private JButton buttonGerarRelCredenciamento = new JButton("Gerar Relatório", createImageIcon("/imagens/document-sign-icon.png"));
	private JButton buttonGerarRelEtiquietas = new JButton("Gerar Etiquetas", createImageIcon("/imagens/bar-code-icon.png"));
	private JButton buttonGerarRelCracha = new JButton("Gerar Crachás", createImageIcon("/imagens/bar-code-icon.png"));
	private JButton buttonGerarMapaFrequencia = new JButton("Gerar Mapa Frequência", createImageIcon("/imagens/Ok-icon.png"));
	private JButton buttonExportarFrequencia = new JButton("Exportar Frequência", createImageIcon("/imagens/export-freq-icon.png"));
	private JButton buttonImportarFrequencia = new JButton("Importar Frequência", createImageIcon("/imagens/import-freq-icon.png"));
	private JButton btnImportarCredenciamentos = new JButton("Importar Credenciamentos", createImageIcon("/imagens/import-icon.png"));
	private JButton btnExportarCredenciamentos = new JButton("Exportar Credenciamentos", createImageIcon("/imagens/export-icon.png"));
	private JButton buttonIntervaloTurma = new JButton("Iniciar Intervalo");
	private JButton buttonFimIntervaloTurma = new JButton("Finalizar Intervalo"); //Precisa ser dois botões, por que o lintener é agregado ao TEXT do button :(
	private JFileChooser filePlanilhaTurmaCSV = new JFileChooser();
	private JFileChooser filePlanilhaInscritosCSV = new JFileChooser();
	private JFileChooser filePlanilhaCrachaOrgaoCSV = new JFileChooser();
	private JFileChooser fileImportacaoSQL = new JFileChooser();
	private JFileChooser fileExportacaoSQL = new JFileChooser();
	private JFileChooser pastaExportacaoFrequencia = new JFileChooser();
	private JFileChooser filesImportacaoFrequencia = new JFileChooser();
	private ParticipanteInscritoTable listaParticipanteInscrito = new ParticipanteInscritoTable();
	private NovosParticipantesTable ListaNovosParticipantes = new NovosParticipantesTable();
	private CredenciamentoTable listaCredenciamento = new CredenciamentoTable();
	private FrequenciaTable listaFrequencia = new FrequenciaTable();
	private TurmaTable listaTurma = new TurmaTable();
	private JScrollPane scrollPaneParticipanteInscrito = new JScrollPane();
	private JComboBox comboBoxOrgao = new JComboBox();
	private JComboBox comboBoxOrgaoCredenciamento = new JComboBox();
	private JComboBox comboBoxOrgaoFrequencia = new JComboBox();
	private JComboBox comboBoxTurmaFrequencia = new JComboBox();
	private JComboBox comboBoxSalaTurma = new JComboBox();
	private JComboBox comboBoxEventoTurma = new JComboBox();
	private JComboBox comboBoxHorarioTurma = new JComboBox();
	private JComboBox comboBoxInstrutorTurma = new JComboBox();
	private JComboBox comboBoxTurmaRelCredenciamento = new JComboBox();
	private JComboBox comboBoxTurmaRelEtiquetas = new JComboBox();
	private JComboBox comboBoxTurmaMapaFrequencia = new JComboBox();
	private JCheckBox chckbxExibirMinutos;
	private List<Orgao> listaOrgao = new ArrayList<Orgao>();
	private List<Orgao> listaOrgaoCredenciamento = new ArrayList<Orgao>();
	private List<Orgao> listaOrgaoFrequencia = new ArrayList<Orgao>();
	private List<Turma> listaTurmaFrequencia = new ArrayList<Turma>();
	private List<Evento> listaEvento = new ArrayList<Evento>();
	private List<Sala> listaSala = new ArrayList<Sala>();
	private List<Horario> listaHorario = new ArrayList<Horario>();
	private List<Instrutor> listaInstrutor = new ArrayList<Instrutor>();
	private JLabel labelListaCredenciados = new JLabel("Lista de Participantes Credenciados");
	private JLabel labelParticipantesInscritos = new JLabel("Participantes Inscritos");
	private JLabel labelNovosParticipantes = new JLabel("Novos Participantes");
	private JLabel labelListaParticipantesFrequencia = new JLabel("Participantes com registro de frequência:");
	private JLabel labelVagasRestantes = new JLabel("Vagas não preenchidas:");
	private JLabel labelConteudoNomeTurma = new JLabel("Sem Informação.");
	private JLabel labelConteudoNomeSala = new JLabel("Sem Informação.");
	private JLabel labelConteudoCapacidade = new JLabel("Sem Informação.");
	private JLabel labelConteudoHorario = new JLabel("Sem Informação.");
	private JLabel labelConteudoInstrutor = new JLabel("Sem Informação.");
	private JLabel labelConteudoConteudo = new JLabel("Sem Informação.");
	private JLabel labelHorarioInicialTurma = new JLabel("Horário Inicial");
	private JLabel labelConteudoInscritosTurmaFrequencia = new JLabel("Sem Informação.");
	private JLabel labelHorarioFinalTurma = new JLabel("Horário Final");
	private JLabel labelNomeSalaTurma = new JLabel("Nome");
	private JLabel labelCapacidadeSalaTurma = new JLabel("Capacidade");
	private JLabel labelNomeTurma = new JLabel("Nome da Turma");
	private JPopupMenu jPopupMenuListaCredenciamento = new JPopupMenu();
	private JPopupMenu jPopupMenuListaNovosParticipantes = new JPopupMenu();
	private JPopupMenu jPopupMenuListaParticipantesInscritos = new JPopupMenu();
	private JPopupMenu jPopupMenuListaTurmas = new JPopupMenu();
	private JMenuItem jMenuItemExcluirTurma = new JMenuItem();
	private JMenuItem jMenuItemEditarTurma = new JMenuItem();
	private JMenuItem jMenuItemExcluirCredenciamento = new JMenuItem();
	private JMenuItem jMenuItemExcluirNovoParticipante = new JMenuItem();
	private JMenuItem jMenuItemEditarNovoParticipante = new JMenuItem();
	private JMenuItem jMenuItemEditarParticipantesInscritos = new JMenuItem();
	private Participante participanteCredenciamento;
	private ParticipanteInscrito participanteInscrito;
	private Participante participante;
	private Turma turma;
	private Frequencia frequencia;
	private JScrollPane scrollPaneTableTurma = new JScrollPane();
	private JFormattedTextField textFieldCapacidadeSalaTurma;
	private JLabel labelListaTurma = new JLabel("Lista de Turmas:");
	private JTextField textFieldNomeDisciplina = new JTextField();
	private JLabel labelMensagemSucessoFrequencia = new JLabel("Operação realizada com sucesso!");
	private JLabel labelMensagemSucessoCredenciamento = new JLabel("Operação realizada com sucesso!");
	private JLabel labelAguardeCarregandoDados = new JLabel("Aguarde, Carregando dados....");
	private JTextField textFieldInscricaoRelEtiqueta;
	private JTextField textFieldNomeRelEtiqueta;
	private JButton buttonGerarPlanilhaCrach = new JButton("Gerar Planilha Crachá");
	
	private WaitPreview waitPreviewRelatorios; 
	private JTextField textFieldInscricaoEspelho;
	private JButton buttonCarregarEspelhoFrequencia = new JButton("Carregar Frequência");
	private final JTextArea textAreaEspelhoFrequencia = new JTextArea();
	private final JButton btnCopiarExtrato = new JButton("Copiar Extrato");

	
	public JButton getButtonCarregarEspelhoFrequencia() {
		return buttonCarregarEspelhoFrequencia;
	}

	public PrincipalFrame() {
		setTitle("Registro de Frequência dos Participantes");
		addAbas = new JTabbedPane(JTabbedPane.TOP);

		criarContainerFrequencia();
		criarContainerTurma();
		criarContainerCadastramento();
		criarContainerCredenciamento();
		criarContainerImportarExportar();
		criarContainerRelatorios();
		criarContainerEspelhoFrequencia();

		addAbas.addTab("Frequência", abaFrequencia);
		addAbas.addTab("Turma", abaTurma);
		addAbas.addTab("Cadastramento", abaCadastramento);
		addAbas.addTab("Credenciamento", abaCredenciamento);
		addAbas.addTab("Importar / Exportar", abaImportaExportar);
		addAbas.addTab("Relatórios", abaRelatorios);
		addAbas.addTab("Espelho Frequência", abaEspelhoFrequencia);
		btnCopiarExtrato.setBounds(23, 554, 180, 23);
		
		abaEspelhoFrequencia.add(btnCopiarExtrato);
		getContentPane().add(addAbas);

		setIconImage(getToolkit().createImage(getClass().getResource("/imagens/icon_frequencia.png")));
		setResizable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 650);
		setLocationRelativeTo(null);
	}

	private void criarContainerEspelhoFrequencia() {
		JLabel label = new JLabel("Inscrição");
		label.setFont(new Font("Tahoma", Font.BOLD, 11));
		label.setBounds(23, 22, 105, 14);
		abaEspelhoFrequencia.add(label);
		
		abaEspelhoFrequencia.add(getTextFieldInscricaoEspelho());
		
		JLabel lblFrequncias = new JLabel("Espelho da Frequência (Extrato):");
		lblFrequncias.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblFrequncias.setBounds(23, 83, 246, 14);
		abaEspelhoFrequencia.add(lblFrequncias);

		JScrollPane scrollBar = new JScrollPane();
		scrollBar.setViewportBorder(null);
		scrollBar.setBounds(23, 100, 741, 443);
		abaEspelhoFrequencia.add(scrollBar);
		textAreaEspelhoFrequencia.setForeground(new Color(0, 100, 0));
		textAreaEspelhoFrequencia.setFont(new Font("Courier New", Font.PLAIN, 12));
		textAreaEspelhoFrequencia.setEditable(false);
		textAreaEspelhoFrequencia.setBackground(SystemColor.control);
		scrollBar.setViewportView(textAreaEspelhoFrequencia);

		buttonCarregarEspelhoFrequencia.setBounds(211, 35, 203, 23);
		abaEspelhoFrequencia.add(buttonCarregarEspelhoFrequencia);

	}

	private void criarContainerImportarExportar() {

		filePlanilhaTurmaCSV.setFileSelectionMode(JFileChooser.FILES_ONLY);
		filePlanilhaTurmaCSV.setFileFilter(new FileNameExtensionFilter("Arquivos CSV (*.csv)", "csv"));

		filePlanilhaInscritosCSV.setFileSelectionMode(JFileChooser.FILES_ONLY);
		filePlanilhaInscritosCSV.setFileFilter(new FileNameExtensionFilter("Arquivos CSV (*.csv)", "csv"));

		filePlanilhaCrachaOrgaoCSV.setFileSelectionMode(JFileChooser.FILES_ONLY);
		filePlanilhaCrachaOrgaoCSV.setFileFilter(new FileNameExtensionFilter("Arquivos CSV (*.csv)", "csv"));

		buttonImportarCSV_Turma_CFC.setBounds(235, 24, 191, 41);
		abaImportaExportar.add(buttonImportarCSV_Turma_CFC);

		buttonImportarCSV_Inscrito_CFC.setBounds(447, 24, 200, 41);
		abaImportaExportar.add(buttonImportarCSV_Inscrito_CFC);

		buttonImportar.setBounds(235, 136, 200, 41);
		abaImportaExportar.add(buttonImportar);

		buttonExportar.setBounds(22, 136, 191, 41);
		abaImportaExportar.add(buttonExportar);

		fileImportacaoSQL.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileImportacaoSQL.setFileFilter(new FileNameExtensionFilter("Arquivos SQL(*.sql)", "sql"));

		fileExportacaoSQL.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileExportacaoSQL.setFileFilter(new FileNameExtensionFilter("Arquivos SQL(*.sql)", "sql"));
		labelAguardeCarregandoDados.setForeground(Color.BLUE);

		labelAguardeCarregandoDados.setFont(new Font("Tahoma", Font.BOLD, 13));
		labelAguardeCarregandoDados.setBounds(292, 476, 264, 23);
		abaImportaExportar.add(labelAguardeCarregandoDados);

		buttonImportarFrequencia.setBounds(235, 370, 200, 41);
		abaImportaExportar.add(buttonImportarFrequencia);

		buttonExportarFrequencia.setBounds(22, 370, 191, 41);
		abaImportaExportar.add(buttonExportarFrequencia);

		JLabel labelImmportarFrequencia = new JLabel("Dados de Frequência:");
		labelImmportarFrequencia.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelImmportarFrequencia.setBounds(22, 355, 413, 14);
		abaImportaExportar.add(labelImmportarFrequencia);
		JLabel labelImportarDadosCFC = new JLabel("Importar dados do SISFIE (Arquivos em formato CSV):");
		labelImportarDadosCFC.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelImportarDadosCFC.setBounds(22, 11, 296, 14);
		abaImportaExportar.add(labelImportarDadosCFC);

		JLabel labelImportarDados = new JLabel("Dados de Novos Participantes:");
		labelImportarDados.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelImportarDados.setBounds(22, 123, 413, 14);
		abaImportaExportar.add(labelImportarDados);
		buttonImportarCSV_Evento_CFC.setBounds(22, 24, 191, 41);

		abaImportaExportar.add(buttonImportarCSV_Evento_CFC);

		buttonGerarPlanilhaCrach.setBounds(616, 145, 135, 23);
		buttonGerarPlanilhaCrach.setVisible(false);

		abaImportaExportar.add(buttonGerarPlanilhaCrach);

		filesImportacaoFrequencia.setFileSelectionMode(JFileChooser.FILES_ONLY);
		filesImportacaoFrequencia.setMultiSelectionEnabled(true);
		filesImportacaoFrequencia.setFileFilter(new FileNameExtensionFilter("Arquivos CSV (*.csv)", "csv"));

		pastaExportacaoFrequencia.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
		
		
		JLabel lblDadosDeCredenciados = new JLabel("Dados de Credenciados:");
		lblDadosDeCredenciados.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDadosDeCredenciados.setBounds(22, 237, 413, 14);
		abaImportaExportar.add(lblDadosDeCredenciados);
		
		btnExportarCredenciamentos.setBounds(22, 252, 191, 41);
		abaImportaExportar.add(btnExportarCredenciamentos);
		
		btnImportarCredenciamentos.setBounds(235, 252, 200, 41);
		abaImportaExportar.add(btnImportarCredenciamentos);
		
		JLabel lblDosNotebooks = new JLabel("Dos Notebooks");
		JLabel lblMquinaCentral = new JLabel("Na Máquina Central");
		JLabel lblDaMquinaCentral = new JLabel("Da Máquina Central");
		JLabel lblNosNotebooks = new JLabel("Nos Notebooks");
		JLabel label_4 = new JLabel("Dos Notebooks");
		JLabel label_5 = new JLabel("Na Máquina Central");
		lblDosNotebooks.setForeground(Color.GRAY);
		lblDosNotebooks.setHorizontalAlignment(SwingConstants.CENTER);
		lblDosNotebooks.setBounds(22, 294, 191, 14);
		
		abaImportaExportar.add(lblDosNotebooks);
		lblMquinaCentral.setHorizontalAlignment(SwingConstants.CENTER);
		lblMquinaCentral.setForeground(Color.GRAY);
		lblMquinaCentral.setBounds(235, 294, 191, 14);
		
		abaImportaExportar.add(lblMquinaCentral);
		lblDaMquinaCentral.setHorizontalAlignment(SwingConstants.CENTER);
		lblDaMquinaCentral.setForeground(Color.GRAY);
		lblDaMquinaCentral.setBounds(22, 177, 191, 14);
		
		abaImportaExportar.add(lblDaMquinaCentral);
		lblNosNotebooks.setHorizontalAlignment(SwingConstants.CENTER);
		lblNosNotebooks.setForeground(Color.GRAY);
		lblNosNotebooks.setBounds(235, 177, 200, 14);
		
		abaImportaExportar.add(lblNosNotebooks);
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		label_4.setForeground(Color.GRAY);
		label_4.setBounds(22, 411, 191, 14);
		
		abaImportaExportar.add(label_4);
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		label_5.setForeground(Color.GRAY);
		label_5.setBounds(235, 411, 191, 14);
		
		abaImportaExportar.add(label_5);
	}

	private void criarContainerRelatorios() {
		JPanel panelRelatorioCredenciamento = new JPanel();
		panelRelatorioCredenciamento.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Relatório de Credenciamento",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));

		panelRelatorioCredenciamento.setBounds(20, 11, 738, 96);
		abaRelatorios.add(panelRelatorioCredenciamento);
		panelRelatorioCredenciamento.setLayout(null);

		JLabel label = new JLabel("Turma");
		label.setBounds(10, 24, 37, 14);
		label.setFont(new Font("Tahoma", Font.BOLD, 11));
		panelRelatorioCredenciamento.add(label);

		comboBoxTurmaRelCredenciamento.setBounds(10, 38, 353, 20);
		panelRelatorioCredenciamento.add(comboBoxTurmaRelCredenciamento);

		buttonGerarRelCredenciamento.setBounds(525, 24, 203, 48);
		panelRelatorioCredenciamento.add(buttonGerarRelCredenciamento);

		JPanel panelRelatorioEtiqueta = new JPanel();
		panelRelatorioEtiqueta.setBounds(20, 132, 738, 164);
		abaRelatorios.add(panelRelatorioEtiqueta);
		panelRelatorioEtiqueta.setLayout(null);
		panelRelatorioEtiqueta.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Relatório de Etiquetas",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));

		buttonGerarRelEtiquietas.setBounds(553, 31, 175, 48);
		panelRelatorioEtiqueta.add(buttonGerarRelEtiquietas);
		
		buttonGerarRelCracha.setBounds(553, 90, 175, 48);
		panelRelatorioEtiqueta.add(buttonGerarRelCracha);

		JLabel label_1 = new JLabel("Turma");
		label_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		label_1.setBounds(10, 48, 37, 14);
		panelRelatorioEtiqueta.add(label_1);
		comboBoxTurmaRelEtiquetas.setEnabled(false);

		comboBoxTurmaRelEtiquetas.setBounds(10, 62, 353, 20);
		panelRelatorioEtiqueta.add(comboBoxTurmaRelEtiquetas);

		JLabel label_2 = new JLabel("Inscrição");
		label_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		label_2.setBounds(10, 107, 105, 14);
		panelRelatorioEtiqueta.add(label_2);

		textFieldInscricaoRelEtiqueta = new JTextField();
		textFieldInscricaoRelEtiqueta.setEnabled(true);
		textFieldInscricaoRelEtiqueta.setBounds(10, 121, 180, 20);
		panelRelatorioEtiqueta.add(textFieldInscricaoRelEtiqueta);

		JLabel label_3 = new JLabel("Nome do Participante");
		label_3.setFont(new Font("Tahoma", Font.BOLD, 11));
		label_3.setBounds(200, 107, 167, 14);
		panelRelatorioEtiqueta.add(label_3);

		textFieldNomeRelEtiqueta = new JTextField();
		textFieldNomeRelEtiqueta.setEnabled(true);
		textFieldNomeRelEtiqueta.setColumns(10);
		textFieldNomeRelEtiqueta.setBounds(200, 121, 300, 20);
		panelRelatorioEtiqueta.add(textFieldNomeRelEtiqueta);
		chckbxApenasNovosParticipantes.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				comboBoxTurmaRelEtiquetas.setEnabled(e.getStateChange() != ItemEvent.SELECTED);
			}
		});
		chckbxApenasNovosParticipantes.setSelected(true);
		chckbxApenasNovosParticipantes.setFont(new Font("Tahoma", Font.BOLD, 11));
		chckbxApenasNovosParticipantes.setBounds(10, 19, 203, 23);
		panelRelatorioEtiqueta.add(chckbxApenasNovosParticipantes);
		
		JPanel panelRelatorioMapaFrequencia = new JPanel();
		panelRelatorioMapaFrequencia.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Mapa Geral Frequência",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		JLabel labelTurmaMapaFrequencia = new JLabel("Turma");

		panelRelatorioMapaFrequencia.setLayout(null);
		panelRelatorioMapaFrequencia.setBounds(20, 317, 738, 124);

		abaRelatorios.add(panelRelatorioMapaFrequencia);
		labelTurmaMapaFrequencia.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelTurmaMapaFrequencia.setBounds(10, 24, 37, 14);

		panelRelatorioMapaFrequencia.add(labelTurmaMapaFrequencia);
		comboBoxTurmaMapaFrequencia.setBounds(10, 38, 353, 20);

		panelRelatorioMapaFrequencia.add(comboBoxTurmaMapaFrequencia);
		buttonGerarMapaFrequencia.setBounds(525, 24, 203, 48);

		panelRelatorioMapaFrequencia.add(buttonGerarMapaFrequencia);
		
		chckbxExibirMinutos = new JCheckBox("Exibir minutos ao invés de P, F ou PP.");
		chckbxExibirMinutos.setToolTipText("Se marcado será mostrado no relatório os minutos por turno e não a letra indicativa (P, F ou PP)");
		chckbxExibirMinutos.setFont(new Font("Tahoma", Font.BOLD, 11));
		chckbxExibirMinutos.setBounds(10, 78, 353, 23);
		panelRelatorioMapaFrequencia.add(chckbxExibirMinutos);
		
		JLabel labelAguardeProcessando = new JLabel("Aguarde, Gerando relatório...");
		labelAguardeProcessando.setIcon(createImageIcon("/imagens/waiting.gif"));
		labelAguardeProcessando.setVisible(false);
		waitPreviewRelatorios = new WaitPreview(labelAguardeProcessando);
		
		labelAguardeProcessando.setForeground(Color.BLUE);
		labelAguardeProcessando.setFont(new Font("Tahoma", Font.BOLD, 13));
		labelAguardeProcessando.setBounds(282, 478, 264, 23);
		abaRelatorios.add(labelAguardeProcessando);
		

	}

	public JButton getButtonGerarRelCracha() {
		return buttonGerarRelCracha;
	}

	private void criarContainerCredenciamento() {
		comboBoxOrgaoCredenciamento.setBounds(22, 118, 388, 20);
		AutoCompletion.enable(comboBoxOrgaoCredenciamento);
		abaCredenciamento.add(comboBoxOrgaoCredenciamento);

		JLabel labelOrgaoCredenciamento = new JLabel("Órgão");
		labelOrgaoCredenciamento.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelOrgaoCredenciamento.setBounds(22, 102, 105, 14);
		abaCredenciamento.add(labelOrgaoCredenciamento);

		textFieldNomeCredenciamento.setColumns(10);
		textFieldNomeCredenciamento.setBounds(22, 71, 388, 20);
		abaCredenciamento.add(textFieldNomeCredenciamento);

		JLabel labelNomeCredenciamento = new JLabel("Nome do Participante");
		labelNomeCredenciamento.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelNomeCredenciamento.setBounds(22, 56, 167, 14);
		abaCredenciamento.add(labelNomeCredenciamento);

		abaCredenciamento.add(getTextFieldInscricaoCredenciamento());

		JLabel labelInscricaoCredenciamento = new JLabel("Inscrição");
		labelInscricaoCredenciamento.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelInscricaoCredenciamento.setBounds(22, 11, 105, 14);
		abaCredenciamento.add(labelInscricaoCredenciamento);

		JScrollPane scrollPaneListaCredenciamento = new JScrollPane();
		scrollPaneListaCredenciamento.setBounds(24, 233, 730, 188);
		scrollPaneListaCredenciamento.setViewportView(listaCredenciamento);
		abaCredenciamento.add(scrollPaneListaCredenciamento);

		buttonLimparCredenciamento.setBounds(402, 168, 89, 23);
		abaCredenciamento.add(buttonLimparCredenciamento);

		buttonRegistrarCredenciamento.setBounds(287, 168, 105, 23);
		abaCredenciamento.add(buttonRegistrarCredenciamento);

		labelListaCredenciados.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelListaCredenciados.setBounds(24, 216, 346, 14);
		abaCredenciamento.add(labelListaCredenciados);

		jMenuItemExcluirCredenciamento = new JMenuItem();
		jMenuItemExcluirCredenciamento.setText("Excluir");
		jPopupMenuListaCredenciamento.add(jMenuItemExcluirCredenciamento);

		labelMensagemSucessoCredenciamento.setForeground(Color.BLUE);
		labelMensagemSucessoCredenciamento.setFont(new Font("Tahoma", Font.BOLD, 13));
		labelMensagemSucessoCredenciamento.setBounds(287, 432, 264, 23);
		abaCredenciamento.add(labelMensagemSucessoCredenciamento);
	}

	private void criarContainerCadastramento() {
		JLabel labelNumInscricao = new JLabel("Inscrição");
		labelNumInscricao.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelNumInscricao.setBounds(22, 11, 105, 14);
		abaCadastramento.add(labelNumInscricao);

		textFieldInscricao.setBounds(22, 25, 105, 20);
		textFieldInscricao.setColumns(10);
		abaCadastramento.add(textFieldInscricao);

		JLabel labelNomeParticipante = new JLabel("Nome do Participante");
		labelNomeParticipante.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelNomeParticipante.setBounds(22, 56, 167, 14);
		abaCadastramento.add(labelNomeParticipante);

		textFieldNomParticipante.setBounds(22, 71, 388, 20);
		textFieldNomParticipante.setColumns(10);
		abaCadastramento.add(textFieldNomParticipante);

		JLabel labelOrgao = new JLabel("Órgão");
		labelOrgao.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelOrgao.setBounds(22, 102, 105, 14);
		abaCadastramento.add(labelOrgao);

		comboBoxOrgao.setBounds(22, 118, 388, 20);
		AutoCompletion.enable(comboBoxOrgao);
		abaCadastramento.add(comboBoxOrgao);

		buttonPesquisarInscritos.setBounds(139, 159, 145, 23);
		abaCadastramento.add(buttonPesquisarInscritos);

		scrollPaneParticipanteInscrito.setBounds(22, 217, 732, 164);
		abaCadastramento.add(scrollPaneParticipanteInscrito);
		scrollPaneParticipanteInscrito.setViewportView(listaParticipanteInscrito);

		buttonNovoParticipante.setBounds(440, 159, 89, 23);
		abaCadastramento.add(buttonNovoParticipante);

		buttonLimparAbaInscricao.setBounds(539, 159, 89, 23);
		abaCadastramento.add(buttonLimparAbaInscricao);

		labelParticipantesInscritos.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelParticipantesInscritos.setBounds(22, 200, 227, 14);
		abaCadastramento.add(labelParticipantesInscritos);

		labelNovosParticipantes.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelNovosParticipantes.setBounds(22, 392, 202, 14);
		abaCadastramento.add(labelNovosParticipantes);

		JScrollPane scrollPaneListaNovosParticipanes = new JScrollPane();
		scrollPaneListaNovosParticipanes.setBounds(22, 409, 732, 164);
		scrollPaneListaNovosParticipanes.setViewportView(ListaNovosParticipantes);
		abaCadastramento.add(scrollPaneListaNovosParticipanes);

		buttonPesquisarNovosParticipantes.setBounds(294, 159, 136, 23);
		abaCadastramento.add(buttonPesquisarNovosParticipantes);

		// buttonGerarArquivo.setBounds(578, 159, 125, 23);
		// abaCadastramento.add(buttonGerarArquivo);

		// jMenuItemExcluirNovoParticipante = new JMenuItem();
		// jMenuItemExcluirNovoParticipante.setText("Excluir");
		// jPopupMenuListaNovosParticipantes.add(jMenuItemExcluirNovoParticipante);

		jMenuItemEditarNovoParticipante = new JMenuItem();
		jMenuItemEditarNovoParticipante.setText("Editar Participante Novo");
		jPopupMenuListaNovosParticipantes.add(jMenuItemEditarNovoParticipante);

		jMenuItemEditarParticipantesInscritos = new JMenuItem();
		jMenuItemEditarParticipantesInscritos.setText("Editar Participante Inscrito");
		jPopupMenuListaParticipantesInscritos.add(jMenuItemEditarParticipantesInscritos);

	}

	private void criarContainerFrequencia() {

		abaFrequencia.add(getTextFieldInscricaoFrequencia());

		textFieldNomeFrequencia.setEnabled(false);
		textFieldNomeFrequencia.setColumns(10);
		textFieldNomeFrequencia.setBounds(22, 128, 363, 20);
		textFieldNomeFrequencia.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(new Integer(KeyEvent.VK_ENTER).toString()), "enter");
		abaFrequencia.add(textFieldNomeFrequencia);

		comboBoxOrgaoFrequencia.setEnabled(false);
		AutoCompletion.enable(comboBoxOrgaoFrequencia);
		comboBoxOrgaoFrequencia.setBounds(22, 175, 363, 20);
		abaFrequencia.add(comboBoxOrgaoFrequencia);

		buttonRegistrarFrequencia.setBounds(22, 235, 95, 23);
		abaFrequencia.add(buttonRegistrarFrequencia);

		buttonLimparFrequencia.setBounds(127, 235, 89, 23);
		abaFrequencia.add(buttonLimparFrequencia);

		comboBoxTurmaFrequencia.setBounds(22, 37, 359, 20);
		AutoCompletion.enable(comboBoxTurmaFrequencia);
		abaFrequencia.add(comboBoxTurmaFrequencia);

		labelListaParticipantesFrequencia.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelListaParticipantesFrequencia.setBounds(22, 269, 287, 14);
		abaFrequencia.add(labelListaParticipantesFrequencia);

		labelVagasRestantes.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelVagasRestantes.setBounds(583, 269, 175, 14);
		abaFrequencia.add(labelVagasRestantes);

		JLabel labelInscricaoFrequencia = new JLabel("Inscrição");
		labelInscricaoFrequencia.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelInscricaoFrequencia.setBounds(22, 68, 105, 14);
		abaFrequencia.add(labelInscricaoFrequencia);

		JLabel labelNomeFrequencia = new JLabel("Nome do Participante");
		labelNomeFrequencia.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelNomeFrequencia.setBounds(22, 113, 167, 14);
		abaFrequencia.add(labelNomeFrequencia);

		JLabel labelOrgaoFrequencia = new JLabel("Órgão");
		labelOrgaoFrequencia.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelOrgaoFrequencia.setBounds(22, 159, 105, 14);
		abaFrequencia.add(labelOrgaoFrequencia);

		JLabel labelTurma = new JLabel("Turma");
		labelTurma.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelTurma.setBounds(22, 21, 105, 14);
		abaFrequencia.add(labelTurma);

		JScrollPane scrollPaneTableFrequencia = new JScrollPane();
		scrollPaneTableFrequencia.setBounds(22, 284, 736, 243);
		scrollPaneTableFrequencia.setViewportView(listaFrequencia);
		abaFrequencia.add(scrollPaneTableFrequencia);

		panelInformacoesTurma.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Informa\u00E7\u00F5es da Turma",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelInformacoesTurma.setBounds(395, 20, 363, 200);
		panelInformacoesTurma.setLayout(null);

		JLabel labelNomeSalaInformacao = new JLabel("Sala:");
		labelNomeSalaInformacao.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelNomeSalaInformacao.setBounds(24, 48, 89, 14);
		panelInformacoesTurma.add(labelNomeSalaInformacao);

		JLabel labelCapacidadeInformacao = new JLabel("Capaciadade:");
		labelCapacidadeInformacao.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelCapacidadeInformacao.setBounds(24, 150, 89, 14);
		panelInformacoesTurma.add(labelCapacidadeInformacao);

		JLabel labelHorarioInformacao = new JLabel("Horário:");
		labelHorarioInformacao.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelHorarioInformacao.setBounds(24, 73, 89, 14);
		panelInformacoesTurma.add(labelHorarioInformacao);

		JLabel labelTurmaInformacao = new JLabel("Turma:");
		labelTurmaInformacao.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelTurmaInformacao.setBounds(24, 23, 89, 14);
		panelInformacoesTurma.add(labelTurmaInformacao);

		JLabel labelInstrutorInformacao = new JLabel("Instrutor:");
		labelInstrutorInformacao.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelInstrutorInformacao.setBounds(24, 98, 89, 14);
		panelInformacoesTurma.add(labelInstrutorInformacao);

		labelConteudoNomeTurma.setBounds(111, 23, 242, 14);
		panelInformacoesTurma.add(labelConteudoNomeTurma);

		labelConteudoNomeSala.setBounds(111, 48, 242, 14);
		panelInformacoesTurma.add(labelConteudoNomeSala);

		labelConteudoCapacidade.setBounds(111, 150, 242, 14);
		panelInformacoesTurma.add(labelConteudoCapacidade);

		labelConteudoHorario.setBounds(111, 73, 242, 14);
		panelInformacoesTurma.add(labelConteudoHorario);

		labelConteudoInstrutor.setBounds(111, 98, 242, 14);
		panelInformacoesTurma.add(labelConteudoInstrutor);
		panelInformacoesTurma.setVisible(false);
		abaFrequencia.add(panelInformacoesTurma);

		JLabel labelInscritosTurmaFrequencia = new JLabel("Inscritos:");
		labelInscritosTurmaFrequencia.setBounds(24, 123, 89, 14);
		panelInformacoesTurma.add(labelInscritosTurmaFrequencia);
		labelInscritosTurmaFrequencia.setFont(new Font("Tahoma", Font.BOLD, 11));

		labelConteudoInscritosTurmaFrequencia.setBounds(111, 123, 242, 14);
		panelInformacoesTurma.add(labelConteudoInscritosTurmaFrequencia);

		JLabel labelConteudoTurma = new JLabel("Conteúdo:");
		labelConteudoTurma.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelConteudoTurma.setBounds(24, 175, 89, 14);
		panelInformacoesTurma.add(labelConteudoTurma);

		labelConteudoConteudo.setBounds(109, 175, 244, 14);
		panelInformacoesTurma.add(labelConteudoConteudo);

		labelMensagemSucessoFrequencia.setForeground(Color.BLUE);
		labelMensagemSucessoFrequencia.setVisible(Boolean.FALSE);
		labelMensagemSucessoFrequencia.setFont(new Font("Tahoma", Font.BOLD, 14));
		labelMensagemSucessoFrequencia.setBounds(291, 538, 278, 20);
		abaFrequencia.add(labelMensagemSucessoFrequencia);
		buttonFinalizarFrequenciaTurma.setToolTipText("Finalizar Frequência Turma");
		
		buttonFinalizarFrequenciaTurma.setBounds(560, 235, 196, 23);
		abaFrequencia.add(buttonFinalizarFrequenciaTurma);
		
		/**
		 * @TODO Ocultado os botões de Intervalo a pedido do cliente (Antonio)
		 */
		buttonIntervaloTurma.setBounds(395, 235, 132, 23);
		buttonIntervaloTurma.setVisible(false);
		abaFrequencia.add(buttonIntervaloTurma);
		
		buttonFimIntervaloTurma.setBounds(395, 235, 155, 23);
		buttonFimIntervaloTurma.setVisible(false);
		abaFrequencia.add(buttonFimIntervaloTurma);
	}

	private void criarContainerTurma() {
		comboBoxEventoTurma.setBounds(22, 26, 442, 20);
		AutoCompletion.enable(comboBoxEventoTurma);
		abaTurma.add(comboBoxEventoTurma);
		panelHorarioTurma.setBounds(23, 154, 357, 155);

		abaTurma.add(panelHorarioTurma);
		panelHorarioTurma.setLayout(null);

		comboBoxHorarioTurma.setBounds(11, 21, 336, 20);
		comboBoxHorarioTurma.setEnabled(Boolean.FALSE);
		AutoCompletion.enable(comboBoxHorarioTurma);
		panelHorarioTurma.add(comboBoxHorarioTurma);

		textFieldDescricaoHorarioTurma.setEnabled(Boolean.FALSE);
		textFieldDescricaoHorarioTurma.setBounds(11, 68, 336, 20);
		panelHorarioTurma.add(textFieldDescricaoHorarioTurma);
		textFieldDescricaoHorarioTurma.setColumns(50);

		labelNomeTurma.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelNomeTurma.setBounds(23, 104, 172, 14);
		abaTurma.add(labelNomeTurma);

		textFieldNomeTurma.setBounds(22, 120, 358, 20);
		textFieldNomeTurma.setEnabled(Boolean.FALSE);
		textFieldNomeTurma.setColumns(50);
		abaTurma.add(textFieldNomeTurma);

		buttonSalvarTurma.setBounds(300, 326, 76, 22);
		abaTurma.add(buttonSalvarTurma);

		buttonLimparTurma.setBounds(393, 326, 86, 22);
		abaTurma.add(buttonLimparTurma);

		panelSalaTurma.setBorder(new TitledBorder(null, "Sala", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelSalaTurma.setBounds(397, 154, 359, 155);

		abaTurma.add(panelSalaTurma);
		panelSalaTurma.setLayout(null);
		comboBoxSalaTurma.setBounds(10, 21, 339, 20);
		AutoCompletion.enable(comboBoxSalaTurma);
		comboBoxSalaTurma.setEnabled(Boolean.FALSE);
		panelSalaTurma.add(comboBoxSalaTurma);
		labelNomeSalaTurma.setBounds(10, 53, 145, 14);
		panelSalaTurma.add(labelNomeSalaTurma);
		labelNomeSalaTurma.setFont(new Font("Tahoma", Font.BOLD, 11));
		textFieldNomeSalaTurma.setBounds(10, 69, 224, 20);
		textFieldNomeSalaTurma.setEnabled(Boolean.FALSE);
		panelSalaTurma.add(textFieldNomeSalaTurma);
		textFieldNomeSalaTurma.setColumns(50);
		labelCapacidadeSalaTurma.setBounds(250, 53, 99, 14);
		panelSalaTurma.add(labelCapacidadeSalaTurma);
		labelCapacidadeSalaTurma.setFont(new Font("Tahoma", Font.BOLD, 11));

		checkBoxSalaAtivoTurma.setBounds(10, 102, 97, 23);
		checkBoxSalaAtivoTurma.setEnabled(Boolean.FALSE);
		panelSalaTurma.add(checkBoxSalaAtivoTurma);
		textFieldHorarioInicialTurma.setBounds(10, 115, 163, 20);
		textFieldHorarioInicialTurma.setEnabled(Boolean.FALSE);
		panelHorarioTurma.setBorder(new TitledBorder(null, "Hor\u00E1rio", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelHorarioTurma.add(textFieldHorarioInicialTurma);
		textFieldHorarioInicialTurma.setColumns(50);
		labelHorarioInicialTurma.setBounds(10, 99, 132, 14);
		panelHorarioTurma.add(labelHorarioInicialTurma);
		labelHorarioInicialTurma.setFont(new Font("Tahoma", Font.BOLD, 11));
		textFieldHorarioFinal.setBounds(183, 115, 164, 20);
		textFieldHorarioFinal.setEnabled(Boolean.FALSE);
		panelHorarioTurma.add(textFieldHorarioFinal);
		textFieldHorarioFinal.setColumns(50);
		labelHorarioFinalTurma.setBounds(183, 99, 153, 14);
		panelHorarioTurma.add(labelHorarioFinalTurma);
		labelHorarioFinalTurma.setFont(new Font("Tahoma", Font.BOLD, 11));

		scrollPaneTableTurma.setBounds(22, 371, 733, 191);
		abaTurma.add(scrollPaneTableTurma);
		scrollPaneTableTurma.setViewportView(listaTurma);

		labelListaTurma.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelListaTurma.setBounds(22, 356, 156, 14);
		abaTurma.add(labelListaTurma);

		// jMenuItemExcluirTurma = new JMenuItem();
		// jMenuItemExcluirTurma.setText("Excluir");
		// jPopupMenuListaTurmas.add(jMenuItemExcluirTurma);

		jMenuItemEditarTurma = new JMenuItem();
		jMenuItemEditarTurma.setText("Alterar");
		jPopupMenuListaTurmas.add(jMenuItemEditarTurma);

		comboBoxInstrutorTurma.setBounds(397, 120, 359, 20);
		comboBoxInstrutorTurma.setEnabled(Boolean.FALSE);
		AutoCompletion.enable(comboBoxInstrutorTurma);
		abaTurma.add(comboBoxInstrutorTurma);

		JLabel labelEventoTurma = new JLabel("Evento");
		labelEventoTurma.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelEventoTurma.setBounds(22, 11, 97, 14);
		abaTurma.add(labelEventoTurma);

		JLabel labelDescricaoHorarioTurma = new JLabel("Descrição");
		labelDescricaoHorarioTurma.setBounds(10, 52, 295, 14);
		panelHorarioTurma.add(labelDescricaoHorarioTurma);
		labelDescricaoHorarioTurma.setFont(new Font("Tahoma", Font.BOLD, 11));
		try {
			textFieldCapacidadeSalaTurma = new JFormattedTextField(new MaskFormatter("###"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		textFieldCapacidadeSalaTurma.setEnabled(false);
		textFieldCapacidadeSalaTurma.setColumns(50);
		textFieldCapacidadeSalaTurma.setBounds(250, 69, 99, 20);

		panelSalaTurma.add(textFieldCapacidadeSalaTurma);

		JLabel labelInstrutorTurma = new JLabel("Instrutor");
		labelInstrutorTurma.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelInstrutorTurma.setBounds(397, 104, 102, 14);
		abaTurma.add(labelInstrutorTurma);

		textFieldNomeDisciplina.setEnabled(false);
		textFieldNomeDisciplina.setColumns(50);
		textFieldNomeDisciplina.setBounds(21, 73, 443, 20);
		abaTurma.add(textFieldNomeDisciplina);

		JLabel labelNomeDisciplina = new JLabel("Disciplina");
		labelNomeDisciplina.setFont(new Font("Tahoma", Font.BOLD, 11));
		labelNomeDisciplina.setBounds(22, 57, 172, 14);
		abaTurma.add(labelNomeDisciplina);
	}

	public JButton getButtonSalvarTurma() {
		return buttonSalvarTurma;
	}

	public JButton getButtonLimparTurma() {
		return buttonLimparTurma;
	}

	public Turma getTurma() {
		if (turma == null) {
			turma = new Turma();
		}
		turma.setEvento(new Evento(((ComboBoxItemModel) comboBoxEventoTurma.getSelectedItem()).getId()));
		turma.setHorario(new Horario(((ComboBoxItemModel) comboBoxHorarioTurma.getSelectedItem()).getId()));
		turma.getHorario().setDescHorario(textFieldDescricaoHorarioTurma.getText());
		turma.getHorario().setHrFinal(DateUtil.getCalendarHora(textFieldHorarioFinal.getText(), PersistenceController.FORMATO_HORA_BANCO));
		turma.getHorario().setHrInicial(
				DateUtil.getCalendarHora(textFieldHorarioInicialTurma.getText(), PersistenceController.FORMATO_HORA_BANCO));
		turma.setInstrutor(new Instrutor(((ComboBoxItemModel) comboBoxInstrutorTurma.getSelectedItem()).getId()));
		turma.setNomTurma(textFieldNomeTurma.getText());
		turma.setSala(new Sala(((ComboBoxItemModel) comboBoxSalaTurma.getSelectedItem()).getId()));
		turma.getSala().setFlgAtivo(checkBoxSalaAtivoTurma.isSelected());
		turma.getSala().setNomSala(textFieldNomeSalaTurma.getText());
		turma.setNomDisciplina(textFieldNomeDisciplina.getText());
		if (textFieldCapacidadeSalaTurma.getText().length() == 1) {
			turma.getSala().setNumCapacidade(new Integer("00".concat(textFieldCapacidadeSalaTurma.getText())));
		} else if (textFieldCapacidadeSalaTurma.getText().length() == 2) {
			if (!textFieldCapacidadeSalaTurma.getText().trim().isEmpty()) {
				turma.getSala().setNumCapacidade(new Integer("0".concat(textFieldCapacidadeSalaTurma.getText())));
			}
		} else {
			if (!textFieldCapacidadeSalaTurma.getText().trim().isEmpty()) {
				turma.getSala().setNumCapacidade(new Integer(textFieldCapacidadeSalaTurma.getText().trim()));
			}
		}
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
		if (turma.getSala().getNumCapacidade().toString().length() == 1) {
			textFieldCapacidadeSalaTurma.setText("00".concat(turma.getSala().getNumCapacidade().toString()));
		} else if (turma.getSala().getNumCapacidade().toString().length() == 2) {
			textFieldCapacidadeSalaTurma.setText("0".concat(turma.getSala().getNumCapacidade().toString()));
		} else {
			textFieldCapacidadeSalaTurma.setText(turma.getSala().getNumCapacidade().toString());
		}
		textFieldDescricaoHorarioTurma.setText(turma.getHorario().getDescHorario());
		textFieldHorarioFinal.setText(DateUtil.getDataHora(turma.getHorario().getHrFinal().getTime(),
				PersistenceController.FORMATO_HORA_USUARIO));
		textFieldHorarioInicialTurma.setText(DateUtil.getDataHora(turma.getHorario().getHrInicial().getTime(),
				PersistenceController.FORMATO_HORA_USUARIO));
		textFieldNomeSalaTurma.setText(turma.getSala().getNomSala());
		textFieldNomeTurma.setText(turma.getNomTurma());
		textFieldNomeDisciplina.setText(turma.getNomDisciplina());
		checkBoxSalaAtivoTurma.setSelected(turma.getSala().getFlgAtivo());
		comboBoxEventoTurma.setSelectedItem(new ComboBoxItemModel(turma.getEvento().getId(), turma.getEvento().getNomEvento()));
		comboBoxHorarioTurma.setSelectedItem(new ComboBoxItemModel(turma.getHorario().getId(), turma.getHorario().getDescHorario()));
		comboBoxSalaTurma.setSelectedItem(new ComboBoxItemModel(turma.getSala().getId(), turma.getSala().getNomSala()));
		comboBoxInstrutorTurma.setSelectedItem(new ComboBoxItemModel(turma.getInstrutor().getId(), turma.getInstrutor().getNomInstrutor()));
	}

	public void resetAbaTurma() {
		textFieldNomeTurma.setText("");
		textFieldCapacidadeSalaTurma.setText("");
		textFieldDescricaoHorarioTurma.setText("");
		textFieldHorarioFinal.setText("");
		textFieldHorarioInicialTurma.setText("");
		textFieldNomeSalaTurma.setText("");
		textFieldNomeTurma.setText("");
		textFieldNomeDisciplina.setText("");
		if (comboBoxEventoTurma.getItemCount() != 0) {
			comboBoxEventoTurma.setSelectedIndex(0);
		}
		if (comboBoxHorarioTurma.getItemCount() != 0) {
			comboBoxHorarioTurma.setSelectedIndex(0);
		}
		if (comboBoxSalaTurma.getItemCount() != 0) {
			comboBoxSalaTurma.setSelectedIndex(0);
		}
		if (comboBoxInstrutorTurma.getItemCount() != 0) {
			comboBoxInstrutorTurma.setSelectedIndex(0);
		}
		turma = new Turma();
	}

	public JButton getButtonPesquisarInscritos() {
		return buttonPesquisarInscritos;
	}

	public ParticipanteInscrito getParticipanteInscrito() {
		participanteInscrito = new ParticipanteInscrito();
		participanteInscrito.setParticipante(new Participante());
		participanteInscrito.getParticipante().setNumInscricao(textFieldInscricao.getText());
		participanteInscrito.getParticipante().setNomParticipante(textFieldNomParticipante.getText());
		if (comboBoxOrgao.getItemCount() != 0) {
			participanteInscrito.getParticipante().setOrgao(
					new Orgao(new Integer(((ComboBoxItemModel) comboBoxOrgao.getSelectedItem()).getId())));
		}
		return participanteInscrito;
	}

	public void setParticipanteInscrito(ParticipanteInscrito participanteInscrito) {
		this.participanteInscrito = participanteInscrito;
	}

	public void refreshTableInscritos(List<Participante> listaParticipanteInscritos) {
		listaParticipanteInscrito.reload(listaParticipanteInscritos);
	}

	public List<Orgao> getListaOrgao() {
		return listaOrgao;
	}

	public void setListaOrgao(List<Orgao> listaOrgao) {
		this.listaOrgao = listaOrgao;
		if (listaOrgao != null && !listaOrgao.isEmpty()) {
			comboBoxOrgao.addItem(new ComboBoxItemModel(0, "Selecione"));
			for (Orgao orgao : listaOrgao) {
				comboBoxOrgao.addItem(new ComboBoxItemModel(orgao.getId(), orgao.getNomOrgao()));
			}
		}
	}

	public JComboBox getComboBoxOrgao() {
		return comboBoxOrgao;
	}

	public void setComboBoxOrgao(JComboBox comboBoxOrgao) {
		this.comboBoxOrgao = comboBoxOrgao;
	}

	public JButton getButtonNovoParticipante() {
		return buttonNovoParticipante;
	}

	public void setButtonNovoParticipante(JButton buttonNovoParticipante) {
		this.buttonNovoParticipante = buttonNovoParticipante;
	}

	public ParticipanteInscritoTable getListaParticipanteInscrito() {
		return listaParticipanteInscrito;
	}

	public JButton getButtonLimparAbaInscricao() {
		return buttonLimparAbaInscricao;
	}

	public void resetAbaInscricao() {
		textFieldInscricao.setText("");
		textFieldNomParticipante.setText("");
		if (comboBoxOrgao.getItemCount() != 0) {
			comboBoxOrgao.setSelectedIndex(0);
		}
	}

	public NovosParticipantesTable getListaNovosParticipantes() {
		return ListaNovosParticipantes;
	}

	public JButton getButtonPesquisarNovosParticipantes() {
		return buttonPesquisarNovosParticipantes;
	}

	public CredenciamentoTable getListaCredenciamento() {
		return listaCredenciamento;
	}

	public void setListaCredenciamento(CredenciamentoTable listaCredenciamento) {
		this.listaCredenciamento = listaCredenciamento;
	}

	public JComboBox getComboBoxOrgaoCredenciamento() {
		return comboBoxOrgaoCredenciamento;
	}

	public JButton getButtonLimparCredenciamento() {
		return buttonLimparCredenciamento;
	}

	public void resetAbaCredenciamento() {
		textFieldInscricaoCredenciamento.setText("");
		textFieldNomeCredenciamento.setText("");
		if (comboBoxOrgaoCredenciamento.getItemCount() != 0) {
			comboBoxOrgaoCredenciamento.setSelectedIndex(0);
		}
	}

	public Participante getParticipanteCredenciamento() {
		participanteCredenciamento = new Participante();
		participanteCredenciamento.setNumInscricao(textFieldInscricaoCredenciamento.getText());
		participanteCredenciamento.setNomParticipante(textFieldNomeCredenciamento.getText());
		if (comboBoxOrgaoCredenciamento.getItemCount() != 0) {
			participanteCredenciamento.setOrgao(new Orgao(((ComboBoxItemModel) comboBoxOrgaoCredenciamento.getSelectedItem()).getId()));
		}
		return participanteCredenciamento;
	}

	public void setParticipanteCredenciamento(Participante participante) {
		this.participanteCredenciamento = participante;
	}

	public void refreshTableCredenciamento(List<Credenciamento> credenciamentos) {
		listaCredenciamento.reload(credenciamentos);
	}

	public List<Orgao> getListaOrgaoCredenciamento() {
		return listaOrgaoCredenciamento;
	}

	public void setListaOrgaoCredenciamento(List<Orgao> listaOrgaoCredenciamento) {
		this.listaOrgaoCredenciamento = listaOrgaoCredenciamento;
		if (listaOrgaoCredenciamento != null && !listaOrgaoCredenciamento.isEmpty()) {
			comboBoxOrgaoCredenciamento.addItem(new ComboBoxItemModel(0, "Selecione"));
			for (Orgao orgao : listaOrgaoCredenciamento) {
				comboBoxOrgaoCredenciamento.addItem(new ComboBoxItemModel(orgao.getId(), orgao.getNomOrgao()));
			}
		}
	}

	public JLabel getLabelListaCredenciados() {
		return labelListaCredenciados;
	}

	public JButton getButtonRegistrarCredenciamento() {
		return buttonRegistrarCredenciamento;
	}

	public JLabel getLabelParticipantesInscritos() {
		return labelParticipantesInscritos;
	}

	public JLabel getLabelNovosParticipantes() {
		return labelNovosParticipantes;
	}

	public void setLabelNovosParticipantes(JLabel labelNovosParticipantes) {
		this.labelNovosParticipantes = labelNovosParticipantes;
	}

	public JButton getButtonGerarArquivo() {
		return buttonGerarArquivo;
	}

	public void setLabelParticipantesInscritos(JLabel labelParticipantesInscritos) {
		this.labelParticipantesInscritos = labelParticipantesInscritos;
	}

	public void refreshTableNovosParticipantes(List<Participante> participante) {
		ListaNovosParticipantes.reload(participante);
	}

	public Participante getParticipante() {
		participante = new Participante();
		participante.setNumInscricao(textFieldInscricao.getText());
		participante.setNomParticipante(textFieldNomParticipante.getText());
		if (comboBoxOrgao.getItemCount() != 0) {
			participante.setOrgao(new Orgao(((ComboBoxItemModel) (comboBoxOrgao.getSelectedItem())).getId()));
		}
		return participante;
	}

	public void setParticipante(Participante participante) {
		this.participante = participante;
	}

	public JLabel getLabelListaParticipantesFrequencia() {
		return labelListaParticipantesFrequencia;
	}

	public JLabel getLabelVagas() {
		return labelVagasRestantes;
	}

	public JButton getButtonLimparFrequencia() {
		return buttonLimparFrequencia;
	}

	public void resetAbaFrequencia() {
		frequencia = new Frequencia();
		limparTextFieldInscricaoFrequencia();
		getTextFieldInscricaoFrequencia();
		textFieldNomeFrequencia.setText("");
		if (comboBoxOrgaoFrequencia.getItemCount() != 0) {
			comboBoxOrgaoFrequencia.setSelectedIndex(0);
		}
		// if (comboBoxTurmaFrequencia.getItemCount() != 0) {
		// comboBoxTurmaFrequencia.setSelectedIndex(0);
		// }
	}

	public Frequencia getFrequencia() {
		frequencia = new Frequencia();
		frequencia.setParticipante(new Participante());
		if (comboBoxOrgaoFrequencia.getItemCount() != 0) {
			frequencia.getParticipante().setOrgao(new Orgao(((ComboBoxItemModel) comboBoxOrgaoFrequencia.getSelectedItem()).getId()));
		}
		if (comboBoxTurmaFrequencia.getItemCount() != 0) {
			frequencia.setTurma(new Turma(((ComboBoxItemModel) comboBoxTurmaFrequencia.getSelectedItem()).getId()));
		}
		frequencia.getParticipante().setNomParticipante(textFieldNomeFrequencia.getText());
		frequencia.getParticipante().setNumInscricao(getTextFieldInscricaoFrequencia().getText());
		return frequencia;
	}

	public void setFrequencia(Frequencia frequencia) {
		this.frequencia = frequencia;
	}

	public JComboBox getComboBoxOrgaoFrequencia() {
		return comboBoxOrgaoFrequencia;
	}

	public JComboBox getComboBoxTurmaFrequencia() {
		return comboBoxTurmaFrequencia;
	}

	public JButton getButtonRegistrarFrequencia() {
		return buttonRegistrarFrequencia;
	}

	public JPopupMenu getjPopupMenuListaCredenciamento() {
		return jPopupMenuListaCredenciamento;
	}

	public JMenuItem getjMenuItemExcluirCredenciamento() {
		return jMenuItemExcluirCredenciamento;
	}

	public JPopupMenu getjPopupMenuListaNovosParticipantes() {
		return jPopupMenuListaNovosParticipantes;
	}

	public JMenuItem getjMenuItemExcluirNovoParticipante() {
		return jMenuItemExcluirNovoParticipante;
	}

	public List<Orgao> getListaOrgaoFrequencia() {
		return listaOrgaoFrequencia;
	}

	public void setListaOrgaoFrequencia(List<Orgao> listaOrgaoFrequencia) {
		this.listaOrgaoFrequencia = listaOrgaoFrequencia;
		if (listaOrgaoFrequencia != null && !listaOrgaoFrequencia.isEmpty()) {
			comboBoxOrgaoFrequencia.addItem(new ComboBoxItemModel(0, "Selecione"));
			for (Orgao orgao : listaOrgaoFrequencia) {
				comboBoxOrgaoFrequencia.addItem(new ComboBoxItemModel(orgao.getId(), orgao.getNomOrgao()));
			}
		}
	}

	public List<Turma> getListaTurmaFrequencia() {
		return listaTurmaFrequencia;
	}

	public void setListaTurmaFrequencia(List<Turma> listaTurmaFrequencia) {
		this.listaTurmaFrequencia = listaTurmaFrequencia;
		if (listaTurmaFrequencia != null && !listaTurmaFrequencia.isEmpty()) {
			comboBoxTurmaFrequencia.addItem(new ComboBoxItemModel(0, "Selecione"));
			for (Turma turma : listaTurmaFrequencia) {
				comboBoxTurmaFrequencia.addItem(new ComboBoxItemModel(turma.getId(), turma.getNomTurma()));
			}
		}
	}

	public JTextField getTextFieldInscricaoFrequencia() {
		if (textFieldInscricaoFrequencia == null) {
			textFieldInscricaoFrequencia = new JTextField();
			textFieldInscricaoFrequencia.setEnabled(false);
			textFieldInscricaoFrequencia.setBounds(22, 82, 180, 20);
			textFieldInscricaoFrequencia.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					int key = e.getKeyCode();
					if (key == KeyEvent.VK_ENTER) {
						getButtonRegistrarFrequencia().doClick();
					}
				}
			});
		}
		return textFieldInscricaoFrequencia;
	}

	public JTextField getTextFieldInscricaoEspelho() {
		if (textFieldInscricaoEspelho == null) {
			textFieldInscricaoEspelho = new JTextField();
			textFieldInscricaoEspelho.setBounds(23, 36, 180, 20);
			textFieldInscricaoEspelho.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					int key = e.getKeyCode();
					if (key == KeyEvent.VK_ENTER) {
						getButtonCarregarEspelhoFrequencia().doClick();
					}
				}
			});
		}
		return textFieldInscricaoEspelho;
	}
	
	
	private void limparTextFieldInscricaoFrequencia() {
		boolean habilitado = getTextFieldInscricaoFrequencia().isEnabled();
		abaFrequencia.remove(getTextFieldInscricaoFrequencia());
		textFieldInscricaoFrequencia = null;
		abaFrequencia.add(getTextFieldInscricaoFrequencia());
		getTextFieldInscricaoFrequencia().setEnabled(habilitado);
	}

	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = PrincipalFrame.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	public JTextField getTextFieldNomeFrequencia() {
		return textFieldNomeFrequencia;
	}

	public JMenuItem getjMenuItemEditarNovoParticipante() {
		return jMenuItemEditarNovoParticipante;
	}

	public JPanel getPanelInformacoesTurma() {
		return panelInformacoesTurma;
	}

	public JLabel getLabelConteudoNomeTurma() {
		return labelConteudoNomeTurma;
	}

	public JLabel getLabelConteudoNomeSala() {
		return labelConteudoNomeSala;
	}

	public JLabel getLabelConteudoCapacidade() {
		return labelConteudoCapacidade;
	}

	public JLabel getLabelConteudoHorario() {
		return labelConteudoHorario;
	}

	public JLabel getLabelConteudoInstrutor() {
		return labelConteudoInstrutor;
	}

	public JLabel getLabelConteudoConteudo() {
		return labelConteudoConteudo;
	}

	public JMenuItem getjMenuItemEditarParticipantesInscritos() {
		return jMenuItemEditarParticipantesInscritos;
	}

	public JPopupMenu getjPopupMenuListaParticipantesInscritos() {
		return jPopupMenuListaParticipantesInscritos;
	}

	public void refreshTableFrequencias(List<Frequencia> frequencias) {
		listaFrequencia.reload(frequencias);
	}

	public FrequenciaTable getListaFrequencia() {
		return listaFrequencia;
	}

	public JComboBox getComboBoxSalaTurma() {
		return comboBoxSalaTurma;
	}

	public JComboBox getComboBoxEventoTurma() {
		return comboBoxEventoTurma;
	}

	public JComboBox getComboBoxHorarioTurma() {
		return comboBoxHorarioTurma;
	}

	public JPanel getPanelHorarioTurma() {
		return panelHorarioTurma;
	}

	public JPanel getPanelSalaTurma() {
		return panelSalaTurma;
	}

	public List<Evento> getListaEvento() {
		return listaEvento;
	}

	public void setListaEvento(List<Evento> listaEvento) {
		this.listaEvento = listaEvento;
		if (listaEvento != null && !listaEvento.isEmpty()) {
			comboBoxEventoTurma.addItem(new ComboBoxItemModel(0, "Selecione"));
			for (Evento evento : listaEvento) {
				comboBoxEventoTurma.addItem(new ComboBoxItemModel(evento.getId(), evento.getNomEvento()));
			}
		}
	}

	public List<Sala> getListaSala() {
		return listaSala;
	}

	public void setListaSala(List<Sala> listaSala) {
		this.listaSala = listaSala;
		if (listaSala != null && !listaSala.isEmpty()) {
			comboBoxSalaTurma.addItem(new ComboBoxItemModel(0, "Selecione"));
			for (Sala sala : listaSala) {
				comboBoxSalaTurma.addItem(new ComboBoxItemModel(sala.getId(), sala.getNomSala()));
			}
		}
	}

	public List<Horario> getListaHorario() {
		return listaHorario;
	}

	public void setListaHorario(List<Horario> listaHorario) {
		this.listaHorario = listaHorario;
		if (listaHorario != null && !listaHorario.isEmpty()) {
			comboBoxHorarioTurma.addItem(new ComboBoxItemModel(0, "Selecione"));
			for (Horario horario : listaHorario) {
				comboBoxHorarioTurma.addItem(new ComboBoxItemModel(horario.getId(), horario.getDescHorario()));
			}
		}
	}

	public JTextField getTextFieldNomeTurma() {
		return textFieldNomeTurma;
	}

	public JTextField getTextFieldDescricaoHorarioTurma() {
		return textFieldDescricaoHorarioTurma;
	}

	public JTextField getTextFieldHorarioInicialTurma() {
		return textFieldHorarioInicialTurma;
	}

	public JTextField getTextFieldHorarioFinal() {
		return textFieldHorarioFinal;
	}

	public JTextField getTextFieldNomeSalaTurma() {
		return textFieldNomeSalaTurma;
	}

	public JFormattedTextField getTextFieldCapacidadeSalaTurma() {
		return textFieldCapacidadeSalaTurma;
	}

	public void refreshTableTurmas(List<Turma> turmas) {
		listaTurma.reload(turmas);
	}

	public JLabel getLabelListaTurma() {
		return labelListaTurma;
	}

	public JMenuItem getjMenuItemExcluirTurma() {
		return jMenuItemExcluirTurma;
	}

	public JMenuItem getjMenuItemEditarTurma() {
		return jMenuItemEditarTurma;
	}

	public JPopupMenu getjPopupMenuListaTurmas() {
		return jPopupMenuListaTurmas;
	}

	public TurmaTable getListaTurma() {
		return listaTurma;
	}

	public JCheckBox getCheckBoxSalaAtivoTurma() {
		return checkBoxSalaAtivoTurma;
	}

	public JComboBox getComboBoxInstrutorTurma() {
		return comboBoxInstrutorTurma;
	}

	public List<Instrutor> getListaInstrutor() {
		return listaInstrutor;
	}

	public void setListaInstrutor(List<Instrutor> listaInstrutor) {
		this.listaInstrutor = listaInstrutor;
		if (listaInstrutor != null && !listaInstrutor.isEmpty()) {
			comboBoxInstrutorTurma.addItem(new ComboBoxItemModel(0, "Selecione"));
			for (Instrutor instrutor : listaInstrutor) {
				comboBoxInstrutorTurma.addItem(new ComboBoxItemModel(instrutor.getId(), instrutor.getNomInstrutor()));
			}
		}
	}

	public JFileChooser getFilePlanilhaTurmaCSV() {
		return filePlanilhaTurmaCSV;
	}

	public JFileChooser getFilePlanilhaInscritosCSV() {
		return filePlanilhaInscritosCSV;
	}

	public JButton getButtonImportarCSV_Turma_CFC() {
		return buttonImportarCSV_Turma_CFC;
	}

	public JButton getButtonImportarCSV_Inscrito_CFC() {
		return buttonImportarCSV_Inscrito_CFC;
	}

	public JLabel getLabelConteudoInscritosTurmaFrequencia() {
		return labelConteudoInscritosTurmaFrequencia;
	}

	public JTextField getTextFieldNomeDisciplina() {
		return textFieldNomeDisciplina;
	}

	public JLabel getLabelMensagemSucessoFrequencia() {
		return labelMensagemSucessoFrequencia;
	}

	public JFileChooser getFileImportacaoSQL() {
		return fileImportacaoSQL;
	}

	public JFileChooser getFileExportacaoSQL() {
		return fileExportacaoSQL;
	}

	public JButton getButtonImportar() {
		return buttonImportar;
	}

	public JButton getButtonExportar() {
		return buttonExportar;
	}

	public JTextField getTextFieldInscricaoCredenciamento() {
		if (textFieldInscricaoCredenciamento == null) {
			textFieldInscricaoCredenciamento = new JTextField();
			textFieldInscricaoCredenciamento.setBounds(22, 25, 105, 20);
			textFieldInscricaoCredenciamento.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					int key = e.getKeyCode();
					if (key == KeyEvent.VK_ENTER) {
						getButtonRegistrarCredenciamento().doClick();
					}
				}
			});
		}
		return textFieldInscricaoCredenciamento;
	}

	public JLabel getLabelMensagemSucessoCredenciamento() {
		return labelMensagemSucessoCredenciamento;
	}

	public JLabel getLabelAguardeCarregandoDados() {
		return labelAguardeCarregandoDados;
	}

	public JComboBox getComboBoxTurmaRelCredenciamento() {
		return comboBoxTurmaRelCredenciamento;
	}

	public void setListaTurmaRelCredenciamento(List<Turma> listaTurmaRelCredenciamento) {
		comboBoxTurmaRelCredenciamento.removeAllItems();
		if (listaTurmaRelCredenciamento != null && !listaTurmaRelCredenciamento.isEmpty()) {
			comboBoxTurmaRelCredenciamento.addItem(new ComboBoxItemModel(0, "TODAS"));
			for (Turma turma : listaTurmaFrequencia) {
				comboBoxTurmaRelCredenciamento.addItem(new ComboBoxItemModel(turma.getId(), turma.getNomTurma()));
			}
		}
	}

	public void setListaTurmaRelEtiqueta(List<Turma> listaTurmaRelEtiqueta) {
		comboBoxTurmaRelEtiquetas.removeAllItems();
		if (listaTurmaRelEtiqueta != null && !listaTurmaRelEtiqueta.isEmpty()) {
			comboBoxTurmaRelEtiquetas.addItem(new ComboBoxItemModel(0, "TODAS"));
			for (Turma turma : listaTurmaRelEtiqueta) {
				comboBoxTurmaRelEtiquetas.addItem(new ComboBoxItemModel(turma.getId(), turma.getNomTurma()));
			}
		}
	}

	public JButton getButtonGerarRelCredenciamento() {
		return buttonGerarRelCredenciamento;
	}

	public JComboBox getComboBoxTurmaRelEtiquetas() {
		return comboBoxTurmaRelEtiquetas;
	}

	public JButton getButtonGerarRelEtiquietas() {
		return buttonGerarRelEtiquietas;
	}

	public JCheckBox getChckbxApenasNovosParticipantes() {
		return chckbxApenasNovosParticipantes;
	}

	public JTextField getTextFieldInscricaoRelEtiqueta() {
		return textFieldInscricaoRelEtiqueta;
	}

	public JTextField getTextFieldNomeRelEtiqueta() {
		return textFieldNomeRelEtiqueta;
	}

	public JButton getButtonGerarMapaFrequencia() {
		return buttonGerarMapaFrequencia;
	}

	public JComboBox getComboBoxTurmaMapaFrequencia() {
		return comboBoxTurmaMapaFrequencia;
	}

	public void setListaTurmaMapaFrequencia(List<Turma> listaTurmas) {
		comboBoxTurmaMapaFrequencia.removeAllItems();
		if (listaTurmas != null && !listaTurmas.isEmpty()) {
			comboBoxTurmaMapaFrequencia.addItem(new ComboBoxItemModel(0, "TODAS"));
			for (Turma turma : listaTurmas) {
				comboBoxTurmaMapaFrequencia.addItem(new ComboBoxItemModel(turma.getId(), turma.getNomTurma()));
			}
		}
	}

	public JButton getButtonImportarCSV_Evento_CFC() {
		return buttonImportarCSV_Evento_CFC;
	}

	public JFileChooser getFilePlanilhaCrachaOrgaoCSV() {
		return filePlanilhaCrachaOrgaoCSV;
	}

	public JButton getButtonGerarPlanilhaCrach() {
		return buttonGerarPlanilhaCrach;
	}

	public JFileChooser getPastaExportacaoFrequencia() {
		return pastaExportacaoFrequencia;
	}

	public JFileChooser getFilesImportacaoFrequencia() {
		return filesImportacaoFrequencia;
	}

	public JButton getButtonExportarFrequencia() {
		return buttonExportarFrequencia;
	}

	public JButton getButtonImportarFrequencia() {
		return buttonImportarFrequencia;
	}
	

	public JButton getBtnImportarCredenciamentos() {
		return btnImportarCredenciamentos;
	}

	public JButton getBtnExportarCredenciamentos() {
		return btnExportarCredenciamentos;
	}

	public JCheckBox getChckbxExibirMinutos() {
		return chckbxExibirMinutos;
	}

	
	public WaitPreview getWaitPreviewRelatorios() {
		return waitPreviewRelatorios;
	}

	public JButton getButtonFinalizarFrequenciaTurma() {
		return buttonFinalizarFrequenciaTurma;
	}

	
	public JButton getButtonIntervaloTurma() {
		return buttonIntervaloTurma;
	}

	
	public JButton getButtonFimIntervaloTurma() {
		return buttonFimIntervaloTurma;
	}
	
	public JTextArea getTextAreaEspelhoFrequencia() {
		return textAreaEspelhoFrequencia;
	}

	
	public JButton getBtnCopiarExtrato() {
		return btnCopiarExtrato;
	}
}