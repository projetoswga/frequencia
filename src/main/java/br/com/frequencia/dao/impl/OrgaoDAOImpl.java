package br.com.frequencia.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.frequencia.dao.AbstractDAO;
import br.com.frequencia.dao.OrgaoDAO;
import br.com.frequencia.model.Orgao;

/**
 * Implementa o contrato de persistência da entidade <code>Órgao</code>. Utiliza a herança para <code>AbstractDAO</code> para
 * resolver as operações básicas de cadastro com <code>JPA</code>.
 * 
 * @see br.com.frequencia.dao.OrgaoDAO
 * @see br.com.frequencia.dao.AbstractDAO
 * 
 */
public class OrgaoDAOImpl extends AbstractDAO<Orgao, Integer> implements OrgaoDAO {

	/**
	 * @param em
	 *            Recebe a referência para o <code>EntityManager</code>.
	 */
	public OrgaoDAOImpl(EntityManager em) {
		super(em);
	}

	@SuppressWarnings("unchecked")
	public List<Orgao> pesquisarOrgaoByNomeEqual(String nome) {
		if (nome == null || nome.isEmpty())
			return null;
		Query query = getPersistenceContext().createQuery("FROM Orgao o WHERE o.nomOrgao = :nomOrgao",Orgao.class);
		query.setParameter("nomOrgao", nome);
		return query.getResultList();
	}

}
