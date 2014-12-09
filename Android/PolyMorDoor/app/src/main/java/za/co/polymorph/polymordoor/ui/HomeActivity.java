package za.co.polymorph.polymordoor.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

import za.co.polymorph.polymordoor.R;
import za.co.polymorph.polymordoor.util.connect.ConnectivityChangeListener;
import za.co.polymorph.polymordoor.util.connect.ConnectivityChangeReceiver;
import za.co.polymorph.polymordoor.util.door.DoorType;
import za.co.polymorph.polymordoor.util.door.DoorUtil;
import za.co.polymorph.polymordoor.web.spark.device.GetDeviceListener;
import za.co.polymorph.polymordoor.web.spark.device.GetDevices;
import za.co.polymorph.polymordoor.web.spark.door.GetTopDoorState;
import za.co.polymorph.polymordoor.web.spark.door.GetTopDoorStateListener;
import za.co.polymorph.polymordoor.web.spark.door.PostDoorOpen;
import za.co.polymorph.polymordoor.web.spark.door.PostDoorOpenListener;
import za.co.polymorph.polymordoor.web.spark.temperature.GetTemperature;
import za.co.polymorph.polymordoor.web.spark.temperature.GetTemperatureListener;


public class HomeActivity extends Activity implements ConnectivityChangeListener, GetDeviceListener, GetTemperatureListener, GetTopDoorStateListener, PostDoorOpenListener {
    private static final int DOOR_DELAY = 500 + PostDoorOpen.DELAY;
    private static final int POLL_DELAY = 1000;

    private Button _topDoorButton;
    private Button _bottomDoorButton;
    private Button _refreshButton;

    private TextView _tempTextView;
    private TextView _doorStateTextView;
    private Set<Object> _requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectivityChangeReceiver.addConnectivityChangeListener(this);
        setContentView(R.layout.activity_home);
        _requests = new HashSet<Object>();
        getActionBar().hide();
        initView();
        enableAll(false, false);
        refresh();
    }

    @Override
    public void onDestroy() {
        ConnectivityChangeReceiver.removeConnectivityChangeListener(this);
        super.onDestroy();
    }

    public void onClickRefresh(View view) {
        refresh();
    }

    public void onClickTopDoor(View view) {
        postDoorOpen(DoorUtil.getDoorId(DoorType.DOOR_TOP));
        _topDoorButton.setEnabled(false);
    }

    public void onClickBottomDoor(View view) {
        postDoorOpen(DoorUtil.getDoorId(DoorType.DOOR_BOTTOM));
        _bottomDoorButton.setEnabled(false);
    }

    private void initView() {
        _topDoorButton = (Button) findViewById(R.id.button_top_door);
        _bottomDoorButton = (Button) findViewById(R.id.button_bottom_door);
        _refreshButton = (Button) findViewById(R.id.button_refresh);
        _tempTextView = (TextView) findViewById(R.id.text_view_temp);
        _doorStateTextView = (TextView) findViewById(R.id.text_view_top_door_state);

        setButtonText(_topDoorButton, DoorType.DOOR_TOP, false);
        setButtonText(_bottomDoorButton, DoorType.DOOR_BOTTOM, false);
    }

    private void updateView() {
        _refreshButton.setEnabled(true);
    }

    private void enableButtons(boolean isEnabled) {
        _topDoorButton.setEnabled(isEnabled);
        _bottomDoorButton.setEnabled(isEnabled);
    }

    private void enableAll(boolean isEnabled, boolean enableRefresh) {
        enableButtons(isEnabled);

        if(enableRefresh) {
            _refreshButton.setEnabled(isEnabled);
        }

        _tempTextView.setEnabled(isEnabled);
        _doorStateTextView.setEnabled(isEnabled);
    }

    private void setButtonText(Button button, DoorType door, boolean open) {
        String openText = (open) ? "Close" : "Open";
        button.setText(openText + " " + DoorUtil.getDoorName(door));
    }

    private void refresh() {
        _refreshButton.setEnabled(false);
        getDevices();
        getTemperature();
        getTopDoorState();
    }

    private void postDoorOpen(String door) {
        PostDoorOpen request = new PostDoorOpen(this, door, this);
        _requests.add(request);
        request.submit();
    }

    private void getDevices() {
        GetDevices request = new GetDevices(this, this);
        _requests.add(request);
        request.submit();
    }

    private void getTopDoorState() {
        Log.d("HOME", "getTopDoorState");
        GetTopDoorState request = new GetTopDoorState(this, this);
        _requests.add(request);
        request.submit();
    }

    private void getTemperature() {
        GetTemperature request = new GetTemperature(this, this);
        _requests.add(request);
        request.submit();
    }

    private void requestComplete(Object obj) {
        _requests.remove(obj);

        if(_requests.isEmpty()) {
            updateView();
        }
    }

    @Override
    public void getDeviceComplete(GetDevices obj, boolean result) {
        requestComplete(obj);
        enableAll(obj.isConnected(), false);
    }

    @Override
    public void getTemperatureComplete(GetTemperature obj, boolean result) {
        requestComplete(obj);

        if(result) {
            String temp = String.format("%.1f °C", obj.getTemperature());
            _tempTextView.setText(temp);
        }
    }

    @Override
    public void getTopDoorStateComplete(GetTopDoorState obj, boolean result) {
        requestComplete(obj);

        if(result) {
            String state = (obj.isDoorOpen()) ? "OPEN" : "CLOSED";
            _doorStateTextView.setText(state);
        }
    }

    @Override
    public void postDoorOpenComplete(PostDoorOpen obj, boolean result) {
        requestComplete(obj);
        enableButtons(true);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getTopDoorState();
            }
        }, 500);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getTopDoorState();
            }
        }, 3000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getTopDoorState();
            }
        }, 11000);
    }

    @Override
    public void connectivityChanged(boolean isConnected) {
        enableAll(isConnected, true);
    }
}
