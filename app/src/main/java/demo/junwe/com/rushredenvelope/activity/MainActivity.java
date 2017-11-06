package demo.junwe.com.rushredenvelope.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import demo.junwe.com.rushredenvelope.R;
import demo.junwe.com.rushredenvelope.common.BaseAccessibilityService;
import demo.junwe.com.rushredenvelope.utils.SettingConfig;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //QQ抢红包服务名称
    private String serviceName;

    private ImageView mActionEnable;

    private TextView mMainSetting;

    private TextView mDisableMessage;

    private TextView mEnableState;

    private AnimationDrawable mFrameAnim;

    private boolean isJumpSetting;//是否跳转到设置页面,跳转的时候是设置成true

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainSetting = (TextView) findViewById(R.id.main_setting);
        mActionEnable = findViewById(R.id.main_actionEnable);
        mDisableMessage = findViewById(R.id.main_disable_server);
        mEnableState = findViewById(R.id.main_enable_state);
        initView();
        initListener();
    }

    private void initView() {
        //拼接抢红包服务的全名，便于查询服务是否开启
        serviceName = getPackageName() + "/.service.EnvelopeService";
        //财神的动画
        mFrameAnim = (AnimationDrawable) getResources().getDrawable(R.drawable.animation_list);
        mActionEnable.setBackgroundDrawable(mFrameAnim);
        //根据当前服务的状态以及是否开启的状态来进行初始化
        if (isServiceRunning() && isRedEnvelopeEnable()) {
            enableRedEnvelope(true);
        } else {
            enableRedEnvelope(false);
        }

    }

    private void initListener() {
        mActionEnable.setOnClickListener(this);
        mMainSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_actionEnable:
                //开关抢红包
                checkAction();
                break;
            case R.id.main_setting:
                //跳转红包设置页面
                startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                break;

        }
    }

    /**
     * 描述:验证开关
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:2017/11/3 下午2:46
     */
    public void checkAction() {
        if (!isServiceRunning()) {
            //服务没有在运行,打开辅助页面
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            isJumpSetting = true;
        } else {
            //服务有运行，但是还要判断下，现在抢红包是开的还是关的
            if (isRedEnvelopeEnable()) {
                //开启状态，设置成关闭状态
                enableRedEnvelope(false);
            } else {
                //设置红包为开
                enableRedEnvelope(true);
                Toast.makeText(MainActivity.this, "已经开启抢红包，等待红包来到！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 描述:是否正在运行
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:2017/11/1 下午2:04
     */
    public boolean isServiceRunning() {
        return BaseAccessibilityService.getInstance().checkAccessibilityEnabled(serviceName);
    }

    /**
     * 描述:红包开关是否打开
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:2017/11/3 下午2:37
     */
    public boolean isRedEnvelopeEnable() {
        return SettingConfig.getInstance().getReEnable();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        //检测服务是否运行，并且如果是跳转到辅助页面打开服务后返回的，则设置为插件运行中
        if (isServiceRunning() && isJumpSetting == true) {
            //如果是服务开启状态，并且是之前点击打开设置页面后返回的
            SettingConfig.getInstance().setReEnable(true);
            isJumpSetting = false;
        }

        //然后根据服务和红包的开关状态来进行更新页面状态
        if (isServiceRunning() && isRedEnvelopeEnable()) {
            enableRedEnvelope(true);
            Toast.makeText(MainActivity.this, "已经开启抢红包，等待红包来到！", Toast.LENGTH_SHORT).show();
        } else {
            enableRedEnvelope(false);
        }
    }


    /**
     * 描述:开关抢红包
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:2017/11/3 下午2:42
     */
    public void enableRedEnvelope(boolean enable) {
        if (!enable && !isServiceRunning()) {
            mDisableMessage.setVisibility(View.VISIBLE);
            mEnableState.setVisibility(View.GONE);
        } else {
            mDisableMessage.setVisibility(View.GONE);
            mEnableState.setVisibility(View.VISIBLE);
        }
        mEnableState.setText(enable ? "抢红包进行中。。。。" : "抢红包暂停中");
        SettingConfig.getInstance().setReEnable(enable);
        checkAnimation(enable);
    }

    public void checkAnimation(boolean check) {
        if (mFrameAnim == null) {
            return;
        }
        if (check) {
            mFrameAnim.start();
        } else {
            mFrameAnim.stop();
        }
    }

}
