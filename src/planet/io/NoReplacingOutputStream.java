package planet.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This class abstracts an stream that is capable of no replace 
 * the new saved status, according a specified boolean flag.
 * If the file must not be replaced, a new file is created with
 * a filename of type "filename_x", where "filename" is the 
 * specified base filename, and "x" is an iterative version number.
 * That is, if "out" filename just exists, it attempts to create
 * a new file "out_1"; if this just exist, it attempts to create
 * a new file named "out_2"; ... 
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 11-jul-2005
 */
public class NoReplacingOutputStream extends OutputStream {
    /**
     * Shows the current incremental number of version to be used under no 
     * replacing context.
     */
    private static int version = 1;
	/**
	 * Shows if the file must be replaced with the new status to save. 
	 */
	private boolean replace;
	/**
	 * Shows the base filename that must save the new status.
	 */
	private String filename = null;
	/**
	 * FileOutputStream stream to save the bytes stream.
	 */
	private FileOutputStream fos = null;
	
	/**
	 * Constructor that initialize this stream with the specified base
	 * <b>filename</b>. The <b>replace</b> flag shows if the file must
	 * be replaced with new save status or not. If must not be replaced,
	 * a suffix "_x" is added to the filename, where "x" is the number of 
	 * version.
	 * @param filename Base filename to save new status.
	 * @param replace Boolean flag that shows if the file must be replaced.
	 * @throws FileNotFoundException if the new desired file just exists.
	 */
	public NoReplacingOutputStream(String filename, boolean replace) throws FileNotFoundException {
		this.filename = filename;
		this.replace = replace;
		if (!replace) {
            this.filename = getNonExistingFilename(filename);
		}
		this.fos = new FileOutputStream(this.filename);
	}
    
    /**
     * Gets the filename used to save the current state.
     * @return The filename used to save the current state.
     */
    public String getFilename()
    {
        return filename;
    }
    
    /**
     * Finds a non existing filename adding the suffix "_x" to the filename, 
     * where "x" is the number of version.
     * @param filename Destination filename.
     * @return A non existing filename.
     */
    private static String getNonExistingFilename(String filename) 
    {
        String name = new String(filename);
        File f = new File(name);
        
        while (f.exists()) {
            version++;
            name = getNewVersion(filename,version);
            f = null;
            f = new File(name);
        }
        return name;
    }
	
    /**
     * Gets the new name for the file using the current <b>newVersion</b>.
     * @param baseName Original filename.
     * @param newVersion Number of version to use.
     * @return The new filename to use.
     */
    private static String getNewVersion(String baseName, int newVersion)
    {
        //there are extension part
        int index = baseName.lastIndexOf('.');
        if (index != -1)
        {
            return  baseName.substring(0,index)+
                    "_" + newVersion +
                    baseName.substring(index,baseName.length());
        }
        return baseName + "_" + newVersion;
    }
    
	/**
	 * @see java.io.OutputStream#write(int)
	 */
	public void write(int b) throws IOException {
		fos.write(b);
	}
	
	/**
	 * @see java.io.OutputStream#close()
	 */
	public void close() throws IOException {
		fos.close();
	}
	
	/**
	 * @see java.io.OutputStream#flush()
	 */
	public void flush() throws IOException {
		fos.flush();
	}
	
	/**
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	public void write(byte[] b, int off, int len) throws IOException {
		fos.write(b, off, len);
	}
	
	/**
	 * @see java.io.OutputStream#write(byte[])
	 */
	public void write(byte[] b) throws IOException {
		fos.write(b);
	}
}
