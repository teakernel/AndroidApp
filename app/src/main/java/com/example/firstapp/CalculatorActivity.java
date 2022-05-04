package com.example.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firstapp.util.Arith;
public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "CalculatorActivity";
    private TextView tv_result;

    private String showResult = "";
    private String calResult = "";
    private String preNum = "";
    private String nextNum = "";
    private String operator = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        //showContent
        tv_result = findViewById(R.id.tv_content);
        // set scrolling method
        tv_result.setMovementMethod(new ScrollingMovementMethod());

        //set clickListener to all buttons
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_plus).setOnClickListener(this);
        findViewById(R.id.btn_minus).setOnClickListener(this);
        findViewById(R.id.btn_div).setOnClickListener(this);
        findViewById(R.id.btn_mul).setOnClickListener(this);
        findViewById(R.id.btn_sqrt).setOnClickListener(this);
        findViewById(R.id.btn_equal).setOnClickListener(this);
        findViewById(R.id.btn_clear).setOnClickListener(this);
        findViewById(R.id.btn_num0).setOnClickListener(this);
        findViewById(R.id.btn_num1).setOnClickListener(this);
        findViewById(R.id.btn_num2).setOnClickListener(this);
        findViewById(R.id.btn_num3).setOnClickListener(this);
        findViewById(R.id.btn_num4).setOnClickListener(this);
        findViewById(R.id.btn_num5).setOnClickListener(this);
        findViewById(R.id.btn_num6).setOnClickListener(this);
        findViewById(R.id.btn_num7).setOnClickListener(this);
        findViewById(R.id.btn_num8).setOnClickListener(this);
        findViewById(R.id.btn_num9).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //get button id
        int btnId = v.getId();
        String inputText;
        if (btnId == R.id.btn_sqrt){
            inputText = "√";
        } else {
            inputText = ((TextView) v).getText().toString();
        }
        Log.d(TAG, "clickBtn is" + btnId + ", inputText is " + inputText);

        if (btnId == R.id.btn_clear){
            clear("");
        } else if (btnId == R.id.btn_plus || btnId == R.id.btn_minus
                || btnId == R.id.btn_mul || btnId == R.id.btn_div) {
            if (preNum.length() <= 0) {
                Toast.makeText(this, "Please enter at lease one number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (operator.length() == 0 || operator.equals("=")
                || operator.equals("√")){
                Log.d(TAG,"operator is {" + operator + "}." );
                //get the operator
                operator = inputText;
                showResult = showResult + operator;
                tv_result.setText(showResult);
            } else {
                Toast.makeText(this, "Please enter a number", Toast.LENGTH_SHORT).show();
                return;
            }
        } else if (btnId == R.id.btn_cancel) { // if click cancel button
            if (operator.equals("")) {
                if (preNum.length() == 1) {
                    preNum = "0";
                } else if (preNum.length() > 0) {
                    preNum = preNum.substring(0, preNum.length() - 1);
                } else {
                    Toast.makeText(this, "End", Toast.LENGTH_SHORT).show();
                    return;
                }
                showResult = preNum;
                tv_result.setText(showResult);
            } else {
                if (nextNum.length() == 1) {
                    nextNum = "";
                } else if (nextNum.length() > 0){
                    nextNum = nextNum.substring(0, nextNum.length() - 1);
                } else {
                    Toast.makeText(this, "End", Toast.LENGTH_SHORT).show();
                    return;
                }

                showResult = showResult.substring(0, showResult.length() - 1);
                tv_result.setText(showResult);
            }
        } else if (btnId == R.id.btn_equal) {
            if (operator.length() == 0 || operator.equals("＝")) {
                Toast.makeText(this, "Please enter an operator.", Toast.LENGTH_SHORT).show();
                return;
            } else if (nextNum.length() <= 0) {
                Toast.makeText(this, "Please enter a number.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (calculate()) { // if has calculate result
                operator = inputText;
                showResult = showResult + "=" + calResult;
                tv_result.setText(showResult);
            } else {
                Log.d(TAG, "Calculate failure.");
                return;
            }
        } else if (btnId == R.id.btn_sqrt) {
            if (preNum.length() <= 0) {
                    Toast.makeText(this, "Please enter a number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Double.parseDouble(preNum) < 0) {
                    Toast.makeText(this, "The value of the open root cannot be less than 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                calResult = String.valueOf(Math.sqrt(Double.parseDouble(preNum)));
                preNum = calResult;
                nextNum = "";
                operator = inputText;
                showResult = showResult + "√=" + calResult;
                tv_result.setText(showResult);
                Log.d(TAG, "result=" + calResult + ",preNum=" + preNum + ",operator=" + operator);
        } else { // if click number or dot. button
            if (operator.equals("＝")) {
                operator = "";
                preNum = "";
                showResult = "";
            }
            if (btnId == R.id.btn_point) {
                inputText = ".";
            }
            if (operator.equals("")) {
                if (preNum.contains(".") && inputText.equals(".")) {
                    return;
                }
                preNum = preNum + inputText;
            } else {
                if (nextNum.contains(".") && inputText.equals(".")) {
                    return;
                }
                nextNum = nextNum + inputText;
            }
            showResult = showResult + inputText;
            tv_result.setText(showResult);
        }
        return;
    }

    /**
     * calculate method
     * @return true: calculate successful
     */
    private boolean calculate() {
        if (operator.equals("＋")) {
            calResult = String.valueOf(Arith.add(preNum, nextNum));
        } else if (operator.equals("-")) {
            calResult = String.valueOf(Arith.sub(preNum, nextNum));
        } else if (operator.equals("×")) {
            calResult = String.valueOf(Arith.mul(preNum, nextNum));
        } else if (operator.equals("÷")) {
            if (Double.parseDouble(nextNum) == 0) {
                Toast.makeText(this, "The divisor cannot be zero.", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                calResult = String.valueOf(Arith.div(preNum, nextNum));
            }
        }

        Log.d(TAG, "result = " + calResult);
        preNum = calResult;
        nextNum = "";
        return true;
    }

    //Clear TextView Result Content
    private void clear(String text) {
        showResult = text;
        tv_result.setText(showResult);
        operator = "";
        preNum = "";
        nextNum = "";
        calResult = "";
    }
}