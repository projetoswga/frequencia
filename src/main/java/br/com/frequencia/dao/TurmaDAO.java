package br.com.frequencia.dao;

import java.util.List;

import br.com.frequencia.model.Turma;

public interface TurmaDAO extends BaseDAO<Turma>{

	List<Turma> pesquisarTurmas(Integer idEvento);
	
	public List<Turma> getTurmasByNome(String nome);
	
	public boolean existsTurmaByNomeEqual(String nome);
	
	public List<Turma> getTurmasByNomeEqual(String nome);

	Turma pesquisaTurmaPorHorarioSalaOficina(String horario, String sala, String oficina);
	

}
