import java.rmi.*;
import java.util.*;
/************************************************************************/
public interface BroadcastListener extends Remote
{
public void receiveBroadcast(String message) throws RemoteException; // remote method to receive each broadcast method
public void receiveBroadcastName(ArrayList user_List) throws RemoteException; // method to update the user list.
public void receivePrivate(String message) throws RemoteException; // method to receive private method
public void privateWindow(int myIndex, String myName) throws RemoteException;
public void receiveFile(String fileName, byte [] buffer) throws RemoteException;
public void receivesound(String fileName, byte [] buffer) throws RemoteException;
}
/************************************************************************/