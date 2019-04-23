package com.example.lab08;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox todo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            todo = itemView.findViewById(R.id.checkBox);
        }
    }

    private List<Todo> mTodos;

    public ListAdapter(List<Todo> todos) {
        mTodos = todos;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = layoutInflater.inflate(R.layout.list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListAdapter.ViewHolder viewHolder, int i) {
        final Todo curTodo = mTodos.get(i);
        viewHolder.todo.setChecked(curTodo.getChecked());
        viewHolder.todo.setText(curTodo.getTitle());

        viewHolder.todo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                curTodo.setChecked(isChecked);
                notifyDataSetChanged();
            }
        });

        //context menu
        viewHolder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener(){
            @Override
            public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenu.ContextMenuInfo menuInfo) {
                //set the menu title
                menu.setHeaderTitle("Delete " + curTodo.getTitle());
                //add the choices to the menu
                menu.add(1, 1, 1, "Yes").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        removeTodo(viewHolder.getAdapterPosition());
                        Snackbar.make(v, "Item removed", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        return false;
                    }
                });
                menu.add(2, 2, 2, "No");
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTodos.size();
    }

    public void addTodo(String newItem){
        Log.d("debug", newItem);
        Todo.todoList.add(new Todo(newItem, false));
        notifyItemInserted(getItemCount());
    }

    public void removeTodo(int itemPosition){
        Todo.todoList.remove(itemPosition);
        notifyItemRemoved(itemPosition);
    }


}
