package com.liangzhiyang.youmi.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by lsd on 17/4/13.
 */

public class CopyUtils {
    private static final String LABEL = "label";


    /**
     * 获取剪贴板的manager
     *
     * @param context
     * @return
     */
    public static ClipboardManager getClipboardManager(Context context) {
        ClipboardManager clipboardManager =
                (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        return clipboardManager;
    }

    /**
     * 复制文本
     *
     * @param context
     * @param msg
     */
    public static void copyText(Context context, @NonNull String msg) {
        ClipboardManager clipboardManager = getClipboardManager(context);
        ClipData clipData = ClipData.newPlainText(LABEL, msg);
        clipboardManager.setPrimaryClip(clipData);
        ClipData clipData2 = clipboardManager.getPrimaryClip();
        Log.d("copy", clipData2.getDescription().getLabel() + ":" + clipData2.getDescription().getMimeType(0) + ":"
                + clipData2.getItemAt(0).getText());
        if (msg.equals(getText(context))) {
            Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取剪贴板的文本
     *
     * @return 剪贴板的文本
     */
    public static CharSequence getText(Context context) {
        ClipboardManager clipboardManager = getClipboardManager(context);
        ClipData clip = clipboardManager.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).coerceToText(context);
        }
        return null;
    }
}
