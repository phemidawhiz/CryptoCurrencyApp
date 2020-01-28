package com.franklyn.alc.cryptocash.calculate.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.franklyn.alc.cryptocash.R;
import com.franklyn.alc.cryptocash.app.AppController;
import com.franklyn.alc.cryptocash.calculate.presenter.CalculatePresenter;
import com.franklyn.alc.cryptocash.constant.CryptoInterface;
import com.franklyn.alc.cryptocash.helper.io.CustomWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by AGBOMA franklyn on 10/13/17.
 */

public class CalculateFragment extends Fragment implements RadioGroup.OnCheckedChangeListener,
        CryptoInterface.PresenterToCalculate{

    private final String LOG_TAG = CalculateFragment.class.getSimpleName();
    private Context context;
    private CryptoInterface.CalculateToPresenter calculateToPresenter;
    public static boolean checked;
    public static boolean notEmpty;
    private int rgId = -1;
    private String rgSelected = "", rgNotSelected = "";
    private final String RG = "selected";
    public final static String EDIT_VALUE = "edit_value";
    private final String CONVERTED = "converted_value";
    private  String crypto = "", country = "", symbol = "", cash = "";


    @BindView(R.id.crypto_ic)
    ImageView cryptoIc;
    @BindView(R.id.crypto_name)
    TextView cryptoName;
    @BindView(R.id.crypto_colour_up)
    ImageView cryptoColourUp;
    @BindView(R.id.crypto_colour_down)
    ImageView cryptoColourDown;
    @BindView(R.id.country_name)
    TextView countryName;


    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.rb_coin)
    AppCompatRadioButton rbCoin;
    @BindView(R.id.rb_cash)
    AppCompatRadioButton rbCash;
    @BindView(R.id.to)
    TextView to;
    @BindView(R.id.not_selected)
    TextView notSelected;
    @BindView(R.id.not_selected2)
    TextView notSelected2;
    @BindView(R.id.edit_value)
    TextInputEditText editValue;
    @BindView(R.id.convert_btn)
    Button convertBtn;
    @BindView(R.id.selected)
    TextView selected;
    @BindView(R.id.conversion_value)
    TextView conversionValue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setCalculateToPresenter(CryptoInterface.CalculateToPresenter
                                                calculateToPresenter) {
        this.calculateToPresenter = calculateToPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View calculate = inflater.inflate(R.layout.fragment_calcaulate, container, false);
        ButterKnife.bind(this, calculate);
        context = getActivity();

        calculateToPresenter = new CalculatePresenter(context, this);
        setCalculateToPresenter(calculateToPresenter);


        //get bundles form HostActivity
        Bundle bundle = getArguments();
        if(null != bundle){
            crypto = bundle.getString("crypto", "");
            country = bundle.getString("country", "");
            symbol = bundle.getString("symbol", "");
            cash = bundle.getString("cash", "");

            //clear bundles to avoid bundle existences when fragment recreates.
            /*bundle.remove("crypto");
            bundle.remove("country");
            bundle.remove("symbol");
            bundle.remove("cash");*/
        }

        return calculate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cryptoName.setTypeface(AppController.getSegoeSmallBold(context));
        countryName.setTypeface(AppController.getSegoeSmallBold(context));
        rbCoin.setTypeface(AppController.getSegoeNormal(context));
        rbCash.setTypeface(AppController.getSegoeNormal(context));
        selected.setTypeface(AppController.getSegoeNormal(context));
        notSelected.setTypeface(AppController.getSegoeNormal(context));
        notSelected2.setTypeface(AppController.getSegoeNormal(context));
        editValue.setTypeface(AppController.getSegoeNormal(context));
        convertBtn.setTypeface(AppController.getSegoeNormal(context));
        conversionValue.setTypeface(AppController.getSegoeNormal(context));


        cryptoName.setText(crypto);
        countryName.setText(country);
        if(crypto.equalsIgnoreCase("btc")) {
            cryptoIc.setImageResource(R.drawable.ic_btc);
            cryptoColourUp.setImageResource(R.drawable.ic_btc_up);
            cryptoColourDown.setImageResource(R.drawable.ic_btc_arrow);
        }
        else {
            cryptoIc.setImageResource(R.drawable.ic_eth);
            cryptoColourUp.setImageResource(R.drawable.ic_eth_up);
            cryptoColourDown.setImageResource(R.drawable.ic_eth_arrow);
        }

        rbCoin.setText(crypto);
        rbCash.setText(symbol);
        rg.setOnCheckedChangeListener(this);

        editValue.addTextChangedListener(new CustomWatcher(context, editValue,
                convertBtn, rgSelected, EDIT_VALUE));
        editValue.setEnabled(false);
    }


    @Override
    public void onStop() {
        super.onStop();
        AppController.getInstance().stopProgress();
        //set screen to initial state to remove any default save state.
        editValue.setText("");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(RG, rgId);
        outState.putString(EDIT_VALUE, editValue.getText().toString());
        outState.putString(CONVERTED, conversionValue.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.i(LOG_TAG , "onViewStated");
        if(null != savedInstanceState) {
            rgId = savedInstanceState.getInt(RG, -1);
            editValue.setText(savedInstanceState.getString(EDIT_VALUE, ""));
            editValue.setSelection(editValue.getText().length());
            conversionValue.setText(savedInstanceState.getString(CONVERTED, ""));
        }

        if(rgId == rbCoin.getId())
            rbCoin.isChecked();
        if(rgId == rbCash.getId())
            rbCash.isChecked();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        rgId = rg.getCheckedRadioButtonId();
        checked = true;
        editValue.setEnabled(true);
        if(rgId == rbCoin.getId()) {
            rgSelected = rbCoin.getText().toString();
            rgNotSelected = rbCash.getText().toString();
        }
        if(rgId == rbCash.getId()) {
            rgSelected = rbCash.getText().toString();
            rgNotSelected = rbCoin.getText().toString();
        }

        selected.setText(rgSelected);
        editValue.setHint(rgSelected + " value");
        notSelected.setText(rgNotSelected);
        notSelected2.setText(rgNotSelected);

    }

    @OnClick(R.id.convert_btn)
    public void OnCovertBtnClicked() {
        if(checked && notEmpty){
            //call api to get value
            AppController.getInstance().startProgress("Converting to " +rgNotSelected
                    +"...", context);
            calculateToPresenter.sendCalculateDetails(rgSelected, rgNotSelected,
                    editValue.getText().toString(), cryptoName.getText().toString(), cash);
        }
        else if(!checked && !notEmpty) {
            AppController.getInstance().toastMsg(context, "Select conversion type " +
                    "from the radio button");
        }
        else if(checked && !notEmpty) {
            AppController.getInstance().toastMsg(context, "Enter " +rgSelected
                    +" value in the text box");
        }
    }


    /**
     * result contains the converted values from presenter
     * @param result
     */
    @Override
    public void sendResultedValue(String result) {
        conversionValue.setText(result);
        AppController.getInstance().stopProgress();
    }

    /**
     * If any thing goes wrong, user will see error display as toast message.
     * @param error
     */
    @Override
    public void errorMsg(String error) {
        AppController.getInstance().stopProgress();
        AppController.getInstance().toastMsg(context, error);
    }
}
