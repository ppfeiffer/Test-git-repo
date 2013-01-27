/************************************************************************/
import java.rmi.*;
import java.awt.*;
import java.io.*;
import java.io.File;
import java.rmi.server.*;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.JFileChooser;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
/************************************************************************/
public class BroadcastListenerImpl extends UnicastRemoteObject implements BroadcastListener
{
	Frame frame;
	List list;
	List list1;
	TextField tf;
	static JButton button;
	JLabel logout_Label;
	Label displayName_Label;
    JLabel users_Label;
    Label general_char_Label;
	TextArea message_TextArea;
	JScrollPane scrollpane1;
    Label message_label;
    JLabel login_label;
	////////////////////////// private window
	Frame privateFrame;
	TextField privateTextField;
	Button privateButton;
    TextArea privateMessage_TextArea;
    Label privateMessage_label;
	Label privateDisplayName_Label;
	Label file_label ;
	Label voice_label;
	static String privateUserName = " ";
	static String userMessage = "Hellow";
	static int myIndex = 0;
///////////////////////////////	private window end
	String welcome = "";
	static String userName = " ";
	static String privateUserMessage = " ";
	String selectedName = null;
	public int Index = 0;
	static BroadcastServer bs;
	private static BroadcastListenerImpl bli;
	public static int i=0;
/************************************************************************/	
	public void privateWindow(int getIndex, String myName) throws RemoteException
{

	new personal_chat(getIndex, myName);		

} // end of  private_Window function
/************************************************************************/
	public static void user_Message(BroadcastServer bss, BroadcastListenerImpl Bli , String user_Name)
	{
	
		bs=bss;
		bli = Bli;
		userName = user_Name;
		button.setEnabled(true);
	}
/************************************************************************/
	public BroadcastListenerImpl( BroadcastServer broadcastServer)throws RemoteException
	{
		bs = broadcastServer;
		
		frame = new Frame("Chat");
		tf=new TextField();
		       
		list= new List(20);
		
		button=new JButton("Send");
		button.setIcon(new javax.swing.ImageIcon("images\\SalesRep.png"));
		login_label = new JLabel("Login");
		login_label.setIcon(new javax.swing.ImageIcon("images\\login_icon.gif"));
		login_label.setFont(new Font("",Font.BOLD,14));
        message_label=new Label("Message");
		message_label.setFont(new Font("",Font.BOLD,11));
        general_char_Label = new Label("General Chat");
		general_char_Label.setFont(new Font("",Font.BOLD,12));
		logout_Label = new JLabel("Logout");
		logout_Label.setIcon(new javax.swing.ImageIcon("images\\LOGGOFF.PNG"));
		logout_Label.setFont(new Font("",Font.BOLD,14));
        users_Label = new JLabel("Users");
		users_Label.setIcon(new javax.swing.ImageIcon("images\\user1.jpg"));
		users_Label.setFont(new Font("",Font.BOLD,12));
		welcome = "welcome :"+userName;
		displayName_Label = new Label(welcome);
		privateDisplayName_Label = new Label("Welcome");		
		logout_Label.setEnabled(true);
		login_label.setEnabled(true);
				
		message_TextArea=new TextArea();
		message_TextArea.setEditable(false);
		scrollpane1=new JScrollPane(list);
		Panel panel = new Panel();
	
		panel.add(tf);
        panel.add(button);
        panel.add( message_TextArea );
		panel.add(message_label);
        panel.add(login_label);
        panel.add(logout_Label);
		panel.add(scrollpane1);
        panel.add(general_char_Label);
        panel.add(users_Label);
		panel.add(displayName_Label);

		message_TextArea.setBounds(60,70,320,350);
		scrollpane1.setBounds(470,70,180,350);

        users_Label.setBounds(470,40,80,35);
        users_Label.setForeground(Color.red);
        general_char_Label.setBounds(60,40,80,35);
        general_char_Label.setForeground(Color.red);
		message_label.setBounds(5,435,90,30);
        message_label.setForeground(Color.red);
        login_label.setBounds(10,5,60,20);
        displayName_Label.setBounds(220,25,200,20);
		privateDisplayName_Label.setBounds(220,25,200,20);
		displayName_Label.setBackground(Color.cyan);
		displayName_Label.setForeground(Color.blue);
		privateDisplayName_Label.setForeground(Color.blue);
        login_label.setForeground(Color.red);
		logout_Label.setBounds(70,5,90,20);
        logout_Label.setForeground(Color.red);
		        
		tf.setBounds(60,435,320,30);
		button.setBounds(400,435,90,30);
		
		frame.add(panel);
		panel.setLayout(null);
		frame.setSize(800, 600);
        frame.setBackground(Color.orange);
		button.setEnabled(false);
	   	frame.setVisible(true);
		frame.setResizable(false);
/************************************************************************/		
		frame.addWindowListener (new WindowAdapter()     // when close the frame.
        {
			public void windowClosing(WindowEvent e)
			{
				try
				{
					login_label.setEnabled(true);
					logout_Label.setEnabled(false);
					bs.removeBroadcastListener(bli, userName);					//remove this object with this user name.
					
				}
				catch(Exception ex)
				{
					//JOptionPane.showMessageDialog(null,"Error Occured\n", "OOPs",JOptionPane.ERROR_MESSAGE);
				}
				
             System.exit(0);
			}
        }
); 
/************************************************************************/	
		button.addActionListener(new ActionListener()					// anonymous inner class
		{  
			public void actionPerformed( ActionEvent event ) 
			{
				displayName_Label.setText(welcome+userName);			// welcoming the incoming user
				userMessage = tf.getText();
				userMessage = userName + " ->:" + userMessage ;			// getting the message alongwith the user name 
				tf.setText(" ");
				try
				{
				
					bs.text_Message(userMessage);					// remote method called to send the user broadcast message
				
				}
				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(null,"Error Occured\n", "OOPs",JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	); //end of action
/************************************************************************/
		logout_Label.addMouseListener(new java.awt.event.MouseAdapter() 
		{					//start of action
			public void mouseClicked(java.awt.event.MouseEvent evt) 
			{
				Label1MouseClicked(evt);
			}

            private void Label1MouseClicked(java.awt.event.MouseEvent evt) 
			{
			
				try
				{	
					button.setEnabled(false);
					login_label.setEnabled(true);
					logout_Label.setEnabled(false);
					bs.removeBroadcastListener(bli, userName);					//remove this object with this user name.
					JOptionPane.showMessageDialog(null,"Loged Out.");
				}
				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(null,"You are not Login or ohter connection error. ", "CONNECTION ERROR",JOptionPane.ERROR_MESSAGE);
				}
			               
            }
        }
	);	//end of action
/************************************************************************/
    login_label.addMouseListener(new java.awt.event.MouseAdapter()					//start of action
	{					
		public void mouseClicked(java.awt.event.MouseEvent evt)
		{
			Label1MouseClicked(evt);
		}

        private void Label1MouseClicked(java.awt.event.MouseEvent evt) 
		{	button.setEnabled(true);
			logout_Label.setEnabled(true);
			login_label.setEnabled(false);
			BroadcastClient client = new BroadcastClient();
			client.show();
        }
    }
);	//end of action
/************************************************************************/		
		list.addMouseListener(new java.awt.event.MouseAdapter()					//when user selected a member to private chat
		{
			public void mouseClicked(java.awt.event.MouseEvent evt)
			{
				listMouseClicked(evt);
			}

            private void listMouseClicked(MouseEvent evt)
			{
                Index = list.getSelectedIndex();
                selectedName = list.getSelectedItem();
				
				if(selectedName == null)					// if no item there then do nothing
				{
				}
				
				else
				{
					try
					{
					
						new personal_chat (Index, selectedName);					// constructor of class which is used for personal chat
					
					}
					catch(Exception ex)
					{
						JOptionPane.showMessageDialog(null,"Error Occured\n", "OOPs",JOptionPane.ERROR_MESSAGE);
					}
					
					try
					{
						bs.open_personal_window (Index, userName);					// remote method used to locate the other selected usera and to open his chat window
					}
					catch(Exception ex)
					{
						JOptionPane.showMessageDialog(null,"Error in calling remote method\n", "OOPs",JOptionPane.ERROR_MESSAGE);
					}
				
				}
					list.deselect(Index);
			} 	
		}
	);	// end of list action 
/************************************************************************/ 		
}	//end of upper class constructur
/************************************************************************/	
	public void receiveBroadcast(String message) throws RemoteException
	{
		message_TextArea.append(message+"\n");
	}
/************************************************************************/	
	public void receivePrivate(String message) throws RemoteException
	{
		privateMessage_TextArea.append(message+"\n");
	}
/************************************************************************/
	public void receiveBroadcastName(ArrayList user_List) throws RemoteException
	{
		list.clear();					//clear the already exits users and apend all again.
		for (int i=0; i < user_List.size(); i++)
		{
			String  user_Name = ( (String )user_List.get(i) ).toString();					
			list.add(user_Name);					// appending all the users in the the users text area
		}
	}
/************************************************************************/	
	public void receiveFile(String fileName, byte [] buffer) throws RemoteException 
    {
		String fname =  fileName;
		byte [] fileData = buffer;
		
		int saveOption = JOptionPane.showConfirmDialog(null,"Do you want to Save file? :"+fname,"Save File ?",JOptionPane.YES_NO_OPTION);
		if (saveOption==0)				// if yes then save the file in the specified directry
		{
			try
			{
				File directory = new File("C:\\messenger");
				directory.mkdir();
				boolean floderIsCreated = directory.mkdir();
				FileOutputStream fos = new FileOutputStream("C:\\messenger\\"+fname);	// opens the file to write the bytes in file
				fos.write(fileData);						// write the bytes in the file and file is created here
				fos.close();
				
				JOptionPane.showMessageDialog(privateFrame,"File received Successfully...", "File Received"+fname,JOptionPane.INFORMATION_MESSAGE);
			}
			catch(IOException e)
			{
				JOptionPane.showMessageDialog(null,"Error File is not received correctly.\n", "OOPs",JOptionPane.ERROR_MESSAGE);

			}
		}
		
		else					// if user not want to save file then do nothing.
		{	
			
		}
    			
    }
/************************************************************************/
	public void receivesound(String fileName, byte [] buffer) throws RemoteException 
    {
		String fname =  fileName;
		byte [] fileData = buffer;
		
		int saveOption = JOptionPane.showConfirmDialog(null,"Do you want to Save file? :"+fname,"Save File ?",JOptionPane.YES_NO_OPTION);
		if (saveOption==0)				// if yes then save the file in the specified directry
		{
			try
			{
				File directory = new File("C:\\VoiceMails");
				directory.mkdir();
				boolean floderIsCreated = directory.mkdir();
				FileOutputStream fos = new FileOutputStream("C:\\VoiceMails\\"+fname);	// opens the file to write the bytes in file
				fos.write(fileData);						// write the bytes in the file and file is created here
				fos.close();
				
				JOptionPane.showMessageDialog(privateFrame,"File received Successfully...", "File Received"+fname,JOptionPane.INFORMATION_MESSAGE);
			}
			catch(IOException e)
			{
				JOptionPane.showMessageDialog(null,"Error File is not received correctly.\n", "OOPs",JOptionPane.ERROR_MESSAGE);

			}
		}
		
		else					// if user not want to save file then do nothing.
		{	
			
		}
    			
    }
/************************************************************************/



class personal_chat extends UnicastRemoteObject implements Runnable
{	
	public int selectedIndex = 0;
	String printName = null;
	Thread thread;	
	int Index = 0;
	public personal_chat (int Index, String name) throws RemoteException
	{
		this.printName = name ;
		this.selectedIndex = Index ;
		thread = new Thread (this) ;
		thread.start();	
		
	}
	
	public void run()
	{
			
			privateFrame = new Frame("Chat  with: " + printName);
			privateTextField=new TextField();

			privateButton=new Button("Send");
			
			privateMessage_label=new Label("Message");
			privateMessage_label.setFont(new Font("",Font.BOLD,11));

			privateMessage_TextArea =new TextArea();
			privateMessage_TextArea.setEditable(false);
		
			Panel panel = new Panel();
			file_label = new Label();
			voice_label=new Label();
			file_label.setText("Send File");
			voice_label.setText("Voice Mail");
			panel.add(privateTextField);
			panel.add(privateButton);
			panel.add(privateMessage_TextArea);
			panel.add(privateMessage_label);
			panel.add( privateDisplayName_Label );
			panel.add(file_label);
			panel.add(voice_label);
			file_label.setFont(new Font("",Font.BOLD,12));
            file_label.setForeground(Color.red);
			file_label.setBounds(70,5,60,17);
		    voice_label.setFont(new Font("",Font.BOLD,12));
            voice_label.setForeground(Color.red);
			voice_label.setBounds(150,5,60,17);
			privateMessage_TextArea.setBounds(60,70,320,350);
			privateMessage_label.setBounds(5,435,90,30);
			privateMessage_label.setForeground(Color.red);
        
			privateTextField.setBounds(60,435,320,30);
			privateButton.setBounds(400,435,90,30);

			privateFrame.add(panel);
			panel.setLayout(null);
			privateFrame.setSize(520, 600);
			privateFrame.setBackground(Color.orange);
			privateFrame.setVisible(true);
			privateFrame.setResizable(false);
			privateMessage_TextArea.setEditable(false);
/************************************************************************/				
		privateFrame.addWindowListener (new WindowAdapter()     // when close the privateFrame.
			{
				public void windowClosing(WindowEvent e)
				{
					privateFrame.dispose();
					
				}
			}
);
/************************************************************************/				
		privateButton.addActionListener(new ActionListener()					// anonymous inner class
		{  
			public void actionPerformed( ActionEvent event ) 
			{
				privateUserMessage = privateTextField.getText();
				privateUserMessage = userName + " ->:" + privateUserMessage ;
				privateDisplayName_Label.setText("Welcome"+userName);
				privateTextField.setText(" ");
				privateMessage_TextArea.append(privateUserMessage+"\n");
				
				try
				{
					bs.private_chat( selectedIndex, privateUserMessage );
				}
				
				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(null,"Error Occured\n", "OOPs",JOptionPane.ERROR_MESSAGE);
				}
			}
		} 
	); 	//end of action
/************************************************************************/
		file_label.addMouseListener(new java.awt.event.MouseAdapter()					//start of action
		{					
			public void mouseClicked(java.awt.event.MouseEvent evt)
			{
				Label1MouseClicked(evt);
			}

			private void Label1MouseClicked(java.awt.event.MouseEvent evt) 
			{
				try
				{
					new sendfile(selectedIndex);				// calling the constuctor of the class which is dealing with file.
				}
				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(null,"Error Occured in reading the file\n", "OOPs",JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	);	//end of action
//}  //end of run method
	
//} // end of inner class personal chat
/************************************************************************/
		voice_label.addMouseListener(new java.awt.event.MouseAdapter()					//start of action
		{					
			public void mouseClicked(java.awt.event.MouseEvent evt)
			{
				//Label1MouseClicked(evt);
				try
				{
				new Voice_mail(bs, selectedIndex);
				}
				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(null,"Can't send voice mail now.\n", "OOPs",JOptionPane.ERROR_MESSAGE);

				}
				//new soundfile(selectedIndex);
			}
             
		
		}
	);	//end of action
}  //end of run method
	
} // end of inner class personal chat





/************************************************************************/	
class sendfile extends UnicastRemoteObject
{
	String filename=" ";
	byte []buffer = null;
	File file=null;
	int selectedIndex = 0;
	
	public sendfile(int selectedIndex) throws RemoteException
	{
		this.selectedIndex = selectedIndex;				// initilizing the selected user index
		final JFileChooser fc = new JFileChooser();		// choosing the file by userinterface
        fc.showOpenDialog(privateFrame);
		try
		{
			Scanner reader = new Scanner( fc.getSelectedFile() ); 			// will tell about the status of the file
			file = fc.getSelectedFile();				// getting the name of the file withe complete path
			filename = file.getName();
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null,"Error Occured in reading the file\n", "OOPs",JOptionPane.ERROR_MESSAGE);
		}
		
		try
		{
			FileInputStream fis = new FileInputStream(file);	// open the file
			if (fis!=null)
			{
				System.out.println("\n\tFILE FOUND Successfully\n\tReady to Send!!!!!");
				buffer = new byte[fis.available()];		// create the buffer of size of file
				fis.read(buffer);		// read the file in bytes and store the bytes in buffer
				try
				{
					bs.sendFile(filename, buffer, selectedIndex);		// calling the remote mehtod used to send the file to the other connected user.
				}
				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(null,"Error Occured during send  the file\n", "OOPs",JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		
		catch(IOException ioe)
		{
			String ACK = "FAILED";
			buffer = ACK.getBytes();
			System.out.println("\n\tFILE NOT FOUND ERROR\n\tTRY Again!!!!!");
		}

    }

/************************************************************************/
} //end of sendfile inner class


class sendsound extends UnicastRemoteObject
{
	String filename=" ";
	byte []buffer = null;
	File file=null;
	int selectedIndex = 0;
	
	public sendsound(int selectedIndex) throws RemoteException
	{
		this.selectedIndex = selectedIndex;				// initilizing the selected user index
		try
		{
			
			file =new File("junk.wav");				// getting the name of the file withe complete path
			filename = file.getName();
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null,"Error Occured in reading the file\n", "OOPs",JOptionPane.ERROR_MESSAGE);
		}
		
		try
		{
			FileInputStream fis = new FileInputStream(file);	// open the file
			if (fis!=null)
			{
				System.out.println("\n\tFILE FOUND Successfully\n\tReady to Send!!!!!");
				buffer = new byte[fis.available()];		// create the buffer of size of file
				fis.read(buffer);		// read the file in bytes and store the bytes in buffer
				try
				{
					bs.sendsound(filename, buffer, selectedIndex);		// calling the remote mehtod used to send the file to the other connected user.
				}
				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(null,"Error Occured during send  the file\n", "OOPs",JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		
		catch(IOException ioe)
		{
			String ACK = "FAILED";
			buffer = ACK.getBytes();
			System.out.println("\n\tFILE NOT FOUND ERROR\n\tTRY Again!!!!!");
		}

    }

/************************************************************************/
} //end of sendfile inner class



} //end of upper class
/************************************************************************/









