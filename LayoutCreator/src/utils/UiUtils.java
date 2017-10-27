package utils;

import javax.swing.*;
import java.awt.*;

public class UiUtils {
    public static void centerDialog(JDialog dialog, int width, int height) {
        dialog.setPreferredSize(new Dimension(width, height));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //获取屏幕的尺寸
        int screenWidth = screenSize.width; //获取屏幕的宽
        int screenHeight = screenSize.height; //获取屏幕的高
        dialog.setLocation(200, 100);//设置窗口居中显示
    }
}
