package cn.fuck.fishfarming.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.fuck.fishfarming.activity.ui.ServerListUi;

/**
 * Created by Administrator on 2016/12/7.
 */

public class ConstantUtils {

    public static Map<String,String> CONTENTS=new HashMap<>();
    public static Map<String,String> CONTENTS2=new HashMap<>();
    public static Map<String,String> UNITS   =new HashMap<>();
    public static Map<String,Integer> ORDERS  =new HashMap<>();//排序使用
    public static Map<String,Integer> MAXVALUES=new HashMap<>();

    public static List<ServerItem> SERVERS = new ArrayList<>();

    public static class ServerItem
    {
        public String itemName;
        public String itemServer;
        public int    itemId;

    }


    static {

        CONTENTS.put("a1","溶氧");
        CONTENTS.put("a2","溶氧饱和度");
        CONTENTS.put("a3","PH");
        CONTENTS.put("a4","氨氮");
        CONTENTS.put("a5","水温");
        CONTENTS.put("a6","亚硝酸盐");
        CONTENTS.put("a7","液位");
        CONTENTS.put("a8","硫化氢");
        CONTENTS.put("a9","浊度");
        CONTENTS.put("a10","盐度");
        CONTENTS.put("a11","电导率");
        CONTENTS.put("a12","化学需量");
        CONTENTS.put("a0A","盐度");
        CONTENTS.put("a0B","电导率");
        CONTENTS.put("a0C","化学需量");


        MAXVALUES.put("a1",50);
        MAXVALUES.put("a2",50);
        MAXVALUES.put("a3",18);
        MAXVALUES.put("a4",29);
        MAXVALUES.put("a5",50);
        MAXVALUES.put("a6",50);
        MAXVALUES.put("a7",50);
        MAXVALUES.put("a8",50);
        MAXVALUES.put("a9",50);
        MAXVALUES.put("a10",50);
        MAXVALUES.put("a11",50);
        MAXVALUES.put("a12",50);
        MAXVALUES.put("a0A",50);
        MAXVALUES.put("a0B",50);
        MAXVALUES.put("a0C",50);


        CONTENTS2.put("1","溶氧");
        CONTENTS2.put("2","溶氧饱和度");
        CONTENTS2.put("3","PH");
        CONTENTS2.put("4","氨氮");
        CONTENTS2.put("5","水温");
        CONTENTS2.put("6","亚硝酸盐");
        CONTENTS2.put("7","液位");
        CONTENTS2.put("8","硫化氢");
        CONTENTS2.put("9","浊度");
        CONTENTS2.put("10","盐度");
        CONTENTS2.put("11","电导率");
        CONTENTS2.put("12","化学需量");



        UNITS.put("a1","mg/L");
        UNITS.put("a2","%");
        UNITS.put("a3"," ");
        UNITS.put("a4","mg/L");
        UNITS.put("a5","℃");
        UNITS.put("a6","mg/L");
        UNITS.put("a7"," ");
        UNITS.put("a8","mg/L");
        UNITS.put("a9","mg/L");
        UNITS.put("a10","mg/L");
        UNITS.put("a11"," ");
        UNITS.put("a12"," ");


        ORDERS.put("a1",1);
        ORDERS.put("a2",7);
        ORDERS.put("a3",3);
        ORDERS.put("a4",4);
        ORDERS.put("a5",2);
        ORDERS.put("a6",5);
        ORDERS.put("a7",8);
        ORDERS.put("a8",9);
        ORDERS.put("a9",6);
        ORDERS.put("a10",10);
        ORDERS.put("a11",11);
        ORDERS.put("a12",12);





        ServerItem item0 = new ServerItem();
        item0.itemName = "合肥云服务中心";
        item0.itemServer = "socket.tldwlw.com:8443";
        item0.itemId   = 0;

        ServerItem item1 = new ServerItem();
        item1.itemName = "河北云服务中心";
        item1.itemServer = "cdymj-service.tldwlw.com:8888";
        item1.itemId   = 1;



        SERVERS.add(item0);
        SERVERS.add(item1);

    }




}
