package de.vaplus.helper;

import java.io.IOException;
import java.io.InputStream;

public class FileHelper {

	public static byte[] readFromFileInputStream(InputStream is, long length) throws IOException{
		
		
	  if (length > Integer.MAX_VALUE)
	  {
	    return null;
	  }
	  // Create the byte array to hold the data
	  byte[] bytes = new byte[(int) length];
	  // Read in the bytes
	  int offset = 0;
	  int numRead = 0;
	  while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)
	  {
	    offset += numRead;
	  }
	  // Ensure all the bytes have been read in
	  if (offset < bytes.length)
	  {
	    throw new IOException("Could not completely read file ");
	  }
	  // Close the input stream and return bytes
	  is.close();
	  return bytes;
	}
	
}
