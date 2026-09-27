package pacman.gui;

import cinnamon.gui.Screen;
import cinnamon.gui.screens.MainMenu;
import cinnamon.gui.widgets.ContainerGrid;
import cinnamon.gui.widgets.types.Button;
import cinnamon.render.MatrixStack;
import cinnamon.text.Text;
import pacman.world.PacmanWorld;

public class PacmanMainMenu extends Screen {

    @Override
    public void init() {
        //buttons
        ContainerGrid grid = new ContainerGrid(0, 0, 4);

        //play
        Button play = new MainMenu.MainButton(Text.of("Play"), _ -> new PacmanWorld().init());
        grid.addWidget(play);

        //high scores
        Button hiscores = new MainMenu.MainButton(Text.of("High scores"), _ -> client.setScreen(new PacScoresScreen(this)));
        grid.addWidget(hiscores);

        //exit
        Button exitButton = new MainMenu.MainButton(Text.translated("gui.exit"), _ -> client.window.exit());
        grid.addWidget(exitButton);

        //add grid to screen
        int y = (int) (height * 0.15f);
        grid.setPos((width - grid.getWidth()) / 2, y + (height - grid.getHeight() - y) / 2);
        grid.setSkin(MainMenu.GUI_SKIN);
        this.addWidget(grid);
    }

    @Override
    protected void renderBackground(MatrixStack matrices, float delta, int color1, int color2, float size) {
        renderSolidBackground(0xFF161616);
    }
}
