package com.example.sofian.kyodai;


    import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


// declaration de notre activity heritee de Activity
    public class j_kyodai extends Activity {

        private kyodaiView mkyodaiView;

        public static boolean fin = false;
        public static String score;


        /**
         * Called when the activity is first created.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            // initialise notre activity avec le constructeur parent
            super.onCreate(savedInstanceState);
            // charge le fichier main.xml comme vue de l'activit�
            setContentView(R.layout.main);
            if (fin) {

                Intent secondeActivite = new Intent(j_kyodai.this, Main.class);

                startActivity(secondeActivite);
            }


            // recuperation de la vue une voie cree � partir de son id
            mkyodaiView = (kyodaiView)findViewById(R.id.kyodaiView);

            mkyodaiView.setVisibility(View.VISIBLE);


        }

        public void onResume(Bundle savedInstanceState) {
            super.onResume();

        }

        protected void onStop() {
            super.onStop();
        }

        protected void onStart() {

            super.onStart();
            mkyodaiView.setVisibility(View.VISIBLE);
        }
    }