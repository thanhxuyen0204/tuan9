package com.thanhxuyen.multipledeletedemo;

import android.app.Activity;
import android.graphics.Color;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    Activity activity;
    ArrayList<String> arrayList;
    TextView tvEmpty;
    MainViewModel mainViewModel;
    boolean isEnable = false;
    boolean isSelectAll = false;
    ArrayList<String> selectList = new ArrayList<>();

    public MainAdapter(Activity activity, ArrayList<String> arrayList, TextView tvEmpty){
        this.activity = activity;
        this.arrayList= arrayList;
        this.tvEmpty = tvEmpty;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_main,parent,false);
        mainViewModel = ViewModelProviders.of((FragmentActivity)activity)
                .get(MainViewModel.class);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            holder.textView.setText(arrayList.get(position));
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (!isEnable){
                        ActionMode.Callback callback = new ActionMode.Callback() {
                            @Override
                            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                                MenuInflater menuInflater= actionMode.getMenuInflater();
                                menuInflater.inflate(R.menu.menu,menu);
                                return true;
                            }

                            @Override
                            public boolean onPrepareActionMode(final ActionMode actionMode, Menu menu) {
                                isEnable =true;
                                ClickItem(holder);
                                mainViewModel.getText().observe((LifecycleOwner) activity
                                        , new Observer<String>() {
                                            @Override
                                            public void onChanged(String s) {
                                                actionMode.setTitle(String.format("%s Selected",s));
                                            }
                                        });
                                return true;
                            }

                            @Override
                            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                                int id = menuItem.getItemId();
                                switch (id){
                                    case R.id.menu_delete:
                                        for (String s: selectList){
                                            arrayList.remove(s);
                                        }
                                        if (arrayList.size() == 0){
                                            tvEmpty.setVisibility(View.VISIBLE);

                                        }
                                        actionMode.finish();
                                        break;
                                    case R.id.menu_select_all:
                                        if (selectList.size() == arrayList.size()){
                                            isSelectAll =false;
                                            selectList.clear();
                                        }else {
                                            isSelectAll =true;
                                            selectList.clear();
                                            selectList.addAll(arrayList);
                                        }
                                        mainViewModel.setText(String.valueOf(selectList.size()));
                                        notifyDataSetChanged();
                                        break;
                                }
                                return true;
                            }

                            @Override
                            public void onDestroyActionMode(ActionMode actionMode) {
                                isEnable=false;
                                isSelectAll=false;
                                selectList.clear();;
                                notifyDataSetChanged();
                            }
                        };
                        ((AppCompatActivity) view.getContext()).startActionMode(callback);
                    }else{
                        ClickItem(holder);
                    }
                    return true;
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isEnable){
                        ClickItem(holder);
                    }else{
                        Toast.makeText(activity
                                ,"You Clicked"+ arrayList.get(holder.getAdapterPosition())
                                , Toast.LENGTH_SHORT).show();
                    }
                }
            });
            if (isSelectAll){
                holder.ivCheckBox.setVisibility(View.VISIBLE);
                holder.itemView.setBackgroundColor(Color.LTGRAY);
            }else{
                holder.ivCheckBox.setVisibility(View.GONE);
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            }
    }

    private void ClickItem(ViewHolder holder) {
        String s=arrayList.get(holder.getAdapterPosition());
        if (holder.ivCheckBox.getVisibility() == View.GONE){
            holder.ivCheckBox.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor(Color.LTGRAY);
            selectList.add(s);
        }else {
            holder.ivCheckBox.setVisibility(View.GONE);
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            selectList.remove(s);
        }
        mainViewModel.setText(String.valueOf(selectList.size()));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView ivCheckBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
            ivCheckBox = itemView.findViewById(R.id.iv_check_box);
        }
    }
}
