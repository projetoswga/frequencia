package br.com.frequencia.model;

import java.util.Calendar;

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
@Table(name = "FREQUENCIA")
public class Frequencia implements AbstractEntity {

	@Id
	@Column(name = "ID_FREQUENCIA", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PARTICIPANTE", nullable = false)
	private Participante participante;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TURMA", nullable = false)
	private Turma turma;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO")
	private Usuario usuario;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HORARIO_ENTRADA")
	private Calendar horarioEntrada;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HORARIO_SAIDA")
	private Calendar horarioSaida;

	public static String montarDDL() {
		StringBuilder frequencia = new StringBuilder();
		frequencia.append(" create table FREQUENCIA ( ");
		frequencia.append("  ID_FREQUENCIA        INTEGER not null, ");
		frequencia.append("  ID_PARTICIPANTE      INTEGER                 not null, ");
		frequencia.append("  ID_TURMA             INTEGER                 not null, ");
		frequencia.append("  ID_USUARIO           INTEGER                 null, ");
		frequencia.append("  HORARIO_ENTRADA              TIMESTAMP           null, ");
		frequencia.append("  HORARIO_SAIDA              TIMESTAMP            null, ");
		frequencia.append(" constraint PK_FREQUENCIA primary key (ID_FREQUENCIA) ");
		frequencia.append(" ); ");

		frequencia.append(" alter table FREQUENCIA ");
		frequencia.append(" add constraint FK_FREQUENC_FK_FREQUE_TURMA foreign key (ID_TURMA) ");
		frequencia.append(" references TURMA (ID_TURMA) ");
		frequencia.append(" on delete restrict on update restrict; ");

		frequencia.append(" alter table FREQUENCIA ");
		frequencia.append(" add constraint FK_FREQUENC_REFERENCE_USUARIO foreign key (ID_USUARIO) ");
		frequencia.append(" references USUARIO (ID_USUARIO) ");
		frequencia.append(" on delete restrict on update restrict; ");

		frequencia.append(" alter table FREQUENCIA ");
		frequencia.append(" add constraint FK_FREQUENC_REFERENCE_PARTICIP foreign key (ID_PARTICIPANTE) ");
		frequencia.append("  references PARTICIPANTE (ID_PARTICIPANTE) ");
		frequencia.append(" on delete restrict on update restrict; ");

		return frequencia.toString();
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

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Calendar getHorarioEntrada() {
		return horarioEntrada;
	}

	public void setHorarioEntrada(Calendar horarioEntrada) {
		this.horarioEntrada = horarioEntrada;
	}

	public Calendar getHorarioSaida() {
		return horarioSaida;
	}

	public void setHorarioSaida(Calendar horarioSaida) {
		this.horarioSaida = horarioSaida;
	}
}
