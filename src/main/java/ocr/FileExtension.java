package ocr;

import org.apache.commons.io.FilenameUtils;

public class FileExtension {

	
	public static String getExtensionOfFile(String filename) {
	    return FilenameUtils.getExtension(filename);
	}
	public static boolean isPng(String filename) {
		return FileExtension.getExtensionOfFile(filename).contains("png");	
	}
	public static boolean isPdf(String filename) {
		return FileExtension.getExtensionOfFile(filename).contains("pdf");	
	}
}
