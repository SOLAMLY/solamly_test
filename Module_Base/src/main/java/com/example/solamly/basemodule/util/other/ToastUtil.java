package com.example.solamly.basemodule.util.other;

import android.content.Context;
import android.widget.Toast;

import com.example.solamly.basemodule.BaseModelApplication;

/**
 * @Author SOLAMLY
 * @Date 2018/9/20 14:38
 * @Description:
 */

public class ToastUtil {
    public static void ToastShow(Context context,String content){
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public static void ToastShow(String content){
        Toast.makeText(BaseModelApplication.getContext(), content, Toast.LENGTH_SHORT).show();
    }
}
