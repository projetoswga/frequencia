package br.com.frequencia.controller;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import br.com.frequencia.util.JPAUtil;

/**
 * Define uma <code>Controller</code> com funcionalidades para persistência.
 * 
 * <p>
 *  Responsável por carregar, disponibilizar e encerrar o <i>contexto de persistência</i> (<code>EntityManager</code>), 
 *  para as operações de persistência definidas na <code>Controller</code>.
 * </p>
 * 
 * @see br.com.frequencia.controller.AbstractController
 * 
 */
public abstract class PersistenceController extends AbstractController {

	public static final String MSG_SUCESSO = "Operação realizada com sucesso!";
	public static final String MSG_ERRO= "Erro ao tentar salvar o registro!";
	public static final String MSG_EXCLUSAO_REGISTRO = "Nenhum registro selecionado. Selecione um registro da lista para a exclução.";
	public static final String TITLE_ERROR = "Erro!";
	public static final String TITLE_INFORMACAO = "Informação!";
	public static final String TITLE_CONFIRMACAO = "Confirmação!";
	public static final String TITLE_VALIDACAO = "Validação!";
	public static final String FORMATO_HORA_USUARIO = "HH:mm:ss";
	public static final String FORMATO_HORA_BANCO = "HH:mm:ss";
	public static final String FORMATO_DATA_HORA_USUARIO = "dd/MM/yyyy HH:mm:ss";
	public static final String FORMATO_DATA_HORA_BANCO = "dd/MM/yyyy HH:mm:ss";
	private static Logger log = Logger.getLogger(PersistenceController.class);

    private EntityManager persistenceContext;
    private boolean ownsPersistenceContext;
	
    public PersistenceController(){ }
    
    public PersistenceController(AbstractController parent){
    	super(parent);
    }
    
    /**
     * Carrega um novo <i>contexto de persistência</i> (<code>EntityManager</code>).
     */
    protected void loadPersistenceContext() {
    	loadPersistenceContext(null);
    }
    
    /**
     * Carrega o <i>contexto de persistência</i> para uso na Controller. 
     * 
     * <p>
     *  Utiliza o contexto argumento, ou cria um novo senão for informado.
     *  Dessa forma é possível criar compartilhar o mesmo <code>EntityManager</code> em mais de uma <code>Controller</code>.
     *  Caso um novo <code>EntityManager</code> seja obtido, a <code>Controller</code> é considerada "dona" do <i>contexto de persistência</i>.
     * </p>
     * 
     * @param persistenceContext
     */
    protected void loadPersistenceContext(EntityManager persistenceContext) {
    	if (persistenceContext == null) {
            log.debug("Criando um contexto de persistência (EntityManager).");
            this.persistenceContext = JPAUtil.createEntityManager();
            this.ownsPersistenceContext = true;
        } else {
            log.debug("Utilizando contexto de persistência (EntityManager) existente.");
            this.persistenceContext = persistenceContext;
            this.ownsPersistenceContext = false;
        }
    }
    
    /**
     * @return referência para o <i>contexto de persistência</i> (<code>EntityManager</code>).
     */
    public EntityManager getPersistenceContext() {
		return this.persistenceContext;
	}
    
    /**
     * Libera o <code>EntityManager</code>, caso a <code>Controller</code> seja a "dona" do <i>contexto de persistência</i>.
     */
    @Override
    protected void cleanUp() {
        if (ownsPersistenceContext && getPersistenceContext().isOpen()) {
            log.debug("Fechando o contexto de persistência (EntityManager).");
            getPersistenceContext().close();
        }
        super.cleanUp();
    }
}
