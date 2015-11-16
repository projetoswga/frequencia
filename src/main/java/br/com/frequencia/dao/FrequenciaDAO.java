package br.com.frequencia.dao;

import java.util.Calendar;
import java.util.List;

import br.com.frequencia.model.Frequencia;

public interface FrequenciaDAO extends BaseDAO<Frequencia> {

	Frequencia pesquisarFrequencia(Frequencia frequencia);

	List<Frequencia> pesquisarFrequencias(String numInscricao);
	
	List<Frequencia> pesquisarFrequencias();

	List<Frequencia> pesquisarFrequencias(Integer idTurma);
	
	List<Frequencia> pesquisarFrequencias(List<Integer> idsTurmasParaRecarregarlistaFrequencia);

	List<Frequencia> pesquisarFrequenciasAbertas(Integer idTurma);
	
	List<Frequencia> pesquisarFrequenciasParticipanteTurma(String numInscricao, Integer idTurma);

	List<Frequencia> pesquisarFrequenciasParticipanteData(String numInscricao, Calendar dataEntrada);

	List<Frequencia>  pesquisarFrequenciasFinalizadas();

	boolean existeFrequenciaPartTurmaHorEntrada(Integer idParticipante,Integer idTurma, Calendar dataHoraEntrada);
}
