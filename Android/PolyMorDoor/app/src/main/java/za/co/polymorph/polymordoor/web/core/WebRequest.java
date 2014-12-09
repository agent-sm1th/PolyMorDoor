package za.co.polymorph.polymordoor.web.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import org.apache.http.NameValuePair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.net.ssl.SSLHandshakeException;

/**
 *
 * @author Legion
 *
 * An abstract base task used to manage HTTP requests. 
 * This class is intended to be extended for each HTTP request, 
 * and is used to store the HTTP response.  
 *
 */
public abstract class WebRequest extends AsyncTask<Void, Double, Boolean> {
    protected static final String LOG_TAG = "WebApiTask";

    /** context **/
    protected Context _context;
    /** remote API url **/
    protected String _webAccessPoint;
    /** remote API endpoint **/
    protected String _endPoint;
    /** a listener alert on a state change of the request **/
    protected WebRequestListener _listener;

    /** the HTTP response code **/
    protected Integer _statusCode;
    /** the HTTP success response stream **/
    private InputStream _responseStream;
    /** the HTTP response string **/
    private String _responseString;
    /** the HTTP error response stream **/
    private InputStream _errorStream;
    /** the HTTP error string **/
    private String _errorString;

    //================================================================================
    // Life-cycle methods
    //================================================================================
    public WebRequest(Context context, String webAccessPoint, String endPoint, WebRequestListener listener) {
        super();
        _context = context;
        _listener = listener;

        setWebAccessPoint(webAccessPoint);
        setEndPoint(endPoint);
    }

    //================================================================================
    // Public response helper methods
    //================================================================================
    /**
     *
     * @return the context
     */
    public Context getContext() {
        return _context;
    }

    /**
     *
     * @return the remote API url
     */
    public String getWebAccessPoint() {
        return _webAccessPoint;
    }

    /**
     *
     * @return the API endpoint
     */
    public String getEndPoint() {
        return _endPoint;
    }

    /**
     *
     * @return the response status code
     */
    public Integer getStatusCode() {
        return _statusCode;
    }

    /**
     *
     * @return the success response stream
     */
    public InputStream getResponseStream() {
        return _responseStream;
    }

    /**
     *
     * @return the success response string
     */
    public String getResponseString() {
        return _responseString;
    }

    /**
     *
     * @return the error response stream
     */
    public InputStream getErrorStream() {
        return _errorStream;
    }

    /**
     *
     * @return the error response string
     */
    public String getErrorString() {
        return _errorString;
    }

    //================================================================================
    // Helper methods
    //================================================================================
    /**
     *
     * @return the task listener
     */
    protected WebRequestListener getAsyncTaskListener() {
        return _listener;
    }

    /**
     * set the remote API url and ensure correct formatting
     *
     * @param webAccessPoint - the remote API url
     */
    private void setWebAccessPoint(String webAccessPoint) {

        _webAccessPoint = (webAccessPoint == null) ? "" : webAccessPoint;

        if(!_webAccessPoint.endsWith("/")) {
            _webAccessPoint += "/";
        }
    }

    /**
     * set the remote API endpoint and ensure correct formatting
     *
     * @param endPoint - the remote API endpoint
     */
    private void setEndPoint(String endPoint) {

        _endPoint = (endPoint == null) ? "" : endPoint;

//		if(_endPoint.endsWith("/") && _endPoint.length() > 1) {
//			_endPoint = _endPoint.substring(0, _endPoint.length() - 1);
//		}
    }

    /**
     *
     * @param code - the response code
     */
    private void setStatusCode(Integer code) {
        _statusCode = code;
    }

    /**
     *
     * @param stream - the success response stream
     */
    private void setResponseStream(InputStream stream) {
        _responseStream = stream;
    }

    /**
     *
     * @param string - the success response string
     */
    private void setResponseString(String string) {
        _responseString = string;
    }

    /**
     *
     * @param stream - the error response stream
     */
    private void setErrorStream(InputStream stream) {
        _errorStream = stream;
    }

