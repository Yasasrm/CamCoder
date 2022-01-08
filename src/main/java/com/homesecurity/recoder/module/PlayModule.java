package com.homesecurity.recoder.module;
import com.homesecurity.recoder.lib.NetSDKLib.LLong;

public class PlayModule {

    public static LLong startRealPlay(int channel, int stream, String output) {

        LLong playHandle = LoginModule.netsdk.CLIENT_RealPlayEx(LoginModule.loginHandle, channel, null, stream);
        if(playHandle.longValue() == 0) {
            stopRealPlay(playHandle);
        } else {
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
