package demo.junwe.com.rushredenvelope;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import java.util.List;

/**
 * Created by junwen on 2017/10/31.
 */

public class EnvelopeService extends BaseAccessibilityService {


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.e("EnvelopeService", event.toString());
        int eventType = event.getEventType();

        switch (eventType) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                //状态栏变化
//                if (!(event.getParcelableData() instanceof Notification)) {
//                    return;
//                }
//                Notification notification = (Notification) event.getParcelableData();
                List<CharSequence> text = event.getText();
                if (text != null && text.size() > 0) {
                    for (CharSequence charSequence : text) {
                        if ("抢红包咯".equals(charSequence)) {
                            //说明存在红包弹窗，马上进去
                            Toast.makeText(this, "发现红包，点击进去", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;

        }


    }

    @Override
    public void onInterrupt() {

    }

}
