package com.deltabase.everphase.main;

import com.deltabase.everphase.api.EverPhaseApi;
import com.deltabase.everphase.api.Log;
import com.deltabase.everphase.api.localization.LocalizationUtil;
import com.deltabase.everphase.callback.CursorPositionCallback;
import com.deltabase.everphase.callback.KeyCallback;
import com.deltabase.everphase.callback.MouseButtonCallback;
import com.deltabase.everphase.callback.ScrollCallback;
import com.deltabase.everphase.engine.audio.AudioHelper;
import com.deltabase.everphase.level.LevelOverworld;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.WGLEXTSwapControl.wglSwapIntervalEXT;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {

    private static KeyCallback keyCallback;
    private static MouseButtonCallback mouseButtonCallback;
    private static CursorPositionCallback cursorPosCallback;
    private static ScrollCallback scrollCallback;

    private static long lastFrameTime;
    private static float delta;
    private static long window;


    public static void main(String[] args) {
        for (String arg : args) {
            switch (arg.toLowerCase()) {
                case "fullscreen":
                    Settings.fullscreen = true;
                    Log.i("Settings", "Starting in fullscreen mode");
                    break;
                case "vsync":
                    Settings.vSync = true;
                    Log.i("Settings", "VSync enabled");
                    break;
                case "antialiasing":
                    Settings.antialiasing = true;
                    Log.i("Settings", "Anti aliasing enabled");
                    break;
                case "anisotropicfilter":
                    Settings.anisotropicFilter = true;
                    Log.i("Settings", "Anisotropic filtering enabled");
                    break;
                case "bloom":
                    Settings.bloom = true;
                    Log.i("Settings", "Bloom filter enabled");
                    break;
                case "debug":
                    Settings.debugMode = true;
                    Log.i("Settings", "Debug mode enabled");
                    break;
            }

            if (arg.toLowerCase().startsWith("shadowquality")) {
                Settings.shadowQuality = (int) Math.pow(2, Integer.parseInt(arg.split("_")[1]));

                switch (Settings.shadowQuality) {
                    case 0:
                        Log.i("Settings", "Dynamic shadows disabled");
                        break;
                    case 1:
                        Log.i("Settings", "Low quality dynamic shadows enabled");
                        break;
                    case 2:
                        Log.i("Settings", "Medium quality dynamic shadows enabled");
                        break;
                    case 4:
                        Log.i("Settings", "High quality dynamic shadows enabled");
                        break;
                    case 8:
                        Log.i("Settings", "Ultra High quality dynamic shadows enabled");
                        break;
                }
            }

            else if(arg.toLowerCase().startsWith("mipmaplevel"))
                Settings.mipmappingLevel = Integer.parseInt(arg.split("_")[1]);
            else if(arg.toLowerCase().startsWith("mipmaptype")) {
                switch (Integer.parseInt(arg.split("_")[1])) {
                    case 1:
                        Settings.mipmappingType = GL_LINEAR;
                        Log.i("Settings", "Linear mipmapping enabled");
                        break;
                    case 2:
                        Settings.mipmappingType = GL_LINEAR_MIPMAP_NEAREST;
                        Log.i("Settings", "Bilinear mipmapping enabled");
                        break;
                    case 3:
                        Settings.mipmappingType = GL_LINEAR_MIPMAP_LINEAR;
                        Log.i("Settings", "Trilinear mipmapping enabled");
                        break;
                    default:
                        Settings.mipmappingType = GL_NONE;
                        Log.i("Settings", "Mipmapping disabled");
                        break;
                }
            }
        }

        run();
    }

    private static void init() {
        if (!glfwInit())
            Log.wtf("GLFW", "Initialization failed!");

        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());


        window = glfwCreateWindow(Settings.fullscreen ? vidmode.width() : 1080, Settings.fullscreen ? vidmode.height() : 720, "EverPhase", Settings.fullscreen ? glfwGetPrimaryMonitor() : NULL, NULL);

        if (window == NULL)
            Log.wtf("GLFW", "Could not create window!");

        glfwSetKeyCallback(window, keyCallback = new KeyCallback());
        glfwSetMouseButtonCallback(window, mouseButtonCallback = new MouseButtonCallback());
        glfwSetCursorPosCallback(window, cursorPosCallback = new CursorPositionCallback());
        glfwSetScrollCallback(window, scrollCallback = new ScrollCallback());
        glfwSetWindowShouldClose(window, false);

        glfwSetWindowSizeCallback(window, (window, width, height) -> {
            GL11.glViewport(0, 0, width, height);
            glfwSetWindowSize(window, width, height);
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
        EverPhaseApi.EVENT_BUS.registerEventHandlers();

        Log.i("OpenGL", "Version: " + glGetString(GL_VERSION));

        AudioHelper.createContext();
        AudioHelper.loadSoundFile("random");

        EverPhaseApi.RendererUtils.initRenderers();
    }

    public static void setCursorVisibility(boolean visible) {
        glfwSetInputMode(window, GLFW_CURSOR, visible ? GLFW_CURSOR_NORMAL : GLFW_CURSOR_DISABLED);
    }

    private static void run() {
        init();

        LocalizationUtil.loadFiles("lang");

        EverPhaseApi.getEverPhase().thePlayer.setPosition(new Vector3f(0, 0, 0));

        EverPhaseApi.getEverPhase().theLevel = new LevelOverworld();
        EverPhaseApi.getEverPhase().theLevel.initLevel();
        EverPhaseApi.getEverPhase().theLevel.applyPostProcessingEffects();

        while(true) {
            glfwSwapBuffers(window);

            AudioHelper.setListenerPosition();
            EverPhaseApi.EVENT_BUS.processEvents();
            EverPhaseApi.getEverPhase().theLevel.updateLevel();
            EverPhaseApi.getEverPhase().theLevel.handleInput();
            EverPhaseApi.getEverPhase().theLevel.renderLevel();
            EverPhaseApi.getEverPhase().theLevel.renderGUI();

            long currFrameTime = getCurrentTime();
            delta = (currFrameTime - lastFrameTime) / 1000.0F;
            lastFrameTime = currFrameTime;

            if(glfwWindowShouldClose(window)) {
                Log.i("EverPhase", "Window Closed!");
                break;
            }
        }


        glfwSetWindowShouldClose(window, false);
        glfwDestroyWindow(window);
        EverPhaseApi.getEverPhase().theLevel.clean();
        keyCallback.free();
        mouseButtonCallback.free();
        cursorPosCallback.free();
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
}
