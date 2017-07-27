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

    int currentChannel = 0;

    public RadioPlugin() {
        setName("radio_plugin");
    }

    @Override
    public void shutdown() {
        player.close();
        interrupt();
    }

    @Override
    public void run() {
        if (currentChannel == 0) {
            currentChannel = Integer.parseInt(getConfiguration().getProperty("playlist.default.entry"));
        }
        play();
    }

    private void playRadioStream (String spec) throws IOException, JavaLayerException
    {
        URLConnection urlConnection = new URL(spec).openConnection();
        urlConnection.connect();

        if (player != null) {
            player.close();
        }
        player = new Player (urlConnection.getInputStream());
        player.play();
    }

    private void play() {
        String url = getConfiguration().getProperty("playlist.entry.url." + currentChannel);
        if (url != null) {
            try {
                playRadioStream(url);
            } catch (IOException | JavaLayerException e) {
                logger.error("[Glados] > Looks like the radio is broken...", e);
            }
        } else {
            logger.info("[Glados] > Ah ah ah... There's no playlist entry at number " + currentChannel);
        }
    }

    public int getCurrentChannel() {
        return currentChannel;
    }

    public void setCurrentChannel(int currentChannel) {
        this.currentChannel = currentChannel;
    }

}
