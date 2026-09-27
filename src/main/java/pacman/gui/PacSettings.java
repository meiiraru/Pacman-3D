package pacman.gui;

import cinnamon.gui.Screen;
import cinnamon.gui.screens.SettingsScreen;
import cinnamon.settings.Setting;
import cinnamon.settings.Settings;

import java.util.List;

public class PacSettings extends SettingsScreen {

    public PacSettings(Screen parentScreen) {
        super(parentScreen);
    }

    public PacSettings(Screen parentScreen, boolean fromWorld) {
        super(parentScreen, fromWorld);
    }

    @Override
    protected List<Setting<?>> getSettings() {
        //master sound category
        Setting<?> master = null;
        for (Setting<?> setting : super.getSettings()) {
            if (setting.getName().equals("sound.volume.master")) {
                master = setting;
                break;
            }
        }
        assert master != null;

        //return only the used settings
        return List.of(
                //display
                Settings.showFPS,
                Settings.vsync,
                Settings.dynamicFpsLimit,
                Settings.fullscreen,
                Settings.borderless,
                Settings.fov,
                Settings.fpsLimit,
                //graphics
                Settings.fxaa,
                Settings.ssaoLevel,
                Settings.renderScale,
                Settings.bloomStrength,
                //sound
                Settings.soundDevice,
                master,
                //accessibility
                Settings.viewBobbingStrength,
                Settings.gamepadDeadzone,
                //mouse
                Settings.sensibility,
                Settings.invertX,
                Settings.invertY,
                Settings.rawMouse,
                //keybinds
                Settings.forward,
                Settings.backward,
                Settings.left,
                Settings.right
        );
    }
}
