package pacman.gui;

import cinnamon.gui.ParentedScreen;
import cinnamon.gui.Screen;
import cinnamon.gui.widgets.ContainerGrid;
import cinnamon.gui.widgets.types.Label;
import cinnamon.text.Text;
import cinnamon.utils.Alignment;
import pacman.Main;

public class PacScoresScreen extends ParentedScreen {

    public PacScoresScreen(Screen parentScreen) {
        super(parentScreen);
    }

    @Override
    public void init() {
        super.init();

        Label title = new Label(width / 2, 8, Text.of("High Scores"), Alignment.TOP_CENTER);
        title.setTextScale(2f);
        addWidget(title);

        ContainerGrid names  = new ContainerGrid(16, 36, 8, 2);
        names.setAlignment(Alignment.TOP_LEFT);
        ContainerGrid scores = new ContainerGrid(width - 16, 36, 8);
        scores.setAlignment(Alignment.TOP_RIGHT);

        for (int i = 0; i < Math.min(Main.highScores.size(), 10); i++) {
            String highScore = Main.highScores.get(i);
            int index = highScore.lastIndexOf(":");
            String name = highScore.substring(0, index);
            String score = highScore.substring(index + 1);

            names.addWidget(new Label(0, 0, Text.of((i + 1) + ". ")));
            names.addWidget(new Label(0, 0, Text.of(name)));
            scores.addWidget(new Label(0, 0, Text.of(score + "00")));
        }

        this.addWidget(names);
        this.addWidget(scores);
    }
}
