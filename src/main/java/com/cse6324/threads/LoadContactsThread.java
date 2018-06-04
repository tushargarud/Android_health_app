package com.cse6324.threads;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.alibaba.fastjson.JSON;
import com.cse6324.bean.ContactBean;
import com.cse6324.http.Constant;
import com.cse6324.http.HttpUtil;
import com.cse6324.phms.R;
import com.cse6324.service.MyApplication;
import com.cse6324.util.UserUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * Created by tushar on 2/27/2017.
 */

public class LoadContactsThread extends Thread {

    private Context context;
    private Activity activity;


    public LoadContactsThread(Context c, Activity a) {
        context = c;
        activity = a;
    }

    public void run() {

        new HttpUtil(HttpUtil.NORMAL_PARAMS)
                .add("uid", MyApplication.getPreferences(UserUtil.UID))
                .add("token", MyApplication.getPreferences(UserUtil.TOKEN))
                .get(Constant.URL_GETCONTACT, callback);

    }


    private BaseHttpRequestCallback callback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {
            if(!response.isEmpty()) {

                Spinner spContacts = (Spinner)activity.findViewById(R.id.sp_contacts);
                List<ContactBean> spContactsList = JSON.parseArray(response, ContactBean.class);

                List<String> spContactNameList = new ArrayList<String>();
                for(Iterator<ContactBean> itr = spContactsList.iterator(); itr.hasNext(); ) {
                    ContactBean contact = itr.next();
                    spContactNameList.add(contact.getName());
                }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, spContactNameList);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
                spContacts.setAdapter(spinnerArrayAdapter);
            }
        }
    };
}
