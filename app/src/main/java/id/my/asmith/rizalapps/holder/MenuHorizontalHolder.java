package id.my.asmith.rizalapps.holder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.my.asmith.rizalapps.R;

/**
 * Created by Asmith on 9/20/2017.
 */

public class MenuHorizontalHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    // View holder for gridview recycler view as we used in listview
    public TextView title;
    public ImageView imageview;
    public LinearLayout mLayContent;
    public MenuHorizontalHolder(View view) {
        super(view);
        // Find all views ids
        this.title = (TextView) view
                .findViewById(R.id.title);
        this.imageview = (ImageView) view
                .findViewById(R.id.image);
        this.mLayContent = (LinearLayout) view.findViewById(R.id.lay_view);

        mLayContent.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        //Log.d("ASD", "onClick " + getPosition());

        if (getPosition() == 0) {
            Log.d("ASD", "Posisi 1 ");

        }

        if (getPosition() == 1) {

            Log.d("ASD", "Posisi 2 ");
        }

        if (getPosition() == 2) {
            Log.d("ASD", "Posisi 3 ");

        }

        if (getPosition() == 3) {
            Log.d("ASD", "Posisi 4 ");

        }

    }
}
