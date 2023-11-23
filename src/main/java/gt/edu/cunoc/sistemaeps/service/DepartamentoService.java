package gt.edu.cunoc.sistemaeps.service;

import gt.edu.cunoc.sistemaeps.entity.Departamento;
import gt.edu.cunoc.sistemaeps.entity.Municipio;
import java.util.List;

/**
 *
 * @author edvin
 */
public interface DepartamentoService {

    public List<Municipio> getMunicipios(Integer idDepartamento) throws Exception;
    public Municipio getMunicipio(Integer idMunicipio) throws Exception;
    public List<Departamento> getDepartamentos() throws Exception;
    public Departamento getDepartamento(Integer idMunicipio) throws Exception;
}
