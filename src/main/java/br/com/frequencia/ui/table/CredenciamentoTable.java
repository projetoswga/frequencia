package br.com.frequencia.ui.table;

import java.util.List;

import javax.swing.JTable;

import br.com.frequencia.model.Credenciamento;

/**
 * <code>JTable</code> com operações customizadas para entidade <code>ParticipanteInscrito</code>.
 * 
 * @see br.com.frequencia.ui.table.ParticipanteInscritoTableModel
 * 
 */
public class CredenciamentoTable extends JTable {

	private static final long serialVersionUID = 8527710671337514516L;
	private CredenciamentoTableModel modelo;

	public CredenciamentoTable() {
		modelo = new CredenciamentoTableModel();
		setModel(modelo);
	}

	/**
	 * @return <code>Credenciamento</code> selecionada na tabela. Caso a tabela não tenha elementos, retorna <code>null</code>.
	 */
	public Credenciamento getCredenciadoSelected() {
		int i = getSelectedRow();
		if (i < 0) {
			return null;
		}
		return modelo.getCredenciadoAt(i);
	}

	/**
	 * Recarrega a tabela de <code>Credenciamento</code> com a lista <code>credenciamentos</code>.
	 * 
	 * @param credenciamentos
	 *            <code>List</code> com os elementos <code>ParticipanteInscritos</code> que devem ser exibidos na tabela.
	 */
	public void reload(List<Credenciamento> credenciamentos) {
		modelo.reload(credenciamentos);
	}
}
