package gt.edu.cunoc.sistemaeps.specification;

import gt.edu.cunoc.sistemaeps.entity.Carrera;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.entity.UsuarioProyecto;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author edvin
 */
public class ProyectoSpecification {

    private static final String ACTIVO = "activo";
    private static final String NOMBRE_ESTUDIANTE = "nombreCompleto";
    private static final String REGISTRO_ESTUDIANTE = "registroAcademico";
    private static final String ID_USUARIO = "idUsuario";
    private static final String ID_CARRERA = "idCarrera";

    public static Specification<Proyecto> filterBy(ProyectoFilter proyectoFilter) {
        return Specification
                .where(hasNombreEstudiante(proyectoFilter.getNombreEstudiante()))
                .and(hasActivo(proyectoFilter.getActivo()))
                .and(hasRegistroEstudiante(proyectoFilter.getRegistroEstudiante()))
                .and(hasIdUsuarioAsignado(proyectoFilter.getIdUsuarioAsignado()));
    }

    private static Specification<Proyecto> hasNombreEstudiante(String nombreEstudiante) {
        return ((root, query, cb) -> {
            if (nombreEstudiante == null || nombreEstudiante.isBlank()) {
                return cb.conjunction();
            }
            Join<Proyecto, Usuario> usuario = root.join("idUsuarioFk");
            return cb.like(usuario.get(NOMBRE_ESTUDIANTE), "%" + nombreEstudiante + "%");
        });
    }

    private static Specification<Proyecto> hasActivo(Boolean activo) {
        return ((root, query, cb) -> activo == null ? cb.conjunction() : cb.equal(root.get(ACTIVO), activo));
    }

    private static Specification<Proyecto> hasRegistroEstudiante(String registroEstudiante) {
        return ((root, query, cb) -> {
            if (registroEstudiante == null || registroEstudiante.isBlank()) {
                return cb.conjunction();
            }
            Join<Proyecto, Usuario> usuario = root.join("idUsuarioFk");
            return cb.equal(usuario.get(REGISTRO_ESTUDIANTE), registroEstudiante);
        });
    }

    private static Specification<Proyecto> hasIdUsuarioAsignado(Integer idUsuarioAsignado) {
        return ((root, query, cb) -> {
            if (idUsuarioAsignado == null) {
                return cb.conjunction();
            }
            Join<Proyecto, UsuarioProyecto> usuarioProyecto = root.join("usuarioProyectoList");
            Join<UsuarioProyecto, Usuario> usuario = usuarioProyecto.join("idUsuarioFk");
            return cb.equal(usuario.get(ID_USUARIO), idUsuarioAsignado);
        });
    }

    private static Specification<Proyecto> hasIdCarrera(Integer idCarrera) {
        return ((root, query, cb) -> {
            if (idCarrera == null) {
                return cb.conjunction();
            }
            Join<Proyecto, Carrera> carrera = root.join("idCarreraFk");
            return cb.equal(carrera.get(ID_CARRERA), idCarrera);
        });
    }

    public static Specification<Proyecto> filter(ProyectoFilter proyectoFilter) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (proyectoFilter.getIdUsuarioAsignado() != null) {
                Join<Proyecto, UsuarioProyecto> usuarioProyecto = root.join("usuarioProyectoList");
                Join<UsuarioProyecto, Usuario> usuario = usuarioProyecto.join("idUsuarioFk");
                predicate = cb.and(predicate,
                        cb.equal(usuario.get("idUsuario"), proyectoFilter.getIdUsuarioAsignado()),
                        cb.equal(usuarioProyecto.get("activo"), Boolean.TRUE));
            }

            if (proyectoFilter.getIdCarrera() != null) {
                predicate = cb.or(predicate,
                        cb.equal(root.get("idCarreraFk").get("idCarrera"), proyectoFilter.getIdCarrera()));
            }

            return predicate;
        };
    }
}
