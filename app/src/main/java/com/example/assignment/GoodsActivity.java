package com.example.assignment;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/* 3번 페이지 상품목록화면 */
public class GoodsActivity extends AppCompatActivity {

    /* 리사이클러뷰 관련 */
    ArrayList<Goods> dataList;
    RecyclerView recyclerView = null;
    LinearLayoutManager linearLayoutManager = null;
    Adapter adapter = null;

    /* UI 객체 생성 */
    Button register;
    Button delete;
    Button memberInfo;

    /* 로그인 정보 인텐트 받을 변수 */
    static String id;
    static int login;


    /* 등록 다이얼로그로부터 저장되는 전역변수 */
    static String goodsName = "";             // 상품명
    static String uploadText = "";            // 등록문구
    String ImgName;                           // 이미지 이름 (상품명.PNG)
    Bitmap imgBitmap;                         // 이미지 비트맵

    private ImageView uploadImage;            // 업로드시 셋 할 이미지비트맵변수


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        // 비트맵파일 초기화
        removeDir();
        // 상품정보 프리퍼런스 초기화
        removePref();


        // 로그인 인텐트 값 받기
        Intent inIntent = getIntent();
        login = inIntent.getIntExtra("login", 0);
        id = inIntent.getStringExtra("Id");

        // 버튼 세개 객체 생성
        register = (Button) findViewById(R.id.goods_register);
        delete = (Button) findViewById(R.id.goods_delete);
        memberInfo = (Button) findViewById(R.id.goods_memberInfo);

        // 리사이클뷰 구성할 배열 생성
        dataList = new ArrayList<>();

        // 리사이클러뷰 생성
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // 어댑터 등록
        adapter = new Adapter(dataList);
        recyclerView.setAdapter(adapter);


