package br.com.frequencia.relatorio.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

/**
 * @author Igor Galv�o <br/>
 *         Class Util para gerar Relatorios.<br/>
 *         Pode ser exibido no browser
 * @version 1.0
 * @date 23/09/2011
 */

public class RelatorioUtil {

	public static final String CAMINHO_SAIDA = "C:\\Frequencia\\relatorios\\";

	static {
		new File(CAMINHO_SAIDA).mkdirs();
	}

	/**
	 * 
	 * @param lista
	 * @param parametros
	 * @param caminho
	 * @param nomePDF
	 * @param formato
	 *            - Excel, PDF e HTML
	 * @throws JRException
	 * @throws IOException
	 * @funcionalidade: Gerar relatorio com lista e parametos
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void gerarRelatorio(Collection lista, Map parametros, InputStream stream, String nome, String formato)
			throws JRException, IOException {

		/**
		 * @TODO Deixar output dinânmico
		 */
		OutputStream streamSaida = new FileOutputStream(CAMINHO_SAIDA + nome + "." + formato);
		try {
			/* Cria a lista do relatorio */
			JRDataSource colecao = new JRBeanCollectionDataSource(lista == null ? new ArrayList() : lista);
			JasperPrint jasperPrint = JasperFillManager.fillReport(stream, parametros, colecao);
			JRExporter jrExporter = getExporter(formato);
			jrExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			jrExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, streamSaida);
			jrExporter.exportReport();
		} finally {
			stream.close();
			streamSaida.flush();
			streamSaida.close();
		}
	}

	/**
	 * 
	 * @param parametros
	 * @param caminho
	 * @param nome
	 *            - Sem extensão. ex: NomePDF
	 * @param formato
	 *            - Excel, PDF e HTML
	 * @throws JRException
	 * @throws IOException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void gerarRelatorio(Map parametros, InputStream streamJasper, String nome, String formato) throws JRException,
			IOException {
		/**
		 * @TODO Deixar output dinânmico
		 */
		OutputStream streamSaida = new FileOutputStream(CAMINHO_SAIDA + nome + "." + formato);
		try {

			/* Quando nao passa a lista da erro */
			/* Para resolver vou criar uma lista sem nada */
			Collection lista = new ArrayList();
			lista.add(new Object());
			JRDataSource colecao = new JRBeanCollectionDataSource(lista);
			JasperPrint jasperPrint = JasperFillManager.fillReport(streamJasper, parametros, colecao);
			JRExporter jrExporter = getExporter(formato);

			jrExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			jrExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, streamSaida);
			jrExporter.exportReport();
		} finally {
			streamJasper.close();
			streamSaida.flush();
			streamSaida.close();
		}

	}

	/**
	 * 
	 * @param lista
	 * @param parametros
	 * @param caminho
	 * @param nomePDF
	 * @throws JRException
	 * @throws IOException
	 * @funcionalidade: Gerar relatorio com Lista
	 */
	@SuppressWarnings("rawtypes")
	public static void gerarRelatorio(Collection lista, String caminho, String nome, String formato) throws JRException, IOException {
		/**
		 * @TODO Deixar output dinânmico
		 */
		OutputStream streamSaida = new FileOutputStream("C:\\Frequencia\\relatorios\\saida." + formato);
		InputStream stream = new FileInputStream(caminho);
		try {

			/* Cria a lista do relatorio */
			JRDataSource dataSource = new JRBeanCollectionDataSource(lista == null ? new ArrayList() : lista);
			JasperPrint jasperPrint = JasperFillManager.fillReport(stream, new HashMap<String, Object>(), dataSource);
			JRExporter jrExporter = getExporter(formato);
			jrExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			jrExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, streamSaida);
			jrExporter.exportReport();
		} finally {
			stream.close();
			streamSaida.flush();
			streamSaida.close();
		}
	}

	/**
	 * 
	 * @param lista
	 * @param parametros
	 * @param caminho
	 * @param nomePDF
	 * @throws JRException
	 * @throws IOException
	 * @funcionalidade: Gerar relatorio com Lista
	 */
	@SuppressWarnings("rawtypes")
	public static void gerarRelatorio(Collection lista, InputStream streamJasper, String nome, String formato, boolean useSpaceWhite)
			throws JRException, IOException {
		/**
		 * @TODO Deixar output dinânmico
		 */
		OutputStream streamSaida = new FileOutputStream(CAMINHO_SAIDA + nome + "." + formato);
		try {
			/* Cria a lista do relatorio */
			JRDataSource dataSource = new JRBeanCollectionDataSource(lista == null ? new ArrayList() : lista);
			JasperPrint jasperPrint = JasperFillManager.fillReport(streamJasper, new HashMap<String, Object>(), dataSource);
			JRExporter jrExporter = getExporter(formato, useSpaceWhite);
			jrExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			jrExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, streamSaida);
			jrExporter.exportReport();
		} finally {
			streamJasper.close();
			streamSaida.flush();
			streamSaida.close();
		}
	}

	/**
	 * 
	 * @param lista
	 * @param parametros
	 * @param caminho
	 * @param nomePDF
	 * @throws JRException
	 * @throws IOException
	 * @funcionalidade: Gerar relatorio
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void gerarRelatorio(String caminho, String nome, String formato) throws JRException, IOException {
		/**
		 * @TODO Deixar output dinânmico
		 */
		OutputStream streamSaida = new FileOutputStream("C:\\Frequencia\\relatorios\\saida." + formato);
		InputStream stream = new FileInputStream(caminho);
		try {
			/* Quando nao passa a lista da erro */
			/* Para resolver vou criar uma lista sem nada */
			Collection lista = new ArrayList();
			lista.add(new Object());
			JRDataSource colecao = new JRBeanCollectionDataSource(lista);
			JasperPrint jasperPrint = JasperFillManager.fillReport(stream, new HashMap<String, Object>(), colecao);
			JRExporter jrExporter = getExporter(formato);
			jrExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			jrExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, streamSaida);
			jrExporter.exportReport();
		} finally {
			stream.close();
			streamSaida.flush();
			streamSaida.close();
		}
	}

	/**
	 * @param lista
	 * @param caminho
	 * @param nomeXLS
	 * @throws JRException
	 * @throws IOException
	 * @funcionalidade: Gerar relatorio
	 */
	@SuppressWarnings("rawtypes")
	public static void gerarRelatorioExcel(Collection lista, String caminho, String nomeXLS) throws JRException, IOException {
		/**
		 * @TODO Deixar output dinânmico
		 */
		OutputStream streamSaida = new FileOutputStream("C:\\Frequencia\\relatorios\\saida." + nomeXLS);
		InputStream stream = new FileInputStream(caminho);
		try {

			/* Cria a lista do relatorio */
			JRDataSource dataSource = new JRBeanCollectionDataSource(lista == null ? new ArrayList() : lista);
			JasperPrint jasperPrint = JasperFillManager.fillReport(stream, new HashMap<String, Object>(), dataSource);
			JRExporter jrExporter = new JRXlsExporter();
			jrExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			jrExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, streamSaida);
			jrExporter.exportReport();
		} finally {
			stream.close();
			streamSaida.flush();
			streamSaida.close();
		}
	}

	/**
	 * Definir o Tipo do relatorio. HTML, PDF, EXCEL
	 * 
	 * @param formato
	 * @return
	 */
	private static JRExporter getExporter(String formato) {
		JRExporter exporter = null;

		if (Constantes.HTML.toString().trim().equalsIgnoreCase(formato.toString().trim())) {
			exporter = new JRHtmlExporter();
			complementaHtmlExport(exporter);

		}
		if (formato == null || formato.isEmpty() || Constantes.PDF.toString().trim().equalsIgnoreCase(formato.toString().trim())) {
			exporter = new JRPdfExporter();

		}
		if (Constantes.EXCEL.toString().trim().equalsIgnoreCase(formato.toString().trim())) {
			exporter = new JRXlsExporter();
			complementaExcelExport(exporter);

		}

		return exporter;
	}

	/**
	 * Definir o Tipo do relatorio. HTML, PDF, EXCEL
	 * 
	 * @param formato
	 * @return
	 */
	private static JRExporter getExporter(String formato, boolean useSpaceWhite) {
		JRExporter exporter = null;

		if (Constantes.HTML.toString().trim().equalsIgnoreCase(formato.toString().trim())) {
			exporter = new JRHtmlExporter();
			complementaHtmlExport(exporter);

		}
		if (formato == null || formato.isEmpty() || Constantes.PDF.toString().trim().equalsIgnoreCase(formato.toString().trim())) {
			exporter = new JRPdfExporter();

		}
		if (Constantes.EXCEL.toString().trim().equalsIgnoreCase(formato.toString().trim())) {
			exporter = new JRXlsExporter();
			complementaExcelExport(exporter, useSpaceWhite);

		}

		return exporter;
	}

	private static void complementaExcelExport(JRExporter exporter, boolean useSpaceWhite) {
		exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
		if (useSpaceWhite) {
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.FALSE);
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.FALSE);
		}
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.FALSE);
	}

	private static void complementaExcelExport(JRExporter exporter) {
		exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.FALSE);
	}

	private static void complementaHtmlExport(JRExporter exporter) {
		exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "imagemServlet?image=");
		exporter.setParameter(JRHtmlExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
		exporter.setParameter(JRHtmlExporterParameter.IS_WRAP_BREAK_WORD, Boolean.TRUE);
		exporter.setParameter(JRHtmlExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.TRUE);
		exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
		exporter.setParameter(JRHtmlExporterParameter.SIZE_UNIT, JRHtmlExporterParameter.SIZE_UNIT_POINT);
	}

}
