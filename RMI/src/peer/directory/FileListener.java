package peer.directory;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TimerTask;

import peer.commands.operations.AddFilesCommandImpl;
import peer.commands.operations.RemoveFilesCommandImpl;
import server.conmanager.CommandWrapper;
import server.registry.PeerFile;
import factory.PeerCommandFactory;
import factory.peer.AddFilesPeer;
import factory.peer.RemoveFilesPeer;


/**
 * 
 * Source: http://www.rgagnon.com/javadetails/java-0490.html
 *
 * @author Koyuncu Harun
 * 
 */
public class FileListener extends TimerTask implements Runnable {

	private String path;
	private int peerTcp;
	private File filesArray[];
	private HashMap<File, Long> dir;
	private FileManager fm;
	private PeerCommandFactory comm;
	private CommandWrapper command;

	public FileListener(CommandWrapper command, String host, int serverPort, PeerCommandFactory comm, FileManager fm) {
		this.fm = fm;
		// command;
		this.comm = comm;
		this.dir = new HashMap<File, Long>();
		this.peerTcp = command.getTcpPort();
		//System.out.println("PPPPERRR TCP: " + this.peerTcp);
		//System.out.println("adsfasdf: " + command.getTcpPort());

		this.command = new CommandWrapper(null, host, serverPort, 0);
		
		filesArray = new File[fm.getFileList().size()]; 
		for (int i = 0; i < fm.getFileList().size(); i++) {
			filesArray[i] = fm.getFileList().get(i);
		}
		this.path = fm.getSharedFolder();
		// transfer to the hashmap be used a reference and keep the lastModfied value
		for (int i = 0; i < filesArray.length; i++) {
			dir.put(filesArray[i], new Long(filesArray[i].lastModified()));
		}
	}

