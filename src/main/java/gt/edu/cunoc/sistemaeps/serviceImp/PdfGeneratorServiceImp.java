package gt.edu.cunoc.sistemaeps.serviceImp;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import gt.edu.cunoc.sistemaeps.service.PdfGeneratorService;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author edvin
 */
@Service
public class PdfGeneratorServiceImp implements PdfGeneratorService {
    
    @Override
    public MultipartFile generatePdf(Map<String, String> campos, InputStream template, String nombreDocumento) throws Exception {
        XWPFDocument document = new XWPFDocument(template);
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                String text = run.getText(0);
                if (text != null) {
                    for (String key : campos.keySet()) {
                        if (text.contains(key)) {
                            text = text.replace(key, campos.get(key).toString());
                        }
                    }
                    run.setText(text, 0);
                }
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document.write(baos);
        return convertirDocumento(document,nombreDocumento);
    }

    private MultipartFile convertirDocumento(XWPFDocument document,String nombreDocumento) throws Exception {
        PdfOptions options = PdfOptions.create();

        // Convert DOC to PDF
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfConverter.getInstance().convert(document, baos, options);

        // Construct a MultipartFile from the generated PDF content
        MultipartFile pdfFile = new MockMultipartFile(nombreDocumento, nombreDocumento, "application/pdf", baos.toByteArray());

        document.close();
        baos.close();

        return pdfFile;
    }
    
    
}
