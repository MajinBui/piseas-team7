package piseas.network;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
// TODO: set up server transaction for when the server first starts

public class FishyServerRunnable implements Runnable {
	private static final long THREAD_WAIT_TIME = 100;
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	private static final Calendar CALENDAR = Calendar.getInstance();
	private static final String LOG_FORMAT = "%-25s %-10s: %s\n";  // displays as the following:  <date> <tankId>: <log message>
	private static final String PASSCODE = "xBE3GnsotxlFSwb9sg7t";
	private static final String DATE_XPATH_EXPRESSION = "/Piseas/Date/@date";
	private static final String UPDATE_XPATH_EXPRESSION = "/Piseas/Update/details";
	private static final String[] UPDATE_SECTIONS = {"feed", "light", "temperature", "pump", "pH", "conductivity"};
	private static HashMap<String, Queue<FishyServerRunnable>> TANK_IDS = new HashMap<String, Queue<FishyServerRunnable>>();

	private ObjectInputStream inFromClient;
	private ObjectOutputStream outToClient;
	private Socket clientSocket;
	private SERVER_STATE serverState;
	private Transformer transformer;

	/**
	 * Creates a thread to handle a server transaction.  Takes a client socket to recieve and send data.
	 * @param clientSocket
	 */
	public FishyServerRunnable( Socket clientSocket ) {
		this.clientSocket = clientSocket;
		serverState = SERVER_STATE.PROCESS_LOGIN;
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		try {
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Should never be created without a given clientSocket
	 */
	@SuppressWarnings("unused")
	private FishyServerRunnable(){}
	/**
	 * Enumeration used to keep track of server states.
	 * A normal transaction process should be processed as followed:  PROCESS_LOGIN -> CONNECTED -> ENDED 
	 * @author Van
	 *
	 */
	public enum SERVER_STATE {
		PROCESS_LOGIN, CONNECTED, CONNECTED_READING, CONNECTED_WRITING, ENDED
	}


	@Override
	public void run() {
		String tankId= "";
		try {
			while (serverState != SERVER_STATE.ENDED){
				if (serverState == SERVER_STATE.PROCESS_LOGIN) {
					printLogMessage('o', null, "socket Extablished");
					inFromClient = new ObjectInputStream(clientSocket.getInputStream());
					outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
					String passcode = (String) inFromClient.readObject();
					// Check if passcode matches, else exit
					if (passcode.equals(PASSCODE)) {
						printLogMessage('o', null, "Passcode correct, establishing connection");
						serverState = SERVER_STATE.CONNECTED;
					} else {
						printLogMessage('o', null, "Passcode incorrect, ending connection");
						serverState = SERVER_STATE.ENDED;
					}
				} else if (serverState != SERVER_STATE.PROCESS_LOGIN) {
					//	Receive which use case to fulfil
					String transactionToPerform = (String) inFromClient.readObject();
					tankId = (String) inFromClient.readObject();
					//  Check if a transaction for the given tankId is already in progress
					//  Put in queue if so, other wise continue
					if (serverState == SERVER_STATE.CONNECTED) {
						while (!startClientServerTransaction(tankId, this)) {
							printLogMessage('o', tankId, "transaction for given tankId already started;  Waiting in queue.");
							try {
								Thread.sleep(THREAD_WAIT_TIME);
							} catch (InterruptedException e) {
								printLogMessage('e', tankId, "unable to sleep thread");
							}
						}
					}
					printLogMessage('o', tankId, "Switch: " + transactionToPerform);
					//  Each of the following is a use case a client would want to fulfil
					if (transactionToPerform.equals(NetworkTransactionSwitch.SERVER_RETRIEVE_MOBILE_SETTINGS.name())) {
						
						printLogMessage('o', tankId, "preparing to receive mobile data");
						recieveXMLDataFromClient(tankId, NetworkConstants.FILE_SUFFIX_MOBILE);
						printLogMessage('o', tankId, "mobile data recieved");

					} else if (transactionToPerform.equals(NetworkTransactionSwitch.SERVER_RETRIEVE_SENSOR_DATA.name())) {
						printLogMessage('o', tankId, "preparing to receive sensor data");
						recieveXMLDataFromClient(tankId, NetworkConstants.FILE_SUFFIX_SENSOR);
						printLogMessage('o', tankId, "sensor data recieved");
						
					} else if (transactionToPerform.equals(NetworkTransactionSwitch.SERVER_RETRIEVE_ACTION_LOG.name())) {
						
						printLogMessage('o', tankId, "preparing to receive log data");
						recieveXMLDataFromClient(tankId, NetworkConstants.FILE_SUFFIX_LOG);
						printLogMessage('o', tankId, "log data sent");

					} else if (transactionToPerform.equals(NetworkTransactionSwitch.DEVICE_RETRIEVE_MOBILE_SETTINGS.name())) {
						
						printLogMessage('o', tankId, "preparing to send mobile data");
						sendXMLDataToClient(tankId, NetworkConstants.FILE_SUFFIX_MOBILE);
						printLogMessage('o', tankId, "mobile data sent");

					} else if (transactionToPerform.equals(NetworkTransactionSwitch.DEVICE_RETRIEVE_SENSOR_DATA.name())) {
						
						printLogMessage('o', tankId, "preparing to send sensor data");
						sendXMLDataToClient(tankId, NetworkConstants.FILE_SUFFIX_SENSOR);
						printLogMessage('o', tankId, "sensor data sent");
						
					} else if (transactionToPerform.equals(NetworkTransactionSwitch.DEVICE_RETRIEVE_ACTION_LOG.name())) {
						printLogMessage('o', tankId, "preparing to send action log");
						sendXMLDataToClient(tankId, NetworkConstants.FILE_SUFFIX_LOG);
						printLogMessage('o', tankId, "action log sent");
						
					} else if (transactionToPerform.equals(NetworkTransactionSwitch.SERVER_MODIFY_MOBILE_SETTINGS.name())) {
						
						printLogMessage('o', tankId, "preparing to modify mobile settings attribute");
						updateXmlUsingUsingXpath(tankId, NetworkConstants.FILE_SUFFIX_MOBILE);
						printLogMessage('o', tankId, "settings modified");
					
					} else if (transactionToPerform.equals(NetworkTransactionSwitch.SERVER_MODIFY_SENSOR_DATA.name())) {
						
						printLogMessage('o', tankId, "preparing to modify sensor attribute");
						updateXmlUsingUsingXpath(tankId, NetworkConstants.FILE_SUFFIX_SENSOR);
						printLogMessage('o', tankId, "settings modified");
					
					} else if (transactionToPerform.equals(NetworkTransactionSwitch.SERVER_MODIFY_ACTION_LOG.name())) {
						
						printLogMessage('o', tankId, "preparing to modify log attribute");
						updateXmlUsingUsingXpath(tankId, NetworkConstants.FILE_SUFFIX_LOG);
						printLogMessage('o', tankId, "settings modified");
					
					} else if (transactionToPerform.equals(NetworkTransactionSwitch.SERVER_CLEAR_MOBILE_SETTINGS.name())) {
						
						printLogMessage('o', tankId, "preparing to clear settings attribute");
						clearXmlDetails(tankId, NetworkConstants.FILE_SUFFIX_MOBILE);
						printLogMessage('o', tankId, "xml cleared");
						
					} else if (transactionToPerform.equals(NetworkTransactionSwitch.SERVER_APPEND_MOBILE_SETTINGS.name())) {
						
						printLogMessage('o', tankId, "preparing to append to settings element");
						appendXmlData(tankId, NetworkConstants.FILE_SUFFIX_MOBILE);
						printLogMessage('o', tankId, "appended to settings element");
						
					} else if (transactionToPerform.equals(NetworkTransactionSwitch.SERVER_APPEND_ACTION_LOG.name())) {
						
						printLogMessage('o', tankId, "preparing to append to log element");
						appendXmlData(tankId, NetworkConstants.FILE_SUFFIX_LOG);
						printLogMessage('o', tankId, "appended to log element");
						
					} else if (transactionToPerform.equals(NetworkTransactionSwitch.DEVICE_CHECK_DATE_MOBILE_SETTINGS.name())) {
						
						printLogMessage('o', tankId, "preparing to return last modified date of mobile settings");
						getFileDate(tankId, NetworkConstants.FILE_SUFFIX_MOBILE);
						printLogMessage('o', tankId, "date returned of mobile settings");
						
					} else if (transactionToPerform.equals(NetworkTransactionSwitch.DEVICE_CHECK_DATE_SENSOR_DATA.name())) {
						
						printLogMessage('o', tankId, "preparing to return last modified date of sensor data");
						getFileDate(tankId, NetworkConstants.FILE_SUFFIX_SENSOR);
						printLogMessage('o', tankId, "date returned of sensor data");
						
					} else if (transactionToPerform.equals(NetworkTransactionSwitch.DEVICE_CHECK_DATE_ACTION_LOG.name())) {
						
						printLogMessage('o', tankId, "preparing to return last modified date of action log");
						getFileDate(tankId, NetworkConstants.FILE_SUFFIX_LOG);
						printLogMessage('o', tankId, "date returned of action log");
						
					} else if (transactionToPerform.equals(NetworkTransactionSwitch.DEVICE_END_TRANSACTION.name())) {
						printLogMessage('o', tankId, "closing connection");
						outToClient.writeObject("FIN");
						serverState = SERVER_STATE.ENDED;
						printLogMessage('o', tankId, "connection closed");
					}
				}
			}		
		} catch (ClassNotFoundException e2) {
			System.err.println("Server Error: " + e2.getMessage());
			System.err.println("Localized: " + e2.getLocalizedMessage());
			System.err.println("Stack Trace: " + e2.getStackTrace());
			System.err.println("To String: " + e2.toString());
		} catch (TransformerConfigurationException e) {
			System.err.println("Server Error: " + e.getMessage());
			System.err.println("Localized: " + e.getLocalizedMessage());
			System.err.println("Stack Trace: " + e.getStackTrace());
			System.err.println("To String: " + e.toString());
		} catch (ParserConfigurationException e) {
			System.err.println("Server Error: " + e.getMessage());
			System.err.println("Localized: " + e.getLocalizedMessage());
			System.err.println("Stack Trace: " + e.getStackTrace());
			System.err.println("To String: " + e.toString());
		} catch (SAXException e) {
			System.err.println("Server Error: " + e.getMessage());
			System.err.println("Localized: " + e.getLocalizedMessage());
			System.err.println("Stack Trace: " + e.getStackTrace());
			System.err.println("To String: " + e.toString());
		} catch (TransformerException e) {
			System.err.println("Server Error: " + e.getMessage());
			System.err.println("Localized: " + e.getLocalizedMessage());
			System.err.println("Stack Trace: " + e.getStackTrace());
			System.err.println("To String: " + e.toString());
		} catch (IOException e) {
			System.err.println("Server Error: " + e.getMessage());
			System.err.println("Localized: " + e.getLocalizedMessage());
			System.err.println("Stack Trace: " + e.getStackTrace());
			System.err.println("To String: " + e.toString());
		} catch (XPathExpressionException e) {
			System.err.println("Server Error: " + e.getMessage());
			System.err.println("Localized: " + e.getLocalizedMessage());
			System.err.println("Stack Trace: " + e.getStackTrace());
			System.err.println("To String: " + e.toString());
		}
		endClientServerTransaction(tankId);
		
		try {
			clientSocket.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			System.err.println("Server Error: " + e.getMessage());
			System.err.println("Localized: " + e.getLocalizedMessage());
			System.err.println("Stack Trace: " + e.getStackTrace());
			System.err.println("To String: " + e.toString());
		}
		
	}
	/**
	 * Checks if the server is ready to perform a client/server transaction with the given tankId.  Returns
	 * true when the server is ready to start a new transaction with the given tankId, otherwise returns false.
	 * 
	 * @param tankId the id of the server/client transaction
	 * @return returns true if the server is ready to start a transaction, false otherwise
	 */
	private static synchronized boolean startClientServerTransaction(String tankId, FishyServerRunnable obj) {
		boolean rc = false;
		
		if (TANK_IDS.get(tankId) == null) { // no queue, add transaction and continue
			Queue<FishyServerRunnable> queue = new LinkedList<FishyServerRunnable>();
			queue.add(obj);
			TANK_IDS.put(tankId, queue);
			rc = true;
		} else if (TANK_IDS.get(tankId).peek() == null) { // queue is empty, add transaction and continue
			TANK_IDS.get(tankId).add(obj);
			rc = true;
		} else if (TANK_IDS.get(tankId).peek() == obj) { // queue next in line is the current transaction, continue
			rc = true;
		} else if (!TANK_IDS.get(tankId).contains(obj)) { // queue does not contain the current transaction, add to queue
			TANK_IDS.get(tankId).add(obj);
			rc = false;
		}
		else { // play dead
			rc = false;
		}
		// otherwise it exists
		return rc;
	}


	/**
	 * Removes the tankId from the tank transaction list
	 * @param tankId the tankid to end the tank transaction
	 * @return true if id has been removed
	 */
	private static synchronized boolean endClientServerTransaction(String tankId) {
		TANK_IDS.get(tankId).remove();
		return true;
	}
	
	/**
	 * Helper function to choose and send a specified xml to the client with the given suffix
	 * @throws IOException thrown if server connection ends prematurely
	 * @throws SAXException parser unable to read xml
	 * @throws ParserConfigurationException parser unable to read xml
	 * @throws ClassNotFoundException if object recieved by stream is of an unknown type
	 * 
	 * @param suffix the file to build the file path for
	 */
	private void sendXMLDataToClient(String tankId, String suffix) throws 	ClassNotFoundException,
																			ParserConfigurationException,
																			SAXException,
																			IOException {
		serverState = SERVER_STATE.CONNECTED_WRITING;
		// Open file and send
		File file = new File(buildFilePath(tankId, suffix));
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document document = dBuilder.parse(file);

		outToClient.writeObject(document); // send xml
		inFromClient.readObject();  // recieve empty string
	}
	
	/**
	 * Helper function to choose and recieve a specified xml from the client with the given suffix
	 * @param suffix
	 * @throws ClassNotFoundException if object recieved by stream is of an unknown type
	 * @throws IOException thrown if server connection ends prematurely
	 * @throws TransformerConfigurationException parser unable to write xml
	 * @throws TransformerException parser unable to write xml
	 * @throws XPathExpressionException xml does not have the correct xml
	 */
	private void recieveXMLDataFromClient(String tankId, String suffix) throws	ClassNotFoundException,
																				IOException,
																				TransformerConfigurationException,
																				TransformerException, XPathExpressionException {
		serverState = SERVER_STATE.CONNECTED_WRITING;
		Document document = (Document) inFromClient.readObject();
		PrintWriter writer = new PrintWriter(buildFilePath(tankId, suffix), "UTF-8");
		StreamResult result = new StreamResult(writer);
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		NodeList dateNode = (NodeList) xpath.evaluate(DATE_XPATH_EXPRESSION, document, XPathConstants.NODESET);
		dateNode.item(0).setNodeValue(DATE_FORMAT.format(CALENDAR.getTime()));
		
		DOMSource source = new DOMSource(document);
		transformer.transform(source, result);
		writer.close();
		outToClient.writeObject("");

	}
	
	
	private void updateXmlUsingUsingXpath(String tankId, String suffix) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, TransformerException, ClassNotFoundException {
		
		serverState = SERVER_STATE.CONNECTED_WRITING;
		String xpathExpression = (String) inFromClient.readObject();
		String attributeName = (String) inFromClient.readObject();
		String updatedValue = (String) inFromClient.readObject();

		File file = new File(buildFilePath(tankId, suffix));
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document document = dBuilder.parse(file);
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		Node widgetNode = (Node) xpath.evaluate(xpathExpression, document, XPathConstants.NODE);
		
		NamedNodeMap nodeMap = widgetNode.getAttributes();
		
		Node nammedAttr = nodeMap.getNamedItem(attributeName);
		if (updatedValue != null)
			nammedAttr.setTextContent(updatedValue);
		
		for (String term : UPDATE_SECTIONS) {
			if (xpathExpression.toLowerCase().contains(term.toLowerCase())) {
				Node updateNode = (Node) xpath.evaluate(UPDATE_XPATH_EXPRESSION, document, XPathConstants.NODE);
				NamedNodeMap updateNodeMap = updateNode.getAttributes();
				
				Node updatedAttr = updateNodeMap.getNamedItem(term);
				updatedAttr.setTextContent("true");
			}
		}
		
		NodeList dateNode = (NodeList) xpath.evaluate(DATE_XPATH_EXPRESSION, document, XPathConstants.NODESET);
		dateNode.item(0).setNodeValue(DATE_FORMAT.format(CALENDAR.getTime()));
		
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(file);
		transformer.transform(source, result);
		
		outToClient.writeObject("");
		
	}
	
	
	private void appendXmlData(String tankId, String suffix) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, TransformerException, ClassNotFoundException {
		serverState = SERVER_STATE.CONNECTED_WRITING;
		
		String xpathExpression = (String) inFromClient.readObject();
		@SuppressWarnings("unchecked")
		HashMap<String, String> data = (HashMap<String, String>) inFromClient.readObject();
		File file = new File(buildFilePath(tankId, suffix));
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document document = dBuilder.parse(file);
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		Node widgetNode = (Node) xpath.evaluate(xpathExpression, document, XPathConstants.NODE);
		String childElementName = "details";
		if (suffix.equals(NetworkConstants.FILE_SUFFIX_LOG)) {
			childElementName = "Log";
		}
		Element details = document.createElement(childElementName);
		
		for (String key : data.keySet()) {
			details.setAttribute(key, data.get(key));
		}
		widgetNode.appendChild(details);
		
		for (String term : UPDATE_SECTIONS) {
			if (xpathExpression.toLowerCase().contains(term.toLowerCase())) {
				Node updateNode = (Node) xpath.evaluate(UPDATE_XPATH_EXPRESSION, document, XPathConstants.NODE);
				NamedNodeMap updateNodeMap = updateNode.getAttributes();
				
				
				Node updatedAttr = updateNodeMap.getNamedItem(term);
				updatedAttr.setTextContent("true");
			}
		}
		
		NodeList dateNode = (NodeList) xpath.evaluate(DATE_XPATH_EXPRESSION, document, XPathConstants.NODESET);
		dateNode.item(0).setNodeValue(DATE_FORMAT.format(CALENDAR.getTime()));
		
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(file);
		transformer.transform(source, result);
		
		outToClient.writeObject("");
	}
	
	private void clearXmlDetails(String tankId, String suffix) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, TransformerException, ClassNotFoundException {
		
		serverState = SERVER_STATE.CONNECTED_WRITING;
		String xpathExpression = (String) inFromClient.readObject();
		
		File file = new File(buildFilePath(tankId, suffix));
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document document = dBuilder.parse(file);
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		Node widgetNode = (Node) xpath.evaluate(xpathExpression, document, XPathConstants.NODE);
		
		
		while (widgetNode.hasChildNodes()) {
			widgetNode.removeChild(widgetNode.getLastChild());
		}
		
		for (String term : UPDATE_SECTIONS) {
			if (xpathExpression.toLowerCase().contains(term.toLowerCase())) {
				Node updateNode = (Node) xpath.evaluate(UPDATE_XPATH_EXPRESSION, document, XPathConstants.NODE);
				NamedNodeMap updateNodeMap = updateNode.getAttributes();
				
				Node updatedAttr = updateNodeMap.getNamedItem(term);
				updatedAttr.setTextContent("true");
			}
		}
		
		NodeList dateNode = (NodeList) xpath.evaluate(DATE_XPATH_EXPRESSION, document, XPathConstants.NODESET);
		dateNode.item(0).setNodeValue(DATE_FORMAT.format(CALENDAR.getTime()));
		
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(file);
		transformer.transform(source, result);
		

		outToClient.writeObject("");
	}
	
	/**
	 * Return the last modified date of the chosen file
	 * @param tankId
	 * @param suffix
	 * @throws XPathExpressionException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private void getFileDate(String tankId, String suffix) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		serverState = SERVER_STATE.CONNECTED_READING;
		
		File file = new File(buildFilePath(tankId, suffix));
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document document = dBuilder.parse(file);
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		NodeList dateNode = (NodeList) xpath.evaluate(DATE_XPATH_EXPRESSION, document, XPathConstants.NODESET);
		
		outToClient.writeObject(dateNode.item(0).getNodeValue());
		
		outToClient.writeObject("");
		
		
	}
	
	/**
	 * Builds a file path for the given suffix.
	 * @param suffix the file to build the file path for
	 * @return returns the complete file directory of the given suffix
	 */
	private static String buildFilePath(String tankId, String suffix) {
		return String.format(NetworkConstants.FILE_LOCATION_FORMAT, NetworkConstants.FILE_LOCATION_SERVER, tankId, suffix, NetworkConstants.FILE_EXTENSION);
	}
	
	
	/**
	 * Helper function to print server log messages
	 * @param tankId the tankId associated to the error
	 * @param message the message to display
	 * @param output set to o for standard output, e to error
	 */
	public static void printLogMessage(char output, String tankId, String message) {
		if (tankId == null || tankId.equals(""))
			tankId = "<noId>";

		String timeNow = DATE_FORMAT.format(CALENDAR.getTime());
		if (output == 'e') {
			System.err.printf(LOG_FORMAT, timeNow, tankId, message);
		} else {
			System.out.printf(LOG_FORMAT, timeNow, tankId, message);
		}
	}
	
	
	public static void main(String args[]) {
		Thread[] threads = new Thread[10];
		int numberOfConnections = 0;
		int portNumber = 4445;
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(portNumber, 0, InetAddress.getLocalHost());
			System.out.println("Server InetAddress: " +serverSocket.getInetAddress());
			System.out.println("Server Port: " +serverSocket.getLocalPort());
			while (true) {
				while (numberOfConnections < 10){
					threads[numberOfConnections] = new Thread(new FishyServerRunnable(serverSocket.accept()));
					threads[numberOfConnections].start();
					numberOfConnections++;
				}
				while (numberOfConnections > 0) {
					try {
						threads[--numberOfConnections].join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
}
