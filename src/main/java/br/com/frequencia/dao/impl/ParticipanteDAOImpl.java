package br.com.frequencia.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.frequencia.dao.AbstractDAO;
import br.com.frequencia.dao.ParticipanteDAO;
import br.com.frequencia.model.Participante;

/**
 * Implementa o contrato de persistência da entidade <code>Participante</code>. Utiliza a herança para <code>AbstractDAO</code> para
 * resolver as operações básicas de cadastro com <code>JPA</code>.
 * 
 * @see br.com.frequencia.dao.ParticipanteDAO
 * @see br.com.frequencia.dao.AbstractDAO
 * 
 */
public class ParticipanteDAOImpl extends AbstractDAO<Participante, Integer> implements ParticipanteDAO {

	/**
	 * @param em
	 *            Recebe a referência para o <code>EntityManager</code>.
	 */
	public ParticipanteDAOImpl(EntityManager em) {
		super(em);
	}

	/**
	 * Pesquisa participante Novos (cadastrados localmente)
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Participante> pesquisarParticipantes(Participante participante) {
		Query query = null;
		Map<String, Object> mapaParametros = new HashMap<String, Object>();
		StringBuilder strquery = new StringBuilder("FROM Participante p where p.flgRecemCadastrado = :flgRecemCadastrado");
		mapaParametros.put("flgRecemCadastrado", Boolean.TRUE);
		if (participante != null) {
			if (participante.getNumInscricao() != null && !participante.getNumInscricao().equals("")) {
				strquery.append(" AND p.numInscricao LIKE :numInscricao");
				mapaParametros.put("numInscricao", "%".concat(participante.getNumInscricao()).concat("%"));
			}
			if (participante.getNomParticipante() != null && !participante.getNomParticipante().equals("")) {
				strquery.append(" AND p.nomParticipante LIKE :nomParticipante");
				mapaParametros.put("nomParticipante", "%".concat(participante.getNomParticipante()).concat("%"));
			}
			if (participante.getOrgao() != null && participante.getOrgao().getId() != null && participante.getOrgao().getId() != 0
					&& participante.getOrgao().getId() != -1) {
				strquery.append(" AND p.orgao.id = :idOrgao");
				mapaParametros.put("idOrgao", participante.getOrgao().getId());
			}
		}
		strquery.append(" order by p.nomParticipante");
		query = getPersistenceContext().createQuery(strquery.toString());
		for (Entry<String, Object> entry : mapaParametros.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Participante> pesquisarParticipantesInscritos(Participante participante) {
		Query query = null;
		Map<String, Object> mapaParametros = new HashMap<String, Object>();
		StringBuilder strquery = new StringBuilder("FROM Participante p where p.flgRecemCadastrado = :flgRecemCadastrado");
		mapaParametros.put("flgRecemCadastrado", Boolean.FALSE);
		if (participante != null) {
			if (participante.getNumInscricao() != null && !participante.getNumInscricao().equals("")) {
				strquery.append(" AND p.numInscricao LIKE :numInscricao");
				mapaParametros.put("numInscricao", "%".concat(participante.getNumInscricao()).concat("%"));
			}
			if (participante.getNomParticipante() != null && !participante.getNomParticipante().equals("")) {
				strquery.append(" AND p.nomParticipante LIKE :nomParticipante");
				mapaParametros.put("nomParticipante", "%".concat(participante.getNomParticipante()).concat("%"));
			}
			if (participante.getOrgao() != null && participante.getOrgao().getId() != null && participante.getOrgao().getId() != 0
					&& participante.getOrgao().getId() != -1) {
				strquery.append(" AND p.orgao.id = :idOrgao");
				mapaParametros.put("idOrgao", participante.getOrgao().getId());
			}
		}
		strquery.append(" order by p.nomParticipante");
		query = getPersistenceContext().createQuery(strquery.toString());
		for (Entry<String, Object> entry : mapaParametros.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Participante> pesquisarParticipantesNovos() {
		Query query = getPersistenceContext().createQuery("FROM Participante p WHERE p.flgRecemCadastrado = :flgRecemCadastrado");
		query.setParameter("flgRecemCadastrado", Boolean.TRUE);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Participante> pesquisarParticipantesInscritos() {
		Query query = getPersistenceContext().createQuery("FROM Participante p WHERE p.flgRecemCadastrado = :flgRecemCadastrado");
		query.setParameter("flgRecemCadastrado", Boolean.FALSE);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Boolean pesquisarNumInscricao(String numInscricao) {
		Query query = getPersistenceContext().createQuery("FROM Participante p WHERE p.numInscricao = :numInscricao");
		query.setParameter("numInscricao", numInscricao);
		List<Participante> participantes = query.getResultList();
		if (participantes != null && !participantes.isEmpty()) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Participante> pesquisarTodosParticipantes(Participante participante) {
		Query query = null;
		Map<String, Object> mapaParametros = new HashMap<String, Object>();
		StringBuilder strquery = new StringBuilder("FROM Participante p where 1=1");
		if (participante != null) {
			if (participante.getNumInscricao() != null && !participante.getNumInscricao().equals("")) {
				strquery.append(" AND p.numInscricao = :numInscricao");
				mapaParametros.put("numInscricao", participante.getNumInscricao());
			} else {
				if (participante.getNomParticipante() != null && !participante.getNomParticipante().equals("")) {
					strquery.append(" AND p.nomParticipante LIKE :nomParticipante");
					mapaParametros.put("nomParticipante", "%".concat(participante.getNomParticipante()).concat("%"));
				}
				if (participante.getOrgao() != null && participante.getOrgao().getId() != null && participante.getOrgao().getId() != 0
						&& participante.getOrgao().getId() != -1) {
					strquery.append(" AND p.orgao.id = :idOrgao");
					mapaParametros.put("idOrgao", participante.getOrgao().getId());
				}
			}
		}
		query = getPersistenceContext().createQuery(strquery.toString());
		for (Entry<String, Object> entry : mapaParametros.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Participante> pesquisarParticipantesNovosGeracaoArquivo() {
		StringBuilder strQuery = new StringBuilder();
		strQuery.append("FROM Participante p WHERE p.flgRecemCadastrado = :flgRecemCadastrado");
		Query query = getPersistenceContext().createQuery(strQuery.toString());
		query.setParameter("flgRecemCadastrado", Boolean.TRUE);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Participante> pesquisarParticipantesInscritosGeracaoArquivo() {
		StringBuilder strQuery = new StringBuilder();
		strQuery.append("FROM Participante p WHERE p.flgRecemCadastrado = :flgRecemCadastrado");
		strQuery.append(" AND p.flgAlterado = :flgAlterado");
		Query query = getPersistenceContext().createQuery(strQuery.toString());
		query.setParameter("flgRecemCadastrado", Boolean.FALSE);
		query.setParameter("flgAlterado", Boolean.TRUE);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Participante pesquisarParticipantes(String numInscricao) {
		Query query = getPersistenceContext().createQuery("FROM Participante p WHERE p.numInscricao = :numInscricao", Participante.class);
		query.setParameter("numInscricao", numInscricao);
		List<Participante> participantes = query.getResultList();
		if (null != participantes && !participantes.isEmpty())
			return participantes.get(0);
		return null;
	}

	public void importarParticipantes(List<String> listaSQL) {
		for (String sql : listaSQL) {
			String numInscricao = sql.split("[']")[1];
			Query query = getPersistenceContext().createQuery("FROM Participante p WHERE p.numInscricao = :numInscricao", Participante.class);
			query.setParameter("numInscricao", numInscricao);
			if (query.getResultList() != null && !query.getResultList().isEmpty()){
				Participante participante = (Participante) query.getResultList().get(0);
				participante.setFlgAlterado(Boolean.FALSE);
				getPersistenceContext().merge(participante);
			}else {
				query = getPersistenceContext().createNativeQuery(sql, Participante.class);
				query.executeUpdate();
			}
		}
	}

	/**
	 * idTurma é opcional
	 */
	@SuppressWarnings("unchecked")
	public List<Participante> pesquisarParticipantesTurma(Integer idTurma) {
		StringBuilder strquery = new StringBuilder("FROM Participante p");
		Query query = null;
		if (null != idTurma && idTurma != 0) {
			strquery.append(" where exists (select 1 from ParticipanteInscrito pi where pi.participante.id = p.id and pi.turma.id = :idTurma)");
			query = getPersistenceContext().createQuery(strquery.toString(), Participante.class);
			query.setParameter("idTurma", idTurma);
		} else {
			query = getPersistenceContext().createQuery(strquery.toString(), Participante.class);
		}
		List<Participante> participantes = query.getResultList();
		
		return participantes;
	}

