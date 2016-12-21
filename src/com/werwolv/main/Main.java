package com.werwolv.main;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import com.werwolv.entity.EntityCamera;
import com.werwolv.entity.Entity;
import com.werwolv.entity.EntityLight;
import com.werwolv.input.KeyListener;
import com.werwolv.model.ModelTextured;
import com.werwolv.render.ModelLoader;
import com.werwolv.render.OBJModelLoader;
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

    private ShaderStatic shader;
    private Entity entity;
    private Renderer renderer;
    private EntityCamera camera;
    private EntityLight light;

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
        TextureModel texture = new TextureModel(loader.loadTexture("models/dragon"));
        texture.setShineDamper(10);
        texture.setReflectivity(1);
        entity = new Entity(new ModelTextured(OBJModelLoader.loadObjModel("dragon", loader), texture), new Vector3f(0, 0, -10), 0, 60, 0, 1);

        renderer = new Renderer(shader);
        camera = new EntityCamera();
        light = new EntityLight(new Vector3f(20, 20, 0), new Vector3f(1, 1, 1));
    }

    private void update() {
        glfwPollEvents();

        if(KeyListener.keys[GLFW_KEY_SPACE]) System.out.println("Test");
    }

    private void render() {

        glfwSwapBuffers(window);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        entity.increaseRotation(0, 1, 0);

        camera.moveCamera();

        shader.start();
        shader.loadLight(light);
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
