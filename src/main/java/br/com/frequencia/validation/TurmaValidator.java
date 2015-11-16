package br.com.frequencia.validation;

import static javax.validation.Validation.buildDefaultValidatorFactory;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.ValidatorFactory;

import br.com.frequencia.model.Turma;

/**
 * Implementa componente para validar os dados da entidade <code>Turma</code>.
 * 
 * <p>A validação ocorre através do Bean Validations, mecanismo de validação padrão do Java baseado em anotações.</p>
 * 
 */
public class TurmaValidator implements Validator<Turma> {
	
	private static ValidatorFactory factory;
	
	static {
		factory = buildDefaultValidatorFactory();
	}

	public String validate(Turma turma) {
		StringBuilder sb = new StringBuilder();
		if (turma != null) {
			javax.validation.Validator validator = factory.getValidator();
			Set<ConstraintViolation<Turma>> constraintViolations = validator.validate(turma);
			
			if (!constraintViolations.isEmpty()) {
				sb.append("Validação da entidade Turma\n");
				for (ConstraintViolation<Turma> constraint: constraintViolations) {
					String propriedade = retornarNomePropriedade(constraint.getPropertyPath());
					sb.append(String.format("%n%s: %s", propriedade, constraint.getMessage()));
				}
			}
		}
		return sb.toString();
	}

	/**
	 *  Convertendo o nome da variável para uma string amigável para o Usuaário
	 * @param propertyPath --> Contendo a propriedade da classe Turma
	 * @return String amigável para o Usuário
	 */
	private String retornarNomePropriedade(Path propertyPath) {
		if (propertyPath.toString().equals("sala")){
			return "Sala";
		}else if (propertyPath.toString().equals("evento")){
			return "Evento";
		}else if (propertyPath.toString().equals("horario")){
			return "Horário";
		}else if (propertyPath.toString().equals("idInstrutor")){
			return "Instrutor";
		}else if (propertyPath.toString().equals("nomTurma")){
			return "Nome da turma";
		}else if (propertyPath.toString().equals("nomDisciplina")){
			return "Disciplina";
		}
		
		return null;
	}

}
