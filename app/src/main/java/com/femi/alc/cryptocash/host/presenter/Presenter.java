package com.franklyn.alc.cryptocash.host.presenter;

import android.content.Context;

import com.franklyn.alc.cryptocash.app.AppController;
import com.franklyn.alc.cryptocash.constant.CryptoInterface;
import com.franklyn.alc.cryptocash.host.activity.HostActivity;
import com.franklyn.alc.cryptocash.host.model.Model;

/**
 * Created by AGBOMA franklyn on 10/10/17.
 */

public class Presenter implements CryptoInterface.HostToPresenter,
        CryptoInterface.ModelToPresenterHost {

    private final String LOG_TAG = Presenter.class.getSimpleName();
    private Context context;
    private CryptoInterface.PresenterToHost presenterToHost;
    private Model model;

    public Presenter() {
    }

    public Presenter(Context context, HostActivity activity) {
        this.context = context;
        setPresenterToHost(activity);
        model = new Model(context);
        model.setModelToPresenterHost(this);

    }

    private void setPresenterToHost(CryptoInterface.PresenterToHost
                                                 presenterToHost) {
        this.presenterToHost = presenterToHost;
    }

    @Override
    public void getUSDToCountryCash() {
        //check if cashReceived is false,
        // this means the app was open for the first time ever or after activity destroys
        if(!AppController.cashReceived)
            model.getCashValue();
        else
            cashReceived("");
    }

    /**
     * error contains string of error result got from cash value API
     * @param error
     */
    @Override
    public void cashReceived(String error) {
        //save all cash equivalent to 1 USD
        if(AppController.cashReceived)
            presenterToHost.sendCashReceived();
        else {
            presenterToHost.sendError(error);
        }
    }

}
