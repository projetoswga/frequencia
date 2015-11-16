package br.com.frequencia.ui.table;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.com.frequencia.model.Turma;

/**
 * Define um TableModel para entidade <code>Turma</code>, considerando as colunas:
 * <ul>
 * <li>Nome Evento;</li>
 * <li>Nome Turma;</li>
 * <li>Nome Oficina;</li>
 * <li>Horário;</li>
 * <li>Sala;</li>
 * <li>Capacidade;</li>
 * </ul>
 * 
 */
public class TurmaTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 2080196663264995869L;
	private List<Turma> turmas;
	private String[] colNomes = { "Nome Evento", "Nome Turma", "Disciplina", "Horário", "Sala", "Capacidade" };
	private Class<?>[] colTipos = { String.class, String.class, String.class, String.class, String.class, Integer.class };

	public TurmaTableModel() {
	}

	public void reload(List<Turma> turmas) {
		this.turmas = turmas;
		// atualiza o componente na tela
		fireTableDataChanged();
	}

	@Override
	public Class<?> getColumnClass(int coluna) {
		return colTipos[coluna];
	}

	public int getColumnCount() {
		return 5;
	}

	@Override
	public String getColumnName(int coluna) {
		return colNomes[coluna];
	}

	public int getRowCount() {
		if (turmas == null) {
			return 0;
		}
		return turmas.size();
	}

	public Object getValueAt(int linha, int coluna) {
		Turma turma = turmas.get(linha);
		switch (coluna) {
		case 0:
			return turma.getEvento().getNomEvento();
		case 1:
			return turma.getNomTurma();
		case 2:
			return turma.getNomDisciplina();
		case 3:
			return turma.getHorario().getDescHorario();
		case 4:
			return turma.getSala().getNomSala();
		case 5:
			return turma.getSala().getNumCapacidade();
		default:
			return null;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public Turma getTurmaAt(int index) {
		return turmas.get(index);
	}

}
