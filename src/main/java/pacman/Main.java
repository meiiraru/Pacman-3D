package pacman;

import cinnamon.Cinnamon;
import cinnamon.Client;

public class Main {

    public static void main(String... args) {
        //setup cinnamon flags
        Cinnamon.TITLE = "Pacman 3D";
        Cinnamon.NAMESPACE = "pacman";

        //inject cli args
        String[] toAdd = {"--window-title-fps"};
        String[] newArgs = new String[args.length + toAdd.length];
        System.arraycopy(args, 0, newArgs, 0, args.length);
        System.arraycopy(toAdd, 0, newArgs, args.length, toAdd.length);

        //set the main menu
        Client.mainScreen = PacmanMainMenu::new;

        //start the engine
        new Cinnamon(newArgs).run();
    }
}
