package br.com.frequencia.event;

import br.com.frequencia.model.Turma;

/**
 * Evento deve ser gerado durante a inclusão de uma <code>Turma</code>.
 * 
 * <p>Recebe a referência da <code>Turma</code> que foi incluida.</p>
 * 
 */
public class IncluirTurmaEvent extends AbstractEvent<Turma> {
	
	public IncluirTurmaEvent(Turma m) {
		super(m);
	}
}
