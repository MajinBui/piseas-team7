package piseas.network;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;


/**/
public class FishyClient {
	private final static String PASSCODE = "xBE3GnsotxlFSwb9sg7t";
	
	
	public static void main(String args[]) {
    	String tankID = "QWERT"; // To access the saved data, its is at http://www.vanchaubui.com/fish_tanks/<tankID><file_suffix>.html
    	FishyClient.sendMobileXmlData(tankID, "C:\\Users\\Van\\Documents\\fish_test\\input");
    	FishyClient.sendSensorData(tankID, "C:\\Users\\Van\\Documents\\fish_test\\input");
    	FishyClient.sendActionLog(tankID, "C:\\Users\\Van\\Documents\\fish_test\\input");
    	
    	
        FishyClient.updateTemperatureRange(tankID, "21.0", "22.0");
        boolean[][] weekArr = {
        		{ true, false, true, true, true, false, false },
        		{ false, true, false, true, false, false, false } 
        	};

    	int[] hour = { 13, 07};
    	int[] minute = { 50, 30};
        	
    	FishyClient.setFeeding(tankID, weekArr, hour, minute);
        
    	
    	int[] onHr = { 12, 30 };
    	int[] onMin = { 12, 00 };
    	
    	int[] offHr = { 12, 18 };
    	int[] offMin = { 30, 30 };
    	
    	FishyClient.setLighting(tankID, onHr, onMin, offHr, offMin);
    	
    	FishyClient.recieveMobileXmlData(tankID, "C:\\Users\\Van\\Documents\\fish_test\\output");
    	FishyClient.recieveSensorData(tankID, "C:\\Users\\Van\\Documents\\fish_test\\output");
    	FishyClient.recieveActionLog(tankID, "C:\\Users\\Van\\Documents\\fish_test\\output");
	}
	
	
	/**
	 * Prepares to connect the server with the client.  Returns the socket with the server connection
	 * @return Socket the server socket
	 * @throws UnknownHostException if I forget to pay for server.  Panic
	 * @throws IOException if server connection ends abruptly
	 */
	private static Socket connectToServer() throws UnknownHostException, IOException  {
		System.out.println("Connecting to server...");
		InetAddress addr = InetAddress.getByName("198.27.80.59"); //"198.27.80.59" is vanchaubui.com
		Socket clientSocket = new Socket(addr, 4445);
		System.out.println("Connected...");
        return clientSocket;
	}
	
	
	/**
	 * Retrieves the server data of the given tankId saving to the directories given 
	 * @param tankId the TankID of the tank that needs updating
	 * @param mobileXMLSavePath path the the directory to save mobile xml data
	 * @param piXMLSavePath path the the directory to save pi xml data
	 */
	private static void retrieveServerData(String tankId, String transactionToPerform, String parentFilePath, String suffix) {
		Socket clientSocket;
		try {
			clientSocket = connectToServer();
			try {
				
				System.out.println("Retrieving Data");
		        ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
		        ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());

		        outToServer.writeObject(PASSCODE);
		        outToServer.writeObject(transactionToPerform);
		        outToServer.writeObject(tankId);
		        
		        Document document = (Document) inFromServer.readObject();
	
		        String XMLFilePath = String.format("%s/%s%s.xml", parentFilePath, tankId, suffix);
		        
	        	PrintWriter writer = new PrintWriter(XMLFilePath, "UTF-8");
	        	StreamResult result = new StreamResult(writer);
	        	TransformerFactory tFactory = TransformerFactory.newInstance();
			    Transformer transformer = tFactory.newTransformer();
	
			    DOMSource source = new DOMSource(document);
			    transformer.transform(source, result);
			    writer.close();
			    System.out.println("Retrieved Pi Data...");
			    
			    outToServer.writeObject("");
	            return;
			} catch (ClassNotFoundException e) {
		        System.err.println("Client Error: " + e.getMessage());
		        System.err.println("Localized: " + e.getLocalizedMessage());
		        System.err.println("Stack Trace: " + e.getStackTrace());
			} catch (TransformerConfigurationException e) {
				System.err.println("Client Error: " + e.getMessage());
		        System.err.println("Localized: " + e.getLocalizedMessage());
		        System.err.println("Stack Trace: " + e.getStackTrace());
			} catch (TransformerException e) {
				System.err.println("Client Error: " + e.getMessage());
		        System.err.println("Localized: " + e.getLocalizedMessage());
		        System.err.println("Stack Trace: " + e.getStackTrace());
			}  catch (FileNotFoundException e) {
				
			}
		
			clientSocket.close();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.err.println("no data found, maybe xml not initialized yet");
	}
	
	
	/**
	 * Client will retrieve the mobile settings with the given tankId and save to the specified file path. 
	 * @param tankId the TankID of the tank
	 * @param mobileXMLSavePath the parent path where the xml file will be saved
	 */
	public static void recieveMobileXmlData(String tankId, String parentFilePath) {
		System.out.println("recieving mobile data...");
		FishyClient.retrieveServerData(tankId, NetworkTransactionSwitch.DEVICE_RETRIEVE_MOBILE_SETTINGS.name(), parentFilePath, NetworkConstants.FILE_SUFFIX_MOBILE);
	}
	
	
	/**
	 * Client will retrieve the sensor data with the given tankId and save to the specified file path. 
	 * @param tankId the TankID of the tank
	 * @param mobileXMLSavePath the parent path where the xml file will be saved
	 */
	public static void recieveSensorData(String tankId, String parentFilePath) {
		System.out.println("recieving Sensor data...");
		FishyClient.retrieveServerData(tankId, NetworkTransactionSwitch.DEVICE_RETRIEVE_SENSOR_DATA.name(), parentFilePath, NetworkConstants.FILE_SUFFIX_SENSOR);
	}
	
	
	/**
	 * Client will retrieve the action log with the given tankId and save to the specified file path. 
	 * @param tankId the TankID of the tank
	 * @param mobileXMLSavePath the parent path where the xml file will be saved
	 */
	
