package gt.edu.cunoc.sistemaeps.service;

import gt.edu.cunoc.sistemaeps.dto.ActaDto;
import gt.edu.cunoc.sistemaeps.entity.Acta;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author edvin
 */
public interface ActaService {
    public Acta generarActaAnterproyecto(Proyecto proyecto, ActaDto actaDto) throws Exception;
    public Acta getActaAnteproyecto(Proyecto proyecto) throws Exception;
    public Acta getActaExamenGeneral(Proyecto proyecto) throws Exception;
    public Acta getActaAprobacion(Proyecto proyecto) throws Exception;
    public Acta getActa(Integer idActa) throws Exception;
    public Acta actualizarActa(Integer idActa,ActaDto actaDto) throws Exception;
    public Acta crearActaAnteproyecto(Proyecto proyecto, ActaDto actaDto) throws Exception;
    public Acta crearActaExamenGeneral(Proyecto proyecto, ActaDto actaDto) throws Exception;
    public Acta crearActaFinalizacion(Proyecto proyecto, ActaDto actaDto) throws Exception;
    public Page<Acta> getActas(String nombre, String registroAcademico,Pageable pageable) throws Exception;
    public Acta generarActaExamenGeneral(Proyecto proyecto, ActaDto actaDto) throws Exception;
    public Acta generarActaAprobacion(Proyecto proyecto, ActaDto actaDto) throws Exception;
}
