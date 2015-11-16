package br.com.frequencia.crossTab;

public class Header {

	private String label;
	private Integer posicao;

	public Header(String label, Integer posicao) {
		super();
		this.label = label;
		this.posicao = posicao;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getPosicao() {
		return posicao;
	}

	public void setPosicao(Integer posicao) {
		this.posicao = posicao;
	}
	
	@Override
	public String toString() {
		return getLabel();
	}
}
