package com.hspedu.qqclient.view;

import com.hspedu.qqclient.service.FilieClientService;
import com.hspedu.qqclient.service.MessageClinentService;
import com.hspedu.qqclient.service.UserClientService;
import com.hspedu.qqclient.utils.Utility;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

public class QQView {
    private boolean loop = true;
    private String key = "";
    private UserClientService userClientService = new UserClientService();
    private MessageClinentService messageClinentService = new MessageClinentService();
    private FilieClientService filieClientService = new FilieClientService();

    public static void main(String[] args) {
        new QQView().mainMenu();
        System.out.println("客户端登陆失败");
    }

    public void mainMenu() {
        while (loop) {
            System.out.println("=============欢迎登录网络通信系统==============");
            System.out.println("\t\t1.登陆系统");
            System.out.println("\t\t9.退出系统");
            System.out.println("请输入你的选择：");

            key = Utility.readString(1);

            switch (key) {
                case "1":
                    System.out.println("请输入用户名：");
                    String userId = Utility.readString(50);
                    System.out.println("请输入密码：");
                    String pwd = Utility.readString(50);
                    if (userClientService.checkUser(userId, pwd)) {
                        System.out.println("欢迎" + userId + "登陆成功");
                        while (loop) {
                            System.out.println("===================网络通讯系统二级菜单（用户" + userId + ")");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出系统");
                            System.out.println("请输入你的选择：");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1":
                                    userClientService.onlineFriendList();
                                    break;
                                case "2":
                                    System.out.println("请输入想对大家说的话：");
                                    String s = Utility.readString(100);
                                    messageClinentService.sendMessageToAll(s, userId);
                                    break;
                                case "3":
                                    System.out.println("请输入想聊天的在线用户：");
                                    String getterId = Utility.readString(50);
                                    System.out.println("请输入想要输入的话：");
                                    String contend = Utility.readString(100);
                                    messageClinentService.sendMessageToOne(contend, userId, getterId);

                                    System.out.println("私聊消息");
                                    break;
                                case "4":
                                    System.out.println("请输入你想发送文件的用户（在线用户）");
                                    getterId = Utility.readString(50);
                                    System.out.println("请输入发送文件的路径格式");
                                    String src = Utility.readString(100);
                                    System.out.println("请输入你想发送到对应的路径格式");
                                    String dest = Utility.readString(100);
                                    filieClientService.sendFileToOne(src, dest, userId, getterId);
                                    break;
                                case "9":
                                    userClientService.logout();
                                    loop = false;
                                    break;
                            }
                        }

                    } else {
                        System.out.println("服务器登陆失败");
                    }

                    break;
                case "9":
                    loop = false;
                    break;
            }

        }
    }
}
