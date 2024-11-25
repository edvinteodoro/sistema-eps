package gt.edu.cunoc.sistemaeps.specification;

import gt.edu.cunoc.sistemaeps.entity.Bitacora;
import gt.edu.cunoc.sistemaeps.entity.Carrera;
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
public class BitacoraSpecification {

    private static final String REVISION_CONTRAPARTE = "revisionContraparte";
    private static final String NOMBRE_ESTUDIANTE = "nombreCompleto";
    private static final String REGISTRO_ESTUDIANTE = "registroAcademico";
    private static final String ID_USUARIO = "idUsuario";
    private static final String ID_ROL = "idRol";
    private static final String ID_CARRERA = "idCarrera";
    private static final String ID_PROYECTO = "idProyecto"; 

    public static Specification<Bitacora> filterBy(BitacoraFilter bitacoraFilter) {
        return Specification
                .where(hasNombreEstudiante(bitacoraFilter.getNombreEstudiante()))
                .and(hasRevisionContraparte(bitacoraFilter.getRevisionContraparte()))
                .and(hasRegistroEstudiante(bitacoraFilter.getRegistroEstudiante()))
                .and(hasIdUsuarioAsignado(bitacoraFilter.getIdUsuarioAsignado()))
                .and(hasIdCarrera(bitacoraFilter.getIdCarrera()))
                .and(hasIdProyecto(bitacoraFilter.getIdProyecto()));
    }

    private static Specification<Bitacora> hasNombreEstudiante(String nombreEstudiante) {
        return ((root, query, cb) -> {
            if (nombreEstudiante == null || nombreEstudiante.isEmpty()) {
                return cb.conjunction();
            }
            Join<Bitacora, Proyecto> proyecto = root.join("idProyectoFk");
            Join<Proyecto, Usuario> usuario = proyecto.join("idUsuarioFk");
            return cb.like(usuario.get(NOMBRE_ESTUDIANTE), "%"+nombreEstudiante+"%");
        });
    }

    private static Specification<Bitacora> hasRevisionContraparte(Boolean revisonContraparte) {
        return ((root, query, cb) -> revisonContraparte == null ? cb.conjunction()
                : cb.equal(root.get(REVISION_CONTRAPARTE), revisonContraparte));
    }

    private static Specification<Bitacora> hasRegistroEstudiante(String registroEstudiante) {
        return ((root, query, cb) -> {
            if (registroEstudiante == null || registroEstudiante.isEmpty()) {
                return cb.conjunction();
            }
            Join<Bitacora, Proyecto> proyecto = root.join("idProyectoFk");
            Join<Proyecto, Usuario> usuario = proyecto.join("idUsuarioFk");
            return cb.equal(usuario.get(REGISTRO_ESTUDIANTE), registroEstudiante);
        });
    }

    private static Specification<Bitacora> hasIdUsuarioAsignado(Integer idUsuarioAsignado) {
        return ((root, query, cb) -> {
            if (idUsuarioAsignado == null) {
                return cb.conjunction();
            }
            Join<Bitacora, Proyecto> proyecto = root.join("idProyectoFk");
            Join<Proyecto, UsuarioProyecto> usuarioProyecto = proyecto.join("usuarioProyectoList");
            Join<UsuarioProyecto, Usuario> usuario = usuarioProyecto.join("idUsuarioFk");
            Join<UsuarioProyecto, Rol> rol = usuarioProyecto.join("idRolFk"); // Join with Rol
            Predicate idUsuarioPredicate = cb.equal(usuario.get(ID_USUARIO), idUsuarioAsignado);
            Predicate idRolPredicate = rol.get(ID_ROL).in(RolUtils.ID_ROL_ASESOR, RolUtils.ID_ROL_CONTRAPARTE, RolUtils.ID_ROL_SUPERVISOR);
            return cb.and(idUsuarioPredicate, idRolPredicate); // Combine predicates with AND
        });
    }
    
    private static Specification<Bitacora> hasIdCarrera(Integer idCarrera) {
        return ((root, query, cb) -> {
            if (idCarrera==null) {
                return cb.conjunction();
            }
            Join<Bitacora, Proyecto> proyecto = root.join("idProyectoFk");
            Join<Proyecto, Carrera> carrera = proyecto.join("idCarreraFk");
            return cb.equal(carrera.get(ID_CARRERA), idCarrera); // Combine predicates with AND
        });
    }
    
    private static Specification<Bitacora> hasIdProyecto(Integer idProyecto) {
        return ((root, query, cb) -> {
            if (idProyecto==null) {
                return cb.conjunction();
            }
            Join<Bitacora, Proyecto> proyecto = root.join("idProyectoFk");
            return cb.equal(proyecto.get(ID_PROYECTO), idProyecto); // Combine predicates with AND
        });
    }
}
