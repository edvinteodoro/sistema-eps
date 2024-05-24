package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.dto.BitacoraDto;
import gt.edu.cunoc.sistemaeps.dto.RecursoDto;
import gt.edu.cunoc.sistemaeps.entity.Bitacora;
import gt.edu.cunoc.sistemaeps.entity.Carrera;
import gt.edu.cunoc.sistemaeps.entity.Elemento;
import gt.edu.cunoc.sistemaeps.entity.EtapaProyecto;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import gt.edu.cunoc.sistemaeps.entity.Recurso;
import gt.edu.cunoc.sistemaeps.entity.Rol;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.entity.UsuarioProyecto;
import gt.edu.cunoc.sistemaeps.repository.BitacoraRepository;
import gt.edu.cunoc.sistemaeps.repository.RecursoRepository;
import gt.edu.cunoc.sistemaeps.service.BitacoraService;
import gt.edu.cunoc.sistemaeps.service.CarreraService;
import gt.edu.cunoc.sistemaeps.service.ElementoService;
import gt.edu.cunoc.sistemaeps.service.EtapaService;
import gt.edu.cunoc.sistemaeps.service.NotificacionService;
import gt.edu.cunoc.sistemaeps.service.RolService;
import gt.edu.cunoc.sistemaeps.service.StorageService;
import gt.edu.cunoc.sistemaeps.service.UsuarioProyectoService;
import gt.edu.cunoc.sistemaeps.service.UsuarioService;
import gt.edu.cunoc.sistemaeps.specification.BitacoraFilter;
import gt.edu.cunoc.sistemaeps.specification.BitacoraSpecification;
import gt.edu.cunoc.sistemaeps.util.EtapaUtils;
import gt.edu.cunoc.sistemaeps.util.RolUtils;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author edvin
 */
@Service
public class BitacoraServiceImp implements BitacoraService {

    private final BitacoraRepository bitacoraRepository;
    private final RolService rolService;
    private final UsuarioService usuarioService;
    private final UsuarioProyectoService usuarioProyectoService;
    private final RecursoRepository recursoRepository;
    private final StorageService storageService;
    private final EtapaService etapaService;
    private final ElementoService elementoService;
    private final CarreraService carreraService;
    private final NotificacionService notificacionService;

    private final int ID_ELEMENTO_CARTA_FINALIZACION_ASESOR = 12;
    private final int ID_ELEMENTO_FINIQUITO_CONTRAPARTE = 13;
    private final int ID_ELEMENTO_INFORME_FINAL = 14;

    public BitacoraServiceImp(BitacoraRepository bitacoraRepository, RolService rolService,
            UsuarioService usuarioService, RecursoRepository recursoRepository,
            StorageService storageService, EtapaService etapaService,
            ElementoService elementoService, UsuarioProyectoService usuarioProyectoService,
            CarreraService carreraService, NotificacionService notificacionService) {
        this.bitacoraRepository = bitacoraRepository;
        this.rolService = rolService;
        this.usuarioService = usuarioService;
        this.recursoRepository = recursoRepository;
        this.storageService = storageService;
        this.etapaService = etapaService;
        this.elementoService = elementoService;
        this.usuarioProyectoService = usuarioProyectoService;
        this.carreraService = carreraService;
        this.notificacionService = notificacionService;
    }

