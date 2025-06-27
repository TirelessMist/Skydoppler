package ae.skydoppler.config.chat_matcher_config;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.config.SkydopplerConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Unique;

import java.awt.*;
import java.util.List;

/**
 * Configuration screen for chat matching functions.
 */
public class ChatMatchConfigScreen extends Screen {

    private final SkydopplerConfig config;
    private final MinecraftClient client = MinecraftClient.getInstance();
    private final Screen parent;

    private final float panelScreenWidthPercent = 0.65f; // Width of the panel
    /**
     * The currently selected function for which the child menu is open.
     */
    private ChatMatchConfigEntryData selectedFunction = null;
    /**
     * List of buttons representing each chat match function.
     * This will be populated in the init method.
     */
    private List<ButtonWidget> functionButtons;

    public ChatMatchConfigScreen(@NotNull SkydopplerConfig config, Screen parent) {
        super(Text.translatable("config.ae.skydoppler.chatnotification.title"));
        this.config = config;
        this.parent = parent;
    }

    public static Screen buildConfigScreen(@NotNull SkydopplerConfig config, Screen parent) {
        return new ChatMatchConfigScreen(config, parent);
    }

    @Override
    protected void init() {
        // Initialize the list of function buttons
        functionButtons = new java.util.ArrayList<>();

        // Calculate panel dimensions (to match render method)
        int panelX = this.width / 2 - (int) (this.width * panelScreenWidthPercent / 2);
        int panelRight = this.width / 2 + (int) (this.width * panelScreenWidthPercent / 2);
        int contentTop = (int) (this.height * 0.1f);
        int buttonWidth = panelRight - panelX - 10; // 5px spacing on each side

        // Start position for the first button (5px from the content top)
        int currentY = contentTop + 5;

        // Add buttons for each function
        for (ChatMatchConfigEntryData function : config.userChatMatchConfig.functions) {
            ButtonWidget button = ButtonWidget.builder(Text.literal(function.name), btn -> {
                // Open child menu for managing this function
                client.setScreen(ChatMatchFunctionConfigScreen.buildConfigScreen(function, this));
            }).dimensions(panelX + 5, currentY, buttonWidth, 20).build();

            addDrawableChild(button);
            functionButtons.add(button);

            // Move down for the next button (20px height + 5px spacing)
            currentY += 25;
        }

        // Add an exit button
        addDrawableChild(ButtonWidget.builder(Text.translatable("config.ae.skydoppler.chatnotification.exit"), button -> {
            client.setScreen(parent);
        }).dimensions(width / 2 - 100, height - 30, 200, 20).build());

        // Add a button to create a new function
        addDrawableChild(ButtonWidget.builder(Text.literal("+"), button -> {
            // Create a new function with default values
            ChatMatchConfigEntryData newFunction = new ChatMatchConfigEntryData();
            newFunction.name = "New Function"; // Default name
            newFunction.enabled = true; // Default enabled state
            newFunction.playSound = false; // Default play sound state
            newFunction.displayTitle = ""; // Default display title
            newFunction.displayCustomChatMessage = ""; // Default custom chat message
            newFunction.hideOriginalChatMessage = false; // Default hide original chat message state
            newFunction.executeCommands = List.of(); // Default empty command list
            newFunction.matches = List.of(); // Default empty matches list

            // Open the child menu for the new function
            client.setScreen(ChatMatchFunctionConfigScreen.buildConfigScreen(newFunction, this));
        }).dimensions(panelX + 5, height - 30, 20, 20).build());
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {

        // Draw the background, centered on the screen
        int panelX = this.width / 2 - (int) (this.width * panelScreenWidthPercent / 2);
        int panelY = 0;
        int panelRight = this.width / 2 + (int) (this.width * panelScreenWidthPercent / 2);
        int panelBottom = this.height;
        int panelColor = new Color(0, 0, 0, 128).getRGB();

        // Draw the panel background
        drawContext.fill(panelX, panelY, panelRight, panelBottom, panelColor);

        // Draw the content background on top of the panel background
        drawContext.fill(panelX, (int) (panelY + this.height * 0.1f), panelRight, (int) (panelBottom - this.height * 0.1f), new Color(0, 0, 0, 192).getRGB());

        // Draw the Widgets
        super.render(drawContext, mouseX, mouseY, delta);

        // Draw the title text, centered, at the top of the panel
        drawContext.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
    }

    private static class ChatMatchFunctionConfigScreen extends Screen {

        private final MinecraftClient client = MinecraftClient.getInstance();
        private final Screen parent;
        private final ChatMatchConfigEntryData savedFunctionDataForUndo;
        private final float panelScreenWidthPercent = 0.65f; // Width of the panel
        private ChatMatchConfigEntryData functionData;
        private boolean hasMadeAnyChanges = false;
        private ButtonWidget undoAllButton;

        public ChatMatchFunctionConfigScreen(ChatMatchConfigEntryData functionData, Screen parent) {
            super(Text.translatable("config.ae.skydoppler.chatnotification.function_config.title", functionData.name)); // Settings for Chat Function <function name>
            this.functionData = functionData;
            this.parent = parent;
            savedFunctionDataForUndo = functionData;
        }

        /**
         * Builds a configuration screen for managing a specific chat match function.
         *
         * @param functionData The function data to manage.
         * @param parent       The parent screen to return to when exiting.
         * @return A new instance of ChatMatchFunctionConfigScreen.
         */
        public static Screen buildConfigScreen(ChatMatchConfigEntryData functionData, Screen parent) {
            return new ChatMatchFunctionConfigScreen(functionData, parent);
        }

        @Override
        protected void init() {

            int panelX = this.width / 2 - (int) (this.width * panelScreenWidthPercent / 2);
            int panelY = 0;
            int panelRight = this.width / 2 + (int) (this.width * panelScreenWidthPercent / 2);
            int panelBottom = this.height;
            int panelColor = new Color(0, 0, 0, 128).getRGB();
            int contentTop = (int) (this.height * 0.1f);

            //region Bottom Buttons
            final int bottomButtonHeight = 20;
            final int buttonWidth = (panelRight - panelX - 29) / 2; // 29 = 12px left margin + 5px between buttons + 12px right margin

            // Add Undo All Changes button
            undoAllButton = addDrawableChild(ButtonWidget.builder(Text.translatable("skydoppler.undo_all_changes"), button -> {
                        functionData = savedFunctionDataForUndo;
                        hasMadeAnyChanges = false;
                        client.setScreen(this);
                    }).dimensions(panelX + 12, height - 30, buttonWidth, bottomButtonHeight)
                    .tooltip(Tooltip.of(Text.translatable("skydoppler.undo_all_changes.tooltip")))
                    .build());
            undoAllButton.active = hasMadeAnyChanges; // Disable if no changes have been made

            // Add Save And Exit button
            addDrawableChild(ButtonWidget.builder(Text.translatable("skydoppler.save_and_exit"), button -> {
                        client.setScreen(parent);
                    }).dimensions(panelX + 17 + buttonWidth, height - 30, buttonWidth, bottomButtonHeight)
                    .tooltip(Tooltip.of(Text.translatable("skydoppler.save_and_exit.tooltip")))
                    .build());
            //endregion

            //region Function Settings
            int buttonHeight = 20;
            int currentY = contentTop + 5; // Start position for the first widget
            var nameTextBox = addDrawableChild(new TextFieldWidget(this.textRenderer, (int) (this.width * 0.3f), (int) Math.max(getTextRenderer().fontHeight + 8, this.height * 0.1f), Text.translatable("config.ae.skydoppler.chatnotification.name")));
            nameTextBox.setText(functionData.name);
            nameTextBox.setPosition(panelX + 10, currentY);
            nameTextBox.setChangedListener(text -> {

                functionData.name = text;
                makeChange();
            });
            currentY += buttonHeight + 5; // Move down for the next widget

            // Add Enabled toggle
            var enabledCheckbox = addDrawableChild(CheckboxWidget.builder(Text.translatable("config.ae.skydoppler.chatnotification.enabled"), this.textRenderer)
                    .pos(panelX + 10, currentY)
                    .checked(functionData.enabled)
                    .tooltip(Tooltip.of(Text.translatable("config.ae.skydoppler.chatnotification.enabled.tooltip")))
                    .callback((checkbox, checked) -> {
                        functionData.enabled = checked;
                        makeChange();
                    })
                    .build()
            );
            currentY += buttonHeight + 5; // Move down for the next widget

            // Add Play Sound toggle
            var playSoundCheckbox = addDrawableChild(CheckboxWidget.builder(Text.translatable("config.ae.skydoppler.chatnotification.play_sound"), this.textRenderer)
                    .pos(panelX + 10, currentY)
                    .checked(functionData.playSound)
                    .tooltip(Tooltip.of(Text.translatable("config.ae.skydoppler.chatnotification.play_sound.tooltip")))
                    .callback((checkbox, checked) -> {
                        functionData.playSound = checked;
                        makeChange();
                    })
                    .build()
            );
            currentY += buttonHeight + 5; // Move down for the next widget

            // Add Display Title text box
            var displayTitleTextBox = addDrawableChild(new TextFieldWidget(this.textRenderer, (int) (this.width * 0.3f), (int) Math.max(getTextRenderer().fontHeight + 8, this.height * 0.1f), Text.translatable("config.ae.skydoppler.chatnotification.display_title")));
            nameTextBox.setText(functionData.displayTitle);
            nameTextBox.setPosition(panelX + 10, currentY);
            nameTextBox.setChangedListener(text -> {

                functionData.displayTitle = text;
                makeChange();
            });
            currentY += buttonHeight + 5; // Move down for the next widget
            //endregion

        }

        @Unique
        private void undoAllChanges() {
            functionData = savedFunctionDataForUndo;
            hasMadeAnyChanges = false;
            undoAllButton.active = false; // Disable the undo button after undoing changes
        }

        @Unique
        private void makeChange() {
            hasMadeAnyChanges = true;
            undoAllButton.active = true; // Enable the undo button if changes are made
        }

        @Unique
        private void saveChangesAndLeave() {
            // Save the changes to the config
            SkydopplerConfig config = SkydopplerConfig.load(SkydopplerClient.CONFIG_PATH);
            config.userChatMatchConfig.functions.removeIf(f -> f.name.equals(functionData.name)); // Remove old function with the same name
            config.userChatMatchConfig.functions.add(functionData); // Add the updated function
            config.save(SkydopplerClient.CONFIG_PATH);

            client.setScreen(parent); // Return to the parent screen
        }

        @Override
        public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {

            // Draw the background, centered on the screen
            int panelX = this.width / 2 - (int) (this.width * panelScreenWidthPercent / 2);
            int panelY = 0;
            int panelRight = this.width / 2 + (int) (this.width * panelScreenWidthPercent / 2);
            int panelBottom = this.height;
            int panelColor = new Color(0, 0, 0, 128).getRGB();

            // Draw the panel background
            drawContext.fill(panelX, panelY, panelRight, panelBottom, panelColor);

            // Draw the content background on top of the panel background
            drawContext.fill(panelX, (int) (panelY + this.height * 0.1f), panelRight, (int) (panelBottom - this.height * 0.1f), new Color(0, 0, 0, 192).getRGB());

            // Draw the Widgets
            super.render(drawContext, mouseX, mouseY, delta);

            // Draw the title text, centered, at the top of the panel
            drawContext.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        }
    }

}

/*Thank
 you for the code. Now, please make a config screen for adding and managing chat match "functions". It should be in ChatMatchConfigScreen.java. buildConfigScreen method will be the method used to create instances of this config screen. The screen should have a main scrollable list of all the "functions" from SkydopplerClient.CONFIG (hereon referred to as "the config"), and an exit button. The scrolling should be smooth and there should be a scroll bar. Each "function" in the main list can be clicked on to open a child menu that allows the user to manage/see all the details of the specific chat matching "function". Inside, it should allow the user to configure all the settings for a ChatMatchConfigEntryData instance. There should be a text box for the name (it should default to the first 24 (at maximum, 24. can be less) characters of the first ChatMatchEntryData's matchString. there should be a toggle for enabled, playSound, text boxes for displayTitle and displayCustomChatMessage. there should be a toggle for hideOriginalChatMessage. there should be two collapsable sections. One for the List of executeCommands text boxes, and the other for the List of matches, where each match should be its own child collapsable section. This child menu should also have the same scrolling features as the main menu. Whenever a value is modified/verified, the config should be updated and saved. Thank you.*/
