package com.thanhxuyen.multipledeletedemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView tvEmpty;
    ArrayList<String> arrayList = new ArrayList<>();
    MainAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycle_view);
        tvEmpty = findViewById(R.id.tv_empty);

        arrayList.addAll(Arrays.asList("One", "Two", "Three", "Four"," Five","Six",
                "Seven","Eight","Nine","Ten","Eleven","Twelve","Thirteen"
                ,"Fourteen","Fifteen"));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter =new MainAdapter(this,arrayList, tvEmpty);
        recyclerView.setAdapter(adapter);
    }
}







