import com.kms.katalon.core.annotation.AfterTestCase
import com.kms.katalon.core.annotation.AfterTestSuite
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext
import com.kms.katalon.keyword.microsoftteam.MicrosoftTeamUpdate

class SampleListener {
	MicrosoftTeamUpdate microsoftUpdate = new  MicrosoftTeamUpdate()
	
	/**
	 * Executes after every test case ends.
	 * @param testCaseContext related information of the executed test case.
	 */
	@AfterTestCase
	def synchronized sampleAfterTestCase(TestCaseContext testCaseContext) {
		microsoftUpdate.getTestcaseStatus(testCaseContext)
	}

	/**
	 * Executes after every test suite ends.
	 * @param testSuiteContext: related information of the executed test suite.
	 */
	@AfterTestSuite
	def sampleAfterTestSuite(TestSuiteContext testSuiteContext) {
		microsoftUpdate.updateMicrosoftTeam(testSuiteContext)
	}
}