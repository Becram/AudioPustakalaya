package activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bikram.apitest.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import adapter.AudioListAdapter;
import adapter.RecyclerAdaper;
import api.PustakalayaApiInterface;
import helper.ItemClickSupport;
import model.AllAudioBooks;
import model.ModelAudioBookDetails;
import model.RecycleItem;
import model.Track;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by bikram on 1/25/16.
 */
public class AudioPlayFragment extends Fragment implements Callback<ModelAudioBookDetails>{

    private List<Track> mListItems;
    private AudioListAdapter mAdapter;
    private PustakalayaApiInterface APIInterface;
     public ListView listView;
    protected String BASE_URL="http://www.pustakalaya.org";

    private TextView mSelectedTrackTitle;


    private MediaPlayer mMediaPlayer;
    private ImageView mPlayerControl;
    private RecyclerView audioRecyclerView;
    private LinearLayoutManager mLayoutManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.tab_play_details, container, false);


        audioRecyclerView = (RecyclerView) v.findViewById(R.id.track_recycler_view);
        audioRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        audioRecyclerView.setLayoutManager(mLayoutManager);




//        listView = (ListView)v.findViewById(R.id.track_list_view);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
//        {
//            @Override public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
//            {
//                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
//            }
//        });






        return  v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mListItems = new ArrayList<Track>();
//
//        mAdapter = new AudioListAdapter(getContext(), mListItems);
//        listView.setAdapter(mAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.pustakalaya.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIInterface=retrofit.create(PustakalayaApiInterface.class);
        Call<ModelAudioBookDetails> call = APIInterface.getAudioBooksDetails(AudioDetails.bookid);

        call.enqueue(this);



        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                togglePlayPause();
            }
        });





    }

    private void togglePlayPause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
//            mPlayerControl.setImageResource(R.drawable.play);
        } else {
            mMediaPlayer.start();
//            mPlayerControl.setImageResource(R.drawable.pause44);
        }
    }

    @Override
    public void onResponse(final Response<ModelAudioBookDetails> response, Retrofit retrofit) {
//        loadTracks(response);

        mAdapter = new AudioListAdapter(getContext(),createList(response));
        audioRecyclerView.clearFocus();
        audioRecyclerView.setAdapter(mAdapter);
        ItemClickSupport.addTo(audioRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                Log.d("item", String.valueOf(position));
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                    mMediaPlayer.reset();
                }

                try {


//                    URI audio_url = URI.parse(BASE_URL + createList(response).get(position).getTrackURL());
//                    Uri uri  = Uri.parse(BASE_URL + createList(response).get(position).getTrackURL());


                    String audio_url = URLEncoder.encode(BASE_URL + createList(response).get(position).getTrackURL(), "UTF-8");
//                    mMediaPlayer.setDataSource("http://www.pustakalaya.org/sound/4%20Tips%20for%20Teaching%20listening%20and%20speaking_B%204.mp3");
//                    MediaPlayer player = MediaPlayer.create(this, BASE_URL + createList(response).get(position).getTrackURL()));


                    mMediaPlayer.setDataSource(audio_url);
                    Log.d("track url", audio_url);
                    mMediaPlayer.prepareAsync();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



        mAdapter.notifyDataSetChanged();




    }

    @Override
    public void onFailure(Throwable t) {

    }


    private List<Track> createList(Response<ModelAudioBookDetails> t) {

        List<Track> result = new ArrayList<>();
        for (int i = 0; i <  t.body().getContent().getChapters().size(); i++) {
            Track ci = new Track();
            ci.track_title = t.body().getContent().getChapters().get(i).getChapter();
            ci.track_url=t.body().getContent().getChapters().get(i).getFile();
            Log.d("get count", String.valueOf(t.body().getContent().getTitle())) ;


            result.add(ci);

        }

        return result;


    }



}
