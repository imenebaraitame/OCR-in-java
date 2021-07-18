package ocr;

import java.awt.Color;
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

public class ImgProcess {

	public static final String IMAGE_MAGICK_PATH;
	static {
		if (Utils.isLinux()){
			IMAGE_MAGICK_PATH = "D:\\ImageMagick-7.1.0-Q16-HDRI";
		} else {
			IMAGE_MAGICK_PATH = "/usr/bin/";
		}	
	}
	
	// Windows
	//public static final String IMAGE_MAGICK_PATH = "D:\\ImageMagick-7.1.0-Q16-HDRI";
	// Linux
	//public static final String IMAGE_MAGICK_PATH = "/usr/bin/";

	private String imagePath1;
	
	public ImgProcess(String imagePath1) {
		this.imagePath1 = imagePath1;
		
	}
	
//Straightening a rotated image.
	
  public String deskewImage(String imagePath1) throws IOException {
	    BufferedImage bi = ImageIO.read( new File(imagePath1));
	    ImageDeskew id = new ImageDeskew(bi);
	   
	    final double MINIMUM_DESKEW_THRESHOLD = 0.05d;
	  
	    double imageSkewAngle = id.getSkewAngle(); // determine skew angle
	    //System.out.println(imageSkewAngle);
	    if ((imageSkewAngle > MINIMUM_DESKEW_THRESHOLD || imageSkewAngle < -(MINIMUM_DESKEW_THRESHOLD))) {
	        bi = ImageHelper.rotateImage(bi, -imageSkewAngle); // deskew image
	    }
	    String straightenImgPath = "./deskewImage.jpg";	   
	    ImageIO.write(bi, "jpg", new File(straightenImgPath));
        
	    return straightenImgPath;
	}
  
//Image magick manipulation: increase contrast and density, 
//                           get rid of a black border around the image.
  
	public String magickManipulation(String deskew) throws IOException, InterruptedException, IM4JavaException {
          ProcessStarter.setGlobalSearchPath(IMAGE_MAGICK_PATH);
	      // create the operation, add images and operators/options
	      IMOperation op = new IMOperation();
	      op.addImage();
	      op.density(300);
	      op.brightnessContrast(5d, 25d).sharpen(5d, 5d);
	      op.bordercolor("black").border(1).fuzz (0.95).fill("white").draw("color 0,0 floodfill");
	      op.addImage();
	      // execute the operation
	      ConvertCmd cmd = new ConvertCmd();
	      BufferedImage img =  ImageIO.read(new File(deskew));
	      String outfile = "./magickManipulation.jpg";
	      ImageIO.write(img, "jpg", new File(outfile));
          cmd.run(op,img,outfile);
          
          return outfile;
         
	}
	    
//Binaries Image: This step converts a multicolored image (RGB) 
//                to a black and white image (monochrome image).	
	

   public  String bufferedImage (String imagePath1) throws Exception {
	   BufferedImage myPicture = ImageIO.read( new File(imagePath1));
	       int width = myPicture.getWidth();
	       int height = myPicture.getHeight();
   BufferedImage bufferedImage = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
             for (int y = 0; y < height; y++)
                 {
                     for (int x = 0; x < width; x++)
                         {
    	                  //Get RGB Value
                          int val = myPicture.getRGB(x, y);
                          //Source in the format:0xRRGGBB
                          //Convert to three separate channels
                          Color col = new Color(val, true);
                          int r =col.getRed(); //or (0x00ff0000 & val) >> 16;  ____RR___
                          int g =col.getGreen(); //or (0x0000ff00 & val) >> 8; ______GG__
                          int b =col.getBlue(); //or (0x000000ff & val);       ________BB
                          int m=(r+g+b);
                         //(255+255+255)/2 =383.5 middle of dark and light
                          if(m>=383)
                           {
                            // for light color it set white
                               bufferedImage.setRGB(x, y,Color.WHITE.getRGB());
                           }
                           else{
                            // for dark color it will set black
                               bufferedImage.setRGB(x, y, 0);
                           }
                         }
                 }
             String imagePath2 ="./bufferedImage.jpg";
             ImageIO.write(bufferedImage,"jpg", new File(imagePath2));
			return imagePath2;


       }
}
