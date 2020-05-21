package it.polimi.ingsw.psp1.santorini.gui;

import it.polimi.ingsw.psp1.santorini.network.Client;

public class MainGui {

    public static void main(String[] args) throws InterruptedException {
        Client client = new Client();
        GuiServerHandler guiServerHandler = new GuiServerHandler(client);

        client.setServerHandler(guiServerHandler);

        Gui.launch(args);
    }

}
