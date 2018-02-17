package li.sau.mobohj.exercise4;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get contex
        context = getApplicationContext();

        // Get UI components
        addNum1 = (EditText) findViewById(R.id.addNum1);
        addNum2 = (EditText) findViewById(R.id.addNum2);
        addResult = (TextView) findViewById(R.id.sumResult);

        //subNum1 = (EditText) findViewById(R.id.subNum1);
        //subNum2 = (EditText) findViewById(R.id.subNum2);
        //subResult = (TextView) findViewById(R.id.subResult);
    }

    public void calculateAdd(View view) {
        try {
            Integer num1 = Integer.parseInt(addNum1.getText().toString());
            Integer num2 = Integer.parseInt(addNum2.getText().toString());
            addResult.setText(String.valueOf(num1 + num2));
        } catch (NumberFormatException ex) {
            Toast.makeText(context, getString(R.string.nan_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void calculateSubtract(View view) {
        try {
            Integer num1 = Integer.parseInt(subNum1.getText().toString());
            Integer num2 = Integer.parseInt(subNum2.getText().toString());
            subResult.setText(String.valueOf(num1 - num2));
        } catch (NumberFormatException ex) {
            Toast.makeText(context, getString(R.string.nan_error), Toast.LENGTH_SHORT).show();
        }
    }
}
