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

import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Table(name = "TURMA")
public class Turma implements AbstractEntity{

	@Id
	@Column(name = "ID_TURMA", unique = true, nullable = false)
	private Integer id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SALA", nullable = false)
	private Sala sala;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_EVENTO", nullable = false)
	private Evento evento;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_HORARIO", nullable = false)
	private Horario horario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO")
	private Usuario usuario;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_INSTRUTOR", nullable = false)
	private Instrutor instrutor;

	@NotNull @NotEmpty
	@Column(name = "NOM_TURMA", nullable = false)
	private String nomTurma;

	@NotNull @NotEmpty
	@Column(name = "NOM_DISCIPLINA", nullable = false)
	private String nomDisciplina;
	
	/*
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INICIO_INTERVALO")
	private Calendar inicioIntervalo;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FIM_INTERVALO")
	private Calendar fimIntervalo;
*/
	public Turma(Integer id) {
		this.id = id;
	}
	
	public Turma() {
	}

	public static String montarDDL() {
		StringBuilder turma = new StringBuilder();
		turma.append(" create table TURMA ( ");
		turma.append(" ID_TURMA             INTEGER not null, ");
		turma.append(" ID_SALA              INTEGER                 not null, ");
		turma.append(" ID_EVENTO            INTEGER                 not null, ");
		turma.append(" ID_HORARIO           INTEGER                 not null, ");
		turma.append(" ID_USUARIO           INTEGER                 null, ");
		turma.append(" ID_INSTRUTOR         INTEGER                 not null, ");
		turma.append(" NOM_TURMA            VARCHAR(255)         not null, ");
		turma.append(" NOM_DISCIPLINA       VARCHAR(255)         not null, ");
		turma.append("  INICIO_INTERVALO    TIMESTAMP           null, ");
		turma.append("  FIM_INTERVALO       TIMESTAMP           null, ");
		turma.append(" constraint PK_TURMA primary key (ID_TURMA) ");
		turma.append(" ); ");

		turma.append(" alter table TURMA ");
		turma.append(" add constraint FK_TURMA_FK_TURMA__EVENTO foreign key (ID_EVENTO) ");
		turma.append(" references EVENTO (ID_EVENTO) ");
		turma.append(" on delete restrict on update restrict; ");

		turma.append(" alter table TURMA ");
		turma.append(" add constraint FK_TURMA_REFERENCE_USUARIO foreign key (ID_USUARIO) ");
		turma.append(" references USUARIO (ID_USUARIO) ");
		turma.append(" on delete restrict on update restrict; ");

		turma.append(" alter table TURMA ");
		turma.append(" add constraint FK_TURMA_REFERENCE_SALA foreign key (ID_SALA) ");
		turma.append(" references SALA (ID_SALA) ");
		turma.append(" on delete restrict on update restrict; ");

		turma.append(" alter table TURMA ");
		turma.append(" add constraint FK_TURMA_REFERENCE_HORARIO foreign key (ID_HORARIO) ");
		turma.append(" references HORARIO (ID_HORARIO) ");
		turma.append(" on delete restrict on update restrict; ");

		return turma.toString();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Sala getSala() {
		return sala;
	}

	public void setSala(Sala sala) {
		this.sala = sala;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public Horario getHorario() {
		return horario;
	}

	public void setHorario(Horario horario) {
		this.horario = horario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getNomTurma() {
		return nomTurma;
	}

	public void setNomTurma(String nomTurma) {
		this.nomTurma = nomTurma;
	}

	public Instrutor getInstrutor() {
		return instrutor;
	}

	public void setInstrutor(Instrutor instrutor) {
		this.instrutor = instrutor;
	}

	
	public String getNomDisciplina() {
		return nomDisciplina;
	}

	
	public void setNomDisciplina(String nomDisciplina) {
		this.nomDisciplina = nomDisciplina;
	}

}
