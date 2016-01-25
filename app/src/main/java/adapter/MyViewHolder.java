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

    private static TextView mName;

    private static  ImageView mPicture;
    private static TextView mAuthor;

    public MyViewHolder(View itemView) {
        super(itemView);
        mPicture = (ImageView) itemView.findViewById(R.id.icon_recycle);
        mName = (TextView) itemView.findViewById(R.id.firstLine);
        mAuthor = (TextView) itemView.findViewById(R.id.secondLine);


    }

    public static TextView getTextViewName() {
        return mName;
    }

    public static ImageView getPicture() {
        return mPicture;
    }
    public static TextView getAuthorViewName() {
        return mAuthor;
    }
}
