package ocr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.ProcessStarter;

import com.recognition.software.jdeskew.ImageDeskew;

import net.sourceforge.tess4j.util.ImageHelper;

public class ImageProcess {

	public static final String IMAGE_MAGICK_PATH;
	public static final double MINIMUM_DESKEW_THRESHOLD = 0.05d;
	private String imagePath;
	
	static {
		if (Utils.isWindows()){
			IMAGE_MAGICK_PATH = "D:\\ImageMagick-7.1.0-Q16-HDRI";
		} else {
			IMAGE_MAGICK_PATH = "/usr/bin/";
		}	
	}
	
	// Windows
	//public static final String IMAGE_MAGICK_PATH = "D:\\ImageMagick-7.1.0-Q16-HDRI";
	// Linux
	//public static final String IMAGE_MAGICK_PATH = "/usr/bin/";

	
	
	public ImageProcess(String imagePath) {
		this.imagePath = imagePath;
		
	}
	
	/**
	 * Straightening a rotated image.
	 * @param inputImgPath
	 * @return Output image file path
	 * @throws IOException
	 */
  public String deskewImage(String inputImgPath) throws IOException {
	    BufferedImage bi = ImageIO.read( new File(inputImgPath));
	    ImageDeskew id = new ImageDeskew(bi);
	    double imageSkewAngle = id.getSkewAngle(); // determine skew angle
	    if ((imageSkewAngle > MINIMUM_DESKEW_THRESHOLD || imageSkewAngle < -(MINIMUM_DESKEW_THRESHOLD))) {
	        bi = ImageHelper.rotateImage(bi, -imageSkewAngle); // deskew image
	    }
	    String straightenImgPath = "./deskewImage.png";	   
	    ImageIO.write(bi, "png", new File(straightenImgPath));
        
	    return straightenImgPath;
	}
  
  
  /**
	 * Get rid of a black border around image.
	 * @param inputImage
	 * @return Output image file path
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IM4JavaException
	 */
  
  public String removeBorder(String inputImage) throws IOException, InterruptedException, IM4JavaException {
	  ProcessStarter.setGlobalSearchPath(IMAGE_MAGICK_PATH);
	  IMOperation op = new IMOperation();
	  op.addImage();
	  op.density(300);
	  op.bordercolor("black").border(1).fuzz (0.95).fill("white").draw("color 0,0 floodfill");
	  op.addImage();
	  ConvertCmd cmd = new ConvertCmd();
      BufferedImage image =  ImageIO.read(new File(inputImage));
      String outFile = "./borderRemoved.png";
      ImageIO.write(image, "png", new File(outFile));
      cmd.run(op,inputImage,outFile);
	  return outFile;
  }
  
 /**
  * In this step we make the text white and background black.
  * monochrome: converts a multicolored image (RGB), to a black and white image.
  * negate: Replace each pixel with its complementary color (White becomes black).
  * Use .fill white .fuzz 11% p_opaque "#000000" to fill the text with white (so we can see most 
  * of the original image)
  * Apply a light .blur (1d,1d) to the image.
  * @param deSkew
  * @return Output image file path
  * @throws IOException
  * @throws InterruptedException
  * @throws IM4JavaException
  */
	public String binaryInverse(String deskew) throws IOException, InterruptedException, IM4JavaException {
        ProcessStarter.setGlobalSearchPath(IMAGE_MAGICK_PATH);
	      // create the operation, add images and operators/options
	      IMOperation op = new IMOperation();
	      op.addImage();
	      op.density(300);
	      op.format("png").monochrome().negate().fill("white").fuzz(0.11).p_opaque("#000000").blur(1d,1d);
	      op.addImage();
	    
	      // execute the operation
	      ConvertCmd cmd = new ConvertCmd();
	      BufferedImage img =  ImageIO.read(new File(deskew));
	      String outfile = "./binaryInverseImg.png";
	      ImageIO.write(img, "png", new File(outfile));
        cmd.run(op,img,outfile);
        
        return outfile;
       
	}
   
 /**
  * In this step every thing in black becoming transparent.
  * we simply combine the original image with binaryInverseImg (the black and white version). 
  * @param OriginalImgPath
  * @param nbackgroundImgPath
  * @return Output image file path
  * @throws IOException
  * @throws InterruptedException
  * @throws IM4JavaException
  */
  
      public String imageTransparent(String originalImgPath, String nbackgroundImgPath) 
    		  throws IOException, InterruptedException, IM4JavaException {
    	  ProcessStarter.setGlobalSearchPath(IMAGE_MAGICK_PATH);
    	  IMOperation op = new IMOperation(); 
	      op.addImage();
	      op.density(300);
	      op.addImage();
	      op.density(300);
	      op.alpha("off").compose("copy_opacity").composite();
	      op.addImage();
	      ConvertCmd cmd = new ConvertCmd();
	      BufferedImage IMG1 =  ImageIO.read(new File(originalImgPath));
	      BufferedImage IMG2 =  ImageIO.read(new File(nbackgroundImgPath));
	      String outputFile = "./transparentImg.png";
	      ImageIO.write(IMG1,"png", new File(outputFile));
	      ImageIO.write(IMG2,"png", new File(outputFile));
	      cmd.run(op,originalImgPath,nbackgroundImgPath,outputFile);
		  
		return outputFile;
    	  
      }
      
      
    public static String ImgAfterDeskewingWithoutBorder(String imagePath) 
     		     throws IOException, InterruptedException, IM4JavaException {
    	
    	ImageProcess image = new ImageProcess(imagePath );
		String imageDeskew = image.deskewImage(imagePath);
		String imageNBorder = image.removeBorder(imageDeskew);
		
		return imageNBorder;	
    }
    public static String ImgAfterRemovingBackground(String imagePath) throws IOException, InterruptedException, IM4JavaException {
    	
    	ImageProcess image = new ImageProcess(imagePath );
    	String imageNBorder = ImageProcess.ImgAfterDeskewingWithoutBorder(imagePath);
    	String binaryInv = image.binaryInverse(imageNBorder);
		String finalImage = image.imageTransparent(imageNBorder,binaryInv);
		
		return finalImage;
    }
}
