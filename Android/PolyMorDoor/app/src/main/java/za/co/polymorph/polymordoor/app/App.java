package za.co.polymorph.polymordoor.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

/**
 * Created by Legion on 14/10/21.
 * Copyright (c) 2014 Polymorph Systems. All rights reserved.
 */
public class App extends Application {

    /** application-wide context **/
    private static Context _context;

    //================================================================================
    // Life-cycle methods
    //================================================================================
    public void onCreate() {
        super.onCreate();
        // initialize application context
        initContext();
    }

    //================================================================================
    // Initialization helper methods
    //================================================================================
    /**
     *
     * set an application-wide context
     */
    private void initContext() {
        _context = getApplicationContext();
    }

    //================================================================================
    // Static resource methods
    //================================================================================
    /**
     *
     * @return the application context
     */
    public static Context getContext() {
        return _context;
    }

    /**
     *
     * @return the application resources
     */
    public static Resources getApplicationResources() {
        return getContext().getResources();
    }

    /**
     *
     * @return the application configuration
     */
    public static Configuration getApplicationConfiguration() {
        return getApplicationResources().getConfiguration();
    }

    /**
     *
     * @return the application locale
     */
    public static Locale getLocale() {
        return getApplicationConfiguration().locale;
    }
}
