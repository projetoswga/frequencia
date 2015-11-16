package br.com.frequencia.event;

import java.util.List;

import br.com.frequencia.model.Credenciamento;

/**
 * Evento deve ser gerado durante um registro de um Participante para o credenciamento.
 * 
 * <p>
 * Recebe a referência de um <code>Credenciamento</code> que foi incluido.
 * </p>
 * 
 */
public class CredenciarParticipanteEvent extends AbstractEvent<List<Credenciamento>> {

	public CredenciarParticipanteEvent(List<Credenciamento> credenciamentos) {
		super(credenciamentos);
	}

}
