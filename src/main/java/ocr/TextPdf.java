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
	String fulltext;
	String docPath;
        public TextPdf(String fulltext, String docPath) {
	          this.fulltext = fulltext;
	          this.docPath = docPath;
	        }

	    void MakeDocument() throws FileNotFoundException, DocumentException {
	    	Document document = new Document(PageSize.LETTER);
	    	//2)Get a PdfWriter instance
	    	FileOutputStream fos = new FileOutputStream(docPath);
	    	System.out.println("File will be created at: " + new File(docPath).getPath());
	    	PdfWriter.getInstance(document, fos);
	    	//3)Open the Document
	    	document.open();
	    	//4)Add content
	    	Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
	    	Paragraph paragraph = new Paragraph(fulltext, font);
	    	document.add(paragraph);
	    	//5)close the document
	    	document.close();
	    }
		
}
