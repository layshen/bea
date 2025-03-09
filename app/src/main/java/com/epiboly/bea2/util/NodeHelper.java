package com.epiboly.bea2.util;

import com.epiboly.bea2.R;

/**
 * @author vemao
 * @time 2023/2/8
 * @describe
 */
public class NodeHelper {

    public static int getDrawableByNodeType(int type){
        if (type == 1){
            return R.drawable.node_png_01;
        } else if (type == 2){
            return R.drawable.node_png_02;
        }else if (type == 3){
            return R.drawable.node_png_03;
        }else if (type == 4){
            return R.drawable.node_png_04;
        }else if (type == 5){
            return R.drawable.node_png_05;
        }else if (type == 6){
            return R.drawable.node_png_06;
        }else{
            return R.drawable.node_png_07;
        }
    }

    public static String getNameByType(int nid) {
        switch (nid){
            case 1:
                return "T级节点";
            case 2:
                return "A级节点";
            case 3:
                return "B级节点";
            case 4:
                return "C级节点";
            case 5:
                return "D级节点";
            case 6:
                return "E级节点";
            case 7:
                return "F级节点";
        }
        return "未知";
    }
}
