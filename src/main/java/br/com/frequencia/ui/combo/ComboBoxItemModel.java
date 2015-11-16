package br.com.frequencia.ui.combo;

public class ComboBoxItemModel {

	private Integer id;
	private String descricao;

	public ComboBoxItemModel(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return descricao;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
