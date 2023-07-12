package in.mplay.app.json;

import java.io.Serializable;

public class JsonChannels implements Serializable {
    private static final long serialVersionUID = 1L;
    private String channel_name, channel_logo;

    public JsonChannels(String channel_name, String channel_logo) {
        this.channel_name = channel_name;
        this.channel_logo = channel_logo;
    }
    public String getChannel_name() {
        return channel_name;
    }
    public String getChannel_logo() {
        return channel_logo;
    }

}