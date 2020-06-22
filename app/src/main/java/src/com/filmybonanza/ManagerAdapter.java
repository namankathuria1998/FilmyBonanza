package src.com.filmybonanza;

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
import src.com.filmybonanza.activities.ManagerActivity;
import src.com.filmybonanza.dynamodbClient.DynamodbClient;

public class ManagerAdapter extends  RecyclerView.Adapter<ManagerAdapter.viewholder>{

    ArrayList<Event> arrayList;
    Context context;


    public ManagerAdapter(ArrayList<Event> arrayList, Context context) {
        this.arrayList = arrayList;
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
        Event obj=arrayList.get(position);
        Picasso.get().load(obj.getPoster()).into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
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

                    DeleteItemRequest deleteItemRequest=new DeleteItemRequest();
                    deleteItemRequest.setTableName("Events");

                    // deleting from list of upcoming movies
                    Map<String, AttributeValue> map=new HashMap<>();
                    map.put("eventId" , new AttributeValue(arrayList.get(getAdapterPosition()).getEventId()));
                    deleteItemRequest.setKey(map);
                    DynamodbClient.getClient().deleteItem(deleteItemRequest);

                    // Deleting from booking history
                    DeleteItemRequest deleteItemRequest1 =new DeleteItemRequest();
                    deleteItemRequest.setTableName("UsersBookings");
                    Map<String,AttributeValue>mymap=new HashMap<>();
                    mymap.put("eventId", new AttributeValue(arrayList.get(getAdapterPosition()).getEventId()));
                    deleteItemRequest.setKey(mymap);
                    DynamodbClient.getClient().deleteItem(deleteItemRequest);

                    arrayList.remove(getAdapterPosition());
                    ManagerActivity.getAdapter().notifyDataSetChanged();


                }
            });

        }

    }
}
