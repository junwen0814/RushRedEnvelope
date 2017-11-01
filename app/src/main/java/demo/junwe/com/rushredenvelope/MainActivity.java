package demo.junwe.com.rushredenvelope;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private final int REQUEST_CODE = 0;

    //QQ抢红包服务名称
    private String serviceName;


    private ImageView caishen;

    private AnimationDrawable frameAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        caishen = findViewById(R.id.main_caishen);
        initView();
        initListener();
    }

    private void initView() {
        serviceName = getPackageName() + "/.EnvelopeService";
        frameAnim = (AnimationDrawable) getResources().getDrawable(R.drawable.animation_list);
        caishen.setBackgroundDrawable(frameAnim);

        frameAnim.start();
    }

    private void initListener() {

        //开启关闭红包
        caishen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isServiceRunning()) {
                    //服务没有在运行,打开辅助页面
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    //可以运行抢红包
                    Toast.makeText(MainActivity.this, "已经开启抢红包，等待红包来到！", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    @Override
    protected void onRestart() {
        super.onRestart();
        //再检查一下是否已经running
        if (isServiceRunning()) {
            Toast.makeText(MainActivity.this, "已经开启抢红包，等待红包来到！", Toast.LENGTH_SHORT).show();
        }
    }

}
