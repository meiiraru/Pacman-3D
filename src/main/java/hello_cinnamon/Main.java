package hello_cinnamon;

import cinnamon.Cinnamon;
import cinnamon.Client;

public class Main {

    public static void main(String... args) {
        //setup cinnamon flags
        Cinnamon.TITLE = "Hello Cinnamon";
        Cinnamon.NAMESPACE = "hello_cinnamon";

        //custom main menu
        Client.mainScreen = HelloMainMenu::new;

        //start the engine
        new Cinnamon(args).run();
    }
}
