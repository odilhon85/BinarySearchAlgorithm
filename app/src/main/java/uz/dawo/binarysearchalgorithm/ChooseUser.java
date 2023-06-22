package uz.dawo.binarysearchalgorithm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.io.IOException;

public class ChooseUser extends AppCompatActivity {

    CardView android;
    CardView user;

    LottieAnimationView android_anim;
    LottieAnimationView user_anim;

    TextView android_name;
    TextView user_name;

    SharedPreferences sPref;
    final String ANDROID_KEY = "androidKey";
    final String USER_KEY = "userKey";
    int android_try_num;
    int user_try_num;

    SoundPool sp;
    final int MAX_STREAMS = 1;
    Integer applause, draw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user);

        int firstNum = getIntent().getIntExtra("first",0);
        int lastNum = getIntent().getIntExtra("last", 0);

        android = findViewById(R.id.card_user);
        user = findViewById(R.id.card_android);

        android_name = findViewById(R.id.tv_android);
        user_name = findViewById(R.id.tv_user);

        android_anim = findViewById(R.id.lav_android);
        user_anim = findViewById(R.id.lav_user);

        user.setOnClickListener(v ->{
            Intent intent = new Intent(ChooseUser.this, GameUser.class);
            intent.putExtra("first", firstNum);
            intent.putExtra("last", lastNum);
            startActivity(intent);
        });

        android.setOnClickListener(v ->{
            Intent intent = new Intent(ChooseUser.this, GamePC.class);
            intent.putExtra("first", firstNum);
            intent.putExtra("last", lastNum);
            startActivity(intent);
        });

        updateScores();

        sp = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener((arg0, arg1, arg2) -> {
            // TODO Auto-generated method stub
        });
        try {
            applause = sp.load(getAssets().openFd("applause.mp3"), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            draw = sp.load(getAssets().openFd("draw.mp3"), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void updateScores(){
        sPref = getSharedPreferences("BinarySearchAlgorithm",MODE_PRIVATE);
        android_try_num = sPref.getInt(ANDROID_KEY,0);
        user_try_num = sPref.getInt(USER_KEY,0);
        if(android_try_num != 0 && user_try_num != 0){
            setNames(android_try_num, android_name.getText().toString() ,android_name);
            setNames(user_try_num, user_name.getText().toString() ,user_name);
            if(android_try_num < user_try_num){
                setAnimation(android_anim, user_anim, false);
                platAudio(false);
            }
            if(user_try_num < android_try_num){
                setAnimation(user_anim, android_anim, false);
                platAudio(false);
            }
            if(android_try_num == user_try_num){
                setAnimation(android_anim, user_anim, true);
                platAudio(true);
            }

            android.setClickable(false);
            user.setClickable(false);
        }else{
            if(android_try_num>0){
                setNames(android_try_num, android_name.getText().toString() ,android_name);
            }
            if(user_try_num>0){
                setNames(user_try_num, user_name.getText().toString() ,user_name);
            }
        }
    }

    public void setAnimation(LottieAnimationView winner, LottieAnimationView loser, boolean isDraw){
        if(isDraw){
            winner.setAnimation(R.raw.handshake);
            loser.setAnimation(R.raw.handshake);
        }else{
            winner.setAnimation(R.raw.win_anim);
            loser.setAnimation(R.raw.lose);
        }
    }

    public void setNames(int num, String msg, TextView view){
        if(!msg.contains(String.valueOf(num))){
            String old =msg + " => " + num;
            view.setText(old);
        }
    }

    public void platAudio(boolean isDraw){
        if(isDraw){
            sp.play(draw, 0.1f, 0.1f, 0, 0, 1);
        }else{
            sp.play(applause, 0.1f, 0.1f, 0, 0, 1);
        }
    }

    public void setScoreToDefault(){
        sPref = getSharedPreferences("BinarySearchAlgorithm",MODE_PRIVATE);
        sPref.edit().putInt(ANDROID_KEY, 0).apply();
        sPref.edit().putInt(USER_KEY, 0).apply();
    }

    @Override
    protected void onResume() {
        updateScores();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        sp.release();
        setScoreToDefault();
        super.onDestroy();
    }
}