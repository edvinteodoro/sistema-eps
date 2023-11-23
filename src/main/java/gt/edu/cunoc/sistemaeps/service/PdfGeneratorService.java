package gt.edu.cunoc.sistemaeps.service;

import java.io.InputStream;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author edvin
 */
public interface PdfGeneratorService {

    public MultipartFile generatePdf(Map<String, String> fields, InputStream template, String nombreDocumento) throws Exception;
}
