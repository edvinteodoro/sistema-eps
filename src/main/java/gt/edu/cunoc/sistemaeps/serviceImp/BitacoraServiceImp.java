package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.entity.Bitacora;
import gt.edu.cunoc.sistemaeps.entity.Recurso;
import gt.edu.cunoc.sistemaeps.entity.Rol;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.repository.BitacoraRepository;
import gt.edu.cunoc.sistemaeps.repository.RecursoRepository;
import gt.edu.cunoc.sistemaeps.service.BitacoraService;
import gt.edu.cunoc.sistemaeps.service.RolService;
import gt.edu.cunoc.sistemaeps.service.UsuarioService;
import gt.edu.cunoc.sistemaeps.specification.BitacoraFilter;
import gt.edu.cunoc.sistemaeps.specification.BitacoraSpecification;
import gt.edu.cunoc.sistemaeps.util.RolUtils;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 *
 * @author edvin
 */
@Service
public class BitacoraServiceImp implements BitacoraService{

    private final BitacoraRepository bitacoraRepository;
    private final RolService rolService;
    private final UsuarioService usuarioService;
    private final RecursoRepository recursoRepository;

    public BitacoraServiceImp(BitacoraRepository bitacoraRepository, RolService rolService, 
            UsuarioService usuarioService,RecursoRepository recursoRepository) {
        this.bitacoraRepository = bitacoraRepository;
        this.rolService = rolService;
        this.usuarioService = usuarioService;
        this.recursoRepository = recursoRepository;
    }
    
    
    @Override
    public Page<Bitacora> getBitacoras(Pageable pageable) throws Exception{
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Rol rolUsuario = this.rolService.getLoggedUsuarioRol();
        BitacoraFilter filter = new BitacoraFilter();
        if (Objects.equals(rolUsuario.getIdRol(), RolUtils.ID_ROL_ESTUDIANTE)) {
            filter.setRegistroEstudiante(usuario.getRegistroAcademico());
            Specification<Bitacora> spec = BitacoraSpecification.filterBy(filter);
            return bitacoraRepository.findAll(spec, pageable);
        } else if (Objects.equals(rolUsuario.getIdRol(), RolUtils.ID_ROL_SECRETARIA)
                || Objects.equals(rolUsuario.getIdRol(), RolUtils.ID_ROL_SUPERVISOR)
                || Objects.equals(rolUsuario.getIdRol(), RolUtils.ID_ROL_ASESOR)
                || Objects.equals(rolUsuario.getIdRol(), RolUtils.ID_ROL_COORDINADOR_CARRERA)
                || Objects.equals(rolUsuario.getIdRol(), RolUtils.ID_ROL_CONTRAPARTE)) {
            filter.setIdUsuarioAsignado(usuario.getIdUsuario());
            Specification<Bitacora> spec = BitacoraSpecification.filterBy(filter);
            return bitacoraRepository.findAll(spec, pageable);
        } else {
            throw new Exception("Sin permisos para ver proyecto");
        }
    }

    @Override
    public Bitacora getBitacora(Integer idBitacora) throws Exception {
        return this.bitacoraRepository.findById(idBitacora).get();
    }

    @Override
    public List<Recurso> getRecursosBitacora(Integer idBitacora) throws Exception {
        return this.recursoRepository.findRecursos(idBitacora);
    }
    
}
