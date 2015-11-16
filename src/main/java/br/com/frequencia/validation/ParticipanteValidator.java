package br.com.frequencia.validation;

import static javax.validation.Validation.buildDefaultValidatorFactory;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.ValidatorFactory;

import br.com.frequencia.model.Participante;

/**
 * Implementa componente para validar os dados da entidade <code>Participante</code>.
 * 
 * <p>
 * A validação ocorre através do Bean Validations, mecanismo de validação padrão do Java baseado em anotações.
 * </p>
 * 
 */
public class ParticipanteValidator implements Validator<Participante> {

	private static ValidatorFactory factory;

	static {
		factory = buildDefaultValidatorFactory();
	}

	public String validate(Participante participante) {
		StringBuilder sb = new StringBuilder();
		if (participante != null) {
			javax.validation.Validator validator = factory.getValidator();
			Set<ConstraintViolation<Participante>> constraintViolations = validator.validate(participante);

			if (!constraintViolations.isEmpty()) {
				sb.append("Validação da entidade Participante\n");
				for (ConstraintViolation<Participante> constraint : constraintViolations) {
					String propriedade = retornarNomePropriedadeParticipante(constraint.getPropertyPath());
					sb.append(String.format("%n%s: %s", propriedade, constraint.getMessage()));
				}
				sb.append("\n\n");
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

}
