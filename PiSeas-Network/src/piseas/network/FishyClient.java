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
    	String tankID = "QWERT"; // To access the saved data, its is at vanchaubui.com/fish_tanks/<tankID>.html
    	System.out.println(NetworkTransactionSwitch.SERVER_RETRIEVE_SENSOR_DATA.name());
    	//	how to receive data
    	FishyClient.sendSensorData(tankID, "C:\\Users\\Van\\Documents\\fish_test\\input");
    	FishyClient.sendMobileXmlData(tankID, "C:\\Users\\Van\\Documents\\fish_test\\input");
    	FishyClient.sendActionLog(tankID, "C:\\Users\\Van\\Documents\\fish_test\\input");
    	FishyClient.sendManualCommands(tankID, "C:\\Users\\Van\\Documents\\fish_test\\input");
        
        FishyClient.recieveMobileXmlData(tankID, "C:\\Users\\Van\\Documents\\fish_test\\output\\");
        FishyClient.recieveSensorData(tankID, "C:\\Users\\Van\\Documents\\fish_test\\output\\");
        FishyClient.recieveActionLog(tankID, "C:\\Users\\Van\\Documents\\fish_test\\output\\");
        FishyClient.recieveManualCommands(tankID, "C:\\Users\\Van\\Documents\\fish_test\\output\\");
	}
	/**
	 * Connects to the server
	 * @return Socket to the server
	 * @throws UnknownHostException Happens when I forget to pay for server.  Panic
	 * @throws IOException Panic
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
	 * Updates the mobile device server data with the xml given according to id 
	 * @param tankId the TankID of the tank that needs updating
	 * @param mobileXMLSavePath path to the xml file to send
	 */
	public static void recieveMobileXmlData(String tankId, String parentFilePath) {
		System.out.println("Sending mobile data...");
		FishyClient.retrieveServerData(tankId, NetworkTransactionSwitch.DEVICE_RETRIEVE_MOBILE_SETTINGS.name(), parentFilePath, NetworkConstants.FILE_SUFFIX_MOBILE);
	}
	
	
	/**
	 * Updates the pi device server data with the xml given according to id 
	 * @param tankId the TankID of the tank that needs updating
	 * @param mobileXMLSavePath path to the xml file to send
	 */
	public static void recieveSensorData(String tankId, String parentFilePath) {
		System.out.println("Sending Sensor data...");
		FishyClient.retrieveServerData(tankId, NetworkTransactionSwitch.DEVICE_RETRIEVE_SENSOR_DATA.name(), parentFilePath, NetworkConstants.FILE_SUFFIX_SENSOR);
	}
	
	
	/**
	 * Updates the pi device server data with the xml given according to id 
	 * @param tankId the TankID of the tank that needs updating
	 * @param mobileXMLSavePath path to the xml file to send
	 */
	public static void recieveActionLog(String tankId, String parentFilePath) {
		System.out.println("Sending ActionLog data...");
		FishyClient.retrieveServerData(tankId, NetworkTransactionSwitch.DEVICE_RETRIEVE_ACTION_LOG.name(), parentFilePath, NetworkConstants.FILE_SUFFIX_LOG);
	}
	
	
	/**
	 * Updates the pi device server data with the xml given according to id 
	 * @param tankId the TankID of the tank that needs updating
	 * @param mobileXMLSavePath path to the xml file to send
	 */
	public static void recieveManualCommands(String tankId, String parentFilePath) {
		System.out.println("Sending ManualCommands data...");
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
	 * Updates the mobile device server data with the xml given according to id 
	 * @param tankId the TankID of the tank that needs updating
	 * @param mobileXMLSavePath path to the xml file to send
	 */
	public static void sendMobileXmlData(String tankId, String parentFilePath) {
		System.out.println("Sending mobile data...");
		FishyClient.writeToServerData(tankId, NetworkTransactionSwitch.SERVER_RETRIEVE_MOBILE_SETTINGS.name(), parentFilePath, NetworkConstants.FILE_SUFFIX_MOBILE);
	}
	
	
	/**
	 * Updates the pi device server data with the xml given according to id 
	 * @param tankId the TankID of the tank that needs updating
	 * @param mobileXMLSavePath path to the xml file to send
	 */
	public static void sendSensorData(String tankId, String parentFilePath) {
		System.out.println("Sending pi data...");
		FishyClient.writeToServerData(tankId, NetworkTransactionSwitch.SERVER_RETRIEVE_SENSOR_DATA.name(), parentFilePath, NetworkConstants.FILE_SUFFIX_SENSOR);
	}
	
	
	/**
	 * Updates the pi device server data with the xml given according to id 
	 * @param tankId the TankID of the tank that needs updating
	 * @param mobileXMLSavePath path to the xml file to send
	 */
	public static void sendActionLog(String tankId, String parentFilePath) {
		System.out.println("Sending pi data...");
		FishyClient.writeToServerData(tankId, NetworkTransactionSwitch.SERVER_RETRIEVE_ACTION_LOG.name(), parentFilePath, NetworkConstants.FILE_SUFFIX_LOG);
	}
	
	
	/**
	 * Updates the pi device server data with the xml given according to id 
	 * @param tankId the TankID of the tank that needs updating
	 * @param mobileXMLSavePath path to the xml file to send
	 */
	public static void sendManualCommands(String tankId, String parentFilePath) {
		System.out.println("Sending pi data...");
		FishyClient.writeToServerData(tankId, NetworkTransactionSwitch.SERVER_RETRIEVE_MANUAL_COMMANDS.name(), parentFilePath, NetworkConstants.FILE_SUFFIX_COMMANDS);
	}
	
	
}
