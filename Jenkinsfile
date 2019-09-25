#!groovy
library 'pipeline-library'

def isMaster = env.BRANCH_NAME.equals('master')

buildModule {
	sdkVersion = '8.1.1.GA' // use a master build with ARM64 support
	npmPublish = false // By default it'll do github release on master anyways too
	iosLabels = 'osx && xcode-11'
}
