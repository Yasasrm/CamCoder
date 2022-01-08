package com.homesecurity.recoder.module;
import com.homesecurity.recoder.lib.NetSDKLib.LLong;

public class PlayModule {

    public static LLong startRealPlay(int channel, int stream, int id, String time) {

        LLong playHandle = LoginModule.netsdk.CLIENT_RealPlayEx(LoginModule.loginHandle, channel, null, stream);
        if(playHandle.longValue() == 0) {
            System.err.println("Failed to start real-time monitoring, error code: " + LoginModule.netsdk.CLIENT_GetLastError());
            stopRealPlay(playHandle);
        } else {
            System.out.println("Success to start real-play");
            System.out.println(id+"] "+playHandle);
            String output = "C:\\Users\\yasas\\Desktop\\New folder\\video_"+time+""+(id==0?".dav":"_dat");
            LoginModule.netsdk.CLIENT_SaveRealData(playHandle,output);
        }

        return playHandle;
    }

    public static void stopRealPlay(LLong playHandle){
        if (playHandle.longValue()==0){
            return;
        }
        LoginModule.netsdk.CLIENT_StopSaveRealData(playHandle);
        boolean bRet = LoginModule.netsdk.CLIENT_StopRealPlayEx(playHandle);
        if (bRet){
            playHandle.setValue(0);
        }
    }
}
