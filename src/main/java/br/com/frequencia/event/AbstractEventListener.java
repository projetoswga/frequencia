package br.com.frequencia.event;

/**
 * Contrato para componentes com a capacidade de definir tratamento adequado para um evento.
 * 
 * <p>
 *  Em conjunto com <code>AbstractController</code> e <code>AbstractEvent</code>, esse componente 
 *  é parte do trecho que implementa o design pattern <strong>Observer</strong>.
 * </p>
 * 
 * <p><code>AbstractEventListener</code> atua como <i>observador</i>.</p>
 * 
 * @see br.com.frequencia.event.AbstractEvent
 * 
 * @param <M> tipo do Evento que deverá ser tratado.
 */
public interface AbstractEventListener<M extends AbstractEvent<?>> {

	 public void handleEvent(M event);

}
