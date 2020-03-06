package com.ironfake.quotes.network;

import com.ironfake.quotes.model.Quote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OIKOApi {
    @GET("/test/?")
    public Call<List<Quote>> getQuotes(@Query("limit") int limit, @Query("offset") int offset);

    @GET("/test/{id}")
    public Call<Quote> getQuoteDetail(@Path("id") int id);


}
