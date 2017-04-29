package at.ac.univie.citybike;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView result = (TextView) findViewById(R.id.result);

        float percent = GetSecondJson.calculate();

        result.setText(Float.toString(percent));

    }
}
