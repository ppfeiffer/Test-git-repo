/*

Project : " Java RMI IM Messenger "
Leader	:
		 Muhammad Umer
Members :
		Bilawal Sarwar
		Abdul Hadi Khan Khusa
	
							*/


/************************************************************************/
import java.net.*;
import java.rmi.*;
import javax.swing.*;
/************************************************************************/
public class BroadcastServerApp
{
	public static void main(String args[])
	{
		try
		{
			BroadcastServerImpl bsi;
			bsi = new BroadcastServerImpl();				
			Naming.rebind("BroadcastServer", bsi);
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null,"Binding or Registry Error Occured\n", "OOPs",JOptionPane.ERROR_MESSAGE);
		}
	}
}
/************************************************************************/