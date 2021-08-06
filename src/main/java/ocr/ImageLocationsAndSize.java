package ocr;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import org.apache.pdfbox.contentstream.operator.DrawObject;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.contentstream.PDFStreamEngine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.state.Concatenate;
import org.apache.pdfbox.contentstream.operator.state.Restore;
import org.apache.pdfbox.contentstream.operator.state.Save;
import org.apache.pdfbox.contentstream.operator.state.SetGraphicsStateParameters;
import org.apache.pdfbox.contentstream.operator.state.SetMatrix;

public class ImageLocationsAndSize extends PDFStreamEngine {

    private float imageYPosition, imageXPosition, imageXScale, imageYScale;

    /**
     * @throws IOException If there is an error loading text stripper properties.
     */
    public ImageLocationsAndSize() throws IOException {
        // preparing PDFStreamEngine
        addOperator(new Concatenate());
        addOperator(new DrawObject());
        addOperator(new SetGraphicsStateParameters());
        addOperator(new Save());
        addOperator(new Restore());
        addOperator(new SetMatrix());
    }

    public void processOperator(Operator operator, List<COSBase> operands) throws IOException {
        String operation = operator.getName();
        if ("Do".equals(operation)) {
            COSName objectName = (COSName) operands.get(0);
            // get the PDF object
            PDXObject xObject = getResources().getXObject(objectName);
            // check if the object is an image object
            if (xObject instanceof PDImageXObject) {
                Matrix ctmNew = getGraphicsState().getCurrentTransformationMatrix();
                // displayed size in user space units
                this.imageXScale = ctmNew.getScalingFactorX();
                this.imageYScale = ctmNew.getScalingFactorY();
                // position of image in the pdf in terms of user space units
                this.imageXPosition = ctmNew.getTranslateX();
                this.imageYPosition = ctmNew.getTranslateY();
            } else if (xObject instanceof PDFormXObject) {
                PDFormXObject form = (PDFormXObject) xObject;
                showForm(form);
            }
        } else {
            super.processOperator(operator, operands);
        }
    }

    /**
     * Place image on existing pdf.
     * @param inputFilePath
     * @param outputFilePath
     * @param imgPath
     * @throws DocumentException
     * @throws IOException
     */
    public void merge(String inputFilePath, String outputFilePath, String imgPath) throws DocumentException, IOException {
        OutputStream file = new FileOutputStream(outputFilePath);
        PdfReader pdfReader = new PdfReader(inputFilePath);
        PdfStamper pdfStamper = new PdfStamper(pdfReader, file);
        //Image to be added in existing pdf file.
        Image Img = Image.getInstance(imgPath);
        //Scale image's width and height
        Img.scaleAbsolute(this.imageXScale, this.imageYScale);
        //Set position for image in PDF
        Img.setAbsolutePosition(this.imageXPosition, this.imageYPosition);

        // loop on all the PDF pages (i is the pdfPageNumber)
        int nbrOfPages = pdfReader.getNumberOfPages();
        for (int i = 1; i <= nbrOfPages; i++) {
            //getOverContent() allows you to content on top of existing pdf
            PdfContentByte pdfContentByte = pdfStamper.getOverContent(i);
            pdfContentByte.addImage(Img);
        }
        System.out.println("Document will be created at: " + new File(outputFilePath).getPath());
        pdfStamper.close();
    }

}

