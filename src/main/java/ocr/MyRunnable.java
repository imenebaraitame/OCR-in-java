package ocr;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MyRunnable implements Runnable {
	public MyRunnable(InputStream istrm, OutputStream ostrm) {
	      istrm_ = istrm;
	      ostrm_ = ostrm;
	  }
	@Override
	  public void run() {
	      
	          final byte[] buffer = new byte[1024];
	          try {
	        	  //when read() returns -1, there is no more data to be read.
				for (int length = 0; (length = istrm_.read(buffer)) != -1; )
				  {
						ostrm_.write(buffer, 0, length);
					
					}
				  
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      
	     
	  }
	  private final OutputStream ostrm_;
	  private final InputStream istrm_;	
}