    /**
     *
     * @param string - the error response string
     */
    private void setErrorString(String string) {
        _errorString = string;
    }
    //================================================================================
    // Connection helpers
    //================================================================================
    /**
     * check whether the device is connected
     *
     * @param context - the application context
     * @return a boolean whether the device has a connection
     */
    public static boolean isConnected(Context context) {
        boolean isConnected = false;
        // Check Internet connectivity
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    //================================================================================
    // HTTP response helpers
    //================================================================================
    /**
     * check whether the request response is good by comparing it to common error responses
     *
     * @param statusCode
     * @return a boolean whether the response was successful
     */
    public boolean isStatusCodeOk(Integer statusCode) {

        if(statusCode == null) {
            return false;
        }

        if(Arrays.asList(WebUtil.LIST_STATUS_CODE_BAD).contains(statusCode)) {
            return false;
        }

        return true;
    }

    //================================================================================
    // HTTP connection methods
    //================================================================================
    /**
     * save the request response
     * there is a bug in the fetching of the response code,
     * so this must be called twice.
     * if an error occurred, the response is included in the
     * error stream, rather than the input stream
     *
     * @param connection - the URL connection
     * @return a boolean whether the request was successful
     */
    private boolean saveResponse(HttpURLConnection connection) {
        boolean result = false;

        // getLatest response code (try twice as this method throws an exception)
        try {
            connection.getResponseCode();
        } catch(IOException e) {
            // expected exception, allow this to fail silently
        }

        // store the response code
        try {
            int responseCode = connection.getResponseCode();
            setStatusCode(responseCode);
        } catch(IOException e) {
            Log.e(LOG_TAG, "saveResponse::IOException getting response code from connection  \"" + connection + "\"");
            e.printStackTrace();
        }

        // store the response stream (this throws an exception if the call was unsuccessful)
        try {
            InputStream responseStream = null;
            responseStream = connection.getInputStream();
            setResponseStream(responseStream);
            setResponseString(getStringFromStream(responseStream));
            result = true;
        } catch(IOException e) {
            Log.e(LOG_TAG, "saveResponse::IOException getting input stream from connection  \"" + connection + "\"");
            e.printStackTrace();
            result = false;
        }

        // store the error stream if unsuccessful
        if(!result) {
            InputStream errorStream = null;
            errorStream = connection.getErrorStream();
            setErrorString(getStringFromStream(errorStream));
            setErrorStream(errorStream);
        }

        return result;
    }

    /**
     * submit a HTTP request
     *
     * @return
     * @throws NullPointerException
     * @throws java.io.IOException
     */
    public boolean submitGetRequest(String body) throws NullPointerException, SSLHandshakeException, IOException {
        String url = getWebAccessPoint() + getEndPoint();
        HttpURLConnection connection = getHttpUrlConnection(url, "GET");

        boolean result = false;
        // TODO: add body
        connection.connect(); // necessary?
        result = saveResponse(connection);
        connection.disconnect();
        System.gc();

        return result;
    }

    /**
     * submit a HTTP request based on the request type (POST, PUT)
     *
     * @return a boolean whether the request was successful
     * @throws NullPointerException, IOException
     */
    public boolean submitPostRequest(String body) throws NullPointerException, SSLHandshakeException, IOException {
        String url = getWebAccessPoint() + getEndPoint();
        HttpURLConnection connection = getHttpUrlConnection(url, "POST");
        boolean result = false;

        connection.setDoInput(true);
        connection.setDoOutput(true);

        OutputStream outputStream = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        writer.write(body);
        writer.flush();
        writer.close();
        outputStream.close();

        result = saveResponse(connection);

        connection.disconnect();
        System.gc();

        return result;
    }

    //================================================================================
    // HTTP client helpers
    //================================================================================
    /**
     *
     * @param url - the url to connect to
     * @return a HTTP url connection
     * @throws java.io.IOException
     */
    public HttpURLConnection getHttpUrlConnection(String url) throws IOException {
        HttpURLConnection connection = null;
        URL obj = new URL(url);
        connection = (HttpURLConnection) obj.openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setRequestProperty(WebUtil.HEADER_CONTENT_TYPE, WebUtil.HEADER_CONTENT_TYPE_VALUE);
        return connection;
    }

    /**
     *
     * @param url - the url to connect to
     * @param request - the url-encoded request body
     * @return a HTTP url connection
     * @throws java.io.IOException
     */
    public HttpURLConnection getHttpUrlConnection(String url, String request) throws IOException {
        HttpURLConnection connection = getHttpUrlConnection(url);
        connection.setRequestMethod(request);
        return connection;
    }

    //================================================================================
    // HTTP encode and consume helpers
    //================================================================================
    /**
     * getLatest a url-encoded string of name value pairs
     *
     * @param params - the name value pairs to be encoded
     * @param sort - whether to sort the pairs according to their name
     * @param startsWith - a string to prefix the encoded string with
     * @return a url-encoded string
     */
    public static String getQueryString(List<NameValuePair> params, boolean sort, String startsWith) {
        String queryString = getQueryString(params, sort);

        if(queryString != null && queryString.length() > 0) {
            queryString = startsWith + queryString;
        }

        return queryString;
    }

    /**
     * getLatest a url-encoded string of name value pairs
     *
     * @param params - the name value pairs to be encoded
     * @param sort - whether to sort the pairs according to their name
     * @return a url-encoded string
     */
    public static String getQueryString(List<NameValuePair> params, boolean sort) {

        if(params == null) {
            return "";
        }

        String queryString = "";

        if(sort) {
            Collections.sort(params, new Comparator<NameValuePair>() {
                @Override
                public int compare(NameValuePair e1, NameValuePair e2) {
                    return e1.getName().compareTo(e2.getName());
                }
            });
        }

        queryString = getQuery(params);

        return queryString;
    }

    /**
     * getLatest a url-encoded string of name value pairs
     *
     * @param params - the name value pairs to be encoded
     * @return a url-encoded string
     */
    public static String getQuery(List<NameValuePair> params) {

        if(params == null) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first) {
                first = false;
            }
            else {
                result.append("&");
            }

            result.append(URLEncoder.encode(pair.getName()));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue()));
        }

        return result.toString();
    }

    /**
     * getLatest a string from an input stream
     *
     * @param inputStream
     * @return
     */
    public String getStringFromStream(InputStream inputStream) {

        if(inputStream == null) {
            return null;
        }

        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder total = new StringBuilder();
        String line;

        try{

            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            r.close();
        }
        catch(IOException e) {
            Log.e(LOG_TAG, "getStringFromStream::IOException trying to read line from input stream  \"" + inputStream + "\"");
            e.printStackTrace();
            return "";
        }

        return total.toString();
    }

    //================================================================================
    // Async task methods
    //================================================================================
    /**
     * an execute method for AsyncTask that can specify if the task should be run concurrently
     * pre-HoneyComb, this was the default setting.
     *
     * @param concurrent - a boolean whether to run this task concurrently
     * @param params
     * @return the task
     */
    public AsyncTask<Void, Double, Boolean> execute(boolean concurrent, Void...params) {

        if(concurrent) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                return executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }

        return execute();
    }

    /**
     * this is run on a background thread
     * all work should go here
     */
    @Override
    protected abstract Boolean doInBackground(Void... params);

    /**
     * this is run on the UI thread
     * this is called when the task is complete
     */
    @Override
    protected abstract void onPostExecute(Boolean result);

    /**
     * this is run on the UI thread
     * this is called when the task makes progress
     */
    @Override
    protected void onProgressUpdate(Double... values) {

        if(getAsyncTaskListener() != null && values.length > 0) {
            getAsyncTaskListener().webTaskProgress(this, values[0]);
        }
    }

    /**
     * this is run on the UI thread
     * this is called when the task makes progress
     */
    @Override
    protected void onCancelled() {

        if(getAsyncTaskListener() != null) {
            getAsyncTaskListener().webTaskCancelled(this, false);
        }
    }

    /**
     * this is run on the UI thread
     * this is called when the task is cancelled
     *
     * @param - whether the task completed successfully
     */
    @Override
    protected void onCancelled(Boolean result) {

        if(getAsyncTaskListener() != null) {
            getAsyncTaskListener().webTaskCancelled(this, result);
        }
    }
}