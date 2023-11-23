package gt.edu.cunoc.sistemaeps.service;

import gt.edu.cunoc.sistemaeps.dto.InstitucionDto;
import gt.edu.cunoc.sistemaeps.entity.Institucion;

/**
 *
 * @author edvin
 */
public interface InstitucionService {
    public Institucion save(Institucion institucion) throws Exception;
    public Institucion crearInstitucion(InstitucionDto institucionDto) throws Exception;
    public Institucion actualizarInstitucion(Integer idProyecto,InstitucionDto institucionDto) throws Exception;
}
