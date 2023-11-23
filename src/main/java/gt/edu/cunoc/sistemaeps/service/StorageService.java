/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gt.edu.cunoc.sistemaeps.service;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author edvin
 */
public interface StorageService {
    public String saveFile(MultipartFile file, String path) throws Exception;
    public String getFile(String key) throws Exception;
}
