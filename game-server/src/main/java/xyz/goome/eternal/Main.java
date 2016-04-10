package xyz.goome.eternal;

import xyz.goome.eternal.net.GameServer;

/**
 * Created by matrix on 16/3/26.
 */
public class Main {

    public static void main(String[] args) {
        new GameServer(8080).run();
    }

}
