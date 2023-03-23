/*
 * Copyright (c) 2018 superblaubeere27
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.ren.skidmatrix;

import me.ren.skidmatrix.command.CommandManager;
import me.ren.skidmatrix.fileSystem.FileManager;
import me.ren.skidmatrix.modules.ModuleManager;
import me.ren.skidmatrix.scripting.ScriptManager;
import me.ren.skidmatrix.valuesystem.ValueManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SkidMatrix {
    /*
    Metadata
     */
    @NotNull
    public static final String CLIENT_NAME = "SkidMatrix";
    @NotNull
    public static final String CLIENT_AUTHOR = "renCodes";
    public static final double CLIENT_VERSION_NUMBER = 2023.1;
    @NotNull
    public static final String CLIENT_VERSION = CLIENT_VERSION_NUMBER + "a";
    @NotNull
    public static final String CLIENT_INITIALS;
    public static SkidMatrix INSTANCE;

    static {
        List<Character> chars = new ArrayList<>();

        for (char c : CLIENT_NAME.toCharArray())
            if (Character.toUpperCase(c) == c) chars.add(c);

        char[] c = new char[chars.size()];

        for (int i = 0; i < chars.size(); i++) {
            c[i] = chars.get(i);
        }

        CLIENT_INITIALS = new String(c);

    }

    public ModuleManager moduleManager;
    public CommandManager commandManager;
    public ValueManager valueManager;
    private FileManager fileManager;
    public ScriptManager scriptManager;

    public SkidMatrix() {
        INSTANCE = this;
    }

    public void startClient() {
        scriptManager = new ScriptManager();
        fileManager = new FileManager();
        valueManager = new ValueManager();
        commandManager = new CommandManager();
        moduleManager = new ModuleManager();

        fileManager.loadScripts();

        commandManager.addCommands();
        moduleManager.addModules();

        fileManager.load();
    }

    public void stopClient() {
        try {
            fileManager.save();
        } catch (Exception e) {
            System.err.println("Failed to save settings:");
            e.printStackTrace();
        }
    }

}
