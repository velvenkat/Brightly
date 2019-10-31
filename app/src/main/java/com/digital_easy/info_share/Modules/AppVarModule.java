package com.digital_easy.info_share.Modules;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppVarModule implements Parcelable {


    protected AppVarModule(Parcel in) {
        share_setting_img = in.readParcelable(GeneralVarModel.class.getClassLoader());
        uncomment_img = in.readParcelable(GeneralVarModel.class.getClassLoader());
        comment_img = in.readParcelable(GeneralVarModel.class.getClassLoader());
        level3_default_img = in.readParcelable(GeneralVarModel.class.getClassLoader());
        level3_default_create_image = in.readParcelable(GeneralVarModel.class.getClassLoader());
        prof_default_img = in.readParcelable(GeneralVarModel.class.getClassLoader());
        level2_default_img = in.readParcelable(GeneralVarModel.class.getClassLoader());
        level1_default_img = in.readParcelable(GeneralVarModel.class.getClassLoader());
        sharepic = in.readParcelable(GeneralVarModel.class.getClassLoader());
        level3title = in.readParcelable(GeneralVarModel.class.getClassLoader());
        level2title = in.readParcelable(GeneralVarModel.class.getClassLoader());
        level1title = in.readParcelable(GeneralVarModel.class.getClassLoader());
        level1_filter = in.createTypedArrayList(GeneralVarModel.CREATOR);
        menu = in.createTypedArrayList(GeneralVarModel.CREATOR);
        level2_filter = in.createTypedArrayList(GeneralVarModel.CREATOR);
        level3_audio_img = in.readParcelable(GeneralVarModel.class.getClassLoader());
        level3_youtube_img = in.readParcelable(GeneralVarModel.class.getClassLoader());
        level3_file_img = in.readParcelable(GeneralVarModel.class.getClassLoader());
    }

    public static final Creator<AppVarModule> CREATOR = new Creator<AppVarModule>() {
        @Override
        public AppVarModule createFromParcel(Parcel in) {
            return new AppVarModule(in);
        }

        @Override
        public AppVarModule[] newArray(int size) {
            return new AppVarModule[size];
        }
    };

    public GeneralVarModel getShare_setting_img() {
        return share_setting_img;
    }

    public void setShare_setting_img(GeneralVarModel share_setting_img) {
        this.share_setting_img = share_setting_img;
    }

    @SerializedName("share_setting_img")
    @Expose
    GeneralVarModel share_setting_img;

    public GeneralVarModel getUncomment_img() {
        return uncomment_img;
    }

    public void setUncomment_img(GeneralVarModel uncomment_img) {
        this.uncomment_img = uncomment_img;
    }

    public GeneralVarModel getComment_img() {
        return comment_img;
    }

    public void setComment_img(GeneralVarModel comment_img) {
        this.comment_img = comment_img;
    }

    @SerializedName("uncomment")
    @Expose
    GeneralVarModel uncomment_img;

    @SerializedName("comment")
    @Expose
    GeneralVarModel comment_img;

    @SerializedName("level3_default_img")
    @Expose
    GeneralVarModel level3_default_img;

    public GeneralVarModel getLevel3_default_create_image() {
        return level3_default_create_image;
    }

    public void setLevel3_default_create_image(GeneralVarModel level3_default_create_image) {
        this.level3_default_create_image = level3_default_create_image;
    }

    @SerializedName("level3_default_create_image")
    @Expose
    GeneralVarModel level3_default_create_image;

    public GeneralVarModel getProf_default_img() {
        return prof_default_img;
    }

    public void setProf_default_img(GeneralVarModel prof_default_img) {
        this.prof_default_img = prof_default_img;
    }

    @SerializedName("profile_default_img")
    @Expose
    GeneralVarModel prof_default_img;


    public GeneralVarModel getLevel3_default_img() {
        return level3_default_img;
    }

    public void setLevel3_default_img(GeneralVarModel level3_default_img) {
        this.level3_default_img = level3_default_img;
    }

    public GeneralVarModel getLevel2_default_img() {
        return level2_default_img;
    }

    public void setLevel2_default_img(GeneralVarModel level2_default_img) {
        this.level2_default_img = level2_default_img;
    }

    public GeneralVarModel getLevel1_default_img() {
        return level1_default_img;
    }

    public void setLevel1_default_img(GeneralVarModel level1_default_img) {
        this.level1_default_img = level1_default_img;
    }

    public GeneralVarModel getSharepic() {
        return sharepic;
    }

    public void setSharepic(GeneralVarModel sharepic) {
        this.sharepic = sharepic;
    }

    public GeneralVarModel getLevel3title() {
        return level3title;
    }

    public void setLevel3title(GeneralVarModel level3title) {
        this.level3title = level3title;
    }

    public GeneralVarModel getLevel2title() {
        return level2title;
    }

    public void setLevel2title(GeneralVarModel level2title) {
        this.level2title = level2title;
    }

    public GeneralVarModel getLevel1title() {
        return level1title;
    }

    public void setLevel1title(GeneralVarModel level1title) {
        this.level1title = level1title;
    }

    public List<GeneralVarModel> getLevel1_filter() {
        return level1_filter;
    }

    public void setLevel1_filter(List<GeneralVarModel> level1_filter) {
        this.level1_filter = level1_filter;
    }

    public List<GeneralVarModel> getMenu() {
        return menu;
    }

    public void setMenu(List<GeneralVarModel> menu) {
        this.menu = menu;
    }

    public List<GeneralVarModel> getLevel2_filter() {
        return level2_filter;
    }

    public void setLevel2_filter(List<GeneralVarModel> level2_filter) {
        this.level2_filter = level2_filter;
    }

    public GeneralVarModel getLevel3_audio_img() {
        return level3_audio_img;
    }

    public void setLevel3_audio_img(GeneralVarModel level3_audio_img) {
        this.level3_audio_img = level3_audio_img;
    }

    public GeneralVarModel getLevel3_youtube_img() {
        return level3_youtube_img;
    }

    public void setLevel3_youtube_img(GeneralVarModel level3_youtube_img) {
        this.level3_youtube_img = level3_youtube_img;
    }

    public GeneralVarModel getLevel3_file_img() {
        return level3_file_img;
    }

    public void setLevel3_file_img(GeneralVarModel level3_file_img) {
        this.level3_file_img = level3_file_img;
    }

    @SerializedName("level2_default_img")
    @Expose
    GeneralVarModel level2_default_img;

    @SerializedName("level1_default_img")
    @Expose
    GeneralVarModel level1_default_img;

    @SerializedName("share_icon")
    @Expose
    GeneralVarModel sharepic;

    @SerializedName("level3title")
    @Expose
    GeneralVarModel level3title;

    @SerializedName("level2title")
    @Expose
    GeneralVarModel level2title;

    @SerializedName("level1title")
    @Expose
    GeneralVarModel level1title;

    @SerializedName("level1_filter")
    @Expose
    List<GeneralVarModel> level1_filter;

    @SerializedName("menu")
    @Expose
    List<GeneralVarModel> menu;

    @SerializedName("level2_filter")
    @Expose
    List<GeneralVarModel> level2_filter;

    @SerializedName("level3_audio_img")
    @Expose
    GeneralVarModel level3_audio_img;

    @SerializedName("level3_youtube_img")
    @Expose
    GeneralVarModel level3_youtube_img;

    @SerializedName("level3_file_img")
    @Expose
    GeneralVarModel level3_file_img;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(share_setting_img, flags);
        dest.writeParcelable(uncomment_img, flags);
        dest.writeParcelable(comment_img, flags);
        dest.writeParcelable(level3_default_img, flags);
        dest.writeParcelable(level3_default_create_image, flags);
        dest.writeParcelable(prof_default_img, flags);
        dest.writeParcelable(level2_default_img, flags);
        dest.writeParcelable(level1_default_img, flags);
        dest.writeParcelable(sharepic, flags);
        dest.writeParcelable(level3title, flags);
        dest.writeParcelable(level2title, flags);
        dest.writeParcelable(level1title, flags);
        dest.writeTypedList(level1_filter);
        dest.writeTypedList(menu);
        dest.writeTypedList(level2_filter);
        dest.writeParcelable(level3_audio_img, flags);
        dest.writeParcelable(level3_youtube_img, flags);
        dest.writeParcelable(level3_file_img, flags);
    }
}
