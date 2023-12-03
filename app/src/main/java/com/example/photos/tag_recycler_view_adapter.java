package com.example.photos;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class tag_recycler_view_adapter extends RecyclerView.Adapter<tag_recycler_view_adapter.MyViewHolder>{

    Context context;
    List<String> allTagsList;

    private final tag_recycler_view_interface tagRecyclerViewInterface;
    public tag_recycler_view_adapter(Context context, List<String> allTagsList, tag_recycler_view_interface tagRecyclerViewInterface){
        this.allTagsList = allTagsList;
        this.context = context;
        this.tagRecyclerViewInterface = tagRecyclerViewInterface;
    }

    @NonNull
    @Override
    public tag_recycler_view_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the layout (give look to our rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tag_recycler_view_row, parent, false);
        return new tag_recycler_view_adapter.MyViewHolder(view, tagRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull tag_recycler_view_adapter.MyViewHolder holder, int position) {
        //assign values to views we created in the recycler view based on position of recycler view
        String[] split = allTagsList.get(position).split("=", 2);
        holder.tagType.setText(split[0]);
        holder.tagValue.setText(split[1]);
    }

    @Override
    public int getItemCount() {
        //recycler view gets how many total items want to be displayed
        return allTagsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //grabs views from tag_recycler_view_row file
        //similar to onCreate

        TextView tagType;
        TextView tagValue;
        public MyViewHolder(@NonNull View itemView, tag_recycler_view_interface tagRecyclerViewInterface) {
            super(itemView);
            tagType = itemView.findViewById(R.id.tagTypeTextView);
            tagValue = itemView.findViewById(R.id.tagValueTextView);
            ImageView deleteTagButton = itemView.findViewById(R.id.deleteTagButton);

            deleteTagButton.setOnClickListener(view -> {
                if (tagRecyclerViewInterface != null) {
                    int pos = getAdapterPosition();

                    if (pos!=RecyclerView.NO_POSITION){
                        tagRecyclerViewInterface.onDeleteTagClick(pos);
                    }
                }
            });

        }
    }


    public void notifyOfAdd() {
        notifyItemInserted(allTagsList.size() - 1); // Notify adapter about the data change
    }

    public void notifyOfRemoval(int pos) {
        notifyItemRemoved(pos);
    }

}
