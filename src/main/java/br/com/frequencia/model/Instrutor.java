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
@Table(name = "INSTRUTOR")
public class Instrutor implements AbstractEntity{

	@Id
	@Column(name = "ID_INSTRUTOR", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NotNull @NotEmpty
	@Column(name = "NOM_INSTRUTOR", nullable = false)
	private String nomInstrutor;

	@Column(name = "flg_ativo", nullable = false)
	private Boolean flgAtivo;

	public Instrutor() {
	}
	
	public Instrutor(Integer id) {
		this.id = id;
	}

	public static String montarDDL() {
		StringBuilder instrutor = new StringBuilder();
		instrutor.append(" create table INSTRUTOR ( ");
		instrutor.append(" ID_INSTRUTOR            INTEGER not null, ");
		instrutor.append(" NOM_INSTRUTOR           VARCHAR(255)         not null, ");
		instrutor.append(" FLG_ATIVO            BOOL                 not null, ");
		instrutor.append(" constraint PK_INSTRUTOR primary key (ID_INSTRUTOR) ");
		instrutor.append(" ); ");
		return instrutor.toString();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNomInstrutor() {
		return nomInstrutor;
	}

	public void setNomInstrutor(String nomInstrutor) {
		this.nomInstrutor = nomInstrutor;
	}

	public Boolean getFlgAtivo() {
		return flgAtivo;
	}

	public void setFlgAtivo(Boolean flgAtivo) {
		this.flgAtivo = flgAtivo;
	}
}
