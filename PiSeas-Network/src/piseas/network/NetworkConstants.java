package piseas.network;
/**
 * This class holds the global constant values of the networking package.
 * @author Van
 *
 */
public class NetworkConstants {
	public static final String FILE_EXTENSION = ".xml";
	public static final String FILE_SUFFIX_MOBILE = "_mobile_settings"; 
	public static final String FILE_SUFFIX_SENSOR = "_sensor_data";
	public static final String FILE_SUFFIX_LOG = "_action_log";
	//public static final String FILE_SUFFIX_COMMANDS = "_manual_commands";
	public static final String FILE_LOCATION_SERVER = "/var/www/vanchaubui.com/public_html/fish_tanks/";
	public static final String FILE_LOCATION_PISEAS ="";
	
	public static final String FILE_LOCATION_FORMAT = "%s%s%s%s";
	
	public static final String XPATH_TANK = "/Piseas/Tank";
	public static final String XPATH_TANK_DETAILS = "/Piseas/Tank/details";
	public static final String XPATH_TEMPERATURE = "/Piseas/Temperature";
	public static final String XPATH_TEMPERATURE_DETAILS = "/Piseas/Temperature/details";
	public static final String XPATH_CONDUCTIVITY = "/Piseas/Conductivity";
	public static final String XPATH_CONDUCTIVITY_DETAILS = "/Piseas/Conductivity/details";
	public static final String XPATH_PUMP= "/Piseas/Pump";
	public static final String XPATH_PUMP_DETAILS = "/Piseas/Pump/details";
	public static final String XPATH_PH = "/Piseas/PH";
	public static final String XPATH_PH_DETAILS = "/Piseas/PH/details";
	public static final String XPATH_SENSOR = "/Piseas/Sensor";
	public static final String XPATH_UPDATE = "/Piseas/Update/details";
	public static final String XPATH_LIGHT = "/Piseas/Light";
	public static final String XPATH_LIGHT_DETAILS = "/Piseas/Light/details";
	public static final String XPATH_FEED = "/Piseas/Feed";
	public static final String XPATH_FEED_DETAILS = "/Piseas/Feed/details";
	public static final String XPATH_LOG = "/Piseas/Logs";
}
