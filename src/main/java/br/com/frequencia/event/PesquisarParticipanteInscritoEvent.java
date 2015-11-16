package br.com.frequencia.event;

import java.util.List;

import br.com.frequencia.model.Participante;

/**
 * Evento deve ser gerado durante a pesquisa de Participante.
 * 
 * <p>
 *  Recebe um <code>List</code> com a(s) <code>Participante<code>(s) encontrada(s).
 * </p>
 * 
 */
public class PesquisarParticipanteInscritoEvent extends AbstractEvent<List<Participante>> {

	public PesquisarParticipanteInscritoEvent(List<Participante> target) {
		super(target);
	}
}
