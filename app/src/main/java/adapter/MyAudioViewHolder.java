package adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bikram.apitest.R;

import model.Track;

/**
 * Created by bikram on 1/26/16.
 */
//public class MyAudioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
public class MyAudioViewHolder extends RecyclerView.ViewHolder {

    private static TextView mAudioTitle;

    private static ImageView mPicture;
    private static TextView mAuthor;

    public MyAudioViewHolder(View itemView) {
        super(itemView);
//        mPicture = (ImageView) itemView.findViewById(R.id.icon_recycle);
        mAudioTitle = (TextView) itemView.findViewById(R.id.track_title);
//        mAuthor = (TextView) itemView.findViewById(R.id.secondLine);
//        itemView.setOnClickListener(this);


    }

    public static TextView getTextViewAudioTitle() {
        return mAudioTitle;
    }

    public static ImageView getPicture() {
        return mPicture;
    }
    public static TextView getAuthorViewName() {
        return mAuthor;
    }

//    @Override
//    public void onClick(View v) {
//
//        Track t=new Track();
//        Log.d("clicked", t.getTrackURL());
//
//    }
}
