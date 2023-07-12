package in.mplay.app.json;

import java.io.Serializable;

public class JsonMovies implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id, title, description, quality, language, channel, category, img_link, d_link, date;

    public JsonMovies(String id,String title, String description, String quality, String language, String channel, String category, String img_link, String d_link, String date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.quality = quality;
        this.language = language;
        this.channel = channel;
        this.category = category;
        this.img_link = img_link;
        this.d_link = d_link;
        this.date = date;
    }
    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getQuality() {
        return quality;
    }
    public String getLanguage() {
        return language;
    }
    public String getChannel() {
        return channel;
    } public String getCategory() {
        return category;
    }
    public String getImg_link() {
        return img_link;
    }
    public String getD_link() {
        return d_link;
    }
    public String getDate() {
        return date;
    }

}