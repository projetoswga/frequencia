package br.com.frequencia.event;

import br.com.frequencia.model.Participante;

/**
 * Evento deve ser gerado durante a inclusão de um Participante.
 * 
 * <p>
 * Recebe a referência da <code>ParticipanteInscrito</code> que foi incluida.
 * </p>
 * 
 */
public class IncluirParticipanteEvent extends AbstractEvent<Participante> {

	public IncluirParticipanteEvent(Participante target) {
		super(target);
	}

}
