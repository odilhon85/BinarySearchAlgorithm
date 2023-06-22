package uz.dawo.binarysearchalgorithm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    TextInputEditText firstNum, lastNum;
    Button start;

    SharedPreferences sPref;
    final String ANDROID_KEY = "androidKey";
    final String USER_KEY = "userKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstNum = findViewById(R.id.tiFirstNum);
        lastNum = findViewById(R.id.tiSecNum);

        start = findViewById(R.id.btnStart);
        start.setOnClickListener(v ->{
            String first = firstNum.getText().toString();
            String last = lastNum.getText().toString();

            if(!first.equals("") && !last.equals("")){
                Intent intent = new Intent(MainActivity.this, ChooseUser.class);
                intent.putExtra("first", Integer.parseInt(first));
                intent.putExtra("last", Integer.parseInt(last));
                startActivity(intent);
            }else{
                Toast.makeText(MainActivity.this, "Please fill the blanks", Toast.LENGTH_SHORT).show();
            }
        });


        setToDefault();

    }

    public void setToDefault(){
        sPref = getSharedPreferences("BinarySearchAlgorithm",MODE_PRIVATE);
        sPref.edit().putInt(ANDROID_KEY, 0).apply();
        sPref.edit().putInt(USER_KEY, 0).apply();
    }

    @Override
    protected void onResume() {
        setToDefault();
        super.onResume();
    }
}