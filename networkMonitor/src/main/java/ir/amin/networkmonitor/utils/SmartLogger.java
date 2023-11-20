package ir.amin.networkmonitor.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SmartLogger {
    private static SmartLogger instance;
    public static SmartLogger Companion;//this is just for compatibility issue after converted from kotlin
    private static boolean enableLogger = true;//can be used to disable logger in release build type and enable it in debug build type
    private static final String separator = "<-";

    public static SmartLogger getInstance() {
        if (instance == null) {
            instance = new SmartLogger();
            Companion = instance;
        }
        return instance;
    }

    public static void logDebug(String msg) {
        getInstance().instanceLogDebug(msg);
    }

    public static void logError(String msg) {
        getInstance().instanceLogError(msg);
    }

    public static void logDebug() {
        getInstance().instanceLogDebug("");
    }

    private static Context context = null;
    private static String classPrifix = "";

    private String LOGGER_NAME = "SmartLogger";

    public static void initLogger(Context contextValue) {
        context = contextValue;
    }

    public static void initLogger(Context contextValue, boolean enableLoggerValue) {
        context = contextValue;
        enableLogger = enableLoggerValue;
    }

    public static void setClassPrefix(String prefix) {
        classPrifix = prefix;
    }

    public static void setEnableLogger(boolean enableLoggerValue) {
        enableLogger = enableLoggerValue;
    }

    public static void releaseContext() {
        context = null;
    }

    public void instanceLogDebug(String msg) {
        if (checkEnableLogger())
            return;
        if (getElement().getClassName().startsWith(classPrifix))
            Logger.getLogger(LOGGER_NAME).warning(getLogString(msg));
//        Logger.getLogger("fg").log(Level.ALL,"l");
//        Logger.getLogger("fg").log(Level.ALL,"l");
    }

    public void instanceLogError(String msg) {
        if (checkEnableLogger())
            return;
        if (getElement().getClassName().startsWith(classPrifix))
            Logger.getLogger(LOGGER_NAME).log(Level.SEVERE, getLogString(msg));
    }

    private boolean checkEnableLogger() {
        return !enableLogger;
    }

    private String getLogString(String msg) {
        return " \n" + getLogDivider() + "\n" + getHeaders() + "\n" + msg + "\n" + getLogDivider();
    }

    private String getHeaders() {
        //todo add real instance class name
        StackTraceElement element = getElement();
        String className = element.getClassName();
        String methodName = getElementLastMethod();
        String methodTrace = getElementMethods();
        String lineNumber = String.valueOf(element.getLineNumber());
        return "Class:  " + className + "\n" + "Method: " + methodName + "(Line: " + lineNumber + ") " + "| Method Trace: " + methodTrace + "\n" + "Version:" + getVersion() + "\n" + getMessageDivider();
    }

    private String getMessageDivider() {
        return "**************************************************************************************************************";
    }

    private String getLogDivider() {
        return "--------------------------------------------------------------------------------------------------------------";
    }

    private StackTraceElement getElement() {
        boolean inThis = false;
        for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
            //find first element witch is not this class
            if (stackTraceElement.getClassName().endsWith(SmartLogger.class.getName()))
                inThis = true;
            else if (inThis) {
                return stackTraceElement;
            }
        }

        return Thread.currentThread().getStackTrace()[4];
    }

    private String getElementLastMethod() {
        String methodTails = getElementMethods();
        return methodTails.substring(0, methodTails.indexOf(separator));
    }

    private String getElementMethods() {
        String methodTail = "";
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        for (int i = trace.length - 1; i >= 0; i--) {
            if (trace[i].getMethodName().equals("getElementMethods"))
                break;
            if (!trace[i].getClassName().endsWith(SmartLogger.class.getName()))
                methodTail = separator + trace[i].getMethodName() + methodTail;
//                methodTail.append(trace[i].getMethodName()).append(separator);
        }
        return methodTail.substring(separator.length());
    }


    private String getVersion() {
        if (context == null)
            return "No version(Not initialized use initLogger to initialize!)";
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);

//            Toast.makeText(this,
//                    "PackageName = " + info.packageName + "\nVersionCode = "
//                            + info.versionCode + "\nVersionName = "
//                            + info.versionName + "\nPermissions = " + info.permissions, Toast.LENGTH_SHORT).show()
            String versionCode = String.valueOf(info.versionCode);
            String versionName = info.versionName;
            return versionName + "(" + versionCode + ")";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "PackageManager.NameNotFoundException";
    }


}