package com.example.mhike;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.mhike.models.Hike;
import java.util.ArrayList;

public class HikeAdapter extends ArrayAdapter<Hike> {
    private Activity context;
    private ArrayList<Hike> hikes;

    public HikeAdapter(Activity context, ArrayList<Hike> hikes) {
        super(context, R.layout.item_hike, hikes);
        this.context = context;
        this.hikes = hikes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.item_hike, null);
            holder = new ViewHolder();
            holder.txtName = row.findViewById(R.id.txtHikeName);
            holder.txtLocation = row.findViewById(R.id.txtHikeLocation);
            holder.txtDate = row.findViewById(R.id.txtHikeDate);
            holder.txtLength = row.findViewById(R.id.txtHikeLength);
            row.setTag(holder);
        } else { holder = (ViewHolder) row.getTag(); }

        Hike hike = hikes.get(position);
        holder.txtName.setText(hike.getName());
        holder.txtLocation.setText("Location: " + hike.getLocation());
        holder.txtDate.setText("Date: " + hike.getDate());
        holder.txtLength.setText("Length: " + hike.getLength() + " km");
        return row;
    }
    static class ViewHolder { TextView txtName, txtLocation, txtDate, txtLength; }
}