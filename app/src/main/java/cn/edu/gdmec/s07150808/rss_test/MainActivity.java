package cn.edu.gdmec.s07150808.rss_test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.net.URLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    private MainActivity seft;
    private EditText editText;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        seft=this;
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.btn);
        editText = (EditText) findViewById(R.id.edit_input);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = URLEncoder.encode(editText.getText().toString());
                new GetNewsInfoTake(seft).execute(str);

            }
        });
        new GetNewsInfoTake(seft).execute("足球");
    }
}
