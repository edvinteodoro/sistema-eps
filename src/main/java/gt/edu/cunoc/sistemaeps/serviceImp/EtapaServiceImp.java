/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.entity.Etapa;
import gt.edu.cunoc.sistemaeps.entity.EtapaProyecto;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import gt.edu.cunoc.sistemaeps.repository.EtapaProyectoRepository;
import gt.edu.cunoc.sistemaeps.repository.EtapaRepository;
import gt.edu.cunoc.sistemaeps.service.EtapaService;
import gt.edu.cunoc.sistemaeps.util.DateUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author edvin
 */
@Service
public class EtapaServiceImp implements EtapaService {

    private final EtapaRepository etapaRepository;
    private final EtapaProyectoRepository etapaProyectoRepository;

    public EtapaServiceImp(EtapaRepository etapaRepository, EtapaProyectoRepository etapaProyectoRepository) {
        this.etapaRepository = etapaRepository;
        this.etapaProyectoRepository = etapaProyectoRepository;
    }

    @Override
    public Etapa getEtapa(Integer idEtapa) {
        return this.etapaRepository.findById(idEtapa).get();
    }

    @Override
    public EtapaProyecto saveEtapaProyecto(EtapaProyecto etapaProyecto) throws Exception {
        return this.etapaProyectoRepository.save(etapaProyecto);
    }

    @Override
    public EtapaProyecto getEtapaProyecto(Integer idProyecto, Integer idEtapa) {
        return this.etapaProyectoRepository.findEtapaProyecto(idProyecto, idEtapa);
    }

    @Override
    public EtapaProyecto crearEtapaProyecto(Integer idEtapa, Proyecto proyecto) throws Exception {
        Etapa etapa = getEtapa(idEtapa);
        EtapaProyecto etapaProyecto = new EtapaProyecto();
        etapaProyecto.setIdEtapaFk(etapa);
        etapaProyecto.setIdProyectoFk(proyecto);
        return saveEtapaProyecto(etapaProyecto);
    }

    @Override
    public EtapaProyecto getEtapaProyectoActivo(Integer idProyecto) throws Exception {
        return this.etapaProyectoRepository.findEtapaProyecto(idProyecto, Boolean.TRUE);
    }

    @Override
    public EtapaProyecto finalizarEtapaProyecto(EtapaProyecto etapaProyecto) throws Exception {
        etapaProyecto.setActivo(Boolean.FALSE);
        etapaProyecto.setFechaFin(DateUtils.getCurrentDate());
        etapaProyecto.setEditable(Boolean.FALSE);
        return saveEtapaProyecto(etapaProyecto);
    }

    @Override
    public EtapaProyecto activarModoEdicion(EtapaProyecto etapaProyecto) throws Exception {
        etapaProyecto.setEditable(Boolean.TRUE);
        return saveEtapaProyecto(etapaProyecto);
    }

    @Override
    public EtapaProyecto activarModoRevision(EtapaProyecto etapaProyecto) throws Exception {
        etapaProyecto.setEditable(Boolean.FALSE);
        return saveEtapaProyecto(etapaProyecto);
    }

}
