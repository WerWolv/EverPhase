package com.werwolv.main;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import com.werwolv.entity.EntityPlayer;
import com.werwolv.entity.Entity;
import com.werwolv.entity.EntityLight;
import com.werwolv.fbo.FrameBufferMinimap;
import com.werwolv.fbo.FrameBufferObject;
import com.werwolv.fbo.FrameBufferWater;
import com.werwolv.gui.Gui;
import com.werwolv.input.CursorListener;
import com.werwolv.input.KeyListener;
import com.werwolv.input.MouseListener;
import com.werwolv.render.RendererGui;
import com.werwolv.render.RendererMaster;
import com.werwolv.render.ModelLoader;
import com.werwolv.resource.TextureModel;
import com.werwolv.resource.TextureTerrain;
import com.werwolv.resource.TextureTerrainPack;
import com.werwolv.terrain.Terrain;
import com.werwolv.terrain.TileWater;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static long lastFrameTime;
    private static float delta;

    private final int TICKS_PER_SECOND = 50;
    private final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
    private final int MAX_FRAMESKIP = 5;

    private Thread thread;
    private boolean running = false;

    private static long window;

    private KeyListener keyCallback;
    private MouseListener mouseButtonCallback;
    private CursorListener cursorPosCallback;

    private ModelLoader loader = new ModelLoader();

    private RendererMaster renderer;
    private RendererGui guiRenderer;

    private Entity entity;

    private List<Entity> entities = new ArrayList<>();
    private List<Terrain> terrains = new ArrayList<>();
    private List<TileWater> waters = new ArrayList<>();
    private List<EntityLight> lights = new ArrayList<>();
    private static EntityPlayer player;

    private Terrain terrain;

    private TextureTerrain bgTexture;
    private TextureTerrain rTexture;
    private TextureTerrain gTexture;
    private TextureTerrain bTexture;
    private TextureTerrain blendMap;

    private TextureTerrainPack textureTerrainPack;

    private List<Gui> guis = new ArrayList<>();

    private FrameBufferObject fboMiniMap, fboWater;

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

        renderer = new RendererMaster(loader);
        guiRenderer = new RendererGui(loader);

        fboMiniMap = new FrameBufferMinimap();
        fboWater = renderer.getFboWater();
    }

    private void addContent() {

        TextureModel texture = new TextureModel(loader.loadTexture("white"));
        texture.setShineDamper(10);
        texture.setReflectivity(1);
        entity = new Entity(loader,"dragon", "white", new Vector3f(0, 0, -10), new Vector3f(0, 60, 0), 1);

        player = new EntityPlayer(new Vector3f(0, 10, 0), new Vector3f(0, 0, 0), 1);

        bgTexture = new TextureTerrain(loader.loadTexture("grassy"));
        rTexture = new TextureTerrain(loader.loadTexture("dirt"));
        gTexture = new TextureTerrain(loader.loadTexture("path"));
        bTexture = new TextureTerrain(loader.loadTexture("pinkFlowers"));

        textureTerrainPack = new TextureTerrainPack(bgTexture, rTexture, gTexture, bTexture);
        blendMap = new TextureTerrain(loader.loadTexture("blendMap"));

        terrain = new Terrain(0, -1, loader, textureTerrainPack, blendMap, "heightmap");

        Gui minmapGui = new Gui(((FrameBufferMinimap) fboMiniMap).getMiniMapTexture(),  new Vector2f(0.85F, 0.75F), new Vector2f(0.30F, 0.30F));
        guis.add(minmapGui);

        lights.add(new EntityLight(new Vector3f(-100, 10, -100), new Vector3f(1, 0, 0), new Vector3f(1, 0.01F, 0.002F)));
        lights.add(new EntityLight(new Vector3f(-50, 10, -100), new Vector3f(0, 1, 0), new Vector3f(1, 0.01F, 0.002F)));
        lights.add(new EntityLight(new Vector3f(-20, 10, -75), new Vector3f(0, 0, 1), new Vector3f(1, 0.01F, 0.002F)));
        lights.add(new EntityLight(new Vector3f(50, 10, -100), new Vector3f(1, 1, 0), new Vector3f(1, 0.01F, 0.002F)));
        lights.add(new EntityLight(new Vector3f(30, 10, 30), new Vector3f(1, 0, 1), new Vector3f(1, 0.01F, 0.002F)));

        entities.add(entity);
        terrains.add(terrain);

        waters.add(new TileWater(75, -75, 0));
    }

    private void renderMinimap() {
        ((FrameBufferMinimap) fboMiniMap).bindMinimapFrameBuffer();

        float lastPitch = player.getPitch();
        Vector3f playerPos = player.getPosition();
        player.setPitch(90.0F);
        player.setPosition(new Vector3f(playerPos.x, terrain.getHeightOfTerrain(playerPos.x, playerPos.z) + 100.0F, playerPos.z));
        renderer.getRendererWater().renderWithoutEffects(waters);
        renderer.renderScene(entities, terrains, lights, player, new Vector4f(0, -1, 0, 1000));
        player.setPosition(playerPos);
        player.setPitch(lastPitch);

        fboMiniMap.unbindCurrentFrameBuffer();
    }

    private void renderWaterEffects() {
        glEnable(GL30.GL_CLIP_DISTANCE0);

        ((FrameBufferWater) fboWater).bindReflectionFrameBuffer();

        float distance = 2 * (player.getPosition().y - waters.get(0).getHeight());
        player.setPosition(new Vector3f(player.getPosition().x, player.getPosition().y - distance, player.getPosition().z));
        player.setPitch(-player.getPitch());

        renderer.renderScene(entities, terrains, lights, player, new Vector4f(0, 1, 0, -waters.get(0).getHeight()));

        player.setPosition(new Vector3f(player.getPosition().x, player.getPosition().y + distance, player.getPosition().z));
        player.setPitch(-player.getPitch());

        ((FrameBufferWater) fboWater).bindRefractionFrameBuffer();
        renderer.renderScene(entities, terrains, lights, player, new Vector4f(0, -1, 0, waters.get(0).getHeight()));
        GL11.glDisable(GL30.GL_CLIP_DISTANCE0);

        fboWater.unbindCurrentFrameBuffer();

    }

    private void update() {
        glfwPollEvents();
        handleInput();
        player.move(terrain);

        long currFrameTime = getCurrentTime();
        delta = (currFrameTime - lastFrameTime) / 1000.0F;
        lastFrameTime = currFrameTime;
    }

    private void render() {
        glfwSwapBuffers(window);
        entity.increaseRotation(0, 0.5F, 0);

        renderWaterEffects();
        renderMinimap();

        renderer.renderScene(entities, terrains, lights, player, new Vector4f(0, -1, 0, 100000));
        renderer.getRendererWater().render(waters, lights, player);


        guiRenderer.render(guis);

    }

    private void handleInput() {


        if(KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) System.exit(0);
    }

    private void run() {
        int loops;
        double nextGameTick = System.currentTimeMillis();

        init();
        addContent();
        while(running) {
            loops = 0;
            while(System.currentTimeMillis() > nextGameTick && loops < MAX_FRAMESKIP) {
                update();
                render();

                nextGameTick += SKIP_TICKS;
                loops++;
            }
            if(glfwWindowShouldClose(window)) {
                running = false;
            }
        }


        renderer.clean();
        guiRenderer.clean();
        keyCallback.free();
        loader.clean();
    }

    public static int[] getWindowSize() {
        IntBuffer w = BufferUtils.createIntBuffer(4);
        IntBuffer h = BufferUtils.createIntBuffer(4);

        glfwGetWindowSize(window, w, h);

        return new int[] { w.get(0), h.get(0) };
    }

    private static long getCurrentTime() {
        return (long)(GLFW.glfwGetTime() * 1000.0F);
    }

    public static float getFrameTimeSeconds() {
        return delta;
    }

    public static EntityPlayer getPlayer() {
        return player;
    }
}
