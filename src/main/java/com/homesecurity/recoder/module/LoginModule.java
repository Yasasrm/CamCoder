package com.homesecurity.recoder.module;

import com.homesecurity.recoder.lib.NetSDKLib;
import com.homesecurity.recoder.lib.NetSDKLib.LLong;
import com.sun.jna.ptr.IntByReference;

import java.io.File;
import java.util.Date;

public class LoginModule {

    public static NetSDKLib netsdk = NetSDKLib.NETSDK_INSTANCE;
    public static NetSDKLib configsdk = NetSDKLib.CONFIG_INSTANCE;
    public static Date date = new Date();

    public static LLong loginHandle = new LLong(0);

    public static boolean bInit = false;
    public static boolean bLogOpen = false;

    public static boolean init(NetSDKLib.fDisConnect disConnect, NetSDKLib.fHaveReConnect haveReConnect) {
        bInit = netsdk.CLIENT_Init(disConnect, null);
        if (!bInit) {
            System.out.println("SDK initialization failed!");
            return false;
        }

        NetSDKLib.LOG_SET_PRINT_INFO setLog = new NetSDKLib.LOG_SET_PRINT_INFO();
        File path = new File("./sdklog/");
        if (!path.exists()) {
            path.mkdir();
        }
        String logPath = path.getAbsoluteFile().getParent() + "\\sdklog\\" + date.toString() + ".log";
        setLog.nPrintStrategy = 0;
        setLog.bSetFilePath = 1;
        System.arraycopy(logPath.getBytes(), 0, setLog.szLogFilePath, 0, logPath.getBytes().length);
        System.out.println(logPath);
        setLog.bSetPrintStrategy = 1;
        bLogOpen = netsdk.CLIENT_LogOpen(setLog);
        if (!bLogOpen) {
            System.err.println("Failed to open NetSDK log");
        }

        netsdk.CLIENT_SetAutoReconnect(haveReConnect, null);

        int waitTime = 5000;
        int tryTimes = 1;
        netsdk.CLIENT_SetConnectTime(waitTime, tryTimes);

        NetSDKLib.NET_PARAM netParam = new NetSDKLib.NET_PARAM();
        netParam.nConnectTime = 10000;
        netParam.nGetConnInfoTime = 3000;
        netParam.nGetDevInfoTime = 3000;
        netsdk.CLIENT_SetNetworkParam(netParam);

        return true;
    }

    public static void cleanup() {
        if (bLogOpen) {
            netsdk.CLIENT_LogClose();
        }

        if (bInit) {
            netsdk.CLIENT_Cleanup();
        }
    }

    public static NetSDKLib.NET_DEVICEINFO_Ex deviceInfo = new NetSDKLib.NET_DEVICEINFO_Ex();

    public static boolean login(String ip, int port, String username, String password) {
        IntByReference errorCode = new IntByReference(0);
        loginHandle = netsdk.CLIENT_LoginEx2(ip, port, username, password, 0, null, deviceInfo, errorCode);
        if (loginHandle.longValue() == 0)
            System.err.println("Error occurred while connecting to the camera!");
        else
            System.out.println("Successfully connected to the camera..");
        return loginHandle.longValue() == 0 ? false : true;
    }

    public static boolean logout() {
        if (loginHandle.longValue() == 0) {
            return false;
        }

        boolean bRet = netsdk.CLIENT_Logout(loginHandle);

        if (bRet)
            loginHandle.setValue(0);
        return bRet;
    }
}
