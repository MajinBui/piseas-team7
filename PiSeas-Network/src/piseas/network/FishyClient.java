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

			public void printSuccess(boolean rc, String function) {
				System.out.println(function + ((rc)? ": passed": ": failed"));
			}

			@Override
			public void run() {
				printSuccess(FishyClient.sendMobileXmlData(tankID, testInputDir), "sendMobileXmlData");
				printSuccess(FishyClient.sendSensorData(tankID, testInputDir), "sendSensorData");
				printSuccess(FishyClient.sendActionLog(tankID, testInputDir), "sendActionLog");

				printSuccess(FishyClient.updateTemperatureRange(tankID, 21.0f, 22.0f), "updateTemperatureRange");
				printSuccess(FishyClient.setLighting(tankID, onHr, onMin, offHr, offMin, true, false), "setLighting");
				printSuccess(FishyClient.setFeeding(tankID, weekArr, hour, minute, true, false), "setFeeding");

				printSuccess(FishyClient.appendActionLog(tankID, date, desc, type), "appendActionLog");
				printSuccess(FishyClient.checkActionLogUpdated(tankID, "2017-02-20T19:19:19+0500"), "checkActionLogUpdated");
				printSuccess(FishyClient.updateConductivityRange(tankID, 7, 1), "updateConductivityRange");
				printSuccess(FishyClient.updateTemperatureRange(tankID, 7, 1), "updateTemperatureRange");
				printSuccess(FishyClient.updatePhRange(tankID, 1, 7), "updatePhRange");
				printSuccess(FishyClient.updateTankDetailsMobileSettings(tankID, "1234", 20, "blah blah", "tropical"), "updateTankDetailsMobileSettings");
				printSuccess(FishyClient.updatePump(tankID, true, false, false), "updatePump");

				printSuccess(FishyClient.updateSensorSensorData(tankID, conductivity, pHcurrent), "updateSensorSensorData");
				printSuccess(FishyClient.updateFeedSensorData(tankID, 3, feedHr, feedMin), "updateFeedSensorData");
				printSuccess(FishyClient.updateTemperatureSensorData(tankID, -4), "updateTemperatureSensorData");
				printSuccess(FishyClient.updateTankDetailsSensorData(tankID, "1234"), "updateTankDetailsSensorData");

				printSuccess(FishyClient.updateConductivity(tankID, 7, 1, true), "updateConductivity");
				printSuccess(FishyClient.updatePh(tankID, 7, 1, true), "updatePh");
				printSuccess(FishyClient.updateManualCommands(tankID, false, false, false, false), "updateManualCommands");
				printSuccess(FishyClient.updateUpdateMobileSettings(tankID, true, true, true, true, true, false), "updateUpdateMobileSettings");

				printSuccess(FishyClient.retrieveMobileXmlData(tankID, testOutputDir), "retrieveMobileXmlData");
				printSuccess(FishyClient.retrieveSensorData(tankID, testOutputDir), "retrieveSensorData");
				printSuccess(FishyClient.retrieveActionLog(tankID, testOutputDir), "retrieveActionLog");
			}

		};

		Runnable test2 = new Runnable() {
			public void printSuccess(boolean rc, String function) {
				System.out.println(function + ((rc)? ": passed": ": failed"));
			}
			@Override
			public void run() {
				printSuccess(FishyClient.sendMobileXmlData(tankID2, testInputDir), "sendMobileXmlData");
				printSuccess(FishyClient.sendSensorData(tankID2, testInputDir), "sendSensorData");
				printSuccess(FishyClient.sendActionLog(tankID2, testInputDir), "sendActionLog");

				printSuccess(FishyClient.updateTemperatureRange(tankID2, 21.0f, 22.0f), "updateTemperatureRange");
				printSuccess(FishyClient.setLighting(tankID2, onHr, onMin, offHr, offMin, true, false), "setLighting");
				printSuccess(FishyClient.setFeeding(tankID2, weekArr, hour, minute, true, false), "setFeeding");

				printSuccess(FishyClient.appendActionLog(tankID2, date, desc, type), "appendActionLog");
				printSuccess(FishyClient.checkActionLogUpdated(tankID2, "2017-02-20T19:19:19+0500"), "checkActionLogUpdated");
				printSuccess(FishyClient.updateConductivityRange(tankID2, 7, 1), "updateConductivityRange");
				printSuccess(FishyClient.updateTemperatureRange(tankID2, 7, 1), "updateTemperatureRange");
				printSuccess(FishyClient.updatePhRange(tankID2, 1, 7), "updatePhRange");
				printSuccess(FishyClient.updateTankDetailsMobileSettings(tankID2, "1234", 20, "blah blah", "tropical"), "updateTankDetailsMobileSettings");
				printSuccess(FishyClient.updatePump(tankID2, true, false, false), "updatePump");

				printSuccess(FishyClient.updateSensorSensorData(tankID2, conductivity, pHcurrent), "updateSensorSensorData");
				printSuccess(FishyClient.updateFeedSensorData(tankID2, 3, feedHr, feedMin), "updateFeedSensorData");
				printSuccess(FishyClient.updateTemperatureSensorData(tankID2, -4), "updateTemperatureSensorData");
				printSuccess(FishyClient.updateTankDetailsSensorData(tankID2, "1234"), "updateTankDetailsSensorData");

				printSuccess(FishyClient.updateConductivity(tankID2, 7, 1, true), "updateConductivity");
				printSuccess(FishyClient.updatePh(tankID2, 7, 1, true), "updatePh");
				printSuccess(FishyClient.updateManualCommands(tankID2, false, false, false, false), "updateManualCommands");
				printSuccess(FishyClient.updateUpdateMobileSettings(tankID2, true, true, true, true, true, false), "updateUpdateMobileSettings");

				printSuccess(FishyClient.retrieveMobileXmlData(tankID2, testOutputDir), "retrieveMobileXmlData");
				printSuccess(FishyClient.retrieveSensorData(tankID2, testOutputDir), "retrieveSensorData");
				printSuccess(FishyClient.retrieveActionLog(tankID2, testOutputDir), "retrieveActionLog");
			}

		};

		Thread t1 = new Thread(test1);
		Thread t2 = new Thread(test2);

		Thread t3 = new Thread(test1);
		Thread t4 = new Thread(test2);
		System.out.println("Starting tests");
		t1.start();
		t2.start();
		t3.start();
		t4.start();


		try {
			t1.join();
			t2.join();
			t3.join();
			t4.join();
			System.out.println("End tests");			
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

		connection.outToServer.writeObject("");
	}


	/**
	 * Client will retrieve the mobile settings with the given tankId and save to the specified file path. 
	 * @param tankId the TankID of the tank
	 * @param parentFilePath the parent path where the xml file will be saved
	 * @return true if successful, false otherwise
	 */
	public static boolean retrieveMobileXmlData(String tankId, String parentFilePath) {
		FishyConnection fishyConnection;
		boolean rc = true;
		try {
			fishyConnection = new FishyConnection();
			try {
				FishyClient.retrieveServerData(fishyConnection, tankId, NetworkTransactionSwitch.DEVICE_RETRIEVE_MOBILE_SETTINGS.name(), parentFilePath, NetworkConstants.FILE_SUFFIX_MOBILE);
			} catch (TransformerException e) {
				rc = false;
				System.err.println("Unable to save data");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			rc = false;
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			rc = false;
			System.err.println("Client may be out of date");
		}
		return rc;
	}


	/**
	 * Client will retrieve the sensor data with the given tankId and save to the specified file path. 
	 * @param tankId the TankID of the tank
	 * @param parentFilePath the parent path where the xml file will be saved
	 * @return true if successful, false otherwise
	 */
	public static boolean retrieveSensorData(String tankId, String parentFilePath) {
		FishyConnection fishyConnection;
		boolean rc = true;
		try {
			fishyConnection = new FishyConnection();
			try {
				FishyClient.retrieveServerData(fishyConnection, tankId, NetworkTransactionSwitch.DEVICE_RETRIEVE_SENSOR_DATA.name(), parentFilePath, NetworkConstants.FILE_SUFFIX_SENSOR);
			} catch (TransformerException e) {
				rc = false;
				System.err.println("Unable to save data");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			rc = false;
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			rc = false;
			System.err.println("Client may be out of date");
		}
		return rc;
	}


	/**
	 * Client will retrieve the action log with the given tankId and save to the specified file path. 
	 * @param tankId the TankID of the tank
	 * @param parentFilePath the parent path where the xml file will be saved
	 * @return true if successful, false otherwise
	 */
	public static boolean retrieveActionLog(String tankId, String parentFilePath) {
		FishyConnection fishyConnection;
		boolean rc = true;
		try {
			fishyConnection = new FishyConnection();
			try {
				FishyClient.retrieveServerData(fishyConnection, tankId, NetworkTransactionSwitch.DEVICE_RETRIEVE_ACTION_LOG.name(), parentFilePath, NetworkConstants.FILE_SUFFIX_LOG);
			} catch (TransformerException e) {
				rc = false;
				System.err.println("Unable to save data");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			rc = false;
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			rc = false;
			System.err.println("Client may be out of date");
		}
		return rc;
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

	}


	/**
	 * Client will send the mobile settings with the given tankId located to the specified file path. 
	 * @param tankId the TankID of the tank
	 * @param parentFilePath the parent path of the saved data
	 * @return true if successful, false otherwise
	 */
	public static boolean sendMobileXmlData(String tankId, String parentFilePath) {
		FishyConnection fishyConnection;
		boolean rc = true;
		try {
			fishyConnection = new FishyConnection();
			try {
				FishyClient.writeToServerData(fishyConnection, tankId, NetworkTransactionSwitch.SERVER_RETRIEVE_MOBILE_SETTINGS.name(), parentFilePath, NetworkConstants.FILE_SUFFIX_MOBILE);
			} catch (ParserConfigurationException e) {
				rc = false;
				System.err.println("unable to parse xml");
				e.printStackTrace();
			} catch (SAXException e) {
				rc = false;
				System.err.println("unable to parse xml");
			} catch (FileNotFoundException e) {
				rc = false;
				System.err.println("unable to locate xml");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			rc = false;
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			rc = false;
			System.err.println("Client may be out of date");
		}
		return rc;
	}


	/**
	 * Client will send the sensor data with the given tankId located to the specified file path. 
	 * @param tankId the TankID of the tank
	 * @param parentFilePath the parent path of the saved data
	 * @return true if successful, false otherwise
	 */
	public static boolean sendSensorData(String tankId, String parentFilePath) {
		FishyConnection fishyConnection = null;
		boolean rc = true;
		try {
			fishyConnection = new FishyConnection();
			try {
				FishyClient.writeToServerData(fishyConnection, tankId, NetworkTransactionSwitch.SERVER_RETRIEVE_SENSOR_DATA.name(), parentFilePath, NetworkConstants.FILE_SUFFIX_SENSOR);
			} catch (ParserConfigurationException e) {
				rc = false;
				System.err.println("unable to parse xml");
				e.printStackTrace();
			} catch (SAXException e) {
				rc = false;
				System.err.println("unable to parse xml");
			} catch (FileNotFoundException e) {
				rc = false;
				System.err.println("unable to locate xml");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			rc = false;
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			rc = false;
			System.err.println("Client may be out of date");
		}
		return rc;
	}


	/**
	 * Client will send the action log with the given tankId located to the specified file path. 
	 * @param tankId the TankID of the tank
	 * @param parentFilePath the parent path of the saved data
	 * @return true if successful, false otherwise
	 */
	public static boolean sendActionLog(String tankId, String parentFilePath) {
		FishyConnection fishyConnection;
		boolean rc = true;
		try {
			fishyConnection = new FishyConnection();
			try {
				FishyClient.writeToServerData(fishyConnection, tankId, NetworkTransactionSwitch.SERVER_RETRIEVE_ACTION_LOG.name(), parentFilePath, NetworkConstants.FILE_SUFFIX_LOG);
			} catch (ParserConfigurationException e) {
				rc = false;
				System.err.println("unable to parse xml");
				e.printStackTrace();
			} catch (SAXException e) {
				rc = false;
				System.err.println("unable to parse xml");
			} catch (FileNotFoundException e) {
				rc = false;
				System.err.println("unable to locate xml");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			rc = false;
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			rc = false;
			System.err.println("Client may be out of date");
		}
		return rc;
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
	 * @return true if successful, false otherwise
	 */
	public static boolean updateTemperatureRange(String tankId, float min, float max) {
		FishyConnection fishyConnection;
		boolean rc = true;
		try {
			fishyConnection = new FishyConnection();
			try {
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_TEMPERATURE_DETAILS, "min", Float.toString(min));
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_TEMPERATURE_DETAILS, "max", Float.toString(max));
			} catch (Exception e) {
				rc = false;
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			rc = false;
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			rc = false;
			System.err.println("Client may be out of date");
		}
		return rc;
	}

	/**
	 * Updates the mobile settings for temperature
	 * @param tankId the TankID of the tank
	 * @param min the updated min range
	 * @param max the updated max range
	 * @param auto the updated value for auto temperature management
	 * @return true if successful, false otherwise
	 */
	public static boolean updateTemperature(String tankId, float min, float max, boolean auto) {
		FishyConnection fishyConnection;
		boolean rc = true;
		try {
			fishyConnection = new FishyConnection();
			try{
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_TEMPERATURE_DETAILS, "min", Float.toString(min));
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_TEMPERATURE_DETAILS, "max", Float.toString(max));
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_TEMPERATURE_DETAILS, "auto", Boolean.toString(auto));
			} catch (Exception e) {
				rc = false;
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			rc = false;
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			rc = false;
			System.err.println("Client may be out of date");
		}
		return rc;
	}


	/**
	 * Updates the mobile settings for ph range
	 * @param tankId the TankID of the tank
	 * @param min the updated min range
	 * @param max the updated max range
	 * @return true if successful, false otherwise
	 */
	public static boolean updatePhRange(String tankId, float min, float max) {
		FishyConnection fishyConnection;
		boolean rc = true;
		try {
			fishyConnection = new FishyConnection();
			try {
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_PH_DETAILS, "pHmin", Float.toString(min));
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_PH_DETAILS, "pHmax", Float.toString(max));
			} catch (Exception e) {
				rc = false;
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			rc = false;
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			rc = false;
			System.err.println("Client may be out of date");
		}
		return rc;
	}


	/**
	 * Updates the mobile settings for pH
	 * @param tankId the TankID of the tank
	 * @param min the updated min range
	 * @param max the updated max range
	 * @param auto the updated value for auto ph check
	 * @return true if successful, false otherwise
	 */
	public static boolean updatePh(String tankId, float min, float max, boolean auto) {
		FishyConnection fishyConnection;
		boolean rc = true;
		try {
			fishyConnection = new FishyConnection();
			try{
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_PH_DETAILS, "pHmin", Float.toString(min));
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_PH_DETAILS, "pHmax", Float.toString(max));
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_PH_DETAILS, "auto", Boolean.toString(auto));
			} catch (Exception e) {
				rc = false;
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			rc = false;
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			rc = false;
			System.err.println("Client may be out of date");
		}
		return rc;
	}


	/**
	 * Updates the mobile settings for water conductivity range
	 * @param tankId the TankID of the tank
	 * @param min the updated min range
	 * @param max the updated max range
	 * @return true if successful, false otherwise
	 */
	public static boolean updateConductivityRange(String tankId, int min, int max) {
		FishyConnection fishyConnection;
		boolean rc = true;
		try {
			fishyConnection = new FishyConnection();
			try {
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_CONDUCTIVITY_DETAILS, "cMin", Integer.toString(min));
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_CONDUCTIVITY_DETAILS, "cMax", Integer.toString(max));
			} catch (Exception e) {
				rc = false;
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			rc = false;
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			rc = false;
			System.err.println("Client may be out of date");
		}
		return rc;
	}


	/**
	 * Updates the mobile settings for water conductivity
	 * @param tankId the TankID of the tank
	 * @param min the updated min range
	 * @param max the updated max range
	 * @param auto the updated value for auto water conductivity check 
	 * @return true if successful, false otherwise
	 */
	public static boolean  updateConductivity(String tankId, int min, int max, boolean auto) {
		FishyConnection fishyConnection;
		boolean rc = true;
		try {
			fishyConnection = new FishyConnection();
			try {
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_CONDUCTIVITY_DETAILS, "cMin", Integer.toString(min));
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_CONDUCTIVITY_DETAILS, "cMax", Integer.toString(max));
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_CONDUCTIVITY_DETAILS, "auto", Boolean.toString(auto));
			} catch (Exception e) {
				rc = false;
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			rc = false;
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			rc = false;
			System.err.println("Client may be out of date");
		}
		return rc;
	}


	/**
	 * Updates the tank destails of the server
	 * @param tankId the TankID of the tank; Cannot be updated
	 * @param password the updated password/pin for the tank
	 * @param size the size of the tank
	 * @param description descripton of the tank
	 * @param type the type of tank; tropical, fresh
	 * @return true if successful, false otherwise
	 */
	public static boolean updateTankDetailsMobileSettings(String tankId, String password, int size, String description, String type) {
		FishyConnection fishyConnection;
		boolean rc = true;
		try {
			fishyConnection = new FishyConnection();
			try {
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_TANK_DETAILS, "id", tankId);
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_TANK_DETAILS, "password", password);
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_TANK_DETAILS, "size", Integer.toString(size));
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_TANK_DETAILS, "description", description);
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_TANK_DETAILS, "type", type);
			} catch (Exception e) {
				rc = false;
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			rc = false;
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			rc = false;
			System.err.println("Client may be out of date");
		}
		return rc;
	}


	/**
	 * Updates pump data of the server.
	 * @param tankId the TankID of the tank
	 * @param manualDrain the updated value for manual drain
	 * @param manualFill the updated value for manual fill
	 * @param auto the updated value for auto water management
	 * @return true if successful, false otherwise
	 */
	public static boolean updatePump(String tankId, boolean manualDrain, boolean manualFill, boolean auto) {
		FishyConnection fishyConnection;
		boolean rc = true;
		try {
			fishyConnection = new FishyConnection();
			try {
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_PUMP_DETAILS, "manualDrain", Boolean.toString(manualDrain));
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_PUMP_DETAILS, "manualFill", Boolean.toString(manualFill));
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_PUMP_DETAILS, "auto", Boolean.toString(auto));
			} catch (Exception e) {
				rc = false;
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			rc = false;
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			rc = false;
			System.err.println("Client may be out of date");
		}
		return rc;
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
	 * @return true if successful, false otherwise
	 */
	public static boolean setFeeding(String tankId, boolean[][] weekArr, int[] hour, int[] minute, boolean auto, boolean manual) {
		FishyConnection fishyConnection;
		boolean rc = true;
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
				rc = false;
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			rc = false;
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			rc = false;
			System.err.println("Client may be out of date");
		}
		return rc;
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
	 * @return true if successful, false otherwise
	 */
	public static boolean setLighting(String tankId, int[] onHr, int[] onMin, int[] offHr, int[] offMin, boolean autoLight, boolean manualLight) {
		FishyConnection fishyConnection;
		boolean rc = true;
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
				rc = false;
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			rc = false;
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			rc = false;
			System.err.println("Client may be out of date");
		}
		return rc;
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

		return date;
	}

	/**
	 * Compares the given date to the date the server has on the mobile settings xml.
	 * @param tankId the tankId
	 * @param date the date to compare
	 * @return true if file has been updated, false otherwise
	 */
	public static boolean checkMobileSettingsUpdated(String tankId, Date date) {

		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			String fileDate = "";
			try {
				fileDate = retrieveDataDate(fishyConnection, tankId, NetworkTransactionSwitch.DEVICE_CHECK_DATE_MOBILE_SETTINGS.name());
			} catch (Exception e) {
				System.err.println("Function used improperly or server bug; please complain to van");
			}

			fishyConnection.finish(tankId);
			DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			return !fileDate.equals(DATE_FORMAT.format(date));
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
	 * @return true if file has been updated, false otherwise
	 */
	public static boolean checkMobileSettingsUpdated(String tankId, String date) {

		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			String fileDate = "";
			try {
				fileDate = retrieveDataDate(fishyConnection, tankId, NetworkTransactionSwitch.DEVICE_CHECK_DATE_MOBILE_SETTINGS.name());
			} catch (Exception e) {
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			fishyConnection.finish(tankId);

			return !fileDate.equals(date);
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
	 * @return true if file has been updated, false otherwise
	 */
	public static boolean checkSensorDataUpdated(String tankId, Date date) {

		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			String fileDate = "";
			try {
				fileDate = retrieveDataDate(fishyConnection, tankId, NetworkTransactionSwitch.DEVICE_CHECK_DATE_SENSOR_DATA.name());
			} catch (Exception e) {
				System.err.println("Function used improperly or server bug; please complain to van");
			}

			fishyConnection.finish(tankId);

			return !fileDate.equals(date);
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
	 * @return true if file has been updated, false otherwise 
	 */
	public static boolean checkActionLogUpdated(String tankId, String date) {

		FishyConnection fishyConnection;
		try {
			fishyConnection = new FishyConnection();
			String fileDate = "";
			try {
				fileDate = retrieveDataDate(fishyConnection, tankId, NetworkTransactionSwitch.DEVICE_CHECK_DATE_ACTION_LOG.name());
			} catch (Exception e) {
				System.err.println("Function used improperly or server bug; please complain to van");
			}

			fishyConnection.finish(tankId);

			return !fileDate.equals(date);
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
	 * @return true if successful, false otherwise
	 */
	public static boolean updateTankDetailsSensorData(String tankId, String password) {
		FishyConnection fishyConnection;
		boolean rc = true;
		try {
			fishyConnection = new FishyConnection();
			try {
				modifySensorXmlData(fishyConnection, tankId, NetworkConstants.XPATH_TANK, "password", password);
			} catch (Exception e) {
				rc = false;
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			rc = false;
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			rc = false;
			System.err.println("Client may be out of date");
		}
		return rc;
	}


	/**
	 * Update the feed data of the sensor xml.
	 * @param tankId the tankId
	 * @param totalFeeds the total amount of feeds of the day
	 * @param feedHr the updated last fed hour
	 * @param feedMin the updated last fed minute of the hour
	 * @return true if successful, false otherwise
	 */
	public static boolean updateFeedSensorData(String tankId, int totalFeeds, int feedHr, int feedMin) {
		FishyConnection fishyConnection;
		boolean rc = true;
		try {
			fishyConnection = new FishyConnection();
			try {
				modifySensorXmlData(fishyConnection, tankId, NetworkConstants.XPATH_FEED, "totalFeeds", Integer.toString(feedHr));
				modifySensorXmlData(fishyConnection, tankId, NetworkConstants.XPATH_FEED, "feedHr", Integer.toString(feedHr));
				modifySensorXmlData(fishyConnection, tankId, NetworkConstants.XPATH_FEED, "feedMin", Integer.toString(feedMin));
			} catch (Exception e) {
				rc = false;
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			rc = false;
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			rc = false;
			System.err.println("Client may be out of date");
		}
		return rc;
	}


	/**
	 * Update the temperature data of the sensor xml.
	 * @param tankId the tankId
	 * @param currentTemp the updated temperature
	 * @return true if successful, false otherwise
	 */
	public static boolean updateTemperatureSensorData(String tankId, int currentTemp) {
		FishyConnection fishyConnection;
		boolean rc = true;
		try {
			fishyConnection = new FishyConnection();
			try {
				modifySensorXmlData(fishyConnection, tankId, NetworkConstants.XPATH_TEMPERATURE, "currentTemp", Integer.toString(currentTemp));
			} catch (Exception e) {
				rc = false;
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			rc = false;
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			rc = false;
			System.err.println("Client may be out of date");
		}
		return rc;
	}


	/**
	 * Update the sensor data of the sensor xml.
	 * @param tankId the tankId
	 * @param conductivity the updated conductivity value
	 * @param pHcurrent the updated ph value
	 * @return true if successful, false otherwise
	 */
	public static boolean updateSensorSensorData(String tankId, int conductivity, float pHcurrent) {
		FishyConnection fishyConnection;
		boolean rc = true;
		try {
			fishyConnection = new FishyConnection();
			try {
				modifySensorXmlData(fishyConnection, tankId, NetworkConstants.XPATH_SENSOR, "conductivity", Integer.toString(conductivity));
				modifySensorXmlData(fishyConnection, tankId, NetworkConstants.XPATH_SENSOR, "pHcurrent", Float.toString(pHcurrent));
			} catch (Exception e) {
				rc = false;				
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			rc = false;
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			rc = false;
			System.err.println("Client may be out of date");
		}
		return rc;
	}

	/**
	 * Append a single line to the log xml.
	 * @param tankId the tankId
	 * @param date the date of the log
	 * @param desc the desc enum
	 * @param type the type enum
	 * @return true if successful, false otherwise
	 */
	public static boolean appendActionLog(String tankId, String date, String desc, String type) {
		FishyConnection fishyConnection;
		boolean rc = true;
		try {
			fishyConnection = new FishyConnection();
			try {
				HashMap<String, String> data = new HashMap<String, String>();
				data.put("date", date);
				data.put("desc", desc);
				data.put("type", type);
				appendXmlData(fishyConnection, NetworkTransactionSwitch.SERVER_APPEND_ACTION_LOG, tankId, NetworkConstants.XPATH_LOG, data);
			} catch (Exception e) {
				rc = false;
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			rc = false;
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			rc = false;
			System.err.println("Client may be out of date");
		}
		return rc;
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
	 * @return true if successful, false otherwise
	 */
	public static boolean appendActionLog(String tankId, String[] date, String[] desc, String[] type) {
		FishyConnection fishyConnection;
		boolean rc = true;
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
				rc = false;
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			rc = false;
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			rc = false;
			System.err.println("Client may be out of date");
		}
		return rc;
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
	 * @return true if successful, false otherwise
	 */
	public static boolean updateUpdateMobileSettings(String tankId, boolean conductivity, boolean feed, boolean light, boolean pH, boolean pump, boolean temperature ) {
		FishyConnection fishyConnection;
		boolean rc = true;
		try {
			fishyConnection = new FishyConnection();
			try {
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_UPDATE, "conductivity", Boolean.toString(conductivity));
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_UPDATE, "feed", Boolean.toString(feed));
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_UPDATE, "light", Boolean.toString(light));
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_UPDATE, "pH", Boolean.toString(pH));
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_UPDATE, "pump", Boolean.toString(pump));
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_UPDATE, "temperature", Boolean.toString(temperature));
			} catch (Exception e) {
				rc = false;
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			rc = false;
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			rc = false;
			System.err.println("Client may be out of date");
		}
		return rc;
	}

	/**
	 * Updates the manual attribute in the mobile settings xml.
	 * @param tankId tankId the tankId
	 * @param manualFeed true or false if changed
	 * @param manualLight true or false if changed
	 * @param manualDrain true or false if changed
	 * @param manualFill true or false if changed
	 * @return true if successful, false otherwise
	 */
	public static boolean updateManualCommands(String tankId, boolean manualFeed, boolean manualLight, boolean manualDrain, boolean manualFill) {
		FishyConnection fishyConnection;
		boolean rc = true;
		try {
			fishyConnection = new FishyConnection();
			try {
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_FEED, "manual", Boolean.toString(manualFeed));
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_LIGHT, "manual", Boolean.toString(manualLight));
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_PUMP_DETAILS, "manualDrain", Boolean.toString(manualDrain));
				modifyMobileXmlData(fishyConnection, tankId, NetworkConstants.XPATH_PUMP_DETAILS, "manualFill", Boolean.toString(manualFill));
			} catch (Exception e) {
				rc = false;
				System.err.println("Function used improperly or server bug; please complain to van");
			}
			fishyConnection.finish(tankId);
		} catch (IOException e) {
			rc = false;
			System.err.println("Connection to server lost");
		} catch (ClassNotFoundException e) {
			rc = false;
			System.err.println("Client may be out of date");
		}
		return rc;
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
