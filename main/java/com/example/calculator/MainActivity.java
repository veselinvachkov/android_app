package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView workingsTV;
    TextView resultsTV;

    String workings = "";
    String formula = "";
    String tempFormula = "";
    boolean leftBracket = true;  // To toggle between opening and closing brackets

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTextViews();
    }

    private void initTextViews() {
        workingsTV = findViewById(R.id.workingsTextView);
        resultsTV = findViewById(R.id.resultTextView);
    }

    private void setWorkings(String givenValue) {
        workings = workings + givenValue;
        workingsTV.setText(workings);
    }

    public void equalOnClick(View view) {
        String finalResult = "";
        try {
            Context rhino = Context.enter();
            rhino.setOptimizationLevel(-1);
            Scriptable scope = rhino.initStandardObjects();

            // Before evaluating, replace "^" with Math.pow() for power handling
            checkForPowerOf();  // Process any power operations

            // Evaluate the expression using Rhino
            finalResult = rhino.evaluateString(scope, formula, "javascript", 1, null).toString();
        } catch (Exception e) {
            finalResult = "Error";
        }

        resultsTV.setText(finalResult);  // Display the result
    }

    private void checkForPowerOf() {
        // This method scans for any power operator (^) and replaces it with Math.pow()
        ArrayList<Integer> indexOfPowers = new ArrayList<>();
        for (int i = 0; i < workings.length(); i++) {
            if (workings.charAt(i) == '^')
                indexOfPowers.add(i);
        }

        formula = workings;
        tempFormula = workings;
        for (Integer index : indexOfPowers) {
            changeFormula(index);  // Replace power operator with Math.pow()
        }
        formula = tempFormula;
    }

    private void changeFormula(Integer index) {
        String numberLeft = "";
        String numberRight = "";

        // Extract the number to the left of '^'
        for (int i = index - 1; i >= 0; i--) {
            if (isNumeric(workings.charAt(i)))
                numberLeft = workings.charAt(i) + numberLeft;
            else
                break;
        }

        // Extract the number to the right of '^'
        for (int i = index + 1; i < workings.length(); i++) {
            if (isNumeric(workings.charAt(i)))
                numberRight = numberRight + workings.charAt(i);
            else
                break;
        }

        // Replace ^ with Math.pow() to make the expression evaluable in JavaScript
        String original = numberLeft + "^" + numberRight;
        String changed = "Math.pow(" + numberLeft + "," + numberRight + ")";
        tempFormula = tempFormula.replace(original, changed);  // Replace the power operation
    }

    private boolean isNumeric(char c) {
        return (c <= '9' && c >= '0') || c == '.';
    }

    public void clearOnClick(View view) {
        workingsTV.setText("");
        workings = "";
        resultsTV.setText("");
        leftBracket = true;  // Reset the bracket state
    }

    // Brackets functionality (alternating between '(' and ')')
    public void bracketsOnClick(View view) {
        if (leftBracket) {
            setWorkings("(");
            leftBracket = false;
        } else {
            setWorkings(")");
            leftBracket = true;
        }
    }

    // Power operator
    public void powerOfOnClick(View view) {
        setWorkings("^");
    }

    // Standard number and operator button click methods
    public void divisionOnClick(View view) {
        setWorkings("/");
    }

    public void sevenOnClick(View view) {
        setWorkings("7");
    }

    public void eightOnClick(View view) {
        setWorkings("8");
    }

    public void nineOnClick(View view) {
        setWorkings("9");
    }

    public void timesOnClick(View view) {
        setWorkings("*");
    }

    public void fourOnClick(View view) {
        setWorkings("4");
    }

    public void fiveOnClick(View view) {
        setWorkings("5");
    }

    public void sixOnClick(View view) {
        setWorkings("6");
    }

    public void minusOnClick(View view) {
        setWorkings("-");
    }

    public void oneOnClick(View view) {
        setWorkings("1");
    }

    public void twoOnClick(View view) {
        setWorkings("2");
    }

    public void threeOnClick(View view) {
        setWorkings("3");
    }

    public void addOnClick(View view) {
        setWorkings("+");
    }

    public void decimalOnClick(View view) {
        setWorkings(".");
    }

    public void zeroOnClick(View view) {
        setWorkings("0");
    }
}
