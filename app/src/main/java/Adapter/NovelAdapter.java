package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.btlandroid.Novel;
import com.example.btlandroid.R;

import java.util.List;

public class NovelAdapter extends ArrayAdapter<Novel> {
    public NovelAdapter(Context context, int layout, List<Novel> novelList) {
        super(context, layout, novelList);
    }
    private class ViewHolder{
        ImageView imgHinh;
        TextView txtTen, txtTG;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.novel_lists, parent, false);
            holder = new ViewHolder();
            holder.imgHinh = convertView.findViewById(R.id.Illustration);
            holder.txtTen = convertView.findViewById(R.id.novelName);
            holder.txtTG = convertView.findViewById(R.id.novelAuthor);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Novel novel = getItem(position);
        holder.txtTen.setText(novel.getNovelName());
        holder.txtTG.setText(novel.getNovelAuthor());
        holder.imgHinh.setImageResource(novel.getNovelImage());
        return convertView;
    }
}
