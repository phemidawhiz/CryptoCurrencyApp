package com.franklyn.alc.cryptocash.db_lite;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by AGBOMA franklyn on 10/10/17.
 */

public class CryptoContract {

    //Authority
    public static final String AUTHORITY = "com.franklyn.alc.cryptocash.db_lite";

    //Base Uri
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);

    //Path
    public static final String PATH_CARD_ADDED = "CardAdded";

    //Column interface card added content table
    public interface CardAddedColumn {

        String CRYPTO_TYPE = "cypto_type";
        String COUNTRY_TYPE = "country_type";
    }

    public CryptoContract() {
    }

    //Table card added
    public static class CardAdded implements CardAddedColumn, BaseColumns {

        //Uri to add content to Path
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_CARD_ADDED).build();
        //Uri for directory search
        public static final String CONTENT_DIR_SEARCH = ContentResolver.CURSOR_DIR_BASE_TYPE
                +"/vnd."+ AUTHORITY +"."+ PATH_CARD_ADDED;
        //Uri for item search
        public static final String CONTENT_ITEM_SEARCH = ContentResolver.CURSOR_ITEM_BASE_TYPE
                +"/vnd."+ AUTHORITY +"."+ PATH_CARD_ADDED;

        public static final String[] PROJECTIONS = {
                _ID, CRYPTO_TYPE, COUNTRY_TYPE };

        //Sorting type
        public static final String SORT_ORDER = _ID +" DESC";
    }
}