        // 레이아웃매니저 등록
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);


        // 상품 등록버튼
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* 상품등록창 팝업 */
                AlertDialog.Builder builder = new AlertDialog.Builder(GoodsActivity.this);
                View view = LayoutInflater.from(GoodsActivity.this).inflate(R.layout.dialog_apply, null, false);
                builder.setView(view);

                AlertDialog dialogApply = builder.create();
                dialogApply.show();


                // UI 객체 생성
                Button apply = (Button) view.findViewById(R.id.pop_apply);
                Button cancle = (Button) view.findViewById(R.id.pop_cancle);
                EditText editCompany = (EditText) view.findViewById(R.id.editCompany);
                uploadImage = (ImageView) view.findViewById(R.id.uploadImg);
                EditText editGoods = (EditText) view.findViewById(R.id.editName);

                // 이미지 뷰 누르면 앨범에서 이미지 가져오기 -> 이미지 뷰에 셋
                uploadImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, 100);
                    }
                });

                // 등록버튼
                apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String company = editCompany.getText().toString();
                        goodsName = editGoods.getText().toString();                 // 상품명 지정
                        uploadText = "[" + company + "]\n" + goodsName;

                        // 이미 등록된 상품명인지 확인 - 파일명으로
                        String duplicateCheck = "";
                        boolean result = checkDuplication(goodsName);


                        if ((goodsName.equals("")) || (company.equals("")))
                            Toast.makeText(getApplicationContext(), "입력되지 않은 정보가 존재합니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                        else if (result == false)
                            Toast.makeText(getApplicationContext(), "이미 등록된 상품명입니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                        else if (imgBitmap == null)
                            Toast.makeText(getApplicationContext(), "사진을 등록해주세요.", Toast.LENGTH_SHORT).show();
                        else {
                            /* 등록하려는 상품명이 중복되지 않으면 저장 */

                            // 내부저장소에 이미지 저장
                            ImgName = goodsName + ".PNG";       // 이미지 파일 이름 = 상품명.PNG 로 지정
                            savedImg(imgBitmap);                // 이미지 저장하는 함수 호출

                            // 상품명, 등록문구 프리퍼런스 저장 (중복방지 + 삭제시 사용)
                            SharedPreferences prefs = getSharedPreferences(goodsName + "_goods", 0);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("goodsName", goodsName);
                            editor.putString("upLoadText", uploadText);
                            editor.apply();


                            // 리사이클러뷰 업데이트
                            Goods goods = new Goods(imgBitmap, uploadText);
                            dataList.add(goods);
                            adapter.notifyDataSetChanged();

                            // 이미지셋 초기화
                            imgBitmap = null;
                            uploadImage = null;

                            // 안내문구, 다이얼로그 종료
                            Toast.makeText(getApplicationContext(), "상품이 등록되었습니다", Toast.LENGTH_SHORT).show();
                            dialogApply.dismiss();

                        }

                    }
                });

                // 닫기버튼
                cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogApply.dismiss();
                    }
                });
            }
        });



        // 삭제버튼 - 기존 상품은 삭제 불가
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(GoodsActivity.this);
                View view = LayoutInflater.from(GoodsActivity.this).inflate(R.layout.dialog_delete,null,false);
                builder.setView(view);

                AlertDialog deleteDialog = builder.create();

                if(dataList.size()==0){     // 삭제할 수 있는게 없으면
                    Toast.makeText(getApplicationContext(),"삭제할 수 있는 상품이 없습니다.\n상품 등록 후 사용해주세요",Toast.LENGTH_SHORT).show();
                }else{

                    deleteDialog.show();

                    /* UI 객체 생성 */
                    EditText editDeleteGoods = (EditText) view.findViewById(R.id.dPop_editGoodsName);
                    Button deleteGoods = (Button) view.findViewById(R.id.dPop_delete) ;
                    Button cancleDelete = (Button) view.findViewById(R.id.dPop_cancle);


                    // 삭제 버튼 누르면 삭제 - 기존 상품은 삭제 불가
                    deleteGoods.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String deleteGoods = editDeleteGoods.getText().toString();

                            // 해당하는 상품명이 존재하는지 판별 - 이미지 파일명으로 판별
                            boolean result = checkIsFile(deleteGoods);

                            // 아무것도 입력되지 않았을 때
                            if (deleteGoods.equals(""))
                                Toast.makeText(getApplicationContext(), "상품명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                            else if (result == false)
                                Toast.makeText(getApplicationContext(), "해당하는 상품이 없습니다.", Toast.LENGTH_SHORT).show();
                                // 해당하는 상품이 있을 때
                            else {
                                // 이미지 비트맵 찾기
                                String path = "/data/data/com.example.assignment/files/Images/" + deleteGoods + ".PNG";
                                Bitmap bitmap = BitmapFactory.decodeFile(path);

                                // 상품목록 찾기
                                SharedPreferences prefs = getSharedPreferences(deleteGoods + "_goods", 0);
                                String text = prefs.getString("upLoadText", "");
                                Goods goods;
                                int i = 0;
                                while (i < dataList.size()) {
                                    goods = dataList.get(i);
                                    if (goods.getName_goods().equals(text)) {
                                        dataList.remove(goods);             // 데이터리스트에서 지우기
                                        break;
                                    } else
                                        i++;
                                }

                                /* 삭제  */
                                // 레이아웃 업데이트
                                adapter.notifyDataSetChanged();

                                // 이미지비트맵 삭제
                                String imgFilePath = "/data/data/com.example.assignment/files/Images/" + goodsName + ".PNG";
                                fileDelete(imgFilePath);

                                // 프리퍼런스 삭제
                                String prefFilePath = "/data/data/com.example.assignment/shared_prefs/" + goodsName + "_goods.xml";
                                fileDelete(prefFilePath);

                                // 안내문구, 다이얼로그 종료
                                Toast.makeText(getApplicationContext(), "상품 삭제 완료!", Toast.LENGTH_SHORT).show();
                                deleteDialog.dismiss();
                            }
                        }
                    });

                    // 취소 버튼 누르면 다이얼로그 종료
                    cancleDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteDialog.dismiss();
                        }
                    });
                }

            }
        });

    // 회원정보 버튼
        memberInfo.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        // 회원일때 가입한 회원 정보 보여주기
        if (login == 1) {
            SharedPreferences prefs = getSharedPreferences(id + "_info", 0);
            String name = prefs.getString("Name", "");
            String number = prefs.getString("Number", "");
            String address = prefs.getString("Address", "");

            Toast.makeText(getApplicationContext(), "개인정보\n" + "이름 : " + name + "\n" + "전화번호 : " + number + "\n" + "주소 : " + address, Toast.LENGTH_LONG).show();

        } else { // 회원이 아닐때 회원가입 여부 물어본 후 원하면 회원가입페이지로 이동
            Intent intent = new Intent(getApplicationContext(), AskJoinPopup.class);
            startActivity(intent);

        }
    }
    });

}

    // 이미지 뷰에 셋
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 100) && (resultCode == RESULT_OK) && (data != null) && (data.getData() != null)) {
            Uri selectedImageUri = data.getData();
            ContentResolver resolver = getContentResolver();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;

            try {
                InputStream inputStream = resolver.openInputStream(selectedImageUri);
                imgBitmap = BitmapFactory.decodeStream(inputStream, null, options);
                uploadImage.setImageBitmap(imgBitmap);

            } catch (Exception e) {
            }
        }
    }

    // 액티비티 로드시 PNG 파일 초기화
    public void removeDir( ) {
        try{
            File file = new File(getFilesDir() + "/Images");
            File[] childFileList = file.listFiles();
            for(File childFile : childFileList) {
                childFile.delete();    //하위 파일
            }
            file.delete();    //root 삭제

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    // 액티비티 로드시 프리퍼런스의 상품 정보만 초기화
    public void removePref( ){
        try{
            File file = new File("/data/data/com.example.assignment/shared_prefs");
            File[] childFileList = file.listFiles();
            for(File childFile : childFileList){
                String fName = childFile.getName();     // 이름의 끝 다섯글자가 "goods"면 삭제
                String splitName[] = fName.split("_");

                if(splitName[splitName.length-1].equals("goods.xml"))
                    childFile.delete();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 파일명으로 중복데이터 확인
    public boolean checkDuplication(String goodsName){
        try{
            File file = new File(getFilesDir() + "/Images");
            File[] childFileList = file.listFiles();
            for(File childFile : childFileList) {
                String checkName = goodsName + ".PNG";
                String fName = childFile.getName();

                if(checkName.equals(fName))      // 중복이면 false 리턴
                    return false;
            }
        }catch (Exception e){                           // 처음에 없으면 true 리턴
            return true;
        }
        return true;
    }

    // 파일명으로 삭제할 데이터 있는지 확인
    public boolean checkIsFile(String goodsName){
        try{
            File file = new File(getFilesDir() + "/Images");
            File[] childFileList = file.listFiles();
            for(File childFile : childFileList) {
                String checkName = goodsName + ".PNG";
                String fName = childFile.getName();

                if(checkName.equals(fName))      // 있으면 true 리턴
                    return true;
            }
        }catch (Exception e){                           // 처음에 없으면 true 리턴
            return false;
        }
        return false;
    }


    // 이미지 내부저장소에 저장
    public void savedImg (Bitmap bitmap){
        try{
            // 저장할 파일 경로
            File storageDir = new File(getFilesDir() + "/Images");
            if(!storageDir.exists())
                storageDir.mkdirs();

            File file = new File(storageDir, ImgName);
            FileOutputStream fout = null;


            try{
                fout = new FileOutputStream(file);
                imgBitmap.compress(Bitmap.CompressFormat.PNG, 1, fout);

            }catch (FileNotFoundException e){
                e.printStackTrace();
            }finally {
                try{
                    assert fout != null;
                    fout.close();
                }catch (IOException e){ e.printStackTrace();}
            }

        }catch (Exception e){
        }
    }


    // 이미지파일 삭제
    public boolean fileDelete(String filePath){
        try{
            File file = new File(filePath);
            if(file.exists()){
                if(file.delete()) {
                    return true;
                }else{
                    return false;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    // 백버튼 막기
    @Override public void onBackPressed() {
    }

}