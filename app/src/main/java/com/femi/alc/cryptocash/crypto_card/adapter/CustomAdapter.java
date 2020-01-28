package com.franklyn.alc.cryptocash.crypto_card.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.franklyn.alc.cryptocash.R;
import com.franklyn.alc.cryptocash.app.AppController;
import com.franklyn.alc.cryptocash.crypto_card.fragment.CryptoCardFragment;
import com.franklyn.alc.cryptocash.db_lite.CryptoContract;

import java.util.ArrayList;

/**
 * Created by AGBOMA franklyn on 10/12/17.
 */

public class CustomAdapter extends RecyclerViewCursorAdapter<CustomAdapter.ItemHolder> {

    private final String LOG_TAG = CustomAdapter.class.getSimpleName();
    private Context context;
    private Cursor cursor;
    private CryptoCardFragment cryptoCardFragment;

    public CustomAdapter(Context context, CryptoCardFragment cryptoCardFragment, Uri cursorUri,
                         String[] projections, String sortOrder) {
        super(null);
        this.context = context;
        this.cryptoCardFragment = cryptoCardFragment;
        cursor = context.getContentResolver().query(cursorUri, projections, null, null, sortOrder);
        swapCursor(cursor);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_items, parent, false);
        return new ItemHolder(view);
    }

    @Override
    protected void onBindViewHolder(final ItemHolder holder, Cursor cursor) {

        //get data ids from database should user want to delete card
        String getId = cursor.getString(cursor.getColumnIndex(CryptoContract.CardAdded._ID));
        Log.i(LOG_TAG, "ids: " +getId);

        String getCrypto = cursor.getString(cursor.getColumnIndex(
                CryptoContract.CardAdded.CRYPTO_TYPE));
        String getCountry = cursor.getString(cursor.getColumnIndex(
                CryptoContract.CardAdded.COUNTRY_TYPE));
        //add id
        holder.idNo.setText(getId);

        //set type face here
        holder.cryptoName.setTypeface(AppController.getSegoeSmallBold(context));
        holder.countryName.setTypeface(AppController.getSegoeSmallBold(context));
        //crypto and country name.
        holder.cryptoName.setText(getCrypto);
        holder.countryName.setText(getCountry);

        if(getCrypto.equalsIgnoreCase("btc")) {
            holder.cryptoIc.setImageResource(R.drawable.ic_btc);
            holder.cryptoColourUp.setImageResource(R.drawable.ic_btc_up);
            holder.cryptoColourDown.setImageResource(R.drawable.ic_btc_arrow);
        }
        if(getCrypto.equalsIgnoreCase("eth")) {
            holder.cryptoIc.setImageResource(R.drawable.ic_eth);
            holder.cryptoColourUp.setImageResource(R.drawable.ic_eth_up);
            holder.cryptoColourDown.setImageResource(R.drawable.ic_eth_arrow);
        }

        //Loop and compare country with list of respective cashValue
        String getKeyValue = "";
        try {
            for(String keyValue: AppController.cashValueList.keySet()){
                if(getCountry.toUpperCase().contains(keyValue)){
                    getKeyValue = String.valueOf(AppController.cashValueList.get(keyValue));
                    holder.countryValue.setText(getKeyValue);
                    holder.cashSymbol.setText(keyValue);
                    break;
                }
                else {
                    if(getKeyValue.isEmpty()) {
                        getKeyValue = "0";
                        holder.countryValue.setText(getKeyValue);
                        holder.cashSymbol.setText("?");
                    }
                }
            }
        }
        catch (Exception i){
            i.printStackTrace();
            getKeyValue = "0";
            holder.countryValue.setText(getKeyValue);
            holder.cashSymbol.setText("?");
        }

        //when clicked.
        holder.hostCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG, "onClicked");
                //open conversion screen.
                cryptoCardFragment.getClickedContent(holder.cryptoName.getText().toString(),
                        holder.countryName.getText().toString(),
                        holder.cashSymbol.getText().toString(),
                        holder.countryValue.getText().toString());
            }
        });
        //when long pressed.
        holder.hostCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i(LOG_TAG, "onLongClicked");
                //return id number
                cryptoCardFragment.getDBId(holder.idNo.getText().toString(),
                        holder.countryName.getText().toString());
                //pop up dialog to delete card and context from database.
                return true;
            }
        });
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        private RelativeLayout hostCard;
        private TextView idNo, cashSymbol, cryptoNo, cryptoName, countryName, countryValue;
        private ImageView cryptoIc, cryptoColourUp, cryptoColourDown;

        public ItemHolder(View itemView) {
            super(itemView);
            hostCard = (RelativeLayout) itemView.findViewById(R.id.host_card);
            idNo = (TextView) itemView.findViewById(R.id.id_no);
            cashSymbol = (TextView) itemView.findViewById(R.id.cash_symbol);
            cryptoName = (TextView) itemView.findViewById(R.id.crypto_name);
            cryptoIc = (ImageView) itemView.findViewById(R.id.ic_crypto);
            cryptoColourUp = (ImageView) itemView.findViewById(R.id.crypto_colour_up);
            cryptoColourDown = (ImageView) itemView.findViewById(R.id.crypto_colour_down);
            countryName = (TextView) itemView.findViewById(R.id.country_name);
            countryValue = (TextView) itemView.findViewById(R.id.country_value);
        }
    }
}
