package gt.edu.cunoc.sistemaeps.service;

import gt.edu.cunoc.sistemaeps.entity.Correlativo;
import java.util.List;

/**
 *
 * @author edvin
 */
public interface CorrelativoService {
    public Correlativo save(Correlativo correlativo);
    public Correlativo getCorrelativo(Integer idCarrera,Integer idEtapa);
    public Correlativo getCorrelativo(Integer idEtapa);
    public List<Correlativo> getAll();
}
