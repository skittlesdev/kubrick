import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.github.skittlesdev.kubrick.R;

import java.util.List;

class AppAdapter extends BaseAdapter {

    private List<ApplicationInfo> mAppList;
    private Context context;


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<ApplicationInfo> getmAppList() {
        return mAppList;
    }

    public void setmAppList(List<ApplicationInfo> mAppList) {
        this.mAppList = mAppList;
    }

    @Override
    public int getCount() {
        return mAppList.size();
    }

    @Override
    public ApplicationInfo getItem(int position) {
        return mAppList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context,
                    R.layout.item_list_app, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        ApplicationInfo item = getItem(position);
        holder.iv_icon.setImageDrawable(item.loadIcon(getPackageManager()));
        holder.tv_name.setText(item.loadLabel(getPackageManager()));
        return convertView;
    }

    class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;

        public ViewHolder(View view) {
            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(this);
        }
    }
}