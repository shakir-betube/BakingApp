package com.appsys.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.INotificationSideChannel;

import com.google.android.exoplayer2.ExoPlayerLibraryInfo;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Utils {

    private final static String sBaseUrl = "https://d17h27t6h515a5.cloudfront.net";
    private final static String sRecipePath = "/topher/2017/May/59121517_baking/baking.json";

    public Utils() {

    }

    public JSONArray getRecipes() throws ApiException, InternetException {
        return getResponse(sBaseUrl + sRecipePath);
    }

    public JSONArray getRecipes(Context c) throws ApiException, InternetException {
        InputStream fos = null;
        String response = "";
        try {
            fos = c.getAssets().open("recipe.json");
            byte[] bytes = new byte[fos.available()];
            fos.read(bytes);
            fos.close();
            response = new String(bytes);
            return new JSONArray(response);
        } catch (IOException e) {
            throw new InternetException(e);
        } catch (JSONException e) {
            throw new JSONParsingException("Api response is not json: " + e.getMessage(), response);
        }
    }

    public static String getUserAgent(Context context, String applicationName) {
        String versionName;
        try {
            String packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "?";
        }
        return applicationName + "/" + versionName + " (Linux;Android " + Build.VERSION.RELEASE
                + ") " + "ExoPlayerLib/" + ExoPlayerLibraryInfo.VERSION;
    }

    private JSONArray getResponse(String strUrl) throws ApiException, InternetException {
        HttpURLConnection urlConnection = null;
        String response = "";
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in;
            int httpResponseCode = urlConnection.getResponseCode();
            boolean success = (httpResponseCode == HttpURLConnection.HTTP_OK);
            if (success)
                in = urlConnection.getInputStream();
            else
                in = urlConnection.getErrorStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (!hasInput)
                throw new ApiException("Api response is empty");

            response = scanner.next();
            JSONArray json = new JSONArray(response);

            if (success)
                return json;

            throw new ApiException("Api Response Code is " + httpResponseCode);
        } catch (JSONException e) {
            throw new JSONParsingException("Api response is not json: " + e.getMessage(), response);
        } catch (MalformedURLException e) {
            throw new ApiException("Uri: " + strUrl + ", could not be parsed");
        } catch (IOException e) {
            throw new InternetException(e);
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
    }
}
