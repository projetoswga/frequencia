package br.com.frequencia.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.frequencia.dao.AbstractDAO;
import br.com.frequencia.dao.CredenciamentoDAO;
import br.com.frequencia.model.Credenciamento;

/**
 * Implementa o contrato de persistência da entidade <code>Credenciamento</code>. Utiliza a herança para <code>AbstractDAO</code> para
 * resolver as operações básicas de cadastro com <code>JPA</code>.
 * 
 * @see br.com.frequencia.dao.CredenciamentoDAO
 * @see br.com.frequencia.dao.AbstractDAO
 * 
 */
public class CredenciamentoDAOImpl extends AbstractDAO<Credenciamento, Integer> implements CredenciamentoDAO {

	/**
	 * @param em
	 *            Recebe a referência para o <code>EntityManager</code>.
	 */
	public CredenciamentoDAOImpl(EntityManager em) {
		super(em);
	}

	@SuppressWarnings("unchecked")
	public Credenciamento recuperarCredenciamento(Integer idParticipante) {
		Query query = getPersistenceContext().createQuery("FROM Credenciamento c WHERE c.participante.id = :idParticipante");
		query.setParameter("idParticipante", idParticipante);
		List<Credenciamento> lista = query.getResultList();
		if (lista != null && !lista.isEmpty())
		return lista.get(0);
		return null;
	}
}
