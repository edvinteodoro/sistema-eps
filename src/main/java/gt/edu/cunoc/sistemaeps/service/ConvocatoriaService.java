package gt.edu.cunoc.sistemaeps.service;

import gt.edu.cunoc.sistemaeps.dto.ConvocatoriaDto;
import gt.edu.cunoc.sistemaeps.entity.Convocatoria;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author edvin
 */

public interface ConvocatoriaService {
    public Convocatoria generarConvocatoriaAnteproyecto(Proyecto proyecto, ConvocatoriaDto convocatoriaDto) throws Exception;
    public Convocatoria actualizarConvocatoriaAnteproyecto(Proyecto proyecto, ConvocatoriaDto convocatoriaDto) throws Exception;
    public Convocatoria getConvocatoriaAnteproyecto(Proyecto proyecto) throws Exception;
    public Convocatoria generarConvocatoriaExamenGeneral(Proyecto proyecto, ConvocatoriaDto convocatoriaDto) throws Exception;
    public Convocatoria getConvocatoriaExamenGeneral(Proyecto proyecto) throws Exception;
    public Convocatoria actualizarConvocatoriaExamenGeneral(Proyecto proyecto, ConvocatoriaDto convocatoriaDto) throws Exception;
    public void cargarConvocatoriaExamenGeneral(Proyecto proyecto, MultipartFile file) throws Exception;
    public void cargarConvocatoria(Proyecto proyecto, MultipartFile file) throws Exception;
}
