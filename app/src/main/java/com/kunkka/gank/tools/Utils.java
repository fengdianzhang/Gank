package com.kunkka.gank.tools;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kunkka on 17/4/12.
 */

public class Utils {
    public static String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {

            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}
