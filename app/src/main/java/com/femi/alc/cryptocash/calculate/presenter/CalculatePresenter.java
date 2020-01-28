package com.franklyn.alc.cryptocash.calculate.presenter;

import android.content.Context;
import android.util.Log;

import com.franklyn.alc.cryptocash.app.AppController;
import com.franklyn.alc.cryptocash.calculate.fragment.CalculateFragment;
import com.franklyn.alc.cryptocash.calculate.model.CalculateModel;
import com.franklyn.alc.cryptocash.constant.CryptoInterface;

/**
 * Created by AGBOMA franklyn on 10/19/17.
 */

public class CalculatePresenter implements CryptoInterface.CalculateToPresenter,
        CryptoInterface.ModelToPresenterCalculate{

    private final String LOG_TAG = CalculatePresenter.class.getSimpleName();

    private Context context;
    private CryptoInterface.PresenterToCalculate presenterToCalculate;
    private CalculateModel model;


    public CalculatePresenter(Context context, CalculateFragment calculateFragment) {
        this.context = context;
        setPresenterToCalculate(calculateFragment);
        model = new CalculateModel(context);
        model.setModelToPresenterCalculate(this);
    }

    public void setPresenterToCalculate(CryptoInterface.PresenterToCalculate
                                                presenterToCalculate) {
        this.presenterToCalculate = presenterToCalculate;
    }

    /**
     * parameters sent from calculateFragment.
     * which contains
     * @param selected = what use what to convert from
     * @param value = what need to be converted
     * @param coinType = if BTC or ETH
     * @param cashValue = cash representation to dollar(USD) as got from API
     */
    @Override
    public void sendCalculateDetails(String selected,  String notSelected,
                                     String value, String coinType, String cashValue) {
        if(selected.equals("?") || notSelected.equals("?")){
            //if cash for country was not retrieved from Api call
            //or BTC/ETH is not available, do this.
            Log.i(LOG_TAG, "Not selected here: " +notSelected);
            presenterToCalculate.sendResultedValue("0.00");
        }
        else {
            //other wise do this.
            model.getConversion(selected, coinType, value, cashValue);
        }
    }

    @Override
    public void resultReceived(String selected, String value, double usdResult,
                               String coinType, String cashValue) {

        String getActualWithComma = "";

        if(selected.equalsIgnoreCase(coinType)){
            // user whats to covert from coin to cash.
            Log.i(LOG_TAG, "coin to cash");
            double getValue = Double.parseDouble(value);
            double getApiCashValue = Double.parseDouble(cashValue);
            //get value inserted to coin represented.
            double getUserCoinValue = getValue * usdResult;
            double getUserApiConversion = getUserCoinValue * getApiCashValue;
            //add commas to result by division of 3.
            getActualWithComma = AppController.getInstance()
                    .getAmountFormat(String.valueOf(getUserApiConversion), false);
        }
        else {
            //user whats to convert from cash to coin.
            Log.i(LOG_TAG, "cash to coin");
            Log.i(LOG_TAG, "cash: " +cashValue);
            Log.i(LOG_TAG, "value to convert: " +value);

            //get string to double
            double getValue = Double.parseDouble(value);
            double getApiCashValue = Double.parseDouble(cashValue);
            //get value inserted to dollar(USD) represented.
            double getUserValueToUsd = getValue / getApiCashValue;
            //get users actual conversion
            double getUserApiConversion = getUserValueToUsd / usdResult;
            //add commas to result by division of 3.
            getActualWithComma = AppController.getInstance()
                    .getAmountFormat(String.valueOf(getUserApiConversion), true);
        }

        Log.i(LOG_TAG, "Converted value: " +getActualWithComma);
        presenterToCalculate.sendResultedValue(getActualWithComma);
    }

    @Override
    public void errorMsg(String error) {
        presenterToCalculate.errorMsg(error);
    }
}
