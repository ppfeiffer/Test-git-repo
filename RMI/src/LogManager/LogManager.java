package LogManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * These class handles the logs.
 * 
 * @author Paul Pfeiffer-Vogl(ppfeiffer@student.tgm.ac.at)
 * @version 26-09-2012
 */
public class LogManager {

	// the object to write in the log file
	private FileWriter logs = null;
	private BufferedWriter logsOut = null;
	// the time when the log has been written
	private long start = 0;
	// true -> debug information is given
	private boolean debug = false;
	// the job-name (the actual date)
	private String jobName;


	/**
	 * In the constructor the streams are created to write a log data.
	 * 
	 * @param path
	 * 				path to the log file, the name of the file inclusive
	 * @param jobName
	 * 				the job name that is created for the log file
	 */
	public LogManager(boolean debug) {
		this.start = System.currentTimeMillis();
		
		// time for the log is created
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-kkmmss");
		this.jobName = format.format(new Date()).toString() + ".log";

		if (debug)
			System.out.println("ERRORLOGPATH: " + this.jobName);

		try {
			
			this.logs = new FileWriter(this.jobName, true);
			this.logsOut = new BufferedWriter(this.logs);
			
		} catch (IOException e) {
			System.out.println("Coudn't create Log file for " + this.jobName
					+ ". (" + e.getMessage() + ")");
			if (this.debug == true)
				e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Coudn't create Log file for " + this.jobName
					+ ". (" + e.getMessage() + ")");

			if (this.debug == true)
				e.printStackTrace();
		}

		this.createFile();	// create the file
	}

	/**
	 * These method is directly called by the constructor. The method calls writeHeadLine().
	 */
	private void createFile() {
		writeHeadLine();
	}

	/**
	 * @return the jobName
	 */
	public String getJobName() {
		return jobName;
	}

	/**
	 * The execute-path is returend.
	 * 
	 * @return The execute-path is returend.
	 */
	public String getCurrentDir() {
		String executionPath = null;
		try {
			executionPath = System.getProperty("user.dir");
			// System.out.print("Executing at =>"+executionPath.replace("\\",
			// File.separator));
		} catch (Exception e) {
			try {
				this.logsOut.write("Exception caught =" + e.getMessage());
			} catch (IOException e1) {
				System.out.println("Exception caught =" + e.getMessage());
			}
			System.out.println("Exception caught =" + e.getMessage());
		}
		return executionPath;
	}

	/**
	 * The method writes the time and the jobname at the beginning.
	 */
	private synchronized void writeHeadLine() {
		try {
			this.logs = new FileWriter(this.jobName, true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.logsOut = new BufferedWriter(this.logs);

		try {
			// at the beginnung the data is written to the file
			this.logsOut.write(new Date().toString().replace(" ", "_")
					+ " - JobName: " + this.jobName + '\n');
		} catch (IOException e) {
			System.out.println("Error occured writing log file(writeHeadLine)");
		}

		// close the streams
		try {
			this.logsOut.close();
			this.logs.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * At the end of the log file a specific footer is written. (time of the log, logname, ...)
	 */
	private synchronized void writeFooter() {
		try {
			this.logs = new FileWriter(this.jobName, true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.logsOut = new BufferedWriter(this.logs);

		// the difference between the start and the stop is calculated
		long tmp = System.currentTimeMillis() - start;
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		// data is formated
		Date date = new Date(tmp);
		// now write the data to the log file
		try {
			this.logsOut.write("\n\n" + new Date().toString().replace(" ", "_")
					+ " - LOG STOPPED AFTER " + dateFormat.format(date) + '\n');
			this.logsOut.write("\nProgram wasn't terminated by an error!");
		} catch (IOException e) {
			System.out.println("Error occured writing log file(writeHeadLine)");
		}

		try {
			this.logsOut.close();
			this.logs.close();
		} catch (IOException e) {
			if (this.debug == true)
				e.printStackTrace();
		}
	}

	/**
	 * The actual timestamp is returned. [HH:mm:ss]
	 * @return timestamp
	 */
	private String getCurrentTimeStamp() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();

		return dateFormat.format(date);
	}

	/**
	 * This method is used to write in the log file. Programs can use this method for their log.
	 * The actual timestamp is set before.
	 * 
	 * @param buffer
	 *            the message that is written to the file (String)
	 */
	public synchronized void write(String buffer) {
		try {
			this.logs = new FileWriter(this.jobName, true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.logsOut = new BufferedWriter(this.logs);
		try {
			this.logsOut.write('\n' + this.getCurrentTimeStamp() + " - ");
			this.logsOut.write(buffer);
		} catch (IOException e) {
			System.out.println("Error occured writing log file(write)\nerror:"
					+ e.getMessage());
		}
		try {
			this.logsOut.close();
			this.logs.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * The streams will be closed and resources are cleaned.
	 * The specific footer will be written.
	 * At the end all log files are packed in a .zip archive
	 */
	public void close() {
		// Footer wird geschrieben
		this.writeFooter();
		try {
			this.logsOut.close();
			this.logs.close();
		} catch (IOException e) {
			// TODO Auto-generated catch bloc

			e.printStackTrace();
		}
		if (this.debug)
			System.out.println("jobname: " + this.jobName);
	}

}
