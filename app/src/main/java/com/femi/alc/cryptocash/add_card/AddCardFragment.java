package com.franklyn.alc.cryptocash.add_card;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.franklyn.alc.cryptocash.R;
import com.franklyn.alc.cryptocash.app.AppController;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by AGBOMA franklyn on 10/9/17.
 */

public class AddCardFragment extends DialogFragment implements AdapterView.OnItemSelectedListener,
        RadioGroup.OnCheckedChangeListener{

    private final String LOG_TAG = AddCardFragment.class.getSimpleName();
    private Context context;
    private int rdId = -1;
    private ArrayAdapter<String> countryAdpt = null;
    private ArrayList<String> countList = new ArrayList<String>(){{
        add("Select");
        add("Angolan:Kwanza(AOA)");
        add("Argentina:Peso(ARS)");
        add("Australia:Dollar(AUD)");
        add("Belgium:Euro(EUR)");
        add("Benin republic:CFA(XOF)");
        add("Brazil:Real(BRL)");
        add("Cameroon:CFA(XAF)");
        add("China:Yuan(CNY)");
        add("France:Euro(EUR)");
        add("Gambia:Dasali(GMD)");
        add("Germany:Euro(EUR)");
        add("Ghana:Cedi(GHS)");
        add("India:Rupee(INR)");
        add("Iraq:Dinar(IQD)");
        add("Italy:Euro(EUR)");
        add("Jamaica:Dollar(JMD)");
        add("Japan:Yen(JPY)");
        add("Kenya:Shilling(KES)");
        add("LiberiaDollar(LRD)");
        add("Nigeria:Naira(NGN)");
        add("South Africa:Rand(ZAR)");
    }};
    private String countrySelected = "";
    private String radioSelected = "";
    private final String COUNTRY = "country";
    private final String RADIO = "radio";

    @BindView(R.id.add_card_title)
    TextView addTitle;
    @BindView(R.id.select_crypto)
    TextView selectCrypto;
    @BindView(R.id.select_country)
    TextView selectCountry;
    @BindView(R.id.select_to)
    TextView selectTo;
    @BindView(R.id.add_now)
    TextView addNow;
    @BindView(R.id.cancel_now)
    TextView cancelNow;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.rb_btc)
    AppCompatRadioButton rbBtc;
    @BindView(R.id.rb_eth)
    AppCompatRadioButton rbEth;
    @BindView(R.id.spin_country)
    Spinner spinCountry;



    public AddCardFragment() {
    }

    //set up interface to send addCard content to HostActivity
    private SendAddContent send;
    public interface SendAddContent{
        void sendContent(String crypto, String country);
    }

    public void setSend(SendAddContent send) {
        this.send = send;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View addNew = inflater.inflate(R.layout.fragment_add_card, container, false);
        ButterKnife.bind(this, addNew);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(false);

        context = getActivity();

        addTitle.setTypeface(AppController.getSegoeSmallBold(context));
        selectCrypto.setTypeface(AppController.getSegoeNormal(context));
        selectCountry.setTypeface(AppController.getSegoeNormal(context));
        selectTo.setTypeface(AppController.getSegoeNormal(context));
        rbBtc.setTypeface(AppController.getSegoeNormal(context));
        rbEth.setTypeface(AppController.getSegoeNormal(context));
        cancelNow.setTypeface(AppController.getSegoeNormal(context));
        addNow.setTypeface(AppController.getSegoeNormal(context));

        countryAdpt = new ArrayAdapter<String>(
                context,
                R.layout.text_spin,
                countList);
        countryAdpt.notifyDataSetChanged();
        countryAdpt.setDropDownViewResource(R.layout.text_spin);

        spinCountry.setAdapter(countryAdpt);

        spinCountry.setOnItemSelectedListener(this);
        rg.setOnCheckedChangeListener(this);
        return addNew;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (null != savedInstanceState){
            //get fragment contents.
            rdId = savedInstanceState.getInt(RADIO, -1);
            countrySelected = savedInstanceState.getString(COUNTRY, "");
        }
        if(rdId == rbBtc.getId())
            rbBtc.isChecked();
        if(rdId == rbEth.getId())
            rbEth.isChecked();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //save state if fragment recreates
        outState.putInt(RADIO, rdId);
        outState.putString(COUNTRY, countrySelected);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //if selection item is not equal to position 0: select
        if(position != 0)
            countrySelected = parent.getItemAtPosition(position).toString();
        else
            countrySelected = ""; //other select empty.
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        rdId = rg.getCheckedRadioButtonId();

        if(rdId == rbBtc.getId())
            radioSelected = rbBtc.getText().toString().trim();
        else if(rdId == rbEth.getId())
            radioSelected = rbEth.getText().toString().trim();
    }


    @OnClick(R.id.add_now)
    public void onAddNowClicked(){
        Log.i(LOG_TAG, "Selected items: " +radioSelected +" "+ countrySelected);
        //check all condition of user inputs
        if(radioSelected.isEmpty() && countrySelected.isEmpty())
            toastMsg("Select crypto type and country type");
        else if(radioSelected.isEmpty() && !countrySelected.isEmpty())
            toastMsg("Select crypto type");
        else if(!radioSelected.isEmpty() && countrySelected.isEmpty())
            toastMsg("Select country type");
        else{
            //interface on try should it be screen rotates while fragment is showing.
            //if all condition are met, send to HostActivity
            if(null != send)
                send.sendContent(radioSelected, countrySelected);
            radioSelected = "";
            countrySelected = "";
            getDialog().dismiss();
        }
    }

    @OnClick(R.id.cancel_now)
    public void onCancelNowClicked(){
        getDialog().dismiss();
    }

    private void toastMsg(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}

