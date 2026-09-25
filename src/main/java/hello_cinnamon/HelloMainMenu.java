package hello_cinnamon;

import cinnamon.gui.Screen;
import cinnamon.gui.screens.MainMenu;
import cinnamon.gui.screens.SettingsScreen;
import cinnamon.gui.widgets.WidgetList;
import cinnamon.gui.widgets.types.Button;
import cinnamon.gui.widgets.types.Label;
import cinnamon.render.MatrixStack;
import cinnamon.render.batch.VertexConsumer;
import cinnamon.text.HoverEvent;
import cinnamon.text.Style;
import cinnamon.text.Text;
import cinnamon.utils.Alignment;
import cinnamon.utils.Colors;
import org.joml.Math;

public class HelloMainMenu extends Screen {

    @Override
    public void init() {
        //superclass init
        super.init();

        //title label
        Label title = new Label(width / 2, height / 4, Text.of("Cinnamon").withStyle(Style.EMPTY.hoverEvent(new HoverEvent.ShowText(Text.of("Hello")))), Alignment.CENTER);
        title.setTextScale(2f);
        addWidget(title);

        //create a widget list to hold our main menu buttons
        WidgetList mainMenu = new WidgetList(width / 2, height / 2, width - 20, height / 3, 4);
        mainMenu.setAlignment(Alignment.TOP_CENTER);
        addWidget(mainMenu);

        //add buttons to the main menu

        //play button
        Button play = new MainMenu.MainButton(Text.of("Start Game"), button -> new HelloWorld().init());
        play.setRenderBackground(false);
        mainMenu.addWidget(play);

        //engine settings button
        Button settings = new MainMenu.MainButton(Text.of("Engine Settings"), button -> client.setScreen(new SettingsScreen(this)));
        settings.setRenderBackground(false);
        mainMenu.addWidget(settings);

        //exit button
        Button exit = new MainMenu.MainButton(Text.of("Exit").withStyle(Style.EMPTY.color(0xFFFF7272)), button -> client.window.exit());
        exit.setRenderBackground(false);
        exit.setTooltip(Text.of("Goodbye!"));
        mainMenu.addWidget(exit);
    }

    @Override
    protected void renderChildren(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        //render the widgets
        super.renderChildren(matrices, mouseX, mouseY, delta);

        //render minecraft-like splash text
        Text splashText = Text.of("Hello Cinnamon!").withStyle(Style.EMPTY.color(Colors.YELLOW));

        //transform our rendering matrix stack to make the splash text rotate and scale
        matrices.pushMatrix();
        matrices.translate(width / 2f + 40, height / 4f + 5, 0);
        matrices.scale((Math.sin((client.ticks + delta) * 0.1f) / 2f + 0.5f + 1f) * 0.5f);
        matrices.rotateZ(-22.5f);
        splashText.render(VertexConsumer.MAIN, matrices, 0, 0, Alignment.CENTER);
        matrices.popMatrix();
    }
}
