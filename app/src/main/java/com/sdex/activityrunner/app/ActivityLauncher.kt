package com.sdex.activityrunner.app

import android.app.Activity
import com.google.android.material.snackbar.Snackbar
import com.sdex.activityrunner.R
import com.sdex.activityrunner.intent.IntentBuilderActivity
import com.sdex.activityrunner.preferences.AppPreferences
import com.sdex.activityrunner.preferences.SettingsActivity
import com.sdex.activityrunner.util.IntentUtils
import com.sdex.activityrunner.util.RunActivityTask

class ActivityLauncher(
    private val activity: Activity
) {

    fun launchActivity(model: ActivityModel) {
        if (model.exported) {
            IntentUtils.launchActivity(activity, model.componentName, model.name)
        } else {
            tryRunWithRoot(model)
        }
    }

    fun launchActivityWithRoot(model: ActivityModel) {
        tryRunWithRoot(model)
    }

    fun launchActivityWithParams(model: ActivityModel) {
        IntentBuilderActivity.start(activity, model)
    }

    private fun tryRunWithRoot(model: ActivityModel) {
        val appPreferences = AppPreferences(activity)
        if (appPreferences.isRootIntegrationEnabled) {
            val runActivityTask = RunActivityTask(model.componentName)
            runActivityTask.execute()
        } else {
            Snackbar.make(
                activity.findViewById(android.R.id.content),
                R.string.settings_error_root_not_active,
                Snackbar.LENGTH_LONG
            ).setAction(
                R.string.action_settings
            ) {
                SettingsActivity.start(activity)
            }.show()
        }
    }
}
