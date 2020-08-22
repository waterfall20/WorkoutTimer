package com.example.workouttimer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class WorkoutListAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Model> recordList;

    public WorkoutListAdapter(Context context, int layout, ArrayList<Model> recordList) {
        this.context = context;
        this.layout = layout;
        this.recordList = recordList;
    }

    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int position) {
        return recordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView name, work_time, rest_time, number, set_count, set_during;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = new ViewHolder();

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.name = row.findViewById(R.id.edtName);
            holder.work_time = row.findViewById(R.id.edtWorkoutTime);
            holder.rest_time = row.findViewById(R.id.edtRestTime);
            holder.number = row.findViewById(R.id.edtNumber);
            holder.set_count = row.findViewById(R.id.edtSetCount);
            holder.set_during = row.findViewById(R.id.edtSetDuring);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Model model = recordList.get(position);
        holder.name.setText(model.getName());
        holder.work_time.setText(model.getWorkTime()+"秒");
        holder.rest_time.setText(model.getRestTime()+"秒");
        holder.number.setText(model.getNumber()+"回");
        holder.set_count.setText(model.getSetCount()+"回");
        holder.set_during.setText(model.getSetDuring()+"秒");

        return row;
    }
}


