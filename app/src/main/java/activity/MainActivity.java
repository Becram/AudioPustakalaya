package activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

import com.example.bikram.apitest.R;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

import adapter.RecyclerAdaper;
import api.PustakalayaApiInterface;
import model.AllAudioBooks;
import model.RecycleItem;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class MainActivity extends AppCompatActivity implements Callback<AllAudioBooks>{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    protected Button button_get;
    protected String API="http://www.pustakalaya.org";
    protected ListView list;
    protected PustakalayaApiInterface APIInterface;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.pustakalaya.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIInterface=retrofit.create(PustakalayaApiInterface.class);
        Call<AllAudioBooks> call = APIInterface.getAllAudioBooks();
        //asynchronous call
        call.enqueue(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.activity_main_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }







            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.menu_main, menu);
                return true;
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                return true;
            }


    @Override
    public void onResponse(Response<AllAudioBooks> response, Retrofit retrofit) {

//        ArrayList<String> arrayAdapter = null;
//        int NoOfBook=response.body().getContent().size();
//        for (int i=0;i<NoOfBook;i++){
//
//            arrayAdapter.add(response.body().getContent().get(i).getBookCover());
//
//        Content Tets=response.body().getContent().get(i);
//        Log.d("body",Tets.getBookCover()+" "+Tets.getPid() +"_ "+String.valueOf(i+1));
////        Log.d("body",);
////            List<Content> arrayAdapter=new ArrayList<>();
//            mAdapter=new RecyclerAdaper(arrayAdapter);
//
//
//            mRecyclerView.setAdapter(mAdapter);

//        }

        mAdapter = new RecyclerAdaper(getApplicationContext(),createList(response));
        mRecyclerView.clearFocus();
        mRecyclerView.setAdapter(mAdapter);

//        RecyclerAdaper adapter = (RecyclerAdaper) mRecyclerView.getAdapter();
        Log.d("snkbdj", ToStringBuilder.reflectionToString(response));
//        adapter.notifyDataSetChanged();



    }

    @Override
    public void onFailure(Throwable t) {

        Log.d("body","Failed");


    }

    private List<RecycleItem> createList(Response<AllAudioBooks> t) {

        List<RecycleItem> result = new ArrayList<>();
        for (int i = 0; i < t.body().getContent().size(); i++) {
            RecycleItem ci = new RecycleItem();
            ci.title = t.body().getContent().get(i).getTitle();
            ci.image=t.body().getContent().get(i).getCover();
            ci.Author=t.body().getContent().get(i).getAuthor();
            ci.id=t.body().getContent().get(i).getId();
            Log.d("get count", String.valueOf(t.body().getCount())) ;


            result.add(ci);

        }

        return result;


    }
}
