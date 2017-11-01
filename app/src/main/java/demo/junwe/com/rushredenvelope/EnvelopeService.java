package demo.junwe.com.rushredenvelope;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;

import java.util.List;


public class EnvelopeService extends BaseAccessibilityService {


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        //获取包名
        CharSequence packageName = event.getPackageName();
        if (TextUtils.isEmpty(packageName)) {
            return;
        }

        switch (eventType) {

            //状态栏变化
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:

                if (QQConstant.QQ_PACKAGE_NAME.equals(packageName)) {
                    //QQ处理
                    progressQQStatusBar(event);
                }

                break;

            //窗口切换的时候回调
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:

                if (QQConstant.QQ_PACKAGE_NAME.equals(packageName)) {
                    //处理QQ聊天页面
                    progressQQChat(event);
                }

                break;
        }


    }

    /**
     * 描述:处理QQ状态栏
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:2017/11/1 下午1:49
     */
    public void progressQQStatusBar(AccessibilityEvent event) {
        List<CharSequence> text = event.getText();
        //开始检索界面上是否有QQ红包的文本，并且他是通知栏的信息
        if (text != null && text.size() > 0) {
            for (CharSequence charSequence : text) {
                if (charSequence.toString().contains(QQConstant.QQ_ENVELOPE_KEYWORD)) {
                    //说明存在红包弹窗，马上进去
                    if (event.getParcelableData() != null && event.getParcelableData() instanceof Notification) {
                        Notification notification = (Notification) event.getParcelableData();
                        if (notification == null) {
                            return;
                        }
                        PendingIntent pendingIntent = notification.contentIntent;
                        if (pendingIntent == null) {
                            return;
                        }
                        try {
                            pendingIntent.send();
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 描述:处理QQ聊天红包
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:2017/11/1 下午1:56
     */
    public void progressQQChat(AccessibilityEvent event) {
        if (TextUtils.isEmpty(event.getClassName())) {
            return;
        }
        if (QQConstant.QQ_IM_CHAT_ACTIVITY.equals(event.getClassName().toString())) {
            //如果当前界面转换到了QQ聊天页面，则搜索红包的字眼，点击他

            //普通红包
            List<AccessibilityNodeInfo> envelope = findViewListByText(QQConstant.QQ_CLICK_TAKE_APART, false);
            progressNormal(envelope);

            //口令红包
            List<AccessibilityNodeInfo> passwordList = findViewListByText(QQConstant.QQ_CLICK_PASSWORD_DIALOG, false);
            progressPassword(passwordList);

        }
    }


    /**
     * 描述:处理QQ红包
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:2017/11/1 下午5:02
     */
    public void progressNormal(List<AccessibilityNodeInfo> passwordList) {
        if (passwordList != null && passwordList.size() > 0) {
            for (AccessibilityNodeInfo accessibilityNodeInfo : passwordList) {
                if (accessibilityNodeInfo != null && !TextUtils.isEmpty(accessibilityNodeInfo.getText()) && QQConstant.QQ_CLICK_TAKE_APART.equals(accessibilityNodeInfo.getText().toString())) {
                    performViewClick(accessibilityNodeInfo);
                }
            }
        }
    }

    /**
     * 描述:处理口令红包
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:2017/11/1 下午4:58
     *
     * @param passwordList
     */
    public void progressPassword(List<AccessibilityNodeInfo> passwordList) {
        if (passwordList != null && passwordList.size() > 0) {
            for (AccessibilityNodeInfo accessibilityNodeInfo : passwordList) {

                if (accessibilityNodeInfo != null && !TextUtils.isEmpty(accessibilityNodeInfo.getText()) && QQConstant.QQ_CLICK_PASSWORD_DIALOG.equals(accessibilityNodeInfo.getText().toString())) {
                    //如果口令红包存在，就在输入框中进行输入，然后发送
                    AccessibilityNodeInfo parent = accessibilityNodeInfo.getParent();
                    if (parent != null) {
                        CharSequence contentDescription = parent.getContentDescription();
                        if (!TextUtils.isEmpty(contentDescription)) {
                            //1. 获取口令
                            String key = (String) contentDescription;
                            if (key.contains(",") && key.contains("口令:")) {
                                key = key.substring(key.indexOf("口令:") + 3, key.lastIndexOf(","));
                            }
                            Log.e("口令", key);

                            //2. 填写口令到编辑框上进行发送
                            AccessibilityNodeInfo chat_edit = findViewByID("com.tencent.mobileqq:id/input");
                            if (chat_edit != null) {
                                copyKeyToEdit(chat_edit, key);


                                AccessibilityNodeInfo sendMessage = findViewByID("com.tencent.mobileqq:id/fun_btn");
                                if (sendMessage != null && Button.class.getName().equals(sendMessage.getClassName())) {
                                    performViewClick(sendMessage);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 描述:复制文本到
     * 作者:卜俊文
     * 邮箱:344176791@qq.com
     * 日期:2017/11/1 下午4:18
     */
    private void copyKeyToEdit(AccessibilityNodeInfo chat_edit, String key) {
        //android>21 = 5.0时可以用ACTION_SET_TEXT
        //android>18 3.0.1可以通过复制的手段,先确定焦点，再粘贴ACTION_PASTE
        //使用剪切板
        ClipboardManager clipboard = (ClipboardManager) MyApp.context.getSystemService(Context.CLIPBOARD_SERVICE);
        //获取焦点
        chat_edit.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
        //需要替换的key
        clipboard.setPrimaryClip(ClipData.newPlainText("text", key));
        //粘贴进入内容
        chat_edit.performAction(AccessibilityNodeInfo.ACTION_PASTE);
    }


    @Override
    public void onInterrupt() {

    }

}
