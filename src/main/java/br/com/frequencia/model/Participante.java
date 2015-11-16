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
@Table(name = "PARTICIPANTE")
public class Participante implements AbstractEntity {

	@Id
	@Column(name = "ID_PARTICIPANTE", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ORGAO")
	private Orgao orgao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO")
	private Usuario usuario;

	@Column(name = "NUM_INSCRICAO", nullable = false)
	private String numInscricao;

	@NotNull
	@NotEmpty
	@Column(name = "NOM_PARTICIPANTE")
	private String nomParticipante;

	@Column(name = "NOM_CRACHA")
	private String nomCracha;

	@NotNull
	@Column(name = "FLG_RECEM_CADASTRADO", nullable = false)
	private Boolean flgRecemCadastrado;

	@NotNull
	@Column(name = "FLG_ALTERADO", nullable = false)
	private Boolean flgAlterado;

	public Participante(Integer id) {
		this.id = id;
	}

	public Participante() {
	}

	public static String montarDDL() {
		StringBuilder participante = new StringBuilder();
		participante.append(" create table PARTICIPANTE ( ");
		participante.append(" ID_PARTICIPANTE      INTEGER not null, ");
		participante.append(" ID_ORGAO             INTEGER                 null, ");
		participante.append(" ID_USUARIO           INTEGER                 null, ");
		participante.append(" NUM_INSCRICAO        VARCHAR(255)         not null, ");
		participante.append(" NOM_PARTICIPANTE     VARCHAR(255)         not null, ");
		participante.append(" NOM_CRACHA           VARCHAR(50)          null, ");
		participante.append(" FLG_RECEM_CADASTRADO BOOL                 not null, ");
		participante.append(" FLG_ALTERADO 		   BOOL                 not null, ");
		participante.append(" constraint PK_PARTICIPANTE primary key (ID_PARTICIPANTE) ");
		participante.append(" ); ");

		participante.append(" alter table PARTICIPANTE ");
		participante.append(" add constraint FK_PARTICIP_FK_PARTIC_ORGAO foreign key (ID_ORGAO) ");
		participante.append(" references ORGAO (ID_ORGAO) ");
		participante.append(" on delete restrict on update restrict; ");

		participante.append(" alter table PARTICIPANTE ");
		participante.append(" add constraint FK_PARTICIP_FK_PARTIC_USUARIO foreign key (ID_USUARIO) ");
		participante.append(" references USUARIO (ID_USUARIO) ");
		participante.append(" on delete restrict on update restrict; ");

		return participante.toString();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Orgao getOrgao() {
		return orgao;
	}

	public void setOrgao(Orgao orgao) {
		this.orgao = orgao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getNumInscricao() {
		return numInscricao;
	}

	public void setNumInscricao(String numInscricao) {
		this.numInscricao = numInscricao;
	}

	public String getNomParticipante() {
		return nomParticipante;
	}

	public void setNomParticipante(String nomParticipante) {
		this.nomParticipante = nomParticipante;
	}

	public String getNomCracha() {
		return nomCracha;
	}

	public void setNomCracha(String nomCracha) {
		this.nomCracha = nomCracha;
	}

	public Boolean getFlgRecemCadastrado() {
		return flgRecemCadastrado;
	}

	public void setFlgRecemCadastrado(Boolean flgRecemCadastrado) {
		this.flgRecemCadastrado = flgRecemCadastrado;
	}

	public Boolean getFlgAlterado() {
		return flgAlterado;
	}

	public void setFlgAlterado(Boolean flgAlterado) {
		this.flgAlterado = flgAlterado;
	}
}
