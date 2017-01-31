package com.deltabase.everphase.engine.toolbox;

import com.deltabase.everphase.main.Main;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ScreenShotHelper {

    public static void takeScreenShot() {
        int width = Main.getWindowSize()[0];
        int height = Main.getWindowSize()[1];

        File file = new File("screenshots/" + System.currentTimeMillis() + ".png");
        file.mkdirs();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        GL11.glReadBuffer(GL11.GL_FRONT);
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
        GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);


        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                int i = (x + (width * y)) * 4;
                int r = buffer.get(i) & 0xFF;
                int g = buffer.get(i + 1) & 0xFF;
                int b = buffer.get(i + 2) & 0xFF;
                image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
            }
        }

        try {
            ImageIO.write(image, "png", file);
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

}
