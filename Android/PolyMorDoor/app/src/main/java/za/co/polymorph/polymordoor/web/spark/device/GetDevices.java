package za.co.polymorph.polymordoor.web.spark.device;

import android.content.Context;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import za.co.polymorph.polymordoor.web.core.WebGetRequest;
import za.co.polymorph.polymordoor.web.core.WebRequest;
import za.co.polymorph.polymordoor.web.core.WebRequestListener;
import za.co.polymorph.polymordoor.web.core.WebUtil;

/**
 * Created by Legion on 14/11/27.
 */
public class GetDevices implements WebRequestListener {
    private static final String LOG_TAG = "GetDevices";
    private List<NameValuePair> _list;
    private Context _context;
    private GetDeviceListener _listener;
    private boolean _isConnected;

    public GetDevices(Context context, GetDeviceListener listener) {
        _context = context;
        _listener = listener;
        _list = new ArrayList<NameValuePair>();
        _list.add(new BasicNameValuePair("access_token", WebUtil.ACCESS_TOKEN));
    }

    public void submit() {
        String body = WebRequest.getQuery(_list);
        WebGetRequest request  = new WebGetRequest(
                _context,
                WebUtil.getWebApiUrl(),
                "?" + body,
                this);
        request.execute(true);
    }

    public boolean isConnected() {
        return _isConnected;
    }

    private void setConnected(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            _isConnected = jsonObject.getBoolean("connected");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void webTaskComplete(WebRequest task, Boolean result) {
        Log.d(LOG_TAG, "webTaskComplete::result = " + result);

        if(result) {
            setConnected(task.getResponseString());
        }

        _listener.getDeviceComplete(this, result);
    }

    @Override
    public void webTaskCancelled(WebRequest task, Boolean result) {
        Log.d(LOG_TAG, "webTaskCancelled::result = " + result);
        _listener.getDeviceComplete(this, false);
    }

    @Override
    public void webTaskProgress(WebRequest task, Double progress) {

    }
}
