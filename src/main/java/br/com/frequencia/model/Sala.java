package br.com.frequencia.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "SALA")
public class Sala implements AbstractEntity {

	@Id
	@Column(name = "ID_SALA", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NotNull
	@NotEmpty
	@Column(name = "NOM_SALA", nullable = false)
	private String nomSala;

	@NotNull
	@Column(name = "NUM_CAPACIDADE", nullable = false)
	private Integer numCapacidade;

	@Column(name = "flg_ativo", nullable = false)
	private Boolean flgAtivo;

	public Sala() {
	}

	public Sala(Integer id) {
		this.id = id;
	}

	public static String montarDDL() {
		StringBuilder sala = new StringBuilder();
		sala.append(" create table SALA ( ");
		sala.append(" ID_SALA              INTEGER not null, ");
		sala.append(" NOM_SALA             VARCHAR(255)         not null, ");
		sala.append(" NUM_CAPACIDADE       INTEGER                 not null, ");
		sala.append(" FLG_ATIVO            BOOL                 not null, ");
		sala.append(" constraint PK_SALA primary key (ID_SALA) ");
		sala.append(" ); ");
		return sala.toString();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNomSala() {
		return nomSala;
	}

	public void setNomSala(String nomSala) {
		this.nomSala = nomSala;
	}

	public Integer getNumCapacidade() {
		return numCapacidade;
	}

	public void setNumCapacidade(Integer numCapacidade) {
		this.numCapacidade = numCapacidade;
	}

	public Boolean getFlgAtivo() {
		return flgAtivo;
	}

	public void setFlgAtivo(Boolean flgAtivo) {
		this.flgAtivo = flgAtivo;
	}
}
