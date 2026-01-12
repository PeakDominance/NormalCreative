package me.peakdominance.nc;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.terraformersmc.modmenu.gui.widget.ModMenuButtonWidget;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (ConfigScreenFactory<Screen>) screen -> new SettingsScreen(Minecraft.getInstance().screen);
    }
}

class SettingsScreen extends Screen {
    public Screen parent;
    protected SettingsScreen(Screen parent) {
        super(Component.literal("Normal Creative settings"));
        this.parent = parent;
    }

    public Button backButton;
    public Button extraItemsButton;
    public Button modernItemsButton;
    public Button numIdsButton;
    public Button extraIdsButton;
    @Override
    protected void init() {
        backButton = ModMenuButtonWidget.builder(Component.literal("<- Back"), button -> Minecraft.getInstance().setScreen(this.parent))
                .bounds(10, 10, 45, 20)
                .build();
        extraItemsButton = ModMenuButtonWidget.builder(
                    NormalCreative.config.getSetting("extraItems") ? Component.literal("Hide extra items").withStyle(ChatFormatting.GREEN) : Component.literal("Show extra items").withStyle(ChatFormatting.RED), button -> {
                        NormalCreative.config.setSetting("extraItems", !NormalCreative.config.getSetting("extraItems"));
                        this.rebuildWidgets();
                    })
                    .bounds(width / 2 - 205, 35, 200, 20)
                    .tooltip(Tooltip.create(Component.literal("Extra items like barrier, dragon egg, farmland etc. in Decoration Blocks tab")))
                    .build();
            modernItemsButton = ModMenuButtonWidget.builder(
                    NormalCreative.config.getSetting("modernItems") ? Component.literal("Hide modern versions items").withStyle(ChatFormatting.GREEN) : Component.literal("Show modern versions items").withStyle(ChatFormatting.RED), button -> {
                        NormalCreative.config.setSetting("modernItems", !NormalCreative.config.getSetting("modernItems"));
                        this.rebuildWidgets();
                    })
                    .bounds(width / 2 + 5, 35, 200, 20)
                    .tooltip(Tooltip.create(Component.literal("All (almost) of the 1.21 items, added only to the search tab to not clutter other tabs with weird stuff")))
                    .build();

            numIdsButton = ModMenuButtonWidget.builder(
                    NormalCreative.config.getSetting("numIds") ? Component.literal("Hide numerical item ids").withStyle(ChatFormatting.GREEN) : Component.literal("Show numerical item ids").withStyle(ChatFormatting.RED), button -> {
                        NormalCreative.config.setSetting("numIds", !NormalCreative.config.getSetting("numIds"));
                        this.rebuildWidgets();
                    })
                    .bounds(width / 2 - 205, 60, 200, 20)
                    .tooltip(Tooltip.create(Component.literal("Pre 1.9 numerical ids")))
                    .build();

        extraIdsButton = ModMenuButtonWidget.builder(
                        NormalCreative.config.getSetting("extraIds") ? Component.literal("Hide extra item ids").withStyle(ChatFormatting.GREEN) : Component.literal("Show extra item ids").withStyle(ChatFormatting.RED), button -> {
                            NormalCreative.config.setSetting("extraIds", !NormalCreative.config.getSetting("extraIds"));
                            this.rebuildWidgets();
                        })
                .bounds(width / 2 + 5, 60, 200, 20)
                .tooltip(Tooltip.create(Component.literal("Extra item ids like petrified slabs, wall banners (requires numerical ids to be enabled)")))
                .build();

        addRenderableWidget(backButton);
        addRenderableWidget(extraItemsButton);
        addRenderableWidget(modernItemsButton);
        addRenderableWidget(numIdsButton);
        addRenderableWidget(extraIdsButton);
        addRenderableWidget(new Text("Note: Some changes require a relog to take place if you've already", width / 2, 100, 10, 10));
        addRenderableWidget(new Text("opened the creative menu, as the content builds when you first open it after joining a world", width / 2, 110, 10, 10));

    }
}

class Text extends AbstractWidget {
    public String text;
    public Text(String text, int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());
        this.text = text;
    }
    @Override
    protected void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
        context.drawCenteredString(
                Minecraft.getInstance().font,
                Component.literal(this.text).withStyle(ChatFormatting.WHITE),
                this.getX(),
                this.getY(),
                200
        );
    }
    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}
}