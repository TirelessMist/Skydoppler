package ae.skydoppler.mixin.client;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.scoreboard.ScoreboardHandler;
import net.minecraft.scoreboard.*;
import net.minecraft.scoreboard.number.NumberFormat;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Scoreboard.class)
public abstract class ScoreboardMixin {

    @Inject(method = "updateScoreboardTeam", at = @At("RETURN"))
    // Called when a Team is updated on the scoreboard (on Hypixel, the team name is updated to update scoreboard lines)
    private void onUpdateScoreboardTeam(Team team, CallbackInfo ci) {

        // NOTE: In future, we might want to send the Prefix, Name, and Suffix separately to the scoreboard handler because I'm pretty sure the icon (like in the location) is in the Prefix, and this could make things faster.


        // Retrieve the prefix and suffix. These may be null so check first.
        Text prefixComponent = team.getPrefix();
        Text suffixComponent = team.getSuffix();

        String combinedString = "";

        // Process the prefix component and its siblings.
        if (prefixComponent != null) {
            combinedString = combinedString + prefixComponent.getString();
        }

        // Process the suffix component and its siblings.
        if (suffixComponent != null) {
            combinedString = combinedString + suffixComponent.getString();
        }

        ScoreboardHandler.scoreboardTeamUpdate(combinedString);

    }

    @Inject(method = "updateScore", at = @At("HEAD"))
    private void onUpdateScore(ScoreHolder holder, ScoreboardObjective objective, ScoreboardScore score, CallbackInfo ci) {

    }

    @Inject(method = "addObjective(Ljava/lang/String;Lnet/minecraft/scoreboard/ScoreboardCriterion;Lnet/minecraft/text/Text;Lnet/minecraft/scoreboard/ScoreboardCriterion$RenderType;ZLnet/minecraft/scoreboard/number/NumberFormat;)Lnet/minecraft/scoreboard/ScoreboardObjective;", at = @At("HEAD"))
    private void onAddObjective(String name, ScoreboardCriterion criterion, Text displayName, ScoreboardCriterion.RenderType renderType, boolean displayAutoUpdate, NumberFormat numberFormat, CallbackInfoReturnable<ScoreboardObjective> cir) {
        if (SkydopplerClient.debugModeEnabled)
            System.out.println("Adding objective: " + name);

        name = name.trim();
        if (criterion == ScoreboardCriterion.DUMMY) {
            SkydopplerClient.isPlayingSkyblock = name.equalsIgnoreCase("SBScoreboard");
        }
    }

}
