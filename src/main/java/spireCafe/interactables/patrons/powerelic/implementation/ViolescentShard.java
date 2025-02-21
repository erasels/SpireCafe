package spireCafe.interactables.patrons.powerelic.implementation;

import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.interactables.patrons.powerelic.PowerelicAllowlist;
import spireCafe.interactables.patrons.powerelic.PowerelicConfig;
import spireCafe.util.Wiz;

import java.io.IOException;
import java.util.*;

import static spireCafe.Anniv7Mod.makeID;

public class ViolescentShard extends AbstractSCRelic implements CustomSavable<ArrayList<String>> {
    public static final String ID = makeID(ViolescentShard.class.getSimpleName());
    public static final String assetID = "ViolescentShard";
    public ArrayList<String> relicPoolHistory = new ArrayList<>();
    public ViolescentShard() {
        super(ID, assetID, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }
    @Override
    public ArrayList<String> onSave() {
        return relicPoolHistory;
    }
    @Override
    public void onLoad(ArrayList<String> history) {
        relicPoolHistory = history;
    }
    @SpirePatch2(clz = AbstractDungeon.class,method="loadSave")
    public static class RestoreRelicPoolPatch{
        @SpirePostfixPatch public static void Patch(){
            if(Wiz.adp()!=null){
                for(AbstractRelic r : Wiz.adp().relics){
                    if(r instanceof ViolescentShard){
                        ((ViolescentShard)r).restoreRelicPool();
                    }
                }
            }
        }
    }

    public void restoreRelicPool() {
        if (!relicPoolHistory.isEmpty()) {
            Anniv7Mod.logger.info("Violescent Shard: reloaded game in progress; restoring the following relics to the pool:");
        }
        for (int i = relicPoolHistory.size() - 1; i >= 0; i -= 1) {
            String str = relicPoolHistory.get(i);
            Anniv7Mod.logger.info("Violescent Shard: " + str);
            int colon = str.indexOf(":");
            String pool = str.substring(0, colon);
            String str2 = str.substring(colon + 1);
            int colon2 = str2.indexOf(":");
            String indexStr = str2.substring(0, colon2);
            int index = Integer.parseInt(indexStr);
            String key = str2.substring(colon2 + 1);
            if (pool.equals("common"))
                AbstractDungeon.commonRelicPool.add(index, key);
            else if (pool.equals("uncommon"))
                AbstractDungeon.uncommonRelicPool.add(index, key);
            else if (pool.equals("rare"))
                AbstractDungeon.rareRelicPool.add(index, key);
            else if (pool.equals("shop"))
                AbstractDungeon.shopRelicPool.add(index, key);
            else if (pool.equals("boss"))
                AbstractDungeon.bossRelicPool.add(index, key);
        }
        relicPoolHistory.clear();
    }


    @SpirePatch2(clz = AbstractDungeon.class, method = "getRewardCards")
    public static class ShardRewardPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"card","i","numCards","rarity"})
        public static void Patch(@ByRef AbstractCard[] card, int i, int numCards, AbstractCard.CardRarity rarity) {
            if (!(Wiz.curRoom() instanceof MonsterRoomBoss && PowerelicConfig.EXCLUDE_BOSS_ROOMS)) {
                if (i == AbstractDungeon.floorNum % numCards) {
                    if (Wiz.adp().hasRelic(ViolescentShard.ID)) {
                        ViolescentShard shard = (ViolescentShard) Wiz.adp().getRelic(ViolescentShard.ID);
                        int roll = AbstractDungeon.cardRng.random(100);
                        Anniv7Mod.logger.info("Violescent Shard: rolled " + roll);
                        if ((roll / 100f) < PowerelicConfig.CARDED_RELIC_SUBSTITUTION_CHANCE) {
                            AbstractRelic relic;
                            relic = returnViolescentRandomRelic(shard, rarity == AbstractCard.CardRarity.RARE || PowerelicConfig.DEBUG_ALL_RELICS_ARE_BOSS_RELICS);
                            if (relic == null) {
                                Anniv7Mod.logger.info("Violescent Shard: ...we're out of cardable relics, so the original card remains unchanged");
                                return;
                            }
                            Anniv7Mod.logger.info("Violescent Shard: " + card[0].toString() + " gets swapped out for " + relic.toString());
                            PowerelicCard newCard = PowerelicCard.fromCopy(relic);
                            newCard.cardIsFromCardReward = true;
                            card[0]=newCard;
                        }
                    }
                }
            }
        }
        static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(Iterator.class, "hasNext");
                return new int[]{(LineFinder.findAllInOrder(ctBehavior, new ArrayList<Matcher>(), finalMatcher))[1]};
            }
        }
    }

    private static ArrayList<String> getNonBossRelicKeyList(){
        ArrayList<String> allkeys=new ArrayList<>();
        allkeys.addAll(AbstractDungeon.commonRelicPool);
        allkeys.addAll(AbstractDungeon.uncommonRelicPool);
        allkeys.addAll(AbstractDungeon.rareRelicPool);
        allkeys.addAll(AbstractDungeon.shopRelicPool);
        //Must use cardRNG here -- relicRNG will change if player S&Qs on reward screen
        long roll = AbstractDungeon.cardRng.randomLong();
        Collections.shuffle(allkeys,new Random(roll));
        return allkeys;
    }

    private static ArrayList<String> getBossRelicKeyList(){
        ArrayList<String> allkeys=new ArrayList<>();
        allkeys.addAll(AbstractDungeon.bossRelicPool);
        long roll = AbstractDungeon.cardRng.randomLong();
        Collections.shuffle(allkeys,new Random(roll));
        return allkeys;
    }

    private static AbstractRelic returnViolescentRandomRelic(ViolescentShard shard, boolean rare) {
        ArrayList<String> allkeys;
        String key;
        if(!rare) {
            allkeys=getNonBossRelicKeyList();
            key=returnViolescentRandomRelicKey(0,allkeys);
            if(key==null){
                Anniv7Mod.logger.info("Violescent Shard: not enough non-boss relics");
                return null;
            }
        }else{
            allkeys=getBossRelicKeyList();
            key=returnViolescentRandomRelicKey(3,allkeys);
            if(key==null){
                Anniv7Mod.logger.info("Violescent Shard: not enough boss relics; use non-boss relic instead");
                return returnViolescentRandomRelic(shard,false);
            }
        }

        //remove the relic (of unknown rarity) from the relic pool it came from.
        //if the relic exists in multiple pools for whatever reason, remove only the first one.
        //why yes, this *is* extremely cursed.
        if(removeRelicFromPool(shard,key,AbstractDungeon.commonRelicPool,"common"));
        else if(removeRelicFromPool(shard,key,AbstractDungeon.uncommonRelicPool,"uncommon"));
        else if(removeRelicFromPool(shard,key,AbstractDungeon.rareRelicPool,"rare"));
        else if(removeRelicFromPool(shard,key,AbstractDungeon.shopRelicPool,"shop"));
        else if(removeRelicFromPool(shard,key,AbstractDungeon.bossRelicPool,"boss"));

        return RelicLibrary.getRelic(key).makeCopy();
    }

    private static boolean removeRelicFromPool(ViolescentShard shard, String key, ArrayList<String> pool,String poolname){
        int index=pool.indexOf(key);
        if(index>=0){
            pool.remove(key);
            shard.relicPoolHistory.add(poolname+":"+ index +":"+key);
            return true;
        }
        return false;
    }

    private static String returnViolescentRandomRelicKey(int minimumRemainingKeysAllowed,ArrayList<String>remainingkeys) {
        if(remainingkeys.size()<=minimumRemainingKeysAllowed)
            return null;
        String key=remainingkeys.remove(0);
        AbstractRelic relic=RelicLibrary.getRelic(key).makeCopy();
        if(PowerelicAllowlist.isRelicConvertibleToCard(relic)){
            return key;
        }else{
            //if relic is uncardable, reroll until we're out of relics
            return returnViolescentRandomRelicKey(0,remainingkeys);
        }
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = {SaveFile.class})
    public static class ClearRelicPoolHistory {
        @SpirePrefixPatch
        public static void Patch() {
            if(Wiz.adp()!=null) {
                for (AbstractRelic relic : Wiz.adp().relics) {
                    if(relic instanceof ViolescentShard){
                        ((ViolescentShard)relic).relicPoolHistory.clear();
                    }
                }
            }
        }
    }

    public static boolean getOutfoxedStatus() {
        return Anniv7Mod.modConfig == null ? false : Anniv7Mod.modConfig.getBool("outfoxed");
    }

    public static void setOutfoxedStatus(boolean status) {
        if (Anniv7Mod.modConfig != null) {
            Anniv7Mod.modConfig.setBool("outfoxed", status);
            try {
                Anniv7Mod.modConfig.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
