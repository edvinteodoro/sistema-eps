/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.specification;

import gt.edu.cunoc.sistemaeps.entity.Usuario;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author edvin
 */
public class UsuarioSpecification {

    private static final String NOMBRE = "nombreCompleto";
    private static final String REGISTRO_ACADEMICO = "registroAcademico";
    private static final String COLEGIADO = "numeroColegiado";

    public static Specification<Usuario> filterBy(String nombre,
            String registroAcademico, String numeroColegiado) {
        return Specification
                .where(hasNombre(nombre))
                .and(hasRegistroAcademico(registroAcademico))
                .and(hasNumeroColegiado(numeroColegiado));
    }

    private static Specification<Usuario> hasNombre(String nombre) {
        return ((root, query, cb) -> nombre == null || nombre.isEmpty() ? cb.conjunction() : cb.like(root.get(NOMBRE), "%" + nombre + "%"));
    }

    private static Specification<Usuario> hasRegistroAcademico(String registroAcademico) {
        return ((root, query, cb) -> registroAcademico == null || registroAcademico.isEmpty() ? cb.conjunction() : cb.equal(root.get(REGISTRO_ACADEMICO), registroAcademico));
    }

    private static Specification<Usuario> hasNumeroColegiado(String numeroColegiado) {
        return ((root, query, cb) -> numeroColegiado == null || numeroColegiado.isEmpty() ? cb.conjunction() : cb.equal(root.get(COLEGIADO), numeroColegiado));
    }
}
