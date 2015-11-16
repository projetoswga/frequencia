package br.com.frequencia.event;

import java.util.List;

import br.com.frequencia.model.Participante;

/**
 * Evento deve ser gerado durante a pesquisa de Participante.
 * 
 * <p>
 *  Recebe um <code>List</code> com a(s) <code>Participante<code>(s) encontrado(s).
 * </p>
 * 
 */
public class PesquisarNovosParticipantesEvent extends AbstractEvent<List<Participante>> {

	public PesquisarNovosParticipantesEvent(List<Participante> target) {
		super(target);
	}
}
