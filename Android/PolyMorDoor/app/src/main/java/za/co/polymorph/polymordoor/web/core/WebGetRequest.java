package za.co.polymorph.polymordoor.web.core;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import javax.net.ssl.SSLHandshakeException;

/**
 * 
 * @author Legion
 * 
 * A task used to perform a HTTP GET request in the background.
 *
 */
public class WebGetRequest extends WebRequest {
	
	public WebGetRequest(Context context, String webAccessPoint, String endPoint, WebRequestListener taskListener) {
		super(context, webAccessPoint, endPoint, taskListener);
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		Boolean result = false;

		try {
			// submit the GET request
			if(!isCancelled() && isConnected(getContext())) {
				result = submitGetRequest(null);
			}
		}
		catch (SSLHandshakeException e) {
			Log.e(LOG_TAG, "doInBackground::SSLHandshakeException trying submit GET request");
			e.printStackTrace();
			result = false;
		}		
		catch (NullPointerException e) {
			Log.e(LOG_TAG, "doInBackground::NullPointerException trying submit GET request");
			e.printStackTrace();
			result = false;
		}		
		catch (IOException e) {
			Log.e(LOG_TAG, "doInBackground::IOException trying submit GET request");
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		Log.d(LOG_TAG, "WebApiGetTask onPostExecute -> url = " + getWebAccessPoint() + getEndPoint());
		Log.d(LOG_TAG, "WebApiGetTask onPostExecute -> result = " + result);
		Log.d(LOG_TAG, "WebApiGetTask onPostExecute -> status code = " + getStatusCode());
		Log.d(LOG_TAG, "WebApiGetTask onPostExecute -> response = " + getResponseString());
		Log.d(LOG_TAG, "WebApiGetTask onPostExecute -> error = " + getErrorString());
		
		if(getAsyncTaskListener() != null) {
			getAsyncTaskListener().webTaskComplete(this, result);
		}
	}
}
