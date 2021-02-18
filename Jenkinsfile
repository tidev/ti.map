#!groovy
library 'pipeline-library'

def isMaster = env.BRANCH_NAME.equals('master')

buildModule {
	sdkVersion = '9.3.2.GA'
	npmPublish = isMaster // By default it'll do github release on master anyways too
	iosLabels = 'osx && xcode-12'
}
