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
@Table(name = "USUARIO")
public class Usuario implements AbstractEntity{

	@Id
	@Column(name = "ID_USUARIO", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NotNull @NotEmpty
	@Column(name = "NOM_USUARIO", nullable = false)
	private String nomUsuario;

	@Column(name = "EMAIL", nullable = false)
	private String email;

	@Column(name = "flg_ativo", nullable = false, columnDefinition = "boolean default true")
	private Boolean flgAtivo;

	public Usuario() {
	}
	
	public Usuario(Integer id) {
		this.id = id;
	}

	public static String montarDDL() {
		StringBuilder evento = new StringBuilder();
		evento.append(" create table USUARIO ( ");
		evento.append(" ID_USUARIO           INTEGER                 not null, ");
		evento.append(" NOM_USUARIO          VARCHAR(255)         not null, ");
		evento.append(" EMAIL                VARCHAR(50)          null, ");
		evento.append(" FLG_ATIVO            BOOL                 not null default TRUE, ");
		evento.append(" constraint PK_USUARIO primary key (ID_USUARIO) ");
		evento.append(" ); ");
		return evento.toString();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNomUsuario() {
		return nomUsuario;
	}

	public void setNomUsuario(String nomUsuario) {
		this.nomUsuario = nomUsuario;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getFlgAtivo() {
		return flgAtivo;
	}

	public void setFlgAtivo(Boolean flgAtivo) {
		this.flgAtivo = flgAtivo;
	}
}
