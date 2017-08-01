package com.appsbyolympia.olympiabardis.inventoryproject.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.appsbyolympia.olympiabardis.inventoryproject.data.InventoryContract.InventoryEntry;



public class InventoryDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = InventoryDbHelper.class.getSimpleName();
    public static final String DATABASE_NAME = "inventory.db";
    public static final int DATABASE_VERSION = 1;

    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE " + InventoryContract.InventoryEntry.TABLE_NAME + " ("
                + InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + InventoryEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL, "
                + InventoryEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER DEFAULT 0, "
                + InventoryEntry.COLUMN_PRODUCT_IMAGE + " TEXT NOT NULL, "
                + InventoryEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, "
                + InventoryEntry.COLUMN_SUPPLIER_CONTACT + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
