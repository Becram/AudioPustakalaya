package activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bikram.apitest.R;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
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
    private List<RecycleItem> result;
    private static String sort;
    private ProgressBar progress;
    private Button download;
    private ThinDownloadManager downloadManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mRecyclerView = (RecyclerView) findViewById(R.id.activity_main_recycler_view);
        progress= (ProgressBar)findViewById(R.id.progress);
        download= (Button) findViewById(R.id.download);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Retrofit_Reloader("date");
        progress.setVisibility(View.VISIBLE);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(),"press",Toast.LENGTH_LONG).show();
                Download();

            }
        });





    }
     public void Download(){
         Uri downloadUri = Uri.parse("http://pustakalaya.org/sound/kermit_ruffins-_breakfast_lunch__dinner.mp3");
         Uri destinationUri = Uri.parse(Environment.DIRECTORY_DOWNLOADS);
         Log.d("cool",Environment.DIRECTORY_DOWNLOADS);
         DownloadRequest downloadRequest = new DownloadRequest(downloadUri)

                 .setRetryPolicy(new DefaultRetryPolicy())
                 .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)

                 .setDownloadListener(new DownloadStatusListener() {
                     @Override
                     public void onDownloadComplete(int id) {
                         Toast.makeText(getApplication(), "completed", Toast.LENGTH_LONG).show();


                     }

                     @Override
                     public void onDownloadFailed(int id, int errorCode, String errorMessage) {
                         Toast.makeText(getApplication(), "download failed", Toast.LENGTH_LONG).show();


                     }

                     @Override
                     public void onProgress(int id, long totalBytes, long downlaodedBytes, int progress)
                     {
                         Toast.makeText(getApplication(), "download progress", Toast.LENGTH_LONG).show();
                     }
                 });


         downloadManager = new ThinDownloadManager();
         int downloadId = downloadManager.add(downloadRequest);


     }






    public void Retrofit_Reloader(String s){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.pustakalaya.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIInterface = retrofit.create(PustakalayaApiInterface.class);
//        Log.d("sort",sort);
        Call<AllAudioBooks> call = APIInterface.getAllAudioBooks(s);
        //asynchronous call
        call.enqueue(this);


    }




            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.menu_main, menu);
                return true;
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {



                switch (item.getItemId()) {
                    case R.id.menu_load_author:
                        sort="author";
                        Retrofit_Reloader(sort);

                        return true;
                    case R.id.menu_load_by_title:
                        sort="title";
                        Retrofit_Reloader(sort);
                        return true;
                    case R.id.menu_load_date:
                        sort="date";
                        Retrofit_Reloader(sort);
                        return true;
                    default:
                        return super.onOptionsItemSelected(item);
                }

            }


    @Override
    public void onResponse(final Response<AllAudioBooks> response, Retrofit retrofit) {
        progress.setVisibility(View.GONE);

//        Log.d("size of book list", String.valueOf(response.body().getContent().size()));
//        Log.d("size of book list", String.valueOf(response.body().getContent().get(109).getTitle()));




          createList(response);
          mAdapter = new RecyclerAdaper(result,mRecyclerView,getApplication());
          mRecyclerView.clearFocus();
          mRecyclerView.setAdapter(mAdapter);


//        RecyclerAdaper adapter = (RecyclerAdaper) mRecyclerView.getAdapter();
        Log.d("snkbdj", ToStringBuilder.reflectionToString(response));
       mAdapter.notifyDataSetChanged();

        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {


            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {


                Log.d("Cool", response.body().getContent().get(position).getTitle());

                Intent i = new Intent(getApplicationContext(), AudioDetails.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("bookID", response.body().getContent().get(position).getId());
                Log.d("book id",response.body().getContent().get(position).getId());
                startActivity(i);


            }

        });



    }

    @Override
    public void onFailure(Throwable t) {

        Log.d("body","Failed");


    }



    private void createList(Response<AllAudioBooks> t) {

        result = new ArrayList<>();
        for (int i = 0; i < t.body().getContent().size(); i++) {
            RecycleItem ci = new RecycleItem();
            ci.title = t.body().getContent().get(i).getTitle();
            ci.image=t.body().getContent().get(i).getCover();
            ci.Author=t.body().getContent().get(i).getAuthor();
            ci.id=t.body().getContent().get(i).getId();
            Log.d("get count", String.valueOf(t.body().getCount())) ;


            result.add(ci);

        }

//        return result;


    }
}
