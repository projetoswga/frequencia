package br.com.frequencia.dao;

import java.util.List;

import br.com.frequencia.model.Orgao;

public interface OrgaoDAO extends BaseDAO<Orgao> {

	List<Orgao> pesquisarOrgaoByNomeEqual(String nome);

}
