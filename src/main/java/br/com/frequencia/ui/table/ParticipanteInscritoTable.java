package br.com.frequencia.ui.table;

import java.util.List;

import javax.swing.JTable;

import br.com.frequencia.model.Participante;

/**
 * <code>JTable</code> com operações customizadas para entidade <code>Participante</code>.
 * 
 * @see br.com.frequencia.ui.table.ParticipanteInscritoTableModel
 * 
 */
public class ParticipanteInscritoTable extends JTable {

	private static final long serialVersionUID = 8527710671337514516L;
	private ParticipanteInscritoTableModel modelo;

	public ParticipanteInscritoTable() {
		modelo = new ParticipanteInscritoTableModel();
		setModel(modelo);
	}

	/**
	 * @return <code>Participante</code> selecionado na tabela. Caso a tabela não tenha elementos, retorna <code>null</code>.
	 */
	public Participante getParticipanteInscritoSelected() {
		int i = getSelectedRow();
		if (i < 0) {
			return null;
		}
		return modelo.getParticipanteInscritoAt(i);
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
