package id.my.asmith.rizalapps.views.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import id.my.asmith.rizalapps.holder.MenuHorizontalHolder;
import id.my.asmith.rizalapps.R;
import id.my.asmith.rizalapps.model.MenuHorizontalModel;

/**
 * Created by Asmith on 9/20/2017.
 */

public class MenuHorizontalAdapter extends
        RecyclerView.Adapter<MenuHorizontalHolder> {

    private ArrayList<MenuHorizontalModel> arrayList;
    private Context context;

    public MenuHorizontalAdapter(Context context,
                                ArrayList<MenuHorizontalModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }

    @Override
    public void onBindViewHolder(MenuHorizontalHolder holder, int position) {
        final MenuHorizontalModel model = arrayList.get(position);

        MenuHorizontalHolder mainHolder = (MenuHorizontalHolder) holder;// holder

        Bitmap image = BitmapFactory.decodeResource(context.getResources(),
                model.getImage());// This will convert drawbale image into
        // bitmap

        // setting title
        mainHolder.title.setText(model.getTitle());

        mainHolder.imageview.setImageBitmap(image);


    }


    @Override
    public MenuHorizontalHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.main_content_menu_horizontal, viewGroup, false);
        final MenuHorizontalHolder listHolder = new MenuHorizontalHolder(mainGroup);
        return listHolder;

    }

}
