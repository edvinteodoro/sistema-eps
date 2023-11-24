/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.service;

import gt.edu.cunoc.sistemaeps.dto.ComentarioDto;
import gt.edu.cunoc.sistemaeps.dto.ProyectoDto;
import gt.edu.cunoc.sistemaeps.dto.UsuarioDto;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author edvin
 */
public interface ProyectoService {
    
    public Proyecto getProyecto(Integer idProyecto);
    public Page<Proyecto> getProyectos(Pageable pageable) throws Exception;
    public Proyecto crearProyecto(ProyectoDto proyectoDto) throws Exception;
    public void solicitarRevision(Integer idProyecto) throws Exception;
    public void solicitarCambios(Integer idProyecto,ComentarioDto comentarioDto) throws Exception;
    public void aprobarProyectoSecretaria(Integer idProyecto) throws Exception;
    public void aprobarProyectoSupervisor(Integer idProyecto, UsuarioDto asesorDto) throws Exception;
}
