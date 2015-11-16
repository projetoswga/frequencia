package br.com.frequencia.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.frequencia.dao.AbstractDAO;
import br.com.frequencia.dao.ParticipanteInscritoDAO;
import br.com.frequencia.model.Participante;
import br.com.frequencia.model.ParticipanteInscrito;

/**
 * Implementa o contrato de persistência da entidade <code>ParticipanteInscrito</code>. Utiliza a herança para <code>AbstractDAO</code> para
 * resolver as operações básicas de cadastro com <code>JPA</code>.
 * 
 * @see br.com.frequencia.dao.ParticipanteInscritoDAO
 * @see br.com.frequencia.dao.AbstractDAO
 * 
 */
public class ParticipanteInscritoDAOImpl extends AbstractDAO<ParticipanteInscrito, Integer> implements ParticipanteInscritoDAO {

	/**
	 * @param em
	 *            Recebe a referência para o <code>EntityManager</code>.
	 */
	public ParticipanteInscritoDAOImpl(EntityManager em) {
		super(em);
	}

	@SuppressWarnings("unchecked")
	public List<ParticipanteInscrito> pesquisarParticipanteInscritos(ParticipanteInscrito participanteInscrito) {
		Query query = null;
		Map<String, Object> mapaParametros = new HashMap<String, Object>();
		StringBuilder strquery = new StringBuilder(
				"FROM ParticipanteInscrito pi where pi.participante.flgRecemCadastrado = :flgRecemCadastrado");
		mapaParametros.put("flgRecemCadastrado", Boolean.FALSE);
		if (participanteInscrito != null) {
			if (participanteInscrito.getId() != null) {
				strquery.append(" AND pi.id = :id");
				mapaParametros.put("id", participanteInscrito.getId());
			}
			if (participanteInscrito.getParticipante() != null) {
				if (participanteInscrito.getParticipante().getId() != null && participanteInscrito.getParticipante().getId() != -1) {
					strquery.append(" AND pi.participante.id = :idParticipante");
					mapaParametros.put("idParticipante", participanteInscrito.getParticipante().getId());
				}
				if (participanteInscrito.getParticipante().getNomCracha() != null
						&& !participanteInscrito.getParticipante().getNomCracha().equals("")) {
					strquery.append(" AND pi.participante.nomCracha LIKE :nomCracha");
					mapaParametros.put("nomCracha", "%".concat(participanteInscrito.getParticipante().getNomCracha()).concat("%"));
				}
				if (participanteInscrito.getParticipante().getNomParticipante() != null
						&& !participanteInscrito.getParticipante().getNomParticipante().equals("")) {
					strquery.append(" AND pi.participante.nomParticipante LIKE :nomParticipante");
					mapaParametros.put("nomParticipante",
							"%".concat(participanteInscrito.getParticipante().getNomParticipante()).concat("%"));
				}
				if (participanteInscrito.getParticipante().getNumInscricao() != null
						&& !participanteInscrito.getParticipante().getNumInscricao().equals("")) {
					strquery.append(" AND pi.participante.numInscricao LIKE :numInscricao");
					mapaParametros.put("numInscricao", "%".concat(participanteInscrito.getParticipante().getNumInscricao()).concat("%"));
				}
				if (participanteInscrito.getParticipante().getOrgao() != null
						&& participanteInscrito.getParticipante().getOrgao().getId() != null
						&& participanteInscrito.getParticipante().getOrgao().getId() != 0
						&& participanteInscrito.getParticipante().getOrgao().getId() != -1) {
					strquery.append(" AND pi.participante.orgao.id = :idOrgao");
					mapaParametros.put("idOrgao", participanteInscrito.getParticipante().getOrgao().getId());
				}
				if (participanteInscrito.getParticipante().getUsuario() != null
						&& participanteInscrito.getParticipante().getUsuario().getId() != null) {
					strquery.append(" AND pi.participante.usuario.id = :idUsuario");
					mapaParametros.put("idUsuario", participanteInscrito.getParticipante().getUsuario().getId());
				}
			}
			if (participanteInscrito.getTurma() != null && participanteInscrito.getTurma().getId() != null
					&& participanteInscrito.getTurma().getId() != -1) {
				strquery.append(" AND pi.participante.turma.id = :idTurma");
				mapaParametros.put("idTurma", participanteInscrito.getTurma().getId());
			}
		}
		query = getPersistenceContext().createQuery(strquery.toString());
		for (Entry<String, Object> entry : mapaParametros.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ParticipanteInscrito> pesquisarParticipanteInscritos(Participante participante) {
		Query query = null;
		Map<String, Object> mapaParametros = new HashMap<String, Object>();
		StringBuilder strquery = new StringBuilder(
				"FROM ParticipanteInscrito pi where pi.participante.flgRecemCadastrado = :flgRecemCadastrado");
		mapaParametros.put("flgRecemCadastrado", Boolean.FALSE);
		if (participante != null) {
			if (participante.getNumInscricao() != null && !participante.getNumInscricao().equals("")) {
				strquery.append(" AND pi.participante.numInscricao LIKE :numInscricao");
				mapaParametros.put("numInscricao", "%".concat(participante.getNumInscricao()).concat("%"));
			}
			if (participante.getNomParticipante() != null && !participante.getNomParticipante().equals("")) {
				strquery.append(" AND pi.participante.nomParticipante LIKE :nomParticipante");
				mapaParametros.put("nomParticipante", "%".concat(participante.getNomParticipante()).concat("%"));
			}
			if (participante.getOrgao() != null && participante.getOrgao().getId() != null && participante.getOrgao().getId() != 0
					&& participante.getOrgao().getId() != -1) {
				strquery.append(" AND pi.participante.orgao.id = :idOrgao");
				mapaParametros.put("idOrgao", participante.getOrgao().getId());
			}
		}
		
		strquery.append(" order by pi.turma.horario.descHorario");
		/** @TODO o correto seria o order by abaixo */ 
		//strquery.append(" order by pi.turma.horario.hrInicial");
		
		query = getPersistenceContext().createQuery(strquery.toString());
		for (Entry<String, Object> entry : mapaParametros.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ParticipanteInscrito> pesquisarParticipantesInscritos() {
		Query query = getPersistenceContext().createQuery(
				"FROM ParticipanteInscrito pi WHERE pi.participante.flgRecemCadastrado = :flgRecemCadastrado");
		query.setParameter("flgRecemCadastrado", Boolean.FALSE);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ParticipanteInscrito> pesquisarParticipanteInscritos(String numInscricao) {
		Query query = getPersistenceContext().createQuery(
				"FROM ParticipanteInscrito pi WHERE pi.participante.numInscricao LIKE :numInscricao");
		query.setParameter("numInscricao", numInscricao);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ParticipanteInscrito> pesquisarParticipanteInscritosPorTurma(Integer idTurma) {
		Query query = getPersistenceContext().createQuery("FROM ParticipanteInscrito pi WHERE pi.turma.id = :idTurma order by pi.participante.nomParticipante");
		query.setParameter("idTurma", idTurma);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ParticipanteInscrito> pesquisarParticipanteInscritosPorTurmaInscNomeNovos(Integer idTurma,Participante participante) {
		Query query = null;
		Map<String, Object> mapaParametros = new HashMap<String, Object>();
		StringBuilder strquery = new StringBuilder(
				"FROM ParticipanteInscrito pi where 1 = 1 ");
		if (null != idTurma && idTurma > 0){
			strquery.append(" AND pi.turma.id = :idTurma ");
			mapaParametros.put("idTurma", idTurma);
		}
		if (participante != null) {
			if (participante.getNumInscricao() != null && !participante.getNumInscricao().equals("")) {
				strquery.append(" AND pi.participante.numInscricao LIKE :numInscricao");
				mapaParametros.put("numInscricao", "%".concat(participante.getNumInscricao()).concat("%"));
			}
			if (participante.getNomParticipante() != null && !participante.getNomParticipante().equals("")) {
				strquery.append(" AND pi.participante.nomParticipante LIKE :nomParticipante");
				mapaParametros.put("nomParticipante", "%".concat(participante.getNomParticipante()).concat("%"));
			}
			if (participante.getOrgao() != null && participante.getOrgao().getId() != null && participante.getOrgao().getId() != 0
					&& participante.getOrgao().getId() != -1) {
				strquery.append(" AND pi.participante.orgao.id = :idOrgao");
				mapaParametros.put("idOrgao", participante.getOrgao().getId());
			}
		}
		strquery.append(" order by pi.participante.nomParticipante");
		query = getPersistenceContext().createQuery(strquery.toString());
		for (Entry<String, Object> entry : mapaParametros.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		return query.getResultList();
	}
}
