package demo.junwe.com.rushredenvelope;

import android.app.Application;
import android.content.Context;

/**
 * Created by junwen on 2017/10/31.
 */

public class MyApp extends Application{

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        BaseAccessibilityService.getInstance().init(getApplicationContext());
    }
}
