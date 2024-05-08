package eldenring;

import basemod.*;
import basemod.eventUtil.AddEventParams;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.Patcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eldenring.bosses.MalikethBoss;
import eldenring.bosses.MorgottBoss;
import eldenring.elites.*;
import eldenring.enumeration.ConfigMenuEnum;
import eldenring.events.AlexanderDuelEvent;
import eldenring.events.AlexanderLavaEvent;
import eldenring.events.AlexanderStuckEvent;
import eldenring.events.RyaSnakeEvent;
import eldenring.events.conditions.AlexanderDuelCondition;
import eldenring.events.conditions.AlexanderLavaCondition;
import eldenring.events.conditions.AlexanderStuckCondition;
import eldenring.events.conditions.RyaSnakeCondition;
import eldenring.monsters.*;
import eldenring.potions.*;
import eldenring.powers.DestinedDeathPower;
import eldenring.powers.HemorrhagePower;
import eldenring.powers.OmenBairnPower;
import eldenring.powers.ScarletRotPower;
import eldenring.relics.BaseRelic;
import eldenring.util.GeneralUtils;
import eldenring.util.KeywordInfo;
import eldenring.util.TextureLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scannotation.AnnotationDB;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@SpireInitializer
public class EldenRingSTS implements
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        PostInitializeSubscriber,
        AddAudioSubscriber {
    public static ModInfo info;
    public static String modID;
    static { loadModInfo(); }
    public static final Logger logger = LogManager.getLogger(modID); //Used to output to the console.
    private static final String resourcesFolder = "eldenring";

    private static Properties eldenRingProperties = new Properties();
    public static final String ENABLE_PLACEHOLDER_SETTINGS = "enablePlaceholder";
    public static boolean enablePlaceholder = true;

    private static final String MOD_NAME = "EldenRing";
    private static final String CONFIG_NAME = "EldenRingConfig";
    //This is used to prefix the IDs of various objects like cards and relics,
    //to avoid conflicts between different mods using the same name for things.
    public static String makeID(String id) {
        return modID + ":" + id;
    }

    //This will be called by ModTheSpire because of the @SpireInitializer annotation at the top of the class.
    public static void initialize() {
        new EldenRingSTS();
        loadConfigFile();
    }

    public EldenRingSTS() {
        BaseMod.subscribe(this); //This will make BaseMod trigger all the subscribers at their appropriate times.
        logger.info(modID + " subscribed to BaseMod.");
    }

    @Override
    public void receiveEditRelics() {
        if(!ConfigMenuEnum.RELIC.getButton()){
            new AutoAdd(modID)
                    .packageFilter(BaseRelic.class)
                    .any(BaseRelic.class, (info, relic) -> {
                        if (relic.pool != null) {
                            BaseMod.addRelicToCustomPool(relic, relic.pool);
                        } else {
                            BaseMod.addRelic(relic, relic.relicType);
                        }

                        if (info.seen) {
                            UnlockTracker.markRelicAsSeen(relic.relicId);
                        }
                    });
        }
    }

    @Override
    public void receivePostInitialize() {
        createConfigMenu();

        startManualPowers();
        if(!ConfigMenuEnum.POTION.getButton()){startPotionManual();}
        if(!ConfigMenuEnum.NORMAL.getButton()){startMonsterManual();}
        if(!ConfigMenuEnum.BOSS.getButton()){startBossManual();}
        if(!ConfigMenuEnum.ELITE.getButton()){startEliteManual();}
        if(!ConfigMenuEnum.EVENT.getButton()){startEventManual();}
    }

    public void createConfigMenu(){
        logger.info("Creating Config Mod Panel!");
        Texture badgeTexture = TextureLoader.getTexture(resourcePath("badge.png"));
        ModPanel settingsPanel = new ModPanel();

        UIStrings descriptionString = CardCrawlGame.languagePack.getUIString(makeID("Restart"));
        settingsPanel.addUIElement(new ModLabel(descriptionString.TEXT[0], 350.0f, 750.0f, Color.GOLD, settingsPanel, label -> {}));

        Arrays.stream(ConfigMenuEnum.values()).forEach(item -> {
            UIStrings enumUiText = CardCrawlGame.languagePack.getUIString(makeID(item.getUiId()));
            final ModLabeledToggleButton toggleMonsters = new ModLabeledToggleButton(enumUiText.TEXT[0], 350.0f, item.getyPos() ,
                    Settings.CREAM_COLOR, FontHelper.charDescFont, item.getButton(), settingsPanel, label -> {}, button -> {
                try {
                    SpireConfig config = new SpireConfig(MOD_NAME, CONFIG_NAME, eldenRingProperties);
                    item.setButton(button.enabled);
                    config.setBool(item.getFileId(), button.enabled);
                    config.save();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            });
            settingsPanel.addUIElement(toggleMonsters);
        });
        BaseMod.registerModBadge(badgeTexture, info.Name, GeneralUtils.arrToString(info.Authors), info.Description, settingsPanel);
    }

    public static void loadConfigFile(){
        logger.info("Loading Config File!");
        try {
            Arrays.stream(ConfigMenuEnum.values()).forEach(item -> {
                eldenRingProperties.setProperty(item.getFileId(), "false");
            });

            SpireConfig config = new SpireConfig(MOD_NAME, CONFIG_NAME, eldenRingProperties);
            config.load();

            Arrays.stream(ConfigMenuEnum.values()).forEach(item -> {
                item.setButton(config.getBool(item.getFileId()));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startManualPowers(){
        BaseMod.addPower(OmenBairnPower.class, OmenBairnPower.POWER_ID);
        BaseMod.addPower(ScarletRotPower.class, ScarletRotPower.POWER_ID);
        BaseMod.addPower(HemorrhagePower.class, HemorrhagePower.POWER_ID);
        BaseMod.addPower(DestinedDeathPower.class, DestinedDeathPower.POWER_ID);
    }

    public void addEliteManual(){
        BaseMod.addMonster(OrdovisElite.ID, OrdovisElite::new);
        BaseMod.addMonster(SiluriaElite.ID, SiluriaElite::new);
        BaseMod.addMonster(OlegElite.ID, OlegElite::new);
        BaseMod.addMonster(EngvallElite.ID, EngvallElite::new);
        BaseMod.addMonster(NightCavalryLiurnyaElite.ID, NightCavalryLiurnyaElite::new);
        BaseMod.addMonster(NightCavalryAltusElite.ID, NightCavalryAltusElite::new);
        BaseMod.addMonster(FinlayElite.ID, FinlayElite::new);
    }

    public void startEliteManual(){
        addEliteManual();
        BaseMod.addEliteEncounter(TheCity.ID, new MonsterInfo(OrdovisElite.ID, 3));
        BaseMod.addEliteEncounter(TheCity.ID, new MonsterInfo(SiluriaElite.ID, 3));
        BaseMod.addEliteEncounter(Exordium.ID, new MonsterInfo(OlegElite.ID, 3));
        BaseMod.addEliteEncounter(Exordium.ID, new MonsterInfo(EngvallElite.ID, 3));
        BaseMod.addEliteEncounter(TheCity.ID, new MonsterInfo(NightCavalryAltusElite.ID, 2));
        BaseMod.addEliteEncounter(Exordium.ID, new MonsterInfo(NightCavalryLiurnyaElite.ID, 2));
        BaseMod.addEliteEncounter(TheCity.ID, new MonsterInfo(FinlayElite.ID, 3));
    }

    public void addBossManual() {
        BaseMod.addMonster(MorgottBoss.ID, MorgottBoss::new);
        BaseMod.addMonster(MalikethBoss.ID, MalikethBoss::new);
    }

    public void startBossManual(){
        addBossManual();
        BaseMod.addBoss(TheCity.ID, MorgottBoss.ID, bossIconPath(MorgottBoss.FAKE_ID), bossIconPath(MorgottBoss.FAKE_ID));
        BaseMod.addBoss(TheBeyond.ID, MalikethBoss.ID, bossIconPath(MalikethBoss.FAKE_ID), bossIconPath(MalikethBoss.FAKE_ID));
    }

    public void addMonsterManual(){
        BaseMod.addMonster(FootSoldierMonster.ID, () -> new MonsterGroup(new AbstractMonster[]{
                new FootSoldierMonster(0.0F, 10.0F),
                new FootSoldierMonster(250.0F, -10.0F)
        }));

        BaseMod.addMonster(HighwaymanMonster.ID, () -> new MonsterGroup(new AbstractMonster[]{
                new HighwaymanMonster(-40.0F, 10.0F),
                new HighwaymanMonster(120.0F, -30.0F),
                new HighwaymanMonster(250.0F, 20.0F),
                new HighwaymanMonster(400.0F, -10.0F)
        }));

        BaseMod.addMonster(GreatSwordGodrickSoldierMonster.ID, () -> new MonsterGroup(new AbstractMonster[]{
                new GreatSwordGodrickSoldierMonster(0.0F, 5.0F),
                new SwordGodrickSoldierMonster(250.0F, -15.0F)
        }));

        BaseMod.addMonster(SwordGodrickSoldierMonster.ID, () -> new MonsterGroup(new AbstractMonster[]{
                new GreatShieldGodrickSoldierMonster(0.0F, 10.0F),
                new SwordGodrickSoldierMonster(250.0F, -10.0F)
        }));

        BaseMod.addMonster(TorchGodrickSoldierMonster.ID, () -> new MonsterGroup(new AbstractMonster[]{
                new GreatSwordGodrickSoldierMonster(0.0F, 0.0F),
                new TorchGodrickSoldierMonster(250.0F, -15.0F)
        }));

        BaseMod.addMonster(GreatShieldGodrickSoldierMonster.ID, () -> new MonsterGroup(new AbstractMonster[]{
                new GreatSwordGodrickSoldierMonster(0.0F, 10.0F),
                new GreatShieldGodrickSoldierMonster(250.0F, -12.0F)
        }));

        BaseMod.addMonster(GiantLandOctopusMonster.ID, () -> new MonsterGroup(new AbstractMonster[]{
                new GiantLandOctopusMonster(0.0F, 0.0F),
        }));

        BaseMod.addMonster(ExileSoldierMonster.ID, () -> new MonsterGroup(new AbstractMonster[]{
                new ExileSoldierMonster(-60.0F, 0.0F),
                new ExileSoldierMonster(100.0F, -30.0F),
                new ExileSoldierMonster(300.0F, 20.0F)
        }));

        BaseMod.addMonster(NobleSorcererMonster.ID, () -> new MonsterGroup(new AbstractMonster[]{
                new NobleSorcererMonster(-60.0F, 0.0F),
                new NobleSorcererMonster(100.0F, -30.0F),
                new NobleSorcererMonster(300.0F, 20.0F)
        }));
    }

    public void startMonsterManual(){
        addMonsterManual();

        BaseMod.addMonsterEncounter(Exordium.ID, new MonsterInfo(FootSoldierMonster.ID, 3));
        BaseMod.addMonsterEncounter(Exordium.ID, new MonsterInfo(NobleSorcererMonster.ID, 3));
        BaseMod.addStrongMonsterEncounter(Exordium.ID, new MonsterInfo(GreatSwordGodrickSoldierMonster.ID, 3));
        BaseMod.addStrongMonsterEncounter(Exordium.ID, new MonsterInfo(SwordGodrickSoldierMonster.ID, 3));
        BaseMod.addStrongMonsterEncounter(Exordium.ID, new MonsterInfo(TorchGodrickSoldierMonster.ID, 3));
        BaseMod.addStrongMonsterEncounter(Exordium.ID, new MonsterInfo(GreatShieldGodrickSoldierMonster.ID, 3));
        BaseMod.addStrongMonsterEncounter(Exordium.ID, new MonsterInfo(HighwaymanMonster.ID, 1));
        BaseMod.addStrongMonsterEncounter(Exordium.ID, new MonsterInfo(GiantLandOctopusMonster.ID, 2));
        BaseMod.addStrongMonsterEncounter(Exordium.ID, new MonsterInfo(ExileSoldierMonster.ID, 2));
    }

    public void startPotionManual(){
        BaseMod.addPotion(MagicPotPotion.class, Color.BLUE, Color.WHITE, Color.GRAY, MagicPotPotion.ID);
        BaseMod.addPotion(AcademyMagicPotPotion.class, Color.BLUE, Color.WHITE, Color.GRAY, AcademyMagicPotPotion.ID);
        BaseMod.addPotion(VolcanoPotPotion.class, Color.RED, Color.YELLOW, Color.ORANGE, VolcanoPotPotion.ID);
        BaseMod.addPotion(NeutralizingBolusesPotion.class, Color.GREEN, Color.GRAY, Color.BLACK, NeutralizingBolusesPotion.ID);
        BaseMod.addPotion(BloodboilAromaticPotion.class, Color.RED, Color.FIREBRICK, Color.BLACK, BloodboilAromaticPotion.ID);
        BaseMod.addPotion(MagicGreasePotion.class, Color.BLUE, Color.WHITE, Color.GRAY, MagicGreasePotion.ID);
        BaseMod.addPotion(ShieldGreasePotion.class, Color.BLUE, Color.WHITE, Color.GRAY, ShieldGreasePotion.ID);
    }

    public void startEventManual(){
        startEventMonster();

        BaseMod.addEvent(new AddEventParams.Builder(AlexanderStuckEvent.ID, AlexanderStuckEvent.class).dungeonID(Exordium.ID).bonusCondition(new AlexanderStuckCondition()).create());
        BaseMod.addEvent(new AddEventParams.Builder(AlexanderLavaEvent.ID, AlexanderLavaEvent.class).dungeonID(TheCity.ID).bonusCondition(new AlexanderLavaCondition()).create());
        BaseMod.addEvent(new AddEventParams.Builder(RyaSnakeEvent.ID, RyaSnakeEvent.class).bonusCondition(new RyaSnakeCondition()).create());
        BaseMod.addEvent(new AddEventParams.Builder(AlexanderDuelEvent.ID, AlexanderDuelEvent.class).dungeonID(TheBeyond.ID).bonusCondition(new AlexanderDuelCondition()).create());
    }

    public void startEventMonster(){
        BaseMod.addMonster(AlexanderEventMonster.ID, AlexanderEventMonster::new);
    }

    /*----------Localization----------*/

    //This is used to load the appropriate localization files based on language.
    private static String getLangString()
    {
        return Settings.language.name().toLowerCase();
    }
    private static final String defaultLanguage = "eng";

    @Override
    public void receiveEditStrings() {
        /*
            First, load the default localization.
            Then, if the current language is different, attempt to load localization for that language.
            This results in the default localization being used for anything that might be missing.
            The same process is used to load keywords slightly below.
        */
        loadLocalization(defaultLanguage); //no except catching for default localization, you better have at least one that works.
        if (!defaultLanguage.equals(getLangString())) {
            try {
                loadLocalization(getLangString());
            }
            catch (GdxRuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio(makeID("GLINT_PEBBLE"), audioPath("GlintstonePebble"));
        BaseMod.addAudio(makeID("CERULEAN_TEARS"), audioPath("CeruleanTears"));
    }

    private void loadLocalization(String lang) {
        //While this does load every type of localization, most of these files are just outlines so that you can see how they're formatted.
        //Feel free to comment out/delete any that you don't end up using.
        BaseMod.loadCustomStringsFile(CardStrings.class,
                localizationPath(lang, "CardStrings.json"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                localizationPath(lang, "CharacterStrings.json"));
        BaseMod.loadCustomStringsFile(EventStrings.class,
                localizationPath(lang, "EventStrings.json"));
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                localizationPath(lang, "OrbStrings.json"));
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                localizationPath(lang, "PotionStrings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                localizationPath(lang, "PowerStrings.json"));
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                localizationPath(lang, "RelicStrings.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class,
                localizationPath(lang, "UIStrings.json"));
    }

    @Override
    public void receiveEditKeywords()
    {
        Gson gson = new Gson();
        String json = Gdx.files.internal(localizationPath(defaultLanguage, "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        KeywordInfo[] keywords = gson.fromJson(json, KeywordInfo[].class);
        for (KeywordInfo keyword : keywords) {
            registerKeyword(keyword);
        }

        if (!defaultLanguage.equals(getLangString())) {
            try
            {
                json = Gdx.files.internal(localizationPath(getLangString(), "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
                keywords = gson.fromJson(json, KeywordInfo[].class);
                for (KeywordInfo keyword : keywords) {
                    registerKeyword(keyword);
                }
            }
            catch (Exception e)
            {
                logger.warn(modID + " does not support " + getLangString() + " keywords.");
            }
        }
    }

    private void registerKeyword(KeywordInfo info) {
        BaseMod.addKeyword(modID.toLowerCase(), info.PROPER_NAME, info.NAMES, info.DESCRIPTION);
    }

    //These methods are used to generate the correct filepaths to various parts of the resources folder.
    public static String localizationPath(String lang, String file) {
        return resourcesFolder + "/localization/" + lang + "/" + file;
    }
    public static String resourcePath(String file) {
        return resourcesFolder + "/" + file;
    }
    public static String characterPath(String file) {
        return resourcesFolder + "/character/" + file;
    }
    public static String powerPath(String file) {
        return resourcesFolder + "/powers/" + file;
    }
    public static String relicPath(String file) {
        return resourcesFolder + "/relics/" + file;
    }
    public static String monsterPath(String file) {
        return resourcesFolder + "/monsters/" + file + ".png";
    }
    public static String bossIconPath(String file) {
        return resourcesFolder + "/monsters/map/" + file + "Icon.png";
    }
    public static String audioPath(String file) {
        return resourcesFolder + "/audio/" + file + ".ogg";
    }


    //This determines the mod's ID based on information stored by ModTheSpire.
    private static void loadModInfo() {
        Optional<ModInfo> infos = Arrays.stream(Loader.MODINFOS).filter((modInfo)->{
            AnnotationDB annotationDB = Patcher.annotationDBMap.get(modInfo.jarURL);
            if (annotationDB == null)
                return false;
            Set<String> initializers = annotationDB.getAnnotationIndex().getOrDefault(SpireInitializer.class.getName(), Collections.emptySet());
            return initializers.contains(EldenRingSTS.class.getName());
        }).findFirst();
        if (infos.isPresent()) {
            info = infos.get();
            modID = info.ID;
        }
        else {
            throw new RuntimeException("Failed to determine mod info/ID based on initializer.");
        }
    }

    //STATIC EVENT VARIABLES

}
