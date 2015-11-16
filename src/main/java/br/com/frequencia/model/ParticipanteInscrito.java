package br.com.frequencia.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "PARTICIPANTE_INSCRITO")
public class ParticipanteInscrito implements AbstractEntity{

	@Id
	@Column(name = "ID_PARTICIPANTE_INSCRITO", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PARTICIPANTE", nullable = false)
	private Participante participante;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TURMA")
	private Turma turma;

	public static String montarDDL() {
		StringBuilder participanteInscrito = new StringBuilder();
		participanteInscrito.append(" create table PARTICIPANTE_INSCRITO ( ");
		participanteInscrito.append(" ID_PARTICIPANTE_INSCRITO INTEGER not null, ");
		participanteInscrito.append(" ID_PARTICIPANTE      INTEGER                 not null, ");
		participanteInscrito.append(" ID_TURMA     INTEGER                 null, ");
		participanteInscrito.append(" constraint PK_PARTICIPANTE_INSCRITO primary key (ID_PARTICIPANTE_INSCRITO) ");
		participanteInscrito.append(" ); ");

		participanteInscrito.append(" alter table PARTICIPANTE_INSCRITO ");
		participanteInscrito.append(" add constraint FK_PARTICIP_FK_PARTIC_PARTICIP foreign key (ID_PARTICIPANTE) ");
		participanteInscrito.append(" references PARTICIPANTE (ID_PARTICIPANTE) ");
		participanteInscrito.append(" on delete restrict on update restrict; ");

		participanteInscrito.append(" alter table PARTICIPANTE_INSCRITO ");
		participanteInscrito.append(" add constraint FK_PARTICIP_REFERENCE_TURMA foreign key (ID_TURMA_OFICINA) ");
		participanteInscrito.append(" references TURMA (ID_TURMA) ");
		participanteInscrito.append(" on delete restrict on update restrict; ");

		return participanteInscrito.toString();
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
}
