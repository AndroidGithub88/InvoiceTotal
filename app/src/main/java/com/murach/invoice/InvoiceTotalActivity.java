package com.murach.invoice;

import java.text.NumberFormat;

import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class InvoiceTotalActivity extends Activity
        implements OnEditorActionListener {

    private EditText editTextSubTotalID;
    private TextView textViewDiscountPercentID;
    private TextView textViewDiscountAmountID;
    private TextView textViewTotalID;

    private String subtotalString;

    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_total);

        editTextSubTotalID = (EditText) findViewById(R.id.editTextSubTotalID);
        textViewDiscountPercentID = (TextView) findViewById(R.id.textViewDiscountPercentID);
        textViewDiscountAmountID = (TextView) findViewById(R.id.textViewDiscountAmountID);
        textViewTotalID = (TextView) findViewById(R.id.textViewTotalID);

        editTextSubTotalID.setOnEditorActionListener(this);

        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    @Override
    public void onPause() {
        Editor editor = savedValues.edit();
        editor.putString("subtotalString", subtotalString);
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        subtotalString = savedValues.getString("subtotalString", ""); // private variable
        editTextSubTotalID.setText(subtotalString);
        calculateAndDisplay();
    }

    private void calculateAndDisplay() {
        // get subtotal
        subtotalString = editTextSubTotalID.getText().toString(); // converst to string
        float subtotal;

        if (subtotalString.equals("")) {
            subtotal = 0;
        } else {
            subtotal = Float.parseFloat(subtotalString); // convert string to float
        }

        // get discount percent
        float discountPercent = 0;

        if (subtotal >= 200) {
            discountPercent = .2f;
        } else if (subtotal >= 100) {
            discountPercent = .1f;
        } else {
            discountPercent = 0;
        }

        // calculate discount
        float discountAmount = subtotal * discountPercent;
        float total = subtotal - discountAmount;

        // display data on the layout
        NumberFormat percent = NumberFormat.getPercentInstance();
        textViewDiscountPercentID.setText(percent.format(discountPercent));

        NumberFormat currency = NumberFormat.getCurrencyInstance();
        textViewDiscountAmountID.setText(currency.format(discountAmount));
        textViewTotalID.setText(currency.format(total));
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        calculateAndDisplay();

        // hide soft keyboard
        return false;
    }
}
