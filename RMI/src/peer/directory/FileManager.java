package peer.directory;

import java.io.File;
import java.util.ArrayList;

/**
 * This class handels the shared folder and the files in the shared folder.
 * If the given shared folder doesnt exists on the computer, the programm
 * creates it. Every file in the shared folder must be registered with the
 * method "add" to a file registery, otherwise the files arent ready to use
 * for transfes. 
 * 
 * @author Koyuncu Harun (hkoyuncu@student.tgm.ac.at)
 * @version 2013.01.04
 */

public class FileManager {

	// these are the needed attributes 
	private ArrayList<File> fileList;
	private int fileCounter;
	private String sharedFolder;
	
	/**
	 * We overwrite the defoualt constructor.
	 * 
	 * @param sharedFolder		the path to the shared folder
	 */
	public FileManager(String sharedFolder) {
		// create a new object of the file registery
		this.fileList = new ArrayList<File>();
		// set default value 
		this.fileCounter = 0;
		// setting the shared folder
		this.sharedFolder = sharedFolder;
	}
	
	/**
	 * @return the sharedFolder
	 */
	public String getSharedFolder() {
		return sharedFolder;
	}

	/**
	 * @param sharedFolder the sharedFolder to set
	 */
	public void setSharedFolder(String sharedFolder) {
		this.sharedFolder = sharedFolder;
	}
	
	/**
	 * This method checks if the shared folder exists.
	 * If not the it will be created. 
	 */
	public boolean createDir() {
		try {
			// check if the value of the variable is valid
			if (this.sharedFolder != null && !this.sharedFolder.equals("")) {
				// now we save a boolean value into a varible to check the status of the
				// directory creation
				boolean success = new File(this.sharedFolder).mkdir();
				// if the shared folder doesnt exists before then we print a specific information
				if (success) {
					System.out.println("Shared Folder erstellt: " + this.sharedFolder + ".");
					return true;
				}
				// if the directory exists...
				else {
					System.out.println("Shared Folder existiert bereits.");
					return false;
				}
			}
			return false;
		}
		// if there is an error, we catch it and print it to the console
		catch (Exception e) {
			return false;
		}
	}

	/**
	 * This method adds a file from the shared folder to the file registery.
	 * 
	 * @param newFile		name of the file that should be registered
	 */
	public boolean addFile(String newFile) {
		try {

			// we create a new file with the name from the parameter
			File file = new File(newFile);
			// if the file is not in the registery and the file exists on the
			// shared folder
			if (!fileList.contains(file) && file.exists()) {
				// we add this file to the file registery
				fileList.add(file);
				// then we incerement the index counter
				fileCounter++;
				
				return true;
			}
			// when the file is already in the file registery
			else {
				// we print a specific message
				// System.out.println("Die Datei " + newFile + " ist schon im Register.");
				return false;
			}
		}
		// if there is an error, we catch it and print it to the console
		catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * This method deletes a file with the given name from
	 * the registery and from the shared folder.
	 * 
	 * @param fileName		the name of the file that schould be deleted
	 */
	public boolean deleteFile(String fileName) {
		try {
			// we iterate through the file registery 
			for (int i = 0; i < this.fileList.size(); i++) {
				// and save the name of the file at the position i into a variable
				String tempName = this.fileList.get(i).getAbsolutePath();
				// now we check the the names and if they are equal
				if (tempName.equals(fileName)) {
					// and then we delete the entry from the file registery
					this.fileList.remove(i);
					return true;
				}
			}
			return false;
		}
		// if there is an error, we catch it and print it to the console
		catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * @return the fileList
	 */
	public ArrayList<File> getFileList() {
		return fileList;
	}

	/**
	 * @param fileList the fileList to set
	 */
	public void setFileList(ArrayList<File> fileList) {
		this.fileList = fileList;
	}

	/**
	 * This method searchs a given file name in the file registery.
	 * 
	 * @param fileName		file that will be searched
	 * @param size			size of the file which schould be searched 
	 */
	public boolean findFile(String fileName, long size) {
		// we iterate through the file registery and ..
		for (int i = 0; i < this.fileList.size(); i ++) {
			// save the file at the position i to a temporally object 
			File temp = this.fileList.get(i);
			// now we set the identfiers from the temprally file
			String fileProps = temp.getName() + temp.length();
			// now we set the identifiers from the search file
			String actualPros = fileName + size;
			// we compare the two identifiers and if the match
			if (fileProps.equals(actualPros)) {
				// then we print a success message, that the file was found in the file registery
				//System.out.println("Die gesuchte Datei " + fileName + " wurde im Registery gefunden.");
				return true;
			}
			
		}
		return false;
	}
	
	/**
	 * This method searchs a given file name in the file registery.
	 * 
	 * @param fileName		file that will be searched
	 */
	public String findFile(String fileName) {
		String result = null;
		// we iterate through the file registery and ..
		for (int i = 0; i < this.fileList.size(); i++) {
			System.out.println(fileList.get(i).getAbsolutePath());
			// we compare the two identifiers and if the match
			if (fileList.get(i).getName().equals(fileName)) {
				// then we print a success message, that the file was found in the file registery
				System.out.println("Die gesuchte Datei " + fileName + " wurde im Registery gefunden.");
				result = fileList.get(i).getAbsolutePath();
				
				return result;
			}
			// if the file wasnt found in the file registery
			else {
				// we print a specific message
				//System.out.println("Daie Datei " + fileName + " konnte nicht gefunden werden.");
				//return result;
			}
		}
		return result;
	}
	
	
	
	/**
	 * This method scans alle the files in the given shared folder.
	 * Only files, which are located in the root directory of the
	 * shared folder will be handled. Files in subdirectories will
	 * be ignored. Then the method will register everey valid file
	 * into the file registery.
	 */
	public boolean scanSharedFolder() {
		try {
			
			// Create a file object which points to the shared folder
			File sharedFolder = new File(this.sharedFolder);
			// now we get all the files from the shared folder and save it
			// to a file array
			File[] files = sharedFolder.listFiles();
			if(files == null)
			{
				System.out.println("SharedFolder was invalid!");
				return false;
			}
			
			// now we iterate throug the result set from the file array object...
			for (int i = 0; i < files.length; i++) {
				// and if a file is really a file and not a directory
				if (files[i].isFile()) {
					// we add the file to the file registery
					addFile(files[i].getAbsolutePath());
				}
			}
			
			return true;
		}
		// if there is an error, we catch it and print it to the console
		catch (Exception e) {
			e.printStackTrace();
		}
		// an information will be printed
		//System.out.println("Shared Folder wird nach Datein durchsucht.");
		return false;
	}
	
	/**
	 * This method prints all files, which are registered, into the console.
	 * If no files were registered then a specific output will be printed
	 */
	public void printAllFiles() {
		// now we iterate through the "fileRegistery" and print from each file it's name and size
		for (int i = 0; i< this.fileList.size(); i++) {
			System.out.println("Dateiname: " + this.fileList.get(i).getName() + ", Dateigroesse: " + (this.fileList.get(i).length()/1024) + " KB");
		}
		// if there is no files, we print a specific message 
		if (this.fileList.size() == 0) {
			System.out.println("Keine Datein im Shared Folder registriert.");
		}
	}
}
 
