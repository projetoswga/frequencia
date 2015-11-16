package br.com.frequencia.dao;

import br.com.frequencia.model.Credenciamento;

public interface CredenciamentoDAO extends BaseDAO<Credenciamento> {

	Credenciamento recuperarCredenciamento(Integer idParticipante);

}
