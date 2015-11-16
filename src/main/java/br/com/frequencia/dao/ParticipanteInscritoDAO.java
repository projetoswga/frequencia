package br.com.frequencia.dao;

import java.util.List;

import br.com.frequencia.model.Participante;
import br.com.frequencia.model.ParticipanteInscrito;

public interface ParticipanteInscritoDAO extends BaseDAO<ParticipanteInscrito> {

	/**
	 * Pesquisa participanteInscrito de acordo com o parametro. Caso o parametro esteja nulo ou vazio, a pesquisa retornará todos os
	 * participantes, do contrário, usará o parametro como filtro para a pesquisa.
	 * 
	 * @param participanteInscrito
	 * @return lista de participantes
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	List<ParticipanteInscrito> pesquisarParticipanteInscritos(ParticipanteInscrito participanteInscrito);

	/**
	 * Pesquisa participanteInscrito de acordo com o parametro.
	 * 
	 * @param participante
	 * @return participanteInscrito
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	List<ParticipanteInscrito> pesquisarParticipanteInscritos(Participante participante);

	/**
	 * Pesquisa o participanteInscritos onde a flgRecemCadastrado seja <code>false</code>.
	 * 
	 * @param participante
	 * @return participante
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	List<ParticipanteInscrito> pesquisarParticipantesInscritos();

	List<ParticipanteInscrito> pesquisarParticipanteInscritos(String numInscricao);

	List<ParticipanteInscrito> pesquisarParticipanteInscritosPorTurma(Integer idTurma);

	List<ParticipanteInscrito> pesquisarParticipanteInscritosPorTurmaInscNomeNovos(Integer idTurma, Participante participante);

}
