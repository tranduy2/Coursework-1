package com.example.mhike;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mhike.models.Observation;

import java.util.ArrayList;

public class ObservationAdapter extends ArrayAdapter<Observation> {

    Activity context;
    ArrayList<Observation> list;

    public ObservationAdapter(Activity context, ArrayList<Observation> list) {
        super(context, R.layout.item_observation, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.item_observation, null);

            holder = new ViewHolder();
            holder.txtNote = row.findViewById(R.id.txtNote);
            holder.txtTime = row.findViewById(R.id.txtTime);
            holder.txtComment = row.findViewById(R.id.txtComment);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Observation obs = list.get(position);

        holder.txtNote.setText("Note: " + obs.getNote());
        holder.txtTime.setText("Time: " + obs.getTime());
        holder.txtComment.setText("Comment: " + obs.getComment());

        return row;
    }

    static class ViewHolder {
        TextView txtNote, txtTime, txtComment;
    }
}
