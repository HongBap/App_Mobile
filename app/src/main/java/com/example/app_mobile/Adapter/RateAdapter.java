package com.example.app_mobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.app_mobile.Model.Rate;
import com.example.app_mobile.R;

import java.util.List;

public class RateAdapter extends BaseAdapter {

    private Context mcontext;
    private List<Rate> mrates;

    public RateAdapter(Context context, List<Rate> rates) {
        mcontext = context;
        mrates = rates;
    }

    @Override
    public int getCount() {
        return mrates.size();
    }

    @Override
    public Rate getItem(int position) {
        return mrates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_rateiterm, null);
        }

        TextView tvUserBinhLuan = (TextView) view.findViewById(R.id.tvUserBinhLuan);
        tvUserBinhLuan.setText(getItem(position).getUserName());

        TextView tvBinhLuan = (TextView) view.findViewById(R.id.tvBinhLuan);
        tvBinhLuan.setText(getItem(position).getRateComment());

        TextView tvStart1 = (TextView) view.findViewById(R.id.tvStart1);
        TextView tvStart2 = (TextView) view.findViewById(R.id.tvStart2);
        TextView tvStart3 = (TextView) view.findViewById(R.id.tvStart3);
        TextView tvStart4 = (TextView) view.findViewById(R.id.tvStart4);
        TextView tvStart5 = (TextView) view.findViewById(R.id.tvStart5);

        if(getItem(position).getRatePoint()==0) {
            tvStart1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_outline_24, 0, 0, 0);
            tvStart2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_outline_24, 0, 0, 0);
            tvStart3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_outline_24, 0, 0, 0);
            tvStart4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_outline_24, 0, 0, 0);
            tvStart5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_outline_24, 0, 0, 0);
        }
        else if(getItem(position).getRatePoint()==1) {
            tvStart1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_24, 0, 0, 0);
            tvStart2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_outline_24, 0, 0, 0);
            tvStart3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_outline_24, 0, 0, 0);
            tvStart4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_outline_24, 0, 0, 0);
            tvStart5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_outline_24, 0, 0, 0);
        }
        else if(getItem(position).getRatePoint()==2) {
            tvStart1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_24, 0, 0, 0);
            tvStart2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_24, 0, 0, 0);
            tvStart3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_outline_24, 0, 0, 0);
            tvStart4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_outline_24, 0, 0, 0);
            tvStart5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_outline_24, 0, 0, 0);
        }
        else if(getItem(position).getRatePoint()==3) {
            tvStart1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_24, 0, 0, 0);
            tvStart2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_24, 0, 0, 0);
            tvStart3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_24, 0, 0, 0);
            tvStart4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_outline_24, 0, 0, 0);
            tvStart5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_outline_24, 0, 0, 0);
        }
        else if(getItem(position).getRatePoint()==4) {
            tvStart1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_24, 0, 0, 0);
            tvStart2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_24, 0, 0, 0);
            tvStart3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_24, 0, 0, 0);
            tvStart4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_24, 0, 0, 0);
            tvStart5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_outline_24, 0, 0, 0);
        }
        else{
            tvStart1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_24, 0, 0, 0);
            tvStart2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_24, 0, 0, 0);
            tvStart3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_24, 0, 0, 0);
            tvStart4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_24, 0, 0, 0);
            tvStart5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_24, 0, 0, 0);
        }
        return view;
    }
}
