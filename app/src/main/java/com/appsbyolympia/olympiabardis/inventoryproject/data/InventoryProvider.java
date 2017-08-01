package com.appsbyolympia.olympiabardis.inventoryproject.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import com.appsbyolympia.olympiabardis.inventoryproject.data.InventoryContract.InventoryEntry;


public class InventoryProvider extends ContentProvider {
    public static final String LOG_TAG = InventoryProvider.class.getSimpleName();
    private static final int PRODUCTS = 100;
    private static final int PRODUCT_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_INVENTORIES, PRODUCTS);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_INVENTORIES + "/#", PRODUCT_ID);
    }

    private InventoryDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new InventoryDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                cursor = database.query(InventoryContract.InventoryEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;

            case PRODUCT_ID:
                selection = InventoryContract.InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(InventoryContract.InventoryEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    @Override
    public Uri insert (Uri uri, ContentValues contentValues){
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertProduct(Uri uri, ContentValues values) {
        String name = values.getAsString(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Product requires a  name");
        }
        Integer price = values.getAsInteger(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE);
        if (price != null && price < 0) {
            throw new IllegalArgumentException("Product requires a valid price");
        }
        String image = values.getAsString(InventoryContract.InventoryEntry.COLUMN_PRODUCT_IMAGE);
        if (image == null) {
            throw new IllegalArgumentException("Product requires a image");
        }
        String supplier = values.getAsString(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME);
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier requires a name");
        }

        String contact = values.getAsString(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_CONTACT);
        if (contact == null){
            throw new IllegalArgumentException("The supplier requires a valid contact");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(InventoryContract.InventoryEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case PRODUCTS:
                return updateProduct(uri, contentValues, selection, selectionArgs);

            case PRODUCT_ID:
                selection = InventoryContract.InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateProduct(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME)) {
            String name = values.getAsString(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Product requires a name");
            }
        }

        if (values.containsKey(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE)) {
            Integer price = values.getAsInteger(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE);
            if (price != null && price < 0) {
                throw new IllegalArgumentException("Product requires a valid price");
            }
        }

        if (values.containsKey(InventoryContract.InventoryEntry.COLUMN_PRODUCT_IMAGE)) {
            String image = values.getAsString(InventoryContract.InventoryEntry.COLUMN_PRODUCT_IMAGE);
            if (image == null) {
                throw new IllegalArgumentException("Product requires an image");
            }
        }

        if (values.containsKey(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME)) {
            String supplier = values.getAsString(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME);
            if (supplier == null) {
                throw new IllegalArgumentException("Supplier requires a name");
            }
        }

        if (values.containsKey(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_CONTACT)) {
            String contact = values.getAsString(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_CONTACT);
            if (contact == null) {
                throw new IllegalArgumentException("Supplier requires valid contact info");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(InventoryContract.InventoryEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case PRODUCTS:
                rowsDeleted = database.delete(InventoryContract.InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case PRODUCT_ID:
                selection = InventoryContract.InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(InventoryContract.InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Deleting is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return InventoryEntry.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return InventoryEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
