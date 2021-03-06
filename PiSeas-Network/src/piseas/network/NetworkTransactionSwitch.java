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
	SERVER_RETRIEVE_MOBILE_SETTINGS,
	SERVER_RETRIEVE_SENSOR_DATA,
	SERVER_RETRIEVE_ACTION_LOG,
	SERVER_MODIFY_MOBILE_SETTINGS,
	SERVER_MODIFY_SENSOR_DATA,
	SERVER_MODIFY_ACTION_LOG,
	SERVER_CLEAR_MOBILE_SETTINGS,
	SERVER_APPEND_MOBILE_SETTINGS,
	SERVER_APPEND_ACTION_LOG,
	DEVICE_END_TRANSACTION,
	DEVICE_CHECK_DATE_MOBILE_SETTINGS,
	DEVICE_CHECK_DATE_SENSOR_DATA,
	DEVICE_CHECK_DATE_ACTION_LOG,
	DEVICE_CHECK_PASSWORD_MOBILE_SETTINGS
}
