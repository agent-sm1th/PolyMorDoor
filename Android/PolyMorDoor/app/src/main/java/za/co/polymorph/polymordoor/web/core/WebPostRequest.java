package za.co.polymorph.polymordoor.web.core;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import javax.net.ssl.SSLHandshakeException;

/**
 * 
 * @author Legion
 * 
 * A task used to perform a HTTP POST request in the background.
 *
 */
public class WebPostRequest extends WebRequest {
	
	/** HTTP request body **/
	private String _requestBody;
	
	public WebPostRequest(Context context, String webAccessPoint, String endPoint, String requestBody, WebRequestListener taskListener) {
		super(context, webAccessPoint, endPoint, taskListener);
		_requestBody = requestBody;
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		Boolean result = false;

		try {
			// submit the POST request
			if(!isCancelled() && isConnected(getContext())) {
				result = submitPostRequest(_requestBody);
			} 
		}
		catch (SSLHandshakeException e) {
			Log.e(LOG_TAG, "doInBackground::SSLHandshakeException trying submit POST request");
			e.printStackTrace();
			result = false;
		}		
		catch (NullPointerException e) {
			Log.e(LOG_TAG, "doInBackground::NullPointerException trying submit POST request");
			e.printStackTrace();
			result = false;
		}		
		catch (IOException e) {
			Log.e(LOG_TAG, "doInBackground::IOException trying submit POST request");
			e.printStackTrace();
			result = false;
		}		
		
		
		return result;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		Log.d(LOG_TAG, "WebApiPostTask onPostExecute -> url = " + getWebAccessPoint() + getEndPoint());
		Log.d(LOG_TAG, "WebApiPostTask onPostExecute -> request = " + _requestBody);
		Log.d(LOG_TAG, "WebApiPostTask onPostExecute -> result = " + result);
		Log.d(LOG_TAG, "WebApiPostTask onPostExecute -> status code = " + getStatusCode());
		Log.d(LOG_TAG, "WebApiPostTask onPostExecute -> response = " + getResponseString());
		Log.d(LOG_TAG, "WebApiPostTask onPostExecute -> error = " + getErrorString());

		if(getAsyncTaskListener() != null) {
			getAsyncTaskListener().webTaskComplete(this, result);
		}		
	}
}
