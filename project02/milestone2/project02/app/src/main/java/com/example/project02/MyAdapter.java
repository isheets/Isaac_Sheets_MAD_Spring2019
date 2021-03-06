package com.example.project02;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.OnClickListener;

import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ButtonViewHolder> {

    private Context context;
    private List<Integer> buttonList;
    private View.OnClickListener mOnItemClickListener;

    public MyAdapter(Context context, List<Integer> buttonList) {
        this.context = context;
        this.buttonList = buttonList;
    }

    @NonNull
    @Override
    public ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_custom_layout, viewGroup,false);
        return new ButtonViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ButtonViewHolder viewHolder, int i) {
        Log.d("debuglog", "Button Number: " + i);
    }

    @Override
    public int getItemCount() {
        Log.d("debuglog", "List Length: " + buttonList.size());
        return buttonList.size();
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    class ButtonViewHolder extends RecyclerView.ViewHolder {
        Button gridButt;

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            gridButt = itemView.findViewById(R.id.gridButt);

            gridButt.setTag(this);
            gridButt.setOnClickListener(mOnItemClickListener);
        }
    }
}


