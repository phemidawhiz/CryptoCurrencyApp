package com.franklyn.alc.cryptocash.host.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.franklyn.alc.cryptocash.app.AppController;
import com.franklyn.alc.cryptocash.constant.CryptoInterface;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AGBOMA franklyn on 10/10/17.
 */

public class Model {

    private final String LOG_TAG = Model.class.getSimpleName();
    private Context context;
    private CryptoInterface.ModelToPresenterHost modelToPresenterHost;

    public Model() {
    }

    public Model(Context context) {
        this.context = context;
    }

    public void setModelToPresenterHost(CryptoInterface.ModelToPresenterHost
                                                    modelToPresenterHost) {
        this.modelToPresenterHost = modelToPresenterHost;
    }

    public void getCashValue() {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                AppController.BASE_CASH_API, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    Log.i(LOG_TAG, "Cash response: " +response.toString());
                    JSONObject rates = response.getJSONObject("rates");
                    double ngn = rates.getDouble("NGN");
                    double ghs = rates.getDouble("GHS");
                    double cny = rates.getDouble("CNY");
                    double eur = rates.getDouble("EUR");
                    double inr = rates.getDouble("INR");
                    double jpy = rates.getDouble("JPY");
                    double zar = rates.getDouble("ZAR");
                    double kes = rates.getDouble("KES");
                    double ars = rates.getDouble("ARS");
                    double aud = rates.getDouble("AUD");
                    double aoa = rates.getDouble("AOA");
                    double xof = rates.getDouble("XOF");
                    double xaf = rates.getDouble("XAF");
                    double gmd = rates.getDouble("GMD");
                    double iqd = rates.getDouble("IQD");
                    double jmd = rates.getDouble("JMD");
                    double brl = rates.getDouble("BRL");
                    double lrd = rates.getDouble("LRD");

                    //if all goes well this line will be reached, the set cashReceived true.
                    AppController.cashReceived = true;
                    //save cashValue on list
                    AppController.cashValueList.put("NGN", ngn);
                    AppController.cashValueList.put("GHS", ghs);
                    AppController.cashValueList.put("CNY", cny);
                    AppController.cashValueList.put("EUR", eur);
                    AppController.cashValueList.put("INR", inr);
                    AppController.cashValueList.put("JPY", jpy);
                    AppController.cashValueList.put("ZAR", zar);
                    AppController.cashValueList.put("KES", kes);
                    AppController.cashValueList.put("ARS", ars);
                    AppController.cashValueList.put("AUD", aud);
                    AppController.cashValueList.put("AOA", aoa);
                    AppController.cashValueList.put("XOF", xof);
                    AppController.cashValueList.put("XAF", xaf);
                    AppController.cashValueList.put("GMD", gmd);
                    AppController.cashValueList.put("IQD", iqd);
                    AppController.cashValueList.put("JMD", jmd);
                    AppController.cashValueList.put("BRL", brl);
                    AppController.cashValueList.put("LRD", lrd);
                    //all list save, send info to presenter.
                    modelToPresenterHost.cashReceived("");

                }
                catch (JSONException i){
                    i.printStackTrace();
                    //if any thing went wrong.
                    AppController.cashReceived = false;
                    AppController.cashValueList.clear();
                    modelToPresenterHost.cashReceived("Error loading, " +
                            "check your connection or refresh page");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(LOG_TAG, "Error: " +error.getMessage());
                //if any thing went wrong.
                AppController.cashReceived = false;
                AppController.cashValueList.clear();
                modelToPresenterHost.cashReceived("Error loading, " +
                        "check your connection or refresh page");
            }
        }) {

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                String requestCode = String.valueOf(response.statusCode);
                Log.i(LOG_TAG, "successful, requestCode: " + requestCode);
                return super.parseNetworkResponse(response);
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000, 3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(request, AppController.TAG);
    }
}
