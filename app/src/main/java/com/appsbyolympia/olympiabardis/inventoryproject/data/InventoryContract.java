package com.appsbyolympia.olympiabardis.inventoryproject.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class InventoryContract {
    public static final String CONTENT_AUTHORITY = "com.appsbyolympia.olympiabardis.inventoryproject";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_INVENTORIES = "inventories";

    private InventoryContract(){
    }

    public static final class InventoryEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORIES);
        public final static String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORIES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORIES;
        public final static String TABLE_NAME = "products";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_PRODUCT_NAME = "name";
        public final static String COLUMN_PRODUCT_PRICE = "price";
        public final static String COLUMN_PRODUCT_QUANTITY = "quantity";
        public final static String COLUMN_PRODUCT_IMAGE = "image";
        public final static String COLUMN_SUPPLIER_NAME = "supplier";
        public final static String COLUMN_SUPPLIER_CONTACT = "contact";
    }
}
