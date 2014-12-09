package za.co.polymorph.polymordoor.web.spark.temperature;

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
public class GetTemperature implements WebRequestListener {
    private static final String LOG_TAG = "GetTemperature";
    private List<NameValuePair> _list;
    private Context _context;
    private GetTemperatureListener _listener;
    private Double _temperature;

    public GetTemperature(Context context, GetTemperatureListener listener) {
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
                WebUtil.getTemperatureEndpoint() + "?" + body,
                this);
        request.execute(true);
    }

    public Double getTemperature() {
        return _temperature;
    }

    private void setTemperature(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            _temperature = jsonObject.getDouble("result");
        } catch(JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void webTaskComplete(WebRequest task, Boolean result) {
        Log.d(LOG_TAG, "webTaskComplete::result = " + result);

        if(result) {
            setTemperature(task.getResponseString());
        }

        _listener.getTemperatureComplete(this, result);
    }

    @Override
    public void webTaskCancelled(WebRequest task, Boolean result) {
        Log.d(LOG_TAG, "webTaskCancelled::result = " + result);
        _listener.getTemperatureComplete(this, false);
    }

    @Override
    public void webTaskProgress(WebRequest task, Double progress) {

    }
}
