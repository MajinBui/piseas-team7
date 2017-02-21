package piseas.network;

/**
 * This enum is used between the server and client to determine which action to perform
 * @author Van
 *
 */
public enum NetworkTransactionSwitch {
	DEVICE_RETRIEVE_MOBILE_SETTINGS,
	DEVICE_RETRIEVE_SENSOR_DATA,
	DEVICE_RETRIEVE_ACTION_LOG,
	DEVICE_RETRIEVE_MANUAL_COMMANDS,
	SERVER_RETRIEVE_MOBILE_SETTINGS,
	SERVER_RETRIEVE_SENSOR_DATA,
	SERVER_RETRIEVE_ACTION_LOG,
	SERVER_RETRIEVE_MANUAL_COMMANDS
}
