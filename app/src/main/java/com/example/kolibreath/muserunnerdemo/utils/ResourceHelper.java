package com.example.kolibreath.muserunnerdemo.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;

public class ResourceHelper {
    public static int getThemeColor(Context ctx,int attr, int defaultColor) throws PackageManager.NameNotFoundException {
        int themeColor = 0;
        String pkg = ctx.getPackageName();

        Context pkgCtx = ctx.createPackageContext(pkg,0);
        ApplicationInfo info = ctx.getPackageManager().getApplicationInfo(pkg,0);
        Resources.Theme theme = pkgCtx.getTheme();

        TypedArray ta = theme.obtainStyledAttributes(new int[]{attr});
        themeColor  = ta.getColor(0,defaultColor);
        ta.recycle();

        return themeColor;
    }
}
