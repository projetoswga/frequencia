package br.com.frequencia.dao.impl;

import javax.persistence.EntityManager;

import br.com.frequencia.dao.AbstractDAO;
import br.com.frequencia.dao.HorarioDAO;
import br.com.frequencia.model.Horario;

/**
 * Implementa o contrato de persistência da entidade <code>Horario</code>. Utiliza a herança para <code>AbstractDAO</code> para resolver as
 * operações básicas de cadastro com <code>JPA</code>.
 * 
 * @see br.com.frequencia.dao.HorarioDAO
 * @see br.com.frequencia.dao.AbstractDAO
 * 
 */
public class HorarioDAOImpl extends AbstractDAO<Horario, Integer> implements HorarioDAO {

	/**
	 * @param em
	 *            Recebe a referência para o <code>EntityManager</code>.
	 */
	public HorarioDAOImpl(EntityManager em) {
		super(em);
	}
}
