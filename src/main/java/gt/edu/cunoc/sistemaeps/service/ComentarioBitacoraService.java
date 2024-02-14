package gt.edu.cunoc.sistemaeps.service;

import gt.edu.cunoc.sistemaeps.dto.ComentarioBitacoraDto;
import gt.edu.cunoc.sistemaeps.entity.ComentarioBitacora;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author edvin
 */
public interface ComentarioBitacoraService {
    public Page<ComentarioBitacora> getComentariosBitacora(Integer idBitacora,Pageable pageable) throws Exception;
    public ComentarioBitacora crearComentario(Integer idBitacora,ComentarioBitacoraDto comentarioBitacoraDto) throws Exception;
}
