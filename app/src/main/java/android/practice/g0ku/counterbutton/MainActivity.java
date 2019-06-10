package android.practice.g0ku.counterbutton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.practice.g0ku.counterview.CounterView;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final CounterView view = findViewById(R.id.counter);

        view.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setCount(view.count()+1);
            }
        });

        view.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setCount(view.count()-1);
            }
        });





        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.showLoader();

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                view.hideLoader();


//        ((Counter)findViewById(R.id.counter)).setCount(null);
                            }
                        });
                    }
                }).start();
            }
        });



    }
}
