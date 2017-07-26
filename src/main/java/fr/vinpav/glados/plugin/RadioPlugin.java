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

        player = new Player (urlConnection.getInputStream());
        player.play();
    }

    private void play(int playlistEntry) {
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
        if (command.size() == 1) {
            execute(command.get(0));
        } else if (command.size() == 2) {
            execute(command.get(0), command.get(1));
        }
    }

    private void execute(String order) {
        switch (order) {
            case "help": {
                System.out.println(getCommandList());
                break;
            }
            case "list": {
                System.out.println(getPlaylist());
                break;
            }
            default: {
                System.out.println("[Glados] > I don't know what you're talking about.");
            }
        }
    }

    private void execute(String order, String object){
        switch (order) {
            case "play": {
                play(Integer.parseInt(object));
                break;
            }
            default: {
                System.out.println("[Glados] > I don't know what you're talking about.");
            }
        }
    }

    private String getCommandList() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[Glados] > Here's the " + describe() + " command list ?\n");
        stringBuilder.append("radio_plugin help;                  : this help.\n");
        stringBuilder.append("radio_plugin list;                  : the radio playlist.\n");
        stringBuilder.append("radio_plugin play [channel number]; : play the specified entry from the playlist.\n");
        stringBuilder.append("Type 'radio_plugin start/stop;' to turn the radio on/off.");

        return stringBuilder.toString();
    }

    private String getPlaylist() {
        int entryCount = Integer.parseInt(getConfiguration().getProperty("playlist.entry.count"));
        StringBuilder playlist = new StringBuilder();

        for (int i = 1; i<entryCount; i++) {
            playlist.append(i + "/ " + getConfiguration().getProperty("playlist.entry.name." + i));
        }

        return playlist.toString();
    }
}
