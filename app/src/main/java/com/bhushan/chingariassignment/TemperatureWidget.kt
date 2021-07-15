package com.bhushan.chingariassignment

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 * - onUpdate, invoke from caller to update new widget UI
 * - Shared Preference, provides current temperature value
 */
class TemperatureWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        val currentTemp = SharedPrefHelper.getCurrentTemp(context)
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, currentTemp)
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        currentTempString: String?
    ) {
        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.temp_widget)
        views.setTextViewText(R.id.appwidget_text, context.resources.getString(R.string.temp, currentTempString))
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}

