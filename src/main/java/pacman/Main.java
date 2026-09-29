package pacman;

import cinnamon.Cinnamon;
import cinnamon.Client;
import cinnamon.events.Events;
import cinnamon.gui.GUISkin;
import cinnamon.settings.ArgsOptions;
import cinnamon.settings.CliOption;
import cinnamon.utils.IOUtils;
import cinnamon.utils.Resource;
import pacman.gui.PacmanMainMenu;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final List<String> highScores = new ArrayList<>(10);

    public static CliOption<Boolean> renderDebug = CliOption.flagOption("pac_debug", "Render debug map for pacman", "--pac-debug");

    public static void main(String... args) {
        //setup cinnamon flags
        Cinnamon.TITLE = "Pacman 3D";
        Cinnamon.NAMESPACE = "pacman";

        //inject cli args
        ArgsOptions.WINDOW_TITLE_FPS.set(true);
        ArgsOptions.register(renderDebug);

        //set the main menu
        Client.mainScreen = PacmanMainMenu::new;

        //register client events
        Events.CLIENT_INIT.register("pacman:init", () -> {
            //set the GUI skin to minimal dark
            GUISkin.setCurrentSkin(new Resource("data/gui_skins/minimal_dark.json"));

            //load the high scores from the file
            highScores.clear();
            byte[] data = IOUtils.readFileCompressed(IOUtils.ROOT_FOLDER.resolve("highscores.moon"));
            if (data != null) {
                String[] lines = new String(data).split("\n");
                for (String line : lines) {
                    if (!line.isBlank())
                        highScores.add(line);
                }
            }

            //sort the scores
            highScores.sort((a, b) -> {
                int i = a.lastIndexOf(":");
                int j = b.lastIndexOf(":");
                String aScore = a.substring(i + 1);
                String bScore = b.substring(j + 1);

                int compare = Integer.compare(Integer.parseInt(bScore), Integer.parseInt(aScore));
                if (compare != 0)
                    return compare;

                String aName = a.substring(0, i);
                String bName = b.substring(0, j);
                return aName.compareTo(bName);
            });
        });

        Events.CLIENT_EXIT.register("pacman:save_scores", Main::saveScores);

        //start the engine
        new Cinnamon(args).run();
    }

    public static void saveScores() {
        //save the high scores to the file
        String data = String.join("\n", highScores);
        IOUtils.writeFileCompressed(IOUtils.ROOT_FOLDER.resolve("highscores.moon"), data.getBytes());
    }
}
