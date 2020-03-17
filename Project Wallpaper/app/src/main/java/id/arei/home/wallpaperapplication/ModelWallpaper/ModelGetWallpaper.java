package id.arei.home.wallpaperapplication.ModelWallpaper;

import com.google.gson.annotations.SerializedName;

public class ModelGetWallpaper {
    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String contentImage;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentImage() {
        return contentImage;
    }

    public void setContentImage(String contentImage) {
        this.contentImage = contentImage;
    }
}
