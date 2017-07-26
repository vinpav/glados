package fr.vinpav.glados.plugin;


import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

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
        play(Integer.parseInt(getConfiguration().getProperty("playlist.default.entry")));
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

    private void play(int playlistEntry) {
        System.out.println("[Glados] > Switchin to " + getConfiguration().getProperty("playlist.entry.name." + playlistEntry));
        String url = getConfiguration().getProperty("playlist.entry.url." + playlistEntry);
        if (url != null) {
            try {
                playRadioStream(url);
            } catch (IOException | JavaLayerException e) {
                logger.error("[Glados] > Looks like the radio is broken...", e);
            }
        } else {
            System.out.println("[Glados] > Ah ah ah... There's no playlist entry at number " + playlistEntry);
        }
    }

    @Override
    public void execute(List<String> command) {
        switch (command.get(0)) {
            case "help": {
                System.out.println(getCommandList());
                break;
            }
            case "list": {
                System.out.println(getPlaylist());
                break;
            }
            case "startup": {
                play(Integer.parseInt(command.get(1)));
                break;
            }
            default: {
                System.out.println("[Glados] > I don't know what you're talking about.");
            }
        }
    }

    private String getCommandList() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[Glados] > Here's the " + describe() + " command list\n");
        stringBuilder.append("radio_plugin help;                  : this help.\n");
        stringBuilder.append("radio_plugin list;                  : the radio playlist.\n");
        stringBuilder.append("radio_plugin startup [channel number]; : startup the specified entry from the playlist.\n");

        return stringBuilder.toString();
    }

    private String getPlaylist() {
        int entryCount = Integer.parseInt(getConfiguration().getProperty("playlist.entry.count"));
        StringBuilder playlist = new StringBuilder();

        for (int i = 1; i<entryCount; i++) {
            playlist.append(i);
            playlist.append("/ ");
            playlist.append(getConfiguration().getProperty("playlist.entry.name." + i));
            playlist.append("\n");
        }

        return playlist.toString();
    }
}
