package com.cse6324.phms;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cse6324.bean.MedicineBean;
import com.cse6324.http.Constant;
import com.cse6324.http.HttpUtil;
import com.cse6324.service.MyApplication;
import com.cse6324.threads.LoadContactsThread;
import com.cse6324.util.UserUtil;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * Created by tusha on 2/24/2017.
 */

public class AddMedicineActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout container;
    static int rTimesCount=0;
    private static final int CAMERA_REQUEST = 1888;
    private static final int RESULT_LOAD_IMAGE = 1;
    ImageView imgMedicine, imgOpenCamera, imgOpenGallery, imgIncreaseTimes;
    TextView txtStartDate, txtEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_add_medicine);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);

        container = (LinearLayout)findViewById(R.id.ll_reminder_times);

        imgIncreaseTimes = (ImageView) findViewById(R.id.img_increase_times);
        imgIncreaseTimes.setOnClickListener(this);

    /*    imgOpenCamera = (ImageView) findViewById(R.id.img_take_photo);
        imgOpenCamera.setOnClickListener(this);

        imgOpenGallery = (ImageView) findViewById(R.id.img_select_gallery);
        imgOpenGallery.setOnClickListener(this);

        imgMedicine = (ImageView) findViewById(R.id.img_medicine);      */

        txtStartDate = (TextView) findViewById(R.id.txt_start_date);
        txtStartDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v) {
                String selectedTime = ((TextView)v).getText().toString();
                int year = Integer.parseInt(selectedTime.split("-")[0]);
                int month = Integer.parseInt(selectedTime.split("-")[1])-1;
                int day = Integer.parseInt(selectedTime.split("-")[2]);
                DatePickerDialog dpd = new DatePickerDialog(AddMedicineActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker timePicker, int year, int month, int day) {
                        ((TextView)v).setText(year + "-" + String.format("%02d", month+1) + "-" + String.format("%02d", day));
                    }
                }, year,month,day);
                dpd.show();
            }});

        txtEndDate = (TextView) findViewById(R.id.txt_end_date);
        txtEndDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v) {
                String selectedTime = ((TextView)v).getText().toString();
                int year = Integer.parseInt(selectedTime.split("-")[0]);
                int month = Integer.parseInt(selectedTime.split("-")[1])-1;
                int day = Integer.parseInt(selectedTime.split("-")[2]);
                DatePickerDialog dpd = new DatePickerDialog(AddMedicineActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker timePicker, int year, int month, int day) {
                        ((TextView)v).setText(year + "-" + String.format("%02d", month+1) + "-" + String.format("%02d", day));
                    }
                }, year,month,day);
                dpd.show();
            }});

        Thread loadContactsThread = new LoadContactsThread(getBaseContext(),this);
        loadContactsThread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_save:
                MedicineBean mb = generateMedicineBean();
                new HttpUtil(HttpUtil.NORMAL_PARAMS)
                        .add("uid", MyApplication.getPreferences(UserUtil.UID))
                        .add("token", MyApplication.getPreferences(UserUtil.TOKEN))
                        .add("mid",mb.getMid())
                        .add("name",mb.getName())
                        .add("reminder",mb.getReminder())
                        .add("times",mb.getTimes())
                        .add("days",mb.getDays())
                        .add("start_date",mb.getStart_date())
                        .add("end_date",mb.getEnd_date())
                        .add("quantity",mb.getQuantity())
                        .add("unit",mb.getUnit())
                        .add("instructions",mb.getInstructions())
                        .add("notification",mb.getNotification())
                        .add("contacts",mb.getContacts())
                        .get(Constant.URL_ADDMEDICINE, addMedicineCallback);
                break;
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private BaseHttpRequestCallback addMedicineCallback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {

            if (response == null || response.length() == 0) {
                Toast.makeText(getBaseContext(), "Connect fail", Toast.LENGTH_SHORT).show();
            } else if(headers.get("Status-Code").equals("1")){
                Toast.makeText(getBaseContext(), headers.get("summary"), Toast.LENGTH_SHORT).show();
                AddMedicineActivity.this.finish();
            }else{
                Toast.makeText(getBaseContext(), headers.get("summary"), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.img_increase_times:
                LinearLayout ll = new LinearLayout(this);
                ll.setId(rTimesCount);
                ll.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv = new TextView(this);
                tv.setText("09:00");
                tv.setTextColor(Color.BLACK);
                tv.setTextSize(18);
                tv.setId(rTimesCount+1000);
                ll.addView(tv);
                tv.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(final View v) {
                        Calendar c = Calendar.getInstance();
                        String selectedTime = ((TextView)v).getText().toString();
                        int hour = Integer.parseInt(selectedTime.split(":")[0]);
                        int minute = Integer.parseInt(selectedTime.split(":")[1]);
                        TimePickerDialog tpd = new TimePickerDialog(AddMedicineActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                ((TextView)v).setText(String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute));
                            }
                        }, hour,minute, true);
                        tpd.show();
                    }});

                ImageView iv = new ImageView(this);
                iv.setId(rTimesCount+2000);
                iv.setImageResource(R.mipmap.ic_highlight_off_black_24dp);
                iv.setMaxWidth(25);
                iv.setMaxHeight(25);

                iv.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        int imageId = ((ImageView)v).getId();
                        int hLayoutId = imageId - 2000;
                        container.removeView(findViewById(hLayoutId));
                    }});

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)view.getLayoutParams();
                params.gravity = Gravity.LEFT;
                ll.addView(iv,params);

                container.addView(ll);
                rTimesCount++;

                break;

    /*        case R.id.img_take_photo:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null)
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                break;

            case R.id.img_select_gallery:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;          */

            case R.id.txt_start_date:
                break;

            case R.id.txt_end_date:
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().finishActivity(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data!=null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgMedicine.setImageBitmap(imageBitmap);
        }

        else if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imgMedicine.setImageURI(selectedImage);
        }
    }

    protected MedicineBean generateMedicineBean() {
        MedicineBean mb = new MedicineBean();

        CharSequence name = ((TextView)findViewById(R.id.txt_medicine_name)).getText();
        if(name!=null)
            mb.setName(name.toString());

        boolean reminder = ((Switch)findViewById(R.id.sw_reminder)).isChecked();
        if(reminder==true)
            mb.setReminder("1");
        else
            mb.setReminder("0");

        LinearLayout hl;
        List<String> times = new ArrayList<String>();
        for (int i = 0; i < container.getChildCount(); i++) {
            hl = (LinearLayout)container.getChildAt(i);
            times.add(((TextView)hl.getChildAt(0)).getText().toString());
        }
        mb.setTimes(TextUtils.join(", ", times));

        List<String> days = new ArrayList<String>();
        if(((CheckBox)findViewById(R.id.txtSun)).isChecked())
            days.add("Sun");
        if(((CheckBox)findViewById(R.id.txtMon)).isChecked())
            days.add("Mon");
        if(((CheckBox)findViewById(R.id.txtTue)).isChecked())
            days.add("Tue");
        if(((CheckBox)findViewById(R.id.txtWed)).isChecked())
            days.add("Wed");
        if(((CheckBox)findViewById(R.id.txtThu)).isChecked())
            days.add("Thu");
        if(((CheckBox)findViewById(R.id.txtFri)).isChecked())
            days.add("Fri");
        if(((CheckBox)findViewById(R.id.txtSat)).isChecked())
            days.add("Sat");
        mb.setDays(TextUtils.join(", ", days));

        CharSequence startDate = ((TextView)findViewById(R.id.txt_start_date)).getText();
        if(startDate!=null)
            mb.setStart_date(startDate.toString());

        CharSequence endDate = ((TextView)findViewById(R.id.txt_end_date)).getText();
        if(endDate!=null)
            mb.setEnd_date(endDate.toString());

        Editable quantity = ((MaterialEditText)findViewById(R.id.txt_dose_quantity)).getText();
        if(quantity!=null)
            mb.setQuantity(quantity.toString());

        String unit = ((Spinner)findViewById(R.id.sp_unit)).getSelectedItem().toString();
        mb.setUnit(unit);

        Editable instructions = ((MaterialEditText)findViewById(R.id.txt_med_instructions)).getText();
        if(instructions!=null)
            mb.setInstructions(instructions.toString());

        boolean notifications = ((Switch)findViewById(R.id.sw_notifiations)).isChecked();
        if(notifications==true)
            mb.setNotification("1");
        else
            mb.setNotification("0");

        if(((Spinner)findViewById(R.id.sp_contacts)).getCount()!=0) {
            String contacts = ((Spinner) findViewById(R.id.sp_contacts)).getSelectedItem().toString();
            mb.setContacts(contacts);
        }
        else
            mb.setContacts(null);

        return mb;
    }

}
