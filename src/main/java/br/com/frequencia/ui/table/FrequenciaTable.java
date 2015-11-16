package br.com.frequencia.ui.table;

import java.util.List;

import javax.swing.JTable;

import br.com.frequencia.model.Frequencia;

/**
 * <code>JTable</code> com operações customizadas para entidade <code>ParticipanteInscrito</code>.
 * 
 * @see br.com.frequencia.ui.table.ParticipanteInscritoTableModel
 * 
 */
public class FrequenciaTable extends JTable {

	private static final long serialVersionUID = 8527710671337514516L;
	private FrequenciaTableModel modelo;

	public FrequenciaTable() {
		modelo = new FrequenciaTableModel();
		setModel(modelo);
	}

	/**
	 * @return <code>Frequencia</code> selecionada na tabela. Caso a tabela não tenha elementos, retorna <code>null</code>.
	 */
	public Frequencia getFrequenciaSelected() {
		int i = getSelectedRow();
		if (i < 0) {
			return null;
		}
		return modelo.geFrequenciaAt(i);
	}

	/**
	 * Recarrega a tabela de <code>Frequencia</code> com a lista <code>frequencias</code>.
	 * 
	 * @param frequencias
	 *            <code>List</code> com os elementos de <code>Frequencia</code> que devem ser exibidos na tabela.
	 */
	public void reload(List<Frequencia> frequencias) {
		modelo.reload(frequencias);
	}
}
