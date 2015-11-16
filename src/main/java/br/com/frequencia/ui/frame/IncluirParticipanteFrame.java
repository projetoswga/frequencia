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

public class IncluirParticipanteFrame extends JFrame {
	private static final long serialVersionUID = -8430530341657643790L;
	private JTextField textFieldNome = new JTextField();
	private JLabel labelNome = new JLabel("Nome");
	private JLabel labelOrgao = new JLabel("Órgão");
	private JComboBox comboBoxOrgao = new JComboBox();
	private JButton buttonSalvarNovoParticipante = new JButton("Salvar");
	private JButton buttonLimpar = new JButton("Limpar");
	private List<Orgao> listaOrgao = new ArrayList<Orgao>();
	private Participante participante;
	private JTextField textFieldNomeCracha;

	public IncluirParticipanteFrame() {
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

		labelOrgao.setBounds(21, 113, 101, 14);
		getContentPane().add(labelOrgao);

		comboBoxOrgao.setBounds(21, 128, 544, 20);
		AutoCompletion.enable(comboBoxOrgao);
		getContentPane().add(comboBoxOrgao);

		buttonSalvarNovoParticipante.setBounds(196, 181, 89, 23);
		getContentPane().add(buttonSalvarNovoParticipante);

		buttonLimpar.setBounds(294, 181, 89, 23);
		getContentPane().add(buttonLimpar);

		JLabel labelNomeCracha = new JLabel("Nome Crachá");
		labelNomeCracha.setBounds(23, 21, 117, 14);
		getContentPane().add(labelNomeCracha);

		textFieldNomeCracha = new JTextField();
		textFieldNomeCracha.setColumns(10);
		textFieldNomeCracha.setBounds(22, 36, 342, 20);
		getContentPane().add(textFieldNomeCracha);
	}

	public JButton getButtonSalvarNovoParticipante() {
		return buttonSalvarNovoParticipante;
	}

	public void setButtonSalvarNovoParticipante(JButton buttonSalvarNovoParticipante) {
		this.buttonSalvarNovoParticipante = buttonSalvarNovoParticipante;
	}

	public JButton getButtonLimpar() {
		return buttonLimpar;
	}

	public void setButtonLimpar(JButton buttonLimpar) {
		this.buttonLimpar = buttonLimpar;
	}

	public void resetForm() {
		participante = null;
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

	public Participante getParticipante() {
		if (participante == null) {
			participante = new Participante();
		}
		participante.setFlgRecemCadastrado(Boolean.TRUE);
		if (textFieldNome.getText() != null && !textFieldNome.getText().isEmpty()) {
			participante.setNomParticipante(textFieldNome.getText());
		}
		if (comboBoxOrgao.getSelectedIndex() > 0) {
			participante.setOrgao(new Orgao(((ComboBoxItemModel) comboBoxOrgao.getSelectedItem()).getId()));
		}
		if (textFieldNomeCracha.getText() != null && !textFieldNomeCracha.getText().isEmpty()) {
			participante.setNomCracha(textFieldNomeCracha.getText());
		}
		participante.setFlgAlterado(Boolean.FALSE);
		return participante;
	}

	public void setParticipante(Participante participante) {
		this.participante = participante;
		textFieldNome.setText(participante.getNomParticipante());
		comboBoxOrgao.setSelectedIndex(participante.getOrgao().getId());
		textFieldNomeCracha.setText(participante.getNomCracha());
	}
}
