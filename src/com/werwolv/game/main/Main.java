package com.werwolv.game.main;

import com.werwolv.game.api.event.EventBus;
import com.werwolv.game.audio.AudioHelper;
import com.werwolv.game.callback.CursorPositionCallback;
import com.werwolv.game.callback.KeyCallback;
import com.werwolv.game.callback.MouseButtonCallback;
import com.werwolv.game.callback.ScrollCallback;
import com.werwolv.game.entity.EntityPlayer;
import com.werwolv.game.level.Level;
import com.werwolv.game.level.LevelOverworld;
import com.werwolv.game.modelloader.ResourceLoader;
import com.werwolv.launcher.Launcher;
import com.werwolv.launcher.TextAreaOutputStream;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.IntBuffer;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.WGLEXTSwapControl.wglSwapIntervalEXT;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {

    private static KeyCallback keyCallback;
    private static MouseButtonCallback mouseButtonCallback;
    private static CursorPositionCallback cursorPosCallback;
    private static ScrollCallback scrollCallback;

    private static Level currentLevel;
    private static long lastFrameTime;
    private static float delta;
    private static long window;
    private static EntityPlayer player;

    private ResourceLoader loader = new ResourceLoader();

    public void startGame(List<String> args) {
        for (String arg : args) {
            System.out.println(arg);
            switch (arg.toLowerCase()) {
                case "fullscreen":
                    Settings.fullscreen = true;
                    break;
                case "vsync":
                    Settings.vSync = true;
                    break;
                case "antialiasing":
                    Settings.antialiasing = true;
                    break;
                case "anisotropicfilter":
                    Settings.anisotropicFilter = true;
                    break;
                case "shadow":
                    Settings.shadows = true;
                    break;
                case "bloom":
                    Settings.bloom = true;
                    break;
            }
        }

        this.run();
    }

    public static void init() {
        if(!glfwInit()){
            System.err.println("GLFW initialization failed!");
        }

        glfwDestroyWindow(window);

        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());


        window = glfwCreateWindow(Settings.fullscreen ? vidmode.width() : 720, Settings.fullscreen ? vidmode.height() : 480, "GameRunner", Settings.fullscreen ? glfwGetPrimaryMonitor() : NULL, NULL);


        if(window == NULL) {
            System.err.println("Could not create window!");
        }

        glfwSetKeyCallback(window, keyCallback = new KeyCallback());
        glfwSetMouseButtonCallback(window, mouseButtonCallback = new MouseButtonCallback());
        glfwSetCursorPosCallback(window, cursorPosCallback = new CursorPositionCallback());
        glfwSetScrollCallback(window, scrollCallback = new ScrollCallback());
        glfwSetWindowShouldClose(window, false);

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

        if(Settings.antialiasing)
            glEnable(GL13.GL_MULTISAMPLE);

        if(Settings.vSync)
            wglSwapIntervalEXT(1);

        lastFrameTime = getCurrentTime();
        setCursorVisibility(false);
        EventBus.registerEventHandlers();

        System.out.println("OpenGL: " + glGetString(GL_VERSION));

        AudioHelper.createContext();
        AudioHelper.loadSoundFile("random");
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

        currentLevel.applyPostProcessingEffects();
        while(true) {
            glfwSwapBuffers(window);

            AudioHelper.setListener(player);
            EventBus.processEvents();
            currentLevel.updateLevel();
            currentLevel.handleInput();
            currentLevel.renderLevel();
            currentLevel.renderGUI();

            long currFrameTime = getCurrentTime();
            delta = (currFrameTime - lastFrameTime) / 1000.0F;
            lastFrameTime = currFrameTime;

            if(glfwWindowShouldClose(window)) {
                break;
            }
        }
        currentLevel.clean();
        keyCallback.free();
        mouseButtonCallback.free();
        cursorPosCallback.free();
        System.exit(1);
    }
}
