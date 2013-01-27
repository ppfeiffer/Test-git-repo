/*

Project : " Java RMI IM Messenger "
Leader
		M Umer
Members:
		Bilawal Sarwar
		Abdul Hadi Khan Khosa
	
							*/
							
/************************************************************************/
import java.rmi.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener.*;
import javax.swing.*;
/************************************************************************/
public class BroadcastClient
{

	private static BroadcastListenerImpl bli;
	public static String login_user = "";
	public static int i=0;
	public static String logout_user = "";
	private BroadcastServer broadcastServer ;	

	Frame frame = null;
	String serverName = "" ; 	
	static String userName="";
    TextField serverNameText = null;
    TextField  user_name_field= null;
	Button sendRequest = null;
	Button quit = null;
    
    Panel user_Name_Panel;
	Panel top = null;
	Panel center = null;
	Panel bottom = null;

	Panel serverNamePanel = null;
	Panel serverPortPanel = null;

	Panel heading_Panel = null;
	Panel ACK_Panel = null;

	Label ACK_label = null;
    Label user_Name_Lable = null;
	String message = " ";
	

/************************************************************************/
	public BroadcastClient()					// broadcastclient constructor
    {
	}	
/************************************************************************/
public void show() 					//method  to call the main window and login page
{
		if ( i == 0)
		{
			try
			{	
				bli = new BroadcastListenerImpl(broadcastServer);  		// calling constructor to open the main window
		
			}
			catch(Exception ex)
			{
				JOptionPane.showMessageDialog(null,"Error Occured\n", "OOPs",JOptionPane.ERROR_MESSAGE);
			}
		}
		
		frame = new Frame();
		frame.setTitle("Login");
		frame.setBounds(300,150,400,450);
		frame.setLayout(new BorderLayout());
		frame.setResizable(false);

		serverNameText = new TextField(20);
		user_name_field = new TextField(20);
		

		top = new Panel();
		Label title = new Label("Login");
		Label heading = new Label("Connect to Server");
        user_Name_Lable= new Label("User Name:     ");
		title.setFont(new Font("",Font.BOLD,20));
		top.add(title);

		serverNamePanel = new Panel();
		user_Name_Panel = new Panel();
		heading_Panel = new Panel();
		heading_Panel.add(heading);

		ACK_Panel = new Panel();
		ACK_label = new Label("                                                 ");
		ACK_label.setFont(new Font("",Font.BOLD,12));
		ACK_label.setForeground(new Color(243,255,35));
		ACK_Panel.add(ACK_label);

		serverNamePanel.add(new Label("Server Address:"));
        serverNamePanel.add(serverNameText);
        user_Name_Panel.add( user_Name_Lable );
		user_Name_Panel.add(user_name_field);
		center = new Panel(new GridLayout(7,0));

		center.add(heading_Panel);
		center.add(serverNamePanel);
        center.add(user_Name_Panel);
        
		center.add(ACK_Panel);
	
		bottom = new Panel();
		sendRequest = new Button("Send Request");
		quit = new Button("Exit");
		
		bottom.add(quit);
		bottom.add(sendRequest);

		top.setBackground(new Color (252,252,150));
		center.setBackground(Color.orange);
		bottom.setBackground(new Color (252,252,150));

		frame.add(top,BorderLayout.NORTH);
		frame.add(center,BorderLayout.CENTER);
		frame.add(bottom,BorderLayout.SOUTH);
		frame.setLocation(100, 100);
		
		frame.addWindowListener
		(
			new WindowAdapter()
			{
            
				public void windowClosing(WindowEvent we)
				{
					exit();
					
				}
			}
		);
		
		frame.setVisible(true);
		
		quit.addActionListener(	new ActionListener()					 // anonymous inner class
		{ 
			public void actionPerformed( ActionEvent event )
			{
				frame.dispose();
				frame.setVisible(false);
			}
		}
);
	sendRequest.addActionListener(	new ActionListener()					// anonymous inner class
	{ 					
		public void actionPerformed( ActionEvent event )
	    {
			userName = user_name_field.getText();
			serverName = serverNameText.getText();
			
			if(serverName.length() == 0)
			{
				JOptionPane.showMessageDialog(null,"Please Enter a Valid Server Name/Address.", "Invalid Server Name/Address",JOptionPane.INFORMATION_MESSAGE);
			}
			
			else if(userName.length() < 5 || userName.length() > 10)
			{
				JOptionPane.showMessageDialog(null,"Please Enter a Valid User Name. Having at least 5 characters.", "Invalid Name",JOptionPane.INFORMATION_MESSAGE);
			}
		
			else
			{
				try
				{	
															// Make rmi URL to BroadcastServer
					String broadcastServerURL;
					broadcastServerURL ="rmi://" + serverName + "/BroadcastServer";
															// Obtain a reference to that remote object
					broadcastServer =(BroadcastServer)Naming.lookup(broadcastServerURL);
				
					boolean check = broadcastServer.addBroadcastListener( bli, userName);
					
					if(!check)
					{
						JOptionPane.showMessageDialog(frame,"Server is full. Please try later...", "OverFlow",JOptionPane.INFORMATION_MESSAGE);
						broadcastServer =null;
						return;
					}
					
					BroadcastListenerImpl.user_Message(broadcastServer, bli, userName) ;					// passing these arguments to get the reference of each connected user.
					i++;	
			
				}
				catch(Exception ex)
				{
			
					JOptionPane.showMessageDialog(null,"Error Occured\nCould Not Connect \nPlease Try Later.", "OOPs",JOptionPane.ERROR_MESSAGE);
			
				}
				
				frame.dispose();
				frame.setVisible(false);
			
			}
		
		}
		
	}
		
);
		
}
/************************************************************************/
	public void exit()
	{	
		frame.dispose();
		frame.setVisible(false);
		
	}
	
/************************************************************************/	
	public static void main(String[] args)
	{
		BroadcastClient client = new BroadcastClient();
		client.show();
		client.i=i+1;
	}
}
/************************************************************************/