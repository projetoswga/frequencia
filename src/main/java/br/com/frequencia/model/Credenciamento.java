package br.com.frequencia.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "CREDENCIAMENTO")
public class Credenciamento implements AbstractEntity {

	@Id
	@Column(name = "ID_CREDENCIAMENTO", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PARTICIPANTE", nullable = false)
	private Participante participante;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_EVENTO", nullable = false)
	private Evento evento;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HORARIO_ENTRADA", nullable = false)
	private Calendar horarioEntrada;

	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_CADASTRO")
	private Date DataCadastro;

	public Credenciamento() {
	}

	public Credenciamento(Integer id) {
		this.id = id;
	}

	public static String montarDDL() {
		StringBuilder credenciamento = new StringBuilder();
		credenciamento.append(" create table CREDENCIAMENTO ( ");
		credenciamento.append(" ID_CREDENCIAMENTO         INTEGER 	not null, ");
		credenciamento.append(" ID_PARTICIPANTE           INTEGER 	not null, ");
		credenciamento.append(" ID_EVENTO           	  INTEGER 	not null, ");
		credenciamento.append(" HORARIO_ENTRADA           TIMESTAMP not null, ");
		credenciamento.append(" DATA_CADASTRO             DATE 		not null, ");
		credenciamento.append(" constraint PK_CREDENCIAMENTO primary key (ID_CREDENCIAMENTO) ");
		credenciamento.append(" ); ");

		credenciamento.append(" alter table CREDENCIAMENTO ");
		credenciamento.append(" add constraint FK_REFERENCE_18 foreign key (ID_PARTICIPANTE) ");
		credenciamento.append(" references PARTICIPANTE (ID_PARTICIPANTE) ");
		credenciamento.append(" on delete restrict on update restrict; ");

		credenciamento.append(" alter table CREDENCIAMENTO ");
		credenciamento.append(" add constraint FK_REFERENCE_19 foreign key (ID_EVENTO) ");
		credenciamento.append(" references EVENTO (ID_EVENTO) ");
		credenciamento.append(" on delete restrict on update restrict; ");
		return credenciamento.toString();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Participante getParticipante() {
		return participante;
	}

	public void setParticipante(Participante participante) {
		this.participante = participante;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public Calendar getHorarioEntrada() {
		return horarioEntrada;
	}

	public void setHorarioEntrada(Calendar horarioEntrada) {
		this.horarioEntrada = horarioEntrada;
	}

	public Date getDataCadastro() {
		return DataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		DataCadastro = dataCadastro;
	}
}
