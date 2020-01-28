package com.franklyn.alc.cryptocash.db_lite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by AGBOMA franklyn on 10/10/17.
 */

public class CryptoHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "crypto.db";
    private static final int DATABASE_VERSION = 3;
    private Context context;

    public CryptoHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    //Row card added table contents
    public interface CardTable {
        String CARD_ADD_TABLE = "card_added_table";
    }

    final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + CardTable.CARD_ADD_TABLE +"("
            + BaseColumns._ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
            + CryptoContract.CardAdded.CRYPTO_TYPE +" VARCHAR NOT NULL, "
            + CryptoContract.CardAdded.COUNTRY_TYPE +" VARCHAR NOT NULL" +")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ");
        onCreate(db);
    }

    public static void deleteDataBase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }
}
