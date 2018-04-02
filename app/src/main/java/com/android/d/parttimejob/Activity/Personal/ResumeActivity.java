package com.android.d.parttimejob.Activity.Personal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.d.parttimejob.Application.App;
import com.android.d.parttimejob.DataBase.Dao.PluralistDao;
import com.android.d.parttimejob.Entry.Pluralist;
import com.android.d.parttimejob.MyView.Dialog.ChoiceDialog;
import com.android.d.parttimejob.MyView.Dialog.GenderSelectDialog;
import com.android.d.parttimejob.MyView.Dialog.InputDialog;
import com.android.d.parttimejob.MyView.NavigationBar;
import com.android.d.parttimejob.R;
import com.android.d.parttimejob.Util.HttpURLHandler;
import com.android.d.parttimejob.Util.HttpURLTask;
import com.android.d.parttimejob.Util.JSONParser;
import com.android.d.parttimejob.Util.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/**
 * 简历页面
 * Created by Administrator on 2016/8/19.
 */
public class ResumeActivity extends Activity implements View.OnClickListener {

    private NavigationBar mNavigationBar;//导航栏

    private static final String URL = App.PREFIX + "Part-timeJob/PluralistServlet";


    private RelativeLayout mAgeRL;
    private RelativeLayout mGenderRL;
    private RelativeLayout mHeightRL;
    private RelativeLayout mEducationRL;
    private RelativeLayout mSchoolRL;
    private RelativeLayout mFeatureRL;
    private RelativeLayout mExperienceRL;
    private RelativeLayout mEmailRL;

    private ImageView mHeadImage;
    private TextView mNameTV;

    private TextView mAgeTV;
    private TextView mGenderTV;
    private TextView mHeightTV;
    private TextView mEducationTV;
    private TextView mSchoolTV;
    private TextView mEmailTV;


    private String ageT = "";
    private String genderT = "";
    private String heightT = "";
    private String schoolT = "";
    private String educationT = "";
    private String emailT = "";


    private ImageView mGenderEditImg;
    private ImageView mAgeEditImg;
    private ImageView mHeightEditImg;
    private ImageView mSchoolEditImg;
    private ImageView mEducationEditImg;
    private ImageView mFeatureEditImg;
    private ImageView mExperienceEditImg;
    private ImageView mEmailEditImg;


    private Uri imageUri;
    private String imageName;

