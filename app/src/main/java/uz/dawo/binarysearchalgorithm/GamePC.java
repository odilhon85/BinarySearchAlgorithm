package uz.dawo.binarysearchalgorithm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class GamePC extends AppCompatActivity {

    TextInputEditText insertNum;
    TextInputLayout tiLayout;

    Button btSend;

    TextView resultViewer;

    int firstNum;
    int lastNum;

    int initialBorder;
    int terminalBorder;

    int randomNumber;
    int tryNumber = 1;

    SoundPool sp;
    final int MAX_STREAMS = 1;
    Integer correct, wrong;

    SharedPreferences sPref;
    final String USER_KEY = "userKey";
    final String ANDROID_KEY = "androidKey";
    int android_try_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setTitle("You must find number");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        firstNum = getIntent().getIntExtra("first", 0);
        lastNum = getIntent().getIntExtra("last", 0);

        initialBorder = firstNum;
        terminalBorder = lastNum;

        generateRandomNum(firstNum, lastNum);

        tiLayout = findViewById(R.id.tiPCLayout);
        String hint = "Find number between "+firstNum+" and "+lastNum;
        tiLayout.setHint(hint);

        insertNum =findViewById(R.id.etPCNumberInsert);

        btSend = findViewById(R.id.btnSend);

        resultViewer = findViewById(R.id.tvResultShow);
        resultViewer.setText("Your entered numbers will display here");
        resultViewer.setMovementMethod(new ScrollingMovementMethod());

        btSend.setOnClickListener(v -> {
            if(insertNum.getText().toString().equals("")){
                Toast.makeText(GamePC.this, "Please fill the blank", Toast.LENGTH_SHORT).show();
            }else{
                int insertedNum = Integer.parseInt(insertNum.getText().toString());
                if(insertedNum==randomNumber){
                    playCorrect();
                    saveScore(tryNumber);
                    super.onBackPressed();
                }else{
                    if(insertedNum>randomNumber){
                        display(tryNumber+". "+insertedNum+" => my number is smaller");
                        ++tryNumber;
                        insertNum.setText("");
                       playWrong();
                    }
                    if(insertedNum<randomNumber){
                        display(tryNumber+". "+insertedNum+" => my number is bigger");
                        ++tryNumber;
                        insertNum.setText("");
                        playWrong();
                    }
                }
            }
        });

        sp = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener((arg0, arg1, arg2) -> {
            // TODO Auto-generated method stub
        });
        try {
            correct = sp.load(getAssets().openFd("win.mp3"), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            wrong = sp.load(getAssets().openFd("no.mp3"), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveScore(int tryNum){
        sPref = getSharedPreferences("BinarySearchAlgorithm",MODE_PRIVATE);
        sPref.edit().putInt(USER_KEY, tryNum).apply();
    }

    public void playWrong(){
        sp.play(wrong, 0.1f, 0.1f, 0, 0, 1);
    }

    public void playCorrect(){
        sp.play(correct, 0.1f, 0.1f, 0, 0, 1);
    }

    public void display(String msg){
        String result = resultViewer.getText().toString();
        result+="\n"+msg;
        resultViewer.setText(result);
    }

    public void generateRandomNum(int min, int max){
        randomNumber = new Random().nextInt((max - min) + 1) + min;
    }

    @Override
    protected void onDestroy() {
        sp.release();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}