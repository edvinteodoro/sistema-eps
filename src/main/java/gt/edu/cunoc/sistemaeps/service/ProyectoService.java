/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.service;

import gt.edu.cunoc.sistemaeps.dto.ActaDto;
import gt.edu.cunoc.sistemaeps.dto.BitacoraDto;
import gt.edu.cunoc.sistemaeps.dto.ComentarioDto;
import gt.edu.cunoc.sistemaeps.dto.ConvocatoriaDto;
import gt.edu.cunoc.sistemaeps.dto.ProyectoDto;
import gt.edu.cunoc.sistemaeps.dto.UsuarioDto;
import gt.edu.cunoc.sistemaeps.entity.Acta;
import gt.edu.cunoc.sistemaeps.entity.Bitacora;
import gt.edu.cunoc.sistemaeps.entity.Comentario;
import gt.edu.cunoc.sistemaeps.entity.Convocatoria;
import gt.edu.cunoc.sistemaeps.entity.Persona;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author edvin
 */
public interface ProyectoService {

    public Proyecto getProyecto(Integer idProyecto);

    public Proyecto actualizarProyecto(Integer idProyecto, ProyectoDto proyectoDto) throws Exception;

    public Page<Proyecto> getProyectos(String nombre, String registroAcademico, Pageable pageable) throws Exception;

    public Proyecto finalizarProyecto(Integer idProyecto, ComentarioDto comentario) throws Exception;

    public Proyecto crearProyecto(ProyectoDto proyectoDto) throws Exception;

    public List<Proyecto> getProyectosActivos() throws Exception;

    public void solicitarRevision(Integer idProyecto) throws Exception;

    public Comentario solicitarCambios(Integer idProyecto, ComentarioDto comentarioDto) throws Exception;

    public void aprobarProyectoSecretaria(Integer idProyecto) throws Exception;

    public void aprobarProyectoSupervisor(Integer idProyecto, UsuarioDto asesorDto) throws Exception;

    public Convocatoria getConvocatoriaAnteproyecto(Integer idProyecto) throws Exception;

    public Convocatoria getConvocatoriaExamenGeneral(Integer idProyecto) throws Exception;

    public void crearConvocatoriaAnteproyecto(Integer idProyecto, ConvocatoriaDto convocatoriaDto) throws Exception;

    public void actualizarConvocatoriaAnteproyecto(Integer idProyecto, ConvocatoriaDto convocatoriaDto) throws Exception;

    public void cargarConvocatoria(Integer idProyecto, MultipartFile convocatoria) throws Exception;

    public void cargarCartaAceptacionContraparte(Integer idProyecto, MultipartFile cartaAceptacion,MultipartFile oficioContraparte) throws Exception;

    public Acta crearActa(Integer idProyecto, ActaDto actaDto) throws Exception;

    public Acta generarActaAnteproyecto(Integer idProyecto, ActaDto actaDto) throws Exception;

    public Acta generarActaExamenGeneral(Integer idProyecto, ActaDto actaDto) throws Exception;

    public Acta generarActaAprobacion(Integer idProyecto, ActaDto actaDto) throws Exception;

    public Acta getActaAnteproyecto(Integer idProyecto) throws Exception;

    public Acta getActaExamenGeneral(Integer idProyecto) throws Exception;

    public Acta getActaAprobacion(Integer idProyecto) throws Exception;

    public void habilitarBitacora(Integer idProyecto, UsuarioDto asesorDto) throws Exception;

    public void finalizarBitacora(Integer idProyecto, MultipartFile finiquitoContraparte) throws Exception;

    public void aprobarBitacora(Integer idProyecto) throws Exception;

    public void rechazarBitacora(Integer idProyecto) throws Exception;

    public void cargarInformeFinal(Integer idProyecto, MultipartFile cartaAsesor,MultipartFile informeFinal) throws Exception;

    public Bitacora crearBitacora(Integer idProyecto, BitacoraDto bitacoraDto) throws Exception;

    //public Usuario actualizarSupervisor(Integer idProyecto, UsuarioDto usuarioDto) throws Exception;
    public Usuario actualizarAsesor(Integer idProyecto, UsuarioDto usuarioDto) throws Exception;

    public Usuario actualizarContraparte(Integer idProyecto, UsuarioDto usuarioDto) throws Exception;

    public Persona agregarAsesorTecnico(Integer idProyecto, UsuarioDto usuarioDto) throws Exception;

    public void cargarArticulo(Integer idProyecto, MultipartFile articulo, MultipartFile traduccionArticulo, MultipartFile constanciaLinguistica) throws Exception;

    public void aprobarInformeFinalSupervisor(Integer idProyecto) throws Exception;

    //public void cargarConstanciaLinguistica(Integer idProyecto, MultipartFile constanciaLinguistica) throws Exception;

    public void cargarDictamenRevsion(Integer idProyecto, MultipartFile dictamenRevision, MultipartFile cartaRevision) throws Exception;
}
