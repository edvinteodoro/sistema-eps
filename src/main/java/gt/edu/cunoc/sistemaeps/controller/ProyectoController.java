package gt.edu.cunoc.sistemaeps.controller;

import gt.edu.cunoc.sistemaeps.dto.ComentarioDto;
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
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;
    private final ElementoService elementoService;
    private final ComentarioService comentarioService;
    private final EtapaService etapaService;
    private final PersonaService personaService;
    private final InstitucionService institucionService;

    public ProyectoController(ProyectoService proyectoService, ElementoService elementoService,
            ComentarioService comentarioService, EtapaService etapaService,
            PersonaService personaService, InstitucionService institucionService) {
        this.proyectoService = proyectoService;
        this.elementoService = elementoService;
        this.comentarioService = comentarioService;
        this.etapaService = etapaService;
        this.personaService = personaService;
        this.institucionService = institucionService;
    }

    @GetMapping
    public ResponseEntity getProyectos(Pageable pageable) {
        try {
            Page<ProyectoDto> proyectos = this.proyectoService.getProyectos(pageable)
                    .map(ProyectoDto::new);
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

    @PostMapping("/{idProyecto}/elementos/{idElemento}/elemento-proyecto")
    public ResponseEntity crearElementoProyecto(@PathVariable Integer idProyecto,
            @PathVariable Integer idElemento, @ModelAttribute ElementoProyectoDto elementoProyectoDto) {
        try {
            ElementoProyectoDto elementoProyecto = new ElementoProyectoDto(this.elementoService
                    .crearElementoProyecto(idProyecto, idElemento, elementoProyectoDto));
            return ResponseEntity.ok(elementoProyecto);
        } catch (Exception e) {
            System.out.println("error: "+e.getMessage());
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
            System.out.println("error: "+e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    
    @PostMapping("/{idProyecto}/solicitar-cambios")
    public ResponseEntity solicitarRevision(@PathVariable Integer idProyecto,
            @RequestBody ComentarioDto comentarioDto) {
        try {
            this.proyectoService.solicitarCambios(idProyecto, comentarioDto);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            System.out.println("error: "+e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
