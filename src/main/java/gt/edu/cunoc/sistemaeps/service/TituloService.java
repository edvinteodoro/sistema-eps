/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.service;

import gt.edu.cunoc.sistemaeps.entity.Titulo;
import java.util.List;

/**
 *
 * @author edvin
 */
public interface TituloService {
    public Titulo getTitulo(Integer idTitulo);
    public List<Titulo> getTitulos();
}
