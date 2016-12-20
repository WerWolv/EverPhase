package com.werwolv.main;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import com.werwolv.input.KeyListener;
import com.werwolv.render.ModelLoader;
import com.werwolv.render.RawModel;
import com.werwolv.render.Renderer;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

public class Main {

    private Thread thread;
    private boolean running = false;

    public long window;

    private GLFWKeyCallback keyCallback;
    private Renderer renderer = new Renderer();
    private ModelLoader loader = new ModelLoader();
    private float[] vertices = { -0.5F, 0.5F, 0F, -0.5F, -0.5F, 0F, 0.5F, -0.5F, 0F, 0.5F, 0.5F, 0F };
    private int[] indices = { 0, 1, 3, 3, 1, 2};

    private RawModel model;
    public static void main(String[] args) {
        Main game = new Main();
        game.start();
    }

    public void start() {
        running = true;
        thread = new Thread(this::run, "GameRunner");
        thread.start();
    }

    public void init() {
        if(!glfwInit()){
            System.err.println("GLFW initialization failed!");
        }

        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

        window = glfwCreateWindow(800, 600, "GameRunner", NULL, NULL);

        if(window == NULL) {
            System.err.println("Could not create window!");
        }

        glfwSetKeyCallback(window, keyCallback = new KeyListener());

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, 100, 100);
        glfwMakeContextCurrent(window);

        glfwShowWindow(window);

        GL.createCapabilities();
        glClearColor(0.56F, 0.258F, 0.425F, 1.0F);

        glEnable(GL_DEPTH_TEST);

        System.out.println("OpenGL: " + glGetString(GL_VERSION));
    }

    public void update() {
        glfwPollEvents();

        if(KeyListener.keys[GLFW_KEY_SPACE]) System.out.println("Test");
    }

    public void render() {
        glfwSwapBuffers(window);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


        renderer.renderModel(loader.loadToVAO(vertices, indices));
    }

    public void run() {
        init();

        while(running) {
            update();
            render();

            if(glfwWindowShouldClose(window)) {
                running = false;
            }
        }

        keyCallback.free();
        loader.clean();
    }

}
