package com.leo.ddz.ui;

import android.content.Context;
import android.content.Intent;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.leo.ddz.GameAboutAcitvity;

/**
 * 
 * @author Leo
 *
 */
public class MessagePane {

    public static final String RULE_FILE = "ddz_rule.txt";
    public static final String ABOUT_FILE = "ddz_about.txt";
    public static final String KEY_CONTENT = "ddz_about.txt";
    private static String currFile = "";
    
    public static void show(Context context,String filePath) {

    	currFile = filePath;
        String content = MessagePane.getContent(context);
        Intent intent = null;
        if(currFile.equals(ABOUT_FILE)){
	        intent = new Intent(context, GameAboutAcitvity.class);
	        intent.putExtra(KEY_CONTENT, content);
        }else if(currFile.equals(RULE_FILE)){
	        intent.putExtra(KEY_CONTENT, content);
        }
        if(intent!=null) context.startActivity(intent);
    }

    private static String getContent(Context context) {

        byte[] buffer = new byte[8192];

        int length = readBuffer(context, buffer);
        if (length <= 0) return null;

        return loadString(buffer, length);
    }

    private static String loadString(byte[] buffer, int length) {

        try {
            return new String(
                    buffer,
                    0,
                    length,
                    "GBK"
            );
        }
        catch (UnsupportedEncodingException ignored) {
        }

        return null;
    }

    private static int readBuffer(Context context, byte[] buffer) {

        InputStream is = null;

        try {
            is = context.getAssets().open(currFile);
            return is.read(buffer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (is != null) try {
                is.close();
            }
            catch (IOException ignored) {
            }
        }

        return 0;
    }
}
