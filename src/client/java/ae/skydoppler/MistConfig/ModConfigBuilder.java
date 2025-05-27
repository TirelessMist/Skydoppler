package ae.skydoppler.MistConfig;

        import com.terraformersmc.modmenu.gui.widget.LegacyTexturedButtonWidget;
        import net.fabricmc.fabric.impl.client.screen.ButtonList;
        import net.minecraft.client.gui.DrawContext;
        import net.minecraft.client.gui.screen.ButtonTextures;
        import net.minecraft.client.gui.screen.GameModeSwitcherScreen;
        import net.minecraft.client.gui.screen.Screen;
        import net.minecraft.client.gui.widget.ButtonWidget;
        import net.minecraft.client.gui.widget.ElementListWidget;
        import net.minecraft.client.gui.widget.TextFieldWidget;
        import net.minecraft.text.Text;

        import java.awt.*;
        import java.util.ArrayList;
        import java.util.List;

        public class ModConfigBuilder {
            private Screen parent;
            private Text title;
            private boolean globalizedExpanded = false;
            private Runnable savingRunnable;
            private final List<ModConfigCategory> categories = new ArrayList<>();

            public static ModConfigBuilder create() {
                return new ModConfigBuilder();
            }

            public ModConfigBuilder setParentScreen(Screen parent) {
                this.parent = parent;
                return this;
            }

            public ModConfigBuilder setTitle(Text title) {
                this.title = title;
                return this;
            }

            public ModConfigBuilder setGlobalizedExpanded(boolean expanded) {
                this.globalizedExpanded = expanded;
                return this;
            }

            public ModConfigBuilder setSavingRunnable(Runnable runnable) {
                this.savingRunnable = runnable;
                return this;
            }

            public ModConfigCategory getOrCreateCategory(Text name) {
                for (ModConfigCategory cat : categories) {
                    if (cat.getName().equals(name)) return cat;
                }
                ModConfigCategory cat = new ModConfigCategory(name);
                categories.add(cat);
                return cat;
            }

            public ModConfigEntryBuilder entryBuilder() {
                return new ModConfigEntryBuilder();
            }

            public Screen build() {
                return new ConfigScreen(parent, title, categories, globalizedExpanded, savingRunnable);
            }

            private static class ConfigScreen extends Screen {
                private final Screen parent;
                private final List<ModConfigCategory> categories;
                private final boolean globalizedExpanded;
                private final Runnable savingRunnable;
                private ElementListWidget<ConfigEntry> entryList;
                private TextFieldWidget searchField;
                private String searchQuery = "";

                protected ConfigScreen(Screen parent, Text title, List<ModConfigCategory> categories, boolean globalizedExpanded, Runnable savingRunnable) {
                    super(title);
                    this.parent = parent;
                    this.categories = categories;
                    this.globalizedExpanded = globalizedExpanded;
                    this.savingRunnable = savingRunnable;
                }

                @Override
                protected void init() {
                    this.searchField = new TextFieldWidget(textRenderer, width - 160, 10, 150, 20, Text.literal("Search"));
                    this.addDrawableChild(searchField);

                    this.entryList = new ElementListWidget<>(this.client, this.width, this.height, 40, this.height - 40, 25) {
                    };
                    this.children().add(entryList);

                    for (ModConfigCategory category : categories) {
                        entryList.addEntry(new ConfigEntry(category));
                    }

                    this.addDrawableChild(new ButtonWidget(width / 2 - 100, height - 30, 200, 20, Text.literal("Save & Exit"), button -> {
                        if (savingRunnable != null) savingRunnable.run();
                        this.client.setScreen(parent);
                    }));

                    this.addDrawableChild(new ButtonWidget(width / 2 - 100, height - 60, 200, 20, Text.literal("Cancel"), button -> {
                        this.client.setScreen(parent);
                    }));
                }

                @Override
                public void tick() {
                    searchQuery = searchField.getText();
                    entryList.clearEntries();
                    for (ModConfigCategory category : categories) {
                        if (category.getName().getString().toLowerCase().contains(searchQuery.toLowerCase())) {
                            entryList.addEntry(new ConfigEntry(category));
                        }
                    }
                }

                @Override
                public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
                    this.renderBackground(drawContext);
                    drawContext.drawCenteredTextWithShadow(textRenderer, title.getString(), width / 2, 10, 0xFFFFFF);
                    searchField.render(drawContext, mouseX, mouseY, delta);
                    entryList.render(drawContext, mouseX, mouseY, delta);
                    super.render(drawContext, mouseX, mouseY, delta);
                }

                private class ConfigEntry extends ElementListWidget.Entry<ConfigEntry> {
                    private final ModConfigCategory category;

                    public ConfigEntry(ModConfigCategory category) {
                        this.category = category;
                    }

                    @Override
                    public void render(DrawContext drawContext, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float delta) {
                        drawContext.drawTextWithShadow(textRenderer, category.getName(), x + 5, y + 5, 0xFFFFFF);
                        if (hovered) {
                            drawContext.drawTooltip(textRenderer, Text.literal("Click to expand"), mouseX, mouseY);
                        }
                    }

                    @Override
                    public boolean mouseClicked(double mouseX, double mouseY, int button) {
                        if (button == 0) {
                            client.setScreen(new CategoryScreen(ConfigScreen.this, category));
                            return true;
                        }
                        return false;
                    }
                }
            }

            private static class CategoryScreen extends Screen {
                private final Screen parent;
                private final ModConfigCategory category;

                protected CategoryScreen(Screen parent, ModConfigCategory category) {
                    super(category.getName());
                    this.parent = parent;
                    this.category = category;
                }

                @Override
                protected void init() {
                    int y = 40;
                    for (ModConfigListEntry entry : category.getEntries()) {
                        this.addDrawableChild(entry.createWidget(this, 20, y, width - 40));
                        y += 25;
                    }

                    this.addDrawableChild(new ButtonWidget(width / 2 - 100, height - 30, 200, 20, Text.literal("Back"), button -> {
                        this.client.setScreen(parent);
                    }));
                }

                @Override
                public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
                    this.renderBackground(drawContext, mouseX, mouseY, delta);
                    drawContext.drawCenteredTextWithShadow(textRenderer, title.getString(), width / 2, 10, 0xFFFFFF);
                    super.render(drawContext, mouseX, mouseY, delta);
                }
            }
        }