package br.com.frequencia.dao;

import java.util.List;

import br.com.frequencia.model.Sala;

public interface SalaDAO extends BaseDAO<Sala>{
	List<Sala> pesquisarSalaByNome(String nome);
	List<Sala> pesquisarSalaByNomeEqual(String nome);
}
