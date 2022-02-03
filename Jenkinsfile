#!groovy
library 'pipeline-library'

def isMaster = env.BRANCH_NAME.equals('master')

buildModule {
	sdkVersion = '10.0.1.GA'
	npmPublish = isMaster // By default it'll do github release on master anyways too
	iosLabels = 'osx && xcode-12'
}
