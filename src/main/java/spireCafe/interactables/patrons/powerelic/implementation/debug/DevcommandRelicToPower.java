package spireCafe.interactables.patrons.powerelic.implementation.debug;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import basemod.devcommands.relic.Relic;
import basemod.helpers.ConvertHelper;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireCafe.interactables.patrons.powerelic.PowerelicAllowlist;
import spireCafe.interactables.patrons.powerelic.implementation.PowerelicCutscene;
import spireCafe.util.Wiz;

import java.util.*;

public class DevcommandRelicToPower extends ConsoleCommand {
    public DevcommandRelicToPower() {
        this.requiresPlayer = true;
        this.minExtraTokens = 1;
        this.maxExtraTokens = 2;
        this.simpleCheck = false;
    }
    public void execute(String[] tokens, int depth) {
        String[] relicNameArray = (String[])Arrays.copyOfRange(tokens, 2, tokens.length);
        String relicName = Relic.getRelicName(relicNameArray);
        if(relicName.toLowerCase().startsWith("random ")){
            relicName="random";
        }

        ArrayList<AbstractRelic>allRelics=Wiz.adp().relics;
        ArrayList<AbstractRelic>convertibleRelics=new ArrayList<>();
        ArrayList<AbstractRelic>relicsToConvert=new ArrayList<>();
        for(AbstractRelic relic : allRelics){
            if (PowerelicAllowlist.isRelicConvertibleToCard(relic)) {
                convertibleRelics.add(relic);
            }
        }

        if(relicName.equals("random")){
            int count=1;
            if(tokens.length>depth+1 && ConvertHelper.tryParseInt(tokens[depth + 1]) != null) {
                count = ConvertHelper.tryParseInt(tokens[depth + 1], 0);
            }
            if(count>convertibleRelics.size()) {
                count = convertibleRelics.size();
            }
            Collections.shuffle(convertibleRelics,new Random(AbstractDungeon.miscRng.randomLong()));
            relicsToConvert = new ArrayList<>(convertibleRelics.subList(0, count));
        }else {
            for (AbstractRelic relic : convertibleRelics) {
                if (relic.relicId.equals(relicName)) {
                    relicsToConvert.add(relic);
                    break;
                }
            }
        }
        if(relicsToConvert.isEmpty()){
            DevConsole.log("That relic can't be converted.");
        }else {
            PowerelicCutscene.convertListOfRelicsToPowers(relicsToConvert);
        }
    }

    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();
        Iterator var4 = AbstractDungeon.player.relics.iterator();

        //Anniv7Mod.logger.info("Current token is "+tokens[depth]);

        result.add("random");
        while(var4.hasNext()) {
            AbstractRelic relic = (AbstractRelic)var4.next();
            if(PowerelicAllowlist.isRelicConvertibleToCard(relic)) {
                result.add(relic.relicId.replace(' ', '_'));
            }
        }


        if (result.contains(tokens[depth]) && !tokens[depth].equals("random")) {
            complete = true;
        } else if(tokens[depth].equals("random")){
            result.clear();
            result = ConsoleCommand.smallNumbers();
            if(tokens.length>depth+1) {
                if(tokens[depth + 1].matches("\\d+")) {
                    complete = true;
                }
            }
        }

        return result;
    }

    @Override
    protected void errorMsg() {
        DevcommandPowerelic.cmdHelp();
    }

}
