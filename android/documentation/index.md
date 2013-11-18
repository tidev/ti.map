## Description
 External version of Map module to support new Google Map v2 sdk

## Getting Started
 Obtain map API key from google. You can find instructions here: 
    https://developers.google.com/maps/documentation/android/start#the_google_maps_api_key
  

## Requirements
 Testing device needs to have Google Play installed. Otherwise map won't work.
 Add this to tiapp.xml - replace "API KEY HERE" with your API key.

        <manifest>
            <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
            <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
            <uses-permission android:name="com.google.android.providers.gsf.permission.READ_GSERVICES"/>
            <permission android:name="com.map.permission.MAPS_RECEIVE" android:protectionLevel="signature"/>
            <uses-feature android:glEsVersion="0x00020000" android:required="true"/>
            <uses-permission android:name="com.map.permission.MAPS_RECEIVE"/>
            <application>
                <meta-data
                    android:name="com.google.android.maps.v2.API_KEY" android:value="API KEY HERE"/>
            </application>
        </manifest>
  
## Accessing the Ti.Map Module
To access this module from JavaScript, you would do the following:

	var map = require('ti.map');

## Module History

View the [change log](changelog.html) for this module.

## Documentation

  * [Google Maps v2 for Android guide](http://docs.appcelerator.com/titanium/latest/#!/guide/Google_Maps_v2_for_Android)
  * [Map Module API Reference Documentation](http://docs.appcelerator.com/titanium/latest/#!/api/Modules.Map)

## Feedback and Support

Please direct all questions, feedback, and concerns to [info@appcelerator.com](mailto:info@appcelerator.com?subject=Android%20Map%20Module).

## Author

Hieu Pham

## License
Copyright(c) 2013 by Appcelerator, Inc. All Rights Reserved. Please see the LICENSE file included in the distribution for further details.
