package com.franklyn.alc.cryptocash.calculate.model;

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
 * Created by AGBOMA franklyn on 10/19/17.
 */

public class CalculateModel {

    private final String LOG_TAG = CalculateModel.class.getSimpleName();

    private Context context;
    private final String BASE_URL = "https://min-api.cryptocompare.com/data/price?fsym=";
    private final String TO_SYSTEM = "&tsyms=USD";
    private CryptoInterface.ModelToPresenterCalculate modelToPresenterCalculate;

    public CalculateModel() {
    }

    public CalculateModel(Context context) {
        this.context = context;
    }

    public void setModelToPresenterCalculate(CryptoInterface.ModelToPresenterCalculate
                                                     modelToPresenterCalculate) {
        this.modelToPresenterCalculate = modelToPresenterCalculate;
    }

    public void getConversion(final String selected, final String coinType,
                              final String value, final String cashValue) {
        String coinUrl = coinType.toUpperCase();
        String url = BASE_URL + coinUrl + TO_SYSTEM;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i(LOG_TAG, "response: " +response.toString());
                            double usdResult = response.getDouble("USD");
                            modelToPresenterCalculate.resultReceived(selected, value,
                                    usdResult, coinType, cashValue);
                        }
                        catch (JSONException i){
                            i.printStackTrace();
                            //if any thing went wrong.
                            modelToPresenterCalculate.errorMsg("Error getting result, retry");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(LOG_TAG, "Error: " +error.getMessage());
                //if any thing went wrong.
                modelToPresenterCalculate.errorMsg("Error, Please retry");
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
