/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.service;

import gt.edu.cunoc.sistemaeps.dto.ElementoProyectoDto;
import gt.edu.cunoc.sistemaeps.entity.Elemento;
import gt.edu.cunoc.sistemaeps.entity.ElementoProyecto;
import gt.edu.cunoc.sistemaeps.entity.EtapaProyecto;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author edvin
 */
public interface ElementoService {

    public Elemento getElemento(Integer idElemento);
    
    public List<Elemento> getElementos();

    public ElementoProyecto saveElementoProyecto(ElementoProyecto elementoProyecto) throws Exception;

    public ElementoProyecto crearElementoProyecto(Integer idProyecto, Integer idElemento,
            ElementoProyectoDto elementoProyectoDto) throws Exception;
    
    public ElementoProyecto crearElementoProyecto(Proyecto proyecto, Elemento elemento,
            EtapaProyecto etapaProyecto, MultipartFile file) throws Exception;
    
    public ElementoProyecto getElementoProyectoActivo(Integer idProyecto, Integer idElemento) throws Exception;
    
    public void desactivarElementoProyectoActivo(Integer idElementoProyecto) throws Exception;
    
}
