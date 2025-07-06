package ae.skydoppler.config.main_config;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.config.SkydopplerConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
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
    private final Map<String, List<ConfigEntry>> categoryEntries = new HashMap<>();
    private final Set<String> collapsedSubcategories = new java.util.HashSet<>();
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
    }

    public static Screen buildConfigScreen(@NotNull SkydopplerConfig userConfig, Screen parent) {
        return new MainConfigScreen(userConfig, parent);
    }

    @Override
    protected void init() {
        super.init();

        // Clear previous widgets
        this.clearChildren();
        widgets.clear();
        allConfigEntries.clear();
        displayedConfigEntries.clear();
        categoryEntries.clear();

        // Build all config entries
        buildConfigEntries();

        // Set default category if none selected
        if (selectedCategory == null && !categoryEntries.isEmpty()) {
            selectedCategory = categoryEntries.keySet().iterator().next();
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
                .dimensions(categoryPanelCollapsed ? 5 : categoryPanelWidth - 15, topY, 10, 20)
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
            // Process categories in the order they appear in the source code
            // This ensures consistent ordering that matches the MainConfig class definition
            String[] categoryOrder = {"general", "fishing", "dungeons", "inventory", "miscellaneous"};

            for (String categoryName : categoryOrder) {
                try {
                    Field categoryField = userConfig.mainConfig.getClass().getDeclaredField(categoryName);
                    if (Modifier.isPublic(categoryField.getModifiers()) && !Modifier.isStatic(categoryField.getModifiers())) {
                        categoryField.setAccessible(true);
                        Object categoryInstance = categoryField.get(userConfig.mainConfig);

                        List<ConfigEntry> categoryEntryList = new ArrayList<>();

                        // Add category header
                        ConfigEntry categoryHeader = new ConfigEntry(ConfigEntryType.CATEGORY_HEADER,
                                Text.translatable("config.ae.skydoppler.main_config.category." + categoryName),
                                null, null, null, null, null, categoryName, 0);

                        allConfigEntries.add(categoryHeader);
                        categoryEntryList.add(categoryHeader);

                        // Process category fields recursively
                        processClassFields(categoryInstance, categoryName,
                                "config.ae.skydoppler.main_config.category." + categoryName,
                                categoryEntryList, categoryName, 1);

                        categoryEntries.put(categoryName, categoryEntryList);
                    }
                } catch (NoSuchFieldException e) {
                    // Skip categories that don't exist - this allows for future flexibility
                    System.err.println("Warning: Category '" + categoryName + "' not found in MainConfig");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processClassFields(Object instance, String path, String translationPath,
                                    List<ConfigEntry> categoryEntryList, String categoryName, int indentLevel) {
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

                    // Check if this field is a nested config class
                    if (isConfigClass(field.getType())) {
                        // Add subcategory header
                        ConfigEntry subcategoryHeader = new ConfigEntry(ConfigEntryType.SUBCATEGORY_HEADER,
                                Text.translatable(fullTranslationPath),
                                null, null, null, null, fullTranslationPath, categoryName, indentLevel);

                        allConfigEntries.add(subcategoryHeader);
                        categoryEntryList.add(subcategoryHeader);

                        // Process nested fields recursively
                        processClassFields(value, fullPath, fullTranslationPath,
                                categoryEntryList, categoryName, indentLevel + 1);
                    } else {
                        // Add config entry for this field
                        ConfigEntry configEntry = createConfigEntry(instance, field, fullTranslationPath, categoryName, indentLevel);
                        if (configEntry != null) {
                            allConfigEntries.add(configEntry);
                            categoryEntryList.add(configEntry);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            List<ConfigEntry> categoryList = categoryEntries.getOrDefault(selectedCategory, new ArrayList<>());

            // Use a stack to track collapsed subcategories at different levels
            List<String> collapsedParentStack = new ArrayList<>();

            for (ConfigEntry entry : categoryList) {
                if (entry.type == ConfigEntryType.CATEGORY_HEADER) {
                    // Always show category headers and clear the collapsed stack
                    displayedConfigEntries.add(entry);
                    collapsedParentStack.clear();
                } else if (entry.type == ConfigEntryType.SUBCATEGORY_HEADER) {
                    // Remove any collapsed parents at or deeper than this level
                    collapsedParentStack.removeIf(path -> {
                        // Find the indent level of this collapsed parent
                        for (ConfigEntry e : categoryList) {
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

                    // If this subcategory is collapsed, add it to the stack (null check added)
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
                int indentOffset = entry.indentLevel * 15;

                if (entry.type != ConfigEntryType.CATEGORY_HEADER && entry.type != ConfigEntryType.SUBCATEGORY_HEADER) {
                    // Position widget on the right side of the content area
                    int widgetX = contentStartX + contentWidth - widgetWidth - resetButtonWidth - spacing;

                    ClickableWidget widget = createWidget(entry, widgetX, y, widgetWidth);
                    if (widget != null) {
                        widgets.add(widget);
                        this.addDrawableChild(widget);

                        // Add reset button for configurable entries
                        ButtonWidget resetButton = ButtonWidget.builder(Text.literal("Reset"),
                                        button -> resetEntry(entry))
                                .dimensions(contentStartX + contentWidth - resetButtonWidth, y, resetButtonWidth, 20)
                                .build();
                        widgets.add(resetButton);
                        this.addDrawableChild(resetButton);
                    }
                }
            }
            y += entryHeight + entrySpacing;
        }
    }

    private ClickableWidget createWidget(ConfigEntry entry, int x, int y, int width) {
        return switch (entry.type) {
            case BOOLEAN -> CyclingButtonWidget.onOffBuilder(Text.literal("ON"), Text.literal("OFF"))
                    .initially((Boolean) entry.currentValue)
                    .build(x, y, width, 20, entry.displayName, (button, value) -> updateFieldValue(entry, value));

            case INTEGER -> new IntSliderWidget(x, y, width, 20, entry.displayName, (Integer) entry.currentValue,
                    0, 100, value -> updateFieldValue(entry, value));

            case FLOAT -> new FloatSliderWidget(x, y, width, 20, entry.displayName, (Float) entry.currentValue,
                    0.0f, 10.0f, value -> updateFieldValue(entry, value));

            case ENUM -> createEnumWidget(entry, x, y, width);

            default -> null;
        };
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

    private void updateFieldValue(ConfigEntry entry, Object newValue) {
        try {
            entry.field.set(entry.instance, newValue);
            entry.currentValue = newValue;

            // Auto-save the configuration after updating a value
            userConfig.save(SkydopplerClient.CONFIG_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetEntry(ConfigEntry entry) {
        try {
            Object defaultInstance = getDefaultInstance(entry.instance);
            if (defaultInstance != null) {
                Object defaultValue = entry.field.get(defaultInstance);
                entry.field.set(entry.instance, defaultValue);
                entry.currentValue = defaultValue;
                updateWidgets();

                // Auto-save the configuration after resetting a value
                userConfig.save(SkydopplerClient.CONFIG_PATH);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object getDefaultInstance(Object instance) {
        try {
            Field[] fields = defaultConfig.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(defaultConfig);
                if (value.getClass().equals(instance.getClass())) {
                    return value;
                }
                Object nestedDefault = findNestedDefault(value, instance.getClass());
                if (nestedDefault != null) {
                    return nestedDefault;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object findNestedDefault(Object parent, Class<?> targetClass) {
        try {
            Field[] fields = parent.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (Modifier.isPublic(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    Object value = field.get(parent);
                    if (value.getClass().equals(targetClass)) {
                        return value;
                    }
                    if (isConfigClass(value.getClass())) {
                        Object nested = findNestedDefault(value, targetClass);
                        if (nested != null) {
                            return nested;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

        // Draw title
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);

        // Draw category panel
        if (!categoryPanelCollapsed) {
            drawCategoryPanel(context, mouseX, mouseY);
        }

        // Draw scrollbar
        drawScrollbar(context);

        super.render(context, mouseX, mouseY, delta);

        // Draw config entries
        drawConfigEntries(context);
    }

    private void drawCategoryPanel(DrawContext context, int mouseX, int mouseY) {
        int panelX = 5;
        int panelY = 70;
        int panelHeight = this.height - 120;

        // Draw panel background
        context.fill(panelX, panelY, panelX + categoryPanelWidth, panelY + panelHeight, 0x80000000);

        // Draw category buttons
        int buttonY = panelY + 5;
        long currentTime = System.currentTimeMillis();

        // Use the same category order as defined in buildConfigEntries
        String[] categoryOrder = {"general", "fishing", "dungeons", "inventory", "miscellaneous"};

        for (String category : categoryOrder) {
            // Only show categories that actually exist in categoryEntries
            if (!categoryEntries.containsKey(category)) {
                continue;
            }

            boolean isSelected = category.equals(selectedCategory);
            int buttonColor = isSelected ? 0x80FFFFFF : 0x40FFFFFF;
            int textColor = isSelected ? 0xFFFFFF : 0xCCCCCC;

            if (mouseX >= panelX && mouseX < panelX + categoryPanelWidth - 20 &&
                    mouseY >= buttonY && mouseY < buttonY + 20) {
                buttonColor = 0x60FFFFFF;
                if (MinecraftClient.getInstance().mouse.wasLeftButtonClicked()) {
                    selectCategory(category);
                }
            }

            context.fill(panelX + 2, buttonY, panelX + categoryPanelWidth - 20, buttonY + 20, buttonColor);

            // Use scrolling text for category names
            Text categoryText = Text.translatable("config.ae.skydoppler.main_config.category." + category);
            int availableCategoryWidth = categoryPanelWidth - 30; // Leave some padding
            drawScrollingText(context, categoryText, panelX + 5, buttonY + 6, availableCategoryWidth, textColor, currentTime);

            buttonY += 25;
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
                    // Draw collapse/expand arrow for subcategories
                    int arrowX = contentStartX + indentOffset - 12;
                    int arrowY = y + 3;
                    boolean isCollapsed = collapsedSubcategories.contains(entry.translationPath);

                    // Draw arrow button background
                    context.fill(arrowX, arrowY, arrowX + 10, arrowY + 16, 0x40FFFFFF);

                    // Draw arrow symbol
                    String arrowSymbol = isCollapsed ? ">" : "v";
                    context.drawTextWithShadow(this.textRenderer, Text.literal(arrowSymbol),
                            arrowX + 2, arrowY + 4, 0xFFFFFF);

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

    private void drawScrollingText(DrawContext context, Text text, int x, int y, int maxWidth, int color, long currentTime) {
        String textString = text.getString();
        int textWidth = this.textRenderer.getWidth(textString);

        if (textWidth <= maxWidth) {
            // Text fits, draw normally
            context.drawTextWithShadow(this.textRenderer, text, x, y, color);
        } else {
            // Text is too long, implement scrolling at 7 characters per second
            int charactersPerSecond = 7;
            int maxScrollDistance = textWidth - maxWidth;

            // Calculate how long it should take to scroll the full distance
            // Based on character count and speed
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

            // Check category panel clicks
            if (!categoryPanelCollapsed) {
                int panelX = 5;
                int panelY = 70;
                int buttonY = panelY + 5;

                // Use the same category order as in drawCategoryPanel to ensure clicks match visual order
                String[] categoryOrder = {"general", "fishing", "dungeons", "inventory", "miscellaneous"};

                for (String category : categoryOrder) {
                    // Only check categories that actually exist in categoryEntries
                    if (!categoryEntries.containsKey(category)) {
                        continue;
                    }

                    if (mouseX >= panelX && mouseX < panelX + categoryPanelWidth - 20 &&
                            mouseY >= buttonY && mouseY < buttonY + 20) {
                        selectCategory(category);
                        return true;
                    }
                    buttonY += 25;
                }
            }

            // Check subcategory collapse/expand buttons
            if (!searchMode && selectedCategory != null) {
                int contentStartX = categoryPanelCollapsed ? leftMargin : leftMargin + categoryPanelWidth + 10;
                int y = 70 - (int) scrollOffset;

                for (ConfigEntry entry : displayedConfigEntries) {
                    if (y > 60 && y < this.height - 50) {
                        if (entry.type == ConfigEntryType.SUBCATEGORY_HEADER) {
                            int indentOffset = entry.indentLevel * 15;
                            int arrowX = contentStartX + indentOffset - 12;
                            int arrowY = y + 3;

                            if (mouseX >= arrowX && mouseX < arrowX + 10 &&
                                    mouseY >= arrowY && mouseY < arrowY + 16) {
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

    private enum ConfigEntryType {
        CATEGORY_HEADER,
        SUBCATEGORY_HEADER,
        BOOLEAN,
        INTEGER,
        FLOAT,
        ENUM
    }

    private static class ConfigEntry {
        final ConfigEntryType type;
        final Text displayName;
        final Object instance;
        final Field field;
        final Class<?> fieldType;
        final String translationPath;
        final String categoryName;
        final int indentLevel;
        Object currentValue;

        ConfigEntry(ConfigEntryType type, Text displayName, Object instance, Field field,
                    Object currentValue, Class<?> fieldType, String translationPath,
                    String categoryName, int indentLevel) {
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
