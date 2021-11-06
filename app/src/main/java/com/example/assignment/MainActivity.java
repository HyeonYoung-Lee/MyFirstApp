package com.example.assignment;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/* 1번 페이지 - 로그인 화면 */
public class MainActivity extends AppCompatActivity {
    private EditText mEditId;
    private EditText mEditPassword;

    Button buttonLogin;
    Button buttonJoin;
    Button buttonGoToGoods;
    private ActivityResultLauncher<Intent> resultLauncher;

    // 로그인 유무 판별할 전역변수 (0==비로그인/ 1== 로그인)
    static int login = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // editview 구성 - 아이디, 비밀번호
        mEditId = (EditText) findViewById(R.id.main_inputId);
        mEditPassword = (EditText) findViewById(R.id.main_inputPw);


        // 로그인 버튼
        buttonLogin = (Button) findViewById(R.id.main_Login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputId = mEditId.getText().toString();
                String inputPw = mEditPassword.getText().toString();
                // 입력한 id 명의 _info 프리퍼런스 파일 불러오기
                SharedPreferences prefs = getSharedPreferences(inputId+"_info",0);
                // 아이디와 비밀번호 불러오기
                String Id = prefs.getString("Id", "");
                String PW = prefs.getString("Password","");


                if(Id.equals(""))       // id 존재하지 않을때
                    Toast.makeText(getApplicationContext(), "등록된 아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();

                else if(inputPw.equals(PW)==false)  // 비밀번호가 일치하지 않을때
                    Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();

                else{   // id, 비밀번호 입력이 정상이면 login 변수 1로 변경 + goods 페이지 이동
                    login = 1;
                    try{ // 인텐트에 id 값, login(==1) 값 넘겨줌
                        Toast.makeText(getApplicationContext(),"로그인 성공",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), GoodsActivity.class);
                        intent.putExtra("Id", mEditId.getText().toString());
                        intent.putExtra("login", login);
                        startActivity(intent);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });


        // 회원가입버튼
        buttonJoin = (Button) findViewById(R.id.main_Join);
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Join 페이지 이동
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });


        // 로그인 없이 상품목록 가는 버튼
        buttonGoToGoods = (Button) findViewById(R.id.main_GoToGoods);
        buttonGoToGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그인, 회원가입 없이 Goods 페이지 이동 (login == 0)
                Toast.makeText(getApplicationContext(),"로그인 없이 접속했습니다",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), GoodsActivity.class);
                intent.putExtra("Id", "");
                intent.putExtra("login",login);
                startActivity(intent);
            }
        });
    }
}