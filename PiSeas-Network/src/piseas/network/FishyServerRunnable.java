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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
// TODO: check for PIN
// TODO: display detailed message about empty xml
// TODO: add time stamp to xmls

public class FishyServerRunnable implements Runnable {
	private static final long THREAD_WAIT_TIME = 5000;
	private static final DateFormat DATEFORMAT = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
	private static final Calendar CALENDAR = Calendar.getInstance();
	private static final String LOG_FORMAT = "%s %s: %s\n";  // displays as the following:  <date> <tankId>: <log message>
	private static final String PASSCODE = "xBE3GnsotxlFSwb9sg7t";
	
	private static HashMap<String, Queue<FishyServerRunnable>> TANK_IDS = new HashMap<String, Queue<FishyServerRunnable>>();
	
	private ObjectInputStream inFromClient;
	private ObjectOutputStream outToClient;
	private Socket clientSocket;
	
	public SERVER_STATE serverState;
	
	
	public FishyServerRunnable( Socket clientSocket ) {
		this.clientSocket = clientSocket;
		serverState = SERVER_STATE.PROCESS_LOGIN;
	}
	
	
	public enum SERVER_STATE {
		PROCESS_LOGIN, CONNECTED, CONNECTED_READING, CONNECTED_WRITING, ENDED
	}
	
	
	@Override
	public void run() {
		String tankId= "";
		try {
			while (serverState != SERVER_STATE.ENDED){
				if (serverState == SERVER_STATE.PROCESS_LOGIN) {
					System.out.println("Waiting for client to connect...");
				    System.out.println("Socket Extablished...");
				    inFromClient = new ObjectInputStream(clientSocket.getInputStream());
			        outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
			        String passcode = (String)inFromClient.readObject();

			        if (passcode.equals(PASSCODE)) {
			        	printLogMessage('o', null, "Passcode correct, establishing connection");
			        	serverState = SERVER_STATE.CONNECTED;
			        } else {
			        	printLogMessage('o', null, "Passcode incorrect, ending connection");
			        	serverState = SERVER_STATE.ENDED;
			        }
				} else if (serverState == SERVER_STATE.CONNECTED) {
			        //	Read the read or write option and then the tankId to modify
			        String readOrWrite = (String)inFromClient.readObject();
			        tankId = (String)inFromClient.readObject();
			        while (!startClientServerTransaction(tankId)) {
			        	printLogMessage('o', tankId, "transaction for given tankId already started;  Waiting in queue.");
			        	try {
							Thread.sleep(THREAD_WAIT_TIME);
						} catch (InterruptedException e) {
							printLogMessage('e', tankId, "unable to sleep thread");
						}
			        }
			        String xml_filename_pi = String.format("/var/www/vanchaubui.com/public_html/fish_tanks/%s_pi.xml", tankId);
		        	String xml_filename_mobile = String.format("/var/www/vanchaubui.com/public_html/fish_tanks/%s_mobile.xml", tankId);
			        if (readOrWrite.equals("recieving_pi")) {
			        	serverState = SERVER_STATE.CONNECTED_WRITING;
			        	printLogMessage('o', tankId, "recieving tank data");
			        	
			        	Document document = (Document) inFromClient.readObject();
			        	PrintWriter writer = new PrintWriter(xml_filename_pi, "UTF-8");
			        	StreamResult result = new StreamResult(writer);
			        	TransformerFactory tFactory = TransformerFactory.newInstance();
	        		    Transformer transformer = tFactory.newTransformer();

	        		    DOMSource source = new DOMSource(document);
	        		    transformer.transform(source, result);
	        		    writer.close();
	        		    serverState = SERVER_STATE.ENDED;
	        		    outToClient.writeObject("");
	        		    System.out.println("Done recieving_pi");
	        		    
			        } else if (readOrWrite.equals("recieving_mobile")) {
			        	serverState = SERVER_STATE.CONNECTED_WRITING;
			        	printLogMessage('o', tankId, "receiving mobile data...");
			        	Document document = (Document) inFromClient.readObject();
			        	PrintWriter writer = new PrintWriter(xml_filename_mobile, "UTF-8");
			        	StreamResult result = new StreamResult(writer);
			        	TransformerFactory tFactory = TransformerFactory.newInstance();
	        		    Transformer transformer = tFactory.newTransformer();

	        		    DOMSource source = new DOMSource(document);
	        		    transformer.transform(source, result);
	        		    writer.close();
	        		    serverState = SERVER_STATE.ENDED;
	        		    outToClient.writeObject("");
	        		    System.out.println("Done recieving_mobile");
			        } else if (readOrWrite.equals("sending_pi")) {
			        	serverState = SERVER_STATE.CONNECTED_WRITING;
			        	
			        	System.out.println("sending xml data to pi...");
			        	File filePi = new File(xml_filename_pi);
			        	File fileMobile = new File(xml_filename_mobile);
			        	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			        	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			        	Document doc_pi = dBuilder.parse(filePi);
			        	Document doc_mobile = dBuilder.parse(fileMobile);
			        	
			        	outToClient.writeObject(doc_pi);
			        	outToClient.writeObject(doc_mobile);
			        	serverState = SERVER_STATE.ENDED;
			        	inFromClient.readObject();
			        	System.out.println("Done sending_pi");
			        	
			        } else if (readOrWrite.equals("sending_mobile")) {
			        	serverState = SERVER_STATE.CONNECTED_WRITING;
			        	
			        	System.out.println("sending xml data to mobile...");
			        	File filePi = new File(xml_filename_pi);
			        	File fileMobile = new File(xml_filename_mobile);
			        	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			        	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			        	Document doc_pi = dBuilder.parse(filePi);
			        	Document doc_mobile = dBuilder.parse(fileMobile);
			        	
			        	outToClient.writeObject(doc_pi);
			        	outToClient.writeObject(doc_mobile);
			        	serverState = SERVER_STATE.ENDED;
			        	inFromClient.readObject();
			        	System.out.println("Done sending_mobile");
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
		}
		endClientServerTransaction(tankId);
	}
	/**
	 * Checks if the server is ready to perform a client/server transaction with the given tankId.  Returns
	 * true when the server is ready to start a new transaction with the given tankId, otherwise returns false.
	 * 
	 * @param tankId the id of the server/client transaction
	 * @return returns true if the server is ready to start a transaction, false otherwise
	 */
	private synchronized boolean startClientServerTransaction(String tankId) {
		boolean rc = false;
		
		if (TANK_IDS.get(tankId) == null) { // no queue, add transaction and continue
			Queue<FishyServerRunnable> queue = new LinkedList<FishyServerRunnable>();
			queue.add(this);
			TANK_IDS.put(tankId, queue);
			rc = true;
		} else if (TANK_IDS.get(tankId).peek() == null) { // queue is empty, add transaction and continue
			TANK_IDS.get(tankId).add(this);
			rc = true;
		} else if (TANK_IDS.get(tankId).peek() == this) { // queue next in line is the current transaction, continue
			rc = true;
		} else if (!TANK_IDS.get(tankId).contains(this)) { // queue does not contain the current transaction, add to queue
			TANK_IDS.get(tankId).add(this);
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
	private synchronized boolean endClientServerTransaction(String tankId) {
		TANK_IDS.get(tankId).remove();
		return true;
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

		String timeNow = DATEFORMAT.format(CALENDAR.getTime());
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
