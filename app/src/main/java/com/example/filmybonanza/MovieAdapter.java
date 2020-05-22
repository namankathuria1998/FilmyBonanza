package com.example.filmybonanza;


// this class will be used to populate the individual views
// of the list containing the  upcoming movies

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
//
//public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.viewholder>{
//
//    ArrayList<Event> arrayList;
//    Context context;
//
//    public MovieAdapter(ArrayList<Event> arrayList, Context context) {
//        this.arrayList = arrayList;
//        this.context = context;
//    }
//
//    @NonNull
//
//    @Override
//    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = li.inflate(R.layout.layoutforone,parent,false);
//        return new viewholder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull viewholder holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return arrayList.size();
//    }
//
//    class viewholder extends RecyclerView.ViewHolder
//    {
//
//        public viewholder(View itemView) {
//            super(itemView);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    EachObject obj = arrayList.get(getAdapterPosition());
//                    Gson gson=new Gson();
//                    String str =  gson.toJson(obj);
//                    Intent intent=new Intent(context,SingleMovie.class);
//                    intent.putExtra("key",str);
//                    context.startActivity(intent);
//                }
//            });
//
//        }
//
//
//    }
//}