	public static void recieveActionLog(String tankId, String parentFilePath) {
		System.out.println("recieving ActionLog data...");
		FishyClient.retrieveServerData(tankId, NetworkTransactionSwitch.DEVICE_RETRIEVE_ACTION_LOG.name(), parentFilePath, NetworkConstants.FILE_SUFFIX_LOG);
	}
	
	
	/**
	 * Client will retrieve the manual commands with the given tankId and save to the specified file path. 
	 * @param tankId the TankID of the tank
	 * @param mobileXMLSavePath the parent path where the xml file will be saved
	 */
	@Deprecated
	public static void recieveManualCommands(String tankId, String parentFilePath) {
		System.out.println("recieving ManualCommands data...");
		FishyClient.retrieveServerData(tankId, NetworkTransactionSwitch.DEVICE_RETRIEVE_MANUAL_COMMANDS.name(), parentFilePath, NetworkConstants.FILE_SUFFIX_COMMANDS);
	}
	
	/**
	 * Updates the server data with the xml given according to id 
	 * @param tankId the TankID of the tank that needs updating
	 * @param command directive the server will perform
	 * @param mobileXMLSavePath path to the xml file to send
	 */
	private static void writeToServerData(String tankId, String transactionToPerform, String parentFilePath, String suffix) {
		Socket clientSocket = null;
		try {
			clientSocket = connectToServer();
			try {
		        ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
		        ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());

		        outToServer.writeObject(PASSCODE);
		        outToServer.writeObject(transactionToPerform);
		        outToServer.writeObject(tankId);

		        String fileName = tankId + suffix + ".xml";
		        
		        File file = new File(parentFilePath, fileName);
	        	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        	Document doc = dBuilder.parse(file);
		        
		        outToServer.writeObject(doc);
		        inFromServer.readObject();
		        System.out.println("Data sent");
		        
			} catch (ParserConfigurationException e) {
				System.err.println("Client Error: " + e.getMessage());
		        System.err.println("Localized: " + e.getLocalizedMessage());
		        System.err.println("Stack Trace: " + e.getStackTrace());
			} catch (SAXException e) {
				System.err.println("Client Error: " + e.getMessage());
		        System.err.println("Localized: " + e.getLocalizedMessage());
		        System.err.println("Stack Trace: " + e.getStackTrace());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				
			}
			
			clientSocket.close();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return;
	}
	
	
	/**
	 * Client will send the mobile settings with the given tankId located to the specified file path. 
	 * @param tankId the TankID of the tank
	 * @param mobileXMLSavePath the parent path of the saved data
	 */
	public static void sendMobileXmlData(String tankId, String parentFilePath) {
		System.out.println("Sending mobile settings...");
		FishyClient.writeToServerData(tankId, NetworkTransactionSwitch.SERVER_RETRIEVE_MOBILE_SETTINGS.name(), parentFilePath, NetworkConstants.FILE_SUFFIX_MOBILE);
	}
	
	
	/**
	 * Client will send the sensor data with the given tankId located to the specified file path. 
	 * @param tankId the TankID of the tank
	 * @param mobileXMLSavePath the parent path of the saved data
	 */
	public static void sendSensorData(String tankId, String parentFilePath) {
		System.out.println("Sending sensor data...");
		FishyClient.writeToServerData(tankId, NetworkTransactionSwitch.SERVER_RETRIEVE_SENSOR_DATA.name(), parentFilePath, NetworkConstants.FILE_SUFFIX_SENSOR);
	}
	
	
	/**
	 * Client will send the action log with the given tankId located to the specified file path. 
	 * @param tankId the TankID of the tank
	 * @param mobileXMLSavePath the parent path of the saved data
	 */
	
