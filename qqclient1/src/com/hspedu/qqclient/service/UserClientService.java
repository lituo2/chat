package com.hspedu.qqclient.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;
import com.hspedu.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class UserClientService {
    private User user = new User();
    private Socket socket;

    public boolean checkUser(String userId, String pwd) {
        boolean b = false;
        user.setUserId(userId);
        user.setUserwd(pwd);

        try {
            Socket socket = new Socket(InetAddress.getLocalHost(), 9999);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message ms = (Message) ois.readObject();

            if (ms.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)) {

                ClinentConnectServerThread clinentConnectServerThread = new ClinentConnectServerThread(socket);
                clinentConnectServerThread.start();

                ManageClientConnectServerThread.addClientConnectServerThread(userId, clinentConnectServerThread);
                b = true;
            } else {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return b;
    }

    public void onlineFriendList() {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(user.getUserId());

        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(user.getUserId()).getsocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void logout() {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(user.getUserId());

        try {
            //错误了ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(user.getUserId()).getsocket().getOutputStream());
            oos.writeObject(message);
            System.out.println(user.getUserId() + "退出了系统");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
