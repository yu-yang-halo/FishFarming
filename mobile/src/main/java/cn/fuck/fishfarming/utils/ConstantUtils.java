package cn.fuck.fishfarming.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/7.
 */

public class ConstantUtils {
    public static Map<String,String> CONTENTS=new HashMap<>();
    public static Map<String,String> UNITS   =new HashMap<>();

    static {

        CONTENTS.put("1","溶氧");
        CONTENTS.put("2","溶氧饱和度");
        CONTENTS.put("3","PH");
        CONTENTS.put("4","氨氮");
        CONTENTS.put("5","温度");
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



    }




}
