package com.franklyn.alc.cryptocash.db_lite;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.franklyn.alc.cryptocash.db_lite.db_helper.SelectionBuilder;

/**
 * Created by AGBOMA franklyn on 10/10/17.
 */

public class CryptoProvider  extends ContentProvider{

    private Context context;
    private CryptoHelper cryptoHelper;
    private SQLiteDatabase dbReader, dbWriter;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static final int CARD_ADDED_ID = 100;
    private static final int CARD_ADDED_LIST = 101;

    public CryptoProvider() {
    }

    @Override
    public boolean onCreate() {

        cryptoHelper = new CryptoHelper(getContext());
        dbReader = cryptoHelper.getReadableDatabase();
        dbWriter = cryptoHelper.getWritableDatabase();

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        int match = uriMatcher.match(uri);
        final SelectionBuilder selectionBuilder = selectionBuilder(uri, match);

        switch (match) {
            case CARD_ADDED_LIST:
                if(TextUtils.isEmpty(sortOrder))
                    sortOrder = CryptoContract.CardAdded.SORT_ORDER;
                break;
        }

        return selectionBuilder.where(selection, selectionArgs)
                .query(dbReader, projection, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = uriMatcher.match(uri);

        switch (match) {
            case CARD_ADDED_LIST:
                return CryptoContract.CardAdded.CONTENT_DIR_SEARCH;
            case CARD_ADDED_ID:
                return CryptoContract.CardAdded.CONTENT_ITEM_SEARCH;
            default:
                throw new UnsupportedOperationException("uri " +match+ " not found");
        }
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = uriMatcher.match(uri);
        long id = 0;

        switch (match) {
            case CARD_ADDED_LIST: {
                id = dbWriter.insert(CryptoHelper.CardTable.CARD_ADD_TABLE, null, values);
                notifyChange(uri);
                return getUriFold(uri, id);
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri directory: " +uri);
            }
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        if(uri == CryptoContract.BASE_CONTENT_URI) {
            cryptoHelper.close();
            CryptoHelper.deleteDataBase(getContext());

            cryptoHelper = new CryptoHelper(getContext());
            notifyChange(uri);
            return 1;
        }

        final SelectionBuilder selectionBuilder = selectionBuilder(uri, uriMatcher.match(uri));
        int delete = selectionBuilder.where(selection, selectionArgs).delete(dbWriter);
        notifyChange(uri);

        return delete;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {

        final SelectionBuilder selectionBuilder = selectionBuilder(uri, uriMatcher.match(uri));

        int update = selectionBuilder.where(selection, selectionArgs).update(dbWriter, values);
        notifyChange(uri);

        return update;
    }

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(CryptoContract.AUTHORITY, CryptoContract.PATH_CARD_ADDED,
                CARD_ADDED_LIST);
        matcher.addURI(CryptoContract.AUTHORITY, CryptoContract.PATH_CARD_ADDED +"/#",
                CARD_ADDED_ID);

        return matcher;
    }

    private SelectionBuilder selectionBuilder(Uri uri, int match) {

        final SelectionBuilder selectionBuilder = new SelectionBuilder();
        int positionArg = 1;

        switch (match) {
            case CARD_ADDED_LIST: {
                return selectionBuilder.table(CryptoHelper.CardTable.CARD_ADD_TABLE);
            }
            case CARD_ADDED_ID: {
                final String selectionArg = uri.getPathSegments().get(positionArg);
                return selectionBuilder.table(CryptoHelper.CardTable.CARD_ADD_TABLE)
                        .where(CryptoContract.CardAdded._ID +"=?", selectionArg);
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri found: "+match+ "= "+ uri);
            }
        }
    }

    private void notifyChange(Uri uri) {
        getContext().getContentResolver().notifyChange(uri, null);
    }

    private static Uri getUriFold(Uri uri, long id) {
        if(id >0)
            return ContentUris.withAppendedId(uri, id);
        throw new SQLException("Problem appending uri: "+uri);
    }
}
