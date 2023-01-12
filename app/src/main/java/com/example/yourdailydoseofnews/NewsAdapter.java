package com.example.yourdailydoseofnews;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter <Story> {
    public NewsAdapter( Activity context, ArrayList<Story> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate
                    (R.layout.list_item,parent,false);
        }

        Story current_story =  getItem(position);

        String current_title = current_story.mTitle;
        TextView title = convertView.findViewById(R.id.title);
        title.setText(current_title);

        String current_section = current_story.mSection;
        TextView section = convertView.findViewById(R.id.section);
        section.setText(current_section);

        String current_date = current_story.mDate;
        TextView date = convertView.findViewById(R.id.date);
        date.setText(current_date);

        String current_author = current_story.mAuthor;
        TextView author = convertView.findViewById(R.id.author);
        author.setText(current_author);

        return convertView;
    }

}
