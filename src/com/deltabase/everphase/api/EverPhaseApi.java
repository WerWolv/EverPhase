package com.deltabase.everphase.api;

import com.deltabase.everphase.achievement.Achievement;
import com.deltabase.everphase.api.crafting.CraftingRecipe;
import com.deltabase.everphase.api.event.EventBus;
import com.deltabase.everphase.api.event.advance.AchievementGetEvent;
import com.deltabase.everphase.api.event.quest.QuestFinishedEvent;
import com.deltabase.everphase.api.event.quest.QuestTaskFinishedEvent;
import com.deltabase.everphase.engine.modelloader.ResourceLoader;
import com.deltabase.everphase.engine.render.*;
import com.deltabase.everphase.engine.render.shadow.RendererShadowMapMaster;
import com.deltabase.everphase.entity.EntityPlayer;
import com.deltabase.everphase.gui.Gui;
import com.deltabase.everphase.gui.inventory.GuiInventory;
import com.deltabase.everphase.main.Main;
import com.deltabase.everphase.quest.Quest;
import com.deltabase.everphase.skill.ISkillRequirement;
import com.deltabase.everphase.skill.Skill;
import com.deltabase.everphase.world.World;
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
    public World theWorld;

    /**
     * Gets an instance of the Api class to access some deeper objects
     *
     * @return the instance of this class
     */
    public static EverPhaseApi getEverPhase() {
        return INSTANCE;
    }

    /**
     * Gets the list of all updateable objects. These are all objects that implement the
     *
     * @return the list of all updateable objects
     * @link IUpdateable interface and call the @link IUpdateable.setUpdatable method in the
     * constructor.
     */
    public static List<IUpdateable> getUpdateables() {
        return updateables;
    }

    /**
     * Sets up everything in order to use the Api properly. This class should only be
     * called once at the startup of the Game.
     *
     * @throws InstantiationError if the method gets called a second time
     */
    public void initApi() {
        if (hasBeenInitialized)
            throw new InstantiationError("The API can only be initialized once!");

        hasBeenInitialized = true;

        updateables = new ArrayList<>();

        this.thePlayer = new EntityPlayer(new Vector3f(0.0F, 0.0F, 0.0F), 1.0F);

    }

    public static class SkillApi {
        private static Map<String, Skill> registeredSkills = new HashMap<>();

        public static void addSkillsToPlayer() {
            for (String key : registeredSkills.keySet())
                EverPhaseApi.getEverPhase().thePlayer.getPlayerData().skillLevels.putIfAbsent(key, registeredSkills.get(key));
        }

        public static void registerSkill(String skillName, Skill skill) {
            if (registeredSkills.containsKey(skillName)) {
                Log.wtf("SkillApi", "This skill is already registered");
                return;
            }
            registeredSkills.put(skillName, skill);
        }

        public static int addXpToSkill(String skillName, int xpAmount) {
            return EverPhaseApi.getEverPhase().thePlayer.getPlayerData().skillLevels.get(skillName).addXp(xpAmount);
        }

        public Skill getSkill(String skillName) {
            return EverPhaseApi.getEverPhase().thePlayer.getPlayerData().skillLevels.get(skillName);
        }

        public boolean hasPlayerSkillRequirements(ISkillRequirement objectWithSkillRequirement) {
            boolean result = true;

            for (Skill skill : EverPhaseApi.getEverPhase().thePlayer.getPlayerData().skillLevels.values()) {
                if (!objectWithSkillRequirement.getSkillRequirements().contains(skill)) continue;

                for (Skill reqSkill : objectWithSkillRequirement.getSkillRequirements()) {
                    if (skill.getName().equals(reqSkill.getName()))
                        if (skill.getCurrentLevel() < reqSkill.getCurrentLevel())
                            result = false;
                }
            }

            return result;
        }
    }

    /**
     * The QuestingApi. In there are all methods used to add and manage Quests.
     */
    public static class QuestingApi {
        private static Map<String, Quest> registeredQuests = new HashMap<>();

        /**
         * Adds a new quest to the game.
         * @param questName the name of the quest. This is an identifier so please use a constant String for this
         * @param quest the instance of the quest to be registered
         */
        public static void registerQuest(String questName, Quest quest) {
            registeredQuests.put(questName, quest);
        }

        /**
         * If a new quest was added to the game and the player doesn't have it in his list already, it adds them.
         */
        public static void addQuestsToPlayer() {
            registeredQuests.forEach((k, o) -> EverPhaseApi.getEverPhase().thePlayer.getPlayerData().notStartedQuests.putIfAbsent(k, o));
        }

        /**
         * Gets a quest by its name
         * @param questName the name of the quest
         * @return the quest object associated with this name
         */
        public static Quest getQuestByName(String questName) {
            Quest quest = null;

            quest = EverPhaseApi.getEverPhase().thePlayer.getPlayerData().notStartedQuests.get(questName);
            if (quest != null) return quest;

            quest = EverPhaseApi.getEverPhase().thePlayer.getPlayerData().startedQuests.get(questName);
            if (quest != null) return quest;

            quest = EverPhaseApi.getEverPhase().thePlayer.getPlayerData().finishedQuests.get(questName);

            return quest;
        }

        /**
         * Checks if the quest specified by the quest name was started by the player
         * @param questName the name of the quest
         * @return true if the quest was started
         */
        public static boolean isQuestStarted(String questName) {
            return EverPhaseApi.getEverPhase().thePlayer.getPlayerData().startedQuests.containsKey(questName);
        }

        /**
         * Checks if the quest specified by the quest name was finished by the player
         * @param questName the name of the quest
         * @return true if the quest was finished
         */
        public static boolean isQuestFinished(String questName) {
            return EverPhaseApi.getEverPhase().thePlayer.getPlayerData().finishedQuests.containsKey(questName);
        }

        /**
         * Finishes the passed quest task if the quest task name maches the name of the current quest task.
         * @param questName the name of the quest.
         * @param questTaskName the name of the quest task.
         */
        public static void finishQuestTask(String questName, String questTaskName) {
            if (!EverPhaseApi.getEverPhase().thePlayer.getPlayerData().startedQuests.containsKey(questName))
                return;

            if (!getQuestByName(questName).doesQuestHasTask(questTaskName)) {
                Log.i("QuestingApi", "Name of task isn't available in this quest. Are you sure both are written correctly?");
                return;
            }

            if (EverPhaseApi.getEverPhase().thePlayer.getPlayerData().startedQuests.get(questName).getCurrentTask().getTaskName().equals(questTaskName)) {
                EverPhaseApi.EVENT_BUS.postEvent(new QuestTaskFinishedEvent(getQuestByName(questName), EverPhaseApi.getEverPhase().thePlayer.getPlayerData().startedQuests.get(questName).finishCurrentTask()));
            }

            if (EverPhaseApi.getEverPhase().thePlayer.getPlayerData().startedQuests.get(questName).getCurrentTask() == null) {
                finishQuest(questName);
                EverPhaseApi.EVENT_BUS.postEvent(new QuestFinishedEvent(getQuestByName(questName)));
                return;
            }
        }

        /**
         * Starts the passed quest if it wasn't started already.
         * @param questName the quest to start
         */
        public static void startQuest(String questName) {

            if (EverPhaseApi.getEverPhase().thePlayer.getPlayerData().finishedQuests.containsKey(questName) ||
                    EverPhaseApi.getEverPhase().thePlayer.getPlayerData().startedQuests.containsKey(questName)) {
                Log.i("QuestingApi", "No need to start quest. It is already started or finished!");
                return;
            }

            if (!EverPhaseApi.getEverPhase().thePlayer.getPlayerData().finishedQuests.values().containsAll(getQuestByName(questName).getDependencies())) {
                Log.i("QuestingApi", "Quest cannot be started because the player's missing some dependencies!");
                return;
            }

            if (!EverPhaseApi.getEverPhase().thePlayer.getPlayerData().notStartedQuests.containsKey(questName)) {
                Log.i("QuestingApi", "Quest cannot be started because it is already started or finished");
                return;
            }

            EverPhaseApi.getEverPhase().thePlayer.getPlayerData().startedQuests.put(questName, EverPhaseApi.getEverPhase().thePlayer.getPlayerData().notStartedQuests.remove(questName));
        }

        /**
         * Finishes the passed quest if it wasn't finished already.
         * @param questName the quest to finish
         */
        public static void finishQuest(String questName) {
            if (!EverPhaseApi.getEverPhase().thePlayer.getPlayerData().startedQuests.containsKey(questName)) {
                Log.i("QuestingApi", "Cannot finish quest because it is already finished or not yet started");
                return;
            }

            EverPhaseApi.getEverPhase().thePlayer.getPlayerData().finishedQuests.put(questName, EverPhaseApi.getEverPhase().thePlayer.getPlayerData().startedQuests.remove(questName));
        }
    }

    /**
     * The CraftingApi. In there are all methods used to add and manage crafting recipes.
     */
    public static class CraftingApi {
        private static List<CraftingRecipe> registeredShapedRecipes = new ArrayList<>();
        private static List<CraftingRecipe> registeredShapelessRecipes = new ArrayList<>();

        /**
         * Adds a new recipe that doesn't have a layout specified. This means all the items in the recipe can
         * be placed in any order in the crafting interface.
         * @param recipe the recipe to add
         */
        public static void addShapelessCraftingRecipe(CraftingRecipe recipe) {
            registeredShapelessRecipes.add(recipe);
        }

        /**
         * Adds a new recipe that has a layout specified.
         * @param recipe the recipe to add
         */
        private static void addCraftingRecipe(CraftingRecipe recipe) {
            registeredShapedRecipes.add(recipe);
        }

    }

    /**
     * The GuiApi. In there are all methods used to add and manage GUIs
     */
    public static class GuiUtils {
        private static Map<Integer, Gui> registeredGuis = new HashMap<>();
        private static List<Gui> registeredHuds = new ArrayList<>();

        /**
         * Registers a new GUI to the game. From now on the GUIs get accessed by the id.
         *
         * @param gui the GUI to register
         * @return the id of the GUI under which it got registered
         */
        public static int registerGui(Gui gui) {
            registeredGuis.put(registeredGuis.size(), gui);

            Log.i("GuiApi", gui.getClass().getSimpleName() + " was registered under the ID #" + (registeredGuis.size() - 1));

            return registeredGuis.size() - 1;
        }

        /**
         * Registers a GUI as a HUD. It cannot be called by displayGuiScreen as it will always be displayed.
         *
         * @param hud the hud to register
         */
        public static void registerHUD(Gui hud) {
            registeredHuds.add(hud);
        }

        /**
         * Gets all GUIs that were registered previously
         * @return the registered GUIs
         */
        public static Map<Integer, Gui> getRegisteredGuis() {
            return registeredGuis;
        }

        /**
         * Gets all HUDs that were registered previously
         * @return the registered HUDs
         */
        public static List<Gui> getRegisteredHuds() {
            return registeredHuds;
        }

        /**
         * Displays a GUI screen if one isn't open already. Otherwise it closes the current one
         * @param guiID the GUI ID of the GUI to open
         */
        public static void displayGuiScreen(int guiID) {
            Gui gui = getRegisteredGuis().get(guiID);

            if (gui instanceof GuiInventory) {
                GuiInventory guiInventory = (GuiInventory) gui;
                if (EverPhaseApi.getEverPhase().thePlayer.getCurrentGui() == null)
                    EverPhaseApi.getEverPhase().thePlayer.setCurrentGui(guiInventory);
                else EverPhaseApi.getEverPhase().thePlayer.setCurrentGui(null);
                return;
            }

            if (EverPhaseApi.getEverPhase().thePlayer.getCurrentGui() == null)
                EverPhaseApi.getEverPhase().thePlayer.setCurrentGui(gui);
            else EverPhaseApi.getEverPhase().thePlayer.setCurrentGui(null);

            Log.i("GuiApi", getRegisteredGuis().get(guiID).toString());
        }
    }

    /**
     * The RendererUtils. In there are all constants and methods associated with rendering
     */
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

        /**
         * Creates a new projection matrix
         * @return the newly created projection matrix
         */
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

        /**
         * Initializes all renderers
         */
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

    /**
     * The AchievementApi. In there are all methods used to add and manage achievements.
     */
    public static class AchievementApi {

        private static final double DISPLAY_TIME = 5.0D;

        private static List<Achievement> listAchievement = new ArrayList<>();
        private static Queue<Achievement> achievementQueue = new LinkedBlockingQueue<>();
        private static Achievement currProcessedAchievement;

        private static double achievementDisplayTicker = 0;

        /**
         * Adds a new achievement to the Game
         * @param achievement the achievement to add
         */
        public static void addAchievement(Achievement achievement) {
            achievement.setAchievementId(listAchievement.size());
            listAchievement.add(achievement);
        }

        /**
         * Unlocks the passed achievement
         *
         * @param achievement the achievement to unlock
         */
        public static void unlockAchievement(Achievement achievement) {
            for (Achievement ach : listAchievement) {
                if (ach.getAchievementId() == achievement.getAchievementId()) {
                    if (!ach.isAchievementUnlocked()) {
                        EVENT_BUS.postEvent(new AchievementGetEvent(getEverPhase().thePlayer, achievement));
                        ach.unlockAchievement();
                        listAchievement.set(listAchievement.indexOf(ach), ach);
                        achievementDisplayTicker = DISPLAY_TIME;
                        achievementQueue.add(achievement);
                    }
                }
            }
        }

        /**
         * Checks wether an achievement is currently displayed.
         * @return true if an achievement gets displayed
         */
        public static boolean isAchievementBeingDisplayed() {
            if (achievementDisplayTicker > 0) {
                achievementDisplayTicker -= Main.getFrameTimeSeconds();
                return true;
            }

            currProcessedAchievement = null;
            return false;
        }

        /**
         * Gets the progress of the current achievement display animation
         * @return the progress in percent
         */
        public static double getAchievementPlayProgress() {
            return (achievementDisplayTicker / DISPLAY_TIME) * 100;
        }

        /**
         * Gets the achievement that is currently getting processed and displayed
         * @return the currently processed achievement
         */
        public static Achievement getCurrentlyProcessedAchievement() {
            return currProcessedAchievement;
        }

        /**
         * Checks wether the passed achievement is already unlocked
         * @param achievement the achievement to check upon
         * @return if it is unlocked
         */
        public boolean hasAchievementBeenUnlocked(Achievement achievement) {
            for (Achievement ach : listAchievement)
                if (ach.getAchievementId() == achievement.getAchievementId())
                    return ach.isAchievementUnlocked();

            return false;
        }
    }

}
