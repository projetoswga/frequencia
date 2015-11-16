package br.com.frequencia.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.frequencia.dao.AbstractDAO;
import br.com.frequencia.dao.InstrutorDAO;
import br.com.frequencia.model.Instrutor;

/**
 * Implementa o contrato de persistência da entidade <code>Instrutor</code>. Utiliza a herança para <code>AbstractDAO</code> para resolver as
 * operações básicas de cadastro com <code>JPA</code>.
 * 
 * @see br.com.frequencia.dao.InstrutorDAO
 * @see br.com.frequencia.dao.AbstractDAO
 * 
 */
public class InstrutorDAOImpl extends AbstractDAO<Instrutor, Integer> implements InstrutorDAO {

	/**
	 * @param em
	 *            Recebe a referência para o <code>EntityManager</code>.
	 */
	public InstrutorDAOImpl(EntityManager em) {
		super(em);
	}

	/**
	 * Pesquisar o Instrutor Ativo
	 * @return Instrutor Ativo
	 */
	public List<Instrutor> pesquisarInstrutorAtivo() {
		return getPersistenceContext().createQuery("SELECT e FROM Instrutor e WHERE e.flgAtivo = 1",Instrutor.class).getResultList();
	}

	/**
	 * Reliza a pesquisa Instrutores com filtro no nome (via operador <code>like</code>).
	 * 
	 * @see br.com.dao.InstrutorDAO#getInstrutorsByNome(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Instrutor> pesquisarInstrutorByNome(String nome) {
		if (nome == null || nome.isEmpty())
			return null;
		Query query = getPersistenceContext().createQuery("FROM Instrutor o WHERE o.nomInstrutor like :nomInstrutor");
		query.setParameter("nomInstrutor", nome.concat("%"));
		return (List<Instrutor>) query.getResultList();
	}

	/**
	 * Reliza a pesquisa Instrutors com filtro no nome (via operador <code>like</code>).
	 * 
	 * @see br.com.dao.InstrutorDAO#getInstrutorsByNome(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Instrutor> pesquisarInstrutorByNomeEqual(String nome) {
		if (nome == null || nome.isEmpty())
			return null;
		Query query = getPersistenceContext().createQuery("FROM Instrutor o WHERE o.nomInstrutor = :nomInstrutor",Instrutor.class);
		query.setParameter("nomInstrutor", nome);
		return query.getResultList();
	}

	/**
	 * Reliza a pesquisa Instrutors com filtro no nome (via operador <code>like</code>).
	 * 
	 * @see br.com.dao.InstrutorDAO#getInstrutorsByNome(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public boolean existsInstrutorByNomeEqual(String nome) {
		if (nome == null || nome.isEmpty())
			return false;
		Query query = getPersistenceContext().createQuery("FROM Instrutor o WHERE o.nomInstrutor = :nomInstrutor",Instrutor.class);
		query.setParameter("nomInstrutor", nome);
		List<Instrutor> results = query.getResultList();
		return (results != null && !results.isEmpty());
	}

}
