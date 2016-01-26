package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bikram.apitest.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import activity.AudioDetails;
import activity.MainActivity;
import model.RecycleItem;
import model.Track;

/**
 * Created by bikram on 1/22/16.
 */
public class AudioListAdapter extends RecyclerView.Adapter<MyAudioViewHolder> {

    protected String BASE_URL="http://www.pustakalaya.org";

    private Context myContext;
    private MainActivity mainActivity;

    private Context mContext;
    //    private ArrayList<String> mDataset;
    private List<Track> mTrackList;
    @Override
    public MyAudioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_list_row, parent, false);

        return new MyAudioViewHolder(v);
    }



    @Override
    public void onBindViewHolder(MyAudioViewHolder holder, int position) {
        final Track mTrack=mTrackList.get(position);

       MyAudioViewHolder.getTextViewAudioTitle().setText(mTrack.track_title);
//        MyViewHolder.getAuthorViewName().setText(Book.Author);


        Log.d("book id", mTrack.track_title);

//        Picasso.with(mContext).load(BASE_URL+Book.image)
//                .into(MyViewHolder.getPicture());

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(this,Book.title,Toast.LENGTH_LONG).show();
//                Log.d("clicked",mTrack.track_title);
////                Intent i=new Intent(mContext, AudioDetails.class);
////                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                i.putExtra("bookID",Book.id);
////                mContext.startActivity(i);
//            }
//        });




    }


    @Override
    public int getItemCount() {
        return mTrackList.size();

    }


    public void add(int position, String item) {

    }

    public void remove(String item) {
//        int position = mDataset.indexOf(item);
//        mDataset.remove(position);
//        notifyItemRemoved(position);
    }

    public AudioListAdapter( Context m, List<Track> BookList) {
        this.mTrackList = BookList;
        this.myContext=m;
    }

    public List<Track> getRepoList() {
        return mTrackList;
    }



}
