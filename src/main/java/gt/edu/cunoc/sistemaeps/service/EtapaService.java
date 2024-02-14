/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.service;

import gt.edu.cunoc.sistemaeps.entity.Etapa;
import gt.edu.cunoc.sistemaeps.entity.EtapaProyecto;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;

/**
 *
 * @author edvin
 */
public interface EtapaService {

    public Etapa getEtapa(Integer idEtapa);
    public EtapaProyecto saveEtapaProyecto(EtapaProyecto etapaProyecto) throws Exception;
    public EtapaProyecto activarEtapaProyecto(EtapaProyecto etapaProyecto) throws Exception;
    public EtapaProyecto desactivarEtapaProyecto(EtapaProyecto etapaProyecto) throws Exception;
    public EtapaProyecto getEtapaProyecto(Integer idProyecto, Integer idEtapa);
    public EtapaProyecto crearEtapaProyecto(Integer idEtapa, Proyecto proyecto) throws Exception;
    public EtapaProyecto activarModoEdicion(EtapaProyecto etapaProyecto) throws Exception;
    public EtapaProyecto activarModoRevision(EtapaProyecto etapaProyecto) throws Exception;
    public EtapaProyecto finalizarEtapaProyecto(EtapaProyecto etapaProyecto) throws Exception;
    public EtapaProyecto getEtapaProyectoActivo(Integer idProyecto) throws Exception;

}
