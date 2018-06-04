package com.cse6324.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cse6324.bean.ContactBean;
import com.cse6324.bean.MedicineBean;
import com.cse6324.phms.AddMedicineActivity;
import com.cse6324.phms.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jarvis on 2017/3/1.
 */

public class MedicineListAdapter extends RecyclerView.Adapter {

    final static int TITLE = 0;
    final static int ITEM = 1;

    private Context context;
    private List<MedicineBean> medicineList;

    public MedicineListAdapter(Context context) {
        this.context = context;
        this.medicineList = new ArrayList<>();
    }

    public void setList(List<MedicineBean> medicineList) {
        this.medicineList = medicineList;
    }

    @Override
    public int getItemCount() {
        return medicineList.size() + 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        View view;

        if (i == TITLE) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_head_medicine, null);
            view.setLayoutParams(lp);
            return new HeadViewHolder(view);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_medicine, null);
            view.setLayoutParams(lp);
            return new ItemViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TITLE;
        } else
            return ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        if(viewHolder instanceof ItemViewHolder) {

            ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;

            MedicineBean bean = medicineList.get(i - 1);

            itemViewHolder.tvName.setText(bean.getName());
            itemViewHolder.tvQuantity.setText(bean.getQuantity() + " " + bean.getUnit());
            itemViewHolder.tvInstraction.setText(bean.getInstructions());
            itemViewHolder.tvTime.setText(bean.getTimes());

        }else if(viewHolder instanceof HeadViewHolder){

            HeadViewHolder headViewHolder = (HeadViewHolder) viewHolder;

            headViewHolder.tvHead.setText(
                    "Today you have " + medicineList.size() + " medicines to take"
            );
        }

    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantity, tvInstraction, tvTime;

        private ItemViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvQuantity = (TextView) itemView.findViewById(R.id.tv_quantity);
            tvInstraction = (TextView) itemView.findViewById(R.id.tv_instruction);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }

    private class HeadViewHolder extends RecyclerView.ViewHolder {

        TextView tvHead;

        private HeadViewHolder(View itemView) {
            super(itemView);

            tvHead = (TextView) itemView.findViewById(R.id.tv_head);

            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, AddMedicineActivity.class);
                            context.startActivity(intent);
                        }
                    }
            );
        }
    }
}
