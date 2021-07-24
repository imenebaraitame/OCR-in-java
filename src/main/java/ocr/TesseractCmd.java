package ocr;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.ITesseract.RenderedFormat;
import net.sourceforge.tess4j.TesseractException;

public class TesseractCmd {
	String input_file; String output_file; String tesseract_install_path ;String configfile;
	
	public TesseractCmd(String input_file, String output_file, String tesseract_install_path ,String configfile){
		this.input_file = input_file;
		this.output_file = output_file;
		this.tesseract_install_path = tesseract_install_path;
		this.configfile = configfile;
	}
	
	public void textOnlyPdf(String imagePath){
		List<RenderedFormat> formats = new ArrayList<RenderedFormat>(Arrays.asList(RenderedFormat.PDF));
		try {
		Tesseract instance = new Tesseract();
		instance.createDocuments(new String[]{imagePath}, new String[]{output_file}, formats);
		} catch (TesseractException te){
			System.err.println("Error TE: " + te.getMessage());
		}
	}
		
	public void searchableImg() throws IOException {
		
      		String[] command ={ (Utils.isWindows()?"cmd":"bash") , };
		 
		 try {
		 // Instantiating Process object
		 Process p = Runtime.getRuntime().exec(command);
		 // By using geterrorStream() method is to get the error stream of p object
		        new Thread( new MyRunnable(p.getErrorStream(), System.err)).start();
		 // By using getInputStream() method is to get the input stream of p object       
		        new Thread(new MyRunnable(p.getInputStream(), System.out)).start();
		        PrintWriter stdin = new PrintWriter(p.getOutputStream());
		        stdin.println("\""+tesseract_install_path+"\" \""+input_file+"\" \""+output_file+"\" \""+configfile+"\" ");
		        stdin.close();
		       
		        int exitValue = p.waitFor();
				if (exitValue != 0) {
				    System.out.println("Abnormal process termination");
				}
				// Destroy a process
				p.destroy();
				if (p.exitValue() != 0) {
				    System.out.println("Abnormal process termination");
				}
		        
		    } catch (Exception e) {
		             e.printStackTrace();
		        }

	}
	

}
