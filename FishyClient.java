package fishyClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
/**/
public class FishyClient {
	public static void main(String args[]) {
    	String tankID = "QWERT"; // To access the saved data, its is at vanchaubui.com/fish_tanks/<tankID>.html

    	//	how to recieve data
//        HashMap<String, String> dataList = FishyClient.retrieveServerData(tankID);
//        Iterator it = dataList.entrySet().iterator();
//        while (it.hasNext()) {
//        	HashMap.Entry pair = (HashMap.Entry)it.next();
//            System.out.println(pair.getKey() + " = " + pair.getValue());
//            it.remove(); // avoids a ConcurrentModificationException
//        }
        //	how to send server data

    	HashMap<String, String> dataList = new HashMap<String, String>();
        dataList.put("tankId", tankID);
        dataList.put("ph Level", "5");
        dataList.put("water conducitivty level", "30000");
        dataList.put("temperature", "23 degrees");
        dataList.put("feeding time", "7:00pm");
        dataList.put("Light turn on", "4:00pm");
        dataList.put("Light turn off", "7:00am");
        FishyClient.writeToServerData(tankID, dataList);
        
	}
	private static Socket connectToServer() throws UnknownHostException, IOException  {
		InetAddress addr = InetAddress.getByName("198.27.80.59"); //"198.27.80.59" is vanchaubui.com
		Socket clientSocket = new Socket(addr, 4444);
        return clientSocket;
	}
	public static HashMap<String, String> retrieveServerData(String tankId) {
		try {
			Socket clientSocket = connectToServer();
	        ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
	        ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
	        String command = "r";
	        outToServer.writeObject(command);
	        outToServer.writeObject(tankId);
        	HashMap<String, String> inList = (HashMap<String, String>)inFromServer.readObject();
            return inList;
		} catch (UnknownHostException e) {
	        System.err.println("Client Error: " + e.getMessage());
	        System.err.println("Localized: " + e.getLocalizedMessage());
	        System.err.println("Stack Trace: " + e.getStackTrace());
		} catch (IOException e) {
	        System.err.println("Client Error: " + e.getMessage());
	        System.err.println("Localized: " + e.getLocalizedMessage());
	        System.err.println("Stack Trace: " + e.getStackTrace());
		} catch (ClassNotFoundException e) {
	        System.err.println("Client Error: " + e.getMessage());
	        System.err.println("Localized: " + e.getLocalizedMessage());
	        System.err.println("Stack Trace: " + e.getStackTrace());
		}
		System.err.println("no data found!");
		return new HashMap<String, String>();
	}
	
	public static void writeToServerData(String tankId, HashMap<String, String> dataHashMap) {
		try {
			Socket clientSocket = connectToServer();
	        ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
	        ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
	        String command = "w";
	        outToServer.writeObject(command);
	        outToServer.writeObject(tankId);
	        outToServer.writeObject(dataHashMap);
	        inFromServer.readObject();
	        return;
		} catch (UnknownHostException e) {
	        System.err.println("Client Error: " + e.getMessage());
	        System.err.println("Localized: " + e.getLocalizedMessage());
	        System.err.println("Stack Trace: " + e.getStackTrace());
		} catch (IOException e) {
	        System.err.println("Client Error: " + e.getMessage());
	        System.err.println("Localized: " + e.getLocalizedMessage());
	        System.err.println("Stack Trace: " + e.getStackTrace());
		} catch (ClassNotFoundException e) {
	        System.err.println("Client Error: " + e.getMessage());
	        System.err.println("Localized: " + e.getLocalizedMessage());
	        System.err.println("Stack Trace: " + e.getStackTrace());
		}
		return;
	}
}
