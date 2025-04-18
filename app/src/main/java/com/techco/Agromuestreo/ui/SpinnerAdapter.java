package com.techco.Agromuestreo.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.techco.Agromuestreo.R;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<SpinnerItem> {
    private Context context;
    private List<SpinnerItem> items;

    public SpinnerAdapter(Context context, List<SpinnerItem> items) {
        super(context, R.layout.spinner_item, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
        }

        SpinnerItem currentItem = items.get(position);

        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView textView = convertView.findViewById(R.id.textView);

        imageView.setImageResource(currentItem.getImageResId());
        textView.setText(currentItem.getText());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
