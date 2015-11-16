package br.com.frequencia.relatorio.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import br.com.frequencia.crossTab.CrossTab;
import br.com.frequencia.crossTab.Header;
import br.com.frequencia.model.Evento;
import br.com.frequencia.relatorio.dto.AlunoDTO;
import br.com.frequencia.relatorio.dto.CrachaDTO;
import br.com.frequencia.relatorio.dto.CredenciamentoDTO;
import br.com.frequencia.relatorio.dto.EtiquetaDTO;
import br.com.frequencia.relatorio.dto.MapaFrequencia;
import br.com.frequencia.relatorio.util.Constantes;
import br.com.frequencia.relatorio.util.RelatorioUtil;
import br.com.frequencia.util.DateUtil;

public class RelatorioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public void relatorioCredenciamento(List<CredenciamentoDTO> lista, Evento evento, String nomeSaida, String formato) throws JRException,
			IOException {

		InputStream streamJasper = RelatorioBean.class.getResourceAsStream("/relatorios/credenciamento.jasper");
		InputStream streamJasperSub = RelatorioBean.class.getResourceAsStream("/relatorios/subCredenciamento.jasper");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("image", RelatorioBean.class.getResourceAsStream("/imagens/logo.png"));
		map.put("SUBREPORT", streamJasperSub);
		map.put("tituloDescricaoSemana", evento.getNomEvento());
		map.put("tituloDataLocal", evento.getDescLocalizacaoEvento() + " - " + DateUtil.getDataHora(evento.getDataInicio(), "dd/MM/yyyy")
				+ " a " + DateUtil.getDataHora(evento.getDataFim(), "dd/MM/yyyy"));
		map.put("lista", lista);

		RelatorioUtil.gerarRelatorio(map, streamJasper, nomeSaida, formato);
	}

	public void etiqueta() {
		try {
			InputStream streamJasper = RelatorioBean.class.getResourceAsStream("/relatorios/etiqueta.jasper");
			RelatorioUtil.gerarRelatorio(populaEtiqueta(), streamJasper, "Etiqueta", Constantes.EXCEL, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void etiqueta(List<EtiquetaDTO> listaEtiqueDTO, String nomeSaida, String formato) throws JRException, IOException {
		InputStream streamJasper = RelatorioBean.class.getResourceAsStream("/relatorios/etiqueta.jasper");
		RelatorioUtil.gerarRelatorio(listaEtiqueDTO, streamJasper, nomeSaida, formato, true);
	}

	public void crachas(List<CrachaDTO> listaCrachaDTO, String nomeSaida, String formato) throws JRException, IOException {
		InputStream streamJasper = RelatorioBean.class.getResourceAsStream("/relatorios/cracha.jasper");
		InputStream streamJasperSub = RelatorioBean.class.getResourceAsStream("/relatorios/cracha_subreport1.jasper");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("SUBREPORT", streamJasperSub);

		RelatorioUtil.gerarRelatorio(listaCrachaDTO, map, streamJasper, nomeSaida, formato);
	}

	public void relatorioMapa(List<AlunoDTO> listaCross, Evento evento, String nomeRelatorio) throws JRException, IOException {
		calcularTotalFaltas(listaCross);
		InputStream streamJasper = RelatorioBean.class.getResourceAsStream("/relatorios/mapaGeral.jasper");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("image", RelatorioBean.class.getResourceAsStream("/imagens/logo.png"));
		map.put("tituloDescricaoSemana", evento.getNomEvento());
		map.put("tituloDataLocal", evento.getDescLocalizacaoEvento() + " - " + DateUtil.getDataHora(evento.getDataInicio(), "dd/MM/yyyy")
				+ " a " + DateUtil.getDataHora(evento.getDataFim(), "dd/MM/yyyy"));

		RelatorioUtil.gerarRelatorio(montarCrossTab(listaCross), map, streamJasper, nomeRelatorio, Constantes.EXCEL);
	}

	private List<EtiquetaDTO> populaEtiqueta() {
		List<EtiquetaDTO> etiquetas = new LinkedList<EtiquetaDTO>();

		Integer cont = 1;
		while (cont != 1000) {
			EtiquetaDTO dto = new EtiquetaDTO();
			dto.setNomeCompleto("Dom Pedro" + cont);
			dto.setInscricao("1234567" + cont);
			cont++;
			etiquetas.add(dto);
		}
		return etiquetas;
	}

	public List<CrossTab> montarCrossTab(List<AlunoDTO> lista) {
		List<CrossTab> listaRetorno = new ArrayList<CrossTab>();
		Integer cont = 1;
		for (AlunoDTO list : lista) {
			Integer posicao = 1;
			// Atributos do aluno
			listaRetorno.add(new CrossTab(new Header("Nome", posicao++), cont, list.getNomeCompleto()));
			listaRetorno.add(new CrossTab(new Header("Inscrição", posicao++), cont, list.getInscricao()));
			listaRetorno.add(new CrossTab(new Header("Órgão", posicao++), cont, list.getOrgao()));
			for (MapaFrequencia frequencia : list.getMapaFrequencia()) {
				// atributos da frequencia
				listaRetorno.add(new CrossTab(new Header(frequencia.getData(), posicao++), cont, frequencia.getPresenca()));
			}
			listaRetorno.add(new CrossTab(new Header("Total Faltas", posicao++), cont, list.getTotalFaltas()));
			cont++;
		}
		return listaRetorno;
	}

	private void calcularTotalFaltas(List<AlunoDTO> listaFrequencia) {
		for (AlunoDTO alunoDTO : listaFrequencia) {
			Integer totalFaltas = 0;
			for (MapaFrequencia frequencia : alunoDTO.getMapaFrequencia()) {
				if (frequencia.getPresenca() == null || frequencia.getPresenca().isEmpty()) {
					continue;
				}
				if (frequencia.getPresenca().equalsIgnoreCase("F")) {
					totalFaltas++;
				}
				alunoDTO.setTotalFaltas(totalFaltas.toString());
			}
		}
	}
}
