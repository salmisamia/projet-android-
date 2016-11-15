package com.example.sofian.kyodai;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

    // Declaration des images

    public class kyodaiView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

        // Declaration des images
        // constante modelisant les differentes types de cases
        static final int CST_block = 0;
        static final int CST_diamant = 1;
        static final int CST_perso = 2;
        static final int CST_zone = 3;
        //  les variable couleur
        private Bitmap vide;

        private j_kyodai active;

        // initialisation des images

        private Bitmap image6;
        private Bitmap image5;
        private Bitmap image4;
        private Bitmap image3;
        private Bitmap image2;
        private Bitmap image1;


        // Declaration des objets Ressources et Context permettant d'acc�der aux ressources de notre application et de les charger
        private Resources mRes;
        private Context mContext;

        // tableau modelisant la carte du jeu
        int[][] carte;
        int cpt = 0;
        int j1;
        int i1;

        // ancres pour pouvoir centrer la carte du jeu
        int carteTopAnchor;                   // coordonn�es en Y du point d'ancrage de notre carte
        int carteLeftAnchor;                  // coordonn�es en X du point d'ancrage de notre carte

        // variable de lemplacement initialiser a une place indifiner

        int place = 3; // la place de chaque  vecteur
        int pv;
        int score2 = 0;
        boolean clic = false; //teste de touche sur la zone des vecteur
        boolean xx;
        // taille de la carte
        static final int carteWidth = 11;
        static final int carteHeight = 11;
        static final int carteTileSize = 80;


        // constante modelisant les differentes types de cases

        static final int CST_image6 = 6;
        static final int CST_image5 = 5;
        static final int CST_image4 = 4;
        static final int CST_image3 = 3;
        static final int CST_image2 = 2;
        static final int CST_image1 = 1;
        static final int CST_vide = 0;

        int[][] vect;
        int[][] sup;
        int[] depX = {36, 141, 247};
        int[] depY = {330, 330, 330};

        // tableau de reference du terrain
        int[][] ref1 = {
                {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
                {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
                {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
                {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
                {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
                {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
                {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
                {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide}

        };


        // position de reference des diamants
        int[][] refdiamants = {
                {2, 3},
                {2, 6},
                {6, 3},
                {6, 6}
        };

        // position de reference du joueur
        int refxPlayer = 4;
        int refyPlayer = 1;

        // position courante des diamants
        int[] position = {0, 0, 0};


        // position courante du joueur
        int xPlayer = 4;
        int yPlayer = 1;


        // thread utiliser pour animer les zones de depot des diamants
        private boolean in = true;
        private Thread cv_thread;
        SurfaceHolder holder;

        Paint paint;

        /**
         * The constructor called from the main JetBoy activity
         *
         * @param context
         * @param attrs
         */
        public kyodaiView(Context context, AttributeSet attrs) {
            super(context, attrs);

            active = new j_kyodai();
            // permet d'ecouter les surfaceChanged, surfaceCreated, surfaceDestroyed
            holder = getHolder();
            holder.addCallback(this);

            // chargement des images
            mContext = context;
            mRes = mContext.getResources();
            vide = BitmapFactory.decodeResource(mRes, R.drawable.vide);
            image1 = BitmapFactory.decodeResource(mRes, R.drawable.image1);
            image2 = BitmapFactory.decodeResource(mRes, R.drawable.image2);
            image3 = BitmapFactory.decodeResource(mRes, R.drawable.image3);
            image4 = BitmapFactory.decodeResource(mRes, R.drawable.image4);
            image5 = BitmapFactory.decodeResource(mRes, R.drawable.image5);
            image6 = BitmapFactory.decodeResource(mRes, R.drawable.image6);

            // initialisation des parmametres du jeu
            initparameters();

            // creation du thread
            cv_thread = new Thread(this);
            // prise de focus pour gestion des touches
            setFocusable(true);

        }


        // chargement des trois vecteur de departe da facon aleatoire
        private void loadlevel() {
            Random generateur = new Random();
            for (int i = 0; i < 11; i++) {
                for (int j = 0; j < 11; j++) {
                    int randomint = 0 + generateur.nextInt(7 - 0);
                    carte[i][j] = randomint;

                }
            }

        }

        //  enregestrer les preference


        // initialisation du jeu
        public void initparameters() {

            xx = true;
            if (score2 == 0) {
                carte = new int[carteHeight][carteWidth];

                loadlevel();
                vect = new int[3][3];
                //loadvect();
            }
            carteTopAnchor = (getHeight() - carteHeight * carteTileSize) / 2;
            carteLeftAnchor = (getWidth() - carteWidth * carteTileSize) / 2;
            xPlayer = refxPlayer;
            yPlayer = refyPlayer;

            if ((cv_thread != null) && (!cv_thread.isAlive())) {
                cv_thread.start();
                Log.e("-FCT-", "cv_thread.start()");
            }
        }


        // dessine une alerte
        private void showAbout() {
            final boolean[] reponse = new boolean[1];
            AlertDialog.Builder about = new AlertDialog.Builder(mContext);
            about.setTitle("Oops");
            TextView l_viewabout = new TextView(mContext);


            l_viewabout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
            l_viewabout.setPadding(20, 10, 20, 10);
            l_viewabout.setTextSize(20);
            l_viewabout.setText(" - Votre main:" + score2 + "\n \n- Voulez vous Continuer");

            l_viewabout.setMovementMethod(LinkMovementMethod.getInstance());
            about.setView(l_viewabout);
            about.setPositiveButton("OUI", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    score2 = 0;
                    initparameters();

                }

            });
            about.setNegativeButton("NON", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    active.finish();


                }

            });
            about.show();

        }

        // entregestrer preference


        // foction qui returne la position
        int Position(int p) {
            if (p < 3)
                p++;
            else p = 0;

            return p;
        }


        // paint le main

        public void text(Canvas canvas) {

            String xt = Integer.toString(score2);
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(12);
            canvas.drawText(xt, 50, 462, paint);
        }


        // dessin de la carte du jeu
        private void paintcarte(Canvas canvas) {
            for (int i = 0; i < carteHeight; i++) {
                for (int j = 0; j < carteWidth; j++) {
                    switch (carte[i][j]) {
                        case CST_vide:
                            canvas.drawBitmap(vide, i * carteTileSize, j * carteTileSize, null);
                            break;
                        case CST_image1:
                            canvas.drawBitmap(image1, i * carteTileSize, j * carteTileSize, null);
                            break;
                        case CST_image2:
                            canvas.drawBitmap(image2, i * carteTileSize, j * carteTileSize, null);
                            break;
                        case CST_image3:
                            canvas.drawBitmap(image3, i * carteTileSize, j * carteTileSize, null);
                            break;
                        case CST_image4:
                            canvas.drawBitmap(image4, i * carteTileSize, j * carteTileSize, null);
                            break;
                        case CST_image5:
                            canvas.drawBitmap(image5, i * carteTileSize, j * carteTileSize, null);
                            break;
                        case CST_image6:
                            canvas.drawBitmap(image6, i * carteTileSize, j * carteTileSize, null);
                            break;

                    }
                }
            }
        }

        // -----------------------------------------------------------
        private boolean estvalide(int x1, int y1, int x2, int y2) {
            boolean valide = true;
            if (x1 > x2 && y1 == y2) {
                for (int i = x2 + 1; i < x1; i++) {
                    if (carte[i][y1] != 0) {
                        valide = false;
                        break;
                    }

                }

            } else if (x2 > x1 && y1 == y2) {
                for (int i = x1 + 1; i < x2; i++) {
                    if (carte[i][y1] != 0) {
                        valide = false;
                        break;
                    }

                }


            }
            return valide;
        }

        // dessin du jeu (fond uni, en fonction du jeu gagne ou pas dessin du plateau et du joueur des diamants et des fleches)
        private void nDraw(Canvas canvas) {

            canvas.drawRGB(48, 48, 48);

            paintcarte(canvas);
            //paintvect(canvas);
            text(canvas);


        }

        // callback sur le cycle de vie de la surfaceview
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.i("-> FCT <-", "surfaceChanged " + width + " - " + height);
            initparameters();
        }

        public void surfaceCreated(SurfaceHolder arg0) {
            Log.i("-> FCT <-", "surfaceCreated");
        }


        public void surfaceDestroyed(SurfaceHolder arg0) {
            Log.i("-> FCT <-", "surfaceDestroyed");
        }

        /**
         * run (run du thread cr��)
         * on endort le thread, on modifie le compteur d'animation, on prend la main pour dessiner et on dessine puis on lib�re le canvas
         */
        public void run() {
            Canvas c = null;
            while (in) {
                try {
                    cv_thread.sleep(40);

                    try {
                        c = holder.lockCanvas(null);
                        nDraw(c);
                    } finally {
                        if (c != null) {
                            holder.unlockCanvasAndPost(c);
                        }
                    }
                } catch (Exception e) {
                    Log.e("-> RUN <-", "PB DANS RUN");
                }
            }
        }


        // fonction permettant de recuperer les evenements tactiles
        public boolean onTouchEvent(MotionEvent event) {
            final int y = (int) event.getY();
            final int x = (int) event.getX();


            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.i("-> FCT <-", "onTouchEvent: ");

                if (y > 60 && y < 379) {

                    clic = true;

                }else { cpt =0; clic =false;}

            }
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
            }


            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (clic) {

                    int i = (int) (x / 80);
                    int j = (int) ((y) / 80);
                    cpt++;
                    switch (cpt) {
                        case 1: {
                            j1 = j;
                            i1 = i;
                        }
                        default:
                            break;
                    }
                    if (cpt == 2 && carte[i1][j1] == carte[i][j]) {
                        if (((i == i1 && j != j1) || (j == j1 && i != i1)) && ((i == 0 || i == 10) || (j == 0 || j == 10)) || estvalide(i1, j1, i, j)) {
                            carte[i][j] = 0;
                            carte[i1][j1] = 0;
                            score2+= 2;
                        }
                        cpt = 0;
                    } else if (cpt == 2) cpt = 0;

                            }


            }

            return true;


        }
    }

