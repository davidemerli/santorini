package it.polimi.ingsw.psp1.santorini;

import it.polimi.ingsw.psp1.santorini.network.server.Server;

public class Main {

    public static void main(String[] args) {
        Server server = new Server(34567);
        new Thread(server).start();
    }
}

