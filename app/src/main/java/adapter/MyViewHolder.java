package adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bikram.apitest.R;

/**
 * Created by bikram on 1/22/16.
 */
public class MyViewHolder extends RecyclerView.ViewHolder{

    private  TextView mName;

    private   ImageView mPicture;
    private  TextView mAuthor;

    public MyViewHolder(View itemView) {
        super(itemView);
        mPicture = (ImageView) itemView.findViewById(R.id.icon_recycle);
        mName = (TextView) itemView.findViewById(R.id.firstLine);
        mAuthor = (TextView) itemView.findViewById(R.id.secondLine);


    }

    public  TextView getTextViewName() {
        return mName;
    }

    public  ImageView getPicture() {
        return mPicture;
    }
    public  TextView getAuthorViewName() {
        return mAuthor;
    }
}