    @Override
    public Page<Bitacora> getBitacoras(String nombre, String registroAcademico, Pageable pageable) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Rol rolUsuario = this.rolService.getLoggedUsuarioRol();
        BitacoraFilter filter = new BitacoraFilter();
        filter.setNombreEstudiante(nombre);
        filter.setRegistroEstudiante(registroAcademico);
        if (Objects.equals(rolUsuario.getIdRol(), RolUtils.ID_ROL_ESTUDIANTE)) {
            filter.setRegistroEstudiante(usuario.getRegistroAcademico());
            Specification<Bitacora> spec = BitacoraSpecification.filterBy(filter);
            return bitacoraRepository.findAll(spec, pageable);
        } else if (Objects.equals(rolUsuario.getIdRol(), RolUtils.ID_ROL_SUPERVISOR)) {
            Carrera carrera = this.carreraService.getCarrerasUsuario(usuario.getIdUsuario()).get(0).getIdCarreraFk();
            filter.setIdCarrera(carrera.getIdCarrera());
            Specification<Bitacora> spec = BitacoraSpecification.filterBy(filter);
            return bitacoraRepository.findAll(spec, pageable);
        } else {
            filter.setIdUsuarioAsignado(usuario.getIdUsuario());
            Specification<Bitacora> spec = BitacoraSpecification.filterBy(filter);
            return bitacoraRepository.findAll(spec, pageable);
        }
    }

    @Override
    public Bitacora crearBitacora(Proyecto proyecto, BitacoraDto bitacoraDto) throws Exception {
        Bitacora bitacora = new Bitacora(bitacoraDto);
        bitacora.setIdProyectoFk(proyecto);
        bitacora = this.bitacoraRepository.save(bitacora);
        notificarBitacora(proyecto);
        return bitacora;
    }

    private void notificarBitacora(Proyecto proyecto) {
        try {
            Usuario supervisor = this.usuarioProyectoService.getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
            this.notificacionService.notificarRegistroBitacora(supervisor, proyecto);
            Usuario asesor = this.usuarioProyectoService.getAsesorProyecto(proyecto.getIdProyecto()).getIdUsuarioFk();
            this.notificacionService.notificarRegistroBitacora(asesor, proyecto);
            Usuario contraparte = this.usuarioProyectoService.getContraparteProyecto(proyecto.getIdProyecto()).getIdUsuarioFk();
            this.notificacionService.notificarRegistroBitacora(contraparte, proyecto);
        } catch (Exception e) {
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Bitacora actualizarBitacora(Integer idBitacora, BitacoraDto bitacoraDto) throws Exception {
        Bitacora bitacora = getBitacora(idBitacora);
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        if (!bitacora.getIdProyectoFk().getIdUsuarioFk().equals(usuario)) {
            throw new Exception("no tiene permiso para actualizar bitacora");
        }
        bitacora.setDescripcion(bitacoraDto.getDescripcion());
        bitacora.setAvance(bitacoraDto.getAvance());
        bitacora.setFechaReporteInicio(bitacoraDto.getFechaReporteInicioFormat());
        bitacora.setFechaReporteFin(bitacoraDto.getFechaReporteFinFormat());
        return this.bitacoraRepository.save(bitacora);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Bitacora revisarBitacora(Integer idBitacora) throws Exception {
        Bitacora bitacora = getBitacora(idBitacora);
        Proyecto proyecto = bitacora.getIdProyectoFk();
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Usuario supervisor = this.usuarioProyectoService.getSupervisorDisponible(proyecto.getIdCarreraFk().getIdCarrera());
        UsuarioProyecto asesor = this.usuarioProyectoService.getAsesorProyecto(proyecto.getIdProyecto());
        UsuarioProyecto contraparte = this.usuarioProyectoService.getContraparteProyecto(proyecto.getIdProyecto());
        if (supervisor.equals(usuario)) {
            bitacora.setRevisionSupervisor(Boolean.TRUE);
            return this.bitacoraRepository.save(bitacora);
        } else if (asesor.getIdUsuarioFk().equals(usuario)) {
            bitacora.setRevisionAsesor(Boolean.TRUE);
            return this.bitacoraRepository.save(bitacora);
        } else if (contraparte.getIdUsuarioFk().equals(usuario)) {
            bitacora.setRevisionContraparte(Boolean.TRUE);
            return this.bitacoraRepository.save(bitacora);
        } else {
            throw new Exception("No puede marcar bitacora como revisado");
        }
    }

    private String saveFile(MultipartFile file, Bitacora bitacora) throws Exception {
        String registro = bitacora.getIdProyectoFk().getIdUsuarioFk().getRegistroAcademico();
        return this.storageService.saveFile(file, registro);
    }

    @Override
    public void finalizarBitacora(Proyecto proyecto, MultipartFile finiquitoContraparte) throws Exception {
        Elemento elementoFiniquitoContraparte = this.elementoService.getElemento(ID_ELEMENTO_FINIQUITO_CONTRAPARTE);
        EtapaProyecto etapaProyecto = this.etapaService.getEtapaProyecto(
                proyecto.getIdProyecto(),
                EtapaUtils.ID_ETAPA_BITACORA);
        this.elementoService.crearElementoProyecto(proyecto,
                elementoFiniquitoContraparte,
                etapaProyecto, finiquitoContraparte);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Recurso crearRecursoBitacora(Integer idBitacora, RecursoDto recursoDto) throws Exception {
        Bitacora bitacora = this.getBitacora(idBitacora);
        Recurso recurso = new Recurso(recursoDto);
        if (recursoDto.getFile() != null) {
            procesarArchivo(recursoDto.getFile(), bitacora, recurso);
        } else if (recursoDto.getLink() != null && !recursoDto.getLink().isBlank()) {
            procesarLink(recursoDto.getLink(), recurso);
        } else {
            throw new Exception("No se proporcionó ni un archivo ni un enlace válido.");
        }
        recurso.setIdBitacoraFk(bitacora);
        return this.recursoRepository.save(recurso);
    }

    private void procesarArchivo(MultipartFile file, Bitacora bitacora, Recurso recurso) throws Exception {
        String contentType = file.getContentType();
        if (contentType != null) {
            if (contentType.startsWith("image")) {
                guardarRecurso("IMAGEN", file, bitacora, recurso);
            } else if (contentType.equalsIgnoreCase("application/pdf")) {
                if (recurso.getTipoRecurso().equals("INFORME MENSUAL")) {
                    guardarRecurso("INFORME MENSUAL", file, bitacora, recurso);
                    bitacora.setContieneInforme(Boolean.TRUE);
                    this.bitacoraRepository.save(bitacora);
                } else {
                    guardarRecurso("PDF", file, bitacora, recurso);
                }
            } else {
                throw new Exception("Formato de archivo no válido.");
            }
        } else {
            throw new Exception("Tipo de contenido del archivo no válido.");
        }
    }

    private void guardarRecurso(String tipoRecurso, MultipartFile file, Bitacora bitacora, Recurso recurso) throws Exception {
        String link = saveFile(file, bitacora);
        recurso.setLink(link);
        recurso.setDescripcion(file.getName());
        recurso.setTipoRecurso(tipoRecurso);
    }

    private void procesarLink(String link, Recurso recurso) {
        recurso.setLink(link);
        recurso.setTipoRecurso("LINK");
    }

    @Override
    public Bitacora getBitacora(Integer idBitacora) throws Exception {
        return this.bitacoraRepository.findById(idBitacora).get();
    }

    @Override
    public List<Recurso> getRecursosBitacora(Integer idBitacora) throws Exception {
        return this.recursoRepository.findRecursos(idBitacora);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Bitacora eliminarRecurso(Integer idRecurso) throws Exception {
        Recurso recurso = this.recursoRepository.findById(idRecurso).get();
        Bitacora bitacora = recurso.getIdBitacoraFk();
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        if (!bitacora.getIdProyectoFk().getIdUsuarioFk().equals(usuario)) {
            throw new Exception("No tiene permisos para eliminar recurso");
        }
        if (bitacora.isContieneInforme()) {
            bitacora.setContieneInforme(Boolean.FALSE);
            this.bitacoraRepository.save(bitacora);
        }
        this.recursoRepository.delete(recurso);
        return bitacora;
    }

}
