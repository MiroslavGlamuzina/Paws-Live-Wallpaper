package remingtonproductions.pawslivewallpaper;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.authorwjf.live_wallpaper_p1.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Random;

public class Tools {

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static int randInt(int min, int max, Random rand) {
//        ThreadLocalRandom.current().nextInt(min, max + 1);
        return rand.nextInt((max - min) + 1) + min;
    }

    public static String StrCOLOR_ONE = "#66FFB2";
    public static String StrCOLOR_TWO = "#99FFFF";
    public static String StrCOLOR_THREE = "#FF99CC";
    public static String StrCOLOR_FOUR = "#FFB266";
    public static String StrCOLOR_FIVE = "#6666FF";
    public static String StrCOLOR_SIX = "#FFFF33";
    public static String StrCOLOR_SEVEN = "#FF3333";
    public static String StrCOLOR_CUSTOM = "#FF33FF";
    public static int CUSTOM_COLOR = 0;

    //todo return int for the color
    public static int getColorPreference(Context c) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        if (sharedPref.getBoolean(c.getString(R.string.pref_color_color_one), true)) {
            return Color.parseColor(StrCOLOR_ONE);
        } else if (sharedPref.getBoolean(c.getString(R.string.pref_color_color_two), false)) {
            return Color.parseColor(StrCOLOR_TWO);
        } else if (sharedPref.getBoolean(c.getString(R.string.pref_color_color_three), false)) {
            return Color.parseColor(StrCOLOR_THREE);
        } else if (sharedPref.getBoolean(c.getString(R.string.pref_color_color_four), false)) {
            return Color.parseColor(StrCOLOR_FOUR);
        } else if (sharedPref.getBoolean(c.getString(R.string.pref_color_color_five), false)) {
            return Color.parseColor(StrCOLOR_FIVE);
        } else if (sharedPref.getBoolean(c.getString(R.string.pref_color_color_six), false)) {
            return Color.parseColor(StrCOLOR_SIX);
        } else if (sharedPref.getBoolean(c.getString(R.string.pref_color_color_seven), false)) {
            return Color.parseColor(StrCOLOR_SEVEN);
        } else if (sharedPref.getBoolean(c.getString(R.string.pref_color_color_custom), false)) {
            if (SettingsActivity.CUSTOM_COLOR != 0) {
                return SettingsActivity.CUSTOM_COLOR;
            } else if (CUSTOM_COLOR != 0) {
                return CUSTOM_COLOR;
            } else {
                return sharedPref.getInt(c.getString(R.string.pref_color_color_custom_value), readFromFile(c));
            }
        }

        return Color.parseColor(StrCOLOR_ONE);
    }


    public static void writeToFile(int data, Context c) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(c.openFileOutput("customcolour.txt", Context.MODE_PRIVATE));
            String val = String.valueOf(data);
            outputStreamWriter.write(val);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static int readFromFile(Context c) {

        String ret = "";

        try {
            InputStream inputStream = c.openFileInput("customcolour.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        int retint = 0;
        try {
            retint = Integer.parseInt(ret);
        } catch (Exception e) {
            retint = -4000;
        }
        return retint;
    }

    public static float ANIMATION_SPEED_ONE = .5f;
    public static float ANIMATION_SPEED_TWO = 2f;
    public static float ANIMATION_SPEED_THREE = 5.5f;

    public static float getAnimationSpeedPreference(Context c) {
//        Toast.makeText(c, "getanim HIT", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        int res = Integer.parseInt(sharedPref.getString(c.getString(R.string.pref_animation_animation_speed_key), "0"));
        switch (res) {
            case -1:
                return ANIMATION_SPEED_ONE;
            case 0:
                return ANIMATION_SPEED_TWO;
            case 1:
                return ANIMATION_SPEED_THREE;
            default:
                return ANIMATION_SPEED_TWO;
        }
    }

    public static boolean getPawAnimationPreference(Context c) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPref.getBoolean(c.getString(R.string.pref_animation_paw_prints_key), false);
    }

    public static int OPACITY_ONE = 100;
    public static int OPACITY_TWO = 180;
    public static int OPACITY_THREE = 250;

    public static int getOpacityPreference(Context c) {
//        Toast.makeText(c, "getanim HIT", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        int res = Integer.parseInt(sharedPref.getString(c.getString(R.string.pref_animation_animation_speed_key), "0"));
        switch (res) {
            case -1:
                return OPACITY_ONE;
            case 0:
                return OPACITY_TWO;
            case 1:
                return OPACITY_THREE;
            default:
                return OPACITY_TWO;
        }
    }

    public static void removeAllPreferences(Context c) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        prefs.edit().clear();
        PreferenceManager.setDefaultValues(c, R.xml.pref_color, true);
        PreferenceManager.setDefaultValues(c, R.xml.pref_contact_developer, true);
        PreferenceManager.setDefaultValues(c, R.xml.pref_animation, true);
    }

    public static void openAppRating(Context context) {
//        Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName()));
        Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "remingtonproductions.pawslivewallpaper")); //complete change package name like rest online

        boolean marketFound = false;
        // find all applications able to handle our rateIntent
        final List<ResolveInfo> otherApps = context.getPackageManager().queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp : otherApps) {
            if (otherApp.activityInfo.applicationInfo.packageName.equals("com.android.vending")) {
                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                rateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                rateIntent.setComponent(componentName);
                context.startActivity(rateIntent);
                marketFound = true;
                break;
            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "remingtonproductions.pawslivewallpaper"));
            context.startActivity(webIntent);
        }
    }
}
