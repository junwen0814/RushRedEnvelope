package demo.junwe.com.rushredenvelope;

import android.app.Application;

/**
 * Created by junwen on 2017/10/31.
 */

public class MyApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        BaseAccessibilityService.getInstance().init(getApplicationContext());
    }
}
