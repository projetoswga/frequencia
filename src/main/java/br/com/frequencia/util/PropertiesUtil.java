package br.com.frequencia.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

	private static Properties props = null;

	private static Properties getProperties() throws IOException {
		if (props == null) {
			InputStream i = PropertiesUtil.class.getResourceAsStream("/configuracao.properties");
			props = new Properties();
			props.load(i);
			i.close();
		}
		return props;
	}

	public static String getProperty(String chave) {
		try {
			return getProperties().getProperty(chave);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}