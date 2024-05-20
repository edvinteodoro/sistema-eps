package gt.edu.cunoc.sistemaeps.specification;

import gt.edu.cunoc.sistemaeps.entity.Acta;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author edvin
 */
public class ActaSpecification {
    
    
    private static final String NOMBRE_ESTUDIANTE = "nombreCompleto";
    private static final String REGISTRO_ESTUDIANTE = "registroAcademico";
    
    public static Specification<Acta> filterBy(ActaFilter actaFilter) {
        return Specification
                .where(hasNombreEstudiante(actaFilter.getNombreEstudiante()))
                .and(hasRegistroEstudiante(actaFilter.getRegistroEstudiante()));
    }
    
    private static Specification<Acta> hasNombreEstudiante(String nombreEstudiante) {
        return ((root, query, cb) -> {
            if (nombreEstudiante == null || nombreEstudiante.isEmpty()) {
                return cb.conjunction();
            }
            Join<Acta, Proyecto> proyecto = root.join("idProyectoFk");
            Join<Proyecto, Usuario> usuario = proyecto.join("idUsuarioFk");
            return cb.like(usuario.get(NOMBRE_ESTUDIANTE), "%"+nombreEstudiante+"%");
        });
    }
    
    private static Specification<Acta> hasRegistroEstudiante(String registroEstudiante) {
        return ((root, query, cb) -> {
            if (registroEstudiante == null || registroEstudiante.isEmpty()) {
                return cb.conjunction();
            }
            Join<Acta, Proyecto> proyecto = root.join("idProyectoFk");
            Join<Proyecto, Usuario> usuario = proyecto.join("idUsuarioFk");
            return cb.equal(usuario.get(REGISTRO_ESTUDIANTE), registroEstudiante);
        });
    }
    
}
