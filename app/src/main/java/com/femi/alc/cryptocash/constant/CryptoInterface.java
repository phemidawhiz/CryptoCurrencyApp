package com.franklyn.alc.cryptocash.constant;

import com.franklyn.alc.cryptocash.host.pojo.CashValue;

import java.util.ArrayList;

/**
 * Created by AGBOMA franklyn on 10/10/17.
 */

public interface CryptoInterface {

    //HostActivity
    interface HostToPresenter {
        void getUSDToCountryCash();
    }
    interface PresenterToHost {
        void sendCashReceived();
        void sendList(ArrayList<CashValue> cashValueList);
        void sendError(String error);
    }
    interface ModelToPresenterHost {
        void cashReceived(String error);
    }

    //CalculateFragment
    interface CalculateToPresenter {
        void sendCalculateDetails(String selected, String notSelected,
                                  String value, String coinType, String cashValue);
    }
    interface PresenterToCalculate {
        void sendResultedValue(String result);
        void errorMsg(String error);
    }
    interface ModelToPresenterCalculate {
        void resultReceived(String selected, String value, double usdResult,
                            String coinType, String cashValue);
        void errorMsg(String error);
    }
}
