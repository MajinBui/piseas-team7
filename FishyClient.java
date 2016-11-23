package fishyClient;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
/**/
public class FishyClient {
	public static void main(String args[]) {
	    try {
	        // Create the socket
	    	InetAddress addr = InetAddress.getByName("198.27.80.59"); //"198.27.80.59" is vanchaubui.com
	        Socket clientSocket = new Socket(addr, 4444);
	        // Create the input & output streams to the server
	        ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
	        ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
	        // Set to either w or r to write or read data
	        // set readOrWrite to "n" to overwrite data
	        String readOrWrite = "w"; 
	        outToServer.writeObject(readOrWrite);
	        String tankID = "QWERT"; // To access the saved data, its is at vanchaubui.com/fish_tanks/<tankID>.html
	        outToServer.writeObject(tankID);
	        if (readOrWrite.equals("w") || readOrWrite.equals("n")) {
		        /* Choose the values to send over here */
		        HashMap<String, String> dataList = new HashMap<>();
		        dataList.put("tankId", tankID);
		        dataList.put("ph Level", "7");
		        dataList.put("water conducitivty level", "30000");
		        dataList.put("temperature", "23 degrees");
		        dataList.put("feeding time", "7:00pm");
		        dataList.put("Light turn on", "4:00pm");
		        dataList.put("Light turn off", "7:00am");
		        outToServer.writeObject(dataList);
	        } else if (readOrWrite.equals("r")) {
	        	HashMap<String, String> inList = (HashMap<String, String>)inFromServer.readObject();
	        	ArrayList<String> keyList = new ArrayList<>();
                keyList.addAll(inList.keySet());
                Collections.sort(keyList);
	        	for (String key : keyList) {
	        		System.out.println(String.format("key: %s   value: %s", key, inList.get(key)));
	        	}
	        }

	        outToServer.flush();
	        clientSocket.close();
	
	    } catch (Exception e) {
	        System.err.println("Client Error: " + e.getMessage());
	        System.err.println("Localized: " + e.getLocalizedMessage());
	        System.err.println("Stack Trace: " + e.getStackTrace());
	    }
	}
}
