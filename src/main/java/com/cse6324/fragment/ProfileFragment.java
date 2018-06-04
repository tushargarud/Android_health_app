package com.cse6324.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cse6324.bean.UserBean;
import com.cse6324.phms.ContactActivity;
import com.cse6324.phms.EditVitalSignsActivity;
import com.cse6324.phms.LoginActivity;
import com.cse6324.phms.R;
import com.cse6324.phms.SettingActivity;
import com.cse6324.service.MyApplication;
import com.cse6324.util.UserUtil;

import org.w3c.dom.Text;

/**
 * Created by Jarvis on 2017/2/11.
 */

public class ProfileFragment extends Fragment implements View.OnClickListener {

    TextView tvName, tvEmail, tvAge, tvGender, tvHeight, tvWeight, tvBp, tvBsl, tvChol;

    RelativeLayout rlEditVitalSigns;

    LinearLayout layoutContact;

    String age, gender, height, weight, bp, bsl, chol;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, null);

        tvName = (TextView) v.findViewById(R.id.tv_name);
        tvEmail = (TextView) v.findViewById(R.id.tv_email);
        tvAge = (TextView) v.findViewById(R.id.tv_age);
        tvGender = (TextView) v.findViewById(R.id.tv_gender);
        tvHeight = (TextView) v.findViewById(R.id.tv_height);
        tvWeight = (TextView) v.findViewById(R.id.tv_weight);
        tvBp = (TextView) v.findViewById(R.id.tv_bp);
        tvBsl = (TextView) v.findViewById(R.id.tv_bsl);
        tvChol = (TextView) v.findViewById(R.id.tv_chol);

        setInfo();

        rlEditVitalSigns = (RelativeLayout) v.findViewById(R.id.editVitalSigns);
        rlEditVitalSigns.setOnClickListener(this);

        layoutContact = (LinearLayout) v.findViewById(R.id.layout_contact);
        layoutContact.setOnClickListener(this);
        return v;
    }

    public void setInfo(){
        tvName.setText(MyApplication.getPreferences(UserUtil.UNAME));
        tvEmail.setText(MyApplication.getPreferences(UserUtil.EMAIL));

        age = MyApplication.getPreferences(UserUtil.AGE);
        gender = MyApplication.getPreferences(UserUtil.GENDER);
        height = MyApplication.getPreferences(UserUtil.HEIGHT);
        weight = MyApplication.getPreferences(UserUtil.WEIGHT);
        bp = MyApplication.getPreferences(UserUtil.BP);
        bsl = MyApplication.getPreferences(UserUtil.BSL);
        chol = MyApplication.getPreferences(UserUtil.CHOL);

        tvAge.setText(isEmpty(age));
        tvHeight.setText(isEmpty(height));
        tvWeight.setText(isEmpty(weight));
        tvBp.setText(isEmpty(bp));
        tvBsl.setText(isEmpty(bsl));
        tvChol.setText(isEmpty(chol));

        if(gender.isEmpty()){
            gender = "N/A";
        }else if(gender.equals("1"))
            gender = "Male";
        else
            gender = "Female";

        tvGender.setText(gender);

    }

    @Override
    public void onClick(View view) {

        Intent intent;
        switch (view.getId()) {
            case R.id.editVitalSigns:
                intent = new Intent(getActivity(), EditVitalSignsActivity.class);
                getActivity().startActivityForResult(intent,0);
                break;
            case R.id.layout_contact:
                intent = new Intent(getActivity(), ContactActivity.class);
                getContext().startActivity(intent);
                break;
        }
    }

    public String isEmpty(String str) {
        if (str.isEmpty())
            return "N/A";
        else
            return str;
    }

}
