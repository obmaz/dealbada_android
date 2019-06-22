package com.moon.dealocean.ui.navigation;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moon.dealocean.R;
import com.moon.dealocean.util.NavMenuUtil;

/**
 * Created by zambo on 2017-08-11.
 */

public class NavExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private NavExpandedMenuVO mNavExpandedMenuVO;

    public NavExpandableListAdapter(Context context, NavExpandedMenuVO navExpandedMenuVO) {
        mContext = context;
        mNavExpandedMenuVO = navExpandedMenuVO;
    }

    @Override
    public int getGroupCount() {
        try {
            return mNavExpandedMenuVO.getData().size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        try {
            return mNavExpandedMenuVO.getData().get(groupPosition).getSubMenu().size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public NavExpandedMenuDataVO getGroup(int groupPosition) {
        return mNavExpandedMenuVO.getData().get(groupPosition);
    }

    @Override
    public NavExpandedMenuDataVO getChild(int groupPosition, int childPosition) {
        return mNavExpandedMenuVO.getData().get(groupPosition).getSubMenu().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        NavExpandedMenuDataVO navExpandedMenuDataVO = getGroup(groupPosition);

        try {
            mNavExpandedMenuVO.getData().get(groupPosition).setOpened(isExpanded);
            NavMenuUtil.setNavExpandedMenuVO(mContext, mNavExpandedMenuVO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.nav_list_menu_header, null);
        }

        TextView headerTextView = (TextView) convertView.findViewById(R.id.nav_list_menu_header_text);
        ImageView headerIcon = (ImageView) convertView.findViewById(R.id.nav_list_menu_header_image);

        headerTextView.setText(navExpandedMenuDataVO.getTitle());

        if (navExpandedMenuDataVO.getIconImg() != null) {
            int drawableResId = mContext.getResources().getIdentifier(navExpandedMenuDataVO.getIconImg(), "drawable", mContext.getPackageName());
            headerIcon.setImageResource(drawableResId);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final NavExpandedMenuDataVO navExpandedMenuDataVO = getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.nav_list_menu_child, null);
        }

        TextView childTextView = (TextView) convertView.findViewById(R.id.nav_list_menu_child_text);
        ImageView childIcon = (ImageView) convertView.findViewById(R.id.nav_list_menu_child_image);

        childTextView.setText(navExpandedMenuDataVO.getTitle());

        if (!TextUtils.isEmpty(navExpandedMenuDataVO.getIconImg())) {
            int drawableResId = mContext.getResources().getIdentifier(navExpandedMenuDataVO.getIconImg(), "drawable", mContext.getPackageName());
            childIcon.setImageResource(drawableResId);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
