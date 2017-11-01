package demo.junwe.com.rushredenvelope;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    private Button redEnvelope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        redEnvelope = (Button) findViewById(R.id.main_check_red_envelope);
        initListener();
    }

    private void initListener() {

        //开启关闭红包
        redEnvelope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isRunning = BaseAccessibilityService.getInstance().checkAccessibilityEnabled(getPackageName() + "/.EnvelopeService");
                if (!isRunning) {
                    BaseAccessibilityService.getInstance().goAccess();
                } else {
                    //可以运行抢红包
                    NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder(getApplicationContext());
                    notificationCompatBuilder.setTicker("抢红包咯");
                    notificationCompatBuilder.setAutoCancel(true).setContentTitle("抢红包咯").setContentText("一元红包").setSmallIcon(R.drawable.ic_launcher_background).build();
                    NotificationManager notificationManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(1, notificationCompatBuilder.build());
                }
            }
        });
    }
}
