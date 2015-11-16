package br.com.frequencia.validation;

import static javax.validation.Validation.buildDefaultValidatorFactory;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.ValidatorFactory;

import br.com.frequencia.model.Participante;
import br.com.frequencia.model.ParticipanteInscrito;
import br.com.frequencia.model.Turma;

/**
 * Implementa componente para validar os dados da entidade <code>ParticipanteInscrito</code>.
 * 
 * <p>
 * A validação ocorre através do Bean Validations, mecanismo de validação padrão do Java baseado em anotações.
 * </p>
 * 
 */
public class ParticipanteInscritoValidator implements Validator<ParticipanteInscrito> {

	private static ValidatorFactory factory;

	static {
		factory = buildDefaultValidatorFactory();
	}

	public String validate(ParticipanteInscrito participanteInscrito) {
		StringBuilder sb = new StringBuilder();
		if (participanteInscrito != null) {
			javax.validation.Validator validator = factory.getValidator();
			Set<ConstraintViolation<ParticipanteInscrito>> constraintViolations = validator.validate(participanteInscrito);
			Set<ConstraintViolation<Participante>> constraintViolationsParticipante = validator.validate(participanteInscrito
					.getParticipante());
			Set<ConstraintViolation<Turma>> constraintViolationsTurma = validator.validate(participanteInscrito.getTurma());

			if (!constraintViolations.isEmpty()) {
				sb.append("Validação da entidade Participante Inscrito\n");
				for (ConstraintViolation<ParticipanteInscrito> constraint : constraintViolations) {
					sb.append(String.format("%n%s: %s", constraint.getPropertyPath(), constraint.getMessage()));
				}
				sb.append("\n\n");
			}
			if (!constraintViolationsParticipante.isEmpty()) {
				sb.append("Validação da entidade Participante\n");
				for (ConstraintViolation<Participante> constraint : constraintViolationsParticipante) {
					String propriedade = retornarNomePropriedadeParticipante(constraint.getPropertyPath());
					sb.append(String.format("%n%s: %s", propriedade, constraint.getMessage()));
				}
				sb.append("\n\n");
			}
			if (!constraintViolationsTurma.isEmpty()) {
				sb.append("Validação da entidade Turma\n");
				for (ConstraintViolation<Turma> constraint : constraintViolationsTurma) {
					String propriedade = retornarNomePropriedadeTurma(constraint.getPropertyPath());
					sb.append(String.format("%n%s: %s", propriedade, constraint.getMessage()));
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Convertendo o nome da variável para uma string amigável para o Usuário
	 * 
	 * @param propertyPath
	 *            --> Contendo a propriedade da classe <code>ParticipanteInscrito.getParticipante()</code>
	 * @return String amigável para o Usuário
	 * 
	 */
	private String retornarNomePropriedadeParticipante(Path propertyPath) {
		if (propertyPath.toString().equals("orgao")) {
			return "Órgão";
		} else if (propertyPath.toString().equals("usuario")) {
			return "Usuário";
		} else if (propertyPath.toString().equals("numInscricao")) {
			return "Inscrição";
		} else if (propertyPath.toString().equals("nomParticipante")) {
			return "Nome";
		} else if (propertyPath.toString().equals("nomCracha")) {
			return "Nome Crachá";
		} else if (propertyPath.toString().equals("flgRecemCadastrado")) {
			return "Recém Cadastrado";
		}
		return null;
	}

	/**
	 * Convertendo o nome da variável para uma string amigável para o Usuário
	 * 
	 * @param propertyPath
	 *            --> Contendo a propriedade da classe <code>ParticipanteInscrito.getTurma()</code>
	 * @return String amigável para o Usuário
	 * 
	 */
	private String retornarNomePropriedadeTurma(Path propertyPath) {
		if (propertyPath.toString().equals("sala")) {
			return "Sala";
		} else if (propertyPath.toString().equals("evento")) {
			return "Evento";
		} else if (propertyPath.toString().equals("horario")) {
			return "Horário";
		} else if (propertyPath.toString().equals("idInstrutor")) {
			return "Instrutor";
		} else if (propertyPath.toString().equals("nomTurma")) {
			return "Turma";
		} else if (propertyPath.toString().equals("usuario")) {
			return "Usuário";
		}
		return null;
	}

}
