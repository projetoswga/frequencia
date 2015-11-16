package br.com.frequencia.relatorio.util;

public class ValidacaoUtil {

	@SuppressWarnings("static-access")
	public static boolean doubleValido(String doubleToString) {

		if (doubleToString == null || doubleToString.trim().equals("")) {
			return false;
		}

		int qtdPonto = 0;
		int qtdVirgula = 0;
		int cont = 0;
		for (Character numero : doubleToString.toCharArray()) {
			if (!numero.isDigit(numero) && !numero.equals('.') && !numero.equals(',')) {
				return false;
			} else if (numero.equals('.')) {
				// primera posicao com ponto
				if (cont == 0) {
					return false;
				}
				qtdPonto++;
			} else if (numero.equals(',')) {
				// primera posicao com virgula
				if (cont == 0) {
					return false;
				}
				qtdVirgula++;
			}
			cont++;
		}
		if (qtdPonto > 1 || qtdVirgula > 1) {
			return false;
		}

		return true;
	}
}
