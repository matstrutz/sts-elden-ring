package eldenring;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.Patcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eldenring.bosses.MorgottBoss;
import eldenring.monsters.FootSoldierMonster;
import eldenring.monsters.GreatShieldGodrickSoldierMonster;
import eldenring.monsters.GreatSwordGodrickSoldierMonster;
import eldenring.monsters.SwordGodrickSoldierMonster;
import eldenring.monsters.TorchGodrickSoldierMonster;
import eldenring.potions.AcademyMagicPotPotion;
import eldenring.potions.BloodboilAromaticPotion;
import eldenring.potions.MagicGreasePotion;
import eldenring.potions.MagicPotPotion;
import eldenring.potions.NeutralizingBolusesPotion;
import eldenring.potions.ShieldGreasePotion;
import eldenring.potions.VolcanoPotPotion;
import eldenring.powers.OmenBairnPower;
import eldenring.relics.BaseRelic;
import eldenring.util.GeneralUtils;
import eldenring.util.KeywordInfo;
import eldenring.util.TextureLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scannotation.AnnotationDB;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@SpireInitializer
public class EldenRingSTS implements
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        PostInitializeSubscriber {
    public static ModInfo info;
    public static String modID;
    static { loadModInfo(); }
    public static final Logger logger = LogManager.getLogger(modID); //Used to output to the console.
    private static final String resourcesFolder = "eldenring";

    //This is used to prefix the IDs of various objects like cards and relics,
    //to avoid conflicts between different mods using the same name for things.
    public static String makeID(String id) {
        return modID + ":" + id;
    }

    //This will be called by ModTheSpire because of the @SpireInitializer annotation at the top of the class.
    public static void initialize() {
        new EldenRingSTS();
    }

    public EldenRingSTS() {
        BaseMod.subscribe(this); //This will make BaseMod trigger all the subscribers at their appropriate times.
        logger.info(modID + " subscribed to BaseMod.");
    }

    @Override
    public void receiveEditRelics() {
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

    @Override
    public void receivePostInitialize() {
        Texture badgeTexture = TextureLoader.getTexture(resourcePath("badge.png"));
        BaseMod.registerModBadge(badgeTexture, info.Name, GeneralUtils.arrToString(info.Authors), info.Description, null);

        startManualPowers();
        startPotionManual();
        startMonsterManual();
        startBossManual();
    }

    public void startManualPowers(){
        BaseMod.addPower(OmenBairnPower.class, OmenBairnPower.POWER_ID);
    }

    public void startBossManual(){
        BaseMod.addMonster(MorgottBoss.ID, MorgottBoss::new);
        BaseMod.addBoss(TheCity.ID, MorgottBoss.ID, bossIconPath(MorgottBoss.FAKE_ID), bossIconPath(MorgottBoss.FAKE_ID));
    }

    public void startMonsterManual(){
//        BaseMod.addMonster(FootSoldierMonster.ID, () -> new FootSoldierMonster(0.0F, 0.0F));
        BaseMod.addMonster(FootSoldierMonster.ID, () -> new MonsterGroup(new AbstractMonster[]{
                new FootSoldierMonster(0.0F, 10.0F),
                new FootSoldierMonster(250.0F, -10.0F)
        }));

        BaseMod.addMonster(makeID("GodrickSoldierGSS"), () -> new MonsterGroup(new AbstractMonster[]{
                new GreatSwordGodrickSoldierMonster(0.0F, 10.0F),
                new SwordGodrickSoldierMonster(250.0F, -10.0F)
        }));

        BaseMod.addMonster(makeID("GodrickSoldierGHS"), () -> new MonsterGroup(new AbstractMonster[]{
                new GreatShieldGodrickSoldierMonster(0.0F, 10.0F),
                new SwordGodrickSoldierMonster(250.0F, -10.0F)
        }));

        BaseMod.addMonster(makeID("GodrickSoldierGST"), () -> new MonsterGroup(new AbstractMonster[]{
                new GreatSwordGodrickSoldierMonster(0.0F, 10.0F),
                new TorchGodrickSoldierMonster(250.0F, -10.0F)
        }));

        BaseMod.addMonster(makeID("GodrickSoldierGHSS"), () -> new MonsterGroup(new AbstractMonster[]{
                new GreatSwordGodrickSoldierMonster(0.0F, 10.0F),
                new GreatShieldGodrickSoldierMonster(250.0F, -10.0F)
        }));


        BaseMod.addMonsterEncounter(Exordium.ID, new MonsterInfo(FootSoldierMonster.ID, 3));
        BaseMod.addStrongMonsterEncounter(Exordium.ID, new MonsterInfo("GodrickSoldierGSS", 3));
        BaseMod.addStrongMonsterEncounter(Exordium.ID, new MonsterInfo("GodrickSoldierGHS", 3));
        BaseMod.addStrongMonsterEncounter(Exordium.ID, new MonsterInfo("GodrickSoldierGST", 3));
        BaseMod.addStrongMonsterEncounter(Exordium.ID, new MonsterInfo("GodrickSoldierGHSS", 3));
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
}
