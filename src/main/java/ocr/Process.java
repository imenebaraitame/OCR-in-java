package ocr;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Process extends Image {

	Process(String imagePath1, BufferedImage myPicture) {
		super(imagePath1, myPicture);
	}
	int width = myPicture.getWidth();
    int height = myPicture.getHeight();

   public  BufferedImage bufferedImage () throws Exception {
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
             String imagePath2 = Process.class.getResource("/images/bufferedImage.jpg").getPath();
             ImageIO.write(bufferedImage,"jpg", new File(imagePath2));
			return bufferedImage;


       }

}
