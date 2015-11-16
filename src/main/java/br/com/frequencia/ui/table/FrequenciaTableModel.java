package br.com.frequencia.ui.table;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.com.frequencia.controller.PersistenceController;
import br.com.frequencia.model.Frequencia;
import br.com.frequencia.util.DateUtil;

/**
 * Define um TableModel para entidade <code>Frequencia</code>, considerando as colunas:
 * <ul>
 * <li>Participante;</li>
 * <li>Turma;</li>
 * <li>Hora_Entrada;</li>
 * <li>Hora_Saida;</li>
 * </ul>
 * 
 */
public class FrequenciaTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 2080196663264995869L;
	private List<Frequencia> frequencias;
	private String[] colNomes = { "Inscrição", "Participante", "turma", "Hora Entrada", "Hora Saída" };
	private Class<?>[] colTipos = { String.class, String.class, String.class, String.class, String.class };

	public FrequenciaTableModel() {
	}

	public void reload(List<Frequencia> frequencias) {
		this.frequencias = frequencias;
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
		if (frequencias == null) {
			return 0;
		}
		return frequencias.size();
	}

	public Object getValueAt(int linha, int coluna) {
		Frequencia f = frequencias.get(linha);
		switch (coluna) {
		case 0:
			return f.getParticipante().getNumInscricao();
		case 1:
			return f.getParticipante().getNomParticipante();
		case 2:
			return f.getTurma().getNomTurma();
		case 3:
			if (f.getHorarioEntrada() != null) {
				return DateUtil.getDataHora(f.getHorarioEntrada().getTime(), PersistenceController.FORMATO_DATA_HORA_USUARIO);
			} else {
				return "";
			}
		case 4:
			if (f.getHorarioSaida() != null) {
				return DateUtil.getDataHora(f.getHorarioSaida().getTime(), PersistenceController.FORMATO_DATA_HORA_USUARIO);
			} else {
				return "";
			}
		default:
			return null;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public Frequencia geFrequenciaAt(int index) {
		return frequencias.get(index);
	}

}
