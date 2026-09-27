package pacman.gui;

import cinnamon.gui.screens.world.PauseScreen;
import cinnamon.gui.widgets.ContainerGrid;
import cinnamon.gui.widgets.types.Button;
import cinnamon.text.Text;

public class PacPauseMenu extends PauseScreen {

    @Override
    public void init() {
        ContainerGrid grid = new ContainerGrid(0, 0, 8);

        Button resume = new Button(0, 0, 180, 20, Text.translated("gui.pause_screen.resume"), _ -> close());
        Button settings = new Button(0, 0, 180, 20, Text.translated("gui.pause_screen.settings"), _ -> client.setScreen(new PacSettings(this, true)));
        Button menu = new Button(0, 0, 180, 20, Text.translated("gui.pause_screen.main_menu"), _ -> client.disconnect());

        grid.addWidget(resume);
        grid.addWidget(settings);
        grid.addWidget(menu);

        grid.setPos((width - grid.getWidth()) / 2, (int) ((height - grid.getHeight()) * 0.75f));

        this.addWidget(grid);
    }
}
