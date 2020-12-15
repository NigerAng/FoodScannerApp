package com.example.niger.nigerangyidong_150271176_co3320;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FoodArrayAdapter extends ArrayAdapter<String[]> {

    private List<String[]> foodList = new ArrayList<>();

    static class FoodViewHolder{
        TextView Type;
        TextView Purpose;
        TextView Name;
    }
    public FoodArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    //method to add objects into foodList array
    public void add(String[] object){
        foodList.add(object);
        super.add(object);
    }

    //Find the size of the array
    @Override
    public int getCount(){
        return this.foodList.size();
    }

    //find item based on position in the array
    @Override
    public String[] getItem(int position){
        return this.foodList.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View group = convertView;
        FoodViewHolder viewHolder;
        if(group == null){
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            group = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder = new FoodViewHolder();
            viewHolder.Type = group.findViewById(R.id.type);
            viewHolder.Purpose= group.findViewById(R.id.purpose);
            viewHolder.Name= group.findViewById(R.id.name);
            group.setTag(viewHolder);
        }else{
            viewHolder = (FoodViewHolder) group.getTag();
        }

        String[] stat = getItem(position);
        viewHolder.Type.setText(stat[0]);
        viewHolder.Purpose.setText(stat[1]);
        viewHolder.Name.setText(stat[2]);
        return group;
    }
}
