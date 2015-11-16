package br.com.frequencia.dao;

import java.util.List;

import br.com.frequencia.model.AbstractEntity;

/**
 * Contrato de persistência para <code>Entity</code>.
 * 
 * <p>
 * Define as operações basicas de cadastro (CRUD), seguindo o design pattern <code>Data Access Object</code>.
 * </p>
 * 
 */
public interface BaseDAO<Entity extends AbstractEntity> {

	/**
	 * Faz a inserção ou atualização do Entity na base de dados.
	 * 
	 * @param Entity
	 * @return referência atualizada do objeto.
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	Entity save(Entity e);

	/**
	 * Exclui o registro do Entity na base de dados
	 * 
	 * @param Entity
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	void remove(Entity evento);

	/**
	 * @return Lista com todos os Entitys cadastrados no banco de dados.
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	List<Entity> getAll();

	/**
	 * @param id
	 *            filtro da pesquisa.
	 * @return Entity com filtro no id, caso nao exista retorna <code>null</code>.
	 * @throws <code>RuntimeException</code> se algum problema ocorrer.
	 */
	Entity findById(Integer id);

}
