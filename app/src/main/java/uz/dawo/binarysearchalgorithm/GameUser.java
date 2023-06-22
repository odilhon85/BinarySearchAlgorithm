package uz.dawo.binarysearchalgorithm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.Objects;

public class GameUser extends AppCompatActivity {

    TextInputEditText randomNum;
    TextInputLayout tiLayout;

    Button btStart;
    Button btnBigger;
    Button btnEqual;
    Button btnSmaller;

    TextView resultViewer;
    TextView foundNum;

    int firstNum;
    int lastNum;

    int initialBorder;
    int terminalBorder;

    int guessedNumber;
    int tryNumber = 0;

    SoundPool sp;
    final int MAX_STREAMS = 1;
    Integer correct, wrong;

    SharedPreferences sPref;
    final String USER_KEY = "userKey";
    final String ANDROID_KEY = "androidKey";
    int user_try_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_user);

        setTitle("Android must find number");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        firstNum = getIntent().getIntExtra("first", 0);
        lastNum = getIntent().getIntExtra("last", 0);

        initialBorder = firstNum;
        terminalBorder = lastNum;

        randomNum = findViewById(R.id.tiRandomNumber);
        tiLayout = findViewById(R.id.tiLayout);
        String hint = "Choose number between "+firstNum+" and "+lastNum;
        tiLayout.setHint(hint);

        resultViewer = findViewById(R.id.tvResult);
        resultViewer.setMovementMethod(new ScrollingMovementMethod());
        foundNum = findViewById(R.id.tvFoundNumber);
        foundNum.setVisibility(View.GONE);

        btStart = findViewById(R.id.btnStartFinding);
        btnBigger = findViewById(R.id.btnBigger);
        btnEqual = findViewById(R.id.btnEqual);
        btnSmaller = findViewById(R.id.btnSmaller);

        btnBigger.setEnabled(false);
        btnEqual.setEnabled(false);
        btnSmaller.setEnabled(false);

        btStart.setOnClickListener(v ->{
            String enteredNumber = randomNum.getText().toString();
            if(!enteredNumber.equals("")){
                int insertedNum = Integer.parseInt(enteredNumber);
                if(insertedNum<firstNum || insertedNum>lastNum){
                    Toast.makeText(GameUser.this, "You insert wrong number", Toast.LENGTH_SHORT).show();
                }else{
                    setResult("=> "+randomNum.getText().toString()+" <=");
                    guessedNumber = Integer.parseInt(randomNum.getText().toString());
                    btStart.setVisibility(View.GONE);
                    tiLayout.setVisibility(View.GONE);
                    foundNum.setVisibility(View.VISIBLE);
                    btnBigger.setEnabled(true);
                    btnEqual.setEnabled(true);
                    btnSmaller.setEnabled(true);
                    findTheNumberForFirstTime();
                    String msg = ""+tryNumber+". "+getNum();
                    setResult(msg);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            }else{
                Toast.makeText(GameUser.this, "Please fill the blank", Toast.LENGTH_SHORT).show();
            }
        });

        btnBigger.setOnClickListener(v ->{
            if(getNum()<guessedNumber && getNum()!=guessedNumber){
                initialBorder = getNum();
                findTheNumber();
                String msg = ""+tryNumber+". "+getNum();
                setResult(msg);
                playWrong();
            }else{
                dontCheat();
            }

        });

        btnSmaller.setOnClickListener(v ->{
            if(getNum()>guessedNumber && getNum()!=guessedNumber){
                terminalBorder = getNum();
                findTheNumber();
                String msg = ""+tryNumber+". "+getNum();
                setResult(msg);
                playWrong();
            }else{
                dontCheat();
            }

        });

        btnEqual.setOnClickListener(v ->{
            if(getNum()==guessedNumber){
                playCorrect();
                saveScore(tryNumber);
                super.onBackPressed();
            }else{
                dontCheat();
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
        sPref.edit().putInt(ANDROID_KEY, tryNum).apply();
    }

    public void findTheNumberForFirstTime(){
        int result = initialBorder+(terminalBorder-initialBorder)/2;
        foundNum.setText(String.valueOf(result));
        ++tryNumber;
    }

    public void findTheNumber(){
        int result = initialBorder+(terminalBorder-initialBorder)/2;
        if(guessedNumber == getNum()+1 && result == getNum()){
            result+=1;
        }
        foundNum.setText(String.valueOf(result));
        ++tryNumber;
    }

    public void setResult(String msg){
        String result = resultViewer.getText().toString();
        result+="\n"+msg;
        resultViewer.setText(result);
    }

    public int getNum(){
        return Integer.parseInt(foundNum.getText().toString());
    }

    public void dontCheat(){
        Toast.makeText(GameUser.this, "Don't try to cheat me!!!", Toast.LENGTH_SHORT).show();
    }

    public void playWrong(){
        sp.play(wrong, 0.1f, 0.1f, 0, 0, 1);
    }

    public void playCorrect(){
        sp.play(correct, 0.1f, 0.1f, 0, 0, 1);
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