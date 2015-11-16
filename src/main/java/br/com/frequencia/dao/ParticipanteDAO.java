package br.com.frequencia.dao;

import java.util.List;

import br.com.frequencia.model.Participante;

public interface ParticipanteDAO extends BaseDAO<Participante> {

	List<Participante> pesquisarParticipantes(Participante participante);

	List<Participante> pesquisarParticipantesInscritos(Participante participante);

	List<Participante> pesquisarParticipantesNovos();

	List<Participante> pesquisarParticipantesInscritos();

	Boolean pesquisarNumInscricao(String numInscricao);

	List<Participante> pesquisarTodosParticipantes(Participante participanteCredenciado);

	List<Participante> pesquisarParticipantesNovosGeracaoArquivo();

	List<Participante> pesquisarParticipantesInscritosGeracaoArquivo();

	Participante pesquisarParticipantes(String numInscricao);

	void importarParticipantes(List<String> listaSQL);

	List<Participante> pesquisarParticipantesTurma(Integer idTurma);
	
	public List<Participante> pesquisarParticipantes(Integer idTurma, Participante participante, Boolean recemCadastrado);

}
