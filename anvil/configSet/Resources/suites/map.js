/*
 * Appcelerator Titanium Mobile
 * Copyright (c) 2011-2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

module.exports = new function ()
{
	var finish;
	var valueOf;
	var Map;

	var IOS = (Ti.Platform.osname === 'iphone' || Ti.Platform.osname === 'ipad');
	var ANDROID = (Ti.Platform.osname === 'android');
	var IOS7 = (function() {
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
	})();
	
	this.init = function (testUtils)
	{
		finish = testUtils.finish;
		valueOf = testUtils.valueOf;
		Map = require('ti.map');
	};

	this.name = "map";
	
	// ---------------------------------------------------------------
	// Map
	// ---------------------------------------------------------------

	// Test that module is loaded
	this.testModule = function (testRun)
	{
		// Verify that the module is defined
		valueOf(testRun, Map).shouldBeObject();
		finish(testRun);
	};
	
	// Test that all of the Constants are defined
	this.testConstants = function (testRun)
	{
		// Annotation
		valueOf(testRun, Map.ANNOTATION_DRAG_STATE_START).shouldBeNumber();
		valueOf(testRun, Map.ANNOTATION_DRAG_STATE_END).shouldBeNumber();
		valueOf(testRun, Map.ANNOTATION_GREEN).shouldBeNumber();
		valueOf(testRun, Map.ANNOTATION_RED).shouldBeNumber();

		// Map Type
		valueOf(testRun, Map.SATELLITE_TYPE).shouldBeNumber();
		valueOf(testRun, Map.NORMAL_TYPE).shouldBeNumber();
		valueOf(testRun, Map.HYBRID_TYPE).shouldBeNumber();

		if (IOS) {
			// Annotation
			valueOf(testRun, Map.ANNOTATION_DRAG_STATE_NONE).shouldBeNumber();
			valueOf(testRun, Map.ANNOTATION_DRAG_STATE_DRAG).shouldBeNumber();
			valueOf(testRun, Map.ANNOTATION_DRAG_STATE_CANCEL).shouldBeNumber();

			valueOf(testRun, Map.ANNOTATION_PURPLE).shouldBeNumber();
		}

		if (ANDROID) {
			// Annotation
			valueOf(testRun, Map.ANNOTATION_BLUE).shouldBeNumber();
			valueOf(testRun, Map.ANNOTATION_AZURE).shouldBeNumber();
			valueOf(testRun, Map.ANNOTATION_CYAN).shouldBeNumber();
			valueOf(testRun, Map.ANNOTATION_MAGENTA).shouldBeNumber();
			valueOf(testRun, Map.ANNOTATION_ORANGE).shouldBeNumber();
			valueOf(testRun, Map.ANNOTATION_ROSE).shouldBeNumber();
			valueOf(testRun, Map.ANNOTATION_YELLOW).shouldBeNumber();
			valueOf(testRun, Map.ANNOTATION_VIOLET).shouldBeNumber();

			// Map Type
			valueOf(testRun, Map.TERRAIN_TYPE).shouldBeNumber();

			// Google Play
			valueOf(testRun, Map.SUCCESS).shouldBeNumber();
			valueOf(testRun, Map.SERVICE_MISSING).shouldBeNumber();
			valueOf(testRun, Map.SERVICE_VERSION_UPDATE_REQUIRED).shouldBeNumber();
			valueOf(testRun, Map.SERVICE_DISABLED).shouldBeNumber();
			valueOf(testRun, Map.SERVICE_INVALID).shouldBeNumber();
		}

		finish(testRun);
	};
	
	// Test that all of the Functions are defined
	this.testFunctions = function (testRun)
	{
		valueOf(testRun, Map.createView).shouldBeFunction();

		if (IOS7) {
			valueOf(testRun, Map.createCamera).shouldBeFunction();
		}

		if (ANDROID) {
			valueOf(testRun, Map.createRoute).shouldBeFunction();
		}

		finish(testRun);
	};

	// Populate the array of tests based on the 'hammer' convention
	this.tests = require('hammer').populateTests(this);
};
