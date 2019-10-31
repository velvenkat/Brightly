package com.digital_easy.info_share.Modules;

import android.net.Uri;

import java.util.List;

public class MultipleViewCreationModel {
    public int TYPE;
    //Audio
    public Uri URI_Val;
    public String file_path;

    public String url_val;

    //Image
    public List<MultipleImageModel> multipleImageModelListObj;
    public String remove_img_id = "";
    public int Thumbnail_sel_pos;

    public String txt_Val;

    public String name_val;
    public String comp_val;
    public String title_val;
    public String mob__val;
    public String off_val;
    public String email_val;
    public String notes_val;


    //Video
    public Uri Video_file_uri;


}
