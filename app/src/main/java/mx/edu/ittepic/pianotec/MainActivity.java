package mx.edu.ittepic.pianotec;

import android.content.SharedPreferences;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SoundPool soundPool;
    private int sound_a, sound_b, sound_c, sound_error;

    ImageView iv_11, iv_12, iv_13,
            iv_21, iv_22, iv_23,
            iv_31, iv_32, iv_33,
            iv_41, iv_42, iv_43,
            iv_51, iv_52, iv_53;

    Button b_play;

    TextView tv_time, tv_score, tv_best;

    Random r;

    int rockLocationRow1, rockLocationRow2, rockLocationRow3, rockLocationRow4, rockLocationRow5;

    int frameImage, pawInFrameImage, tapImage, emptyImage;

    int currentScore = 0;
    int bestScore;

    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soundPool = new SoundPool.Builder().setMaxStreams(5).build();

        sound_a = soundPool.load(this, R.raw.d1, 1);
        sound_b = soundPool.load(this, R.raw.a1, 1);
        sound_c = soundPool.load(this, R.raw.b1, 1);
        sound_error = soundPool.load(this, R.raw.error, 1);

        final SharedPreferences preferences = getSharedPreferences("PREFS",0);
        bestScore = preferences.getInt("highscore", 0);

        iv_11 = (ImageView) findViewById(R.id.iv_11);
        iv_12 = (ImageView) findViewById(R.id.iv_12);
        iv_13 = (ImageView) findViewById(R.id.iv_13);

        iv_21 = (ImageView) findViewById(R.id.iv_21);
        iv_22 = (ImageView) findViewById(R.id.iv_22);
        iv_23 = (ImageView) findViewById(R.id.iv_23);

        iv_31 = (ImageView) findViewById(R.id.iv_31);
        iv_32 = (ImageView) findViewById(R.id.iv_32);
        iv_33 = (ImageView) findViewById(R.id.iv_33);

        iv_41 = (ImageView) findViewById(R.id.iv_41);
        iv_42 = (ImageView) findViewById(R.id.iv_42);
        iv_43 = (ImageView) findViewById(R.id.iv_43);

        iv_51 = (ImageView) findViewById(R.id.iv_51);
        iv_52 = (ImageView) findViewById(R.id.iv_52);
        iv_53 = (ImageView) findViewById(R.id.iv_53);

        b_play = (Button) findViewById(R.id.b_play);

        tv_score = (TextView) findViewById(R.id.tv_score);
        tv_score.setText("SCORE: "+currentScore);

        tv_best = (TextView) findViewById(R.id.tv_best);
        tv_best.setText("BEST: "+bestScore);

        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_time.setText("TIME: "+millisToTime(15000));

        r = new Random();

        loadImages();

        timer = new CountDownTimer(15000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_time.setText("TIME: "+millisToTime(millisUntilFinished) + 1);
            }

            @Override
            public void onFinish() {
                soundPool.play(sound_error, 1, 1, 0, 0, 1);

                tv_time.setText("TIME: "+millisToTime(0));

                iv_31.setEnabled(false);
                iv_32.setEnabled(false);
                iv_33.setEnabled(false);
                b_play.setVisibility(View.VISIBLE);

                iv_11.setImageResource(emptyImage);
                iv_12.setImageResource(emptyImage);
                iv_13.setImageResource(emptyImage);

                iv_21.setImageResource(emptyImage);
                iv_22.setImageResource(emptyImage);
                iv_23.setImageResource(emptyImage);

                iv_31.setImageResource(emptyImage);
                iv_32.setImageResource(emptyImage);
                iv_33.setImageResource(emptyImage);

                iv_41.setImageResource(emptyImage);
                iv_42.setImageResource(emptyImage);
                iv_43.setImageResource(emptyImage);

                iv_51.setImageResource(emptyImage);
                iv_52.setImageResource(emptyImage);
                iv_53.setImageResource(emptyImage);

                Toast.makeText(MainActivity.this,"¡Game Over!" ,Toast.LENGTH_LONG).show();

                if(currentScore > bestScore){
                    bestScore = currentScore;
                    tv_best.setText("BEST: "+bestScore);

                    SharedPreferences preferences1 = getSharedPreferences("PREFS", 0);
                    SharedPreferences.Editor editor = preferences1.edit();
                    editor.putInt("highscore", bestScore);
                    editor.apply();
                }
            }
        };

        iv_31.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rockLocationRow3 == 1){
                    soundPool.play(sound_a, 1, 1, 0, 0, 1);
                    continueGame();
                }else{
                    endGame();
                }
            }
        });

        iv_32.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rockLocationRow3 == 2){
                    soundPool.play(sound_b, 1, 1, 0, 0, 1);
                    continueGame();
                }else{
                    endGame();
                }
            }
        });


        iv_33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rockLocationRow3 == 3){
                    soundPool.play(sound_c, 1, 1, 0, 0, 1);
                    continueGame();
                }else{
                    endGame();
                }
            }
        });

        b_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initGame();
            }
        });

    }//onCreate

    private void continueGame() {

        //row5
        rockLocationRow5 = rockLocationRow4;
        setRockLocation(rockLocationRow5, 5);

        //row4
        rockLocationRow4 = rockLocationRow3;
        setRockLocation(rockLocationRow4, 4);

        //row3
        rockLocationRow3 = rockLocationRow2;
        setRockLocation(rockLocationRow3, 3);

        //row2
        rockLocationRow2 = rockLocationRow1;
        setRockLocation(rockLocationRow2, 2);

        //row1
        rockLocationRow1 = r.nextInt(3)+1;
        setRockLocation(rockLocationRow1, 1);

        currentScore++;
        tv_score.setText("SCORE: "+currentScore);
    }

    private void initGame() {
        iv_31.setEnabled(true);
        iv_32.setEnabled(true);
        iv_33.setEnabled(true);
        b_play.setVisibility(View.INVISIBLE);

        currentScore = 0;
        tv_score.setText("SCORE: "+currentScore);

        timer.start();

        //row5 - nothing
        //row4
        rockLocationRow4 = 2;
        iv_42.setImageResource(pawInFrameImage);

        //row3
        rockLocationRow3 = 2;
        iv_32.setImageResource(tapImage);

        //row2
        rockLocationRow2 = r.nextInt(3)+1;
        setRockLocation(rockLocationRow2, 2);

        //row1
        rockLocationRow1 = r.nextInt(3)+1;
        setRockLocation(rockLocationRow1, 1);
    }

    private void endGame(){

        soundPool.play(sound_error, 1, 1, 0, 0, 1);

        timer.cancel();

        iv_31.setEnabled(false);
        iv_32.setEnabled(false);
        iv_33.setEnabled(false);
        b_play.setVisibility(View.VISIBLE);

        iv_11.setImageResource(emptyImage);
        iv_12.setImageResource(emptyImage);
        iv_13.setImageResource(emptyImage);

        iv_21.setImageResource(emptyImage);
        iv_22.setImageResource(emptyImage);
        iv_23.setImageResource(emptyImage);

        iv_31.setImageResource(emptyImage);
        iv_32.setImageResource(emptyImage);
        iv_33.setImageResource(emptyImage);

        iv_41.setImageResource(emptyImage);
        iv_42.setImageResource(emptyImage);
        iv_43.setImageResource(emptyImage);

        iv_51.setImageResource(emptyImage);
        iv_52.setImageResource(emptyImage);
        iv_53.setImageResource(emptyImage);

        Toast.makeText(MainActivity.this,"¡Failed!" ,Toast.LENGTH_LONG).show();

    }

    private void setRockLocation(int place, int row) {
        if(row == 1){
            iv_11.setImageResource(emptyImage);
            iv_12.setImageResource(emptyImage);
            iv_13.setImageResource(emptyImage);

            switch (place){
                case 1:
                    iv_11.setImageResource(frameImage);
                    break;
                case 2:
                    iv_12.setImageResource(frameImage);
                    break;
                case 3:
                    iv_13.setImageResource(frameImage);
                    break;
            }
        }//if row == 1

        if(row == 2){
            iv_21.setImageResource(emptyImage);
            iv_22.setImageResource(emptyImage);
            iv_23.setImageResource(emptyImage);

            switch (place){
                case 1:
                    iv_21.setImageResource(frameImage);
                    break;
                case 2:
                    iv_22.setImageResource(frameImage);
                    break;
                case 3:
                    iv_23.setImageResource(frameImage);
                    break;
            }
        }//if row == 2

        if(row == 3){
            iv_31.setImageResource(emptyImage);
            iv_32.setImageResource(emptyImage);
            iv_33.setImageResource(emptyImage);

            switch (place){
                case 1:
                    iv_31.setImageResource(tapImage);
                    break;
                case 2:
                    iv_32.setImageResource(tapImage);
                    break;
                case 3:
                    iv_33.setImageResource(tapImage);
                    break;
            }
        }//if row == 3

        if(row == 4){
            iv_41.setImageResource(emptyImage);
            iv_42.setImageResource(emptyImage);
            iv_43.setImageResource(emptyImage);

            switch (place){
                case 1:
                    iv_41.setImageResource(pawInFrameImage);
                    break;
                case 2:
                    iv_42.setImageResource(pawInFrameImage);
                    break;
                case 3:
                    iv_43.setImageResource(pawInFrameImage);
                    break;
            }
        }//if row == 4

        if(row == 5){
            iv_51.setImageResource(emptyImage);
            iv_52.setImageResource(emptyImage);
            iv_53.setImageResource(emptyImage);

            switch (place){
                case 1:
                    iv_51.setImageResource(frameImage);
                    break;
                case 2:
                    iv_52.setImageResource(frameImage);
                    break;
                case 3:
                    iv_53.setImageResource(frameImage);
                    break;
            }
        }//if row == 5
    }

    private int millisToTime(long millis){
        return (int)millis/1000;
    }

    private void loadImages() {
        frameImage = R.drawable.ic_frame;
        pawInFrameImage = R.drawable.ic_paw_frame;
        tapImage = R.drawable.ic_tap;
        emptyImage = R.drawable.ic_empty;
    }

}






















