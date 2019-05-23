package com.kms.katalon.keyword.microsoftteam

import java.util.concurrent.atomic.AtomicInteger

import org.apache.commons.lang3.StringUtils

import com.andrewthom.microsoft.teams.api.MicrosoftTeams
import com.andrewthom.microsoft.teams.api.Webhook
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext
import com.kms.katalon.core.setting.BundleSettingStore
import com.kms.katalon.core.util.KeywordUtil

public class MicrosoftTeamUpdate {


	static BundleSettingStore bundleSetting
	static String URL
	Map<String, AtomicInteger> stats = new HashMap<String, AtomicInteger>()

	static {
		try {
			bundleSetting = new BundleSettingStore(RunConfiguration.getProjectDir(), 'MicrosoftTeamUpdate', true)
			URL = bundleSetting.getString('MicrosoftTeam URL', '')
			if (StringUtils.isBlank(URL)) {
				throw new IllegalStateException("The MicrosoftTeam URL is missing.")
			}
		} catch (Exception e) {
			e.printStackTrace()
			throw e
		}
	}

	/**
	 * Get test cases status.
	 *
	 * @param testCaseContext related information of the executed test case.
	 */
	public getTestcaseStatus (TestCaseContext testCaseContext) {
		String status = testCaseContext.getTestCaseStatus()
		AtomicInteger stat = stats.get(status)
		if (stat == null) {
			stat = new AtomicInteger(0)
		}
		stat.getAndIncrement()
		stats.put(status, stat)
	}

	/**
	 * Update test result summary to Microsoft Team.
	 *
	 * @param testSuiteContext related information of the executed test suite.
	 */
	public updateMicrosoftTeam(TestSuiteContext testSuiteContext) {
		String message = "Summary execution result of test suite: " + testSuiteContext.getTestSuiteId()
		for (Map.Entry<String, AtomicInteger> entry : stats.entrySet()) {
			message =  message + "\nTotal testcases: " + entry.getKey() + " is " + entry.getValue() + "."
		}
		MicrosoftTeams.forUrl(new Webhook() {
					@Override
					public String getUrl() {
						return URL
					}
				}).sendMessage(message)
	}
}
