/*
 * Copyright (c) 2018 superblaubeere27
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.ren.skidmatrix.modules.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import me.ren.skidmatrix.utils.fontRenderer.GlyphPageFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import me.ren.skidmatrix.SkidMatrix;
import me.ren.skidmatrix.events.KeyEvent;
import me.ren.skidmatrix.events.Render2DEvent;
import me.ren.skidmatrix.gui.tabgui.SubTab;
import me.ren.skidmatrix.gui.tabgui.Tab;
import me.ren.skidmatrix.gui.tabgui.TabGui;
import me.ren.skidmatrix.modules.Module;
import me.ren.skidmatrix.modules.ModuleCategory;
import me.ren.skidmatrix.notifications.NotificationManager;
import me.ren.skidmatrix.utils.GLUtil;
import me.ren.skidmatrix.valuesystem.NumberValue;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HUD extends Module {
    @NotNull
    private TabGui<Module> tabGui = new TabGui<>();
    @NotNull
    private List<Integer> fps = new ArrayList<>();

    @NotNull
    private NumberValue<Integer> fpsStatisticLength = new NumberValue<>("FPSStatisticLength", 250, 10, 500);

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

    public HUD() {
        super("HUD", "The Overlay", ModuleCategory.RENDER, false, true, Keyboard.KEY_NONE);
        setState(true);


        HashMap<ModuleCategory, java.util.List<Module>> moduleCategoryMap = new HashMap<>();

        for (Module module : SkidMatrix.INSTANCE.moduleManager.getModules()) {
            if (!moduleCategoryMap.containsKey(module.getCategory())) {
                moduleCategoryMap.put(module.getCategory(), new ArrayList<>());
            }

            moduleCategoryMap.get(module.getCategory()).add(module);
        }

        moduleCategoryMap.entrySet().stream().sorted(Comparator.comparingInt(cat -> cat.getKey().toString().hashCode())).forEach(cat -> {
            Tab<Module> tab = new Tab<>(cat.getKey().toString());

            for (Module module : cat.getValue()) {
                tab.addSubTab(new SubTab<>(module.getName(), subTab -> subTab.getObject().setState(!subTab.getObject().getState()), module));
            }

            tabGui.addTab(tab);
        });

    }

    private static int rainbow(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), 0.8f, 0.7f).getRGB();
    }

    @EventTarget
    private void render2D(Render2DEvent event) {
        GlyphPageFontRenderer fontRenderer = GlyphPageFontRenderer.create("Consolas", 15, false, false, false);
        FontRenderer fontRenderer2 = mc.fontRendererObj;
        GlyphPageFontRenderer fontRenderer3 = GlyphPageFontRenderer.create("Consolas", 20, false, false, false);

        if (!getState()) return;

        fps.add(Minecraft.getDebugFPS());
        while (fps.size() > fpsStatisticLength.getObject()) {
            fps.remove(0);
        }



        ScaledResolution res = new ScaledResolution(mc);

        int blackBarHeight = fontRenderer2.FONT_HEIGHT * 2 + 4;



        double currSpeed = Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);



        LocalDateTime now = LocalDateTime.now();
        String date = dateFormat.format(now);
        String time = timeFormat.format(now);


        AtomicInteger offset = new AtomicInteger(3);
        AtomicInteger index = new AtomicInteger();

        SkidMatrix.INSTANCE.moduleManager.getModules().stream().filter(mod -> mod.getState() && !mod.isHidden()).sorted(Comparator.comparingInt(mod -> -fontRenderer.getStringWidth(mod.getName()))).forEach(mod -> {
            fontRenderer.drawString(mod.getName(), res.getScaledWidth() - fontRenderer.getStringWidth(mod.getName()) - 3, offset.get(), rainbow(index.get() * 100), true);

            offset.addAndGet(fontRenderer2.FONT_HEIGHT + 2);
            index.getAndIncrement();
        });

        NotificationManager.render();

    }

    @EventTarget
    public void onKey(@NotNull KeyEvent event) {
        tabGui.handleKey(event.getKey());
    }
}
//////////////////////