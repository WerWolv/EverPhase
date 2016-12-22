package com.werwolv.main;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import com.werwolv.entity.EntityCamera;
import com.werwolv.entity.Entity;
import com.werwolv.entity.EntityLight;
import com.werwolv.input.KeyListener;
import com.werwolv.model.ModelTextured;
import com.werwolv.render.RendererMaster;
import com.werwolv.render.ModelLoader;
import com.werwolv.render.OBJModelLoader;
import com.werwolv.resource.TextureModel;
import com.werwolv.terrain.Terrain;
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

    private RendererMaster renderer;

    private Entity entity;
    private EntityCamera camera;
    private EntityLight light;

    private Terrain terrain;

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

        renderer = new RendererMaster();

        TextureModel texture = new TextureModel(loader.loadTexture("models/dragon"));
        texture.setShineDamper(10);
        texture.setReflectivity(1);
        entity = new Entity(new ModelTextured(OBJModelLoader.loadObjModel("dragon", loader), texture), new Vector3f(0, 0, -10), 0, 60, 0, 1);

        camera = new EntityCamera();
        light = new EntityLight(new Vector3f(20, 20, 0), new Vector3f(0.5F, 0.5F, 0.5F));

        terrain = new Terrain(0, 0, loader, new TextureModel(loader.loadTexture("texture")));
    }

    private void update() {
        glfwPollEvents();
    }

    private void render() {
        glfwSwapBuffers(window);
        camera.moveCamera();
        entity.increaseRotation(0, 0.5F, 0);

        renderer.processTerrains(terrain);
        renderer.processEntity(entity);

        renderer.render(light, camera);


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

        renderer.clean();
        keyCallback.free();
        loader.clean();
    }

    public static int[] getWindowSize() {
        IntBuffer w = BufferUtils.createIntBuffer(4);
        IntBuffer h = BufferUtils.createIntBuffer(4);

        glfwGetWindowSize(window, w, h);

        return new int[] { w.get(0), h.get(0) };
    }

}
