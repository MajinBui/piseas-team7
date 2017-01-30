package piseas.network;

import java.io.File;
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

    	//	how to receive data
    	for (int i = 0; i < 10; i++) {
	    	FishyClient.writeToPiData(tankID, "C:\\Users\\Van\\Documents\\fish_test\\input\\QWERT_pi.xml");
	    	FishyClient.writeToMobileData(tankID, "C:\\Users\\Van\\Documents\\fish_test\\input\\QWERT_mobile.xml");
	        FishyClient.retrieveServerData(tankID, "C:\\Users\\Van\\Documents\\fish_test\\output\\", "C:\\Users\\Van\\Documents\\fish_test\\output\\");
    	}
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
	 * Returns the current saved data of the given TankID as a HashMap<String, String>
	 * @param tankId the id of the tank to retrieve data from
	 * @return a HashMap<String, String> of the server data stored
	 */
	private static void retrieveServerData(String tankId, String mobileXMLSavePath, String piXMLSavePath) {
		try {
			Socket clientSocket = connectToServer();
			System.out.println("Retrieving Pi Data");
	        ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
	        ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
	        String command = "sending_pi";
	        outToServer.writeObject(PASSCODE);
	        outToServer.writeObject(command);
	        outToServer.writeObject(tankId);
	        
	        Document document_pi = (Document) inFromServer.readObject();
	        Document document_mobile = (Document) inFromServer.readObject();
	        String xml_filename_pi = String.format("%s/%s_pi.xml", mobileXMLSavePath, tankId);
	        String xml_filename_mobile = String.format("%s/%s_mobile.xml", piXMLSavePath, tankId);
        	PrintWriter writer = new PrintWriter(xml_filename_pi, "UTF-8");
        	StreamResult result = new StreamResult(writer);
        	TransformerFactory tFactory = TransformerFactory.newInstance();
		    Transformer transformer = tFactory.newTransformer();

		    DOMSource source = new DOMSource(document_pi);
		    transformer.transform(source, result);
		    writer.close();
		    System.out.println("Retrieved Pi Data...");
		    System.out.println("Retrieving mobile Data...");
		    PrintWriter writer2 = new PrintWriter(xml_filename_mobile, "UTF-8");
        	StreamResult result2 = new StreamResult(writer);

		    source = new DOMSource(document_mobile);
		    transformer.transform(source, result2);
		    writer2.close();
		    System.out.println("Retrieved mobile Data...");
		    outToServer.writeObject("");
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
		} catch (TransformerConfigurationException e) {
			System.err.println("Client Error: " + e.getMessage());
	        System.err.println("Localized: " + e.getLocalizedMessage());
	        System.err.println("Stack Trace: " + e.getStackTrace());
		} catch (TransformerException e) {
			System.err.println("Client Error: " + e.getMessage());
	        System.err.println("Localized: " + e.getLocalizedMessage());
	        System.err.println("Stack Trace: " + e.getStackTrace());
		}
		System.err.println("no data found, maybe xml not initialized yet");
	}
	/**
	 * Updates the tank with the given TankId with the HashMap<String, String> given
	 * @param tankId the TankID of the tank that needs updating
	 * @param dataHashMap the HashMap<String, String> that the tank's save data will be updated to
	 */
	private static void writeToServerData(String tankId, String command, String mobileXMLSavePath) {
		try {
			Socket clientSocket = connectToServer();
	        ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
	        ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
	        outToServer.writeObject(PASSCODE);
	        outToServer.writeObject(command);
	        outToServer.writeObject(tankId);
	        
	        File file = new File(mobileXMLSavePath);
        	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        	Document doc = dBuilder.parse(file);
	        
	        outToServer.writeObject(doc);
	        inFromServer.readObject();
	        System.out.println("Data sent");
	        return;
		} catch (UnknownHostException e) {
	        System.err.println("Client Error: " + e.getMessage());
	        System.err.println("Localized: " + e.getLocalizedMessage());
	        System.err.println("Stack Trace: " + e.getStackTrace());
		} catch (IOException e) {
	        System.err.println("Client Error: " + e.getMessage());
	        System.err.println("Localized: " + e.getLocalizedMessage());
	        System.err.println("Stack Trace: " + e.getStackTrace());
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
		}
		return;
	}
	
	public static void writeToMobileData(String tankId, String mobileXMLSavePath) {
		System.out.println("Sending mobile data...");
		FishyClient.writeToServerData(tankId, "recieving_mobile", mobileXMLSavePath);
	}
	public static void writeToPiData(String tankId, String mobileXMLSavePath) {
		System.out.println("Sending pi data...");
		FishyClient.writeToServerData(tankId, "recieving_pi", mobileXMLSavePath);
	}
}
