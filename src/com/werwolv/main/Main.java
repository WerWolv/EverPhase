package com.werwolv.main;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import com.werwolv.entity.Camera;
import com.werwolv.entity.Entity;
import com.werwolv.input.KeyListener;
import com.werwolv.model.ModelTextured;
import com.werwolv.render.ModelLoader;
import com.werwolv.render.Renderer;
import com.werwolv.resource.TextureModel;
import com.werwolv.shader.ShaderStatic;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.nio.IntBuffer;

public class Main {

    private Thread thread;
    private boolean running = false;

    private static long window;

    private GLFWKeyCallback keyCallback;
    private ModelLoader loader = new ModelLoader();
    private float[] vertices = { -0.5F, 0.5F, 0F, -0.5F, -0.5F, 0F, 0.5F, -0.5F, 0F, 0.5F, 0.5F, 0F };
    private int[] indices = { 0, 1, 3, 3, 1, 2};
    private float[] textureCoords = {0, 0, 0, 1, 1, 1, 1, 0 };

    private ShaderStatic shader;
    private Entity entity;
    private Renderer renderer;
    private Camera camera;

    public static void main(String[] args) {
        Main game = new Main();
        game.start();
    }

    private void start() {
        running = true;
        thread = new Thread(this::run, "GameRunner");
        thread.start();
    }

    private void init() {
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

        shader = new ShaderStatic();
        entity = new Entity(new ModelTextured(loader.loadToVAO(vertices, textureCoords, indices), new TextureModel(loader.loadTexture("texture"))), new Vector3f(0, 0, -1), 0, 0, 0, 1);

        renderer = new Renderer(shader);
        camera = new Camera();
    }

    private void update() {
        glfwPollEvents();

        if(KeyListener.keys[GLFW_KEY_SPACE]) System.out.println("Test");
    }

    private void render() {

        glfwSwapBuffers(window);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        camera.moveCamera();

        shader.start();
        shader.loadViewMatrix(camera);
        renderer.renderModel(entity, shader);
        shader.stop();
    }

    private void run() {
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
        shader.clean();
    }

    public static int[] getWindowSize() {
        IntBuffer w = BufferUtils.createIntBuffer(4);
        IntBuffer h = BufferUtils.createIntBuffer(4);

        glfwGetWindowSize(window, w, h);

        return new int[] { w.get(0), h.get(0) };
    }

}
