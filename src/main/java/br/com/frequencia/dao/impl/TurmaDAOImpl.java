package br.com.frequencia.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.frequencia.dao.AbstractDAO;
import br.com.frequencia.dao.TurmaDAO;
import br.com.frequencia.model.Turma;

/**
 * Implementa o contrato de persistência da entidade <code>Turma</code>. Utiliza a herança para <code>AbstractDAO</code> para resolver as
 * operações básicas de cadastro com <code>JPA</code>.
 * 
 * @see br.com.frequencia.dao.TurmaDAO
 * @see br.com.frequencia.dao.AbstractDAO
 * 
 */
public class TurmaDAOImpl extends AbstractDAO<Turma, Integer> implements TurmaDAO {

	/**
	 * @param em
	 *            Recebe a referência para o <code>EntityManager</code>.
	 */
	public TurmaDAOImpl(EntityManager em) {
		super(em);
	}

	/**
	 * Reliza a pesquisa Turmas com filtro no nome (via operador <code>like</code>).
	 * 
	 * @see br.com.dao.TurmaDAO#getTurmasByNome(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Turma> getTurmasByNome(String nome) {
		if (nome == null || nome.isEmpty())
			return null;
		Query query = getPersistenceContext().createQuery("FROM Turma o WHERE o.nomTurma like :nomTurma");
		query.setParameter("nomTurma", nome.concat("%"));
		return (List<Turma>) query.getResultList();
	}

	/**
	 * Reliza a pesquisa Turmas com filtro no nome (via operador <code>like</code>).
	 * 
	 * @see br.com.dao.TurmaDAO#getTurmasByNome(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Turma> getTurmasByNomeEqual(String nome) {
		if (nome == null || nome.isEmpty())
			return null;
		Query query = getPersistenceContext().createQuery("FROM Turma o WHERE o.nomTurma = :nomTurma",Turma.class);
		query.setParameter("nomTurma", nome);
		return query.getResultList();
	}

	/**
	 * Reliza a pesquisa Turmas com filtro no nome (via operador <code>like</code>).
	 * 
	 * @see br.com.dao.TurmaDAO#getTurmasByNome(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public boolean existsTurmaByNomeEqual(String nome) {
		if (nome == null || nome.isEmpty())
			return false;
		Query query = getPersistenceContext().createQuery("FROM Turma o WHERE o.nomTurma = :nomTurma",Turma.class);
		query.setParameter("nomTurma", nome);
		List<Turma> results = query.getResultList();
		return (results != null && !results.isEmpty());
	}
	
	@SuppressWarnings("unchecked")
	public List<Turma> pesquisarTurmas(Integer idEvento) {
		Query query = getPersistenceContext().createQuery("FROM Turma o WHERE o.evento.id = :idEvento");
		query.setParameter("idEvento", idEvento);
		return (List<Turma>) query.getResultList();
	}
	
	/**
	 * Pesquisa Turma apartir do Horario, Oficina, Sala
	 */
	@SuppressWarnings("unchecked")
	public Turma pesquisaTurmaPorHorarioSalaOficina(String horario, String sala,
			String oficina) {
		Query query = getPersistenceContext().createQuery(
				"FROM Turma t WHERE t.nomDisciplina like :nomDisciplina AND t.sala.nomSala like :nomSala AND t.horario.descHorario = :nomHorario", Turma.class);
		query.setParameter("nomDisciplina", "%".concat(oficina).concat("%"));
		//pega apenas metade da descrição da sala... normalmente no final do nome estão repetindo informação diferente daquela presente na grade 
		query.setParameter("nomSala", "%".concat(sala).concat("%"));
		query.setParameter("nomHorario", horario);
		List<Turma> turmas = query.getResultList();
		if (null == turmas || turmas.isEmpty()) return null;
				
		return turmas.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<Turma> getAll() {
		Query query = getPersistenceContext().createQuery("FROM Turma t order by t.nomTurma");
		return query.getResultList();
	}

}
