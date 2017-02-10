package com.deltabase.everphase.api;

import com.deltabase.everphase.achievement.Achievement;
import com.deltabase.everphase.api.crafting.CraftingRecipe;
import com.deltabase.everphase.api.event.EventBus;
import com.deltabase.everphase.api.event.advance.AchievementGetEvent;
import com.deltabase.everphase.engine.modelloader.ResourceLoader;
import com.deltabase.everphase.engine.render.*;
import com.deltabase.everphase.engine.render.shadow.RendererShadowMapMaster;
import com.deltabase.everphase.entity.EntityPlayer;
import com.deltabase.everphase.gui.Gui;
import com.deltabase.everphase.gui.inventory.GuiInventory;
import com.deltabase.everphase.inventory.Inventory;
import com.deltabase.everphase.level.Level;
import com.deltabase.everphase.main.Main;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class EverPhaseApi {

    public static final EventBus EVENT_BUS = new EventBus();
    public static final ResourceLoader RESOURCE_LOADER = new ResourceLoader();
    private static final EverPhaseApi INSTANCE = new EverPhaseApi();

    static List<IUpdateable> updateables;
    private static boolean hasBeenInitialized = false;
    public EntityPlayer thePlayer;
    public Level theLevel;

    public static EverPhaseApi getEverPhase() {
        return INSTANCE;
    }

    public static List<IUpdateable> getUpdateables() {
        return updateables;
    }

    public void initApi() {
        if (hasBeenInitialized)
            throw new IllegalAccessError("The API can only be initialized once!");

        hasBeenInitialized = true;

        updateables = new ArrayList<>();

        this.thePlayer = new EntityPlayer(new Vector3f(0.0F, 0.0F, 0.0F), 1.0F);

    }

    public static class CraftingApi {
        private static List<CraftingRecipe> registeredShapedRecipes = new ArrayList<>();
        private static List<CraftingRecipe> registeredShapelessRecipes = new ArrayList<>();

        public static void addShapelessCraftingRecipe(CraftingRecipe recipe) {
            registeredShapelessRecipes.add(recipe);
        }

        private static void addCraftingRecipe(CraftingRecipe recipe) {
            registeredShapedRecipes.add(recipe);
        }

    }

    public static class GuiUtils {
        private static Map<Integer, Gui> registeredGuis = new HashMap<>();
        private static List<Gui> registeredHuds = new ArrayList<>();

        public static void registerGui(Gui gui, int id) {
            if (!registeredGuis.keySet().contains(id))
                registeredGuis.put(id, gui);

            Log.i("GUI", "Gui:" + gui.toString() + " ID:" + id);
        }


        public static Map<Integer, Gui> getRegisteredGuis() {
            return registeredGuis;
        }

        public static List<Gui> getRegisteredHuds() {
            return registeredHuds;
        }

        public static void displayGuiScreen(int guiID) {
            Gui gui = getRegisteredGuis().get(guiID);

            if (gui instanceof GuiInventory)
                Log.wtf("GUI", "Gui is an instance of GuiInventory. Are you sure you did not want to pass the Inventory?");

            if (EverPhaseApi.getEverPhase().thePlayer.getCurrentGui() == null)
                EverPhaseApi.getEverPhase().thePlayer.setCurrentGui(gui);
            else EverPhaseApi.getEverPhase().thePlayer.setCurrentGui(null);

            Log.i("GUI", getRegisteredGuis().get(guiID).toString());
        }

        public static void displayGuiScreen(int guiID, Inventory inventory) {
            Gui gui = getRegisteredGuis().get(guiID);

            if (!(gui instanceof GuiInventory)) {
                Log.wtf("GUI", "Gui is not an instance of GuiInventory!");
                return;
            }

            if (EverPhaseApi.getEverPhase().thePlayer.getCurrentGui() == null) {
                GuiInventory guiInventory = (GuiInventory) gui;
                guiInventory.setInventory(inventory);
                EverPhaseApi.getEverPhase().thePlayer.setCurrentGui(guiInventory);
            } else EverPhaseApi.getEverPhase().thePlayer.setCurrentGui(null);
        }

        public static void registerHUD(Gui gui) {
            registeredHuds.add(gui);
        }

    }

    public static class RendererUtils {
        public static final float FOV = 70.0F;                //The field of view
        public static final float NEAR_PLANE = 0.01F;       //The plane to start rendering
        public static final float FAR_PLANE = 1000.0F;     //The plane to stop rendering

        public static Matrix4f PROJECTION_MATRIX;

        public static RendererMaster RENDERER_MASTER;
        public static RendererEntity RENDERER_ENTITY;      //The renderer to render entities
        public static RendererNormalMapping RENDERER_NM;
        public static RendererTerrain RENDERER_TERRAIN;      //The renderer to render the terrain
        public static RendererSkybox RENDERER_SKYBOX;       //The renderer to render the skybox
        public static RendererWater RENDERER_WATER;        //The renderer to render all water planes
        public static RendererGui RENDERER_GUI;
        public static RendererShadowMapMaster RENDERER_SHADOW_MAP;
        public static RendererParticle RENDERER_PARTICLE;

        public static Matrix4f createProjectionMatrix() {
            Matrix4f projectionMatrix = new Matrix4f();
            float aspectRatio = Main.getAspectRatio();
            float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
            float x_scale = y_scale / aspectRatio;
            float frustum_length = FAR_PLANE - NEAR_PLANE;

            projectionMatrix.m00(x_scale);
            projectionMatrix.m11(y_scale);
            projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
            projectionMatrix.m23(-1);
            projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
            projectionMatrix.m33(0);

            return projectionMatrix;
        }

        public static void initRenderers() {
            PROJECTION_MATRIX = createProjectionMatrix();

            RENDERER_MASTER = new RendererMaster();
            RENDERER_ENTITY = new RendererEntity();
            RENDERER_NM = new RendererNormalMapping();
            RENDERER_TERRAIN = new RendererTerrain();
            RENDERER_SKYBOX = new RendererSkybox();
            RENDERER_WATER = new RendererWater();
            RENDERER_GUI = new RendererGui();
            RENDERER_SHADOW_MAP = new RendererShadowMapMaster();
            RENDERER_PARTICLE = new RendererParticle();
        }
    }

    public static class AchievementApi {

        private static final double DISPLAY_TIME = 5.0D;

        private static List<Achievement> listAchievement = new ArrayList<>();
        private static Queue<Achievement> achievementQueue = new LinkedBlockingQueue<>();
        private static Achievement currProcessedAchievement;

        private static double achievementDisplayTicker = 0;

        public static void addAchievement(Achievement achievement) {
            achievement.setAchievementId(listAchievement.size());
            listAchievement.add(achievement);
        }

        public static void unlockAchievement(EntityPlayer player, Achievement achievement) {
            for (Achievement ach : listAchievement) {
                if (ach.getAchievementId() == achievement.getAchievementId()) {
                    if (!ach.isAchievementUnlocked()) {
                        EVENT_BUS.postEvent(new AchievementGetEvent(player, achievement));
                        ach.unlockAchievement();
                        listAchievement.set(listAchievement.indexOf(ach), ach);
                        achievementDisplayTicker = DISPLAY_TIME;
                        achievementQueue.add(achievement);
                    }
                }
            }
        }

        public static boolean isAchievementBeingDisplayed() {
            if (achievementDisplayTicker > 0) {
                achievementDisplayTicker -= Main.getFrameTimeSeconds();
                return true;
            }

            currProcessedAchievement = null;
            return false;
        }

        public static double getAchievementPlayProgress() {
            return (achievementDisplayTicker / DISPLAY_TIME) * 100;
        }

        public static Achievement getCurrentlyProcessedAchievement() {
            return currProcessedAchievement;
        }

        public boolean hasAchievementBeenUnlocked(Achievement achievement) {
            for (Achievement ach : listAchievement)
                if (ach.getAchievementId() == achievement.getAchievementId())
                    return ach.isAchievementUnlocked();

            return false;
        }
    }

}