	@Override
	public void run() {
		HashSet<File> checkedFiles = new HashSet<File>();
		
		filesArray = new File(path).listFiles();
		
		// scan the files and check for modification/addition
		for (int i = 0; i < filesArray.length; i++) {
			Long current = (Long) dir.get(filesArray[i]);
			checkedFiles.add(filesArray[i]);
			if (current == null) {
				fm.addFile(filesArray[i].getAbsolutePath());
				// new file
				dir.put(filesArray[i], new Long(filesArray[i].lastModified()));
				System.out.println("Added: " + filesArray[i].getName());
				
				Socket socket;
				try {
					//System.out.println("Listener: " + this.command.getHost()+this.command.getTcpPort());
					socket = new Socket(this.command.getHost(),this.command.getTcpPort());
					ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
					
					RemoveFilesPeer rmp = this.comm.getRemoveFiles();
					// cast File Object to a PeerFile Object
					ArrayList<PeerFile> files = new ArrayList<PeerFile>();
					for (int j = 0; j < fm.getFileList().size(); j++) {
						files.add(new PeerFile(fm.getFileList().get(j).getName(), fm.getFileList().get(j).length()));
					}
					
					RemoveFilesCommandImpl rf = new RemoveFilesCommandImpl(files, this.peerTcp);

					this.command.setWrapp(rf);
					rmp.removeFiles(this.command);
					
					out.writeObject(this.command.getWrapp());
					try {
						Object o = in.readObject();
						if (o instanceof RemoveFilesCommandImpl) {
							//System.out.println("received back");
						}
						else if (o instanceof RemoteException) {
							System.err.println("RemoteException: " + ((RemoteException) o).getMessage());
						}
						else {
							//System.out.println("Unkown call at this point: "+ o.getClass());
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					
					AddFilesPeer afp = this.comm.getAddFiles();
					
					// cast File Object to a PeerFile Object
					files = new ArrayList<PeerFile>();
					for (int j = 0; j < fm.getFileList().size(); j++) {
						files.add(new PeerFile(fm.getFileList().get(j).getName(), fm.getFileList().get(j).length()));
					}
					//System.out.println("ADDFILES LISTENER: " + this.peerTcp);
					AddFilesCommandImpl af = new AddFilesCommandImpl(files,this.peerTcp);

					this.command.setWrapp(af);
					afp.addFiles(this.command);
					out.writeObject(this.command.getWrapp());
					try {
						Object o = in.readObject();
						if (o instanceof AddFilesCommandImpl) {
							//System.out.println("AddFilesCommandImpl received back");
						}
						if (o instanceof RemoteException) {
							System.err.println("RemoteException: " + ((RemoteException) o).getMessage());
						}
						else {
							//System.out.println("Unkown call at this point: "+ o.getClass());
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					in.close();
					out.close();
					socket.close();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			else if (current.longValue() != filesArray[i].lastModified()) {
				fm.deleteFile(filesArray[i].getAbsolutePath());
				// modified file
				dir.put(filesArray[i], new Long(filesArray[i].lastModified()));
				System.out.println("Modified: " + filesArray[i].getName());

				Socket socket;
				try {
					socket = new Socket(this.command.getHost(),this.command.getTcpPort());
					ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
					
					RemoveFilesPeer rmp = this.comm.getRemoveFiles();
					// cast File Object to a PeerFile Object
					ArrayList<PeerFile> files = new ArrayList<PeerFile>();
					for (int j = 0; j < fm.getFileList().size(); j++) {
						files.add(new PeerFile(fm.getFileList().get(j).getName(), fm.getFileList().get(j).length()));
					}
					
					RemoveFilesCommandImpl rf = new RemoveFilesCommandImpl(files, this.peerTcp);

					this.command.setWrapp(rf);
					try{ 
						rmp.removeFiles(this.command);
					}
					catch(RemoteException e)
					{
						continue;
					}
					
					out.writeObject(this.command.getWrapp());
					try {
						Object o = in.readObject();
						if (o instanceof RemoveFilesCommandImpl) {
							//System.out.println("received back");
						}
						else if (o instanceof RemoteException) {
							System.err.println("RemoteException: " + ((RemoteException) o).getMessage());
						}
						else {
							//System.out.println("Unkown call at this point: "+ o.getClass());
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					
					fm.scanSharedFolder();
					AddFilesPeer afp = this.comm.getAddFiles();
					
					// cast File Object to a PeerFile Object
					files = new ArrayList<PeerFile>();
					for (int j = 0; j < fm.getFileList().size(); j++) {
						files.add(new PeerFile(fm.getFileList().get(j).getName(), fm.getFileList().get(j).length()));
					}
					//System.out.println("ADDFILES LISTENER: " + this.peerTcp);
					AddFilesCommandImpl af = new AddFilesCommandImpl(files,this.peerTcp);

					this.command.setWrapp(af);
					afp.addFiles(this.command);
					out.writeObject(this.command.getWrapp());
					try {
						Object o = in.readObject();
						if (o instanceof AddFilesCommandImpl) {
							//System.out.println("AddFilesCommandImpl received back");
						}
						if (o instanceof RemoteException) {
							System.err.println("RemoteException: " + ((RemoteException) o).getMessage());
						}
						else {
							//System.out.println("Unkown call at this point: "+ o.getClass());
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					in.close();
					out.close();
					socket.close();

				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		// now check for deleted files
		Set<File> ref = ((HashMap<File,Long>) dir.clone()).keySet();
		ref.removeAll((Set<File>) checkedFiles);
		Iterator<File> it = ref.iterator();
		while (it.hasNext()) {
			File deletedFile = (File) it.next();
			fm.deleteFile(deletedFile.getAbsolutePath());
			System.out.println("Deleted: " + deletedFile.getName());
			dir.remove(deletedFile);
			
			Socket socket;
			try {
				socket = new Socket(this.command.getHost(),this.command.getTcpPort());
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

				RemoveFilesPeer rmp = this.comm.getRemoveFiles();
				// cast File Object to a PeerFile Object
				ArrayList<PeerFile> files = new ArrayList<PeerFile>();
				for (int j = 0; j < fm.getFileList().size(); j++) {
					files.add(new PeerFile(fm.getFileList().get(j).getName(), fm.getFileList().get(j).length()));
				}
				
				RemoveFilesCommandImpl rf = new RemoveFilesCommandImpl(files, this.peerTcp);

				this.command.setWrapp(rf);
				try{
				rmp.removeFiles(this.command);
				}catch(RemoteException e)
				{
					continue;
				}
				out.writeObject(this.command.getWrapp());
				try {
					Object o = in.readObject();
					if (o instanceof RemoveFilesCommandImpl) {
						//System.out.println("received back");
					}
					else if (o instanceof RemoteException) {
						System.err.println("RemoteException: " + ((RemoteException) o).getMessage());
					}
					else {
						//System.out.println("Unkown call at this point: "+ o.getClass());
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				
				AddFilesPeer afp = this.comm.getAddFiles();
				
				// cast File Object to a PeerFile Object
				files = new ArrayList<PeerFile>();
				for (int j = 0; j < fm.getFileList().size(); j++) {
					files.add(new PeerFile(fm.getFileList().get(j).getName(), fm.getFileList().get(j).length()));
				}
				//System.out.println("ADDFILES LISTENER: " + this.peerTcp);
				AddFilesCommandImpl af = new AddFilesCommandImpl(files,this.peerTcp);

				this.command.setWrapp(af);
				afp.addFiles(this.command);
				out.writeObject(this.command.getWrapp());
				try {
					Object o = in.readObject();
					if (o instanceof AddFilesCommandImpl) {
						//System.out.println("AddFilesCommandImpl received back");
					}
					if (o instanceof RemoteException) {
						System.err.println("RemoteException: " + ((RemoteException) o).getMessage());
					}
					else {
						//System.out.println("Unkown call at this point: "+ o.getClass());
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				in.close();
				out.close();
				socket.close();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//System.out.println("##############################################");
		//fm.printAllFiles();
	}
}
