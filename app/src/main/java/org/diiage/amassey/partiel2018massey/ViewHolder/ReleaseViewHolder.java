package org.diiage.amassey.partiel2018massey.ViewHolder;

import android.widget.ImageView;
import android.widget.TextView;

public class ReleaseViewHolder{

    public TextView Title;
    public TextView Format;
    public TextView Year;
    public TextView Status;
    public TextView Artist;

    public ReleaseViewHolder(TextView title, TextView format, TextView year, TextView status, TextView artist) {
        Title = title;
        Format = format;
        Year = year;
        Status = status;
        Artist = artist;
    }
}