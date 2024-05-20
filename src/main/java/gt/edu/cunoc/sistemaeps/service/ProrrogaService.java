package gt.edu.cunoc.sistemaeps.service;

import gt.edu.cunoc.sistemaeps.dto.ProrrogaDto;
import gt.edu.cunoc.sistemaeps.entity.Prorroga;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author edvin
 */
public interface ProrrogaService {
    public Page<Prorroga> getProrrogas(Pageable pageable) throws Exception;
    public Prorroga crearProrroga(Integer idProyecto,ProrrogaDto prorrogaDto) throws Exception;
    public Prorroga responderProrroga(Integer idProrroga,ProrrogaDto prorrogaDto) throws Exception;
    public Prorroga actualizarProrroga(Integer idProrroga, ProrrogaDto prorrogaDto) throws Exception;
    public Prorroga getProrroga(Integer idProrroga) throws Exception;
}
