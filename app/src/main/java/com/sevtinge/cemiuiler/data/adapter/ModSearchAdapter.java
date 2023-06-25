package com.sevtinge.cemiuiler.data.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.sevtinge.cemiuiler.R;
import com.sevtinge.cemiuiler.data.ModData;
import com.sevtinge.cemiuiler.utils.Helpers;
import com.sevtinge.cemiuiler.utils.SearchHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

import moralnorm.recyclerview.widget.RecyclerView;

public class ModSearchAdapter extends RecyclerView.Adapter<ModSearchAdapter.ViewHolder> implements Filterable {

    private Context mContext;
    private String filterString = "";
    private ItemFilter mFilter;
    private onItemClickListener mItemClickListener;//item点击监听
    private final CopyOnWriteArrayList<ModData> modsList = new CopyOnWriteArrayList<ModData>();

    public void setOnItemClickListener(onItemClickListener onItemClick) {
        mItemClickListener = onItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        mContext = viewGroup.getContext();
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_search_result, viewGroup,false);
        //创建一个VIewHolder
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ModData ad = modsList.get(position);

        int start = ad.title.toLowerCase().indexOf(filterString);
        if (start >= 0) {
            Spannable spannable = new SpannableString(ad.title);
            spannable.setSpan(new ForegroundColorSpan(SearchHelper.markColorVibrant), start, start + filterString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.mName.setText(spannable, TextView.BufferType.SPANNABLE);
        } else {
            viewHolder.mName.setText(ad.title);
        }
        viewHolder.mPackageName.setText(ad.breadcrumbs);
        //设置item点击监听事件
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClick(view, ad);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modsList.size();
    }

    @Override
    public Filter getFilter() {
        // 如果ItemFilter对象为空，那么重写创建一个
        if (mFilter == null) {
            mFilter = new ItemFilter();
        }
        return mFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIcon;
        private TextView mName;
        private TextView mPackageName;

        public ViewHolder(View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(android.R.id.icon);
            mName = itemView.findViewById(android.R.id.title);
            mPackageName = itemView.findViewById(android.R.id.summary);
        }
    }


    public interface onItemClickListener {
        void onItemClick(View view, ModData ad);
    }


    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filterString = constraint.toString().toLowerCase();
            final ArrayList<ModData> nlist = new ArrayList<ModData>();

            for (ModData filterableData: SearchHelper.allModsList) {
                if (constraint.toString().equals(SearchHelper.NEW_MODS_SEARCH_QUERY)) {
                    if (SearchHelper.newMods.contains(filterableData.key)) nlist.add(filterableData);
                } else if (filterableData.title.toLowerCase().contains(filterString)) nlist.add(filterableData);
            }

            FilterResults results = new FilterResults();
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            modsList.clear();
            if (results.count > 0 && results.values != null)
                modsList.addAll((ArrayList<ModData>)results.values);
            sortList();
            notifyDataSetChanged();
        }
    }

    private void sortList() {
        modsList.sort(new Comparator<ModData>() {
            public int compare(ModData app1, ModData app2) {
                int breadcrumbs = app1.breadcrumbs.compareToIgnoreCase(app2.breadcrumbs);
                if (breadcrumbs == 0)
                    return app1.title.compareToIgnoreCase(app2.title);
                else
                    return breadcrumbs;
            }
        });
    }
}
