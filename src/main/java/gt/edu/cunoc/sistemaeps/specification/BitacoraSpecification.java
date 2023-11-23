package gt.edu.cunoc.sistemaeps.specification;

import gt.edu.cunoc.sistemaeps.entity.Bitacora;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.entity.UsuarioProyecto;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author edvin
 */
public class BitacoraSpecification {

    private static final String REVISION_CONTRAPARTE = "revisionContraparte";
    private static final String NOMBRE_ESTUDIANTE = "nombre";
    private static final String REGISTRO_ESTUDIANTE = "registroAcademico";
    private static final String ID_USUARIO = "idUsuario";

    public static Specification<Bitacora> filterBy(BitacoraFilter bitacoraFilter) {
        return Specification
                .where(hasNombreEstudiante(bitacoraFilter.getNombreEstudiante()))
                .and(hasRevisionContraparte(bitacoraFilter.getRevisionContraparte()))
                .and(hasRegistroEstudiante(bitacoraFilter.getRegistroEstudiante()))
                .and(hasIdUsuarioAsignado(bitacoraFilter.getIdUsuarioAsignado()));
    }

    private static Specification<Bitacora> hasNombreEstudiante(String nombreEstudiante) {
        return ((root, query, cb) -> {
            if (nombreEstudiante == null || nombreEstudiante.isEmpty()) {
                return cb.conjunction();
            }
            Join<Bitacora, Proyecto> proyecto = root.join("idProyectoFk");
            Join<Proyecto, Usuario> usuario = proyecto.join("idUsuarioFk");
            return cb.like(usuario.get(NOMBRE_ESTUDIANTE), nombreEstudiante);
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
            return cb.equal(usuario.get(ID_USUARIO), idUsuarioAsignado);
        });
    }
}
