package br.com.frequencia.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

/**
 * Durante a execução do projeto é possível trabalhar com várias sessões com o banco de dados. Na JPA a sessão com o banco é representa pelo
 * componente EntityManager.
 * 
 * <p>
 * Essa classe é responsavel por disponibilizar o(s) componente(s) <code>EntityManager</code>. Utilizamos o
 * <code>EntityManagerFactory</code> fornecedor (fábrica) de sessões com o banco de dados.
 * </p>
 * 
 * <p>
 * Caso não consiga carregar a fabrica de sessões, problemas com a conexão com banco ou não encontrou configurador, a execução da aplicação
 * é interrompida (lança <code>ExceptionInInitializerError</code>).
 * </p>
 * 
 * @see persistence.xml
 * 
 */
public class JPAUtil {

	/** Unidade de persistencia definida no arquivo <code>persistence.xml</code> */
	private static final String PERSISTENCE_UNIT_NAME = "frequencia";

	private static EntityManagerFactory emf;

	static {
		try {
			emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	/**
	 * @return Cria e retorna um componente <code>EntityManager</code>.
	 * @throws Lança
	 *             <code>RuntimeException</code> se <code>EntityManagerFactory</code> estiver fechada.
	 */
	public static EntityManager createEntityManager() {
		if (!emf.isOpen())
			throw new PersistenceException("EntityManagerFactory está fechada!");
		return emf.createEntityManager();
	}

	/**
	 * Fecha o <code>EntityManagerFactory</code>.
	 */
	public static void closeEntityManagerFactory() {
		if (emf.isOpen()) {
			emf.close();
		}
	}

}
