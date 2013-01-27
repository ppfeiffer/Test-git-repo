import java.rmi.*;
/************************************************************************/
public interface BroadcastServer extends Remote
{

 boolean addBroadcastListener(BroadcastListener bl, String userName) throws RemoteException ;
 void removeBroadcastListener(BroadcastListener bl, String userName) throws RemoteException ;
 public void text_Message (String userMessage) throws RemoteException ;
 public void private_chat(int Index, String Message) throws RemoteException;
 public void open_personal_window (int Index, String myName) throws RemoteException ;
 public void sendFile(String fileName, byte [] buffer, int Index) throws RemoteException ;
 public void sendsound(String fileName, byte [] buffer, int Index) throws RemoteException ;
 }
/************************************************************************/