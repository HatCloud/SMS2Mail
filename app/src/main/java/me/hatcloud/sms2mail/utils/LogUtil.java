package me.hatcloud.sms2mail.utils;

import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import me.hatcloud.sms2mail.BuildConfig;

public class LogUtil {
    private static final String TAG = LogUtil.class.getSimpleName();

    public static boolean DEBUG = BuildConfig.DEBUG;

    private static Map<String, Long> sTimeMap;

    public static void start(String tag) {
        if (DEBUG) {
            if (sTimeMap == null) {
                sTimeMap = new HashMap<>();
            }
            sTimeMap.put(tag, SystemClock.uptimeMillis());
        }
    }

    public static void print(String tag) {
        print(tag, null);
    }

    public static void print(String tag, String msg) {
        if (DEBUG) {
            if (sTimeMap == null) {
                start(tag);
            }
            Long start = sTimeMap.get(tag);
            if (start != null) {
                Log.d(tag, ((msg == null) ? "" : msg) + (SystemClock.uptimeMillis() - start.longValue()) + " ms");
            }
            sTimeMap.remove(tag);
        }
    }

    public static void d(String msg) {
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void d(String tag, Throwable t, String str) {
        if (DEBUG) {
            Log.d(tag, getLogMsg(str), t);
        }
    }

    public static void d(String tag, String str) {
        if (DEBUG) {
            Log.d(tag, getLogMsg(str));
        }
    }

    public static void dWithNoSwitch(String tag, String str) {
        Log.d(tag, getLogMsg(str));
    }

    private static StringBuilder logBuilder;

    /**
     * 不加日志开关打印
     */
    public static void dWithNoSwitch(String tag, Object... logs) {
        if (logs != null && logs.length > 0) {
            if (logs.length == 1) {
                Log.d(tag, String.valueOf(logs[0]));
            } else {
                StringBuilder sb;
                if (isMain()) {
                    if (logBuilder == null) {
                        logBuilder = new StringBuilder();
                    } else {
                        int len = logBuilder.length();
                        if (len > 0) {
                            logBuilder.setLength(0);
                        }
                    }
                    sb = logBuilder;
                } else {
                    sb = new StringBuilder();
                }
                sb.ensureCapacity(200);
                for (int i = 0; i < logs.length; i++) {
                    sb.append(logs[i]);
                }
                Log.d(tag, getLogMsg(sb.toString()));
            }
        } else {
            Log.d(tag, "logs is empty!");
        }
    }

    public static void d(String tag, Object... logs) {
        if (DEBUG) {
            dWithNoSwitch(tag, logs);
        }
    }

    public static void i(String tag, String str) {
        if (DEBUG) {
            Log.i(tag, getLogMsg(str));
        }
    }

    public static void i(String str) {
        if (DEBUG) {
            Log.i(getTagName(), getLogMsg(str));
        }
    }

    public static void w(String tag, String str) {
        if (DEBUG) {
            Log.w(tag, getLogMsg(str));
        }
    }

    public static void w(String str) {
        if (DEBUG) {
            Log.w(getTagName(), getLogMsg(str));
        }
    }

    public static void e(String tag, Throwable tr, String str) {
        if (DEBUG) {
            Log.e(tag, getLogMsg(str), tr);
        }
    }

    public static void e(String tag, String str) {
        if (DEBUG) {
            Log.e(tag, getLogMsg(str));
        }
    }

    public static void e(String str) {
        if (DEBUG) {
            Log.e(getTagName(), getLogMsg(str));
        }
    }

    public static void v(String tag, String str) {
        if (DEBUG) {
            Log.v(tag, getLogMsg(str));
        }
    }

    public static void v(String str) {
        if (DEBUG) {
            Log.v(getTagName(), getLogMsg(str));
        }
    }

    public static void watch(String tag, Object... objects) {
        if (!DEBUG) {
            return;
        }
        HashMap<String, Object> objectHashMap = new HashMap<>();
        for (Object object : objects) {
            objectHashMap.put(object.getClass().getSimpleName(), object);
        }
        watch(tag, objectHashMap);
    }

    /**
     * Example
     * <p>
     * LogUtil.watch("tag", new HashMap<String, Object>() {{
     * put("name", object);
     * }});
     */
    public static void watch(String tag, Map<String, Object> objectMap) {
        if (!DEBUG) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("  \n┌────────────────────────────────────────────\n");
        sb.append("│ \n");
        sb.append("│ Watching objects\n");
        sb.append("│ \n");
        for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
            if (entry == null) {
                continue;
            }
            sb.append("├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄\n");
            sb.append("│ ");
            sb.append(entry.getKey());
            sb.append(" -> ");
            String msg = entry.getValue() == null ? "null" : entry.getValue().toString();
            sb.append(msg);

            sb.append("\n");
        }
        sb.append("└────────────────────────────────────────────\n");
        Log.d(tag, sb.toString());
    }

    private static String getTagName() {
        StackTraceElement stackTraceElement = new Throwable().fillInStackTrace().getStackTrace()[2];

        String className = stackTraceElement.getClassName();
        if (className.contains("$")) {
            className = className.split("\\$")[1];
        } else if (className.contains(".")) {
            className = className.split("\\.")[className.split("\\.").length - 1];
        }

        return className;
    }

    private static String getLogMsg(String msg) {
        StackTraceElement stackTraceElement = new Throwable().fillInStackTrace().getStackTrace()[2];
        String className = stackTraceElement.getClassName();
        if (className.contains("$")) {
            className = className.split("\\$")[1];
        } else if (className.contains(".")) {
            className = className.split("\\.")[className.split("\\.").length - 1];
        }
        String methodName = stackTraceElement.getMethodName();
        int lineNumber = stackTraceElement.getLineNumber();

        StringBuilder sb = new StringBuilder();
        sb.append("[ (").append(className).append(".java").append(":").append(lineNumber).append(")#")
                .append(methodName).append(" ] ");
        sb.append(msg);
        return sb.toString();
    }

    static boolean isMain() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
