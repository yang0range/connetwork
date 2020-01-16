
package com.yang.library.loghandler;

import com.yang.library.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Log {

    /**
     * 是否显示应用的log 在应用上线的时候需要修改此状态表示为 false
     */
    public static boolean IS_SHOW_LOG = BuildConfig.DEBUG;

    public interface LogInterface {

        void d(String logTag, String logText);

        void e(String logTag, String logText);

        void w(String logTag, String logText);

        void v(String logTag, String logText);

        void i(String logTag, String logText);

        void print(String logText);

    }

    private static LogInterface instance;

    public static LogInterface getInstance() {
        if (instance == null)
            instance = newDefaultAndroidLog();
        return instance;
    }

    public static void setInstance(LogInterface instance) {
        Log.instance = instance;
    }

    private static LogInterface newDefaultAndroidLog() {
        return new LogInterface() {

            @Override
            public void w(String logTag, String logText) {
                android.util.Log.w(logTag, logText);
            }

            @Override
            public void v(String logTag, String logText) {
                android.util.Log.v(logTag, logText);
            }

            @Override
            public void i(String logTag, String logText) {
                android.util.Log.i(logTag, logText);
            }

            @Override
            public void e(String logTag, String logText) {
                android.util.Log.e(logTag, logText);
            }

            @Override
            public void d(String logTag, String logText) {
                android.util.Log.d(logTag, logText);
            }

            @Override
            public void print(String logText) {
                android.util.Log.d("Debug Output", logText);
            }
        };
    }

    public static void d(String logTag, String logText) {
        if (IS_SHOW_LOG) {
            getInstance().d(logTag, logText);
        }
    }

    public static void e(String logTag, String logText) {
        if (IS_SHOW_LOG) {
            getInstance().e(logTag, logText);
        }

    }

    public static void w(String logTag, String logText) {
        if (IS_SHOW_LOG) {
            getInstance().w(logTag, logText);
        }

    }

    public static void v(String logTag, String logText) {
        if (IS_SHOW_LOG) {
            getInstance().v(logTag, logText);
        }

    }

    public static void i(String logTag, String logText) {
        if (IS_SHOW_LOG) {
            getInstance().i(logTag, logText);
        }

    }

    public static void out(String logText) {
        if (IS_SHOW_LOG) {
            getInstance().print(logText);
        }
    }

    public static void outJson(String tag, String msg, String headString) {
        if (IS_SHOW_LOG) {
            printJson(tag, msg, headString);
        }
    }


    public static void printJson(String tag, String msg, String headString) {
        final String LINE_SEPARATOR = System.getProperty("line.separator");
        String message;

        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(4);//最重要的方法，就一行，返回格式化的json字符串，其中的数字4是缩进字符数
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(4);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }

        printLine(tag, true);
        message = headString + LINE_SEPARATOR + message;
        String[] lines = message.split(LINE_SEPARATOR);
        for (String line : lines) {
            android.util.Log.d(tag, "║ " + line);
        }
        printLine(tag, false);
    }

    public static void printLine(String tag, boolean isTop) {
        if (isTop) {
            android.util.Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            android.util.Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
        }
    }

    /**
     * @param matrixName
     * @param a          必须是一个 4x4 matrix 矩阵
     * @return the debug string
     */
    public static String floatMatrixToString(String matrixName, float[] a) {
        String s = "";
        s += "Matrix: " + matrixName + "\n";
        s += "\t " + a[0] + "," + a[1] + "," + a[2] + "," + a[3] + " \n";
        s += "\t " + a[4] + "," + a[5] + "," + a[6] + "," + a[7] + " \n";
        s += "\t " + a[8] + "," + a[9] + "," + a[10] + "," + a[11] + " \n";
        s += "\t " + a[12] + "," + a[13] + "," + a[14] + "," + a[15] + " \n";
        return s;
    }

}
