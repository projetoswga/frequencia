package br.com.frequencia.ui.table;

import java.util.List;

import javax.swing.JTable;

import br.com.frequencia.model.Participante;

/**
 * <code>JTable</code> com operações customizadas para entidade <code>Participante</code>.
 * 
 * @see br.com.frequencia.ui.table.NovosParticipantesTableModel
 * 
 */
public class NovosParticipantesTable extends JTable {

	private static final long serialVersionUID = 8527710671337514516L;
	private NovosParticipantesTableModel modelo;

	public NovosParticipantesTable() {
		modelo = new NovosParticipantesTableModel();
		setModel(modelo);
	}

	/**
	 * @return <code>Participante</code> selecionada na tabela. Caso a tabela não tenha elementos, retorna <code>null</code>.
	 */
	public Participante getParticipanteSelected() {
		int i = getSelectedRow();
		if (i < 0) {
			return null;
		}
		return modelo.getParticipanteAt(i);
	}

	/**
	 * Recarrega a tabela de <code>Participante</code> com a lista <code>Participantes</code>.
	 * 
	 * @param Participantes
	 *            <code>List</code> com os elementos <code>Participantes</code> que devem ser exibidos na tabela.
	 */
	public void reload(List<Participante> Participantes) {
		modelo.reload(Participantes);
	}
}
