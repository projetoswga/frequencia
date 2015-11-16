package br.com.frequencia.ui.table;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.com.frequencia.model.Credenciamento;

/**
 * Define um TableModel para entidade <code>ParticipanteInscrito</code>, considerando as colunas:
 * <ul>
 * <li>Inscrição;</li>
 * <li>Nome;</li>
 * <li>Órgao;</li>
 * <li>Turma;</li>
 * </ul>
 * 
 */
public class CredenciamentoTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 2080196663264995869L;
	private List<Credenciamento> credenciados;
	private String[] colNomes = { "Inscrição", "Nome", "Órgão", "Evento" };
	private Class<?>[] colTipos = { String.class, String.class, String.class, String.class };

	public CredenciamentoTableModel() {
	}

	public void reload(List<Credenciamento> credenciados) {
		this.credenciados = credenciados;
		// atualiza o componente na tela
		fireTableDataChanged();
	}

	@Override
	public Class<?> getColumnClass(int coluna) {
		return colTipos[coluna];
	}

	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int coluna) {
		return colNomes[coluna];
	}

	public int getRowCount() {
		if (credenciados == null) {
			return 0;
		}
		return credenciados.size();
	}

	public Object getValueAt(int linha, int coluna) {
		Credenciamento credenciado = credenciados.get(linha);
		switch (coluna) {
		case 0:
			return credenciado.getParticipante().getNumInscricao();
		case 1:
			return credenciado.getParticipante().getNomParticipante();
		case 2:
			if (credenciado.getParticipante().getOrgao() != null){
				return credenciado.getParticipante().getOrgao().getNomOrgao();
			}else {
				return "";
			}
		case 3:
			return credenciado.getEvento().getNomEvento();
		default:
			return null;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public Credenciamento getCredenciadoAt(int index) {
		return credenciados.get(index);
	}

}
