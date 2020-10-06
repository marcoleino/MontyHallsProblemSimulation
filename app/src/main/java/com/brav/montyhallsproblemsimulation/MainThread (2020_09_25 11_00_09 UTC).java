package com.brav.montyhallsproblemsimulation;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;

public class MainThread extends Thread {

    MainActivity a;
    public void run() {
        startElaboration();
    }

    private void startElaboration() {

            a.st1 = "";
            a.st2 = "";
            Letter correct = Letter.randomLetter();
            Letter chosen = Letter.randomLetter();
            a.st1 = ("Correct: " + correct.name() + "\n\n");
            a.t1.invalidate();
            a.t2.invalidate();
            a.st1 += ("Chosen: " + chosen.name() + "\n\n");

            Letter ship;

            while (true) {
                ship = Letter.randomLetter();
                if (ship != correct && ship != chosen) break;
            }
            a.st1 += ("Behind door " + ship.name());
            a.st1 += (" there is a ship\n\n");


            Letter change;
            while (true) {
                change = Letter.randomLetter();
                if (change != chosen && change != ship) break;
            }

            a.st1 += ("\n\n");
            a.st2 += ("\n\n\n\n\n\n\n\n\n");

            a.st1 += ("I change to door " + change + "\n\n");
            a.st2 += ("I keep door " + chosen + "\n\n");

            if (correct == chosen) {
                a.st2 += ("I was right\n");
                a.st1 += ("I was wrong\n");
                a.c2++;
            } else {
                a.st1 += ("I was right\n");
                a.st2 += ("I was wrong\n");
                a.c1++;
            }
            a.done++;


            Message msg = new Message();
            Bundle b = new Bundle();
            b.putString("t1", a.st1);
            b.putString("t2", a.st2);
            msg.setData(b);
            a.handler.sendMessage(msg);


    }
    public MainThread (MainActivity a){
        this.a = a;
    }



}
