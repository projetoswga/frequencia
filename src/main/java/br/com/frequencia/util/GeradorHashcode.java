package br.com.frequencia.util;

import java.util.Calendar;

public class GeradorHashcode {
	
	/**
	 * Gera o numero de inscrição do candidado baseado no hashcode da hora com milisegundos. Porém só pega os numeros apartir da segunda
	 * posição até a sétima.
	 * 
	 * <p>Formato:</p>
	 * <ul>
	 * <li>Dois últimos dígitos do Ano</li>
	 * <li>O Mes antecedido de 0(zero) caso tenha 1 único dígito</li>
	 * <li>6 dígitos do <code>Calendar</code> em timeInMillis</li>
	 * </ul>
	 * 
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static String gerarHashcode() {
		StringBuilder hashcode = new StringBuilder();
		Calendar calendar = Calendar.getInstance();
		Long horario = calendar.getTimeInMillis();
		Integer hash = horario.hashCode();
		Integer ano = calendar.get(calendar.YEAR);
		Integer mes = calendar.get(calendar.MONTH);
		mes += 1;
		hashcode.append(ano.toString().substring(2));
		if (mes.toString().length() == 1) {
			hashcode.append("0" + mes.toString());
		} else {
			hashcode.append(mes.toString());
		}
		hashcode.append(hash.toString().substring(1, 7));
		return hashcode.toString();
	}
}
