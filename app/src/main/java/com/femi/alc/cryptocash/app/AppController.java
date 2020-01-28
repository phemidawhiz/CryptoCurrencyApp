package com.franklyn.alc.cryptocash.app;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.icu.text.DecimalFormat;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.franklyn.alc.cryptocash.connection.ConnectionReceiver;

import java.util.HashMap;

/**
 * Created by AGBOMA franklyn on 10/10/17.
 */

public class AppController extends Application {

    private final String LOG_TAG = AppController.class.getSimpleName();

    private static AppController instance;
    public static final String TAG = "json_obj_reg";
    private RequestQueue requestQueue;
    private ProgressDialog loading;
    //public static ArrayList<CashValue> cashValueList;
    public final static String BASE_CASH_API = "https://openexchangerates.org/api/" +
            "latest.json?app_id=2d4dbd65d94b4a90b99c1162f4c33005";

    public static HashMap<String, Double> cashValueList;

    public static double NGN = 0.0;
    public static double GHS = 0.0;
    public static double CNY = 0.0;
    public static double EUR = 0.0;
    public static double INR = 0.0;
    public static double JRY = 0.0;
    public static double ZAR = 0.0;
    public static double KES = 0.0;
    public static double ARS = 0.0;
    public static double AUD = 0.0;
    public static double AOA = 0.0;
    public static double XOF = 0.0;
    public static double XAF = 0.0;
    public static double GMD = 0.0;
    public static double IQD = 0.0;
    public static double JMD = 0.0;
    public static double BRL = 0.0;
    public static double LRD = 0.0;
    public static boolean cashReceived;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        cashValueList = new HashMap<>();
        //initialize connection receiver
        new ConnectionReceiver().onReceive(getInstance(), new Intent());

    }

    public static synchronized AppController getInstance(){
        return instance;
    }


    public RequestQueue getRequestQueue() {
        if(null == requestQueue)
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        return requestQueue;
    }


    //request with tag
    public <T> void addToRequestQueue(Request<T> request, String tag){
        request.setTag(TextUtils.isEmpty(tag) ?LOG_TAG :tag);
        getRequestQueue().add(request);
    }

    //request without tag
    public <T> void addToRequestQueue(Request<T> request){
        request.setTag(LOG_TAG);
        getRequestQueue().add(request);
    }

    public void cancelRequestTag(Object tag){
        if(null != requestQueue)
            requestQueue.cancelAll(tag);
    }

    public void toastMsg (Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public void snackMsg (View proposed, String msg, int length) {
        Snackbar.make(proposed, msg, length).show();

    }

    //set up fonts for text.

    public static Typeface getSegoeBold(Context context) {
        return Typeface.createFromAsset(context.getAssets(),
                "fonts/segoeuib.ttf");
    }
    public static Typeface getSegoeSmallBold(Context context) {
        return Typeface.createFromAsset(context.getAssets(),
                "fonts/seguisb.ttf");
    }

    public static Typeface getSegoeNormal(Context context) {
        return Typeface.createFromAsset(context.getAssets(),
                "fonts/segoeui.ttf");
    }


    public void startProgress(String msg, Context context){
        if(null == loading) {
            loading = new ProgressDialog(context);
            loading.setCancelable(false);
            loading.setMessage(msg);
            loading.show();
        }
    }
    public void stopProgress(){
        if(null != loading && loading.isShowing())
            loading.dismiss();
        loading = null;
    }

    /**
     * get amount in string and apply comma separator per hundred.
     * @param amount
     * @return
     */
    public String getAmountFormat(String amount, boolean coin) {
        String newAmount = "";
        if(!amount.isEmpty()){
            try {
                Log.i(LOG_TAG, "amount: " +amount);
                double decimal = Double.parseDouble(amount);
                // if BTC or ETH reduce to 12 decimal, else if cash value reduce to 2 decimal
                if(coin)
                    amount = String.format("%.12f", decimal);
                else
                    amount = String.format("%.2f", decimal);

                Log.i(LOG_TAG, "amount truc: " +amount);

                String[] checkDot = amount.split("\\.");
                if(checkDot.length >1) {
                    newAmount = String.format("%,d", Long.parseLong(checkDot[0]));
                    if(checkDot[1].length() >0)
                        newAmount = newAmount +"."+ checkDot[1];
                }
                else
                    newAmount = String.format("%,d", Long.parseLong(amount));
            }catch(NumberFormatException i) {
                i.printStackTrace();
            }
        }
        return newAmount;
    }

}
