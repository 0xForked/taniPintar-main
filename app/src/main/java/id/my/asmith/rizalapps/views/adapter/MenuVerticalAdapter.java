package id.my.asmith.rizalapps.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import id.my.asmith.rizalapps.R;

/**
 * Created by Asmith on 9/20/2017.
 */

    //Custom gridView adapter
public class MenuVerticalAdapter extends BaseAdapter {
        private Context mContext;
        private final String[] string;
        private final int[] Imageid;

        public MenuVerticalAdapter(Context context,String[] string,int[] Imageid ) {
            mContext = context;
            this.Imageid = Imageid;
            this.string = string;
        }

        @Override
        public int getCount() {
            return string.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View grid;
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {

                grid = new View(mContext);
                grid = inflater.inflate(R.layout.main_content_menu_vertical, null);
                TextView textView = (TextView) grid.findViewById(R.id.gridview_text);
                ImageView imageView = (ImageView)grid.findViewById(R.id.gridview_image);
                textView.setText(string[position]);
                imageView.setImageResource(Imageid[position]);
            } else {
                grid = (View) convertView;
            }

            return grid;
        }
    }

