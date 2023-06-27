package com.example.calculater;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9, bt0, btC, btProzent, btDelenie, btUmnozenie, btMinus, btPlus, btEquel;
    TextView text;
    String string = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);
        bt3 = findViewById(R.id.bt3);
        bt4 = findViewById(R.id.bt4);
        bt5 = findViewById(R.id.bt5);
        bt6 = findViewById(R.id.bt6);
        bt7 = findViewById(R.id.bt7);
        bt8 = findViewById(R.id.bt8);
        bt9 = findViewById(R.id.bt9);
        bt0 = findViewById(R.id.bt0);

        btC = findViewById(R.id.brC);
        btProzent = findViewById(R.id.btProz);
        btDelenie = findViewById(R.id.btDel);
        btUmnozenie = findViewById(R.id.btUmnoz);
        btMinus = findViewById(R.id.btMinus);
        btPlus = findViewById(R.id.btPlus);
        btEquel = findViewById(R.id.btEqual);

        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        bt4.setOnClickListener(this);
        bt5.setOnClickListener(this);
        bt6.setOnClickListener(this);
        bt7.setOnClickListener(this);
        bt8.setOnClickListener(this);
        bt9.setOnClickListener(this);
        bt0.setOnClickListener(this);

        btC.setOnClickListener(this);
        btProzent.setOnClickListener(this);
        btDelenie.setOnClickListener(this);
        btUmnozenie.setOnClickListener(this);
        btMinus.setOnClickListener(this);
        btPlus.setOnClickListener(this);
        btEquel.setOnClickListener(this);

        text = findViewById(R.id.textView4);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt1:
                string += "1";
                break;
            case R.id.bt0:
                string += "0";
                break;
            case R.id.bt2:
                string += "2";
                break;
            case R.id.bt3:
                string += "3";
                break;
            case R.id.bt4:
                string += "4";
                break;
            case R.id.bt5:
                string += "5";
                break;
            case R.id.bt6:
                string += "6";
                break;
            case R.id.bt7:
                string += "7";
                break;
            case R.id.bt8:
                string += "8";
                break;
            case R.id.bt9:
                string += "9";
                break;
            case R.id.brC:
                string = "";
                break;
            case R.id.btDel:
                string += "/";
                break;
            case R.id.btMinus:
                string += "-";
                break;
            case R.id.btPlus:
                string += "+";
                break;
            case R.id.btProz:
                string += "%";
                break;
            case R.id.btUmnoz:
                string += "*";
                break;
            case R.id.btEqual:t:
            string = Double.toString(eval(string));
                break;
        }
        text.setText(string);
    }

    public static double eval(String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)` | number
            //        | functionName `(` expression `)` | functionName factor
            //        | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return +parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    if (!eat(')')) throw new RuntimeException("Missing ')'");
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    if (eat('(')) {
                        x = parseExpression();
                        if (!eat(')')) throw new RuntimeException("Missing ')' after argument to " + func);
                    } else {
                        x = parseFactor();
                    }
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
}