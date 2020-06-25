package src.com.filmybonanza.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import src.com.filmybonanza.R;
import src.com.filmybonanza.activities.ManagerActivity;
import src.com.filmybonanza.client.DynamodbClient;
import src.com.filmybonanza.model.Event;
import src.com.filmybonanza.singleton.DependencyInjection;

public class ManagerAdapter extends  RecyclerView.Adapter<ManagerAdapter.viewholder>{

    ArrayList<Event> adminEventsArrayList;
    Context context;


    public ManagerAdapter(ArrayList<Event> arrayList, Context context) {
        this.adminEventsArrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.managerlayoutforone,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        Event obj= adminEventsArrayList.get(position);
        Picasso.get().load(obj.getPoster()).into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return adminEventsArrayList.size();
    }

    class viewholder extends RecyclerView.ViewHolder
    {
        Button delete;
        ImageView iv;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            delete = itemView.findViewById(R.id.delete);
            iv = itemView.findViewById(R.id.myimage);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DependencyInjection.getEventHandler().deleteUpcomingMovies(adminEventsArrayList.get(getAdapterPosition()).getEventId());
                    DependencyInjection.getEventHandler().deleteMovieFromBookinghistory(adminEventsArrayList.get(getAdapterPosition()).getEventId());
                    adminEventsArrayList.remove(getAdapterPosition());
                    ManagerActivity.getAdapter().notifyDataSetChanged();
                }
            });
        }
    }
}
