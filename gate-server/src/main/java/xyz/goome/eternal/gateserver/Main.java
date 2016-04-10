package xyz.goome.eternal.gateserver;

/**
 * Created by matrix on 16/4/8.
 */
public class Main {

    public static void main(String[] args) {
        GateServer gateServer = new GateServer(8081);
        new Thread(gateServer).start();

        System.out.println("gate server start...");
    }

}
