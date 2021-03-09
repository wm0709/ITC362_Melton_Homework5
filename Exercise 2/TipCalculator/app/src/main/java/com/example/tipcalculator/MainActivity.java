package com.example.tipcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {
    private TipCalculator tipCalc;
    public NumberFormat money = NumberFormat.getCurrencyInstance();
    private EditText billEditText;
    private EditText tipEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tipCalc = new TipCalculator(.17f,100.0f);
        setContentView(R.layout.activity_main);

        billEditText = ( EditText ) findViewById(R.id.et_amount_bill);
        tipEditText = ( EditText ) findViewById(R.id.et_amount_tip_percent);

        TextChangeHandler tch = new TextChangeHandler();
        billEditText.addTextChangedListener( tch );
        tipEditText.addTextChangedListener( tch );
    }

    /** Called when the user clicks on the Calculate button */
    public void calculate(){
        String billString = billEditText.getText().toString();
        String tipString = tipEditText.getText().toString();

        TextView tipTextView =
                (TextView) findViewById(R.id.tv_amount_tip);
        TextView totalTextView =
                (TextView) findViewById(R.id.tv_amount_total);
        try{
            // convert billString and tipString to floats
            float billAmount = Float.parseFloat(billString);
            int tipPercent = Integer.parseInt(tipString);
            // update the model
            tipCalc.setBill(billAmount);
            tipCalc.setTip(.01f * tipPercent);
            // ask Model to calculate tip and total amounts
            float tip = tipCalc.tipAmount();
            float total = tipCalc.totalAmount();
            // update the view with formatted tip and total amounts
            tipTextView.setText(money.format(tip));
            totalTextView.setText(money.format(total));
        } catch(NumberFormatException nfe){
            // pop up an alert view here
        }
    }

    private class TextChangeHandler implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            calculate();
        }
    }
}