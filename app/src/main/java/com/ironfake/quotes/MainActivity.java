package com.ironfake.quotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ironfake.quotes.model.Quote;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import petrov.kristiyan.colorpicker.ColorPicker;

public class MainActivity extends AppCompatActivity
        implements QuotesListFragment.OnQuotesSelectedListener {

    private String choosedColor;
    //private QuoteDetailFragment quoteDetail;
    private int currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quotes);

        if (findViewById(R.id.fragment_container) != null){

            if (savedInstanceState != null)
                return;

            QuotesListFragment firstFragment = new QuotesListFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();

        }
        int[] integers = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24};
        ArrayList<Integer> values = new ArrayList<>();
        for (int i = 0; i < integers.length; i++) {
            values.add(integers[i]);
        }
        int maxTags = 20;
        Log.d("TAG", String.valueOf(values.size()));
        if (values.size() > maxTags){
            while (values.size() > 20){
                values.remove(values.size()-1);
            }
        }
        Log.d("TAG", String.valueOf(values.size()));
    }

    @Override
    public void onQuotesSelected(int id) {
        currentId = id;
        QuoteDetailFragment quoteDetail = (QuoteDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.quote_fragment);

        if (quoteDetail != null){
            quoteDetail.updateQuoteView(id);
        }else{
            QuoteDetailFragment newFragment = new QuoteDetailFragment();
            Bundle args = new Bundle();
            args.putInt(QuoteDetailFragment.ARG_ID, id);
            newFragment.setArguments(args);
            getSupportFragmentManager().popBackStack();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            getSupportFragmentManager().popBackStack();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.chooseColor:
                final ColorPicker colorPicker = new ColorPicker(MainActivity.this);
                final ArrayList<String> colors = new ArrayList<>();
                colors.add("#82B926");
                colors.add("#a276eb");
                colors.add("#6a3ab2");
                colors.add("#666666");
                colors.add("#FFFF00");
                colors.add("#3C8D2F");
                colors.add("#FA9F00");
                colors.add("#FF0000");

                colorPicker
                        .setDefaultColorButton(Color.parseColor("#f84c44"))
                        .setColors(colors)
                        .setColumns(5)
                        .setRoundColorButton(true)
                        .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                            @Override
                            public void onChooseColor(int position, int color) {
                                Log.d("position", "" + color);// will be fired only when OK button was tapped

                                QuoteDetailFragment quoteDetail = (QuoteDetailFragment)
                                        getSupportFragmentManager().findFragmentById(R.id.quote_fragment);

                                QuoteDetailFragment.tagsColor = colors.get(position);
                                onQuotesSelected(currentId);
                            }

                            @Override
                            public void onCancel() {
                            }
                        })
                        .show();
        }

        return super.onOptionsItemSelected(item);
    }
}
