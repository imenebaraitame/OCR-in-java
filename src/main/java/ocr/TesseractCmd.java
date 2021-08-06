package ocr;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.ITesseract.RenderedFormat;
import net.sourceforge.tess4j.TesseractException;

public class TesseractCmd {

    private String inputFile;
    private String outputFile;
    private String configFileValue;

    public TesseractCmd(String inputFile, String outputFile, String configFileValue) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.configFileValue = configFileValue;
    }

    public void textOnlyPdf(String imagePath) {
        List<RenderedFormat> formats = new ArrayList<RenderedFormat>(Arrays.asList(RenderedFormat.PDF));
        try {
            Tesseract instance = new Tesseract();
            //mode 6: Assume a single uniform block of text.
            instance.setPageSegMode(6);
            instance.setTessVariable("user_defined_dpi", "300");
            instance.setDatapath(System.getenv("TESSDATA_PREFIX"));
            instance.setLanguage("eng");//set the Eng language
            instance.setTessVariable("textonly_pdf", configFileValue);//0->make the image visible,1->make the image invisible
            instance.createDocuments(new String[]{imagePath}, new String[]{outputFile}, formats);
        } catch (TesseractException te) {
            System.err.println("TesseractException: " + te.getMessage());
        }
    }

}
