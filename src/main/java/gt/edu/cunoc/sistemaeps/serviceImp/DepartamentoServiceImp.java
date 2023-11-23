/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.entity.Departamento;
import gt.edu.cunoc.sistemaeps.entity.Municipio;
import gt.edu.cunoc.sistemaeps.repository.DepartamentoRepository;
import gt.edu.cunoc.sistemaeps.repository.MunicipioRepository;
import gt.edu.cunoc.sistemaeps.service.DepartamentoService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author edvin
 */
@Service
public class DepartamentoServiceImp implements DepartamentoService {

    private final MunicipioRepository municipioRepository;
    private final DepartamentoRepository departamentoRepository;

    public DepartamentoServiceImp(MunicipioRepository municipioRepository, DepartamentoRepository departamentoRepository) {
        this.municipioRepository = municipioRepository;
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    public List<Municipio> getMunicipios(Integer idDepartamento) throws Exception {
        Departamento departamento = this.departamentoRepository.findById(idDepartamento).get();
        return departamento.getMunicipioList();
    }

    @Override
    public Municipio getMunicipio(Integer idMunicipio) throws Exception {
        return this.municipioRepository.findById(idMunicipio).get();
    }

    @Override
    public List<Departamento> getDepartamentos() throws Exception {
        return this.departamentoRepository.findAll();
    }

    @Override
    public Departamento getDepartamento(Integer idMunicipio) throws Exception {
        return this.departamentoRepository.findDepartamento(idMunicipio);
    }

}
