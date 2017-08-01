package com.appsbyolympia.olympiabardis.inventoryproject;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.appsbyolympia.olympiabardis.inventoryproject.data.InventoryContract;



public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PRODUCT_LOADER = 0;

    InventoryCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView productListView = (ListView) findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
        productListView.setEmptyView(emptyView);

        mCursorAdapter = new InventoryCursorAdapter(this, null);
        productListView.setAdapter(mCursorAdapter);

        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);

                Uri currentProductUri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI, id);

                intent.setData(currentProductUri);

                startActivity(intent);
            }
        });

        getSupportLoaderManager().initLoader(PRODUCT_LOADER, null, this);
    }

    private void insertProduct(){
        Uri uri = Uri.parse("android.resource://com.appsbyolympia.olympiabardis.inventoryproject/drawable/lipstick");
        String image_path = uri.toString().trim();
        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, "Tarte Color Splash Lipstick");
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE, 21);
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY, 6);
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_IMAGE, image_path);
        values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME, "Sephora");
        values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_CONTACT, "71861");

        Uri newUri = getContentResolver().insert(InventoryContract.InventoryEntry.CONTENT_URI, values);
    }

    private void deleteAllProducts(){
        int rowsDeleted = getContentResolver().delete(InventoryContract.InventoryEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows have now deleted from the database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.insert_dummy_data:
                insertProduct();
                return true;

            case R.id.delete_all_entries:
                deleteAllProducts();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle){
        String[] projection = {
                InventoryContract.InventoryEntry._ID,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_IMAGE,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_IMAGE,
                InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryContract.InventoryEntry.COLUMN_SUPPLIER_CONTACT};

        return new CursorLoader(this,
                InventoryContract.InventoryEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        mCursorAdapter.swapCursor(null);
    }
}
