package fr.vinpav.glados.plugin;


import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class RadioPlugin extends Plugin {
    protected static final Logger logger = LoggerFactory.getLogger(RadioPlugin.class);

    Player player;

    public RadioPlugin() {
    }

    @Override
    public void shutdown() {
        logger.info(describe() + " is shutting down.");
        player.close();
        interrupt();
    }

    @Override
    public void run() {
        logger.info("RadioPlugin running...");
        try {
            playRadioStream(" http://e1-live-mp3-128.scdn.arkena.com/europe1.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
    }

    private void playRadioStream (String spec) throws IOException, JavaLayerException
    {
        URLConnection urlConnection = new URL(spec).openConnection();
        urlConnection.connect();

        player = new Player (urlConnection.getInputStream());
        player.play();
    }
}
