package ae.skydoppler.config;

    import net.minecraft.client.gui.screen.Screen;
    import net.minecraft.client.gui.widget.ButtonWidget;
    import net.minecraft.client.gui.DrawContext;
    import net.minecraft.text.Text;

    public class SkydopplerConfigScreen extends Screen {

        private final Screen parent;

        public SkydopplerConfigScreen(Screen parent) {
            super(Text.translatable("screen.skydoppler.config.title"));
            this.parent = parent;
        }

        @Override
        protected void init() {
            addDrawableChild(ButtonWidget.builder(Text.of("Done"), button -> {
                this.client.setScreen(parent);
            }).dimensions(this.width / 2 - 100, this.height - 40, 200, 20).build());
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            renderBackground(context, mouseX, mouseY, delta);
            context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
            super.render(context, mouseX, mouseY, delta);
        }
    }