package activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.bikram.apitest.R;

import java.util.ArrayList;
import java.util.List;

import adapter.AudioListAdapter;
import api.PustakalayaApiInterface;
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.tab_play_details, container, false);
        listView = (ListView)v.findViewById(R.id.track_list_view);



        return  v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListItems = new ArrayList<Track>();

        mAdapter = new AudioListAdapter(getContext(), mListItems);
        listView.setAdapter(mAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.pustakalaya.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIInterface=retrofit.create(PustakalayaApiInterface.class);
        Call<ModelAudioBookDetails> call = APIInterface.getAudioBooksDetails(AudioDetails.bookid);

        call.enqueue(this);

    }

    @Override
    public void onResponse(Response<ModelAudioBookDetails> response, Retrofit retrofit) {
//        loadTracks(response);
        mListItems.clear();
        mListItems.addAll(createList(response));
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onFailure(Throwable t) {

    }

    private List<Track> createList(Response<ModelAudioBookDetails> t) {

        List<Track> result = new ArrayList<>();
        for (int i = 0; i < t.body().getContent().getChapters().size(); i++) {
            Track ci = new Track();
            ci.track_title = t.body().getContent().getChapters().get(i).getChapter();
//            ci.image=t.body().getContent().get(i).getCover();
//            ci.Author=t.body().getContent().get(i).getAuthor();
//            ci.id=t.body().getContent().get(i).getId();



            result.add(ci);

        }
        Log.d("track count", String.valueOf(t.body().getContent().getChapters().size())) ;


        return result;


    }
}
