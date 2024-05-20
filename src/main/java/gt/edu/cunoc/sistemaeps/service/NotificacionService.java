package gt.edu.cunoc.sistemaeps.service;

import gt.edu.cunoc.sistemaeps.dto.ComentarioDto;
import gt.edu.cunoc.sistemaeps.entity.Convocatoria;
import gt.edu.cunoc.sistemaeps.entity.EtapaProyecto;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import gt.edu.cunoc.sistemaeps.entity.Rol;
import gt.edu.cunoc.sistemaeps.entity.Usuario;

/**
 *
 * @author edvin
 */
public interface NotificacionService {
    public void notificarEtapaNuevo(Usuario usuario, EtapaProyecto etapaProyecto);
    public void notificarAsignacionUsuarioProyecto(Usuario usuario,Rol rol,Proyecto proyecto);
    public void notificarSolicitudRevision(Usuario usuario,Proyecto proyecto);
    public void notificarSolicitudCambios(Usuario usuario,Proyecto proyecto,ComentarioDto comentarioDto);
    public void notificarGeneracionConvocatoria(Usuario usuario,Proyecto proyecto, String urlFile);
    public void notificarGeneracionConvocatoriaExamenGeneral(Usuario usuario, Proyecto proyecto, String urlFile); 
    public void notificarCreacionActa(Usuario usuario,Proyecto proyecto);
    public void notificarCreacionActaExamenGeneral(Usuario usuario,Proyecto proyecto);
    public void notificarCreacionActaFinalizacion(Usuario usuario,Proyecto proyecto);
    //public void notificarCargaConvocatoria(Proyecto proyecto,String urlFile);
    public void notificarConvocatoriaAnteproyecto(Usuario usuario, Proyecto proyecto,Convocatoria convocatoria, String urlFile);
    public void notificarConvocatoriaExamenGeneral(Usuario usuario, Proyecto proyecto,Convocatoria convocatoria, String urlFile);
    public void notificarFinalizacionProyecto(Usuario usuario,Proyecto proyecto,ComentarioDto comentarioDto);
    public void notificarRegistroBitacora(Usuario usuario, Proyecto proyecto);
    public void notificarSolicitudProrroga(Usuario usuario, Proyecto proyecto);
    public void notificarRespuestaProrroga(Usuario usuario, Proyecto proyecto);
}
