package fishyServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FishyServer {
	public static void main(String args[]) {
		int portNumber = 4444;
		try {
			 portNumber = Integer.parseInt(args[0]);
		} catch( NumberFormatException e ) {
			System.out.println(String.format("could not parse given port number, using %d as default", portNumber));
		}  catch( ArrayIndexOutOfBoundsException e ) {
			System.out.println(String.format("no parameter given, using %d as default", portNumber));
		}
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(portNumber, 0, InetAddress.getLocalHost());
			System.out.println("Server InetAddress: " +serverSocket.getInetAddress());
			System.out.println("Server Port: " +serverSocket.getLocalPort());
			while (true) {
				try { 	    
					System.out.println("Waiting for client to connect...");
				    Socket clientSocket = serverSocket.accept();
				    System.out.println("Socket Extablished...");
				    //	Open input output stream
		            ObjectInputStream inFromClient = new ObjectInputStream(clientSocket.getInputStream());
		            ObjectOutputStream outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
		            //	Read the read or write option and then the tankId to modify
		            String readOrWrite = (String)inFromClient.readObject();
		            String tankID = (String)inFromClient.readObject();
		            
		            //	Search for tank data
		            String object_filename = String.format("/root/fishy_data/%s", tankID);
		            HashMap<String, String> dataList = new HashMap<>();
		            // Load existing data, empty list otherwise
		            try {
		            	FileInputStream fis = new FileInputStream(object_filename);
		            	ObjectInputStream is = new ObjectInputStream(fis);
		            	dataList = (HashMap<String, String>) is.readObject();
		            	is.close();
		            	fis.close();
		            	System.out.println("Tank file found, loading data...");
	            	} catch (FileNotFoundException e) {
	            		dataList = new HashMap<>();
	            		System.out.println("Tank file not found, loading empty list...");
	            	}
		            //	Write to tank
		            if (readOrWrite.equals("w") || readOrWrite.equals("n")) {
		            	HashMap<String, String> inList = dataList;
		            	if (readOrWrite.equals("n")) {
		            		inList = new HashMap<>();
		            	}
	            		HashMap<String, String> fromClientList = (HashMap<String, String>)inFromClient.readObject();
	            		//	Copy data sent from client into saved data
	            		for (String key : fromClientList.keySet())
	            			inList.put(key, fromClientList.get(key));
			            System.out.println("Data received, write mode...");
			            FileOutputStream fos = new FileOutputStream(object_filename);
			            ObjectOutputStream os = new ObjectOutputStream(fos);
			            os.writeObject(inList);	// Serialize and save datalist for tank
			            os.close();
			            fos.close();
			            try{
			            	// Construct html file to view
			            	String html_filename = String.format("/var/www/vanchaubui.com/public_html/fish_tanks/%s.html", tankID);
			                PrintWriter writer = new PrintWriter(html_filename, "UTF-8");
			                writer.println("<html>");
			                writer.println("<head>");
			                writer.println("<title>PiSeas Data</title>");
			                writer.println("<link rel=\"stylesheet\" href=\"/css/normalize.css\">");
			                writer.println("<link rel=\"stylesheet\" href=\"/css/skeleton.css\">");
			                writer.println("<link rel=\"stylesheet\" href=\"/css/fishy.css\">");
			                writer.println("</head>");
			                writer.println("<body>");
			                writer.println("<div class=\"container\" >");
			                writer.println("<div class=\"row\">");
			                writer.println(String.format("<h1>Fish tank data - TankId: %s</h1>", tankID));
			                writer.println("</div>");
			                writer.println("<div class=\"row\">");
			                writer.println("<div class=\"three columns\">");
			                writer.println("<h5>Key</h5>");
			                writer.println("</div>");
			                writer.println("<div class=\"nine columns\">");
			                writer.println("<h5>Value</h5>");
			                writer.println("</div>");
			                writer.println("</div>");
			                ArrayList<String> keyList = new ArrayList<>();
			                keyList.addAll(inList.keySet());
			                keyList.remove("tankId");
			                Collections.sort(keyList);
			                for (String key : keyList) {
			                	writer.println("<div class=\"row\">");
			                	writer.println("<div class=\"three columns\">");
			                	writer.println("<p><b>");
//				                writer.println("<dt>");
				                writer.println(key);
				                writer.println("</b></p>");
				                writer.println("</div>");
				                writer.println("<div class=\"nine columns\">");
				                writer.println("<p>");
				                writer.println(inList.get(key));
				                writer.println("</p>");
				                writer.println("</div>");
				                writer.println("</div>");
//				              	writer.println("</dl>");
			                }
			                writer.println("</div>");
			                writer.println("</div>");
			                writer.println("</body>");
			                writer.println("</html>");
			                System.out.println("Data written...");
			                writer.close();
			                //	Save html file to apache server location
			                Path file = FileSystems.getDefault().getPath("/var/www/vanchaubui.com/public_html", "fishy.html");
			                try (InputStream in = Files.newInputStream(file);
			                    BufferedReader reader =
			                      new BufferedReader(new InputStreamReader(in))) {
			                    String line = null;
			                    while ((line = reader.readLine()) != null) {
			                        System.out.println(line);
			                    }
			                } catch (IOException x) {
			                    System.err.println(x);
			                }
			            } catch (Exception e) {
					        System.err.println("File Write Error: " + e.getMessage());
					        System.err.println("Localized: " + e.getLocalizedMessage());
					        System.err.println("Stack Trace: " + e.getStackTrace());
					        System.err.println("To String: " + e.toString());
			            }
		            //	Read tank data
		            } else if (readOrWrite.equals("r")) {
		            	System.out.println("Data received, read mode...");
		            	outToClient.writeObject(dataList);
		            	System.out.println("Data sent...");
		            }
		            System.out.println("Connection end...");
		            clientSocket.close();
				} catch (Exception e) {
			        System.err.println("Server Error: " + e.getMessage());
			        System.err.println("Localized: " + e.getLocalizedMessage());
			        System.err.println("Stack Trace: " + e.getStackTrace());
			        System.err.println("To String: " + e.toString());
				}
			}
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
	
	public class TankSensorSpoof {
		public HashMap<String, String> tankStatus;
		public HashMap<String, String> tankOtherData;
		public TankSensorSpoof() {
			tankStatus = new HashMap<>();
			tankStatus.put("pHLevel", "7");
			tankStatus.put("waterConductivityLevel", "15000");
			tankStatus.put("temperatureLevel", "23 degrees");
			tankStatus.put("lightStatus", "On");
			tankStatus.put("pHStatus", "Good");
			tankStatus.put("waterConductivityStatus", "Good");
		}
	}
	// TODO: UPGRADE
	public class TankXML {
		private String tankID;
		private String pH_level;
		private String water_conductivity_level;
		private String temperature_level;
		private String feeding_time;
		private String light_on;
		private String light_off;
		
		public TankXML() {
			tankID = "default";
		}
		public TankXML(String tankID) {
			this();
			this.tankID = tankID;
			File inputFile = new File(String.format("/var/www/vanchaubui.com/public_html/fishtanks/%s.xml", tankID));
			if (inputFile.exists() && !inputFile.isDirectory()) {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder;
				try {
					dBuilder = dbFactory.newDocumentBuilder();
	                Document doc = dBuilder.newDocument();
	                // root element
	                Element rootElement = doc.createElement("PiiSeas");
	                doc.appendChild(rootElement);
	                //  supercars element
	                Element value = doc.createElement("Value");
	                Element tank = doc.createElement("Tank");
	                tank.setAttribute("TankID", tankID);
	                Element ph = doc.createElement("PH");
	                ph.appendChild(value);
	                Element waterConductivity = doc.createElement("WaterConductivity");
	                ph.appendChild(value);
	                Element temperature = doc.createElement("Temperature");
	                Element feeding = doc.createElement("Feeding");
	                Element lighting = doc.createElement("Lighting");
	                tank.appendChild(ph);
	                tank.appendChild(waterConductivity);
	                tank.appendChild(temperature);
	                tank.appendChild(feeding);
	                tank.appendChild(lighting);
	                rootElement.appendChild(tank);
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//TODO: READ XML DATA
		}
		
	}
	public class Tank {
		private PH ph;
		private WaterConductivity waterConductivity;
	}
	public class Data {
		protected double value;
		protected Threshold threshold;
		
	}
	public class Threshold {
		private double max;
		private double min;
		
		public Threshold(){
			this(0, 0);
		}
		public Threshold(double max, double min) {
			this.max = max;
			this.min = min;
		}
	}
	
	public class PH extends Data {
		
	}
	public class WaterConductivity extends Data {
		
	}
	public class Temperature extends Data {
		
	}
	
}
