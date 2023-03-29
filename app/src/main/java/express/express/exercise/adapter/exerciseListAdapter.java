package express.express.exercise.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.workout.exercise.R;

import java.io.IOException;
import java.io.InputStream;

public class exerciseListAdapter extends RecyclerView.Adapter<exerciseListAdapter.exerciseListAdapterViewHolder>{

    String[] elist;
    String[] imglist;
    UserClickListener listener;
    Context context;

    public exerciseListAdapter(String[] elist,String[] imglist,UserClickListener listener,Context context) {
        this.elist = elist;
        this.listener = listener;
        this.imglist=imglist;
        this.context=context;
    }

    @Override
    public exerciseListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.exe_list_item,parent,false);
        exerciseListAdapterViewHolder s=new exerciseListAdapterViewHolder(view);
        return s;
    }

    @Override
    public void onBindViewHolder(exerciseListAdapterViewHolder holder, final int position) {
        AssetManager manager = context.getAssets();
        InputStream open = null;
        try {
            open = manager.open(imglist[position]);

            Bitmap bitmap = BitmapFactory.decodeStream(open);
            holder.img.setImageBitmap(bitmap);
            holder.tv.setText(elist[position]);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onUserClick(position);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return elist.length;
    }

    public class exerciseListAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tv;
        View view;
        public exerciseListAdapterViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            img=itemView.findViewById(R.id.list_img);
            tv=itemView.findViewById(R.id.list_txt);
        }

    }

    public interface UserClickListener
    {
        public void onUserClick(int pos);
    }
}
