#!groovy
library 'pipeline-library'

def isMaster = env.BRANCH_NAME.equals('master')

buildModule {
	sdkVersion = '9.2.0.v20200911073932' // use a master build with ARM64 sim, and macOS support
	npmPublish = isMaster // By default it'll do github release on master anyways too
	iosLabels = 'osx && xcode-12'
}
