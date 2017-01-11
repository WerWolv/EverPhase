package com.werwolv.main;

import com.werwolv.api.event.EventBus;
import com.werwolv.callback.CursorPositionCallback;
import com.werwolv.callback.KeyCallback;
import com.werwolv.callback.MouseButtonCallback;
import com.werwolv.entity.EntityPlayer;
import com.werwolv.entity.particle.ParticleManager;
import com.werwolv.level.Level;
import com.werwolv.level.LevelOverworld;
import com.werwolv.modelloader.ModelLoader;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {

    private static KeyCallback keyCallback;
    private static MouseButtonCallback mouseButtonCallback;
    private static CursorPositionCallback cursorPosCallback;

    private static Level currentLevel;
    private static long lastFrameTime;
    private static float delta;
    private static long window;
    private static EntityPlayer player;
    private static boolean fullscreen = false;
    private boolean running = true;

    private ModelLoader loader = new ModelLoader();

    public static void main(String[] args) {
        for (String arg : args) {
            System.out.println(arg);
            switch (arg) {
                case "fullscreen":
                    fullscreen = true;
                    break;
            }
        }

        Main game = new Main();
        game.run();
    }

    public static void init() {
        if(!glfwInit()){
            System.err.println("GLFW initialization failed!");
        }

        glfwDestroyWindow(window);

        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint(GLFW_SAMPLES, 8);

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());


        window = glfwCreateWindow(fullscreen ? vidmode.width() : 720, fullscreen ? vidmode.height() : 480, "GameRunner", fullscreen ? glfwGetPrimaryMonitor() : NULL, NULL);


        if(window == NULL) {
            System.err.println("Could not create window!");
        }

        glfwSetKeyCallback(window, keyCallback = new KeyCallback());
        glfwSetMouseButtonCallback(window, mouseButtonCallback = new MouseButtonCallback());
        glfwSetCursorPosCallback(window, cursorPosCallback = new CursorPositionCallback());

        glfwSetWindowSizeCallback(window, (window, width, height) -> {
            GL11.glViewport(0, 0, width, height);
            glfwSetWindowSize(window, width, height);
            currentLevel.reInitRenderer();
        });

        glfwSetWindowPos(window, 100, 100);
        glfwMakeContextCurrent(window);

        glfwShowWindow(window);

        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);

        lastFrameTime = getCurrentTime();
        setCursorVisibility(false);
        EventBus.registerEventHandlers();

        System.out.println("OpenGL: " + glGetString(GL_VERSION));
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

    private void run() {
        init();

        player = new EntityPlayer(loader, new Vector3f(0, 10, 0), new Vector3f(0, 0, 0), 1);

        currentLevel = new LevelOverworld(player);

        currentLevel.initLevel();
        while(running) {
            glfwSwapBuffers(window);

            EventBus.processEvents();
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
