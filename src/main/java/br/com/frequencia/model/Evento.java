package br.com.frequencia.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Table(name = "EVENTO")
public class Evento implements AbstractEntity{

	@Id
	@Column(name = "ID_EVENTO", unique = true, nullable = false)
	private Integer id;

	@NotNull @NotEmpty
	@Column(name = "NOM_EVENTO", nullable = false)
	private String nomEvento;
	
	@NotNull @NotEmpty
	@Column(name = "DES_LOCALIZACAO_EVENTO", nullable = false)
	private String descLocalizacaoEvento;

	@Column(name = "flg_ativo", nullable = false)
	private Boolean flgAtivo;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_REALIZACAO_INICIO", nullable = false)
	private Date dataInicio;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_REALIZACAO_FIM", nullable = false)
	private Date dataFim;

	public Evento() {
	}
	
	public Evento(Integer id) {
		this.id = id;
	}

	public static String montarDDL() {
		StringBuilder evento = new StringBuilder();
		evento.append(" create table EVENTO ( ");
		evento.append(" ID_EVENTO            	INTEGER not null, ");
		evento.append(" NOM_EVENTO           	VARCHAR(255)         not null, ");
		evento.append(" DES_LOCALIZACAO_EVENTO 	VARCHAR(255)         not null, ");
		evento.append(" FLG_ATIVO             	BOOL                 not null, ");
		evento.append(" DT_REALIZACAO_INICIO    DATE              not null, ");
		evento.append(" DT_REALIZACAO_FIM		DATE              not null, ");
		evento.append(" constraint PK_EVENTO primary key (ID_EVENTO) ");
		evento.append(" ); ");
		return evento.toString();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNomEvento() {
		return nomEvento;
	}

	public void setNomEvento(String nomEvento) {
		this.nomEvento = nomEvento;
	}

	public Boolean getFlgAtivo() {
		return flgAtivo;
	}

	public void setFlgAtivo(Boolean flgAtivo) {
		this.flgAtivo = flgAtivo;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getDescLocalizacaoEvento() {
		return descLocalizacaoEvento;
	}

	public void setDescLocalizacaoEvento(String descLocalizacaoEvento) {
		this.descLocalizacaoEvento = descLocalizacaoEvento;
	}
}
