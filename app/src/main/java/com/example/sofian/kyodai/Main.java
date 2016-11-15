package com.example.sofian.kyodai;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class Main extends AppCompatActivity {

    private Button jouer = null;
    private Button apropos=null;
    private Button retour=null;
    private Button quitter=null;
    private CheckBox check;
    MediaPlayer son;
    boolean sel;
    boolean clic;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        jouer = (Button) findViewById(R.id.button);
        quitter = (Button) findViewById(R.id.button4);
        apropos= (Button) findViewById(R.id.button3);
        check =(CheckBox) findViewById(R.id.checkBox);
        son=MediaPlayer.create(getBaseContext(),R.raw.boom);
        clic=false;


        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (check.isChecked()) {
                    sel=true;
                    son.start();

                }else{ son.pause(); sel=false;}
            }
        });



        jouer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Le premier paramètre est le nom de l'activité actuelle
                // Le second est le nom de l'activité de destination
                clic=true;
                Intent secondeActivite = new Intent( Main.this, j_kyodai.class);

                startActivity(secondeActivite);
            }
        });

        apropos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.apropos);
                retour = (Button) findViewById(R.id.retour);
                retour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        Intent Activite = new Intent( Main .this,  Main .class);
                        startActivity(Activite);

                    }
                });

            }
        });


        quitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Le premier paramètre est le nom de l'activité actuelle
                // Le second est le nom de l'activité de destination
                son.pause();
                finish();
            }
        });

    }




    protected void onStop() {
        super.onStop();
        if(!clic)
            son.pause();

    }



    protected void onStart() {

        super.onStart();

        clic=false;
        if(sel)
            son.start();


    }
}
