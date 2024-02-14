package gt.edu.cunoc.sistemaeps.serviceImp;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import gt.edu.cunoc.sistemaeps.service.PdfGeneratorService;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
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

        replaceTextInParagraphs(document, campos);
        replaceTextInTableCells(document, campos);

        //ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //document.write(baos);
        //MultipartFile pdfFile = new MockMultipartFile(nombreDocumento, nombreDocumento, "vnd.openxmlformats-officedocument.wordprocessingml.document", baos.toByteArray());
        return convertirDocumento(document, nombreDocumento);
    }

    private void replaceTextInParagraphs(XWPFDocument document, Map<String, String> campos) {
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            replaceTextInRuns(paragraph.getRuns(), campos);
        }
    }

    private void replaceTextInTableCells(XWPFDocument document, Map<String, String> campos) {
        for (XWPFTable table : document.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        replaceTextInRuns(paragraph.getRuns(), campos);
                    }
                }
            }
        }
    }

    private void replaceTextInRuns(List<XWPFRun> runs, Map<String, String> campos) {
        for (XWPFRun run : runs) {
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

    private MultipartFile convertirDocumento(XWPFDocument document, String nombreDocumento) throws Exception {
        
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
   
    

    /*
    
    private MockMultipartFile convertirDocumento(XWPFDocument document, String nombreDocumento) throws Exception {
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        Document pdfDocument = new Document();
        PdfWriter.getInstance(pdfDocument, pdfOutputStream);
        pdfDocument.open();

        for (XWPFParagraph paragraph : document.getParagraphs()) {
            pdfDocument.add(new Paragraph(paragraph.getText()));
        }

        pdfDocument.close();

        // Convert the ByteArrayOutputStream to a byte array
        byte[] bytes = pdfOutputStream.toByteArray();

        // Create a MockMultipartFile with the byte array
        return new MockMultipartFile(nombreDocumento,nombreDocumento,"application/pdf", bytes);

    }
    
    private static void convertDocxToPdf(String docxFilePath, String pdfFilePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(docxFilePath);
             XWPFDocument document = new XWPFDocument(fis);
             PDDocument pdfDocument = new PDDocument();
             FileOutputStream fos = new FileOutputStream(pdfFilePath)) {

            PDPage page = new PDPage();
            pdfDocument.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page);

            for (XWPFTable table : document.getTables()) {
                processTable(contentStream, table);
            }

            contentStream.close();
            pdfDocument.save(fos);
        }
    }
    
    private static void convertDocxToPdf(String docxFilePath, String pdfFilePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(docxFilePath);
             XWPFDocument document = new XWPFDocument(fis);
             PDDocument pdfDocument = new PDDocument();
             FileOutputStream fos = new FileOutputStream(pdfFilePath)) {

            PDPage page = new PDPage();
            pdfDocument.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page);

            for (XWPFParagraph paragraph : document.getParagraphs()) {
                String text = paragraph.getText();
                contentStream.beginText();
                contentStream.newLineAtOffset(50, PDRectangle.A4.getHeight() - 50);
                contentStream.showText(text);
                contentStream.endText();
                contentStream.newLineAtOffset(0, -15);
            }

            contentStream.close();
            pdfDocument.save(fos);
        }
    }*/
}
