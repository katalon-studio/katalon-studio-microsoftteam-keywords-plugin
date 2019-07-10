package com.kms.katalon.keyword.microsoftteams

import java.util.concurrent.atomic.AtomicInteger

import org.apache.commons.lang3.StringUtils

import com.andrewthom.microsoft.teams.api.MicrosoftTeams
import com.andrewthom.microsoft.teams.api.Webhook
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext
import com.kms.katalon.core.setting.BundleSettingStore
import com.kms.katalon.core.util.KeywordUtil

public class MicrosoftTeamsUtils {


	static BundleSettingStore bundleSetting
	static String URL
	static boolean enabled = false
	
	Map<String, AtomicInteger> stats = new HashMap<String, AtomicInteger>()

	static {
		try {
			bundleSetting = new BundleSettingStore(RunConfiguration.getProjectDir(), 'MicrosoftTeamsIncomingWebhook', true)
			URL = bundleSetting.getString('MicrosoftTeamsIncomingWebhook', '')
			if (StringUtils.isBlank(URL)) {
				KeywordUtil.logInfo("Microsoft Teams Incoming Webhook is empty.")
			} else {
				enabled = true
			}
		} catch (Exception e) {
			e.printStackTrace()
		}
	}

	/**
	 * Get test cases status.
	 *
	 * @param testCaseContext related information of the executed test case.
	 */
	public getTestcaseStatus (TestCaseContext testCaseContext) {
		if (enabled) {
			String status = testCaseContext.getTestCaseStatus()
			AtomicInteger stat = stats.get(status)
			if (stat == null) {
				stat = new AtomicInteger(0)
			}
			stat.getAndIncrement()
			stats.put(status, stat)
		}
	}

	/**
	 * Update test result summary to Microsoft Team.
	 *
	 * @param testSuiteContext related information of the executed test suite.
	 */
	public updateMicrosoftTeam(TestSuiteContext testSuiteContext) {
		if (enabled) {
			String message = "Summary execution result of Test Suite: " + testSuiteContext.getTestSuiteId()
			for (Map.Entry<String, AtomicInteger> entry : stats.entrySet()) {
				message =  message + "\n\n${entry.getKey()}: ${entry.getValue()}"
			}
			MicrosoftTeams.forUrl(new Webhook() {
						@Override
						public String getUrl() {
							return URL
						}
					}).sendMessage(message)
		}
	}
}
