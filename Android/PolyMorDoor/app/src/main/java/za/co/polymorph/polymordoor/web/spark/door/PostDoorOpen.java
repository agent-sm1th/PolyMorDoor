package za.co.polymorph.polymordoor.web.spark.door;

import android.content.Context;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import za.co.polymorph.polymordoor.web.core.WebPostRequest;
import za.co.polymorph.polymordoor.web.core.WebRequest;
import za.co.polymorph.polymordoor.web.core.WebRequestListener;
import za.co.polymorph.polymordoor.web.core.WebUtil;

/**
 * Created by Legion on 14/11/27.
 */
public class PostDoorOpen implements WebRequestListener {
    private static final String LOG_TAG = "PostDoorOpen";
    public static int DELAY = 10 * 1000;
    private List<NameValuePair> _list;
    private Context _context;
    private PostDoorOpenListener _listener;

    public PostDoorOpen(Context context, String door, PostDoorOpenListener listener) {
        _context = context;
        _listener = listener;
        _list = new ArrayList<NameValuePair>();
        _list.add(new BasicNameValuePair("access_token", WebUtil.ACCESS_TOKEN));
        _list.add(new BasicNameValuePair("args", WebUtil.PIN + "," + door));// + "," + DELAY)); // optional arg : time to stay open
    }

    public void submit() {
        String body = WebRequest.getQuery(_list);
        WebPostRequest request  = new WebPostRequest(
                _context,
                WebUtil.getWebApiUrl(),
                WebUtil.getDoorEndpoint(),
                body,
                this);
        request.execute(true);

    }

    @Override
    public void webTaskComplete(WebRequest task, Boolean result) {
        Log.d(LOG_TAG, "webTaskComplete::result = " + result);
        _listener.postDoorOpenComplete(this, result);
    }

    @Override
    public void webTaskCancelled(WebRequest task, Boolean result) {
        Log.d(LOG_TAG, "webTaskCancelled::result = " + result);
        _listener.postDoorOpenComplete(this, false);
    }

    @Override
    public void webTaskProgress(WebRequest task, Double progress) {

    }
}
