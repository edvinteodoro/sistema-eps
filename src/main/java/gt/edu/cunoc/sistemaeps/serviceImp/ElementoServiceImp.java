package gt.edu.cunoc.sistemaeps.serviceImp;

import gt.edu.cunoc.sistemaeps.dto.ElementoProyectoDto;
import gt.edu.cunoc.sistemaeps.entity.Elemento;
import gt.edu.cunoc.sistemaeps.entity.ElementoProyecto;
import gt.edu.cunoc.sistemaeps.entity.EtapaProyecto;
import gt.edu.cunoc.sistemaeps.entity.Proyecto;
import gt.edu.cunoc.sistemaeps.entity.Usuario;
import gt.edu.cunoc.sistemaeps.repository.ElementoProyectoRepository;
import gt.edu.cunoc.sistemaeps.repository.ElementoRepository;
import gt.edu.cunoc.sistemaeps.service.ElementoService;
import gt.edu.cunoc.sistemaeps.service.EtapaService;
import gt.edu.cunoc.sistemaeps.service.StorageService;
import gt.edu.cunoc.sistemaeps.service.UsuarioService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author edvin
 */
@Service
public class ElementoServiceImp implements ElementoService {

    private final String PDF = "PDF";
    private final String TEXT = "TEXT";

    private final ElementoRepository elementoRepository;
    private final ElementoProyectoRepository elementoProyectoRepository;
    private final StorageService storageService;
    private final EtapaService etapaService;
    private final UsuarioService usuarioService;

    public ElementoServiceImp(ElementoRepository elementoRepository,
            ElementoProyectoRepository elementoProyectoRepository, StorageService storageService,
            EtapaService etapaService, UsuarioService usuarioService) {
        this.elementoRepository = elementoRepository;
        this.elementoProyectoRepository = elementoProyectoRepository;
        this.storageService = storageService;
        this.etapaService = etapaService;
        this.usuarioService = usuarioService;
    }

    @Override
    public Elemento getElemento(Integer idElemento) {
        return this.elementoRepository.findById(idElemento).get();
    }

    @Override
    public List<Elemento> getElementos() {
        return this.elementoRepository.findAll();
    }

    @Override
    public ElementoProyecto getElementoProyectoActivo(Integer idProyecto, Integer idElemento) throws Exception {
        return this.elementoProyectoRepository.findElementoProyecto(idProyecto, idElemento, Boolean.TRUE);
    }

    @Override
    public ElementoProyecto saveElementoProyecto(ElementoProyecto elementoProyecto) throws Exception {
        return this.elementoProyectoRepository.save(elementoProyecto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ElementoProyecto crearElementoProyecto(Integer idProyecto, Integer idElemento,
            ElementoProyectoDto elementoProyectoDto) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        Elemento elemento = getElemento(idElemento);
        EtapaProyecto etapaProyecto = this.etapaService.getEtapaProyecto(idProyecto, elemento.getIdEtapaFk().getIdEtapa());
        ElementoProyecto elementoProyectoNuevo = new ElementoProyecto();
        if (elemento.getTipo().equals(PDF)) {
            String key = this.storageService.saveFile(elementoProyectoDto.getFile(), usuario.getRegistroAcademico());
            elementoProyectoNuevo.setInformacion(key);
        } else {
            elementoProyectoNuevo.setInformacion(elementoProyectoDto.getInformacion());
        }
        desactivarElementoProyectoActivo(idProyecto, idElemento);
        elementoProyectoNuevo.setIdElementoFk(elemento);
        elementoProyectoNuevo.setIdEtapaProyectoFk(etapaProyecto);
        return saveElementoProyecto(elementoProyectoNuevo);
    }

    private void desactivarElementoProyectoActivo(Integer idProyecto, Integer idElemento) throws Exception {
        ElementoProyecto elementoProyectoActivo = getElementoProyectoActivo(idProyecto, idElemento);
        if (elementoProyectoActivo != null) {
            elementoProyectoActivo.setActivo(Boolean.FALSE);
            saveElementoProyecto(elementoProyectoActivo);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void desactivarElementoProyectoActivo(Integer idElementoProyecto) throws Exception {
        Usuario usuario = this.usuarioService.getLoggedUsuario();
        ElementoProyecto elementoProyectoActivo = this.elementoProyectoRepository.findById(idElementoProyecto).get();
        Proyecto proyecto = elementoProyectoActivo.getIdEtapaProyectoFk().getIdProyectoFk();
        if (!proyecto.getIdUsuarioFk().equals(usuario)) {
            throw new Exception("no tiene permiso para desactivar elemento");
        }
        elementoProyectoActivo.setActivo(Boolean.FALSE);
        saveElementoProyecto(elementoProyectoActivo);
    }

    public ElementoProyecto crearElementoProyecto(Proyecto proyecto, Elemento elemento,
            EtapaProyecto etapaProyecto, MultipartFile file) throws Exception {
        System.out.println("elemento id: "+elemento.getIdEtapaFk().getIdEtapa());
        System.out.println("etapa proyecto: "+etapaProyecto.getIdEtapaFk().getIdEtapa());
        if (!elemento.getIdEtapaFk().equals(etapaProyecto.getIdEtapaFk())) {
            throw new Exception("No se puede crear elemento");
        }
        desactivarElementoProyectoActivo(proyecto.getIdProyecto(), elemento.getIdElemento());
        ElementoProyecto elementoProyectoNuevo = new ElementoProyecto();
        String key = this.storageService.saveFile(file, proyecto.getIdUsuarioFk().getRegistroAcademico());
        elementoProyectoNuevo.setInformacion(key);
        elementoProyectoNuevo.setIdElementoFk(elemento);
        elementoProyectoNuevo.setIdEtapaProyectoFk(etapaProyecto);
        return saveElementoProyecto(elementoProyectoNuevo);
    }

}
