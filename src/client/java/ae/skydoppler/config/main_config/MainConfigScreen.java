package ae.skydoppler.config.main_config;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.config.SkydopplerConfig;
import ae.skydoppler.config.held_item_config.HeldItemConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class MainConfigScreen extends Screen {

    private final SkydopplerConfig userConfig;
    private final Screen parent;

    private final List<ConfigEntry> allConfigEntries = new ArrayList<>();
    private final List<ConfigEntry> displayedConfigEntries = new ArrayList<>();
    private final List<ClickableWidget> widgets = new ArrayList<>();
    private final List<MainConfigCategory> categories = new ArrayList<>();
    private final Set<String> collapsedSubcategories = new java.util.HashSet<>();
    private final Map<String, Object> previousValues = new HashMap<>(); // Track previous values for widget updates
    private final int entryHeight = 25;
    private final int entrySpacing = 5;
    private final int leftMargin = 20;
    private final int scrollbarWidth = 8;
    private final int searchBarHeight = 20;
    private final int categoryPanelWidth = 150;
    // Default config instance for reset functionality
    private final SkydopplerConfig.MainConfig defaultConfig = new SkydopplerConfig.MainConfig();
    private double scrollOffset = 0;
    private double maxScroll = 0;
    // UI state
    private boolean searchMode = false;
    private boolean categoryPanelCollapsed = false;
    private String selectedCategory = null;
    private String searchQuery = "";

    // Scrollbar interaction state
    private boolean draggingScrollbar = false;
    private int dragStartY = 0;
    private double dragStartScrollOffset = 0;

    // UI components
    private TextFieldWidget searchField;
    private ButtonWidget searchButton;
    private ButtonWidget exitSearchButton;
    private ButtonWidget toggleCategoryPanelButton;

    public MainConfigScreen(SkydopplerConfig userConfig, Screen parent) {
        super(Text.translatable("config.ae.skydoppler.main_config.title"));
        this.userConfig = userConfig;
        this.parent = parent;
        // Load UI state from config
        this.categoryPanelCollapsed = userConfig.mainConfig.categoryPanelCollapsed;
        initializeCategories();
    }

    public static Screen buildConfigScreen(@NotNull SkydopplerConfig userConfig, Screen parent) {
        return new MainConfigScreen(userConfig, parent);
    }

    private void initializeCategories() {
        categories.clear();
        // Use the new category system from MainConfig
        MainConfigCategory[] configCategories = userConfig.mainConfig.getCategories();
        categories.addAll(java.util.Arrays.asList(configCategories));
        // Categories are already sorted by priority in getCategories()
    }

    @Override
    protected void init() {
        super.init();

        // Clear previous widgets
        this.clearChildren();
        widgets.clear();
        allConfigEntries.clear();
        displayedConfigEntries.clear();

        // Build all config entries
        buildConfigEntries();

        // Set default category if none selected
        if (selectedCategory == null && !categories.isEmpty()) {
            selectedCategory = categories.get(0).getFieldName();
        }

        // Create UI components
        createUIComponents();

        // Update displayed entries and widgets
        updateDisplayedEntries();
        updateWidgets();
    }

    private void createUIComponents() {
        int topY = 40;
        int searchX = categoryPanelCollapsed ? leftMargin : leftMargin + categoryPanelWidth + 10;
        int searchWidth = this.width - searchX - 100 - leftMargin;

        // Search field
        searchField = new TextFieldWidget(this.textRenderer, searchX, topY, searchWidth, searchBarHeight, Text.literal("Search..."));
        searchField.setPlaceholder(Text.literal("Search config options..."));
        searchField.setText(searchQuery);
        this.addDrawableChild(searchField);

        // Search button
        searchButton = ButtonWidget.builder(Text.literal("Search"), button -> performSearch())
                .dimensions(searchX + searchWidth + 5, topY, 40, searchBarHeight)
                .build();
        this.addDrawableChild(searchButton);

        // Exit search button (only visible in search mode)
        if (searchMode) {
            exitSearchButton = ButtonWidget.builder(Text.literal("Clear"), button -> exitSearchMode())
                    .dimensions(searchX + searchWidth + 50, topY, 40, searchBarHeight)
                    .build();
            this.addDrawableChild(exitSearchButton);
        }

        // Category panel toggle button
        toggleCategoryPanelButton = ButtonWidget.builder(
                        Text.literal(categoryPanelCollapsed ? ">" : "<"),
                        button -> toggleCategoryPanel())
                .dimensions(categoryPanelCollapsed ? 5 : categoryPanelWidth - 15, topY, 14, 22)
                .build();
        this.addDrawableChild(toggleCategoryPanelButton);

        // Back button
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.back"),
                        button -> {
                            if (this.client != null) {
                                this.client.setScreen(parent);
                            }
                        })
                .dimensions(this.width / 2 - 100, this.height - 30, 200, 20)
                .build());
    }

    private void buildConfigEntries() {
        try {
            for (MainConfigCategory category : categories) {
                String categoryName = category.getFieldName();
                Object categoryInstance = category.getCategoryInstance();

                // Add category header
                ConfigEntry categoryHeader = new ConfigEntry(ConfigEntryType.CATEGORY_HEADER,
                        category.getLabel(),
                        null, null, null, null, null, categoryName, 0);

                allConfigEntries.add(categoryHeader);

                // Add special button entries first
                if (categoryName.equals("general")) {
                    ConfigEntry buttonEntry = new ConfigEntry(ConfigEntryType.BUTTON,
                            Text.translatable("config.ae.skydoppler.main_config.category.general.held_item_config"),
                            null, null, null, null, "config.ae.skydoppler.main_config.category.general.held_item_config", categoryName, 1);
                    buttonEntry.buttonAction = () -> {
                        if (this.client != null) {
                            this.client.setScreen(new HeldItemConfigScreen(userConfig, this));
                        }
                    };
                    allConfigEntries.add(buttonEntry);
                }

                // Process category fields recursively
                processClassFields(categoryInstance, categoryName,
                        "config.ae.skydoppler.main_config.category." + categoryName,
                        categoryName, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processClassFields(Object instance, String path, String translationPath,
                                    String categoryName, int indentLevel) {
        try {
            Class<?> clazz = instance.getClass();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (Modifier.isPublic(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    Object value = field.get(instance);
                    String fieldName = field.getName();
                    String fullPath = path + "." + fieldName;
                    String fullTranslationPath = translationPath + "." + fieldName;

                    // Skip UI state fields that are not actual config categories
                    if (isUIStateField(fieldName)) {
                        continue;
                    }

                    // Check if this field is a nested config class
                    if (isConfigClass(field.getType())) {
                        // Add subcategory header
                        ConfigEntry subcategoryHeader = new ConfigEntry(ConfigEntryType.SUBCATEGORY_HEADER,
                                Text.translatable(fullTranslationPath),
                                null, null, null, null, fullTranslationPath, categoryName, indentLevel);

                        allConfigEntries.add(subcategoryHeader);

                        // Process nested fields recursively
                        processClassFields(value, fullPath, fullTranslationPath,
                                categoryName, indentLevel + 1);
                    } else {
                        // Add config entry for this field
                        ConfigEntry configEntry = createConfigEntry(instance, field, fullTranslationPath, categoryName, indentLevel);
                        if (configEntry != null) {
                            allConfigEntries.add(configEntry);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isUIStateField(String fieldName) {
        // List of field names that are UI state, not actual config categories or settings
        return fieldName.equals("categoryPanelCollapsed");
    }

    private ConfigEntry createConfigEntry(Object instance, Field field, String translationPath, String categoryName, int indentLevel) {
        try {
            Class<?> fieldType = field.getType();
            Object currentValue = field.get(instance);

            ConfigEntryType entryType;
            if (fieldType == boolean.class || fieldType == Boolean.class) {
                entryType = ConfigEntryType.BOOLEAN;
            } else if (fieldType == int.class || fieldType == Integer.class) {
                entryType = ConfigEntryType.INTEGER;
            } else if (fieldType == float.class || fieldType == Float.class) {
                entryType = ConfigEntryType.FLOAT;
            } else if (fieldType.isEnum()) {
                entryType = ConfigEntryType.ENUM;
            } else {
                return null; // Skip unsupported types
            }

            return new ConfigEntry(entryType,
                    Text.translatable(translationPath),
                    instance, field, currentValue, fieldType, translationPath, categoryName, indentLevel);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isConfigClass(Class<?> clazz) {
        // Check if this is a nested config class (has public non-static fields)
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isPublic(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())) {
                return true;
            }
        }
        return false;
    }

    private void updateDisplayedEntries() {
        displayedConfigEntries.clear();

        if (searchMode) {
            // Show search results (ignore collapse state in search mode)
            displayedConfigEntries.addAll(performSearchInternal(searchQuery));
        } else if (selectedCategory != null) {
            // Show selected category with collapsed subcategories filtered out
            List<ConfigEntry> categoryEntries = new ArrayList<>();

            // Filter entries for the selected category
            for (ConfigEntry entry : allConfigEntries) {
                if (entry.categoryName.equals(selectedCategory)) {
                    categoryEntries.add(entry);
                }
            }

            // Use a stack to track collapsed subcategories at different levels
            List<String> collapsedParentStack = new ArrayList<>();

            for (ConfigEntry entry : categoryEntries) {
                if (entry.type == ConfigEntryType.CATEGORY_HEADER) {
                    // Always show category headers and clear the collapsed stack
                    displayedConfigEntries.add(entry);
                    collapsedParentStack.clear();
                } else if (entry.type == ConfigEntryType.SUBCATEGORY_HEADER) {
                    // Remove any collapsed parents at or deeper than this level
                    collapsedParentStack.removeIf(path -> {
                        // Find the indent level of this collapsed parent
                        for (ConfigEntry e : categoryEntries) {
                            if (e.type == ConfigEntryType.SUBCATEGORY_HEADER &&
                                    e.translationPath != null && e.translationPath.equals(path)) {
                                return e.indentLevel >= entry.indentLevel;
                            }
                        }
                        return false;
                    });

                    // Only show this subcategory if no parent is collapsed
                    if (collapsedParentStack.isEmpty()) {
                        displayedConfigEntries.add(entry);
                    }

                    // If this subcategory is collapsed, add it to the stack
                    if (entry.translationPath != null && collapsedSubcategories.contains(entry.translationPath)) {
                        collapsedParentStack.add(entry.translationPath);
                    }
                } else {
                    // Regular config entry - show only if no parent subcategory is collapsed
                    if (collapsedParentStack.isEmpty()) {
                        displayedConfigEntries.add(entry);
                    }
                }
            }
        } else {
            // Show all entries
            displayedConfigEntries.addAll(allConfigEntries);
        }

        // Calculate scrolling bounds
        int totalHeight = displayedConfigEntries.size() * (entryHeight + entrySpacing);
        int availableHeight = this.height - 120; // Account for title, search bar, and back button
        maxScroll = Math.max(0, totalHeight - availableHeight);
        scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));
    }

    private List<ConfigEntry> performSearchInternal(String query) {
        List<ConfigEntry> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (ConfigEntry entry : allConfigEntries) {
            if (entry.type == ConfigEntryType.CATEGORY_HEADER || entry.type == ConfigEntryType.SUBCATEGORY_HEADER) {
                // Check if category/subcategory name matches
                if (entry.displayName.getString().toLowerCase().contains(lowerQuery)) {
                    results.add(entry);
                }
            } else {
                // Check if field name or translation path matches
                if (entry.displayName.getString().toLowerCase().contains(lowerQuery) ||
                        entry.translationPath.toLowerCase().contains(lowerQuery)) {
                    results.add(entry);
                }
            }
        }

        return results;
    }

    private void performSearch() {
        searchQuery = searchField.getText();
        if (!searchQuery.isEmpty()) {
            searchMode = true;
            exitSearchButton = ButtonWidget.builder(Text.literal("Clear"), button -> exitSearchMode())
                    .dimensions(searchField.getX() + searchField.getWidth() + 50, searchField.getY(), 40, searchBarHeight)
                    .build();
            this.addDrawableChild(exitSearchButton);
            updateDisplayedEntries();
            updateWidgets();
        }
    }

    private void exitSearchMode() {
        searchMode = false;
        searchQuery = "";
        searchField.setText("");
        if (exitSearchButton != null) {
            this.remove(exitSearchButton);
            exitSearchButton = null;
        }
        updateDisplayedEntries();
        updateWidgets();
    }

    private void toggleCategoryPanel() {
        categoryPanelCollapsed = !categoryPanelCollapsed;
        // Save the UI state to config
        userConfig.mainConfig.categoryPanelCollapsed = categoryPanelCollapsed;
        userConfig.saveField(SkydopplerClient.CONFIG_PATH, "mainConfig.categoryPanelCollapsed", categoryPanelCollapsed);
        init(); // Reinitialize to adjust layout
    }

    private void selectCategory(String categoryName) {
        selectedCategory = categoryName;
        scrollOffset = 0;
        updateDisplayedEntries();
        updateWidgets();
    }

    private void updateWidgets() {
        // Remove old widgets (except UI components)
        for (ClickableWidget widget : widgets) {
            this.remove(widget);
        }
        widgets.clear();

        int contentStartX = categoryPanelCollapsed ? leftMargin : leftMargin + categoryPanelWidth + 10;
        int contentWidth = this.width - contentStartX - leftMargin - scrollbarWidth - 10;
        int y = 70 - (int) scrollOffset;

        // Make widgets much smaller - only use 30% of available width for the widget
        int widgetWidth = Math.min(200, contentWidth * 30 / 100);
        int resetButtonWidth = 50;
        int spacing = 10;

        for (ConfigEntry entry : displayedConfigEntries) {
            if (y > 60 && y < this.height - 50) { // Only create widgets for visible entries
                if (entry.type != ConfigEntryType.CATEGORY_HEADER && entry.type != ConfigEntryType.SUBCATEGORY_HEADER) {
                    // Position widget on the right side of the content area
                    int widgetX = contentStartX + contentWidth - widgetWidth - resetButtonWidth - spacing;

                    ClickableWidget widget = createWidget(entry, widgetX, y, widgetWidth);
                    if (widget != null) {
                        widgets.add(widget);
                        this.addDrawableChild(widget);

                        // Add reset button for configurable entries (not buttons)
                        if (entry.type != ConfigEntryType.BUTTON) {
                            boolean isDefault = isValueDefault(entry);
                            ButtonWidget resetButton = ButtonWidget.builder(Text.literal("Reset"),
                                            button -> resetEntry(entry))
                                    .dimensions(contentStartX + contentWidth - resetButtonWidth, y, resetButtonWidth, 20)
                                    .build();
                            resetButton.active = !isDefault; // Disable if already default
                            widgets.add(resetButton);
                            this.addDrawableChild(resetButton);
                        }
                    }
                }
            }
            y += entryHeight + entrySpacing;
        }
    }

    private void updateFieldValue(ConfigEntry entry, Object newValue) {
        try {
            entry.field.set(entry.instance, newValue);
            entry.currentValue = newValue;

            // Use optimized field saving
            String fieldPath = buildFieldPath(entry);
            userConfig.saveField(SkydopplerClient.CONFIG_PATH, fieldPath, newValue);

            // Update widget states if needed
            updateWidgetStates();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String buildFieldPath(ConfigEntry entry) {
        // Build the JSON path for the field by reconstructing the path from the category and field structure
        StringBuilder path = new StringBuilder("mainConfig");

        // Add the category name
        path.append(".").append(entry.categoryName);

        // Parse the translation path to get the nested field structure
        String[] translationParts = entry.translationPath.split("\\.");
        // Skip the first 5 parts: "config", "ae", "skydoppler", "main_config", "category"
        // Then skip the category name part (index 5) since we already added it
        for (int i = 6; i < translationParts.length; i++) {
            path.append(".").append(translationParts[i]);
        }

        return path.toString();
    }

    private void updateWidgetStates() {
        // Only update widgets if values have changed
        boolean needsUpdate = false;
        for (ConfigEntry entry : displayedConfigEntries) {
            if (entry.field != null) {
                try {
                    Object currentValue = entry.field.get(entry.instance);
                    String key = entry.translationPath;
                    if (!Objects.equals(previousValues.get(key), currentValue)) {
                        previousValues.put(key, currentValue);
                        needsUpdate = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (needsUpdate) {
            updateWidgets();
        }
    }

    private boolean isValueDefault(ConfigEntry entry) {
        try {
            Object defaultInstance = getDefaultInstance(entry);
            if (defaultInstance != null) {
                Object defaultValue = entry.field.get(defaultInstance);
                return Objects.equals(entry.currentValue, defaultValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private ClickableWidget createWidget(ConfigEntry entry, int x, int y, int width) {
        ClickableWidget widget = switch (entry.type) {
            case BOOLEAN -> {
                CyclingButtonWidget<Boolean> booleanWidget = CyclingButtonWidget.onOffBuilder(Text.literal("ON"), Text.literal("OFF"))
                        .initially((Boolean) entry.currentValue)
                        .build(x, y, width, 20, entry.displayName, (button, value) -> updateFieldValue(entry, value));

                // Add tooltip for boolean widget
                String tooltipKey = entry.translationPath + ".tooltip";
                booleanWidget.setTooltip(Tooltip.of(Text.translatable(tooltipKey)));

                yield booleanWidget;
            }

            case INTEGER -> {
                IntSliderWidget intWidget = new IntSliderWidget(x, y, width, 20, entry.displayName, (Integer) entry.currentValue,
                        0, 100, value -> updateFieldValue(entry, value));

                // Add tooltip for integer widget
                String tooltipKey = entry.translationPath + ".tooltip";
                intWidget.setTooltip(Tooltip.of(Text.translatable(tooltipKey)));

                yield intWidget;
            }

            case FLOAT -> {
                FloatSliderWidget floatWidget = new FloatSliderWidget(x, y, width, 20, entry.displayName, (Float) entry.currentValue,
                        0.0f, 10.0f, value -> updateFieldValue(entry, value));

                // Add tooltip for float widget
                String tooltipKey = entry.translationPath + ".tooltip";
                floatWidget.setTooltip(Tooltip.of(Text.translatable(tooltipKey)));

                yield floatWidget;
            }

            case ENUM -> {
                ClickableWidget enumWidget = createEnumWidget(entry, x, y, width);
                if (enumWidget != null) {
                    // Add tooltip for enum widget
                    String tooltipKey = entry.translationPath + ".tooltip";
                    enumWidget.setTooltip(Tooltip.of(Text.translatable(tooltipKey)));
                }
                yield enumWidget;
            }

            case BUTTON -> {
                ButtonWidget buttonWidget = ButtonWidget.builder(entry.displayName, button -> {
                    playClickSound(); // Add click sound for button
                    if (entry.buttonAction != null) {
                        entry.buttonAction.run();
                    }
                }).dimensions(x, y, width, 20).build();

                // Add tooltip for button widget
                String tooltipKey = entry.translationPath + ".tooltip";
                buttonWidget.setTooltip(Tooltip.of(Text.translatable(tooltipKey)));

                yield buttonWidget;
            }

            default -> null;
        };

        return widget;
    }

    private void drawScrollingText(DrawContext context, Text text, int x, int y, int maxWidth, int color, long currentTime) {
        String textString = text.getString();
        int textWidth = this.textRenderer.getWidth(textString);

        if (textWidth <= maxWidth) {
            // Text fits, draw normally
            context.drawTextWithShadow(this.textRenderer, text, x, y, color);
        } else {
            // Text is too long, implement scrolling with configurable speed
            int charactersPerSecond = userConfig.mainConfig.accessibility.textScrollingSpeed;
            int maxScrollDistance = textWidth - maxWidth;

            // Calculate how long it should take to scroll the full distance
            long scrollTimeMs = (long) ((textString.length() * 1000.0) / charactersPerSecond);
            long cycleTime = scrollTimeMs * 2; // Double for round trip
            long timeInCycle = currentTime % cycleTime;

            // Calculate scroll offset
            float scrollProgress;
            if (timeInCycle < cycleTime / 2) {
                // First half: scroll from left to right
                scrollProgress = (float) timeInCycle / (cycleTime / 2);
            } else {
                // Second half: scroll from right to left
                scrollProgress = 1.0f - (float) (timeInCycle - cycleTime / 2) / (cycleTime / 2);
            }

            int scrollOffset = (int) (scrollProgress * maxScrollDistance);

            // Enable scissor (clipping) to contain text within bounds
            context.enableScissor(x, y - 2, x + maxWidth, y + 12);

            // Draw the text with offset
            context.drawTextWithShadow(this.textRenderer, text, x - scrollOffset, y, color);

            // Disable scissor
            context.disableScissor();
        }
    }

    private void drawCategoryPanel(DrawContext context, int mouseX, int mouseY) {
        int panelX = 5;
        int panelY = 70;
        int panelHeight = this.height - 120;

        // Draw panel background
        context.fill(panelX, panelY, panelX + categoryPanelWidth, panelY + panelHeight, 0x80000000);

        // Draw category buttons with more padding
        int buttonY = panelY + 8; // Increased top padding from 5 to 8
        long currentTime = System.currentTimeMillis();

        for (MainConfigCategory category : categories) {
            String categoryName = category.getFieldName();
            boolean isSelected = categoryName.equals(selectedCategory);
            int buttonColor = isSelected ? 0x80FFFFFF : 0x40FFFFFF;
            int textColor = isSelected ? 0xFFFFFF : 0xCCCCCC;

            if (mouseX >= panelX && mouseX < panelX + categoryPanelWidth - 20 &&
                    mouseY >= buttonY && mouseY < buttonY + 20) {
                buttonColor = 0x60FFFFFF;
                if (MinecraftClient.getInstance().mouse.wasLeftButtonClicked()) {
                    selectCategory(categoryName);
                }
            }

            // Add more padding around category buttons
            context.fill(panelX + 4, buttonY, panelX + categoryPanelWidth - 24, buttonY + 20, buttonColor); // Increased size from 10x16 to 14x18

            // Use scrolling text for category names with more padding
            int availableCategoryWidth = categoryPanelWidth - 40; // Increased padding from 30 to 40
            drawScrollingText(context, category.getLabel(), panelX + 8, buttonY + 6, availableCategoryWidth, textColor, currentTime); // Increased left padding from 5 to 8

            buttonY += 28; // Increased spacing between buttons from 25 to 28
        }
    }

    private void drawScrollbar(DrawContext context) {
        if (maxScroll <= 0) return;

        int scrollbarX = this.width - leftMargin - scrollbarWidth;
        int scrollbarY = 70;
        int scrollbarHeight = this.height - 120;

        // Draw scrollbar background
        context.fill(scrollbarX, scrollbarY, scrollbarX + scrollbarWidth, scrollbarY + scrollbarHeight, 0x40000000);

        // Draw scrollbar thumb
        double scrollPercent = scrollOffset / maxScroll;
        int thumbHeight = Math.max(10, (int) (scrollbarHeight * (scrollbarHeight / (double) (scrollbarHeight + maxScroll))));
        int thumbY = scrollbarY + (int) (scrollPercent * (scrollbarHeight - thumbHeight));

        context.fill(scrollbarX + 1, thumbY, scrollbarX + scrollbarWidth - 1, thumbY + thumbHeight, 0x80FFFFFF);
    }

    private void drawConfigEntries(DrawContext context) {
        int contentStartX = categoryPanelCollapsed ? leftMargin : leftMargin + categoryPanelWidth + 10;
        int contentWidth = this.width - contentStartX - leftMargin - scrollbarWidth - 10;
        int y = 70 - (int) scrollOffset;

        // Calculate available width for text (leave space for widgets)
        int widgetWidth = Math.min(200, contentWidth * 30 / 100);
        int resetButtonWidth = 50;
        int spacing = 10;
        int textWidth = contentWidth - widgetWidth - resetButtonWidth - spacing - 20;

        long currentTime = System.currentTimeMillis();

        for (ConfigEntry entry : displayedConfigEntries) {
            if (y > 60 && y < this.height - 50) {
                int indentOffset = entry.indentLevel * 15;
                int textX = contentStartX + indentOffset;
                int availableTextWidth = textWidth - indentOffset;

                if (entry.type == ConfigEntryType.CATEGORY_HEADER) {
                    drawScrollingText(context, entry.displayName, textX, y + 5, availableTextWidth, 0xFFD700, currentTime);
                } else if (entry.type == ConfigEntryType.SUBCATEGORY_HEADER) {
                    // Draw collapse/expand arrow for subcategories with more padding
                    int arrowX = contentStartX + indentOffset - 15; // Increased padding from -12 to -15
                    int arrowY = y + 2; // Slightly adjusted Y position
                    boolean isCollapsed = collapsedSubcategories.contains(entry.translationPath);

                    // Draw larger arrow button background with more padding
                    context.fill(arrowX, arrowY, arrowX + 14, arrowY + 18, 0x40FFFFFF); // Increased size from 10x16 to 14x18

                    // Draw arrow symbol centered in the larger area
                    String arrowSymbol = isCollapsed ? ">" : "v";
                    context.drawTextWithShadow(this.textRenderer, Text.literal(arrowSymbol),
                            arrowX + 4, arrowY + 5, 0xFFFFFF); // Adjusted positioning for centering

                    // Draw subcategory text
                    drawScrollingText(context, entry.displayName, textX, y + 5, availableTextWidth, 0xC0C0C0, currentTime);
                } else {
                    // For config entries, draw the label text
                    drawScrollingText(context, entry.displayName, textX, y + 5, availableTextWidth, 0xFFFFFF, currentTime);
                }
            }
            y += entryHeight + entrySpacing;
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (searchField.isFocused() && keyCode == 257) { // Enter key
            performSearch();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (maxScroll > 0) {
            scrollOffset -= verticalAmount * 10;
            scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));
            updateWidgets();
            return true;
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        // Draw title
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);

        // Draw category panel
        if (!categoryPanelCollapsed) {
            drawCategoryPanel(context, mouseX, mouseY);
        }

        // Draw scrollbar
        drawScrollbar(context);


        // Draw config entries
        drawConfigEntries(context);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) { // Left click
            // Check scrollbar interaction
            if (maxScroll > 0) {
                int scrollbarX = this.width - leftMargin - scrollbarWidth;
                int scrollbarY = 70;
                int scrollbarHeight = this.height - 120;

                if (mouseX >= scrollbarX && mouseX < scrollbarX + scrollbarWidth &&
                        mouseY >= scrollbarY && mouseY < scrollbarY + scrollbarHeight) {

                    // Calculate thumb position and size
                    double scrollPercent = scrollOffset / maxScroll;
                    int thumbHeight = Math.max(10, (int) (scrollbarHeight * (scrollbarHeight / (double) (scrollbarHeight + maxScroll))));
                    int thumbY = scrollbarY + (int) (scrollPercent * (scrollbarHeight - thumbHeight));

                    if (mouseY >= thumbY && mouseY < thumbY + thumbHeight) {
                        // Clicked on thumb - start dragging
                        draggingScrollbar = true;
                        dragStartY = (int) mouseY;
                        dragStartScrollOffset = scrollOffset;
                        return true;
                    } else {
                        // Clicked on track - jump to position AND start dragging immediately
                        double newScrollPercent = (mouseY - scrollbarY) / (double) scrollbarHeight;
                        scrollOffset = Math.max(0, Math.min(newScrollPercent * maxScroll, maxScroll));
                        updateWidgets();

                        // Start dragging immediately after jump
                        draggingScrollbar = true;
                        dragStartY = (int) mouseY;
                        dragStartScrollOffset = scrollOffset;
                        return true;
                    }
                }
            }

            // Check category panel clicks with updated coordinates for new padding
            if (!categoryPanelCollapsed) {
                int panelX = 5;
                int panelY = 70;
                int buttonY = panelY + 8; // Updated to match new padding

                // Use the new category system for clicks
                for (MainConfigCategory category : categories) {
                    String categoryName = category.getFieldName();

                    // Updated click area to match new button positioning and padding
                    if (mouseX >= panelX + 4 && mouseX < panelX + categoryPanelWidth - 24 &&
                            mouseY >= buttonY && mouseY < buttonY + 20) {
                        playClickSound(); // Play click sound for category button
                        selectCategory(categoryName);
                        return true;
                    }
                    buttonY += 28; // Updated spacing to match new button spacing
                }
            }

            // Check subcategory collapse/expand buttons with updated coordinates for larger buttons
            if (!searchMode && selectedCategory != null) {
                int contentStartX = categoryPanelCollapsed ? leftMargin : leftMargin + categoryPanelWidth + 10;
                int y = 70 - (int) scrollOffset;

                for (ConfigEntry entry : displayedConfigEntries) {
                    if (y > 60 && y < this.height - 50) {
                        if (entry.type == ConfigEntryType.SUBCATEGORY_HEADER) {
                            int indentOffset = entry.indentLevel * 15;
                            int arrowX = contentStartX + indentOffset - 15; // Updated to match new position
                            int arrowY = y + 2; // Updated to match new position

                            // Updated click area to match new larger button size
                            if (mouseX >= arrowX && mouseX < arrowX + 14 &&
                                    mouseY >= arrowY && mouseY < arrowY + 18) {
                                playClickSound(); // Play click sound for subcategory toggle
                                toggleSubcategory(entry.translationPath);
                                return true;
                            }
                        }
                    }
                    y += entryHeight + entrySpacing;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggingScrollbar && button == 0) {
            int scrollbarY = 70;
            int scrollbarHeight = this.height - 120;

            // Calculate thumb height (same calculation as in render method)
            int thumbHeight = Math.max(10, (int) (scrollbarHeight * (scrollbarHeight / (double) (scrollbarHeight + maxScroll))));

            // Calculate the available drag area (scrollbar height minus thumb height)
            int availableDragArea = scrollbarHeight - thumbHeight;

            // Calculate new scroll position based on drag
            int dragDistance = (int) mouseY - dragStartY;
            double dragPercent = (double) dragDistance / availableDragArea; // Use available drag area, not full scrollbar height
            double newScrollOffset = dragStartScrollOffset + (dragPercent * maxScroll);

            scrollOffset = Math.max(0, Math.min(newScrollOffset, maxScroll));
            updateWidgets();
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && draggingScrollbar) {
            draggingScrollbar = false;
            return true;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void toggleSubcategory(String subcategoryPath) {
        if (collapsedSubcategories.contains(subcategoryPath)) {
            collapsedSubcategories.remove(subcategoryPath);
        } else {
            collapsedSubcategories.add(subcategoryPath);
        }
        updateDisplayedEntries();
        updateWidgets();
    }

    @SuppressWarnings("unchecked")
    private ClickableWidget createEnumWidget(ConfigEntry entry, int x, int y, int width) {
        try {
            Class<? extends Enum> enumClass = (Class<? extends Enum>) entry.fieldType;
            Enum[] enumValues = enumClass.getEnumConstants();
            Enum currentValue = (Enum) entry.currentValue;

            return CyclingButtonWidget.builder(value -> Text.literal(value.toString()))
                    .values(enumValues)
                    .initially(currentValue)
                    .build(x, y, width, 20, entry.displayName, (button, value) -> updateFieldValue(entry, value));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void resetEntry(ConfigEntry entry) {
        try {
            Object defaultInstance = getDefaultInstance(entry);
            if (defaultInstance != null) {
                Object defaultValue = entry.field.get(defaultInstance);
                entry.field.set(entry.instance, defaultValue);
                entry.currentValue = defaultValue;
                updateWidgets();

                // Use optimized field saving
                String fieldPath = buildFieldPath(entry);
                userConfig.saveField(SkydopplerClient.CONFIG_PATH, fieldPath, defaultValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object getDefaultInstance(ConfigEntry entry) {
        try {
            // Use the field path to navigate to the corresponding instance in the default config
            String fieldPath = buildFieldPath(entry);

            // Remove "mainConfig." prefix since we start from defaultConfig
            String relativePath = fieldPath.substring("mainConfig.".length());

            // Split the path and navigate to the parent instance
            String[] pathParts = relativePath.split("\\.");
            Object current = defaultConfig;

            // Navigate to the parent of the field (exclude the last part which is the field name)
            for (int i = 0; i < pathParts.length - 1; i++) {
                Field field = current.getClass().getDeclaredField(pathParts[i]);
                field.setAccessible(true);
                current = field.get(current);
                if (current == null) {
                    return null;
                }
            }

            return current;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object getDefaultCategoryInstance(String categoryName) {
        try {
            Field categoryField = defaultConfig.getClass().getDeclaredField(categoryName);
            categoryField.setAccessible(true);
            return categoryField.get(defaultConfig);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object findMatchingInstance(Object parent, Class<?> targetClass) {
        try {
            if (parent.getClass().equals(targetClass)) {
                return parent;
            }

            Field[] fields = parent.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (Modifier.isPublic(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    Object value = field.get(parent);
                    if (value != null) {
                        if (value.getClass().equals(targetClass)) {
                            return value;
                        }
                        if (isConfigClass(value.getClass())) {
                            Object nested = findMatchingInstance(value, targetClass);
                            if (nested != null) {
                                return nested;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void playClickSound() {
        if (this.client != null) {
            this.client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK.value(), 1.0f));
        }
    }

    private enum ConfigEntryType {
        CATEGORY_HEADER,
        SUBCATEGORY_HEADER,
        BOOLEAN,
        INTEGER,
        FLOAT,
        ENUM,
        BUTTON
    }

    private static class ConfigEntry {
        public final ConfigEntryType type;
        public final Text displayName;
        public final Object instance;
        public final Field field;
        public final Class<?> fieldType;
        public final String translationPath;
        public final String categoryName;
        public final int indentLevel;
        public Object currentValue;
        public Runnable buttonAction; // For button entries

        public ConfigEntry(ConfigEntryType type, Text displayName, Object instance, Field field,
                           Object currentValue, Class<?> fieldType, String translationPath, String categoryName, int indentLevel) {
            this.type = type;
            this.displayName = displayName;
            this.instance = instance;
            this.field = field;
            this.currentValue = currentValue;
            this.fieldType = fieldType;
            this.translationPath = translationPath;
            this.categoryName = categoryName;
            this.indentLevel = indentLevel;
        }
    }

    private static class IntSliderWidget extends SliderWidget {
        private final int min;
        private final int max;
        private final java.util.function.Consumer<Integer> onValueChange;
        private final Text prefix;

        public IntSliderWidget(int x, int y, int width, int height, Text prefix, int initialValue,
                               int min, int max, java.util.function.Consumer<Integer> onValueChange) {
            super(x, y, width, height, Text.empty(), (double) (initialValue - min) / (max - min));
            this.min = min;
            this.max = max;
            this.onValueChange = onValueChange;
            this.prefix = prefix;
            this.updateMessage();
        }

        @Override
        protected void updateMessage() {
            int currentValue = (int) (this.value * (max - min)) + min;
            this.setMessage(Text.literal(prefix.getString() + ": " + currentValue));
        }

        @Override
        protected void applyValue() {
            int currentValue = (int) (this.value * (max - min)) + min;
            onValueChange.accept(currentValue);
        }
    }

    private static class FloatSliderWidget extends SliderWidget {
        private final float min;
        private final float max;
        private final java.util.function.Consumer<Float> onValueChange;
        private final Text prefix;

        public FloatSliderWidget(int x, int y, int width, int height, Text prefix, float initialValue,
                                 float min, float max, java.util.function.Consumer<Float> onValueChange) {
            super(x, y, width, height, Text.empty(), (double) (initialValue - min) / (max - min));
            this.min = min;
            this.max = max;
            this.onValueChange = onValueChange;
            this.prefix = prefix;
            this.updateMessage();
        }

        @Override
        protected void updateMessage() {
            float currentValue = (float) (this.value * (max - min)) + min;
            this.setMessage(Text.literal(prefix.getString() + ": " + String.format("%.2f", currentValue)));
        }

        @Override
        protected void applyValue() {
            float currentValue = (float) (this.value * (max - min)) + min;
            onValueChange.accept(currentValue);
        }
    }
}
