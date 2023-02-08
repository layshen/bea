package com.epiboly.bea.util;

import com.epiboly.bea.rich.R;

/**
 * @author vemao
 * @time 2023/2/8
 * @describe
 */
public class NodeHelper {
    public static int getDrawableByNodeType(int type){
        if (type == 0){
            return R.drawable.node_png_01;
        } else if (type == 1){
            return R.drawable.node_png_02;
        }else if (type == 2){
            return R.drawable.node_png_03;
        }else if (type == 3){
            return R.drawable.node_png_04;
        }else if (type == 4){
            return R.drawable.node_png_05;
        }else if (type == 5){
            return R.drawable.node_png_06;
        }else{
            return R.drawable.node_png_06;
        }
    }

    public static String getNameByType(int nid) {
        switch (nid){
            case 0:
                return "T级节点";
            case 1:
                return "A级节点";
            case 2:
                return "B级节点";
            case 3:
                return "C级节点";
            case 4:
                return "D级节点";
            case 5:
                return "E级节点";
            case 6:
                return "F级节点";
        }
        return "未知";
    }
}
