package br.com.frequencia.model;

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


@Entity
@Table(name = "PARTICIPANTE_DESTAQUE")
public class ParticipanteDestaque implements AbstractEntity{

	@Id
	@Column(name = "ID_PARTICIPANTE_DESTAQUE", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TURMA")
	private Turma turma;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PARTICIPANTE")
	private Participante participante;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_CADASTRO")
	private Date dtCadastro;

	public static String montarDDL() {
		StringBuilder participanteDestaque = new StringBuilder();
		participanteDestaque.append(" create table PARTICIPANTE_DESTAQUE ( ");
		participanteDestaque.append(" ID_PARTICIPANTE_DESTAQUE INTEGER                 not null, ");
		participanteDestaque.append(" ID_TURMA             INTEGER                 null, ");
		participanteDestaque.append(" ID_PARTICIPANTE      INTEGER                 null, ");
		participanteDestaque.append(" DT_CADASTRO          DATE                 null, ");
		participanteDestaque.append(" constraint PK_PARTICIPANTE_DESTAQUE primary key (ID_PARTICIPANTE_DESTAQUE) ");
		participanteDestaque.append(" ); ");

		participanteDestaque.append(" alter table PARTICIPANTE_DESTAQUE ");
		participanteDestaque.append(" add constraint FK_PARTICIP_FK_PARTIC_PARTICIP foreign key (ID_PARTICIPANTE) ");
		participanteDestaque.append(" references PARTICIPANTE (ID_PARTICIPANTE) ");
		participanteDestaque.append(" on delete restrict on update restrict; ");

		participanteDestaque.append(" alter table PARTICIPANTE_DESTAQUE ");
		participanteDestaque.append(" add constraint FK_PARTICIP_REFERENCE_TURMA foreign key (ID_TURMA) ");
		participanteDestaque.append(" references TURMA (ID_TURMA) ");
		participanteDestaque.append(" on delete restrict on update restrict; ");

		return participanteDestaque.toString();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public Participante getParticipante() {
		return participante;
	}

	public void setParticipante(Participante participante) {
		this.participante = participante;
	}

	public Date getDtCadastro() {
		return dtCadastro;
	}

	public void setDtCadastro(Date dtCadastro) {
		this.dtCadastro = dtCadastro;
	}
}
