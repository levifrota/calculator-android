package com.example.atividade1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView displayInput;
    private Double firstNumber = null;
    private String operator = null;
    private boolean isSecondInput = false;

    private DecimalFormat decimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayInput = findViewById(R.id.textView_input);
        setupNumberButtonListeners();
        setupOperatorButtonListeners();
        setupFunctionButtonListeners();

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        decimalFormat = new DecimalFormat("#.##", symbols);
    }

    private void setupNumberButtonListeners() {
        View.OnClickListener numberListener = v -> {
            Button button = (Button) v;
            if (isSecondInput || displayInput.getText().toString().equals("0")) {
                displayInput.setText(button.getText());
            } else {
                displayInput.append(button.getText());
            }
            isSecondInput = false;  // Ready to take new number input
        };

        int[] numberIds = {
                R.id.button_zero, R.id.button_one, R.id.button_two, R.id.button_three,
                R.id.button_four, R.id.button_five, R.id.button_six,
                R.id.button_seven, R.id.button_eight, R.id.button_nine
        };

        for (int id : numberIds) {
            findViewById(id).setOnClickListener(numberListener);
        }
    }

    private void setupOperatorButtonListeners() {
        View.OnClickListener operationListener = v -> {
            Button button = (Button) v;
            try {
                if (firstNumber != null && !isSecondInput) {  // Ensure firstNumber isn't null and it's not the second input
                    double secondNumber = Double.parseDouble(displayInput.getText().toString());
                    firstNumber = performOperation(firstNumber, secondNumber, operator);
                    displayInput.setText(decimalFormat.format(firstNumber));
                } else if (firstNumber == null && !isSecondInput) {
                    firstNumber = Double.parseDouble(displayInput.getText().toString());
                }
                operator = button.getText().toString();  // Set the new operator
                isSecondInput = true;
            } catch (NumberFormatException nfe) {
                displayInput.setText("Error");
                firstNumber = null;  // Reset first number on error
                operator = null;
                isSecondInput = false;
            }
        };

        int[] operatorIds = {
                R.id.button_add, R.id.button_subtract, R.id.button_multiply, R.id.button_divide, R.id.button_power
        };

        for (int id : operatorIds) {
            findViewById(id).setOnClickListener(operationListener);
        }

        findViewById(R.id.button_root).setOnClickListener(v -> {
            try {
                double number = Double.parseDouble(displayInput.getText().toString());
                number = Math.sqrt(number);
                displayInput.setText(decimalFormat.format(number));
                firstNumber = number;  // Update firstNumber with the root result
                operator = null;  // Reset operator
                isSecondInput = true;
            } catch (NumberFormatException nfe) {
                displayInput.setText("Error");
                firstNumber = null;
            }
        });

        findViewById(R.id.button_equals).setOnClickListener(v -> {
            try {
                if (firstNumber != null && operator != null && !isSecondInput) {
                    double secondNumber = Double.parseDouble(displayInput.getText().toString());
                    firstNumber = performOperation(firstNumber, secondNumber, operator);
                    displayInput.setText(decimalFormat.format(firstNumber));
                }
                operator = null;  // Reset the operator
                isSecondInput = true;  // Expecting new input or operation
            } catch (NumberFormatException nfe) {
                displayInput.setText("Error");
                firstNumber = null;  // Reset on error
            }
        });
    }

    private void setupFunctionButtonListeners() {
        findViewById(R.id.button_clear).setOnClickListener(v -> {
            displayInput.setText("0");
            firstNumber = null;
            operator = null;
            isSecondInput = false;
        });
    }

    private double performOperation(double first, double second, String op) {
        switch (op) {
            case "+":
                return first + second;
            case "-":
                return first - second;
            case "*":
                return first * second;
            case "/":
                if (second == 0) return Double.POSITIVE_INFINITY; // Handle division by zero
                return first / second;
            case "^":
                return Math.pow(first, second);
            default:
                throw new UnsupportedOperationException("Unsupported operation: " + op);
        }
    }
}
