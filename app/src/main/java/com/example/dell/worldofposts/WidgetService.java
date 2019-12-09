package com.example.dell.worldofposts;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

public class WidgetService extends IntentService {

    public static final String READ_PREFS="READ PREFS";

    public WidgetService() {
        super("PreferenceService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent!=null){
            if (intent.getAction().equals(READ_PREFS)){
                readPreferences();
            }
        }

    }

    public static void startTheService(Context context){
        Intent intent = new Intent(context,WidgetService.class);
        intent.setAction(READ_PREFS);
        context.startService(intent);
    }

    void readPreferences(){
        SharedPreferences sharedPref = getSharedPreferences("myprefs",Context.MODE_PRIVATE);
        String name = sharedPref.getString("title", "empty");
        String ings = sharedPref.getString("content", "empty");

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(this,WPWidget.class));
        WPWidget.updateAll(this,appWidgetManager,appWidgetIds,name,ings);


    }
}
