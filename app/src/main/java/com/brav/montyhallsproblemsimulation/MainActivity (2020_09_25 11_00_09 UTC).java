package com.brav.montyhallsproblemsimulation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public TextView insert;
    public TextView t1;
    public TextView t2;
    public Switch sf, start;
    public SeekBar ss;
    public int seconds = 0;
    public long multiplier = 1000;
    public Thread t;
    public long done = 0;
    public String st1,st2;
    public long c1 = 0;
    public long c2 = 0;
    public Activity activity;
    public Handler handler2 = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        insert = findViewById(R.id.insert);
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        sf = findViewById(R.id.sfSwitch);
        ss = findViewById(R.id.ss);
        start = findViewById(R.id.ssButton);
        insert.setTextSize(14);
        t1.setTextSize(14);
        t2.setTextSize(14);
        start.setTextSize(14);
        sf.setTextSize(14);
        t1.setMovementMethod(new ScrollingMovementMethod());
        t2.setMovementMethod(new ScrollingMovementMethod());
        insert.setHint("Iterations Number");
        t = new MainThread((MainActivity) activity);

        start.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (TextUtils.isEmpty(insert.getText())) {
                        start.setChecked(false);
                        return;
                    }
                    if (seconds != 0) {
                        startElaboration2();
                        return;
                    }
                    startElaboration();
                } else {
                    t1.setText(null);
                    t2.setText(null);
                    t1.setHint("Change Door");
                    t2.setHint("Keep Door");
                }
            }
        });

        sf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    seconds = 0;
                } else {
                    seconds = 5; //5 secondi è il più lento
                }
            }
        });

        ss.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress +1;
                multiplier = 1000/progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }



    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            String key = b.getString("t1");
            t1.setText(key+"\n\n" + c1 + "/" + done + " ("+(((float)c1/done)*100)+"%)\n");
            key =  b.getString("t2");
            t2.setText(key+"\n\n" + c2 + "/" + done + " ("+(((float)c2/done)*100)+"%)\n");
            t1.invalidate();
            t2.invalidate();

        }
    };






    public void clear(){
        if(activity!=null)
            activity.runOnUiThread(() -> {
               t1.invalidate();
               t2.invalidate();
            });
    }


    private void startElaboration2(){
        done = 0;
        c1 = 0;
        c2 = 0;



        long number = Long.parseLong(insert.getText().toString());

        for(int i = 1; i <= number; i++){
            t = new MainThread((MainActivity)activity);
            handler.postDelayed(t, (multiplier*seconds*i));

        }

    }
    private void startElaboration() {
      // t = new Thread(new Runnable() {
         //   @Override
           // public void run() {
                done = 0;
                c1 = 0;
                c2 = 0;



                long number = Long.parseLong(insert.getText().toString());

                for(int i = 1; i <= number; i++){
                    st1 = "";
                    st2 = "";
                    try {
                        t1.invalidate();
                        t2.invalidate();
                        if(seconds!=0) {
                            Thread.sleep(seconds*multiplier);
                        }
                        clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Letter correct = Letter.randomLetter();
                    Letter chosen = Letter.randomLetter();
                    st1 = ("Correct: " + correct.name() + "\n\n");
                    t1.invalidate();
                    t2.invalidate();
                    st1 += ("Chosen: " + chosen.name() + "\n\n");

                    Letter ship;

                    while(true){
                        ship = Letter.randomLetter();
                        if(ship!=correct && ship!=chosen) break;
                    }
                    st1 += ("Behind door " + ship.name());
                    st1 += (" there is a ship\n\n");


                    Letter change;
                    while(true){
                        change = Letter.randomLetter();
                        if(change!=chosen && change!=ship) break;
                    }

                    st1 += ("\n\n");
                    st2 += ("\n\n\n\n\n\n\n\n\n");

                    st1 += ("I change to door " + change + "\n\n");
                    st2 += ("I keep door " + chosen + "\n\n");

                    if(correct==chosen){
                        st2 += ("I was right\n");
                        st1 += ("I was wrong\n");
                        c2++;
                    }
                    else{
                        st1 += ("I was right\n");
                        st2 += ("I was wrong\n");
                        c1++;
                    }
                    done++;


                    Message msg = new Message();
                    Bundle b = new Bundle();
                    b.putString("t1", st1);
                    b.putString("t2",st2);
                    msg.setData(b);
                    handler.sendMessage(msg);

                }

       //     }


     //   });
      //  t.start();
    }
}