	public static void sendActionLog(String tankId, String parentFilePath) {
		System.out.println("Sending action log...");
		FishyClient.writeToServerData(tankId, NetworkTransactionSwitch.SERVER_RETRIEVE_ACTION_LOG.name(), parentFilePath, NetworkConstants.FILE_SUFFIX_LOG);
	}
	
	
	/**
	 * Client will send the manual commands with the given tankId located to the specified file path. 
	 * @param tankId the TankID of the tank
	 * @param mobileXMLSavePath the parent path of the saved data
	 */
	@Deprecated
	public static void sendManualCommands(String tankId, String parentFilePath) {
		System.out.println("Sending manual commands...");
		FishyClient.writeToServerData(tankId, NetworkTransactionSwitch.SERVER_RETRIEVE_MANUAL_COMMANDS.name(), parentFilePath, NetworkConstants.FILE_SUFFIX_COMMANDS);
	}
	
	/**
	 * Updates the server data with the xml given according to id 
	 * @param tankId the TankID of the tank that needs updating
	 * @param command directive the server will perform
	 * @param mobileXMLSavePath path to the xml file to send
	 */
	private static void modifyServerData(String tankId, String transactionToPerform, String xpathExpression, String attributeName, String updatedValue) {
		Socket clientSocket = null;
		try {
			clientSocket = connectToServer();
			try {
		        ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
		        ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());

		        outToServer.writeObject(PASSCODE);
		        outToServer.writeObject(transactionToPerform);
		        outToServer.writeObject(tankId);
		        
		        outToServer.writeObject(xpathExpression);
		        outToServer.writeObject(attributeName);
		        outToServer.writeObject(updatedValue);

		        inFromServer.readObject();
		        System.out.println("Data modified");
		        
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			clientSocket.close();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return;
	}
	
	
	private static void modifyMobileXmlData(String tankId, String xpathExpression, String attributeName, String updatedValue) {
		FishyClient.modifyServerData(tankId, NetworkTransactionSwitch.SERVER_MODIFY_MOBILE_SETTINGS.name(), xpathExpression, attributeName, updatedValue);
	}
	
	public static void updateTemperatureRange(String tankId, String min, String max) {
		System.out.println("Modifying Temperature range...");
		modifyMobileXmlData(tankId, "/Piseas/Temperature/details", "min", min);
		modifyMobileXmlData(tankId, "/Piseas/Temperature/details", "max", max);
		System.out.println("Temperature range modified");
	}
	
	public static void updateTemperature(String tankId, String min, String max, String auto) {
		System.out.println("Modifying Temperature...");
		modifyMobileXmlData(tankId, "/Piseas/Temperature/details", "min", min);
		modifyMobileXmlData(tankId, "/Piseas/Temperature/details", "max", max);
		modifyMobileXmlData(tankId, "/Piseas/Temperature/details", "auto", auto);
		System.out.println("Temperature modified");
	}
	
	public static void updatePhRange(String tankId, String min, String max) {
		System.out.println("Modifying Ph range...");
		modifyMobileXmlData(tankId, "/Piseas/PH/details", "min", min);
		modifyMobileXmlData(tankId, "/Piseas/PH/details", "max", max);
		System.out.println("Ph range modified");
	}
	
	public static void updatePh(String tankId, String min, String max, String auto) {
		System.out.println("Modifying Ph range...");
		modifyMobileXmlData(tankId, "/Piseas/PH/details", "min", min);
		modifyMobileXmlData(tankId, "/Piseas/PH/details", "max", max);
		modifyMobileXmlData(tankId, "/Piseas/PH/details", "auto", auto);
		System.out.println("Ph modified");
	}
	
	public static void updateConductivity(String tankId, String min, String max, String auto) {
		System.out.println("Modifying Conductivity range...");
		modifyMobileXmlData(tankId, "/Piseas/Conductivity/details", "min", min);
		modifyMobileXmlData(tankId, "/Piseas/Conductivity/details", "max", max);
		modifyMobileXmlData(tankId, "/Piseas/Conductivity/details", "auto", auto);
		System.out.println("Conductivity modified");
	}
	
	public static void updateTankDetails(String tankId, String password, String size, String description, String type) {
		System.out.println("Modifying tank data...");
		modifyMobileXmlData(tankId, "/Piseas/Tank/details", "id", tankId);
		modifyMobileXmlData(tankId, "/Piseas/Tank/details", "password", password);
		modifyMobileXmlData(tankId, "/Piseas/Tank/details", "size", size);
		modifyMobileXmlData(tankId, "/Piseas/Tank/details", "description", description);
		modifyMobileXmlData(tankId, "/Piseas/Tank/details", "type", type);
		System.out.println("Tank data modified");
	}
	
	public static void updatePump(String tankId, String manualDrain, String manualFill, String auto) {
		System.out.println("Modifying pump data...");
		modifyMobileXmlData(tankId, "/Piseas/Tank/details", "manualDrain", manualDrain);
		modifyMobileXmlData(tankId, "/Piseas/Tank/details", "manualFill", manualFill);
		modifyMobileXmlData(tankId, "/Piseas/Tank/details", "auto", auto);
		System.out.println("Pump data modified");
	}
	
	
	private static void clearXmlData(String tankId, String xpathExpression) {
		Socket clientSocket = null;
		try {
			clientSocket = connectToServer();
			try {
		        ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
		        ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());

		        outToServer.writeObject(PASSCODE);
		        outToServer.writeObject(NetworkTransactionSwitch.SERVER_CLEAR_MOBILE_SETTINGS.name());
		        outToServer.writeObject(tankId);
		        
		        outToServer.writeObject(xpathExpression);

		        inFromServer.readObject();
		        System.out.println("Data modified");
		        
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			clientSocket.close();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return;
	}
	
	private static void appendXmlData(String tankId, String xpathExpression, HashMap<String, String> data) {
		Socket clientSocket = null;
		try {
			clientSocket = connectToServer();
			try {
		        ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
		        ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());

		        outToServer.writeObject(PASSCODE);
		        outToServer.writeObject(NetworkTransactionSwitch.SERVER_APPEND_MOBILE_SETTINGS.name());
		        outToServer.writeObject(tankId);
		        
		        outToServer.writeObject(xpathExpression);
		        outToServer.writeObject(data);

		        inFromServer.readObject();
		        System.out.println("Data modified");
		        
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			clientSocket.close();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return;
	}
	
	public static void setFeeding(String tankId, boolean[][] weekArr, int[] hour, int[] minute) {
		clearXmlData(tankId, "/Piseas/Feed");
		
		for (int i = 0; i < weekArr.length; i++) {
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("Mon", Boolean.toString(weekArr[i][0]));
			data.put("Tue", Boolean.toString(weekArr[i][1]));
			data.put("Wed", Boolean.toString(weekArr[i][2]));
			data.put("Thu", Boolean.toString(weekArr[i][3]));
			data.put("Fri", Boolean.toString(weekArr[i][4]));
			data.put("Sat", Boolean.toString(weekArr[i][5]));
			data.put("Sun", Boolean.toString(weekArr[i][6]));
			data.put("hr", Integer.toString(hour[i]));
			data.put("min", Integer.toString(minute[i]));
			appendXmlData(tankId, "/Piseas/Feed", data);
		}
		System.out.println("Feeding set");
	}
	
	public static void setLighting(String tankId, int[] onHr, int[] onMin, int[] offHr, int[] offMin) {
		clearXmlData(tankId, "/Piseas/Light");
		
		for (int i = 0; i < onHr.length; i++) {
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("onHr", Integer.toString(onHr[i]));
			data.put("onMin", Integer.toString(onMin[i]));
			data.put("offHr", Integer.toString(offHr[i]));
			data.put("offMin", Integer.toString(offMin[i]));
			appendXmlData(tankId, "/Piseas/Light ", data);
		}
		
		System.out.println("Lighting set");
	}
	
}
