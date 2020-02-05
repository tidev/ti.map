#!groovy
library 'pipeline-library'

def isMaster = env.BRANCH_NAME.equals('master')

buildModule {
	sdkVersion = '9.0.0.v20200205142057' // use a master build with ARM64 support
	npmPublish = isMaster // By default it'll do github release on master anyways too
	iosLabels = 'osx && xcode-11'
}
