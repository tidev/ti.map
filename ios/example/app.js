
/**
 * If the table view rows are too small on Android, add the following to your tiapp.xml
 * 
<android xmlns:android="http://schemas.android.com/apk/res/android">
    <manifest>
        <supports-screens android:anyDensity="false"/>
    </manifest>
</android>
 *
 */

var IOS = (Ti.Platform.osname === 'iphone' || Ti.Platform.osname === 'ipad');
var ANDROID = (Ti.Platform.osname === 'android');
var UI = require('ui');
var Map = require('ti.map');

function isIOS7Plus() {
    if (Titanium.Platform.name == 'iPhone OS')
    {
        var version = Titanium.Platform.version.split(".");
        var major = parseInt(version[0],10);

        if (major >= 7)
        {
            return true;
        }
    }
    return false;
}

Ti.API.info("iOS7: "+isIOS7Plus());
var top = isIOS7Plus() ? 20 : 0;

//=====================================================================
// Rows
//=====================================================================  
var rows = [
    require('tests/multiMap'),
    require('tests/annotations'),
    require('tests/routes')
];

if (IOS) {
    rows.push(require('tests/camera'));
    rows.push(require('tests/properties'));
}

if (ANDROID) {
    var code = Map.isGooglePlayServicesAvailable();

    if (code != Map.SUCCESS) {
        alert ("Google Play Services is not installed/updated/available");
    } else {
        startUI();
    }
} else {
    startUI();
}

function startUI() {
    UI.init(rows, function(e) {
        rows[e.index].run && rows[e.index].run(UI, Map);
    });
}
