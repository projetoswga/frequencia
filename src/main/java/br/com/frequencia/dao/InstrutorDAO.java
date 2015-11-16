package br.com.frequencia.dao;

import java.util.List;

import br.com.frequencia.model.Instrutor;

public interface InstrutorDAO extends BaseDAO<Instrutor> {

	List<Instrutor> pesquisarInstrutorAtivo();

	List<Instrutor> pesquisarInstrutorByNome(String nome);

	List<Instrutor> pesquisarInstrutorByNomeEqual(String nome);
	
	public boolean existsInstrutorByNomeEqual(String nome);
}
