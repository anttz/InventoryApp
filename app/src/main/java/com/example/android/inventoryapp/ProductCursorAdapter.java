package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

import static com.example.android.inventoryapp.data.ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY;

/**
 * Created by tzouanakos on 29/03/2017.
 */

public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        int idColumnIndex = cursor.getColumnIndex(ProductEntry._ID);
        long id = Integer.parseInt(cursor.getString(idColumnIndex));
        final Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);
        TextView nameTextView = (TextView) view.findViewById(R.id.product_name);
        final TextView quantityTextView = (TextView) view.findViewById(R.id.product_quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.product_price);
        TextView quantityDescritpionTextView = (TextView) view.findViewById(R.id.product_description_quantity);
        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCT_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        String productName = cursor.getString(nameColumnIndex);
        final String productQuantity = cursor.getString(quantityColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        nameTextView.setText(productName);
        quantityTextView.setText(productQuantity);
        priceTextView.setText("Price : $" + productPrice);
        quantityDescritpionTextView.setText("Quantity : ");
        Button saleButton = (Button) view.findViewById(R.id.sale_item_button);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(productQuantity);
                quantity = saveQuantity(context, quantity, currentProductUri);
                quantityTextView.setText(String.valueOf(quantity));
            }
        });
    }

    public int saveQuantity(Context context, int quantity, Uri currentProductUri) {

        if (quantity > 0) {
            quantity--;
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_QUANTITY, String.valueOf(quantity));
        int rowsAffected = context.getContentResolver().update(currentProductUri, values, null, null);
        if (rowsAffected == 0) {
            Toast.makeText(context, R.string.update_failed,
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, R.string.successful_update,
                    Toast.LENGTH_SHORT).show();
        }
        return quantity;
    }
}
