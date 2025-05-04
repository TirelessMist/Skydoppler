package ae.skydoppler.mixin.client;

import ae.skydoppler.scoreboard.ScoreboardHandler;
import net.minecraft.scoreboard.*;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Scoreboard.class)
public abstract class ScoreboardMixin {

    @Inject(method = "updateScoreboardTeam", at = @At("HEAD"))
    // Called when a Team is updated on the scoreboard (on Hypixel, the team name is updated to show new info)
    private void onUpdateScoreboardTeam(Team team, CallbackInfo ci) {

        // Retrieve the prefix and suffix. These may be null so check first.
        Text prefixComponent = team.getPrefix();
        Text suffixComponent = team.getSuffix();

        /*ScoreboardTeam scoreboardTeam = team.getScoreboard().getSlotForObjective(team.getObjective());*/

        String combinedString = "";

        // Process the prefix component and its siblings.
        if (prefixComponent != null) {
            combinedString = combinedString + (extractFullText(prefixComponent));
        } else {
            System.out.println("[ScoreboardMixin] The team's prefix is null.");
        }

        // Process the suffix component and its siblings.
        if (suffixComponent != null) {
            combinedString = combinedString + (extractFullText(suffixComponent));
        } else {
            System.out.println("[ScoreboardMixin] The team's suffix is null.");
        }

        ScoreboardHandler.scoreboardTeamUpdate(combinedString);

    }

    @Inject(method = "updateScore", at = @At("HEAD"))
    private void onUpdateScore(ScoreHolder holder, ScoreboardObjective objective, ScoreboardScore score, CallbackInfo ci) {

    }

    @Unique
    private String extractFullText(Text text) {
        if (text == null) return "";

        // In many Yarn mappings, getString() returns the whole string. If not, you can iterate:
        /*StringBuilder sb = new StringBuilder();*/

        String appendedString = "";

        // Append the text of the base component.
        String baseText = text.getString();
        appendedString = appendedString + (baseText);

        // Now, process each sibling. Note that getSiblings() returns a list of Text components.
        // If getString() already accounts for siblings, this loop may be unnecessary.
        /*for (Text sibling : text.getSiblings()) {
            appendedString = appendedString + (sibling.getString());
        }*/

        return appendedString;
    }


}
