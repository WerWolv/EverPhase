package com.werwolv.main;

import com.werwolv.entity.EntityPlayer;
import com.werwolv.entity.particle.ParticleManager;
import com.werwolv.input.CursorListener;
import com.werwolv.input.KeyListener;
import com.werwolv.input.MouseListener;
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

    private static long lastFrameTime;
    private static float delta;

    private boolean running = true;

    private static long window;

    public static KeyListener keyCallback;
    public static MouseListener mouseButtonCallback;
    public static CursorListener cursorPosCallback;

    private static EntityPlayer player;

    public static Level currentLevel;

    public static void main(String[] args) {
        Main game = new Main();
        game.run();
    }

    private void init() {
        if(!glfwInit()){
            System.err.println("GLFW initialization failed!");
        }

        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        window = glfwCreateWindow(vidmode.width(), vidmode.height(), "GameRunner", glfwGetPrimaryMonitor(), NULL);


        if(window == NULL) {
            System.err.println("Could not create window!");
        }

        glfwSetKeyCallback(window, keyCallback = new KeyListener());
        glfwSetMouseButtonCallback(window, mouseButtonCallback = new MouseListener());
        glfwSetCursorPosCallback(window, cursorPosCallback = new CursorListener());

        glfwSetWindowPos(window, 100, 100);
        glfwMakeContextCurrent(window);

        glfwShowWindow(window);

        GL.createCapabilities();
        glClearColor(0.56F, 0.258F, 0.425F, 1.0F);

        glEnable(GL_DEPTH_TEST);

        System.out.println("OpenGL: " + glGetString(GL_VERSION));

        lastFrameTime = getCurrentTime();
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

    public static int[] getWindowSize() {
        IntBuffer w = BufferUtils.createIntBuffer(4);
        IntBuffer h = BufferUtils.createIntBuffer(4);

        glfwGetWindowSize(window, w, h);

        return new int[] { w.get(0), h.get(0) };
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
}
