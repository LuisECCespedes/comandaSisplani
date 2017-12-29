package com.example.fluc.siservis_comanda.customfonts;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by fluc on 14/11/2017.
 */

public class FontConstants {

    public static final String FONT_BOLD = "fonts/MavenPro-Bold.ttf";
    public static final String FONT_NORMAL = "fonts/MavenPro-Regular.ttf";
    public static final String FONT_MEDIUM = "fonts/MavenPro-Medium.ttf";

    public static Typeface getfontNormal(Context context) {
        return Typeface.createFromAsset(context.getAssets(), FontConstants.FONT_NORMAL);
    }

    public static Typeface getfontBold(Context context) {
        return Typeface.createFromAsset(context.getAssets(), FontConstants.FONT_BOLD);
    }
}
