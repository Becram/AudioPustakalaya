package activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
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
import android.widget.SeekBar;
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
public class AudioPlayFragment extends Fragment implements Callback<ModelAudioBookDetails>, SeekBar.OnSeekBarChangeListener{

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
    private ImageView mSelectedTrackImage;
    private TextView mSelectedTrackChapter;
    private TextView mSelectedTrackStatus;
    private SeekBar mSeekBar;
    Handler seekHandler = new Handler();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.tab_play_details, container, false);

        mSelectedTrackTitle = (TextView) v.findViewById(R.id.selected_track_title);
        mSelectedTrackChapter = (TextView) v.findViewById(R.id.selected_chapter);
        mSelectedTrackStatus = (TextView) v.findViewById(R.id.selected_playpause_status);
//        mSelectedTrackTitle = (TextView) v.findViewById(R.id.selected_track_title);
        mSelectedTrackImage = (ImageView) v.findViewById(R.id.selected_track_image);
        mPlayerControl = (ImageView) v.findViewById(R.id.player_control);
        mSeekBar= (SeekBar) v.findViewById(R.id.seekBar);

        audioRecyclerView = (RecyclerView) v.findViewById(R.id.track_recycler_view);
        audioRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        audioRecyclerView.setLayoutManager(mLayoutManager);
//        mSeekBar.setMax(mMediaPlayer.getDuration());
        mSeekBar.setOnSeekBarChangeListener(this);





        return  v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
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
        APIInterface = retrofit.create(PustakalayaApiInterface.class);
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
        mPlayerControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePlayPause();

            }
        });


        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlayerControl.setImageResource(R.drawable.button_reload);
            }
        });
        int startPosition = 0;
        int total = mMediaPlayer.getDuration();
        Log.d("track", String.valueOf(total));


    }

    Runnable run = new Runnable() {
        @Override
        public void run() {
            SeekUpdation();
        }


    };

    public void SeekUpdation() {

        seekHandler.removeCallbacks(run);
        if (mMediaPlayer != null)
            mSeekBar.setMax(mMediaPlayer.getDuration());
            mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());

        Log.d("max", String.valueOf(mMediaPlayer.getDuration()));

        seekHandler.postDelayed(run, 1000);
    }

    private void togglePlayPause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mPlayerControl.setImageResource(R.drawable.play_new);
//            mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
            mSelectedTrackStatus.setText(R.string.paused);

        } else {
            mMediaPlayer.start();
            SeekUpdation();
//            mSeekBar.setMax(mMediaPlayer.getDuration());
            mPlayerControl.setImageResource(R.drawable.pause_new);
            mSelectedTrackStatus.setText(R.string.playing);
        }
    }

    @Override
    public void onResponse(final Response<ModelAudioBookDetails> response, Retrofit retrofit) {
        mListItems=createList(response);

        mAdapter = new AudioListAdapter(getContext(),createList(response));
        audioRecyclerView.clearFocus();
        audioRecyclerView.setAdapter(mAdapter);
        ItemClickSupport.addTo(audioRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                Track track = mListItems.get(position);
                mSelectedTrackTitle.setText(response.body().getContent().getTitle());
                mSelectedTrackChapter.setText(track.getTitle());

                Picasso.with(getContext()).load(BASE_URL + response.body().getContent().getImage()).into(mSelectedTrackImage);

                Log.d("item", String.valueOf(position));
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                    mMediaPlayer.reset();
                }

                try {

                    String audio_url = BASE_URL + createList(response).get(position).getTrackURL();
                    String fixedUrl = audio_url.replaceAll("\\s", "%20");
//

                    mMediaPlayer.setDataSource(fixedUrl);
                    Log.d("track url", fixedUrl);
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

        Log.d("API failed","failed to fetch data");


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


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser) {
            mMediaPlayer.seekTo(progress);
//            mSeekBar.setMax(mMediaPlayer.getDuration());
            mSeekBar.setProgress(progress);
        }
        Log.d("Seek progeress ", String.valueOf(progress));


    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
