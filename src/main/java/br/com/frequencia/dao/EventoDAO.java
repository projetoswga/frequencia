package br.com.frequencia.dao;

import br.com.frequencia.model.Evento;

public interface EventoDAO extends BaseDAO<Evento> {

	Evento pesquisarEventoAtivo();

}
