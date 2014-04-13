package com.radiusnetworks.ibeaconreference;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Add your initialization code here
        Parse.initialize(this, "vs8lZEBLUKcBbhnWzycGe2p9Gbckacp3Rq5Lor6m", "G7ijrDFQs2JQcmZWfuNetuHjraCfePtfHpPNBIiS");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }

}
