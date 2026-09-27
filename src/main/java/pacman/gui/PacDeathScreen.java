package pacman.gui;

import cinnamon.gui.GUISkin;
import cinnamon.gui.Screen;
import cinnamon.gui.widgets.WidgetList;
import cinnamon.gui.widgets.types.Button;
import cinnamon.gui.widgets.types.ConfirmPopup;
import cinnamon.gui.widgets.types.Label;
import cinnamon.render.MatrixStack;
import cinnamon.text.Text;
import cinnamon.utils.Alignment;
import cinnamon.utils.UIHelper;
import pacman.Main;
import pacman.world.PacmanWorld;

public class PacDeathScreen extends Screen {

    private boolean queriedHighScore = false;

    @Override
    public void init() {
        super.init();

        Label gameOver = new Label(width / 2, 16, Text.of("Game Over!"), Alignment.TOP_CENTER);
        gameOver.setTextScale(2f);
        this.addWidget(gameOver);

        int score = ((PacmanWorld) client.world).getScore();
        Label scoreLabel = new Label(width / 2, gameOver.getY() + gameOver.getHeight() + 16, Text.of("Score: " + score + "00"), Alignment.TOP_CENTER);
        this.addWidget(scoreLabel);

        int highScoreIndex = -1;
        for (int i = 0; i < Math.min(Main.highScores.size(), 10); i++) {
            String line = Main.highScores.get(i);
            int index = line.lastIndexOf(":");
            int scoreValue = Integer.parseInt(line.substring(index + 1));
            if (score > scoreValue) {
                highScoreIndex = i;
                break;
            }
        }

        boolean hasHighScore = score > 0 && (highScoreIndex != -1 || Main.highScores.size() < 10);

        if (hasHighScore) {
            Label hiScore = new Label(width / 2, scoreLabel.getY() + scoreLabel.getHeight() + 16, Text.of("New High Score!"), Alignment.TOP_CENTER);
            hiScore.setTextScale(1.5f);
            this.addWidget(hiScore);
        }

        if (!queriedHighScore && hasHighScore) {
            queriedHighScore = true;
            final int index = highScoreIndex;
            ConfirmPopup popup = new ConfirmPopup.Input(Text.of("New Highscore!\n\nEnter your name:"), Text.of("Name"), (accepted, text) -> {
                if (!accepted)
                    return;

                String name = text.trim();
                String scoreLine = name + ":" + score;
                if (index != -1) {
                    Main.highScores.add(index, scoreLine);
                } else {
                    Main.highScores.add(scoreLine);
                }
            });
            UIHelper.setPopup(width / 2, height / 2, popup);
            popup.open();
        }

        WidgetList list = new WidgetList(width / 2, height / 2 + 8, 180, 60+16*3, 16);
        list.setAlignment(Alignment.TOP_CENTER);

        list.addWidget(new Button(0, 0, 180, 20, Text.of("New Game"), _ -> new PacmanWorld().init()));
        list.addWidget(new Button(0, 0, 180, 20, Text.of("High scores"), _ -> client.setScreen(new PacScoresScreen(this))));
        list.addWidget(new Button(0, 0, 180, 20, Text.of("Main Menu"), _ -> client.disconnect()));

        this.addWidget(list);
    }

    @Override
    protected void renderBackground(MatrixStack matrices, float delta, int color1, int color2, float size) {
        renderSolidBackground(GUISkin.getCurrentSkin().getInt("world_screen_bg_color"));
    }
}
