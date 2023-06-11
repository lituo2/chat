package com.hspedu.qqclient.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class MessageClinentService {
    public void sendMessageToOne(String content, String senderId, String getterId) {

        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COMM_MES);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setContent(content);
        message.setSendTime(new java.util.Date().toString());
        System.out.println(senderId + "对" + getterId + "说了" + content);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getsocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendMessageToAll(String content, String senderId) {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_TOALL_MES);
        message.setSender(senderId);
        message.setContent(content);
        message.setSendTime(new java.util.Date().toString());
        System.out.println(senderId + "说了" + content);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getsocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
