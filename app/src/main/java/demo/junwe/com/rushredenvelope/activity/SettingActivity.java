package demo.junwe.com.rushredenvelope.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mixiaoxiao.smoothcompoundbutton.SmoothCompoundButton;
import com.mixiaoxiao.smoothcompoundbutton.SmoothSwitch;

import demo.junwe.com.rushredenvelope.R;
import demo.junwe.com.rushredenvelope.utils.SettingConfig;

/**
 * Created by junwen on 2017/11/3.
 */

public class SettingActivity extends AppCompatActivity implements SmoothCompoundButton.OnCheckedChangeListener, View.OnClickListener {


    private SmoothSwitch mReEnable;
    private SmoothSwitch mMusicEnable;
    private LinearLayout mReplyMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initListener();
    }


    private void initView() {
        mReEnable = (SmoothSwitch) findViewById(R.id.setting_re_enable);
        mMusicEnable = (SmoothSwitch) findViewById(R.id.setting_music_enable);
        mReplyMessage = (LinearLayout) findViewById(R.id.setting_reply_message);

        //初始化开关
        boolean reEnable = SettingConfig.getInstance().getReEnable();
        boolean reMusicEnable = SettingConfig.getInstance().getReMusicEnable();

        mReEnable.setChecked(reEnable);
        mMusicEnable.setChecked(reMusicEnable);

    }

    private void initListener() {
        mReEnable.setOnCheckedChangeListener(this);
        mMusicEnable.setOnCheckedChangeListener(this);
        mReplyMessage.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(SmoothCompoundButton smoothCompoundButton, boolean check) {
        switch (smoothCompoundButton.getId()) {
            case R.id.setting_re_enable:
                //抢红包开关变化
                SettingConfig.getInstance().setReEnable(check);
                break;
            case R.id.setting_music_enable:
                //抢红包音乐开关变化
                SettingConfig.getInstance().setReMusicEnable(check);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_reply_message:
                //修改回复消息
                updateReplyDialog();
                break;
        }
    }

    private void updateReplyDialog() {
        String reReplyMessage = SettingConfig.getInstance().getReReplyMessage();
        final EditText inputServer = new EditText(SettingActivity.this);
        if (!TextUtils.isEmpty(reReplyMessage)) {
            inputServer.setText(reReplyMessage);
            inputServer.setSelection(reReplyMessage.length());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setTitle("修改回复信息").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (inputServer.getText() != null) {
                    String input = inputServer.getText().toString();
                    SettingConfig.getInstance().setReReplyMessage(input);
                    dialog.dismiss();
                }
            }

        });
        builder.show();
    }
}
