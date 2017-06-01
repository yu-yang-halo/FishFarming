package cn.fuck.fishfarming.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/7.
 */

public class ConstantUtils {
    public static final int ERROR_CODE_SCOKET_COLOSE=0x1000;
    public static final int ERROR_CODE_SCOKET_LOGIN_TIMEOUT=0x1001;
    public static final int ERROR_CODE_SCOKET_READ_TIMEOUT=0x1002;

    public static Map<String,String> CONTENTS=new HashMap<>();
    public static Map<String,String> UNITS   =new HashMap<>();
    public static Map<String,Integer> ORDERS  =new HashMap<>();//排序使用

    static {

        CONTENTS.put("1","溶氧");
        CONTENTS.put("2","溶氧饱和度");
        CONTENTS.put("3","PH");
        CONTENTS.put("4","氨氮");
        CONTENTS.put("5","水温");
        CONTENTS.put("6","亚硝酸盐");
        CONTENTS.put("7","液位");
        CONTENTS.put("8","硫化氢");
        CONTENTS.put("9","浊度");
        CONTENTS.put("10","盐度");
        CONTENTS.put("11","电导率");
        CONTENTS.put("12","化学需量");



        UNITS.put("1","mg/L");
        UNITS.put("2","%");
        UNITS.put("3"," ");
        UNITS.put("4","mg/L");
        UNITS.put("5","℃");
        UNITS.put("6","mg/L");
        UNITS.put("7"," ");
        UNITS.put("8","mg/L");
        UNITS.put("9","mg/L");
        UNITS.put("10","mg/L");
        UNITS.put("11"," ");
        UNITS.put("12"," ");


        ORDERS.put("1",1);
        ORDERS.put("2",7);
        ORDERS.put("3",3);
        ORDERS.put("4",4);
        ORDERS.put("5",2);
        ORDERS.put("6",5);
        ORDERS.put("7",8);
        ORDERS.put("8",9);
        ORDERS.put("9",6);
        ORDERS.put("10",10);
        ORDERS.put("11",11);
        ORDERS.put("12",12);


    }




}
