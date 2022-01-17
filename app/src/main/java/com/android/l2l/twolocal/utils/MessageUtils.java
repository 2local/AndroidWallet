package com.android.l2l.twolocal.utils;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.android.l2l.twolocal.R;
import com.android.l2l.twolocal.utils.constants.AppConstants;
import com.google.android.material.snackbar.Snackbar;

import net.glxn.qrgen.android.QRCode;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public final class MessageUtils {


    private MessageUtils() {
        // This utility class is not publicly instantiable
    }

    public static SweetAlertDialog showLoadingDialog(Context context) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.colorPrimary));
        return dialog;
    }


    public static SweetAlertDialog showConfirmationDialog(String messgae, Context context,
                                                          SweetAlertDialog.OnSweetClickListener confirm,
                                                          SweetAlertDialog.OnSweetClickListener cancel) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
        dialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.colorPrimary));
        dialog.setContentText(messgae);
        dialog.setConfirmButton(context.getString(R.string.confirm), confirm);
        dialog.setCancelButton(context.getString(R.string.cancel), cancel);
        return dialog;
    }

    public static SweetAlertDialog showSuccessDialog(Context context, String content) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE).setContentText(content);
        dialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.colorPrimary));
        dialog.getProgressHelper().setRimColor(context.getResources().getColor(R.color.colorPrimary));
        return dialog;
    }

    public static SweetAlertDialog showSuccessDialog(String content, Context context, SweetAlertDialog.OnSweetClickListener confirm) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE).setContentText(content);
        dialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.colorPrimary));
        dialog.getProgressHelper().setRimColor(context.getResources().getColor(R.color.colorPrimary));
        dialog.setConfirmButton(context.getString(R.string.confirm), confirm);
        return dialog;
    }

    public static SweetAlertDialog showErrorDialog(Context context, String content) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE).setContentText(content);
        dialog.setCancelable(false);
        dialog.setConfirmButtonBackgroundColor(context.getResources().getColor(R.color.red));
        dialog.setConfirmText("dismiss");
        return dialog;
    }

    public static void showMessageSnackbar(String message, View view) {
        if(message !=null) {
            Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
            snack.setDuration(6000);
            snack.show();
        }
    }

    public static void showMessageToast(String message, Context context) {
        if(message !=null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

}
