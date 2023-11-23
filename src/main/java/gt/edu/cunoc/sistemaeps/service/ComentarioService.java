package gt.edu.cunoc.sistemaeps.service;

import gt.edu.cunoc.sistemaeps.dto.ComentarioDto;
import gt.edu.cunoc.sistemaeps.entity.Comentario;
import gt.edu.cunoc.sistemaeps.entity.EtapaProyecto;
import gt.edu.cunoc.sistemaeps.entity.Rol;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author edvin
 */
public interface ComentarioService {
    public Comentario saveComentario(Comentario comentario) throws Exception;
    public Comentario crearComentario(ComentarioDto comentarioDto, EtapaProyecto etapaProyecto, Usuario usuario, Rol rol) throws Exception;
    public Comentario crearComentario(Integer idProyecto,ComentarioDto comentarioDto) throws Exception;
    public Page<Comentario> getComentarios(Integer idProyecto, Pageable pageable) throws Exception;
}
