package br.com.frequencia.model;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "HORARIO")
public class Horario implements AbstractEntity {

	@Id
	@Column(name = "ID_HORARIO", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NotNull
	@NotEmpty
	@Column(name = "DESC_HORARIO", nullable = false)
	private String descHorario;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HR_INICIAL", nullable = false)
	private Calendar hrInicial;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HR_FINAL", nullable = false)
	private Calendar hrFinal;

	public Horario() {
	}

	public Horario(Integer id) {
		this.id = id;
	}

	public static String montarDDL() {
		StringBuilder horario = new StringBuilder();
		horario.append(" create table HORARIO ( ");
		horario.append(" ID_HORARIO           INTEGER not null, ");
		horario.append(" DESC_HORARIO         VARCHAR(50)          not null, ");
		horario.append(" HR_INICIAL           TIMESTAMP            not null, ");
		horario.append(" HR_FINAL             TIMESTAMP            not null, ");
		horario.append(" constraint PK_HORARIO primary key (ID_HORARIO) ");
		horario.append(" ); ");
		return horario.toString();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescHorario() {
		return descHorario;
	}

	public void setDescHorario(String descHorario) {
		this.descHorario = descHorario;
	}

	public Calendar getHrInicial() {
		return hrInicial;
	}

	public void setHrInicial(Calendar hrInicial) {
		this.hrInicial = hrInicial;
	}

	public Calendar getHrFinal() {
		return hrFinal;
	}

	public void setHrFinal(Calendar hrFinal) {
		this.hrFinal = hrFinal;
	}
}
