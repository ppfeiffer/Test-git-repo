/************************************************************************/
import java.io.*;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.BoxLayout;
/************************************************************************/
public class BroadcastServerImpl extends UnicastRemoteObject implements BroadcastServer, Runnable,ActionListener
{

	String SERVER = "" ; 	// the host who receives the messages
	public static String user_Name="";
    Frame frame;
    TextField serverNameText = null;
    public static TextField  user_name= null;
    TextArea user_list1=null;
	JButton sendRequest = null;
	JButton quit = null;
    JLabel menu=null;
    JLabel j_name=null;
	String userName=""+"\n";
	JLabel j_online_value;   			 // label for online users
    final int max_users = 100;
    final String max=new String(""+max_users);
    String online="";
/************************************************************************/
	private Vector listeners;
	public ArrayList users_List;
	private Thread thread;
	String server_LoginName="";
	String user_LoginName="";
	String user_LogoutName = "";
	String message = "";
	String privateMessage = " ";
	String fileName;
	String connect=": is Connected\n";  //connect users list
	String disconnect= ": is Disconnected\n"; //disconnect user list
	byte [] buffer;
	BroadcastListener privat ;
	private int privateIndex = 0;
/************************************************************************/
	public void private_chat(int Index, String Message) throws RemoteException
	{
		privateIndex = Index;
		privateMessage = Message; 
		
		privat = (BroadcastListener)listeners.get(privateIndex);
 
		try
		{
			privat.receivePrivate( Message );
		}
		catch(Exception ex)
		{
			System.out.print("Eoor ... "+ex);
		}
 
	}
/************************************************************************/
public void open_personal_window (int Index, String myName) throws RemoteException
{
	int ownIndex = users_List.indexOf( myName ) ;
	int otherIndex = Index ;
	privat = (BroadcastListener)listeners.get(otherIndex);
	
	try
	{
		privat.privateWindow(ownIndex, myName);
	}
	catch(Exception ex)
	{
		System.out.print("Eoor ... "+ex);
	}

}
/************************************************************************/
 public void sendFile(String fileName, byte [] buffer, int Index) throws RemoteException 
 {
	try
	{
		this.fileName = fileName;
		this.buffer = buffer;
		privat = (BroadcastListener)listeners.get(Index);
		try
		{
			privat.receiveFile(fileName, buffer);
		}
		catch(Exception ex)
		{
			System.out.println("could not send file......" +fileName);
		}
	}
	catch(Exception ex)
	{
		System.out.print("Eoor could not send file ... "+ex);
	}
 }
/************************************************************************/
public void sendsound(String fileName, byte [] buffer, int Index) throws RemoteException 
 {
	try
	{
		this.fileName = fileName;
		this.buffer = buffer;
		privat = (BroadcastListener)listeners.get(Index);
		try
		{
			privat.receivesound(fileName, buffer);
		}
		catch(Exception ex)
		{
			System.out.println("could not send file......" +fileName);
		}
	}
	catch(Exception ex)
	{
		System.out.print("Eoor could not send file ... "+ex);
	}
 }
/************************************************************************/
 public void text_Message (String userMessage) throws RemoteException
 {
	message =  userMessage ;
	sendMessage(message);
 }
/************************************************************************/
public BroadcastServerImpl() throws RemoteException    // initialization
{
    users_List= new ArrayList(max_users);
	listeners = new Vector();
	thread = new Thread(this);
	thread.start();
	online=new String(""+users_List.size());
    frame = new Frame("Server Version 1.0");
    Panel uper=new Panel(new GridLayout(4,0));
     quit=new JButton("Quit");  //button quit
	 quit.setIcon(new javax.swing.ImageIcon("images\\EXIT.PNG")); 
     quit.setSize(20,20);                //setting size of button quit
     j_name=new JLabel("User's List");  //label for displaying user list
	 j_name.setIcon(new javax.swing.ImageIcon("images\\user1.jpg"));
     j_name.setFont(new Font("",Font.BOLD,15));
     user_list1=new TextArea();          //text area for displaying names of user
	 user_list1.append(userName+"");   // here the names will be displayed in server reocrd*************************
     //user_list1.setBackground(Co);  //setting backgroung color of user list
     user_list1.setForeground(Color.BLUE);  //setting foreground color of usr list
     user_list1.setEditable(false);
     user_list1.setSize(100,300);
     menu =new JLabel("SERVER");         //label for dsiplaying server oon top
	 menu.setIcon(new javax.swing.ImageIcon("images\\1.gif"));
	 
     menu.setFont(new Font("",Font.BOLD,20));  //setting font of top label
     Panel p_top=new Panel();                    //panel for top menu
     Panel p_exit=new Panel();                   // panel for bottom buttons
     Panel p_status=new Panel(new GridLayout(2,6));           //panel for displaying status of users
     //p_exit.setBounds(100,100,50,50);
     JLabel j_max=new JLabel("Max Users");              // label for showing maximum no. of users
	 j_max.setIcon(new javax.swing.ImageIcon("images\\Mes.png"));
     j_max.setForeground(Color.red);
     JLabel j_max_value=new JLabel(max);     //label for displaying maximum users  ********************************
     JLabel j_online=new JLabel("Online Users");              // label for showing online no. of users
	 j_online.setIcon(new javax.swing.ImageIcon("images\\images111.jpg"));
     j_online.setForeground(Color.magenta);
     //j_online.setBackground(Color.RED);
     j_online_value=new JLabel(online);     //lable for displaying online users *****************************
     JLabel a=new JLabel("");
     JLabel b=new JLabel("");
     JLabel c=new JLabel("");
     JLabel d=new JLabel("");
     p_status.add(a);
     p_status.add(j_max);
     p_status.add(j_max_value);
     p_status.add(b);
     p_status.add(c);
     p_status.add(j_online);
     p_status.add(j_online_value);
     p_status.add(d);
     p_top.add(menu);   //adding menu to panel top
     p_exit.add(quit);  //adding quit button to panel p_exit
/************************************************************************/     
	quit.addActionListener(	new ActionListener()
	{                                 // inner class for quit button
		public void actionPerformed( ActionEvent event )
		{
		    int exit = JOptionPane.showConfirmDialog(null,"This Will Exit Server", "Are you sure?",1);
            if (exit == JOptionPane.YES_OPTION) {
            System.exit(0);}
		
		}
	}
);		 //end of action
/************************************************************************/    
     uper.add(j_name);
     uper.add(user_list1);
     uper.add(p_status,BorderLayout.CENTER);
 
     frame.add(p_top,BorderLayout.PAGE_START);
     frame.add(uper,BorderLayout.CENTER);
     frame.add(p_exit,BorderLayout.SOUTH);
     frame.setBackground(Color.orange);
     frame.setSize(500,550);
     frame.setResizable(false);
     frame.setVisible(true);
/************************************************************************/	 
    frame.addWindowListener( new WindowAdapter()
	{
		public void windowClosing(WindowEvent we)
		{
			System.exit(0);

		}
	}
);

}
/************************************************************************/
public boolean addBroadcastListener(BroadcastListener bl, String userName) throws RemoteException
{
	
	if( users_List.size() == max_users )
	{
		return false;
		
	}
	user_LoginName = userName;
	users_List.add(user_LoginName);
	int ownIndex = users_List.indexOf( userName ) ;					// adding the name in the ArrayList
	listeners.add(ownIndex, bl);			// adding the each login user at the spacified index
	online=new String(""+users_List.size());
	j_online_value.setText(online);     //lable for displaying online users *****************************
	send_Names(users_List);					// local method called to send list of updated names to all nodes.
	user_list1.append(userName+connect);  //displaying user that are connected
	
	return true;
}
/************************************************************************/
public void removeBroadcastListener(BroadcastListener bl, String userName) throws RemoteException
{
	user_LogoutName = userName;
	int ownIndex = users_List.indexOf( userName ) ;
	users_List.remove(userName);
	listeners.remove(ownIndex);
	send_Names(users_List);
	online = new String(""+users_List.size());
    j_online_value.setText(online);     //lable for displaying online users *****************************
	user_list1.append(userName+disconnect);
	
}
/************************************************************************/
public void run()
{
}
/************************************************************************/
public void send_Names(ArrayList user_List)
{
	
	Vector v1, v2;					// v1 = a clone of the listeners vector, v2 = a new vector
	synchronized(this)
	{
		v1 = (Vector)listeners.clone();
	}
	
	v2 = new Vector();

	Enumeration e1 = v1.elements();					// Broadcast the names to these nodes.
	
	while(e1.hasMoreElements())
	{	
		BroadcastListener bl;
		bl = (BroadcastListener)e1.nextElement();
	
		try
		{	
			try
			{
				bl.receiveBroadcastName( user_List );
			}
			catch(Exception ex)
			{
				v2.addElement(bl);
			}
						
		}
		
		catch(Exception ex)
		{
			v2.addElement(bl);
		}
	}

	Enumeration e2 = v2.elements();
	
	while(e2.hasMoreElements())
	{
		listeners.removeElement(e2.nextElement()); 				// Remove listeners that caused exceptions
	}

}
/************************************************************************/
protected void sendMessage(String message)
{

	Vector v1, v2;
	
	synchronized(this)
	{
		v1 = (Vector)listeners.clone();
	}
	v2 = new Vector();
	Enumeration e1 = v1.elements();
	
	while(e1.hasMoreElements())
	{	
		BroadcastListener bl;
		bl = (BroadcastListener)e1.nextElement();
		try
		{
			bl.receiveBroadcast(message);					// Broadcast the names to these nodes.
		}
		catch(Exception ex)
		{
			v2.addElement(bl);
		}
	}

	Enumeration e2 = v2.elements();
	
	while(e2.hasMoreElements())
	{
		listeners.removeElement(e2.nextElement());
	}
}
/************************************************************************/
	public void actionPerformed(ActionEvent e) 
	{
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
/************************************************************************/
