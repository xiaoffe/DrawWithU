package com.xiaoffe.drawwithu.util;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xiaoffe.drawwithu.R;

/**
 * Created by codeest on 16/9/3.
 */

public class SnackbarUtil {

    private static Snackbar snackbar;
    public static void show(View view, String msg) {
        snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setDuration(Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.setBackgroundColor(Color.parseColor("#ffcccc"));
        snackbarLayout.setAlpha(0.9f);
        ((TextView)snackbarLayout.findViewById(R.id.snackbar_text)).setTextColor(Color.parseColor("#FFFF00"));
        snackbar.show();
    }

    public static void dismiss() {
        if(snackbar==null){
            return;
        }
        if(snackbar.isShown()){
            snackbar.dismiss();
        }
    }
}
