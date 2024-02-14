package gt.edu.cunoc.sistemaeps.controller;

import gt.edu.cunoc.sistemaeps.dto.ActaDto;
import gt.edu.cunoc.sistemaeps.dto.BitacoraDto;
import gt.edu.cunoc.sistemaeps.dto.ComentarioDto;
import gt.edu.cunoc.sistemaeps.dto.ConvocatoriaDto;
import gt.edu.cunoc.sistemaeps.dto.ElementoProyectoDto;
import gt.edu.cunoc.sistemaeps.dto.EtapaProyectoDto;
import gt.edu.cunoc.sistemaeps.dto.InstitucionDto;
import gt.edu.cunoc.sistemaeps.dto.ProyectoDto;
import gt.edu.cunoc.sistemaeps.dto.UsuarioDto;
import gt.edu.cunoc.sistemaeps.service.ComentarioService;
import gt.edu.cunoc.sistemaeps.service.ElementoService;
import gt.edu.cunoc.sistemaeps.service.EtapaService;
import gt.edu.cunoc.sistemaeps.service.InstitucionService;
import gt.edu.cunoc.sistemaeps.service.PersonaService;
import gt.edu.cunoc.sistemaeps.service.ProyectoService;
import gt.edu.cunoc.sistemaeps.service.UsuarioProyectoService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;
    private final ElementoService elementoService;
    private final ComentarioService comentarioService;
    private final EtapaService etapaService;
    private final PersonaService personaService;
    private final InstitucionService institucionService;
    private final UsuarioProyectoService usuarioProyectoService;

    public ProyectoController(ProyectoService proyectoService, ElementoService elementoService,
            ComentarioService comentarioService, EtapaService etapaService,
            PersonaService personaService, InstitucionService institucionService,
            UsuarioProyectoService usuarioProyectoService) {
        this.proyectoService = proyectoService;
        this.elementoService = elementoService;
        this.comentarioService = comentarioService;
        this.etapaService = etapaService;
        this.personaService = personaService;
        this.institucionService = institucionService;
        this.usuarioProyectoService = usuarioProyectoService;
    }

    @GetMapping
    public ResponseEntity getProyectos(@RequestParam(required = false) Boolean activo,
            Pageable pageable) {
        try {
            pageable = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "idProyecto"));
            Page<ProyectoDto> proyectos = this.proyectoService.getProyectos(pageable)
                    .map(ProyectoDto::new);
            return ResponseEntity.ok(proyectos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/activos")
    public ResponseEntity getProyectosActivos() {
        try {
            List<ProyectoDto> proyectos = this.proyectoService.getProyectosActivos()
                    .stream().map(ProyectoDto::new).collect(Collectors.toList());
            return ResponseEntity.ok(proyectos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{idProyecto}")
    public ResponseEntity getProyecto(@PathVariable Integer idProyecto) {
        try {
            ProyectoDto proyecto = new ProyectoDto(this.proyectoService.getProyecto(idProyecto));
            return ResponseEntity.ok(proyecto);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity crearProyecto(@RequestBody ProyectoDto proyectoDto) {
        try {
            ProyectoDto proyecto = new ProyectoDto(this.proyectoService
                    .crearProyecto(proyectoDto));
            return ResponseEntity.ok(proyecto);
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{idProyecto}")
    public ResponseEntity actualizarProyecto(@PathVariable Integer idProyecto,
            @RequestBody ProyectoDto proyectoDto) {
        try {
            ProyectoDto proyecto = new ProyectoDto(this.proyectoService.actualizarProyecto(idProyecto, proyectoDto));
            return ResponseEntity.ok(proyecto);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/{idProyecto}/elementos/{idElemento}/elemento-proyecto")
    public ResponseEntity crearElementoProyecto(@PathVariable Integer idProyecto,
            @PathVariable Integer idElemento, @ModelAttribute ElementoProyectoDto elementoProyectoDto) {
        try {
            ElementoProyectoDto elementoProyecto = new ElementoProyectoDto(this.elementoService
                    .crearElementoProyecto(idProyecto, idElemento, elementoProyectoDto));
            return ResponseEntity.ok(elementoProyecto);
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{idProyecto}/elementos/{idElemento}/elemento-proyecto")
    public ResponseEntity getElementoProyecto(@PathVariable Integer idProyecto,
            @PathVariable Integer idElemento) {
        try {
            ElementoProyectoDto elementoProyecto = new ElementoProyectoDto(this.elementoService
                    .getElementoProyectoActivo(idProyecto, idElemento));
            return ResponseEntity.ok(elementoProyecto);
        } catch (Exception e) {
            System.out.println("error: " + e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{idProyecto}/comentarios")
    public ResponseEntity getComentariosProyecto(@PathVariable Integer idProyecto,
            Pageable pageable) {
        try {
            Page<ComentarioDto> comentarios = this.comentarioService.getComentarios(idProyecto, pageable)
                    .map(ComentarioDto::new);
            return ResponseEntity.ok(comentarios);
        } catch (Exception e) {
            System.out.println("error: " + e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{idProyecto}/etapa-proyecto-activa")
    public ResponseEntity getEtapaProyectoActiva(@PathVariable Integer idProyecto) {
        try {
            System.out.println("etapa");
            EtapaProyectoDto etapaProyecto = new EtapaProyectoDto(
                    this.etapaService.getEtapaProyectoActivo(idProyecto));
            return ResponseEntity.ok(etapaProyecto);
        } catch (Exception e) {
            System.out.println("error: " + e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{idProyecto}/personas/contraparte")
    public ResponseEntity getPersonaContraparte(@PathVariable Integer idProyecto) {
        try {
            UsuarioDto contraparte = new UsuarioDto(
                    this.personaService.getContraparte(idProyecto));
            return ResponseEntity.ok(contraparte);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{idProyecto}/usuarios/supervisor")
    public ResponseEntity getSupervisorProyecto(@PathVariable Integer idProyecto) {
        try {
            UsuarioDto supervisor = new UsuarioDto(
                    this.usuarioProyectoService.getSupervisorProyecto(idProyecto).getIdUsuarioFk());
            return ResponseEntity.ok(supervisor);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{idProyecto}/usuarios/supervisor")
    public ResponseEntity actualizarSupervisor(@PathVariable Integer idProyecto,
            @RequestBody UsuarioDto usuarioDto) {
        try {
            UsuarioDto usuario = new UsuarioDto(this.proyectoService.actualizarSupervisor(idProyecto, usuarioDto));
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{idProyecto}/usuarios/asesor")
    public ResponseEntity getAsesorProyecto(@PathVariable Integer idProyecto) {
        try {
            UsuarioDto asesor = new UsuarioDto(
                    this.usuarioProyectoService.getAsesorProyecto(idProyecto).getIdUsuarioFk());
            return ResponseEntity.ok(asesor);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{idProyecto}/usuarios/asesor")
    public ResponseEntity actualizarAsesor(@PathVariable Integer idProyecto,
            @RequestBody UsuarioDto usuarioDto) {
        try {
            UsuarioDto usuario = new UsuarioDto(this.proyectoService.actualizarAsesor(idProyecto, usuarioDto));
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{idProyecto}/usuarios/contraparte")
    public ResponseEntity actualizarContraparte(@PathVariable Integer idProyecto,
            @RequestBody UsuarioDto usuarioDto) {
        try {
            UsuarioDto usuario = new UsuarioDto(this.proyectoService.actualizarContraparte(idProyecto, usuarioDto));
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{idProyecto}/usuarios/coordinador-carrera")
    public ResponseEntity getCoordinadorCarreraProyecto(@PathVariable Integer idProyecto) {
        try {
            UsuarioDto coordinadorCarrera = new UsuarioDto(
                    this.usuarioProyectoService.getCoordinadoCarreraProyecto(idProyecto).getIdUsuarioFk());
            return ResponseEntity.ok(coordinadorCarrera);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{idProyecto}/usuarios/contraparte")
    public ResponseEntity getContraparteProyecto(@PathVariable Integer idProyecto) {
        try {
            UsuarioDto asesor = new UsuarioDto(
                    this.usuarioProyectoService.getContraparteProyecto(idProyecto).getIdUsuarioFk());
            return ResponseEntity.ok(asesor);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{idProyecto}/personas/asesor")
    public ResponseEntity getPersonaAsesor(@PathVariable Integer idProyecto) {
        try {
            UsuarioDto asesor = new UsuarioDto(
                    this.personaService.getAsesor(idProyecto));
            return ResponseEntity.ok(asesor);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{idProyecto}/personas/asesores-tecnicos")
    public ResponseEntity getPersonaAsesoresTecnicos(@PathVariable Integer idProyecto) {
        try {
            List<UsuarioDto> asespresTecnicos = this.personaService.getAsesoresTecnicos(idProyecto)
                    .stream().map(UsuarioDto::new).collect(Collectors.toList());
            return ResponseEntity.ok(asespresTecnicos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/{idProyecto}/personas/asesores-tecnicos")
    public ResponseEntity agregarAsesoresTecnicos(@PathVariable Integer idProyecto,
            @RequestBody UsuarioDto usuarioDto) {
        try {
            UsuarioDto asesorTecnico = new UsuarioDto(this.proyectoService.agregarAsesorTecnico(idProyecto, usuarioDto));
            return ResponseEntity.ok(asesorTecnico);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{idProyecto}/personas/{idPersona}")
    public ResponseEntity actualizarPersonaProyecto(@PathVariable Integer idProyecto,
            @PathVariable Integer idPersona, @RequestBody UsuarioDto usuarioDto) {
        try {
            UsuarioDto persona = new UsuarioDto(
                    this.personaService.actualizarPersona(idProyecto, idPersona, usuarioDto));
            return ResponseEntity.ok(persona);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{idProyecto}/personas/{idPersona}")
    public ResponseEntity eliminarPersonaProyecto(@PathVariable Integer idProyecto,
            @PathVariable Integer idPersona, @RequestBody UsuarioDto usuarioDto) {
        try {
            UsuarioDto persona = new UsuarioDto(
                    this.personaService.actualizarPersona(idProyecto, idPersona, usuarioDto));
            return ResponseEntity.ok(persona);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{idProyecto}/institucion")
    public ResponseEntity actualizarInstitucion(@PathVariable Integer idProyecto,
            @RequestBody InstitucionDto institucionDto) {
        try {
            InstitucionDto institucion = new InstitucionDto(
                    this.institucionService.actualizarInstitucion(idProyecto, institucionDto));
            return ResponseEntity.ok(institucion);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/{idProyecto}/comentarios")
    public ResponseEntity crearComentario(@PathVariable Integer idProyecto,
            @RequestBody ComentarioDto comentarioDto) {
        try {
            ComentarioDto comentario = new ComentarioDto(this.comentarioService.crearComentario(idProyecto, comentarioDto));
            return ResponseEntity.ok(comentario);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/{idProyecto}/solicitar-revision")
    public ResponseEntity solicitarRevision(@PathVariable Integer idProyecto) {
        try {
            this.proyectoService.solicitarRevision(idProyecto);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/{idProyecto}/solicitar-cambios")
    public ResponseEntity solicitarCambios(@PathVariable Integer idProyecto,
            @RequestBody ComentarioDto comentarioDto) {
        try {
            ComentarioDto comentario = new ComentarioDto(this.proyectoService.solicitarCambios(idProyecto, comentarioDto));
            return ResponseEntity.ok(comentario);
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/{idProyecto}/aprobar-cambios-secretaria")
    public ResponseEntity aprobarCambiosSecretaria(@PathVariable Integer idProyecto) {
        try {
            this.proyectoService.aprobarProyectoSecretaria(idProyecto);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            System.out.println("error: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/{idProyecto}/aprobar-cambios-supervisor")
    public ResponseEntity aprobarCambiosSupervisor(@PathVariable Integer idProyecto,
            @RequestBody UsuarioDto usuarioDto) {
        try {
            this.proyectoService.aprobarProyectoSupervisor(idProyecto, usuarioDto);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            System.out.println("error: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/{idProyecto}/convocatoria-anteproyecto")
    public ResponseEntity crearConvocatoriaAnteproyecto(@PathVariable Integer idProyecto,
            @RequestBody ConvocatoriaDto convocatoriaDto) {
        try {
            this.proyectoService.crearConvocatoriaAnteproyecto(idProyecto, convocatoriaDto);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            System.out.println("error:" + e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{idProyecto}/convocatoria-anteproyecto")
    public ResponseEntity getConvocatoriaAnteproyecto(@PathVariable Integer idProyecto) {
        try {
            ConvocatoriaDto convocatoria = new ConvocatoriaDto(
                    this.proyectoService.getConvocatoriaAnteproyecto(idProyecto));
            return ResponseEntity.ok(convocatoria);
        } catch (Exception e) {
            System.out.println("error:" + e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{idProyecto}/convocatoria-anteproyecto")
    public ResponseEntity actualizarConvocatoriaAnteproyecto(@PathVariable Integer idProyecto,
            @RequestBody ConvocatoriaDto convocatoriaDto) {
        try {
            this.proyectoService.actualizarConvocatoriaAnteproyecto(idProyecto, convocatoriaDto);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            System.out.println("error:" + e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    } 

    @PostMapping("/{idProyecto}/cargar-convocatoria-anteproyecto")
    public ResponseEntity cargarConvocatoriaAnteproyecto(@PathVariable Integer idProyecto,
            @ModelAttribute MultipartFile file) {
        try {
            this.proyectoService.cargarConvocatoria(idProyecto, file);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            System.out.println("error:" + e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    
    @PostMapping("/{idProyecto}/cargar-carta-aceptacion-contraparte")
    public ResponseEntity cargaCartaAceptacionContraparte(@PathVariable Integer idProyecto,
            @ModelAttribute MultipartFile file) {
        try {
            this.proyectoService.cargarCartaAceptacionContraparte(idProyecto, file);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            System.out.println("error:" + e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/{idProyecto}/acta-anteproyecto")
    public ResponseEntity crearActaAnteproyecto(@PathVariable Integer idProyecto,
            @RequestBody ActaDto actaDto) {
        try {
            System.out.println("acta");
            this.proyectoService.crearActaAnteproyecto(idProyecto, actaDto);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            System.out.println("error:" + e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{idProyecto}/acta-anteproyecto")
    public ResponseEntity getActaAnteproyecto(@PathVariable Integer idProyecto) {
        try {
            ActaDto acta = new ActaDto(
                    this.proyectoService.getActaAnteproyecto(idProyecto));
            return ResponseEntity.ok(acta);
        } catch (Exception e) {
            System.out.println("error:" + e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/{idProyecto}/generar-acta-anteproyecto")
    public ResponseEntity generarActaAnteproyecto(@PathVariable Integer idProyecto,
            @RequestBody ActaDto actaDto) {
        try {
            ActaDto acta = new ActaDto(this.proyectoService.generarActaAnteproyecto(idProyecto, actaDto));
            return ResponseEntity.ok(acta);
        } catch (Exception e) {
            System.out.println("error:" + e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/{idProyecto}/habilitar-bitacora")
    public ResponseEntity habilitarBitacora(@PathVariable Integer idProyecto,
            @RequestBody UsuarioDto usuarioDto) {
        try {
            this.proyectoService.habilitarBitacora(idProyecto, usuarioDto);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            System.out.println("error:" + e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/{idProyecto}/finalizar-bitacora")
    public ResponseEntity finalizarBitacora(@PathVariable Integer idProyecto,
            @ModelAttribute MultipartFile cartaAsesor,
            @ModelAttribute MultipartFile finiquitoContraparte,
            @ModelAttribute MultipartFile informeFinal) {
        try {
            this.proyectoService.finalizarBitacora(idProyecto, cartaAsesor, finiquitoContraparte, informeFinal);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            System.out.println("error:" + e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/{idProyecto}/bitacoras")
    public ResponseEntity agregarBitacora(@PathVariable Integer idProyecto,
            @RequestBody BitacoraDto bitacoraDto) {
        try {
            BitacoraDto bitacora = new BitacoraDto(this.proyectoService.crearBitacora(idProyecto, bitacoraDto));
            return ResponseEntity.ok(bitacora);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
