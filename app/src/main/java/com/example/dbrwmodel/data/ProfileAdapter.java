package com.example.dbrwmodel.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbrwmodel.R;

import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    ArrayList<Profile> profiles;



    public ProfileAdapter(Context context, ArrayList<Profile> profiles) {
        this.inflater = LayoutInflater.from(context);
        this.profiles = profiles;
    }

    @NonNull
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag((long)profiles.get(position)._id);
        holder.textViewSname.setText(profiles.get(position).sName.toString());
        holder.textViewName.setText(profiles.get(position).name.toString());
        holder.textViewAge.setText(String.valueOf(profiles.get(position).age));
        int res = profiles.get(position).photo;
        holder.imageViewPhoto.setImageResource(R.drawable.ic_launcher_background);//profiles.get(position).photo);
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textViewSname, textViewName, textViewAge;
        final ImageView imageViewPhoto;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSname = itemView.findViewById(R.id.profileSname);
            textViewName = itemView.findViewById(R.id.profileName);
            textViewAge = itemView.findViewById(R.id.profileAge);
            imageViewPhoto = itemView.findViewById(R.id.profilePhoto);
        }
    }
}
