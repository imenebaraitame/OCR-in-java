package ocr;

import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileOutputStream;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class TextPdf {

    private final String fullText;
    private final String docPath;

    public TextPdf(String fullText, String docPath) {
        this.fullText = fullText;
        this.docPath = docPath;
    }

    void generateDocument() throws FileNotFoundException, DocumentException {
        Document document = new Document(PageSize.LETTER);
        //2) Get a PdfWriter instance
        FileOutputStream fos = new FileOutputStream(this.docPath);
        System.out.println("File will be created at: " + new File(this.docPath).getPath());
        PdfWriter.getInstance(document, fos);
        //3) Open the Document
        document.open();
        //4) Add content
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Paragraph paragraph = new Paragraph(fullText, font);
        document.add(paragraph);
        //5) Close the document
        document.close();
    }

}
