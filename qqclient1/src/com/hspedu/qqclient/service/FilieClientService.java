package com.hspedu.qqclient.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;

import java.io.*;

public class FilieClientService {
    public void sendFileToOne(String src, String dest, String sendId, String getterId) {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_FILE_MES);
        message.setSender(sendId);
        message.setGetter(getterId);
        message.setSrc(src);
        message.setDest(dest);

        FileInputStream fileInputStream = null;
        byte fileBytes[] = new byte[(int) new File(src).length()];
        try {
            fileInputStream = new FileInputStream(src);
            fileInputStream.read(fileBytes);
            message.setFileByte(fileBytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("\n" + sendId + "给" + getterId + "发送文件" + src + "到对方的电脑的目录" + dest);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(sendId).getsocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
