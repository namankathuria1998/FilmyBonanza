package src.com.filmybonanza.views;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import src.com.filmybonanza.R;
import src.com.filmybonanza.activities.ShowEventDetails;
import src.com.filmybonanza.activities.Ticket;
import src.com.filmybonanza.model.BookedEvent;
import src.com.filmybonanza.singleton.DependencyInjection;

public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.viewholder> {

   ArrayList<BookedEvent> bookedEventArrayList;
    Context context;

    public BookingHistoryAdapter(ArrayList<BookedEvent> arrayList, Context context) {
        this.bookedEventArrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override

    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.bookinghistorylayout,parent,false);
        return new  viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        BookedEvent obj = bookedEventArrayList.get(position);
        holder.title.setText(obj.getTitle());
        Picasso.get().load(obj.getPoster()).into(holder.iv);
        holder.date.setText(obj.getDateOfbooking());
        holder.time.setText(obj.getTimeOfBooking());
    }

    @Override
    public int getItemCount() {
        return bookedEventArrayList.size();
    }

    class viewholder extends RecyclerView.ViewHolder
    {
        ImageView iv;
        TextView date , time ,title;

        public viewholder(View itemView) {
            super(itemView);

            iv = itemView.findViewById(R.id.bookimage);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.bookeddate);
            time = itemView.findViewById(R.id.bookedtime);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    BookedEvent bookedEvent= bookedEventArrayList.get(getAdapterPosition());
                    String json = DependencyInjection.getGson().toJson(bookedEvent);
                    Intent intent1 = new Intent(context, Ticket.class);

                    ShowEventDetails obj=new ShowEventDetails();
                    EditText noOfTickets = obj.enterticketdetails.findViewById(R.id.nooftickets);
                    String ntickets= String.valueOf(200*Integer.valueOf(noOfTickets.getText().toString()));
                    intent1.putExtra("fee" , ntickets);
                    intent1.putExtra("SeatsSelected" , obj.seatsSelected);

                    intent1.putExtra("key" , json);
                    context.startActivity(intent1);
                }
            });

        }
    }
}
