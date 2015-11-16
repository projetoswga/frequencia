package br.com.frequencia.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.frequencia.dao.AbstractDAO;
import br.com.frequencia.dao.SalaDAO;
import br.com.frequencia.model.Sala;

/**
 * Implementa o contrato de persistência da entidade <code>Sala</code>. Utiliza a herança para <code>AbstractDAO</code> para resolver as
 * operações básicas de cadastro com <code>JPA</code>.
 * 
 * @see br.com.frequencia.dao.SalaDAO
 * @see br.com.frequencia.dao.AbstractDAO
 * 
 */
public class SalaDAOImpl extends AbstractDAO<Sala, Integer> implements SalaDAO {

	/**
	 * @param em
	 *            Recebe a referência para o <code>EntityManager</code>.
	 */
	public SalaDAOImpl(EntityManager em) {
		super(em);
	}
	
	/**
	 * Reliza a pesquisa Salaes com filtro no nome (via operador <code>like</code>).
	 * 
	 * @see br.com.dao.SalaDAO#getSalasByNome(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Sala> pesquisarSalaByNome(String nome) {
		if (nome == null || nome.isEmpty())
			return null;
		Query query = getPersistenceContext().createQuery("FROM Sala o WHERE o.nomSala like :nomSala", Sala.class);
		query.setParameter("nomSala", nome.concat("%"));
		return query.getResultList();
	}

	/**
	 * Reliza a pesquisa Salas com filtro no nome (via operador <code>like</code>).
	 * 
	 * @see br.com.dao.SalaDAO#getSalasByNome(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Sala> pesquisarSalaByNomeEqual(String nome) {
		if (nome == null || nome.isEmpty())
			return null;
		Query query = getPersistenceContext().createQuery("FROM Sala o WHERE o.nomSala = :nomSala",Sala.class);
		query.setParameter("nomSala", nome);
		return query.getResultList();
	}
	
}
