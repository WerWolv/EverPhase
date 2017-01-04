package com.werwolv.main;

import com.werwolv.callback.CursorPositionCallback;
import com.werwolv.callback.KeyCallback;
import com.werwolv.callback.MouseButtonCallback;
import com.werwolv.entity.EntityPlayer;
import com.werwolv.entity.particle.ParticleManager;
import com.werwolv.level.Level;
import com.werwolv.level.LevelOverworld;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {

    public static KeyCallback keyCallback;
    public static MouseButtonCallback mouseButtonCallback;
    public static CursorPositionCallback cursorPosCallback;

    public static Level currentLevel;
    private static long lastFrameTime;
    private static float delta;
    private static long window;
    private static EntityPlayer player;
    private boolean running = true;

    public static void main(String[] args) {
        Main game = new Main();
        game.run();
    }

    public static void setCursorVisibility(boolean visible) {
        glfwSetInputMode(window, GLFW_CURSOR, visible ? GLFW_CURSOR_NORMAL : GLFW_CURSOR_DISABLED);
    }

    public static long getWindow() {
        return window;
    }

    public static int[] getWindowSize() {
        IntBuffer w = BufferUtils.createIntBuffer(4);
        IntBuffer h = BufferUtils.createIntBuffer(4);

        glfwGetWindowSize(window, w, h);

        return new int[]{w.get(0), h.get(0)};
    }

    public static float getAspectRatio() {
        return (float) getWindowSize()[0] / (float) getWindowSize()[1];
    }


    private static long getCurrentTime() {
        return (long) (GLFW.glfwGetTime() * 1000);
    }

    public static float getFrameTimeSeconds() {
        return delta;
    }

    public static EntityPlayer getPlayer() {
        return player;
    }

    public static void init() {
        if(!glfwInit()){
            System.err.println("GLFW initialization failed!");
        }

        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());


        window = glfwCreateWindow(vidmode.width(), vidmode.height(), "GameRunner", NULL, NULL);


        if(window == NULL) {
            System.err.println("Could not create window!");
        }

        glfwSetKeyCallback(window, keyCallback = new KeyCallback());
        glfwSetMouseButtonCallback(window, mouseButtonCallback = new MouseButtonCallback());
        glfwSetCursorPosCallback(window, cursorPosCallback = new CursorPositionCallback());

        glfwSetWindowPos(window, 0, 0);
        glfwMakeContextCurrent(window);

        glfwShowWindow(window);

        GL.createCapabilities();
        glClearColor(0.56F, 0.258F, 0.425F, 1.0F);

        glEnable(GL_DEPTH_TEST);

        System.out.println("OpenGL: " + glGetString(GL_VERSION));

        lastFrameTime = getCurrentTime();
        setCursorVisibility(false);
    }

    private void run() {
        init();
        player = new EntityPlayer(new Vector3f(0, 10, 0), new Vector3f(0, 0, 0), 1);

        currentLevel = new LevelOverworld(player);

        currentLevel.initLevel();
        while(running) {
            glfwSwapBuffers(window);

            currentLevel.updateLevel();
            currentLevel.renderLevel();
            currentLevel.renderGUI();

            long currFrameTime = getCurrentTime();
            delta = (currFrameTime - lastFrameTime) / 1000.0F;
            lastFrameTime = currFrameTime;

            if(glfwWindowShouldClose(window)) {
                running = false;
            }
        }

        currentLevel.clean();
        keyCallback.free();
        mouseButtonCallback.free();
        cursorPosCallback.free();
        ParticleManager.clean();
    }
}
