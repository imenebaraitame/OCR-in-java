package ocr;

public class Utils {


	public static String getOsName(){
		return System.getProperty("os.name");
	}
	public static boolean isLinux(){
		return Utils.getOsName()=="Linux";
	}
	public static boolean isWindows(){
		return Utils.getOsName()=="Windows";
	}
}
