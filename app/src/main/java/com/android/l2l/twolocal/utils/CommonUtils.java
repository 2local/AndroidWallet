package com.android.l2l.twolocal.utils;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.android.l2l.twolocal.R;
import com.android.l2l.twolocal.utils.constants.AppConstants;

import net.glxn.qrgen.android.QRCode;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public final class CommonUtils {

    private static final String TAG = "CommonUtils";

    private CommonUtils() {
    }

    public static String formatDateAsDayMonthName(String date) {
        //2020-01-26
        try {
            String d = "%s %s";
            return String.format(d, date.substring(8, 10), getMonthName(Integer.parseInt(date.substring(5, 7))));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getMonthName(int index) {

        try {
            String[] monthName = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
                    "Aug", "Sep", "Oct", "Nov",
                    "Dec"};
            if (index - 1 >= 0 && (index - 1) < monthName.length)
                return monthName[index - 1];
        } catch (Exception e) {
            e.printStackTrace();

        }
        return "";
    }


    public static String removeCharactersPriceIfExists(String price) {
        String normalPrice = "0.0";
        if (!TextUtils.isEmpty(price)) {
            price = toEnglish(price);
//            normalPrice = price.replaceAll("[^0-9\\.]", "");
            normalPrice = price.replaceAll(",", "");
        }

        if (TextUtils.isEmpty(normalPrice))
            normalPrice = "0.0";
        if (normalPrice.startsWith("."))
            normalPrice = "0" + normalPrice;
        return normalPrice;
//        if (price.contains(","))
//            return price.replaceAll(",", "");
//        return price;
    }

    public static BigDecimal stringToBigDecimal(String priceStr) {
        String price = removeCharactersPriceIfExists(priceStr);
        return new BigDecimal(price);
    }


    static String[] farsiChars;

    static {
        farsiChars = new String[]{"\u06f0", "\u06f1", "\u06f2", "\u06f3", "\u06f4", "\u06f5", "\u06f6", "\u06f7", "\u06f8", "\u06f9", "\u066c", "\u066b"};
    }

    public static String toEnglish(String number) {
        if (number != null)
            return number.replaceAll(farsiChars[0], "0").replaceAll(farsiChars[1], "1").replaceAll(farsiChars[2], "2").replaceAll(farsiChars[3], "3").replaceAll(farsiChars[4], "4").replaceAll(farsiChars[5], "5").replaceAll(farsiChars[6], "6").replaceAll(farsiChars[7], "7").replaceAll(farsiChars[8], "8").replaceAll(farsiChars[9], "9");
        else
            return "";
    }


    public static Bitmap generateQrCode(String content) {
        return QRCode.from(content).bitmap();
    }


    public static String formatToDecimalPriceTwoDigits(BigDecimal price_long) {
        try {
            NumberFormat numberFormat = DecimalFormat.getNumberInstance(Locale.US);
            DecimalFormat formatter = (DecimalFormat) numberFormat;
            formatter.applyPattern("###,###,##0.00");
            formatter.setRoundingMode(RoundingMode.DOWN);

            return formatter.format(price_long);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "0.0";

    }

    public static String formatToDecimalPriceSixDigits(BigDecimal price) {
        try {
            NumberFormat numberFormat = DecimalFormat.getNumberInstance(Locale.US);
            DecimalFormat formatter = (DecimalFormat) numberFormat;
            formatter.applyPattern("###,###,##0.00####");
            formatter.setRoundingMode(RoundingMode.DOWN);

            return formatter.format(price);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "0.0";

    }

    @SuppressLint("all")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void copyToClipboard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(AppConstants.APP_WALLET_NUMBER_CLIPBOARD_LABEL, text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, context.getString(R.string.copied_to_clip_board), Toast.LENGTH_SHORT).show();
    }

    public static void shareText(Context context, String text, String title) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        context.startActivity(sharingIntent);
    }

    public static String pasteFromClipboard(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        String pasteData = "";
        if ((clipboard.hasPrimaryClip())) {
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            pasteData = item.getText().toString();
        }
        return pasteData;
    }


    public static String encodeBase64ToString(@NotNull String raw_text) {
        return android.util.Base64.encodeToString(raw_text.getBytes(StandardCharsets.UTF_8), android.util.Base64.NO_WRAP);
    }

    @NotNull
    public static String decodeBase64ToString(@NotNull String encoded_string) {
        return new String(android.util.Base64.decode(encoded_string, android.util.Base64.NO_WRAP), StandardCharsets.UTF_8);
    }


    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static List<String> getFirstAndLastDayOfWeek() {
        List<String> dates = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        for (int i = 0; i < 7; i++) {
            dates.add(df.format(c.getTime()));
            c.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public static String convertTimestampTo_dd_MMM_yyyy_HH_mm(String timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(timeStamp) * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault());
        return formatter.format(calendar.getTime());
    }

    public static void openPlayStoreForApp(Context context) {
        final String appPackageName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.GOOGLE_PLAY + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (packageInfo != null)
                return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "1";
    }

    public static void clearApplicationData(Context context) {
        File cache = context.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            int i = 0;
            while (i < children.length) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
                i++;
            }
        }

        assert dir != null;
        return dir.delete();
    }

}
