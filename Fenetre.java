package ocr;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Fenetre extends JFrame {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	BufferedImage image;

	public Fenetre(BufferedImage image){
		this.image = image;
        this.setTitle("Showing the image after binarization");
        this.setSize(new Dimension(image.getWidth(),image.getHeight()));
     // We now ask our object to position itself in the center
        this.setLocationRelativeTo(null);
     // End the process when you click on the red cross
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
      //Instantiation of JPanel object
        JLabel picLabel = new JLabel(new ImageIcon(image));
        JPanel pan = new JPanel();
       
        pan.add(picLabel);
        //We warn our JFrame that our JPanel will be its content pane
        this.setContentPane(pan);
        this.setVisible(true);

}
	 
}
