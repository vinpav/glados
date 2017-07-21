package fr.vinpav.glados;

import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Glados {

    private URL streamUrl;

    public Glados(String aStreamUrl) {
        this.assignURL(aStreamUrl);
    }

    public void start() throws IOException, NoPlayerException {
        Player player = Manager.createPlayer(streamUrl);
        player.start();
    }

    public static void main(String[] args) {
        System.out.println("Starting Glados...");
        Glados myGlados = new Glados("http://mp3lg3.scdn.arkena.com/10489/europe1.mp3");
        try {
            myGlados.start();
            System.out.println("Glados is started.");
        } catch (IOException | NoPlayerException e) {
            e.printStackTrace();
            System.out.println("Fatal error. Program terminated");
            System.exit(1);
        }
    }

    public void assignURL(String aStreamUrl) throws IllegalArgumentException {
        System.out.println("Assigning URL : " + aStreamUrl);
        if (aStreamUrl == null) {
            throw new IllegalArgumentException("Glados cannot read null URL...");
        }
        try {
            this.streamUrl = new URL(aStreamUrl);
            System.out.println("URL assigned.");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Glados cannot read the malformed URL " + aStreamUrl);
        }
    }
}
