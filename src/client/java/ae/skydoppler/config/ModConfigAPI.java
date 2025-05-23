package ae.skydoppler.config;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ModConfigAPI {

    public interface ConfigOption<T> {
        Text getTitle();

        T getValue();

        void setValue(T value);

        T getDefaultValue();

        void reset();

        String getDescription();

        void render(int x, int y, int mouseX, int mouseY);

        boolean mouseClicked(int mouseX, int mouseY, int button);

        void save();
    }

    public static class ConfigScreen extends Screen {
        private final List<ConfigCategory> categories;
        private final boolean autoSave;
        private final Text title;
        private String searchQuery = "";
        private int selectedCategoryIndex = 0;
        private TextFieldWidget searchField;

        protected ConfigScreen(Text title, List<ConfigCategory> categories, boolean autoSave) {
            super(title);
            this.title = title;
            this.categories = categories;
            this.autoSave = autoSave;
        }

        @Override
        protected void init() {
            this.searchField = new TextFieldWidget(textRenderer, width - 160, 10, 150, 20, Text.literal("Search"));
            this.addDrawableChild(searchField);

            int tabX = 10;
            for (int i = 0; i < categories.size(); i++) {
                int index = i;
                this.addDrawableChild(ButtonWidget.builder(categories.get(i).getTitle(), button -> {
                    selectedCategoryIndex = index;
                }).dimensions(tabX, 10, 80, 20).build());
                tabX += 85;
            }

            if (!autoSave) {
                this.addDrawableChild(ButtonWidget.builder(Text.literal("Save"), button -> {
                    saveConfig();
                    this.client.setScreen(null);
                }).dimensions(10, height - 30, 100, 20).build());
            }
        }

        @Override
        public void tick() {
            searchQuery = searchField.getText();
        }

        @Override
        public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
            this.renderBackground(drawContext, mouseX, mouseY, delta);

            drawContext.drawCenteredTextWithShadow(textRenderer, title.getString(), width / 2, 40, 0xFFFFFF);

            List<ConfigOption<?>> optionsToRender;
            if (!searchQuery.isEmpty()) {
                optionsToRender = new ArrayList<>();
                for (ConfigCategory category : categories) {
                    optionsToRender.addAll(category.search(searchQuery));
                }
            } else {
                optionsToRender = categories.get(selectedCategoryIndex).getOptions();
            }

            int startY = 70;
            for (ConfigOption<?> option : optionsToRender) {
                option.render(20, startY, mouseX, mouseY);
                startY += 25;
            }

            super.render(drawContext, mouseX, mouseY, delta);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            List<ConfigOption<?>> optionsToRender;
            if (!searchQuery.isEmpty()) {
                optionsToRender = new ArrayList<>();
                for (ConfigCategory category : categories) {
                    optionsToRender.addAll(category.search(searchQuery));
                }
            } else {
                optionsToRender = categories.get(selectedCategoryIndex).getOptions();
            }

            int optionY = 70;
            for (ConfigOption<?> option : optionsToRender) {
                if (option.mouseClicked((int) mouseX, optionY, button)) {
                    if (autoSave) {
                        option.save();
                    }
                    return true;
                }
                optionY += 25;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

        public void saveConfig() {
            for (ConfigCategory category : categories) {
                for (ConfigOption<?> option : category.getOptions()) {
                    option.save();
                }
            }
        }

        public void close() {
            if (!autoSave) {
                saveConfig();
            }
            super.close();
        }
    }

    public static class ConfigCategory {
        private final Text title;
        private final List<ConfigOption<?>> options = new ArrayList<>();

        public ConfigCategory(Text title) {
            this.title = title;
        }

        public Text getTitle() {
            return title;
        }

        public ConfigCategory addOption(ConfigOption<?> option) {
            options.add(option);
            return this;
        }

        public List<ConfigOption<?>> getOptions() {
            return options;
        }

        public List<ConfigOption<?>> search(String query) {
            List<ConfigOption<?>> results = new ArrayList<>();
            String lower = query.toLowerCase();
            for (ConfigOption<?> option : options) {
                if (option.getTitle().getString().toLowerCase().contains(lower)
                        || option.getDescription().toLowerCase().contains(lower)) {
                    results.add(option);
                }
            }
            return results;
        }
    }
}