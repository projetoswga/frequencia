package br.com.frequencia.dao.impl;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.frequencia.dao.AbstractDAO;
import br.com.frequencia.dao.FrequenciaDAO;
import br.com.frequencia.model.Frequencia;

/**
 * Implementa o contrato de persistência da entidade <code>Frequencia</code>. Utiliza a herança para <code>AbstractDAO</code> para resolver
 * as operações básicas de cadastro com <code>JPA</code>.
 * 
 * @see br.com.frequencia.dao.FrequenciaDAO
 * @see br.com.frequencia.dao.AbstractDAO
 * 
 */
public class FrequenciaDAOImpl extends AbstractDAO<Frequencia, Integer> implements FrequenciaDAO {

	/**
	 * @param em
	 *            Recebe a referência para o <code>EntityManager</code>.
	 */
	public FrequenciaDAOImpl(EntityManager em) {
		super(em);
	}

	public Frequencia pesquisarFrequencia(Frequencia frequencia) {
		Query query = null;
		Map<String, Object> mapaParametros = new HashMap<String, Object>();
		StringBuilder strquery = new StringBuilder("SELECT MAX(f.id) FROM Frequencia f where f.participante.numInscricao = :numInscricao");
		mapaParametros.put("numInscricao", frequencia.getParticipante().getNumInscricao());
		if (frequencia.getParticipante().getNomParticipante() != null && !frequencia.getParticipante().getNomParticipante().equals("")) {
			strquery.append(" AND f.participante.nomParticipante LIKE :nomParticipante");
			mapaParametros.put("nomParticipante", "%".concat(frequencia.getParticipante().getNomParticipante()).concat("%"));
		}
		if (frequencia.getParticipante().getOrgao() != null && frequencia.getParticipante().getOrgao().getId() != null
				&& frequencia.getParticipante().getOrgao().getId() != 0 && frequencia.getParticipante().getOrgao().getId() != -1) {
			strquery.append(" AND f.participante.orgao.id = :idOrgao");
			mapaParametros.put("idOrgao", frequencia.getParticipante().getOrgao().getId());
		}
		if (frequencia.getTurma() != null && frequencia.getTurma().getId() != null) {
			strquery.append(" AND f.turma.id = :idTurma");
			mapaParametros.put("idTurma", frequencia.getTurma().getId());
		}
		query = getPersistenceContext().createQuery(strquery.toString());
		for (Entry<String, Object> entry : mapaParametros.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		Integer ultimoRegistro = query.getFirstResult();
		if (ultimoRegistro != null) {
			return (Frequencia) getPersistenceContext().createQuery("FROM Frequencia f where f.id = " + ultimoRegistro).getSingleResult();
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Frequencia> pesquisarFrequencias(String numInscricao) {
		Query query = getPersistenceContext().createQuery(
				"FROM Frequencia f WHERE f.participante.numInscricao = :numInscricao ORDER BY f.id ASC");
		query.setParameter("numInscricao", numInscricao);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Frequencia> pesquisarFrequenciasParticipanteTurma(String numInscricao, Integer idTurma) {
		Query query = getPersistenceContext().createQuery(
				"FROM Frequencia f WHERE f.participante.numInscricao = :numInscricao AND f.turma.id = :idTurma ORDER BY f.id ASC");
		query.setParameter("numInscricao", numInscricao);
		query.setParameter("idTurma", idTurma);
		return query.getResultList();
	}

	/**
	 * Recuperar todas as frequencia de um Candidato em um determinado dia
	 * @param numInscricao
	 * @param idTurma
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Frequencia> pesquisarFrequenciasParticipanteData(String numInscricao, Calendar dataEntrada) {
		Calendar dataEntradaInicio = new GregorianCalendar(dataEntrada.get(Calendar.YEAR), dataEntrada.get(Calendar.MONTH), dataEntrada.get(Calendar.DAY_OF_MONTH),0,0);
		Calendar dataEntradaFim    = new GregorianCalendar(dataEntrada.get(Calendar.YEAR), dataEntrada.get(Calendar.MONTH), dataEntrada.get(Calendar.DAY_OF_MONTH),23,59);

		Query query = getPersistenceContext().createQuery(
				"FROM Frequencia f WHERE f.participante.numInscricao = :numInscricao AND f.horarioEntrada between :dataEntradaInicio and :dataEntradaFim ORDER BY f.horarioEntrada ASC");
		query.setParameter("numInscricao", numInscricao);
		query.setParameter("dataEntradaInicio", dataEntradaInicio);
		query.setParameter("dataEntradaFim", dataEntradaFim);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Frequencia> pesquisarFrequencias() {
		Query query = getPersistenceContext().createQuery("SELECT DISTINCT f.id FROM Frequencia f GROUP BY f.participante.id");
		StringBuilder ids = new StringBuilder();
		List<Integer> listaIdsFrequencias = query.getResultList();
		if (listaIdsFrequencias != null && !listaIdsFrequencias.isEmpty()) {
			for (Integer id : listaIdsFrequencias) {
				ids.append("," + id);
			}
			query = getPersistenceContext().createQuery("FROM Frequencia f WHERE f.id in(" + ids.substring(1, ids.length()) + ")");
			return query.getResultList();
		} else {
			return null;
		}
	}

	/**
	 * I don't understand!
	 */
	@SuppressWarnings("unchecked")
	public List<Frequencia> pesquisarFrequencias(Integer idTurma) {
		Query query = getPersistenceContext().createQuery(
				"SELECT DISTINCT f.id FROM Frequencia f WHERE f.turma.id = :idTurma GROUP BY f.participante.id");
		query.setParameter("idTurma", idTurma);
		StringBuilder ids = new StringBuilder();
		List<Integer> listaIdsFrequencias = query.getResultList();
		if (listaIdsFrequencias != null && !listaIdsFrequencias.isEmpty()) {
			for (Integer id : listaIdsFrequencias) {
				ids.append("," + id);
			}
			query = getPersistenceContext().createQuery("FROM Frequencia f WHERE f.id in(" + ids.substring(1, ids.length()) + ")");
			return query.getResultList();
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Frequencia> pesquisarFrequencias(List<Integer> idsTurmasParaRecarregarlistaFrequencia) {
		Query query = getPersistenceContext().createQuery(
				"SELECT DISTINCT f.id FROM Frequencia f WHERE f.turma.id in ( :idsTurmasParaRecarregarlistaFrequencia ) GROUP BY f.participante.id");
		query.setParameter("idsTurmasParaRecarregarlistaFrequencia", idsTurmasParaRecarregarlistaFrequencia);
		StringBuilder ids = new StringBuilder();
		List<Integer> listaIdsFrequencias = query.getResultList();
		if (listaIdsFrequencias != null && !listaIdsFrequencias.isEmpty()) {
			for (Integer id : listaIdsFrequencias) {
				ids.append("," + id);
			}
			query = getPersistenceContext().createQuery("FROM Frequencia f WHERE f.id in(" + ids.substring(1, ids.length()) + ")");
			return query.getResultList();
		} else {
			return null;
		}
	}
	
	/**
	 * Recuperar todas as <Frequencia> em aberto de uma determinada <Turma>
	 */
	@SuppressWarnings("unchecked")
	public List<Frequencia> pesquisarFrequenciasAbertas(Integer idTurma) {
		Query query = getPersistenceContext().createQuery("FROM Frequencia f WHERE f.horarioSaida is null and f.turma.id = :idTurma order by f.horarioEntrada");
		query.setParameter("idTurma", idTurma);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Frequencia> pesquisarFrequenciasFinalizadas() {
		Query query = getPersistenceContext().createQuery("FROM Frequencia f WHERE f.horarioEntrada is not null and f.horarioSaida is not null",Frequencia.class);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public boolean existeFrequenciaPartTurmaHorEntrada(Integer idParticipante,Integer idTurma, Calendar dataHoraEntrada) {
		Query query = getPersistenceContext().createQuery(
				"FROM Frequencia f WHERE f.participante.id = :idParticipante AND f.turma.id = :idTurma AND f.horarioEntrada = :dataHoraEntrada",Frequencia.class);
		query.setParameter("idParticipante", idParticipante);
		query.setParameter("idTurma", idTurma);
		query.setParameter("dataHoraEntrada", dataHoraEntrada);
		List<Frequencia> frequencias = query.getResultList();
		return (null != frequencias && !frequencias.isEmpty());
	}

}
