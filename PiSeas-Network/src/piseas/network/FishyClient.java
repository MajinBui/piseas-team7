package piseas.network;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * The Fishy Client class is designed to transmit, recieve and update the xml
 * data held for the Piseas system.
 * @author Van
 *
 */
public class FishyClient {
	private final static String PASSCODE = "xBE3GnsotxlFSwb9sg7t";
	
	public static void main(String args[]) {
		// tester
		// Initialize data
    	final String tankID = "QWERT";
    	final String tankID2 = "QWER";
    	
    	final String testInputDir = "C:\\Users\\Van\\Documents\\fish_test\\input";
    	final String testOutputDir = "C:\\Users\\Van\\Documents\\fish_test\\output";
    	final boolean[][] weekArr = {
        		{ true, false, true, true, true, false, false },
        		{ false, true, false, true, false, false, false } 
        };

    	final int[] hour = { 13, 07 };
    	final int[] minute = { 50, 30 };
    	
    	final int[] onHr = { 12, 30 };
    	final int[] onMin = { 12, 00 };
    	
    	final int[] offHr = { 12, 18 };
    	final int[] offMin = { 30, 30 };
    	
    	final String[] date = {
    			"2017-02-20T19:19:19+0500",
    			"2017-02-20T20:19:19+0500",
    			"2017-02-20T21:19:19+0500",
    			"2017-02-20T22:19:19+0500"
    	};
    	
    	final String[] desc = {
    			"FEEDACTIVE",
    			"TEMPRANGE",
    			"MANFEEDING",
    			"FEEDACTIVE"
    	};
    	
    	final String[] type = {
    			"ACT",
    			"NOT",
    			"MANACT",
    			"ACT"
    	};
    	
    	final int conductivity = 2;
    	final float pHcurrent = 10f;
    	
    	final int feedHr = 12;
    	final int feedMin = 30;
    	Runnable test1 = new Runnable() {
    		
    		@Override
    		public void run() {
    			FishyClient.sendMobileXmlData(tankID, testInputDir);
    	    	FishyClient.sendSensorData(tankID, testInputDir);
    	    	FishyClient.sendActionLog(tankID, testInputDir);
    	    	
    	        FishyClient.updateTemperatureRange(tankID, 21.0f, 22.0f);
    	    	FishyClient.setLighting(tankID, onHr, onMin, offHr, offMin, true, false);
    	    	FishyClient.setFeeding(tankID, weekArr, hour, minute, true, false);
    	    	
    	    	FishyClient.appendActionLog(tankID, date, desc, type);
    	    	FishyClient.checkActionLogUpdated(tankID, "2017-02-20T19:19:19+0500");
    	    	FishyClient.updateConductivityRange(tankID, 7, 1);
    	    	FishyClient.updateTemperatureRange(tankID, 7, 1);
    	    	FishyClient.updatePhRange(tankID, 1, 7);
    	    	FishyClient.updateTankDetailsMobileSettings(tankID, "1234", 20, "blah blah", "tropical");
    	    	FishyClient.updatePump(tankID, true, false, false);
    	    	
    	    	FishyClient.updateSensorSensorData(tankID, conductivity, pHcurrent);
    	    	FishyClient.updateFeedSensorData(tankID, 3, feedHr, feedMin);
    	    	FishyClient.updateTemperatureSensorData(tankID, -4);
    	    	FishyClient.updateTankDetailsSensorData(tankID, "1234");
    	    	
    	    	FishyClient.updateConductivity(tankID, 7, 1, true); 
    	    	FishyClient.updatePh(tankID, 7, 1, true);
    	    	FishyClient.updateManualCommands(tankID, false, false, false, false);
    	    	FishyClient.updateUpdateMobileSettings(tankID, true, true, true, true, true, false);
    	    	
    	    	FishyClient.retrieveMobileXmlData(tankID, testOutputDir);
    	    	FishyClient.retrieveSensorData(tankID, testOutputDir);
    	    	FishyClient.retrieveActionLog(tankID, testOutputDir);
    		}
    		
    	};
    	
    	Runnable test2 = new Runnable() {
    		
    		@Override
    		public void run() {
    			FishyClient.sendMobileXmlData(tankID2, testInputDir);
    	    	FishyClient.sendSensorData(tankID2, testInputDir);
    	    	FishyClient.sendActionLog(tankID2, testInputDir);
    	    	
    	        FishyClient.updateTemperatureRange(tankID2, 21.0f, 22.0f);
    	    	FishyClient.setLighting(tankID2, onHr, onMin, offHr, offMin, true, false);
    	    	FishyClient.setFeeding(tankID2, weekArr, hour, minute, true, false);
    	    	
    	    	FishyClient.appendActionLog(tankID2, date, desc, type);
    	    	FishyClient.checkActionLogUpdated(tankID2, "2017-02-20T19:19:19+0500");
    	    	FishyClient.updateConductivityRange(tankID2, 7, 1);
    	    	FishyClient.updateTemperatureRange(tankID2, 7, 1);
    	    	FishyClient.updatePhRange(tankID2, 1, 7);
    	    	FishyClient.updateTankDetailsMobileSettings(tankID2, "1234", 20, "blah blah", "tropical");
    	    	FishyClient.updatePump(tankID2, true, false, false);
    	    	
    	    	FishyClient.updateSensorSensorData(tankID2, conductivity, pHcurrent);
    	    	FishyClient.updateFeedSensorData(tankID2, 3, feedHr, feedMin);
    	    	FishyClient.updateTemperatureSensorData(tankID2, -4);
    	    	FishyClient.updateTankDetailsSensorData(tankID2, "1234");
    	    	
    	    	FishyClient.updateConductivity(tankID2, 7, 1, true); 
    	    	FishyClient.updatePh(tankID2, 7, 1, true);
    	    	FishyClient.updateManualCommands(tankID2, false, false, false, false);
    	    	FishyClient.updateUpdateMobileSettings(tankID2, true, true, true, true, true, false);
    	    	
    	    	FishyClient.retrieveMobileXmlData(tankID2, testOutputDir);
    	    	FishyClient.retrieveSensorData(tankID2, testOutputDir);
    	    	FishyClient.retrieveActionLog(tankID2, testOutputDir);
    		}
    		
    	};
    	
    	Thread t1 = new Thread(test1);
    	Thread t2 = new Thread(test2);
    	
    	Thread t3 = new Thread(test1);
    	Thread t4 = new Thread(test2);
    	
    	t1.start();
    	t2.start();
    	t3.start();
    	t4.start();
    	
    	
    	try {
			t1.join();
			t2.join();
			t3.join();
			t4.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Retrieves the server data of the given tankId saving to the directories given 
	 * @param tankId the TankID of the tank that needs updating
	 * @param transactionToPerform type of xml transaction
	 * @param parentFilePath path the the directory to save xml data
	 * @param suffix the file suffix
	 * @throws IOException if connection interruped 
	 * @throws ClassNotFoundException if server or client is outdated if server or client is outdated 
	 * @throws TransformerException 
	 */
	private static void retrieveServerData(FishyConnection connection, String tankId, String transactionToPerform, String parentFilePath, String suffix) throws IOException, ClassNotFoundException, TransformerException {
        connection.outToServer.writeObject(transactionToPerform);
        connection.outToServer.writeObject(tankId);
        
        Document document = (Document) connection.inFromServer.readObject();

        String XMLFilePath = String.format("%s/%s%s.xml", parentFilePath, tankId, suffix);
        
    	PrintWriter writer = new PrintWriter(XMLFilePath, "UTF-8");
    	StreamResult result = new StreamResult(writer);
    	TransformerFactory tFactory = TransformerFactory.newInstance();
	    Transformer transformer = tFactory.newTransformer();

	    DOMSource source = new DOMSource(document);
	    transformer.transform(source, result);
	    writer.close();
	    System.out.println("Retrieved Pi Data...");
	    
	    connection.outToServer.writeObject("");
	}
	
	
	/**
	 * Client will retrieve the mobile settings with the given tankId and save to the specified file path. 
	 * @param tankId the TankID of the tank
	 * @param parentFilePath the parent path where the xml file will be saved
	 */
	public static void retrieveMobileXmlData(String tankId, String parentFilePath) {
		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			System.out.println("recieving mobile data...");
			try {
				FishyClient.retrieveServerData(fishyConnection, tankId, NetworkTransactionSwitch.DEVICE_RETRIEVE_MOBILE_SETTINGS.name(), parentFilePath, NetworkConstants.FILE_SUFFIX_MOBILE);
			} catch (TransformerException e) {
				System.err.println("Unable to save data");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
	}
	
	
	/**
	 * Client will retrieve the sensor data with the given tankId and save to the specified file path. 
	 * @param tankId the TankID of the tank
	 * @param parentFilePath the parent path where the xml file will be saved
	 */
	public static void retrieveSensorData(String tankId, String parentFilePath) {
		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			System.out.println("recieving Sensor data...");
			try {
				FishyClient.retrieveServerData(fishyConnection, tankId, NetworkTransactionSwitch.DEVICE_RETRIEVE_SENSOR_DATA.name(), parentFilePath, NetworkConstants.FILE_SUFFIX_SENSOR);
			} catch (TransformerException e) {
				System.err.println("Unable to save data");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
	}
	
	
	/**
	 * Client will retrieve the action log with the given tankId and save to the specified file path. 
	 * @param tankId the TankID of the tank
	 * @param parentFilePath the parent path where the xml file will be saved
	 */
	
	public static void retrieveActionLog(String tankId, String parentFilePath) {
		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			System.out.println("recieving ActionLog data...");
			try {
				FishyClient.retrieveServerData(fishyConnection, tankId, NetworkTransactionSwitch.DEVICE_RETRIEVE_ACTION_LOG.name(), parentFilePath, NetworkConstants.FILE_SUFFIX_LOG);
			} catch (TransformerException e) {
				System.err.println("Unable to save data");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
	}
	
	
	/**
	 * Updates the server data with the xml given according to id 
	 * @param tankId the TankID of the tank that needs updating
	 * @param command directive the server will perform
	 * @param parentFilePath path to the xml file to send
	 * @throws ParserConfigurationException 
	 * @throws IOException if connection interruped 
	 * @throws SAXException if XML data is outdated
	 * @throws ClassNotFoundException if server or client is outdated if server or client is outdated 
	 */
	private static void writeToServerData(FishyConnection connection, String tankId, String transactionToPerform, String parentFilePath, String suffix) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException {
		
		connection.outToServer.writeObject(transactionToPerform);
		connection.outToServer.writeObject(tankId);

        String fileName = tankId + suffix + ".xml";
        
        File file = new File(parentFilePath, fileName);
    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    	Document doc = dBuilder.parse(file);
        
    	connection.outToServer.writeObject(doc);
    	connection.inFromServer.readObject();
        System.out.println("Data sent");
        
	}
	
	
	/**
	 * Client will send the mobile settings with the given tankId located to the specified file path. 
	 * @param tankId the TankID of the tank
	 * @param parentFilePath the parent path of the saved data
	 */
	public static void sendMobileXmlData(String tankId, String parentFilePath) {
		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			System.out.println("Sending mobile settings...");
			try {
				FishyClient.writeToServerData(fishyConnection, tankId, NetworkTransactionSwitch.SERVER_RETRIEVE_MOBILE_SETTINGS.name(), parentFilePath, NetworkConstants.FILE_SUFFIX_MOBILE);
			} catch (ParserConfigurationException e) {
				System.err.println("unable to parse xml");
				e.printStackTrace();
			} catch (SAXException e) {
				System.err.println("unable to parse xml");
			} catch (FileNotFoundException e) {
				System.err.println("unable to locate xml");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
	}
	
	
	/**
	 * Client will send the sensor data with the given tankId located to the specified file path. 
	 * @param tankId the TankID of the tank
	 * @param parentFilePath the parent path of the saved data
	 */
	public static void sendSensorData(String tankId, String parentFilePath) {
		FishyConnection fishyConnection = null;
		try {
			System.out.println("Sending sensor data...");
			fishyConnection = new FishyConnection();
			try {
				FishyClient.writeToServerData(fishyConnection, tankId, NetworkTransactionSwitch.SERVER_RETRIEVE_SENSOR_DATA.name(), parentFilePath, NetworkConstants.FILE_SUFFIX_SENSOR);
			} catch (ParserConfigurationException e) {
				System.err.println("unable to parse xml");
				e.printStackTrace();
			} catch (SAXException e) {
				System.err.println("unable to parse xml");
			} catch (FileNotFoundException e) {
				System.err.println("unable to locate xml");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
	}
	
	
	/**
	 * Client will send the action log with the given tankId located to the specified file path. 
	 * @param tankId the TankID of the tank
	 * @param parentFilePath the parent path of the saved data
	 */
	
	public static void sendActionLog(String tankId, String parentFilePath) {
		FishyConnection fishyConnection;
		try {
			System.out.println("Sending action log...");
			fishyConnection = new FishyConnection();
			try {
				FishyClient.writeToServerData(fishyConnection, tankId, NetworkTransactionSwitch.SERVER_RETRIEVE_ACTION_LOG.name(), parentFilePath, NetworkConstants.FILE_SUFFIX_LOG);
			} catch (ParserConfigurationException e) {
				System.err.println("unable to parse xml");
				e.printStackTrace();
			} catch (SAXException e) {
				System.err.println("unable to parse xml");
			} catch (FileNotFoundException e) {
				System.err.println("unable to locate xml");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
	}
	
	/**
	 * Updates the server data with the xml given according to id 
	 * @param tankId the TankID of the tank that needs updating
	 * @param command directive the server will perform
	 * @param parentFilePath path to the xml file to send
	 * @throws IOException if connection interruped 
	 * @throws ClassNotFoundException if server or client is outdated if server or client is outdated 
	 */
	private static void modifyServerData(FishyConnection connection, String tankId, String transactionToPerform, String xpathExpression, String attributeName, String updatedValue) throws IOException, ClassNotFoundException {

        connection.outToServer.writeObject(transactionToPerform);
        connection.outToServer.writeObject(tankId);
        
        connection.outToServer.writeObject(xpathExpression);
        connection.outToServer.writeObject(attributeName);
        connection.outToServer.writeObject(updatedValue);

        connection.inFromServer.readObject();
        System.out.println("Data modified");
	}
	
	/**
	 * Modifies attribute values of the given value found with the xpath expression
	 * @param connection the current connection to the server
	 * @param tankId the TankID of the tank
	 * @param xpathExpression the xpath expression used to find the correct element
	 * @param attributeName the attribute being searched for
	 * @param updatedValue the new value for the given attribute
	 * @throws ClassNotFoundException if server or client is outdated if server or client is outdated
	 * @throws IOException if connection interruped
	 */
	private static void modifyMobileXmlData(FishyConnection connection, String tankId, String xpathExpression, String attributeName, String updatedValue) throws ClassNotFoundException, IOException {
		FishyClient.modifyServerData(connection, tankId, NetworkTransactionSwitch.SERVER_MODIFY_MOBILE_SETTINGS.name(), xpathExpression, attributeName, updatedValue);
	
	
	}
	
	/**
	 * Modifies attribute values of the given value found with the xpath expression
	 * @param connection the current connection to the server
	 * @param tankId the TankID of the tank
	 * @param xpathExpression the xpath expression used to find the correct element
	 * @param attributeName the attribute being searched for
	 * @param updatedValue the new value for the given attribute
	 * @throws ClassNotFoundException if server or client is outdated if server or client is outdated
	 * @throws IOException if connection interruped
	 */
	private static void modifySensorXmlData(FishyConnection connection, String tankId, String xpathExpression, String attributeName, String updatedValue) throws ClassNotFoundException, IOException {
		FishyClient.modifyServerData(connection, tankId, NetworkTransactionSwitch.SERVER_MODIFY_SENSOR_DATA.name(), xpathExpression, attributeName, updatedValue);
	}
	
	
	
	/**
	 * Modifies attribute values of the given value found with the xpath expression
	 * @param connection the current connection to the server
	 * @param tankId the TankID of the tank
	 * @param xpathExpression the xpath expression used to find the correct element
	 * @param attributeName the attribute being searched for
	 * @param updatedValue the new value for the given attribute
	 * @throws ClassNotFoundException if server or client is outdated if server or client is outdated
	 * @throws IOException if connection interruped
	 */
	@SuppressWarnings("unused")
	private static void modifyActionLogXmlData(FishyConnection connection, String tankId, String xpathExpression, String attributeName, String updatedValue) throws ClassNotFoundException, IOException {
		FishyClient.modifyServerData(connection, tankId, NetworkTransactionSwitch.SERVER_MODIFY_ACTION_LOG.name(), xpathExpression, attributeName, updatedValue);
	}
	
	
	
	/**
	 * Updates the mobile settings for temperature range.
	 * @param tankId the TankID of the tank
	 * @param min the updated min range
	 * @param max the updated max range
	 */
	public static void updateTemperatureRange(String tankId, float min, float max) {
		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			System.out.println("Modifying Temperature range...");
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_TEMPERATURE_DETAILS, "min", Float.toString(min));
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_TEMPERATURE_DETAILS, "max", Float.toString(max));
			System.out.println("Temperature range modified");
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
	}
	
	/**
	 * Updates the mobile settings for temperature
	 * @param tankId the TankID of the tank
	 * @param min the updated min range
	 * @param max the updated max range
	 * @param auto the updated value for auto temperature management
	 */
	public static void updateTemperature(String tankId, float min, float max, boolean auto) {
		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			System.out.println("Modifying Temperature...");
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_TEMPERATURE_DETAILS, "min", Float.toString(min));
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_TEMPERATURE_DETAILS, "max", Float.toString(max));
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_TEMPERATURE_DETAILS, "auto", Boolean.toString(auto));
			System.out.println("Temperature modified");
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
	}
	
	
	/**
	 * Updates the mobile settings for ph range
	 * @param tankId the TankID of the tank
	 * @param min the updated min range
	 * @param max the updated max range
	 */
	public static void updatePhRange(String tankId, float min, float max) {
		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			System.out.println("Modifying Ph range...");
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_PH_DETAILS, "pHmin", Float.toString(min));
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_PH_DETAILS, "pHmax", Float.toString(max));
			System.out.println("Ph range modified");
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
	}
	
	
	/**
	 * Updates the mobile settings for pH
	 * @param tankId the TankID of the tank
	 * @param min the updated min range
	 * @param max the updated max range
	 * @param auto the updated value for auto ph check
	 */
	public static void updatePh(String tankId, float min, float max, boolean auto) {
		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			System.out.println("Modifying Ph range...");
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_PH_DETAILS, "pHmin", Float.toString(min));
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_PH_DETAILS, "pHmax", Float.toString(max));
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_PH_DETAILS, "auto", Boolean.toString(auto));
			System.out.println("Ph modified");
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
	}
	
	
	/**
	 * Updates the mobile settings for water conductivity range
	 * @param tankId the TankID of the tank
	 * @param min the updated min range
	 * @param max the updated max range
	 */
	public static void updateConductivityRange(String tankId, int min, int max) {
		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			System.out.println("Modifying Ph range...");
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_CONDUCTIVITY_DETAILS, "cMin", Integer.toString(min));
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_CONDUCTIVITY_DETAILS, "cMax", Integer.toString(max));
			System.out.println("Conductivity range modified");
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
	}
	
	
	/**
	 * Updates the mobile settings for water conductivity
	 * @param tankId the TankID of the tank
	 * @param min the updated min range
	 * @param max the updated max range
	 * @param auto the updated value for auto water conductivity check 
	 */
	public static void updateConductivity(String tankId, int min, int max, boolean auto) {
		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			System.out.println("Modifying Conductivity range...");
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_CONDUCTIVITY_DETAILS, "cMin", Integer.toString(min));
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_CONDUCTIVITY_DETAILS, "cMax", Integer.toString(max));
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_CONDUCTIVITY_DETAILS, "auto", Boolean.toString(auto));
			System.out.println("Conductivity modified");
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
	}
	
	
	/**
	 * Updates the tank destails of the server
	 * @param tankId the TankID of the tank; Cannot be updated
	 * @param password the updated password/pin for the tank
	 * @param size the size of the tank
	 * @param description descripton of the tank
	 * @param type the type of tank; tropical, fresh
	 */
	public static void updateTankDetailsMobileSettings(String tankId, String password, int size, String description, String type) {
		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			System.out.println("Modifying tank data...");
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_TANK_DETAILS, "id", tankId);
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_TANK_DETAILS, "password", password);
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_TANK_DETAILS, "size", Integer.toString(size));
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_TANK_DETAILS, "description", description);
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_TANK_DETAILS, "type", type);
			System.out.println("Tank data modified");
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
	}
	
	
	/**
	 * Updates pump data of the server.
	 * @param tankId the TankID of the tank
	 * @param manualDrain the updated value for manual drain
	 * @param manualFill the updated value for manual fill
	 * @param auto the updated value for auto water management
	 */
	public static void updatePump(String tankId, boolean manualDrain, boolean manualFill, boolean auto) {
		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			System.out.println("Modifying pump data...");
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_PUMP_DETAILS, "manualDrain", Boolean.toString(manualDrain));
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_PUMP_DETAILS, "manualFill", Boolean.toString(manualFill));
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_PUMP_DETAILS, "auto", Boolean.toString(auto));
			System.out.println("Pump data modified");
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
	}
	
	/**
	 * Clears the contents of the xml found with the xpath given
	 * @param connection the current connection to the server
	 * @param tankId the TankID of the tank
	 * @param xpathExpression the xpath expression used to find the correct element
	 * @throws IOException if connection interruped
	 * @throws ClassNotFoundException if server or client is outdated if server or client is outdated
	 */
	private static void clearXmlData(FishyConnection connection, NetworkTransactionSwitch transactionSwitch,  String tankId, String xpathExpression) throws IOException, ClassNotFoundException {
		
		connection.outToServer.writeObject(transactionSwitch.name());
		connection.outToServer.writeObject(tankId);
        
		connection.outToServer.writeObject(xpathExpression);

		connection.inFromServer.readObject();
        System.out.println("Data modified");
	}
	
	
	/**
	 * Appends xml data to the contents of the element found using the given xpath.
	 * @param connection the current connection to the server
	 * @param tankId the TankID of the tank
	 * @param xpathExpression the xpath expression used to find the correct element
	 * @param data a HashMap of strings to easily send data to the server
	 * @throws IOException if connection interruped
	 * @throws ClassNotFoundException if server or client is outdated if server or client is outdated
	 */
	private static void appendXmlData(FishyConnection connection, NetworkTransactionSwitch transactionSwitch, String tankId, String xpathExpression, HashMap<String, String> data) throws IOException, ClassNotFoundException {

        connection.outToServer.writeObject(transactionSwitch.name());
        connection.outToServer.writeObject(tankId);
        
        connection.outToServer.writeObject(xpathExpression);
        connection.outToServer.writeObject(data);

        connection.inFromServer.readObject();
        System.out.println("Data modified");
	}
	
	
	/**
	 * Sets the Feed xml data on the server using the given arguments.  Each row of a week is
	 * associated to an row of hour and minute.
	 * 
	 * Example of how to use the following function:
	 * <pre>
	 * {@code
	 * final boolean[][] weekArr = {
     *  		{ true, false, true, true, true, false, false },
     *  		{ false, true, false, true, false, false, false } 
     * };
	 *
     * final int[] hour = { 13, 07 };
     * final int[] minute = { 50, 30 };
	 *
	 * }
	 * 
	 * FishyClient.setFeeding(tankID2, weekArr, hour, minute, offMin, true, false);
	 * </pre>
	 * 
	 * This will update the xml to contain the following information:
	 * 
	 * <pre>
	 * {@code
	 * <Feed auto="true" manual="false" schedules="2">
	 * 		<details Fri="true" Mon="true" Sat="false" Sun="false" Thu="true" Tue="false" Wed="true" hr="13" min="50"/>
	 * 		<details Fri="false" Mon="false" Sat="false" Sun="false" Thu="true" Tue="true" Wed="false" hr="7" min="30"/>
	 * </Feed>
	 * }
	 * </pre>
	 * 
	 * @param tankId the TankID of the tank
	 * @param weekArr days of the week to feed
	 * @param hour hour of the day to feed
	 * @param minute minute of the hour to feed
	 * @param auto set automatic feeding
	 * @param manual send manual feeding
	 */
	public static void setFeeding(String tankId, boolean[][] weekArr, int[] hour, int[] minute, boolean auto, boolean manual) {
		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			clearXmlData(fishyConnection, NetworkTransactionSwitch.SERVER_CLEAR_MOBILE_SETTINGS, tankId, NetworkConstants.XPATH_FEED);
			try {
				// Add schedule
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
					appendXmlData(fishyConnection, NetworkTransactionSwitch.SERVER_APPEND_MOBILE_SETTINGS, tankId, NetworkConstants.XPATH_FEED, data);
				}
				// adjust schedule number
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_FEED, "schedules", Integer.toString(weekArr.length));
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_FEED, "auto", Boolean.toString(auto));
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_FEED, "manual", Boolean.toString(manual));
			}
			catch (Exception e) {
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			System.out.println("Feeding set");
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
	}
	
	
	
	/**
	 * Sets the light xml data on the server using the given arguments.  Each element in the arrays
	 * are associated to each other 1 to 1 for each details row.
	 * 
	 * Example of how to use the following function:
	 * <pre>
	 * {@code
	 * 
	 * final int[] onHr = { 12, 30 };
	 * final int[] onMin = { 12, 00 };
	 * 
	 * final int[] offHr = { 12, 18 };
	 * final int[] offMin = { 30, 30 };
	 * }
	 * 
	 * FishyClient.setLighting(tankID2, onHr, onMin, offHr, offMin, true, false);
	 * </pre>
	 * 
	 * This will update the xml to contain the following information:
	 * 
	 * <pre>
	 * {@code
	 * <Light auto="true" manual="false" schedules="2">
	 * 		<details offHr="12" offMin="30" onHr="12" onMin="12"/>
	 * 		<details offHr="18" offMin="30" onHr="30" onMin="0"/>
	 * </Light>
	 * }
	 * </pre>
	 * 
	 * 
	 * @param tankId the TankID of the tank that needs updating
	 * @param onHr an array of hours to turn on
	 * @param onMin an array of mins to turn on
	 * @param offHr an array of hours to turn off
	 * @param offMin an array of mins to turn off
	 * @param autoLight boolean to save
	 * @param manualLight boolean to save
	 */
	public static void setLighting(String tankId, int[] onHr, int[] onMin, int[] offHr, int[] offMin, boolean autoLight, boolean manualLight) {
		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			try {
				clearXmlData(fishyConnection, NetworkTransactionSwitch.SERVER_CLEAR_MOBILE_SETTINGS, tankId, NetworkConstants.XPATH_LIGHT);
				// Add light schedule
				for (int i = 0; i < onHr.length; i++) {
					HashMap<String, String> data = new HashMap<String, String>();
					data.put("onHr", Integer.toString(onHr[i]));
					data.put("onMin", Integer.toString(onMin[i]));
					data.put("offHr", Integer.toString(offHr[i]));
					data.put("offMin", Integer.toString(offMin[i]));
					appendXmlData(fishyConnection, NetworkTransactionSwitch.SERVER_APPEND_MOBILE_SETTINGS, tankId, NetworkConstants.XPATH_LIGHT, data);
				}
				
				// Update attributes
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_LIGHT, "schedules", Integer.toString(onHr.length));
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_LIGHT, "auto", Boolean.toString(autoLight));
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_LIGHT, "manual", Boolean.toString(manualLight));
			} catch (Exception e) {
				System.err.println("Function used improperly or server bug; please complain to van");
				e.printStackTrace();
			}
			System.out.println("Lighting set");
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
	}
	
	/**
	 * Updates the server data with the xml given according to id 
	 * @param tankId the TankID of the tank that needs updating
	 * @param transactionToPerform directive the server will perform
	 * @throws IOException if connection interruped 
	 * @throws ClassNotFoundException if server or client is outdated if server or client is outdated 
	 */
	private static String retrieveDataDate(FishyConnection connection, String tankId, String transactionToPerform) throws IOException, ClassNotFoundException {

        connection.outToServer.writeObject(transactionToPerform);
        connection.outToServer.writeObject(tankId);
        
        String date = (String) connection.inFromServer.readObject();

        connection.inFromServer.readObject();
        System.out.println("date retrieved");
        
        return date;
	}
	
	/**
	 * Compares the given date to the date the server has on the mobile settings xml.
	 * @param tankId the tankId
	 * @param date the date to compare
	 * @return true if the dates are the same, false otherwise
	 */
	public static boolean checkMobileSettingsUpdated(String tankId, Date date) {
		
		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			String fileDate = "";
			try {
				System.out.println("getting last modified date...");
				fileDate = retrieveDataDate(fishyConnection, tankId, NetworkTransactionSwitch.DEVICE_CHECK_DATE_MOBILE_SETTINGS.name());
				System.out.println("last modified date returned");
			} catch (Exception e) {
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			
			fishyConnection.finish(tankId);
			DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			return fileDate.equals(DATE_FORMAT.format(date));
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
		
		return false;
	}
	
	
	/**
	 * Compares the given date to the date the server has on the mobile settings xml.
	 * @param tankId the tankId
	 * @param date the date to compare
	 * @return true if the dates are the same, false otherwise
	 */
	public static boolean checkMobileSettingsUpdated(String tankId, String date) {
		
		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			String fileDate = "";
			try {
				System.out.println("getting last modified date...");
				fileDate = retrieveDataDate(fishyConnection, tankId, NetworkTransactionSwitch.DEVICE_CHECK_DATE_MOBILE_SETTINGS.name());
				System.out.println("last modified date returned");
			} catch (Exception e) {
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			fishyConnection.finish(tankId);
			
			return fileDate.equals(date);
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
		
		return false;
	}
	
	/**
	 * Compares the given date to the date the server has on the sensor data xml.
	 * @param tankId the tankId
	 * @param date the date to compare
	 * @return true if the dates are the same, false otherwise
	 */
	public static boolean checkSensorDataUpdated(String tankId, Date date) {
		
		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			String fileDate = "";
			try {
				System.out.println("getting last modified date...");
				fileDate = retrieveDataDate(fishyConnection, tankId, NetworkTransactionSwitch.DEVICE_CHECK_DATE_SENSOR_DATA.name());
				System.out.println("last modified date returned");
			} catch (Exception e) {
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			
			fishyConnection.finish(tankId);
			
			return fileDate.equals(date);
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
		
		return false;
	}
	
	
	/**
	 * Compares the given date to the date the server has on the action log xml.
	 * @param tankId the tankId
	 * @param date the date to compare
	 * @return true if the dates are the same, false otherwise 
	 */
	public static boolean checkActionLogUpdated(String tankId, String date) {
		
		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			String fileDate = "";
			try {
				System.out.println("getting last modified date...");
				fileDate = retrieveDataDate(fishyConnection, tankId, NetworkTransactionSwitch.DEVICE_CHECK_DATE_ACTION_LOG.name());
				System.out.println("last modified date returned");
			} catch (Exception e) {
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			
			fishyConnection.finish(tankId);
			
			return fileDate.equals(date);
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
		
		return false;
	}
	
	
	/**
	 * Update the tank details of the sensor data xml.
	 * @param tankId the tankId
	 * @param password the updated pin
	 */
	public static void updateTankDetailsSensorData(String tankId, String password) {
		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			try {
				System.out.println("Modifying Sensor Tank Data...");
				modifySensorXmlData(fishyConnection, tankId, NetworkConstants.XPATH_TANK, "password", password);
				System.out.println("Sensor Tank Data modified");
			} catch (Exception e) {
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
	}
	
	
	/**
	 * Update the feed data of the sensor xml.
	 * @param tankId the tankId
	 * @param feedHr the updated last fed hour
	 * @param feedMin the updated last fed minute of the hour
	 */
	public static void updateFeedSensorData(String tankId, int totalFeeds, int feedHr, int feedMin) {
		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			try {
				System.out.println("Modifying Sensor Feed Data...");
				modifySensorXmlData(fishyConnection, tankId, NetworkConstants.XPATH_FEED, "totalFeeds", Integer.toString(feedHr));
				modifySensorXmlData(fishyConnection, tankId, NetworkConstants.XPATH_FEED, "feedHr", Integer.toString(feedHr));
				modifySensorXmlData(fishyConnection, tankId, NetworkConstants.XPATH_FEED, "feedMin", Integer.toString(feedMin));
				System.out.println("Sensor Tank Feed modified");
			} catch (Exception e) {
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
	}
	
	
	/**
	 * Update the temperature data of the sensor xml.
	 * @param tankId the tankId
	 * @param currentTemp the updated temperature
	 */
	public static void updateTemperatureSensorData(String tankId, int currentTemp) {
		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			try {
				System.out.println("Modifying Sensor Temperature Data...");
				modifySensorXmlData(fishyConnection, tankId, NetworkConstants.XPATH_TEMPERATURE, "currentTemp", Integer.toString(currentTemp));
				System.out.println("Sensor Tank Temperature modified");
			} catch (Exception e) {
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
	}
	
	
	/**
	 * Update the sensor data of the sensor xml.
	 * @param tankId the tankId
	 * @param conductivity the updated conductivity value
	 * @param pHcurrent the updated ph value
	 */
	public static void updateSensorSensorData(String tankId, int conductivity, float pHcurrent) {
		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			try {
				System.out.println("Modifying Sensor Sensor Data...");
				modifySensorXmlData(fishyConnection, tankId, NetworkConstants.XPATH_SENSOR, "conductivity", Integer.toString(conductivity));
				modifySensorXmlData(fishyConnection, tankId, NetworkConstants.XPATH_SENSOR, "pHcurrent", Float.toString(pHcurrent));
				System.out.println("Sensor Tank Temperature modified");
			} catch (Exception e) {
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
	}
	
	/**
	 * Append a single line to the log xml.
	 * @param tankId the tankId
	 * @param date the date of the log
	 * @param desc the desc enum
	 * @param type the type enum
	 */
	public static void appendActionLog(String tankId, String date, String desc, String type) {
		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			try {
				HashMap<String, String> data = new HashMap<String, String>();
				data.put("date", date);
				data.put("desc", desc);
				data.put("type", type);
				appendXmlData(fishyConnection, NetworkTransactionSwitch.SERVER_APPEND_ACTION_LOG, tankId, NetworkConstants.XPATH_LOG, data);
			} catch (Exception e) {
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			System.out.println("Action Log Appended");
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
	}
	
	
	/**
	 * Append a list of logs to the sensor xml.
	 * 
	 * 	 * Example of how to use the following function:
	 * <pre>
	 * {@code
	 * String[] desc = {
	 * 		"FEEDACTIVE",
	 * 		"TEMPRANGE",
	 * 		"MANFEEDING",
	 * 		"FEEDACTIVE"
	 * };
	 * 
	 * String[] type = {
	 * 		"ACT",
	 * 		"NOT",
	 * 		"MANACT",
	 * 		"ACT"
	 * };
	 * }
	 * 
	 * FishyClient.appendActionLog(tankID2, weekArr, hour, minute, offMin, true, false);
	 * </pre>
	 * 
	 * This will update the xml to contain the following information:
	 * 
	 * <pre>
	 * {@code
	 * <Logs>
	 * 		<Log date="2017-02-20T19:19:19+0500" desc="FEEDACTIVE" type="ACT"/>
	 * 		<Log date="2017-02-20T20:19:19+0500" desc="TEMPRANGE" type="NOT"/>
	 * 		<Log date="2017-02-20T21:19:19+0500" desc="MANFEEDING" type="MANACT"/>
	 * </Logs>
	 * }
	 * </pre>
	 * 
	 * 
	 * @param tankId the tankId
	 * @param date the date of the log
	 * @param desc the desc enum
	 * @param type the type enum
	 */
	public static void appendActionLog(String tankId, String[] date, String[] desc, String[] type) {
		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			try {
				for (int i = 0; i < date.length; i++) {
					HashMap<String, String> data = new HashMap<String, String>();
					data.put("date", date[i]);
					data.put("desc", desc[i]);
					data.put("type", type[i]);
					appendXmlData(fishyConnection, NetworkTransactionSwitch.SERVER_APPEND_ACTION_LOG, tankId, NetworkConstants.XPATH_LOG, data);
				}
			} catch (Exception e) {
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			System.out.println("Action Log Appended");
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
	}
	
	/**
	 * Set the updated field to the specified value.  For the Piseas device to mark
	 * updated and processed changes.
	 * @param tankId the tankId
	 * @param conductivity true or false if changed
	 * @param feed true or false if changed
	 * @param light true or false if changed
	 * @param pH true or false if changed
	 * @param pump true or false if changed
	 * @param temperature true or false if changed
	 */
	public static void updateUpdateMobileSettings(String tankId, boolean conductivity, boolean feed, boolean light, boolean pH, boolean pump, boolean temperature ) {
		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			System.out.println("Modifying Update Data...");
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_UPDATE, "conductivity", Boolean.toString(conductivity));
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_UPDATE, "feed", Boolean.toString(feed));
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_UPDATE, "light", Boolean.toString(light));
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_UPDATE, "pH", Boolean.toString(pH));
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_UPDATE, "pump", Boolean.toString(pump));
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_UPDATE, "temperature", Boolean.toString(temperature));
			System.out.println("Update modified");
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
	}
	
	/**
	 * Updates the manual attribute in the mobile settings xml.
	 * @param tankId tankId the tankId
	 * @param manualFeed true or false if changed
	 * @param manualLight true or false if changed
	 * @param manualDrain true or false if changed
	 * @param manualFill true or false if changed
	 */
	public static void updateManualCommands(String tankId, boolean manualFeed, boolean manualLight, boolean manualDrain, boolean manualFill) {
		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			System.out.println("Modifying manual Data...");
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_FEED, "manual", Boolean.toString(manualFeed));
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_LIGHT, "manual", Boolean.toString(manualLight));
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_PUMP_DETAILS, "manualDrain", Boolean.toString(manualDrain));
			modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_PUMP_DETAILS, "manualFill", Boolean.toString(manualFill));
			System.out.println("Manual modified");
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			System.err.println("Client may be out of date");
		}
	}
	
	/**
	 * Inner class to store a connection in progress.  Every public function MUST create a FishyConnection
	 * and then pass it to every subsequent private method call. This is vital to ensure the server keeps correct
	 * queue order.  FishyConnection must always end with a finish method call to signal to the server that the client's
	 * place in queue can be cleared.
	 * 
	 * @author Van
	 *
	 */
	private static class FishyConnection {
		public Socket clientSocket;
		public ObjectOutputStream outToServer;
		public ObjectInputStream inFromServer;

		/**
		 * Opens a connection to the server
		 * @throws IOException if connection is lost
		 */
		public FishyConnection() throws IOException {
			InetAddress addr = InetAddress.getByName("198.27.80.59"); //"198.27.80.59" is vanchaubui.com
			clientSocket = new Socket(addr, 4445);
			outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
			inFromServer = new ObjectInputStream(clientSocket.getInputStream());
			outToServer.writeObject(PASSCODE);
		}

		/**
		 * Closes the connection to the server
		 * @param tankId the id of connection that is in queue
		 * @throws IOException if connection is lost
		 * @throws ClassNotFoundException if server or client is outdated if server or client is out of date
		 */
		public void finish(String tankId) throws IOException, ClassNotFoundException {
			outToServer.writeObject(NetworkTransactionSwitch.DEVICE_END_TRANSACTION.name());
			outToServer.writeObject(tankId);

			inFromServer.readObject();
			clientSocket.close();
		}
	}
	
	private FishyClient() {}
}
