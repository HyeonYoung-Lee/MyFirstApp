package com.example.assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
    private  ArrayList<Goods> mydataList;

    public Adapter(ArrayList<Goods> dataList){
        mydataList = dataList;
    }

    // 뷰 홀더를 새로 만들때마다 호출하는 메소드. 뷰홀더와 여기에 연결된 view 생성, 초기화
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recyclerview, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // 뷰홀더를 데이터와 연결할때 호출하는 메소드
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position)
    {
        Goods goods = mydataList.get(position);

        //ViewHolder가 관리하는 View에 position에 해당하는 데이터 바인딩
        viewHolder.goodsName.setText(mydataList.get(position).getName_goods());
        viewHolder.imageView.setImageBitmap(mydataList.get(position).getImage());
    }

    // 데이터 세트 크기 가져오는 메소드. 항목 제거/추가 불가할 때 사용
    @Override
    public int getItemCount()
    {
        //Adapter가 관리하는 전체 데이터 개수 반환
        return mydataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView goodsName;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            goodsName = itemView.findViewById(R.id.name_new);
            imageView = itemView.findViewById(R.id.image_new);

        }

    }
}



