package com.liangzhiyang.youmi.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.Stack;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 *
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class ActivityManager {

    private static Stack<Activity> activityStack;
    private static ActivityManager instance;
    private static Handler globalHandler;
    private static Activity currentActivity;


    private ActivityManager() {
        activityStack = new Stack<Activity>();
        globalHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 单一实例
     */
    public static ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }


    public boolean isHasCurrentActivity() {
        return getActivitySize() >= 1;
    }

    public boolean isHasLastActivity() {
        return getActivitySize() >= 2;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 移除当前Activity（堆栈中最后一个压入的）
     */
    public void removeActivity(Activity activity) {
        if (activity != null && activityStack.contains(activity)) {
            activityStack.remove(activity);
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        if (isHasCurrentActivity()) {
            return activityStack.lastElement();
        }
        return null;
    }

    /**
     * 上一个Activity
     *
     * @return
     */
    public Activity getLastActivity() {
        if (isHasLastActivity()) {
            return activityStack.elementAt(activityStack.size() - 2);
        }
        return null;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishCurrentActivity() {
        if (getActivitySize() > 1) {
            Activity activity = activityStack.lastElement();
            finishActivity(activity);
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.removeElement(activity);
            activity.finish();
        }
    }

    /**
     * 删除除了当前activity以外的所有activity
     */
    public void finishAllOtherActivity() {
        for (int i = 0; i < getActivitySize(); i++) {
            if (activityStack.get(i) != currentActivity()) {
                activityStack.get(i).finish();
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        try {
            if (activityStack.size() > 0) {
                for (Activity activity : activityStack) {
                    if (activity.getClass().equals(cls)) {
                        finishActivity(activity);
                        break;
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.elementAt(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }


    /**
     * 获取集合的大小
     *
     * @return
     */
    public int getActivitySize() {
        if (activityStack == null) {
            return 0;
        }
        return activityStack.size();
    }

    public interface Callback {
        public void act();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            android.app.ActivityManager activityMgr = (android.app.ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());// restartPackage 过期了 tested

            System.exit(0);
        } catch (Exception e) {
        }
    }
}
