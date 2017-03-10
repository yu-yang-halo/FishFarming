package cn.fuck.fishfarming.utils;

import android.view.View;

/**
 * Created by Administrator on 2017/2/23 0023.
 */

public class ViewFindUtils {
    public static  <T extends View>  T find(View containerView,int id){
        return  (T)containerView.findViewById(id);
    }
}
