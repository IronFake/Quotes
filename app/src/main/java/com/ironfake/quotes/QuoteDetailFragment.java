package com.ironfake.quotes;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.ironfake.quotes.model.Quote;
import com.ironfake.quotes.network.NetworkService;

import java.util.ArrayList;
import java.util.List;

import petrov.kristiyan.colorpicker.ColorPicker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuoteDetailFragment extends Fragment {
    final static String ARG_ID = "id";
    static String tagsColor = "#666666"; //default tag color
    private int mCurrentPosition = -1;

    private final int maxTags = 20;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null){
            mCurrentPosition = savedInstanceState.getInt(ARG_ID);
        }
        return inflater.inflate(R.layout.quote_detail_view, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        if (args != null){
            updateQuoteView(args.getInt(ARG_ID));
        }else if (mCurrentPosition != -1){
            updateQuoteView(mCurrentPosition);
        }
    }

    public void updateQuoteView(int id) {
        NetworkService.getInstance()
                .getOIKOApi()
                .getQuoteDetail(id)
                .enqueue(new Callback<Quote>() {
                    @Override
                    public void onResponse(Call<Quote> call, Response<Quote> response) {
                        final Quote quote = response.body();

                        Log.d("TAG", String.valueOf(quote.getCreatedBy()));

                        TextView title = getView().findViewById(R.id.quote);
                        title.setText(quote.getText());
                        ListView listView = getView().findViewById(R.id.tags);

                        List cutList = new ArrayList();
                        if (quote.getTagList().size() > maxTags){
                                cutList = cutTagList(quote.getTagList());
                        }
                        listView.setAdapter(new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_list_item_1, quote.getTagList()){
                            @NonNull
                            @Override
                            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                                View rowView = convertView;
                                if (rowView == null){
                                    rowView = LinearLayout.inflate(getContext(), R.layout.tag_list_layout, null);
                                }
                                TextView tag = rowView.findViewById(R.id.tag);
                                tag.setText(quote.getTagList().get(position));
                                CardView cardView = rowView.findViewById(R.id.cardView);
                                cardView.setCardBackgroundColor(Color.parseColor(tagsColor));
                                return rowView;
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<Quote> call, Throwable t) {

                    }
                });
        mCurrentPosition = id;

    }

    private List<String> cutTagList(List<String> list){
        if (list.size() > maxTags){
            while (list.size() > maxTags){
                list.remove(list.size()-1);
            }
        }
       return list;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_ID, mCurrentPosition);
    }
}
