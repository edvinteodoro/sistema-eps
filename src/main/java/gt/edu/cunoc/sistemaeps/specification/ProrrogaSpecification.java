package gt.edu.cunoc.sistemaeps.specification;

import gt.edu.cunoc.sistemaeps.entity.Prorroga;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import gt.edu.cunoc.sistemaeps.entity.Rol;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.entity.UsuarioProyecto;
import gt.edu.cunoc.sistemaeps.util.RolUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author edvin
 */
public class ProrrogaSpecification {

    private static final String NOMBRE_ESTUDIANTE = "nombre";
    private static final String REGISTRO_ESTUDIANTE = "registroAcademico";
    private static final String ID_USUARIO = "idUsuario";
    private static final String ID_ROL = "idRol";
    
    public static Specification<Prorroga> filterBy(ProrrogaFilter prorrogaFilter) {
        return Specification
                .where(hasNombreEstudiante(prorrogaFilter.getNombreEstudiante()))
                .and(hasRegistroEstudiante(prorrogaFilter.getRegistroEstudiante()))
                .and(hasIdUsuarioAsignado(prorrogaFilter.getIdUsuarioAsignado()));
    }
    
    private static Specification<Prorroga> hasNombreEstudiante(String nombreEstudiante) {
        return ((root, query, cb) -> {
            if (nombreEstudiante == null || nombreEstudiante.isEmpty()) {
                return cb.conjunction();
            }
            Join<Prorroga, Proyecto> proyecto = root.join("idProyectoFk");
            Join<Proyecto, Usuario> usuario = proyecto.join("idUsuarioFk");
            return cb.like(usuario.get(NOMBRE_ESTUDIANTE), nombreEstudiante);
        });
    }
    
    private static Specification<Prorroga> hasRegistroEstudiante(String registroEstudiante) {
        return ((root, query, cb) -> {
            if (registroEstudiante == null || registroEstudiante.isEmpty()) {
                return cb.conjunction();
            }
            Join<Prorroga, Proyecto> proyecto = root.join("idProyectoFk");
            Join<Proyecto, Usuario> usuario = proyecto.join("idUsuarioFk");
            return cb.equal(usuario.get(REGISTRO_ESTUDIANTE), registroEstudiante);
        });
    }
    
    private static Specification<Prorroga> hasIdUsuarioAsignado(Integer idUsuarioAsignado) {
        return ((root, query, cb) -> {
            if (idUsuarioAsignado == null) {
                return cb.conjunction();
            }
            Join<Prorroga, Proyecto> proyecto = root.join("idProyectoFk");
            Join<Proyecto, UsuarioProyecto> usuarioProyecto = proyecto.join("usuarioProyectoList");
            Join<UsuarioProyecto, Usuario> usuario = usuarioProyecto.join("idUsuarioFk");
            Join<UsuarioProyecto, Rol> rol = usuarioProyecto.join("idRolFk"); // Join with Rol
            Predicate idUsuarioPredicate = cb.equal(usuario.get(ID_USUARIO), idUsuarioAsignado);
            Predicate idRolPredicate = rol.get(ID_ROL).in( RolUtils.ID_ROL_SUPERVISOR);
            return cb.and(idUsuarioPredicate, idRolPredicate); // Combine predicates with AND
        });
    }
}
