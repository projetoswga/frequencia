package br.com.frequencia.event;

import java.util.List;

import br.com.frequencia.model.Frequencia;

/**
 * Evento deve ser gerado durante um registro de um Participante para a frequencia.
 * 
 * <p>
 * Recebe a referência de um <code>Frequencia</code> que foi incluido.
 * </p>
 * 
 */
public class RegistrarFrequenciaEvent extends AbstractEvent<List<Frequencia>> {

	public RegistrarFrequenciaEvent(List<Frequencia> frequencias) {
		super(frequencias);
	}

}
