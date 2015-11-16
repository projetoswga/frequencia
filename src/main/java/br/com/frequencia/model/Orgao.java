package br.com.frequencia.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "ORGAO")
public class Orgao implements AbstractEntity{

	@Id
	@Column(name = "ID_ORGAO", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NotNull
	@Column(name = "SIGLA_ORGAO", nullable = false)
	private String siglaOrgao;

	@Column(name = "NOM_ORGAO")
	private String nomOrgao;

	@Column(name = "ID_ORIGEM")
	private Integer idOrigem;

	@Column(name = "flg_ativo", nullable = false, columnDefinition = "boolean default true")
	private Boolean flgAtivo;

	public Orgao(Integer id) {
		this.id = id;
	}
	
	public Orgao() {
	}

	public static String montarDDL() {
		StringBuilder orgao = new StringBuilder();
		orgao.append(" create table ORGAO ( ");
		orgao.append(" ID_ORGAO             INTEGER not null, ");
		orgao.append(" SIGLA_ORGAO          VARCHAR(50)          not null, ");
		orgao.append(" NOM_ORGAO            VARCHAR(255)         null, ");
		orgao.append(" ID_ORIGEM            INTEGER                 null, ");
		orgao.append(" FLG_ATIVO            BOOL                 not null default TRUE, ");
		orgao.append(" constraint PK_ORGAO primary key (ID_ORGAO) ");
		orgao.append(" ); ");
		return orgao.toString();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSiglaOrgao() {
		return siglaOrgao;
	}

	public void setSiglaOrgao(String siglaOrgao) {
		this.siglaOrgao = siglaOrgao;
	}

	public String getNomOrgao() {
		return nomOrgao;
	}

	public void setNomOrgao(String nomOrgao) {
		this.nomOrgao = nomOrgao;
	}

	public Integer getIdOrigem() {
		return idOrigem;
	}

	public void setIdOrigem(Integer idOrigem) {
		this.idOrigem = idOrigem;
	}

	public Boolean getFlgAtivo() {
		return flgAtivo;
	}

	public void setFlgAtivo(Boolean flgAtivo) {
		this.flgAtivo = flgAtivo;
	}
}
