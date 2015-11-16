package br.com.frequencia.app;

import java.util.Locale;

import br.com.frequencia.controller.PrincipalController;
import br.com.frequencia.util.DatabaseCriation;

/**
 * Ponto de entrada da aplicação.
 * 
 */
public class Main {

	public static void main(String[] args) {
		try {
			Locale.setDefault(new Locale("pt", "BR"));
			DatabaseCriation.checkDatabase();
			new PrincipalController();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