	/**
	 * idTurma é opcional
	 */
	@SuppressWarnings("unchecked")
	public List<Participante> pesquisarParticipantes(Integer idTurma, Participante participante, Boolean recemCadastrado) {
		Query query = null;
		Map<String, Object> mapaParametros = new HashMap<String, Object>();
		StringBuilder strquery = new StringBuilder("FROM Participante p where 1 = 1 ");
		if (recemCadastrado) {
			strquery.append(" AND p.flgRecemCadastrado = :flgRecemCadastrado ");
			mapaParametros.put("flgRecemCadastrado", Boolean.TRUE);
		}
		if (participante != null) {
			if (participante.getNumInscricao() != null && !participante.getNumInscricao().equals("")) {
				strquery.append(" AND p.numInscricao = :numInscricao");
				mapaParametros.put("numInscricao", participante.getNumInscricao());
			}
			if (participante.getNomParticipante() != null && !participante.getNomParticipante().equals("")) {
				strquery.append(" AND p.nomParticipante LIKE :nomParticipante");
				mapaParametros.put("nomParticipante", "%".concat(participante.getNomParticipante()).concat("%"));
			}
			if (participante.getOrgao() != null && participante.getOrgao().getId() != null && participante.getOrgao().getId() != 0
					&& participante.getOrgao().getId() != -1) {
				strquery.append(" AND p.orgao.id = :idOrgao");
				mapaParametros.put("idOrgao", participante.getOrgao().getId());
			}
		}
		if (null != idTurma && idTurma != 0) {
			strquery.append(" AND exists (select 1 from ParticipanteInscrito pi where pi.participante.id = p.id and pi.turma.id = :idTurma)");
			mapaParametros.put("idTurma", idTurma);
		}

		strquery.append(" order by p.nomParticipante");

		query = getPersistenceContext().createQuery(strquery.toString(), Participante.class);
		for (Entry<String, Object> entry : mapaParametros.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}


		List<Participante> participantes = query.getResultList();
		
		return participantes;
	}
}
