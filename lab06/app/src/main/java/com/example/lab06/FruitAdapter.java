package com.example.lab06;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {

    ListItemClickListener itemClickListener;
    private List<Fruit> fruits;

    public FruitAdapter(List<Fruit> newFruits, ListItemClickListener fruitClickListener) {
        fruits = newFruits;
        itemClickListener = fruitClickListener;
    }




    @NonNull
    @Override
    public FruitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View fruitView = inflater.inflate(R.layout.list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(fruitView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Fruit fruit = fruits.get(i);
        viewHolder.fruitText.setText(fruit.getName());
    }

    @Override
    public int getItemCount() {
        return fruits.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView fruitText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fruitText = itemView.findViewById(R.id.textView);
            fruitText.setOnClickListener(this);
        }

        public void onClick(View V) {
            itemClickListener.onListItemClick(getAdapterPosition());
        }

    }

    public interface ListItemClickListener{
        void onListItemClick(int position);
    }
}
