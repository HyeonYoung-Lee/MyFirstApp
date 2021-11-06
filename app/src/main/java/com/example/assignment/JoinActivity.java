package com.example.assignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/* 2번 페이지 - 회원가입화면 */

public class JoinActivity extends AppCompatActivity {
    private EditText mEditId;
    private EditText mEditPassword;
    private EditText mEditName;
    private EditText mEditNumber;
    private EditText mEditAddress;

    TextView errorText;
    CheckBox checkboxoverlap;
    Button checkbtn;

    CheckBox checkPwLength;

    Button buttonComplete;
    RadioButton agreement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        // 에디트텍스트 객체 생성
        mEditId = (EditText) findViewById(R.id.editTextTextID);
        mEditPassword = (EditText) findViewById(R.id.editTextTextPassword);
        mEditName = (EditText) findViewById(R.id.join_name);
        mEditNumber = (EditText) findViewById(R.id.join_number);
        mEditAddress = (EditText) findViewById(R.id.join_address);

        // 아이디 경고문구 객체
        errorText = (TextView) findViewById(R.id.join_tryagain);
        // 아이디 중복 체크 객체 (invisible)
        checkboxoverlap = (CheckBox) findViewById(R.id.join_checkboxOverlap);


        // ID 중복검사 버튼
        checkbtn = (Button) findViewById(R.id.join_CheckOverlap);
        checkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Idtemp = mEditId.getText().toString();

                // 해당하는 id_info 프리퍼런스 파일 있는지 확인
                SharedPreferences prefs = getSharedPreferences(Idtemp+"_info",0);
                String Id = prefs.getString("Id","");

                if(Id.equals("")){// id가 중복되지 않았다면 파란색으로 표시 + 경고문구 숨기기 + checkboxOverlap 체크
                    mEditId.setTextColor(Color.BLUE);
                    errorText.setVisibility(View.INVISIBLE);
                    checkboxoverlap.setChecked(true);
                }else{// 등록하려는 id가 이미 등록되어있다면 빨간색으로 표시 + 경고문구 보이기 + checkboxOverlab 해제
                    mEditId.setTextColor(Color.RED);
                    errorText.setVisibility(View.VISIBLE);
                    checkboxoverlap.setChecked(false);
                }
            }
        });

        // 패스워드 규칙 체크박스 객체
        checkPwLength = (CheckBox) findViewById(R.id.checbox1);

        // 개인정보 수집 동의 라디오버튼 객체
        agreement = (RadioButton) findViewById(R.id.checkAgreement);


        // 가입완료 정보 저장 후 처음 화면으로
        buttonComplete = (Button) findViewById(R.id.join_Complete);
        buttonComplete.setOnClickListener(new View.OnClickListener() {
            // 정보 저장
            @Override
            public void onClick(View v) {
                // 비밀번호 자릿수 만족하면 체크박스 체크 + 글씨 파란색
                if ((mEditPassword.length() >= 8) && (mEditPassword.length() <= 16)){
                    checkPwLength.setChecked(true);
                    checkPwLength.setTextColor(Color.BLUE);
                    mEditPassword.setTextColor(Color.BLUE);
                }else{
                    // 자릿수 만족하지 않으면 체크박스 해제 + 글씨 빨간색
                    checkPwLength.setChecked(false);
                    checkPwLength.setTextColor(Color.RED);
                    mEditPassword.setTextColor(Color.RED);
                }


                // 아이디 중복 해제 안됨 || 비밀번호 규칙 만족 안됨
                if((checkboxoverlap.isChecked()==false)||(checkPwLength.isChecked()==false)) {
                    // 가입불가 팝업 띄우기
                    Toast.makeText(getApplicationContext(), "아이디 혹은 비밀번호를 다시 입력하세요.", Toast.LENGTH_SHORT).show();

                }else if(agreement.isChecked()==false) {  // 개인정보 동의 여부
                    // 개인정보 처리약관 동의 팝업 띄우기
                    Toast.makeText(getApplicationContext(), "개인정보 처리 약관에 동의해주세요.", Toast.LENGTH_SHORT).show();

                }else{
                    // 프레퍼런스에 회원정보 저장
                    // id별로 프레퍼런스 파일 생성
                    String fileName = mEditId.getText().toString();

                    SharedPreferences prefs = getSharedPreferences(fileName+"_info",0);
                    SharedPreferences.Editor editor = prefs.edit();
                    String Id = mEditId.getText().toString();
                    String Password = mEditPassword.getText().toString();
                    String Name = mEditName.getText().toString();
                    String Number = mEditNumber.getText().toString();
                    String Address = mEditAddress.getText().toString();

                    editor.putString("Id", Id);
                    editor.putString("Password",Password);
                    editor.putString("Name", Name);
                    editor.putString("Number", Number);
                    editor.putString("Address", Address);
                    editor.apply();

                    // 처음화면으로
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

}
