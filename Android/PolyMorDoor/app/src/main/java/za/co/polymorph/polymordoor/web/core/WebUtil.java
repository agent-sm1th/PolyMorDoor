package za.co.polymorph.polymordoor.web.core;

import org.apache.http.HttpStatus;

/**
 *
 * @author Legion
 *
 * This class contains static helper methods for the web API tasks 
 *
 */
public class WebUtil {

    /** a static list of common error codes **/
    public static final Integer[] LIST_STATUS_CODE_BAD = new Integer[]{
            HttpStatus.SC_BAD_REQUEST, //400
            HttpStatus.SC_UNAUTHORIZED, //401
            HttpStatus.SC_FORBIDDEN, //403
            HttpStatus.SC_NOT_FOUND, //404
            HttpStatus.SC_GONE, //410
            HttpStatus.SC_INTERNAL_SERVER_ERROR, //500
            HttpStatus.SC_BAD_GATEWAY, //502
            HttpStatus.SC_GATEWAY_TIMEOUT //504
    };

    /** request type names **/
    public static final String RequestTypeName[] = new String[]{"GET", "POST", "PUT", "DELETE"};


    /** static names for HTTP headers **/
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded; charset=UTF-8";

    public static final String ACCESS_TOKEN="YOUR_ACCESS_TOKEN";//"YOUR_ACCESS_TOKEN";
    public static final String DEVICE_ID="YOUR_CORE_DEVICE_ID";//"YOUR_CORE_DEVICE_ID";
    public static final String PIN="3025";
    public static final String WEB_URL="https://api.spark.io/v1/devices/";

    /** endpoints **/
    public static final String ENDPOINT_DEVICES = "devices";
    public static final String ENDPOINT_DOOR = "door";
    public static final String ENDPOINT_TEMPERATURE = "temperature";
    public static final String ENDPOINT_TOP_DOOR_STATE = "topdoorstate";
    /**
     *
     * @return the web API URL
     */
    public static String getWebApiUrl() {
        String hostName = WEB_URL + DEVICE_ID;

        if(!hostName.endsWith("/")) {
            hostName += "/";
        }

        return hostName;
    }

/**
     *
     * @return the top door state endpoint
     */
    public static String getDeviceEndpoint() {
        return ENDPOINT_DEVICES;
    }

    /**
     *
     * @return the door endpoint
     */
    public static String getDoorEndpoint() {
        return ENDPOINT_DOOR;
    }

    /**
     *
     * @return the top door state endpoint
     */
    public static String getTopDoorStateEndpoint() {
        return ENDPOINT_TOP_DOOR_STATE;
    }

    /**
     *
     * @return the temperature endpoint
     */
    public static String getTemperatureEndpoint() {
        return ENDPOINT_TEMPERATURE;
    }
}
