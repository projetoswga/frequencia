package br.com.frequencia.ui.table;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.com.frequencia.model.Participante;

/**
 * Define um TableModel para entidade <code>Participante</code>, considerando as colunas:
 * <ul>
 * <li>Inscrição;</li>
 * <li>Nome;</li>
 * <li>Órgao;</li>
 * </ul>
 * 
 */
public class ParticipanteInscritoTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 2080196663264995869L;
	private List<Participante> participantes;
	private String[] colNomes = { "Inscrição", "Nome", "Órgão" };
	private Class<?>[] colTipos = { String.class, String.class, String.class };

	public ParticipanteInscritoTableModel() {
	}

	public void reload(List<Participante> participantes) {
		this.participantes = participantes;
		// atualiza o componente na tela
		fireTableDataChanged();
	}

	@Override
	public Class<?> getColumnClass(int coluna) {
		return colTipos[coluna];
	}

	public int getColumnCount() {
		return 3;
	}

	@Override
	public String getColumnName(int coluna) {
		return colNomes[coluna];
	}

	public int getRowCount() {
		if (participantes == null) {
			return 0;
		}
		return participantes.size();
	}

	public Object getValueAt(int linha, int coluna) {
		Participante pi = participantes.get(linha);
		switch (coluna) {
		case 0:
			return pi.getNumInscricao();
		case 1:
			return pi.getNomParticipante();
		case 2:
			return pi.getOrgao() == null ? "" : pi.getOrgao().getNomOrgao();
		default:
			return null;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public Participante getParticipanteInscritoAt(int index) {
		return participantes.get(index);
	}

}
