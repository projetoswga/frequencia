package br.com.frequencia.ui.frame;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import br.com.frequencia.model.Orgao;
import br.com.frequencia.model.Participante;
import br.com.frequencia.ui.combo.AutoCompletion;
import br.com.frequencia.ui.combo.ComboBoxItemModel;

public class EditarParticipanteInscristoFrame extends JFrame {
	private static final long serialVersionUID = -8430530341657643790L;
	private JTextField textFieldNome = new JTextField();
	private JLabel labelNome = new JLabel("Nome");
	private JLabel labelOrgao = new JLabel("Órgão");
	private JComboBox comboBoxOrgao = new JComboBox();
	private JButton buttonEditarParticipanteInscritos = new JButton("Salvar");
	private JButton buttonLimpar = new JButton("Limpar");
	private List<Orgao> listaOrgao = new ArrayList<Orgao>();
	private Participante participanteInscrito;
	private JTextField textFieldNomeCracha;

	public EditarParticipanteInscristoFrame() {
		setTitle("Incluir Novo Participante");
		getContentPane().setLayout(null);
		setSize(600, 350);
		setLocationRelativeTo(null);
		setIconImage(getToolkit().createImage(getClass().getResource("/imagens/icon_frequencia.png")));

		labelNome.setBounds(21, 67, 46, 14);
		getContentPane().add(labelNome);

		textFieldNome.setBounds(21, 82, 544, 20);
		getContentPane().add(textFieldNome);
		textFieldNome.setColumns(10);

		labelOrgao.setBounds(21, 113, 46, 14);
		getContentPane().add(labelOrgao);

		comboBoxOrgao.setBounds(21, 129, 257, 20);
		AutoCompletion.enable(comboBoxOrgao);
		getContentPane().add(comboBoxOrgao);

		buttonEditarParticipanteInscritos.setBounds(199, 182, 89, 23);
		getContentPane().add(buttonEditarParticipanteInscritos);

		buttonLimpar.setBounds(297, 182, 89, 23);
		getContentPane().add(buttonLimpar);

		textFieldNomeCracha = new JTextField();
		textFieldNomeCracha.setColumns(10);
		textFieldNomeCracha.setBounds(21, 37, 342, 20);
		getContentPane().add(textFieldNomeCracha);

		JLabel label = new JLabel("Nome Crachá");
		label.setBounds(22, 22, 117, 14);
		getContentPane().add(label);
	}

	public JButton getButtonLimpar() {
		return buttonLimpar;
	}

	public void setButtonLimpar(JButton buttonLimpar) {
		this.buttonLimpar = buttonLimpar;
	}

	/**
	 * Populando o modelo a partir do frame
	 * 
	 * @return <code>participanteInscrito</code>
	 */
	public Participante getParticipanteInscrito() {
		if (participanteInscrito == null) {
			participanteInscrito = new Participante();
		}
		participanteInscrito.setFlgRecemCadastrado(Boolean.FALSE);
		if (textFieldNome.getText() != null && !textFieldNome.getText().isEmpty()) {
			participanteInscrito.setNomParticipante(textFieldNome.getText());
		}
		if (comboBoxOrgao.getSelectedIndex() > -1) {
			participanteInscrito.setOrgao(new Orgao(((ComboBoxItemModel) comboBoxOrgao.getSelectedItem()).getId()));
		}
		if (textFieldNomeCracha.getText() != null && !textFieldNomeCracha.getText().isEmpty()) {
			participanteInscrito.setNomCracha(textFieldNomeCracha.getText());
		}
		if (participanteInscrito.getId() != null) {
			participanteInscrito.setFlgAlterado(Boolean.TRUE);
		}
		return participanteInscrito;
	}

	public void setParticipanteInscrito(Participante participanteInscrito) {
		this.participanteInscrito = participanteInscrito;
		textFieldNome.setText(participanteInscrito.getNomParticipante());
		textFieldNomeCracha.setText(participanteInscrito.getNomCracha());
		if (null != participanteInscrito.getOrgao())
			comboBoxOrgao.setSelectedItem(new ComboBoxItemModel(participanteInscrito.getOrgao().getId(), participanteInscrito.getOrgao()
				.getNomOrgao()));
	}

	public void resetForm() {
		participanteInscrito = null;
		textFieldNome.setText("");
		textFieldNomeCracha.setText("");
		if (comboBoxOrgao.getItemCount() != 0) {
			comboBoxOrgao.setSelectedIndex(0);
		}
	}

	public JComboBox getComboBoxOrgao() {
		return comboBoxOrgao;
	}

	public void setComboBoxOrgao(JComboBox comboBoxOrgao) {
		this.comboBoxOrgao = comboBoxOrgao;
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

	public JButton getButtonEditarParticipanteInscritos() {
		return buttonEditarParticipanteInscritos;
	}

	public void setButtonEditarParticipanteInscritos(JButton buttonEditarParticipanteInscritos) {
		this.buttonEditarParticipanteInscritos = buttonEditarParticipanteInscritos;
	}
}
