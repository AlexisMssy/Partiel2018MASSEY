package org.diiage.amassey.partiel2018massey.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.diiage.amassey.partiel2018massey.Models.Release;
import org.diiage.amassey.partiel2018massey.R;
import org.diiage.amassey.partiel2018massey.ViewHolder.ReleaseViewHolder;

import java.util.ArrayList;

public class ReleaseAdapter extends BaseAdapter {

    Activity context;
    ArrayList<Release> releases;
    ReleaseViewHolder releaseViewHolder;

    public ReleaseAdapter(Activity context, ArrayList<Release> releases) {
        this.context = context;
        this.releases = releases;
    }

    @Override
    public int getCount() {
        return releases.size();
    }

    @Override
    public Object getItem(int i) {
        return releases.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v;

        if(view != null){
            v = view;
            releaseViewHolder = (ReleaseViewHolder) view.getTag();
        }else {
            LayoutInflater layoutInflater = this.context.getLayoutInflater();

            v = layoutInflater.inflate(R.layout.item_release, viewGroup, false);

            TextView lblTitle = v.findViewById(R.id.textViewTitle);
            TextView lblFormat = v.findViewById(R.id.textViewFormat);
            TextView lblYear = v.findViewById(R.id.textViewYear);
            TextView lblStatus = v.findViewById(R.id.textViewStatus);
            TextView lblArtist = v.findViewById(R.id.textViewArtist);

            releaseViewHolder = new ReleaseViewHolder(lblTitle, lblFormat, lblYear, lblStatus, lblArtist);

            v.setTag(releaseViewHolder);
        }

        Release release = releases.get(position);

        releaseViewHolder.Title.setText(release.getTitle());
        releaseViewHolder.Format.setText(release.getFormat());
        releaseViewHolder.Year.setText(String.valueOf(release.getYear()));
        releaseViewHolder.Status.setText(release.getStatus());
        releaseViewHolder.Artist.setText(release.getArtist());

        return v;
    }
}
