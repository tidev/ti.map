var hammer = module.exports;
var os = Ti.Platform.osname;

function shouldRunOnPlatform(prop) {
	return !prop.hasOwnProperty('platforms') || prop.platforms[os];
}

// Suites can be filtered based on platform by adding a 'platform' property to a suite entry. This allows for
// controlling which platforms a particular tests suite is valid to run on.
//
// Examples:
//  var testSuites = [
//    { name: suite1, platforms: { iphone:1, ipad:1 } },            // valid on iphone and ipad
//    { name: suite2 },                                             // valid on all platforms
//    { name: suite3, platforms: { android:1, mobileweb:1 }         // valid on android and mobileweb

hammer.populateSuites = function (testSuites) {
	var result = [];
	var len = testSuites.length;
	for (var i = 0; i < len; i++) {
		if (shouldRunOnPlatform(testSuites[i])) {
			result.push({ name:testSuites[i].name });
		}
	}

	return result;
};


// Convention: A recognized test method starts with 'test' and is followed by an uppercase character, a number, or an
// underscore. Use this technique to have your list of tests automatically populated to avoid typos in filling out
// the list of test functions.
//
// Examples:
//  this.testModule
//  this.test_Module
//  this.test1
//
// Tests can be filtered based on platform by adding a 'platform' property to the function. This allows for
// controlling which platforms a particular test is run on, if necessary.
// Examples:
//  this.testModule = function (testRun) {....};
//  this.testModule.platforms = { android:1, iphone:1, ipad:1 };    // only run on android and iOS
//  this.testModule.platforms = { mobileweb:1 };                    // only run on mobileweb

hammer.populateTests = function (obj, timeout) {
	var result = [];
	var re = new RegExp("^test[A-Z_0-9]");
	for (var key in obj) {
		if ((typeof obj[key] === "function") &&
			re.test(key) &&
			shouldRunOnPlatform(obj[key])) {
			var test = { name:key };
			if (timeout) {
				test.timeout = timeout;
			}
			result.push(test);
		}
	}

	return result;
};