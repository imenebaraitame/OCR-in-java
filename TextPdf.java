package ocr;

import java.io.FileNotFoundException;
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
	String path3;
        public TextPdf(String fulltext, String path3) {
	          this.fulltext = fulltext;
	          this.path3 = path3;
	        }

	    void document() throws FileNotFoundException, DocumentException {
	    	Document document = new Document(PageSize.LETTER);
	    	//2)Get a PdfWriter instance
	    	PdfWriter.getInstance(document, new FileOutputStream(path3));
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
