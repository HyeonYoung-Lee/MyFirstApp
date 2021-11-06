package com.example.assignment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/* 회원가입 유무 물어보는 팝업창 */
public class AskJoinPopup extends Activity {
    Button btnYes;
    Button btnNo;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_askjoin);

        btnYes = (Button) findViewById(R.id.btnYes);
        btnNo = (Button) findViewById(R.id.btnNo);

        // 회원가입 창 가는데 동의하면 로그인화면 시작 - 팝업창 종료
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 동의하지 않으면 팝업창 종료
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    // 바깥 레이어 클릭시 안닫히게 하기
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE)
            return false;
        return true;
    }

    // 백버튼 막기
    @Override public void onBackPressed() { super.onBackPressed(); }
}
