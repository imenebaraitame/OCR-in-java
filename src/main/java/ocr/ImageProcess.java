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
	public static final double MINIMUM_DESKEW_THRESHOLD = 0.05d;

    private String imagePath;

    static {
        if (Utils.isWindows()) {
            IMAGE_MAGICK_PATH = "D:\\ImageMagick-7.1.0-Q16-HDRI";
        } else {
            IMAGE_MAGICK_PATH = "/usr/bin/";
        }
    }

    // Windows
    //public static final String IMAGE_MAGICK_PATH = "D:\\ImageMagick-7.1.0-Q16-HDRI";
    // Linux
    //public static final String IMAGE_MAGICK_PATH = "/usr/bin/";

    public ImgProcess(String imagePath) {
        this.imagePath = imagePath;
    }

	/**
	 * Straightening a rotated image.
	 * @param inputImgPath
	 * @return Output image file path
	 * @throws IOException
	 */
	public String deskewImage(String inputImgPath) throws IOException {
        BufferedImage bi = ImageIO.read(new File(inputImgPath));
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
        op.bordercolor("black").border(1).fuzz(0.95).fill("white").draw("color 0,0 floodfill");
        op.addImage();
        ConvertCmd cmd = new ConvertCmd();
        BufferedImage image = ImageIO.read(new File(inputImage));
        String outFile = "./removeBorder.png";
        ImageIO.write(image, "png", new File(outFile));
        cmd.run(op, inputImage, outFile);
        return outFile;
    }

	/**
	 * negate: Replace each pixel with its complementary color (White becomes black).
	 * Use .fill white .fuzz 11% p_opaque "#000000" to fill the text with white (so we can see most of the original image)
	 * Apply a light .blur (0d,1d) to the image.
	 * @param deSkew
	 * @return Output image file path
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IM4JavaException
	 */
    public String magickManipulation(String deSkew) throws IOException, InterruptedException, IM4JavaException {
        ProcessStarter.setGlobalSearchPath(IMAGE_MAGICK_PATH);
        // create the operation, add images and operators/options
        IMOperation op = new IMOperation();
        op.addImage();
        op.density(300);
        op.format("png").negate().fill("white").fuzz(0.11).p_opaque("#000000").blur(1d, 1d);
        op.addImage();

        // execute the operation
        ConvertCmd cmd = new ConvertCmd();
        BufferedImage img = ImageIO.read(new File(deSkew));
        String outfile = "./magickManipulation.png";
        ImageIO.write(img, "png", new File(outfile));
        cmd.run(op, img, outfile);
        return outfile;
    }

	/**
	 * In this step every thing in black becoming transparent.
	 * we simply combine the original image with magickManipulation (the black and white version).
	 * @param img1Path
	 * @param img2Path
	 * @return Output image file path
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IM4JavaException
	 */
    public String imageTransparent(String img1Path, String img2Path) throws IOException, InterruptedException, IM4JavaException {
        ProcessStarter.setGlobalSearchPath(IMAGE_MAGICK_PATH);
        IMOperation op = new IMOperation();
        op.addImage();
        op.density(300);
        op.addImage();
        op.density(300);
        op.compose("copy_opacity").composite();
        op.addImage();
        ConvertCmd cmd = new ConvertCmd();
        BufferedImage img1 = ImageIO.read(new File(img1Path));
        BufferedImage img2 = ImageIO.read(new File(img2Path));
        String outputFile = "./transparentImg.png";
        ImageIO.write(img1, "png", new File(outputFile));
        ImageIO.write(img2, "png", new File(outputFile));
        cmd.run(op, img1Path, img2Path, outputFile);
        return outputFile;
    }

	/**
	 * Binaries Image: This step converts a multicolored image (RGB) to a black and white image (monochrome image).
	 * @param imgOutPath
	 * @return The path for the output image
	 * @throws Exception
	 */
    public String bufferedImage(String imgOutPath) throws Exception {
        BufferedImage myPicture = ImageIO.read(new File(imgOutPath));
        int width = myPicture.getWidth();
        int height = myPicture.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                //Get RGB Value
                int val = myPicture.getRGB(x, y);
                //Source in the format:0xRRGGBB
                //Convert to three separate channels
                Color col = new Color(val, true);
                int r = col.getRed(); //or (0x00ff0000 & val) >> 16;  ____RR___
                int g = col.getGreen(); //or (0x0000ff00 & val) >> 8; ______GG__
                int b = col.getBlue(); //or (0x000000ff & val);       ________BB
                int m = (r + g + b);
                //(255+255+255)/2 =383.5 middle of dark and light
                if (m >= 383) {
                    // for light color it set white
                    bufferedImage.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    // for dark color it will set black
                    bufferedImage.setRGB(x, y, 0);
                }
            }
        }
        String imgPath = "./bufferedImage.png";
        ImageIO.write(bufferedImage, "png", new File(imgPath));
        return imgPath;
    }

}
