package com.ironfake.quotes;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.ironfake.quotes.model.Quote;
import com.ironfake.quotes.network.NetworkService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuotesListFragment extends ListFragment {
    OnQuotesSelectedListener mCallback;

    public interface OnQuotesSelectedListener{
        public void onQuotesSelected(int id);
    }

    private List<Quote> quotes = new ArrayList<>();
    private ArrayAdapter adapter;
    private final int limit = 10;
    private int offset = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getListQuotes();
        adapter = new ArrayAdapter<Quote>(getActivity(),
                android.R.layout.simple_list_item_activated_1, quotes){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                Quote quote = getItem(position);
                tv.setText(quote.getText());
                if (quote.getCreatedBy() == 0)
                    tv.setGravity(Gravity.END);
                else
                    tv.setGravity(Gravity.START);
                return tv;
            }
        };
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        if (getListView().getFooterViewsCount() == 0){
            View footer = ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.listview_footer, null, false);
            footer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getListQuotes();
                }
            });
            getListView().addFooterView(footer, null, false);
        }
        setListAdapter(adapter);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getFragmentManager().findFragmentById(R.id.quote_fragment) != null){
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnQuotesSelectedListener) getActivity();
        }catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnQuotesSelectedListener");
        }
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {

        mCallback.onQuotesSelected(quotes.get(position).getId());
        getListView().setItemChecked(position, true);
    }

    private void getListQuotes(){
        NetworkService.getInstance()
                .getOIKOApi()
                .getQuotes(limit, offset)
                .enqueue(new Callback<List<Quote>>() {
                    @Override
                    public void onResponse(Call<List<Quote>> call, Response<List<Quote>> response) {

                        adapter.setNotifyOnChange(false);
                        if (response.body() != null){
                            adapter.addAll(response.body());
                        }
                        adapter.setNotifyOnChange(true);
                        adapter.notifyDataSetChanged();
                        offset += limit;
                    }

                    @Override
                    public void onFailure(Call<List<Quote>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }
}
































