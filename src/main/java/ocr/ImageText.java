package ocr;

import java.io.File;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class ImageText extends Tesseract {

    private String imagePath;

    public ImageText(String imagePath) {
        this.imagePath = imagePath;
    }

    String generateText() {
        
        this.setTessVariable("user_defined_dpi", "300");
        this.setDatapath(System.getenv("TESSDATA_PREFIX"));
        this.setLanguage("ara+eng");//set the English and Arabic language

        String fullText = null;
        try {
            fullText = this.doOCR(new File(imagePath));
        } catch (TesseractException e) {
            System.err.println("TesseractException:" + e.getMessage());
        }
        return fullText;
    }

}
