package com.appsbyolympia.olympiabardis.inventoryproject;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.appsbyolympia.olympiabardis.inventoryproject.data.InventoryContract;

public class InventoryCursorAdaptor extends CursorAdapter {

    public InventoryCursorAdaptor(Context context, Cursor c) {

        super(context, c, 0 /*flags*/);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);

        int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY);

        String productName = cursor.getString(nameColumnIndex);
        Integer productPrice = cursor.getInt(priceColumnIndex);
        Integer productQuantity = cursor.getInt(quantityColumnIndex);

        if (TextUtils.isEmpty(productName)) {
            productName = context.getString(R.string.unknown_inventory);
        }

        nameTextView.setText(productName);
        priceTextView.setText(String.valueOf(productPrice));
        quantityTextView.setText(String.valueOf(productQuantity));
    }
}
