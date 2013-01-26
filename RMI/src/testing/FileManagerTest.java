package testing;

import static org.junit.Assert.*;
import org.junit.Test;
import peer.directory.FileManager;

/**
 * This J-Unit class tests the methods from the class FileManager.
 * 
 * @author Koyuncu Harun (hkoyuncu@student.tgm.ac.at)
 * @version 2013-01-22
 */

public class FileManagerTest {
	
	// define the private Attributes
	private FileManager fm;
	
	/**
	 * In this method we initialize the FileManager object with
	 * a concrete shared folder.
	 * 
	 * @param path	pach to a sharedFolder
	 */
	public void setUp(String path) {
		this.fm = new FileManager(path);
	}

	/**
	 * Test what will happen if the folder doesn't exist but should be scanned.
	 */
	@Test
	public void testSharedFolderNotExisting() {
		setUp("asdfsdfdsd");
		// check values
		assertEquals(fm.scanSharedFolder(), false);
	}
	
	/**
	 * Test what will happen if  a searched file not exists in the shared folder
	 */
	@Test
	public void testFindFileNotExists() {
		setUp("TestFolder");
		// check values
		assertEquals(fm.findFile("hallo.txt"), null);	
	}
	
	/**
	 * This test checks if the Filemanager class can create a shared folder  with
	 * the value "null". Then we access the shared folder and it  should be "null".
	 */
	@Test
	public void testCreationOftheSharedFolder() {
		// define an unvalid shared folder 
		setUp(null);
		// try to create the shared folder
		fm.createDir();
		// check values
		assertEquals(fm.getSharedFolder(), null);
	}	
	
	/**
	 * This test the amount of files in the shared folder, if teh shared folder
	 * was created newly. The result end up with 0 files in the folder.
	 */
	@Test
	public void testAmountOfFiles() {
		setUp("sharedFolder");
		// create the shared folder
		fm.createDir();
		// scan all files in the shared folder
		fm.scanSharedFolder();
		// check values
		assertEquals(fm.getFileList().size(), 0);
	}
}
