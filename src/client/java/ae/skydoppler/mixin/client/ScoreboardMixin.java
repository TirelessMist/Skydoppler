package ae.skydoppler.mixin.client;

import net.minecraft.scoreboard.*;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(Scoreboard.class)
public class ScoreboardMixin {

    /*@Inject(method = "updateObjective", at = @At("HEAD"))
    private void onUpdateObjective(ScoreboardObjective objective, CallbackInfo ci) {
        System.out.println("--------SCOREBOARD UPDATE OBJECTIVE:--------");
        System.out.println("SCOREBOARD: " + objective.getScoreboard());
        System.out.println("Name: " + objective.getName());
        System.out.println("Display name: " + objective.getDisplayName());
        System.out.println("Render type: " + objective.getRenderType());
        System.out.println("Criterion: " + objective.getCriterion());
        System.out.println("--------------------------");
    }

    @Inject(method = "updateScore", at = @At("HEAD"))
    private void onUpdateScore(ScoreHolder scoreHolder, ScoreboardObjective scoreboardObjective, ScoreboardScore scoreboardScore, CallbackInfo ci) {
        System.out.println("-------SCORE UPDATE:-------");

        System.out.println("SCOREHOLDER:");
        System.out.println("ScoreHolder DisplayName: " + scoreHolder.getDisplayName());
        System.out.println("ScoreHolder NameForScoreboard: " + scoreHolder.getNameForScoreboard());

        System.out.println("SCOREBOARDOBJECTIVE:");
        System.out.println("Scoreboard: " + scoreboardObjective.getScoreboard());
        System.out.println("Name: " + scoreboardObjective.getName());
        System.out.println("Display name: " + scoreboardObjective.getDisplayName());
        System.out.println("Render type: " + scoreboardObjective.getRenderType());
        System.out.println("Criterion: " + scoreboardObjective.getCriterion());

        System.out.println("SCOREBOARDSCORE:");
        System.out.println("ScoreboardScore Score: " + scoreboardScore.getScore());

        if (scoreboardScore.getDisplayText() != null) {
            System.out.println("ScoreboardScore DisplayText: " + scoreboardScore.getDisplayText());
        }

        System.out.println("ScoreboardScore FormattedScore: " + scoreboardScore.getFormattedScore(scoreboardScore.getNumberFormat()));
        System.out.println("ScoreboardScore IsLocked: " + scoreboardScore.isLocked());

        System.out.println("--------------------------");
    }*/

    @Inject(method = "updateScore", at = @At("HEAD"))
    private void onScoreUpdate(ScoreHolder scoreHolder, ScoreboardObjective objective, ScoreboardScore score, CallbackInfo ci) {
        String scoreboardLine = scoreHolder.getStyledDisplayName().getLiteralString();

        if (scoreboardLine != null) {
            scoreboardLine = stripFormattingCodes(scoreboardLine);
            if (scoreboardLine.contains("Location")) {
                System.out.println("[Hypixel Skyblock] Scoreboard Location Line Updated: " + scoreboardLine);
            }
        }

    }

    private String stripFormattingCodes(String text) {
        return text.replaceAll("§.", "");
    }

}
