package li.sau.mobohj.exercise4;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private EditText addNum1;
    private EditText addNum2;
    private TextView addResult;
    private EditText subNum1;
    private EditText subNum2;
    private TextView subResult;
    private EditText mulNum1;
    private EditText mulNum2;
    private TextView mulResult;
    private EditText divNum1;
    private EditText divNum2;
    private TextView divResult;
    private Button showButton;
    private TextView logText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get contex
        context = getApplicationContext();

        // Get UI components
        addNum1 = (EditText) findViewById(R.id.addNum1);
        addNum2 = (EditText) findViewById(R.id.addNum2);
        addResult = (TextView) findViewById(R.id.addResult);

        subNum1 = (EditText) findViewById(R.id.subNum1);
        subNum2 = (EditText) findViewById(R.id.subNum2);
        subResult = (TextView) findViewById(R.id.subResult);

        mulNum1 = (EditText) findViewById(R.id.mulNum1);
        mulNum2 = (EditText) findViewById(R.id.mulNum2);
        mulResult = (TextView) findViewById(R.id.mulResult);

        divNum1 = (EditText) findViewById(R.id.divNum1);
        divNum2 = (EditText) findViewById(R.id.divNum2);
        divResult = (TextView) findViewById(R.id.divResult);

        showButton = (Button) findViewById(R.id.showButton);
        logText = (TextView) findViewById(R.id.logText);
        logText.setMovementMethod(new ScrollingMovementMethod());
    }

    public void calculateAdd(View view) {
        try {
            Integer num1 = Integer.parseInt(addNum1.getText().toString());
            Integer num2 = Integer.parseInt(addNum2.getText().toString());
            addResult.setText(String.valueOf(num1 + num2));
            appendLog(num1 + "+" + num2 + "=" + (num1 + num2));
        } catch (NumberFormatException ex) {
            Toast.makeText(context, getString(R.string.nan_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void calculateSubtract(View view) {
        try {
            Integer num1 = Integer.parseInt(subNum1.getText().toString());
            Integer num2 = Integer.parseInt(subNum2.getText().toString());
            subResult.setText(String.valueOf(num1 - num2));
            appendLog(num1 + "-" + num2 + "=" + (num1 - num2));
        } catch (NumberFormatException ex) {
            Toast.makeText(context, getString(R.string.nan_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void calculateMultiply(View view) {
        try {
            Integer num1 = Integer.parseInt(mulNum1.getText().toString());
            Integer num2 = Integer.parseInt(mulNum2.getText().toString());
            mulResult.setText(String.valueOf(num1 * num2));
            appendLog(num1 + "*" + num2 + "=" + (num1 * num2));
        } catch (NumberFormatException ex) {
            Toast.makeText(context, getString(R.string.nan_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void calculateDivision(View view) {
        try {
            double num1 = Double.parseDouble(divNum1.getText().toString());
            double num2 = Double.parseDouble(divNum2.getText().toString());

            // Tarkistetaan 0 jako
            if (num2 == 0) {
                throw new NumberFormatException();
            }
            divResult.setText(String.valueOf(num1 / num2));
            appendLog(num1 + "/" + num2 + "=" + (num1 / num2));
        } catch (NumberFormatException ex) {
            Toast.makeText(context, getString(R.string.nan_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void appendLog(String line) {
        String lines = logText.getText().toString();
        logText.setText(line + "\n" + lines);
    };

    public void clearAll(View view) {
        addNum1.setText("0");
        addNum2.setText("0");
        addResult.setText("0");
        subNum1.setText("0");
        subNum2.setText("0");
        subResult.setText("0");
        mulNum1.setText("0");
        mulNum2.setText("0");
        mulResult.setText("0");
        divNum1.setText("0");
        divNum2.setText("0");
        divResult.setText("0");
        logText.setText("");
    }

    public void showLog(View view) {
        if (logText.getVisibility() == View.VISIBLE) {
            logText.setVisibility(View.INVISIBLE);
            showButton.setText(R.string.show_log);
        } else {
            logText.setVisibility(View.VISIBLE);
            showButton.setText(R.string.hide_log);
        }
    }
}
