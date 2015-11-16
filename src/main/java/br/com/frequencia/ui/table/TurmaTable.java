package br.com.frequencia.ui.table;

import java.util.List;

import javax.swing.JTable;

import br.com.frequencia.model.Turma;

/**
 * <code>JTable</code> com operações customizadas para entidade <code>Turma</code>.
 * 
 * @see br.com.frequencia.ui.table.TurmaTableModel
 * 
 */
public class TurmaTable extends JTable {

	private static final long serialVersionUID = 8527710671337514516L;
	private TurmaTableModel modelo;

	public TurmaTable() {
		modelo = new TurmaTableModel();
		setModel(modelo);
	}

	/**
	 * @return <code>Turma</code> selecionada na tabela. Caso a tabela não tenha elementos, retorna <code>null</code>.
	 */
	public Turma getTurmaSelected() {
		int i = getSelectedRow();
		if (i < 0) {
			return null;
		}
		return modelo.getTurmaAt(i);
	}

	/**
	 * Recarrega a tabela de <code>turma</code> com a lista <code>turmas</code>.
	 * 
	 * @param turmas
	 *            <code>List</code> com os elementos <code>turmas</code> que devem ser exibidos na tabela.
	 */
	public void reload(List<Turma> turmas) {
		modelo.reload(turmas);
	}
}
