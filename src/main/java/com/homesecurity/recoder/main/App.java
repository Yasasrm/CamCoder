package com.homesecurity.recoder.main;

import com.homesecurity.recoder.lib.NetSDKLib;
import com.homesecurity.recoder.lib.NetSDKLib.LLong;
import com.homesecurity.recoder.module.LoginModule;
import com.homesecurity.recoder.module.PlayModule;
import com.sun.jna.Pointer;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

/**
 * Recorder
 */
public class App {
    private static int j = 0;

    public static void main(String[] args) {
        try {
            boolean run = programConfig();
            FileReader reader = new FileReader("configuration.properties");
            Properties p = new Properties();
            p.load(reader);
            String camIp = p.getProperty("ip");
            int camPort = Integer.parseInt(p.getProperty("port"));
            String camUser = p.getProperty("user");
            String camPassword = p.getProperty("pass");
            String vdoPath = p.getProperty("path");
            int sec = Integer.parseInt(p.getProperty("sec"));
            PlayModule playModule = new PlayModule();
            while (run) {
                System.out.println(LoginModule.init(DisConnectCallBack.getInstance(), HaveReConnectCallBack.getInstance()) ? "Initialization Success!" : "Initialization Failed!");
                LoginModule.login(camIp, camPort, camUser, camPassword);
                LLong playHandle = playModule.startRealPlay(0, 0);
                while (playHandle.longValue() != 0) {
                    LocalDateTime ldt = LocalDateTime.now();
                    String timeSubFldr = DateTimeFormatter.ofPattern("HH").format(ldt);
                    String timeFldr = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(ldt);
                    String time = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH.mm.ss").format(ldt);
                    System.out.print("Recording Started : ["+time+"]");
                    createIfNotExist(vdoPath, timeFldr, timeSubFldr);
                    String output = vdoPath + "\\" + timeFldr + "\\" + timeSubFldr + "\\video_" + j + ".dav";

                    Date d1 = new Date();
                    while (getSecondsPass(d1,new Date()) < sec) {
                        playModule.saveVideo(playHandle, output);
                    }
                    System.out.println();
                    playModule.stopSaveVideo(playHandle);
                    System.out.println("Video Saved : ["+output+"]");
                    j++;
                }
                if (playHandle.longValue() == 0) {
                    LoginModule.logout();
                    LoginModule.cleanup();
                    break;
                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean programConfig() {
        System.out.println("Press (x) to change settings. / any key to start the program.");
        Scanner sc = new Scanner(System.in);
        String condition = sc.nextLine().trim();
        if ("x".equals(condition) || "X".equals(condition)) {
            System.out.println("Please enter camera ip:");
            String ip = sc.nextLine().trim();
            System.out.println("Please enter camera port:");
            String port = sc.nextLine().trim();
            System.out.println("Please enter camera username:");
            String user = sc.nextLine().trim();
            System.out.println("Please enter camera password:");
            String pass = sc.nextLine().trim();
            System.out.println("Please enter path to save videos:");
            String path = sc.nextLine().trim();
            System.out.println("Please enter video length (Seconds):");
            String sec = sc.nextLine().trim();
            Properties p = new Properties();
            p.setProperty("ip", ip);
            p.setProperty("port", port);
            p.setProperty("user", user);
            p.setProperty("pass", pass);
            p.setProperty("path", path);
            p.setProperty("sec", sec);
            try {
                p.store(new FileWriter("configuration.properties"), "CCTV Camera Configurations");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sc.close();
        return true;
    }

    private static void createIfNotExist(String vdoPath, String timeFldr, String timeSubFldr) throws IOException {
        Path path1 = Paths.get(vdoPath);
        Path path2 = Paths.get(vdoPath + "\\" + timeFldr);
        Path path3 = Paths.get(vdoPath + "\\" + timeFldr + "\\" + timeSubFldr);
        if (!Files.exists(path3)) {
            j = 0;
        }
        Files.createDirectories(path1);
        Files.createDirectories(path2);
        Files.createDirectories(path3);
    }

    private static class DisConnectCallBack implements NetSDKLib.fDisConnect {

        private DisConnectCallBack() {
        }

        private static class CallBackHolder {
            private static DisConnectCallBack instance = new DisConnectCallBack();
        }

        public static DisConnectCallBack getInstance() {
            return CallBackHolder.instance;
        }

        public void invoke(NetSDKLib.LLong lLoginID, String pchDVRIP, int nDVRPort, Pointer dwUser) {
            System.out.printf("Device[%s] Port[%d] DisConnect!\n", pchDVRIP, nDVRPort);
        }
    }


    private static class HaveReConnectCallBack implements NetSDKLib.fHaveReConnect {
        private HaveReConnectCallBack() {
        }

        private static class CallBackHolder {
            private static HaveReConnectCallBack instance = new HaveReConnectCallBack();
        }

        public static HaveReConnectCallBack getInstance() {
            return CallBackHolder.instance;
        }

        public void invoke(NetSDKLib.LLong m_hLoginHandle, String pchDVRIP, int nDVRPort, Pointer dwUser) {
            System.out.printf("ReConnect Device[%s] Port[%d]\n", pchDVRIP, nDVRPort);
        }

    }

    private static int getSecondsPass(Date d1, Date d2){
        return (int)(d2.getTime()-d1.getTime())/1000;
    }
}
