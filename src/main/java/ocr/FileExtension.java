package ocr;

import org.apache.commons.io.FilenameUtils;

public class FileExtension {

	
	public static String getExtension(String filename) {
	    return FilenameUtils.getExtension(filename);
	}
	public static boolean isPng(String filename) {
		return FileExtension.getExtension(filename).contains("png");	
	}
	public static boolean isPdf(String filename) {
		return FileExtension.getExtension(filename).contains("pdf");	
	}
}
