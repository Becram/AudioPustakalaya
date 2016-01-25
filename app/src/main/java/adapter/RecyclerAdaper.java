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

/**
 * Created by bikram on 1/22/16.
 */
public class RecyclerAdaper extends RecyclerView.Adapter<MyViewHolder> {

    protected String BASE_URL="http://www.pustakalaya.org";


    private MainActivity mainActivity;

    private Context mContext;
//    private ArrayList<String> mDataset;
    private List<RecycleItem> mBookList;
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_row, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final RecycleItem Book=mBookList.get(position);

        MyViewHolder.getTextViewName().setText(Book.title);
        MyViewHolder.getAuthorViewName().setText(Book.Author);


        Log.d("book id", Book.id);

        Picasso.with(mContext).load(BASE_URL+Book.image)
                .into(MyViewHolder.getPicture());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(this,Book.title,Toast.LENGTH_LONG).show();
                Log.d("clicked",Book.id);
                Intent i=new Intent(mContext, AudioDetails.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("bookID",Book.id);
                mContext.startActivity(i);
            }
        });




    }


    @Override
    public int getItemCount() {
        return mBookList.size();

    }


    public void add(int position, String item) {

    }

    public void remove(String item) {
//        int position = mDataset.indexOf(item);
//        mDataset.remove(position);
//        notifyItemRemoved(position);
    }

    public RecyclerAdaper( Context m, List<RecycleItem> BookList) {
        this.mBookList = BookList;
        this.mContext=m;
    }

    public List<RecycleItem> getRepoList() {
        return mBookList;
    }



}
