package br.com.frequencia.dao.impl;

import javax.persistence.EntityManager;

import br.com.frequencia.dao.AbstractDAO;
import br.com.frequencia.dao.EventoDAO;
import br.com.frequencia.model.Evento;

/**
 * Implementa o contrato de persistência da entidade <code>Evento</code>. Utiliza a herança para <code>AbstractDAO</code> para resolver as
 * operações básicas de cadastro com <code>JPA</code>.
 * 
 * @see br.com.frequencia.dao.EventoDAO
 * @see br.com.frequencia.dao.AbstractDAO
 * 
 */
public class EventoDAOImpl extends AbstractDAO<Evento, Integer> implements EventoDAO {

	/**
	 * @param em
	 *            Recebe a referência para o <code>EntityManager</code>.
	 */
	public EventoDAOImpl(EntityManager em) {
		super(em);
	}

	/**
	 * Pesquisar o Evento Ativo
	 * @return Evento Ativo
	 */
	public Evento pesquisarEventoAtivo() {
		return (Evento) getPersistenceContext().createQuery("SELECT e FROM Evento e WHERE e.flgAtivo = 1").getResultList().get(0);
	}
}
