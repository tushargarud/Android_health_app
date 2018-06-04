package com.cse6324.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cse6324.bean.ContactBean;
import com.cse6324.phms.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jarvis on 2017/2/15.
 */

public class ContactListAdapter extends RecyclerView.Adapter{

    private Context context;
    private List<ContactBean> contactList;

    public  ContactListAdapter(Context context){
        this.context = context;
        this.contactList = new ArrayList<>();
    }

    public void setContactList(List<ContactBean> contactList){
        this.contactList = contactList;
    }

    @Override
    public int getItemCount(){
        return contactList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_contact, null);
        view.setLayoutParams(lp);
        return new ItemViewHolder(view);
    }

    @Override
    public int getItemViewType(int position){
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i){
        ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;

        ContactBean bean = contactList.get(i);

        itemViewHolder.tvTitle.setText(bean.getName());
        itemViewHolder.tvContent.setText(bean.getPhone());
        itemViewHolder.tvContent2.setText(bean.getEmail());

        Random random=new Random();
        int a = random.nextInt(10);
        switch (a){
            case 0:
                itemViewHolder.avatar.setImageResource(R.drawable.avatar_01);
                break;
            case 1:
                itemViewHolder.avatar.setImageResource(R.drawable.avatar_02);
                break;
            case 2:
                itemViewHolder.avatar.setImageResource(R.drawable.avatar_03);
                break;
            case 3:
                itemViewHolder.avatar.setImageResource(R.drawable.avatar_04);
                break;
            case 4:
                itemViewHolder.avatar.setImageResource(R.drawable.avatar_05);
                break;
            case 5:
                itemViewHolder.avatar.setImageResource(R.drawable.avatar_06);
                break;
            case 6:
                itemViewHolder.avatar.setImageResource(R.drawable.avatar_07);
                break;
            case 7:
                itemViewHolder.avatar.setImageResource(R.drawable.avatar_08);
                break;
            case 8:
                itemViewHolder.avatar.setImageResource(R.drawable.avatar_09);
                break;
            case 9:
                itemViewHolder.avatar.setImageResource(R.drawable.avatar_10);
                break;
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder{
        String phone,email;
        ImageView avatar;
        TextView tvTitle,tvContent,tvContent2;

        private ItemViewHolder(View itemView) {
            super(itemView);

            avatar = (ImageView) itemView.findViewById(R.id.iv_item);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            tvContent2 = (TextView) itemView.findViewById(R.id.tv_content2);
        }
    }
}