    public static final int CHOOSE_PICTURE = 1;
    public static final int TAKE_PICTURE = 3;
    public static final int SHOW_PICTURE = 2;


    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resume);
        //初始化
        initView();
        //设置点击事件
        setClickListener();
        //获得数据
        setData();

    }

    /**
     * 设置点击事件
     */
    private void setClickListener() {
        //导航栏
        mNavigationBar.registerListener(new NavigationBar.OnClickListener() {
            @Override
            public void onClickBack() {
                ResumeActivity.this.finish();
            }

            @Override
            public void onClickImg() {

            }

            @Override
            public void onClickRightText() {

                if (!isEdit) {
                    mNavigationBar.setRightText("保存");
                    isEdit = true;

                    mNavigationBar.setRightTextColor(R.color.edit_color);

                    mAgeRL.setClickable(true);
                    mGenderRL.setClickable(true);
                    mHeightRL.setClickable(true);
                    mEducationRL.setClickable(true);
                    mSchoolRL.setClickable(true);
                    mFeatureRL.setClickable(true);
                    mExperienceRL.setClickable(true);
                    mEmailRL.setClickable(true);

                    mGenderEditImg.setVisibility(View.VISIBLE);
                    mAgeEditImg.setVisibility(View.VISIBLE);
                    mHeightEditImg.setVisibility(View.VISIBLE);
                    mSchoolEditImg.setVisibility(View.VISIBLE);
                    mEducationEditImg.setVisibility(View.VISIBLE);
                    mFeatureEditImg.setVisibility(View.VISIBLE);
                    mExperienceEditImg.setVisibility(View.VISIBLE);
                    mEmailEditImg.setVisibility(View.VISIBLE);


                } else {
                    mNavigationBar.setRightText("修改");
                    isEdit = false;
                    mNavigationBar.setRightTextColor(R.color.text_color);

                    mAgeRL.setClickable(false);
                    mGenderRL.setClickable(false);
                    mHeightRL.setClickable(false);
                    mEducationRL.setClickable(false);
                    mSchoolRL.setClickable(false);
                    mFeatureRL.setClickable(false);
                    mExperienceRL.setClickable(false);
                    mEmailRL.setClickable(false);

                    mGenderEditImg.setVisibility(View.INVISIBLE);
                    mAgeEditImg.setVisibility(View.INVISIBLE);
                    mHeightEditImg.setVisibility(View.INVISIBLE);
                    mSchoolEditImg.setVisibility(View.INVISIBLE);
                    mEducationEditImg.setVisibility(View.INVISIBLE);
                    mFeatureEditImg.setVisibility(View.INVISIBLE);
                    mExperienceEditImg.setVisibility(View.INVISIBLE);
                    mEmailEditImg.setVisibility(View.INVISIBLE);

                    Toast.makeText(ResumeActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    //将修改保存起来
                    storeTemp();
                }

            }
        });

        mGenderRL.setOnClickListener(this);
        mAgeRL.setOnClickListener(this);
        mEducationRL.setOnClickListener(this);
        mEmailRL.setOnClickListener(this);
        mExperienceRL.setOnClickListener(this);
        mFeatureRL.setOnClickListener(this);
        mSchoolRL.setOnClickListener(this);
        mHeightRL.setOnClickListener(this);
        mHeadImage.setOnClickListener(this);

        mGenderRL.setClickable(false);
        mAgeRL.setClickable(false);
        mHeightRL.setClickable(false);
        mEducationRL.setClickable(false);
        mSchoolRL.setClickable(false);
        mFeatureRL.setClickable(false);
        mExperienceRL.setClickable(false);
        mEmailRL.setClickable(false);

    }

    /**
     * 初始化view
     */

    private void initView() {
        App.addActivity(this);

        mNavigationBar = (NavigationBar) findViewById(R.id.id_resume_navigation_bar);

        mAgeRL = (RelativeLayout) findViewById(R.id.id_resume_birth);
        mEducationRL = (RelativeLayout) findViewById(R.id.id_resume_education);
        mEmailRL = (RelativeLayout) findViewById(R.id.id_resume_email);
        mExperienceRL = (RelativeLayout) findViewById(R.id.id_resume_experience);
        mFeatureRL = (RelativeLayout) findViewById(R.id.id_resume_feature);
        mGenderRL = (RelativeLayout) findViewById(R.id.id_resume_gender);
        mSchoolRL = (RelativeLayout) findViewById(R.id.id_resume_school);
        mHeightRL = (RelativeLayout) findViewById(R.id.id_resume_height);

        mAgeTV = (TextView) findViewById(R.id.id_resume_birth_tv);
        mEducationTV = (TextView) findViewById(R.id.id_resume_education_tv);
        mEmailTV = (TextView) findViewById(R.id.id_resume_email_tv);
        mGenderTV = (TextView) findViewById(R.id.id_resume_gender_tv);
        mSchoolTV = (TextView) findViewById(R.id.id_resume_school_tv);
        mHeightTV = (TextView) findViewById(R.id.id_resume_height_tv);

        mHeadImage = (ImageView) findViewById(R.id.id_resume_head_img);
        mNameTV = (TextView) findViewById(R.id.id_resume_name);


        mGenderEditImg = (ImageView) findViewById(R.id.id_resume_gender_edit_img);
        mAgeEditImg = (ImageView) findViewById(R.id.id_resume_age_edit_img);
        mHeightEditImg = (ImageView) findViewById(R.id.id_resume_height_edit_img);
        mSchoolEditImg = (ImageView) findViewById(R.id.id_resume_school_edit_img);
        mEducationEditImg = (ImageView) findViewById(R.id.id_resume_education_edit_img);
        mFeatureEditImg = (ImageView) findViewById(R.id.id_resume_feature_edit_img);
        mExperienceEditImg = (ImageView) findViewById(R.id.id_resume_experience_edit_img);
        mEmailEditImg = (ImageView) findViewById(R.id.id_resume_email_edit_img);
    }

    /**
     * 处理点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //修改性别
            case R.id.id_resume_gender:
                final GenderSelectDialog dialog;
                if (genderT == null) {
                    dialog = new GenderSelectDialog(ResumeActivity.this);
                } else if (genderT.equals("男")) {
                    dialog = new GenderSelectDialog(ResumeActivity.this, "男");
                } else if (genderT.equals("女")) {
                    dialog = new GenderSelectDialog(ResumeActivity.this, "女");
                } else {
                    dialog = new GenderSelectDialog(ResumeActivity.this);
                }

                dialog.setOnSelectListener(new GenderSelectDialog.GenderSelectedListener() {
                    @Override
                    public void onMaleSelected() {
                        genderT = "男";
                        mGenderTV.setText(genderT);

                        dialog.dismiss();
                    }

                    @Override
                    public void onFemaleSelected() {
                        genderT = "女";
                        mGenderTV.setText(genderT);
                        dialog.dismiss();
                    }
                });
                dialog.show();

                break;

            //修改年龄
            case R.id.id_resume_birth:

                final InputDialog ageDialog = new InputDialog(ResumeActivity.this, "年龄", ageT);
                ageDialog.setOnInputListener(new InputDialog.InputClickListener() {
                    @Override
                    public void onCancelClick() {
                        ageDialog.dismiss();
                    }

                    @Override
                    public void onOkClick(String content) {

                        ageT = content;
                        mAgeTV.setText(content);
                        ageDialog.dismiss();
                    }
                });
                ageDialog.show();
                break;
            //修改身高
            case R.id.id_resume_height:

                final InputDialog heightDialog = new InputDialog(ResumeActivity.this, "身高", heightT);
                heightDialog.setOnInputListener(new InputDialog.InputClickListener() {
                    @Override
                    public void onCancelClick() {
                        heightDialog.dismiss();
                    }

                    @Override
                    public void onOkClick(String content) {

                        heightT = content;
                        mHeightTV.setText(content + "cm");
                        heightDialog.dismiss();
                    }
                });

                heightDialog.show();
                break;

            //修改学历
            case R.id.id_resume_education:

                final InputDialog educationDialog = new InputDialog(ResumeActivity.this, "学历", educationT);
                educationDialog.setOnInputListener(new InputDialog.InputClickListener() {
                    @Override
                    public void onCancelClick() {
                        educationDialog.dismiss();
                    }

                    @Override
                    public void onOkClick(String content) {

                        educationT = content;
                        mEducationTV.setText(content);
                        educationDialog.dismiss();
                    }
                });

                educationDialog.show();
                break;
            //修改学校
            case R.id.id_resume_school:

                final InputDialog schoolDialog = new InputDialog(ResumeActivity.this, "学校", schoolT);
                schoolDialog.setOnInputListener(new InputDialog.InputClickListener() {
                    @Override
                    public void onCancelClick() {
                        schoolDialog.dismiss();
                    }

                    @Override
                    public void onOkClick(String content) {

                        schoolT = content;
                        mSchoolTV.setText(content);
                        schoolDialog.dismiss();
                    }
                });

                schoolDialog.show();
                break;
            //修改邮箱
            case R.id.id_resume_email:

                final InputDialog emailDialog = new InputDialog(ResumeActivity.this, "邮箱", emailT);
                emailDialog.setOnInputListener(new InputDialog.InputClickListener() {
                    @Override
                    public void onCancelClick() {
                        emailDialog.dismiss();
                    }

                    @Override
                    public void onOkClick(String content) {

                        emailT = content;
                        mEmailTV.setText(content);
                        emailDialog.dismiss();
                    }
                });

                emailDialog.show();
                break;


            //修改特点
            case R.id.id_resume_feature:

                Intent featureIntent = new Intent(ResumeActivity.this, ResumeFeatureActivity.class);
                startActivity(featureIntent);

                break;
            //修改经历
            case R.id.id_resume_experience:

                Intent experienceIntent = new Intent(ResumeActivity.this, ResumeExperienceActivity.class);
                startActivity(experienceIntent);
                break;


            //修改头像
            case R.id.id_resume_head_img:

                final ChoiceDialog choiceDialog = new ChoiceDialog(this);
                choiceDialog.setOnChoiceListener(new ChoiceDialog.ChoiceListener() {
                    @Override
                    public void onFirstSelected() {

                        //隐式调用照相机程序
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, TAKE_PICTURE);

                        choiceDialog.dismiss();

                    }

                    @Override
                    public void onSecondSelected() {

                        imageName =App.mPluralist.getPhone()+ System.currentTimeMillis() + ".jpg";
                        String imagePath = Environment.getExternalStorageDirectory().getPath() + "/Part-timeJob/Image/" + imageName;

                        Util.deleteImage(App.mPluralist.getImageName());
                        App.mPluralist.setImageName(imageName);
                        //存入数据库
                        PluralistDao dao = new PluralistDao(ResumeActivity.this);
                        dao.updateImage(App.mPluralist.getId(), imageName);

                        File imageFile = new File(imagePath);
                        try {
                            if (imageFile.exists()) {
                                boolean delete = imageFile.delete();
                            }
                            boolean newFile = imageFile.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        imageUri = Uri.fromFile(imageFile);
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        //调用的是系统图库
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, CHOOSE_PICTURE);

                        choiceDialog.dismiss();

                    }
                });
                choiceDialog.show();
                break;
            default:
                break;
        }
    }

    /**
     * 设置数据
     */
    private void setData() {
        Pluralist p = App.mPluralist;

        mAgeTV.setText(p.getAge() + "");
        mNameTV.setText(p.getName());
        mSchoolTV.setText(p.getSchool());
        mEducationTV.setText(p.getEducationBackground());
        mGenderTV.setText(p.getGender());
        mEmailTV.setText(p.getEmail());
        mHeightTV.setText(p.getHeight() + "cm");
        //设置头像
        String imageName = p.getImageName();
        Bitmap bitmap = Util.restoreImage(imageName, this);
        if (bitmap != null) {
            mHeadImage.setImageBitmap(bitmap);
        }


        ageT = p.getAge() + "";
        genderT = p.getGender();


        heightT = p.getHeight() + "";
        schoolT = p.getSchool();
        educationT = p.getEducationBackground();
        emailT = p.getEmail();
    }

    private void storeTemp() {


        Pluralist p = App.mPluralist;

        p.setAge(Integer.valueOf(ageT));
        p.setGender(genderT);
        p.setHeight(Integer.valueOf(heightT));
        p.setSchool(schoolT);
        p.setEducationBackground(educationT);
        p.setEmail(emailT);

        //讲修改内容保存起来
        PluralistDao dao = new PluralistDao(this);
        dao.update(p);

        //更行服务器的数据库
        HashMap<String, String> param = new HashMap<>();
        param.put("action", "update");
        param.put("pluralistJson", JSONParser.toJSONString(p));

        HttpURLTask task = new HttpURLTask(URL, "POST", param, new HttpURLHandler());
        task.start();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case CHOOSE_PICTURE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        //此处启动裁剪程序
                        Intent intent = new Intent("com.android.camera.action.CROP");
                        intent.setDataAndType(data.getData(), "image/*");
                        intent.putExtra("scale", true);
                        intent.putExtra("outputX", 64);
                        intent.putExtra("outputY", 64);
                        // aspectX aspectY 是宽高的比例
                        intent.putExtra("aspectX", 1);
                        intent.putExtra("aspectY", 1);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, SHOW_PICTURE);
                    } else {
                        Toast.makeText(ResumeActivity.this, "出错了", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case TAKE_PICTURE:

                if (resultCode == RESULT_OK) {

                    String outImagePath = getImage(data);
                    if (outImagePath == null) return;

                    File outImageFile = new File(outImagePath);
                    Uri uri = Uri.fromFile(outImageFile);

                    if (outImageFile.exists()) {
                        imageName = App.mPluralist.getPhone()+System.currentTimeMillis() + ".jpg";

                        App.mPluralist.setImageName(imageName);
                        //存入数据库
                        PluralistDao dao = new PluralistDao(ResumeActivity.this);
                        dao.updateImage(App.mPluralist.getId(), imageName);

                        String imagePath =
                                Environment.getExternalStorageDirectory().getPath() + "/Part-timeJob/Image/" + imageName;
                        File imageFile = new File(imagePath);
                        try {
                            if (imageFile.exists()) {
                                imageFile.delete();
                            }
                            imageFile.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        imageUri = Uri.fromFile(imageFile);

                        //此处启动裁剪程序
                        Intent intent = new Intent("com.android.camera.action.CROP");
                        intent.setDataAndType(uri, "image/*");
                        intent.putExtra("scale", true);
                        intent.putExtra("outputX", 64);
                        intent.putExtra("outputY", 64);
                        // aspectX aspectY 是宽高的比例
                        intent.putExtra("aspectX", 1);
                        intent.putExtra("aspectY", 1);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, SHOW_PICTURE);
                    }
                }


                break;
            case SHOW_PICTURE:
                if (resultCode == RESULT_OK) {
                    try {
                        //将output_image.jpg对象解析成Bitmap对象，然后设置到ImageView中显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                                .openInputStream(imageUri));
                        mHeadImage.setImageBitmap(bitmap);
                        //将图片发送到服务器
                        sendImage(imageName,bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 从拍照中获得图片并存储
     *
     * @return 图片的地址
     */
    private String getImage(Intent data) {

        Bundle bundle = data.getExtras();
        Bitmap picture = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

        String imageName = "outImage.jpg";
        return Util.storeImage(imageName, picture);
    }


    /**
     * 向服务奇传输照片
     */
    private void sendImage(String imageName, Bitmap bitmap) {

        ByteArrayOutputStream os=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,os);

        byte[] buffer=os.toByteArray();

        HashMap<String ,String> params=new HashMap<>();
        params.put("action","sendImage");
        params.put("pluralistId",App.mPluralist.getId());
        params.put("imageName",imageName);
        params.put("imageCode", Base64.encodeToString(buffer,Base64.DEFAULT));

        HttpURLTask task=new HttpURLTask(URL,params,new HttpURLHandler());
        task.start();
    }

}
