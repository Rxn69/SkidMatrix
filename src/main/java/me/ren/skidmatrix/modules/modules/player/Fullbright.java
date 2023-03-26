/*
 * Copyright (c) 2018 superblaubeere27
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.ren.skidmatrix.modules.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import me.ren.skidmatrix.events.MotionUpdateEvent;
import me.ren.skidmatrix.events.PacketEvent;
import me.ren.skidmatrix.modules.Module;
import me.ren.skidmatrix.modules.ModuleCategory;
import me.ren.skidmatrix.notifications.Notification;
import me.ren.skidmatrix.notifications.NotificationManager;
import me.ren.skidmatrix.notifications.NotificationType;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class Fullbright extends Module {

    private float old;

    public Fullbright() {
        super("Fullbright", "Makes your screen go bright bright", ModuleCategory.PLAYER);
    }

    @Override
    protected void onEnable() {
        super.onEnable();

        old = mc.gameSettings.gammaSetting;
    }

    @Override
    protected void onDisable() {
        super.onDisable();

        mc.gameSettings.gammaSetting = old;
    }

    @EventTarget
    public void onUpdate(TickEvent e) {
        mc.gameSettings.gammaSetting = 99;
    }
}
