/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.service;

import gt.edu.cunoc.sistemaeps.dto.BitacoraDto;
import gt.edu.cunoc.sistemaeps.dto.RecursoDto;
import gt.edu.cunoc.sistemaeps.entity.Bitacora;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import gt.edu.cunoc.sistemaeps.entity.Recurso;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author edvin
 */
public interface BitacoraService {

    public Page<Bitacora> getBitacoras(String nombre, String registroAcademico,Integer idProyecto,Pageable pageable) throws Exception;

    public Bitacora crearBitacora(Proyecto proyecto, BitacoraDto bitacoraDto) throws Exception;

    public Bitacora getBitacora(Integer idBitacora) throws Exception;
    
    public Bitacora actualizarBitacora(Integer idBitacora, BitacoraDto bitacoraDto) throws Exception;

    public Recurso crearRecursoBitacora(Integer idBitacora, RecursoDto recursoDto) throws Exception;

    public List<Recurso> getRecursosBitacora(Integer idBitacora) throws Exception;

    public void finalizarBitacora(Proyecto proyecto,
            MultipartFile finiquitoContraparte) throws Exception;
    
    public Bitacora eliminarRecurso(Integer idRecurso) throws Exception;
    
    
    public Bitacora revisarBitacora(Integer idBitacora) throws Exception;
}
