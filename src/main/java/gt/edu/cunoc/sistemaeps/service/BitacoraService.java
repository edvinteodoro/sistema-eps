/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.service;

import gt.edu.cunoc.sistemaeps.entity.Bitacora;
import gt.edu.cunoc.sistemaeps.entity.Recurso;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author edvin
 */
public interface BitacoraService {
    public Page<Bitacora> getBitacoras(Pageable pageable) throws Exception;
    public Bitacora getBitacora(Integer idBitacora) throws Exception;
    public List<Recurso> getRecursosBitacora(Integer idBitacora) throws Exception;
}
