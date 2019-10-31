package com.digital_easy.info_share.Custom;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.danikula.videocache.HttpProxyCacheServer;
import com.digital_easy.info_share.Activities.BrightlyNavigationActivity;
import com.digital_easy.info_share.Adapters.Thumbnail_ImageAdapter;
import com.digital_easy.info_share.Application.Brightly;
import com.digital_easy.info_share.Application.RealmModel;
import com.digital_easy.info_share.Fragments.CreateMultimediaCard;
import com.digital_easy.info_share.Modules.ContactHelper;
import com.digital_easy.info_share.Modules.ContactHelperModule;
import com.digital_easy.info_share.Modules.JSONObjectReqModule;
import com.digital_easy.info_share.Modules.JSONReqMVModule;
import com.digital_easy.info_share.Modules.MultipleViewCreationModel;
import com.digital_easy.info_share.Modules.MultipleImageModel;
import com.digital_easy.info_share.R;
import com.digital_easy.info_share.Utils.TimeFormat;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.youtube.player.YouTubePlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdapterMultipleViewCreation extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<MultipleViewCreationModel> multipleViewCreationModelList;

    AppCompatActivity _activity;

    MultiviewAdapterListener mListener;
    boolean isCreateCard = false;

    public AdapterMultipleViewCreation(AppCompatActivity activity, ArrayList<MultipleViewCreationModel> model_list, MultiviewAdapterListener listener, boolean _isCreateCard) {
        multipleViewCreationModelList = model_list;
        _activity = activity;
        mListener = listener;
        this.isCreateCard = _isCreateCard;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        MultipleViewCreationModel multiple_creation_modelObj = multipleViewCreationModelList.get(viewType);
        int TYPE = multiple_creation_modelObj.TYPE;
        View crt_view;
        CreateMultimediaCard.enum_MultipleViewType enum_multi_types = CreateMultimediaCard.enum_MultipleViewType.values()[TYPE];
        switch (enum_multi_types) {
            case _ADD:

                crt_view = inflater.inflate(R.layout.lo_btn_add_to_create, parent, false);

                return new Add_Holder(crt_view);
            // Return a new holder instance
            case IMAGE:

                // Inflate the custom layout
                crt_view = inflater.inflate(R.layout.lo_image_create, parent, false);
                return new ImageViewHolder(crt_view, viewType);
            case TITLE:

                // Inflate the custom layout
                crt_view = inflater.inflate(R.layout.lo_title_create, parent, false);
                return new TitleViewHolder(crt_view, viewType);
            case AUDIO:

                // Inflate the custom layout
                crt_view = inflater.inflate(R.layout.lo_audio_create, parent, false);
                return new AudioViewHolder(crt_view, viewType);
            case VIDEO:
                crt_view = inflater.inflate(R.layout.lo_video_create, parent, false);
                return new VideoViewHolder(crt_view, viewType);
            case WEBLINK:
                crt_view = inflater.inflate(R.layout.lo_weblink_create, parent, false);
                return new WeblinkHolder(crt_view, viewType);
            case CONTACT:
                crt_view = inflater.inflate(R.layout.lo_contact_create, parent, false);
                return new ContactHolder(crt_view, viewType);
            case UTUBE:
                crt_view = inflater.inflate(R.layout.lo_youtube_create, parent, false);
                return new YouTubeHolder(crt_view, viewType);
            case TEXT:
                crt_view = inflater.inflate(R.layout.lo_text_crt, parent, false);
                return new TextViewHolder(crt_view, viewType);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MultipleViewCreationModel multiple_creation_modelObj = multipleViewCreationModelList.get(position);
        int TYPE = multiple_creation_modelObj.TYPE;
        CreateMultimediaCard.enum_MultipleViewType enum_multi_types = CreateMultimediaCard.enum_MultipleViewType.values()[TYPE];
        JSONReqMVModule jsonReqMVModuleObj;
        int VIEWPOS;
        switch (enum_multi_types) {
            case _ADD:
                if (position == 0) {
                    ((Add_Holder) holder).btn_del.setVisibility(View.INVISIBLE);
                }
                ((Add_Holder) holder).btn_del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.del_view(position);
                    }
                });

                ((Add_Holder) holder).btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        PopupMenu popup = new PopupMenu(holder.itemView.getContext(), v);
                        //inflating menu from xml resource
                        popup.inflate(R.menu.vw_cr_menu);
                        //adding click listener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {

                                    case R.id.menu_Title:

                                        mListener.on_type_choosed(position + 1, CreateMultimediaCard.enum_MultipleViewType.TITLE.ordinal());

                                        return true;
                                    case R.id.menu_Image:
                                        mListener.on_type_choosed(position + 1, CreateMultimediaCard.enum_MultipleViewType.IMAGE.ordinal());

                                        return true;
                                    case R.id.menu_Audio:
                                        mListener.on_type_choosed(position + 1, CreateMultimediaCard.enum_MultipleViewType.AUDIO.ordinal());
                                        return true;
                                    case R.id.menu_Video:
                                        mListener.on_type_choosed(position + 1, CreateMultimediaCard.enum_MultipleViewType.VIDEO.ordinal());
                                        return true;
                                    case R.id.menu_utube:
                                        mListener.on_type_choosed(position + 1, CreateMultimediaCard.enum_MultipleViewType.UTUBE.ordinal());
                                        return true;
                                    case R.id.menu_weblink:
                                        mListener.on_type_choosed(position + 1, CreateMultimediaCard.enum_MultipleViewType.WEBLINK.ordinal());
                                        return true;
                                    case R.id.menu_contact:
                                        mListener.on_type_choosed(position + 1, CreateMultimediaCard.enum_MultipleViewType.CONTACT.ordinal());
                                        return true;
                                    case R.id.menu_text:
                                        mListener.on_type_choosed(position + 1, CreateMultimediaCard.enum_MultipleViewType.TEXT.ordinal());
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });
                        //displaying the popup
                        popup.show();
                    }
                });
                break;
            case AUDIO:
                if (multiple_creation_modelObj.URI_Val != null || multiple_creation_modelObj.file_path != null) {
                    ((AudioViewHolder) holder).text_audioFile.setVisibility(View.GONE);
                    ((AudioViewHolder) holder).rl_audio_player.setVisibility(View.VISIBLE);
                    if (multiple_creation_modelObj.URI_Val != null) {
                        Uri AudioFileUri = multiple_creation_modelObj.URI_Val;
                        if (!AudioFileUri.toString().trim().equals("")) {
                            ((AudioViewHolder) holder).setMediaPlayer(AudioFileUri, null, true);

                            // mListener.play_audio(AudioFileUri, null, isCreateCard);
                        }
                    } else {
                        String AudioFilePath = multiple_creation_modelObj.file_path;
                        if (!AudioFilePath.trim().equals("")) {
                            ((AudioViewHolder) holder).setMediaPlayer(null, AudioFilePath, true);
                            //mListener.play_audio(null, AudioFilePath, isCreateCard);
                        }
                    }
                }
                break;
            case IMAGE:
                List<MultipleImageModel> list_multi_image_obj = multiple_creation_modelObj.multipleImageModelListObj;

                if (list_multi_image_obj.size() == 1) {


                    Uri uri = new Uri.Builder()
                            .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                            .path(String.valueOf(R.drawable.no_image_available))
                            .build();
                    final ImageRequest imageRequest2 =
                            ImageRequestBuilder.newBuilderWithSource(uri)

                                    .build();
                    ((ImageViewHolder) holder).imageView.setImageRequest(imageRequest2);

                } else {

                    set_card_image(holder.itemView.getContext(), ((ImageViewHolder) holder).imageView, list_multi_image_obj.get(multiple_creation_modelObj.Thumbnail_sel_pos).getImg_url());
                }

                ((ImageViewHolder) holder).set_thumbnail_adapter(multiple_creation_modelObj.multipleImageModelListObj, position);
                ((ImageViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (list_multi_image_obj.size() > 1)
                            mListener.call_image_dialog(position);
                        else
                            mListener.call_action(position, CreateMultimediaCard.enum_MultipleViewType.IMAGE.ordinal());
                    }
                });
                break;
            case VIDEO:

                Uri file_uri = multiple_creation_modelObj.Video_file_uri;

                if (file_uri != null) {
                    if (!isCreateCard && file_uri.toString().contains("http")) {
                        HttpProxyCacheServer proxy = Brightly.getProxy(_activity);
                        String proxyUrl = proxy.getProxyUrl(file_uri.toString());
                        ((VideoViewHolder) holder).video_vw.setVideoURI(Uri.parse(proxyUrl));
                    } else {
                        ((VideoViewHolder) holder).video_vw.setVideoURI(file_uri);
                    }
                    // ((VideoViewHolder) holder).video_vw.setZOrderOnTop(true);
                    ((VideoViewHolder) holder).img_video_upload.setVisibility(View.INVISIBLE);
                    ((VideoViewHolder) holder).video_vw.setVisibility(View.VISIBLE);
                    //MediaController mc = new MediaController(getContext());


                    ((VideoViewHolder) holder).video_vw.seekTo(1);
                    ((VideoViewHolder) holder).play_pause.setVisibility(View.VISIBLE);
                    ((VideoViewHolder) holder).play_pause.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (((VideoViewHolder) holder).video_vw.isPlaying()) {
                                ((VideoViewHolder) holder).play_pause.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.play_btn));
                                ((VideoViewHolder) holder).video_vw.pause();
                            } else {
                                ((VideoViewHolder) holder).play_pause.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.pause));
                                ((VideoViewHolder) holder).video_vw.start();
                            }
                        }
                    });
                    ((VideoViewHolder) holder).video_vw.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            ((VideoViewHolder) holder).play_pause.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.play_btn));
                        }
                    });
                    ((VideoViewHolder) holder).play_pause.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.play_btn));
                }

                ((VideoViewHolder) holder).video_contr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.call_action(position, CreateMultimediaCard.enum_MultipleViewType.VIDEO.ordinal());
                    }
                });


                ((VideoViewHolder) holder).img_video_upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.call_action(position, CreateMultimediaCard.enum_MultipleViewType.VIDEO.ordinal());
                    }
                });
                break;
            case UTUBE:

                ((YouTubeHolder) holder).img_utube.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/"));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setPackage("com.google.android.youtube");
                        view.getContext().startActivity(intent);
                    }
                });
                ((YouTubeHolder) holder).edt_url.setText(multiple_creation_modelObj.txt_Val);
                break;
            case TEXT:
                ((TextViewHolder) holder).edtDesc.setText(multiple_creation_modelObj.txt_Val);


                break;
            case TITLE:
                ((TitleViewHolder) holder).edt_title.setText(multiple_creation_modelObj.txt_Val);
                break;
            case WEBLINK:
                ((WeblinkHolder) holder).edt_url.setText(multiple_creation_modelObj.txt_Val);
                break;
            case CONTACT:
                ((ContactHolder) holder).edt_Name.setText(multiple_creation_modelObj.name_val);
                ((ContactHolder) holder).edt_comp_name.setText(multiple_creation_modelObj.comp_val);
                ((ContactHolder) holder).edt_title.setText(multiple_creation_modelObj.title_val);
                ((ContactHolder) holder).edt_mob_no.setText(multiple_creation_modelObj.mob__val);
                ((ContactHolder) holder).edt_off_no.setText(multiple_creation_modelObj.off_val);
                ((ContactHolder) holder).edt_email.setText(multiple_creation_modelObj.email_val);
                ((ContactHolder) holder).edt_Description.setText(multiple_creation_modelObj.notes_val);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return multipleViewCreationModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class TextViewHolder extends RecyclerView.ViewHolder {

        EditText edtDesc;
        int AdapterPos;

        public TextViewHolder(View itemView, int Position) {
            super(itemView);
            AdapterPos = Position;
            edtDesc = itemView.findViewById(R.id.create_cardDescription);
            edtDesc.addTextChangedListener(new customTextWatcher(edtDesc, CreateMultimediaCard.enum_MultipleViewType.TEXT.ordinal(), AdapterPos, 1));

        }


    }

    class Add_Holder extends RecyclerView.ViewHolder {

        ImageView btn_add;
        ImageView btn_del;
        Button btn_save;

        public Add_Holder(View itemView) {
            super(itemView);
            btn_add = itemView.findViewById(R.id.btn_add);
            btn_del = itemView.findViewById(R.id.btn_del);
            btn_save = itemView.findViewById(R.id.btn_save);
        }
    }

    class WeblinkHolder extends RecyclerView.ViewHolder {

        EditText edt_url;

        public WeblinkHolder(View itemView, int AdapterPos) {
            super(itemView);
            edt_url = itemView.findViewById(R.id.create_cardURL);
            edt_url.addTextChangedListener(new customTextWatcher(edt_url, CreateMultimediaCard.enum_MultipleViewType.WEBLINK.ordinal(), AdapterPos, 1));
        }
    }

    class YouTubeHolder extends RecyclerView.ViewHolder {

        EditText edt_url;
        ImageView img_utube;

        public YouTubeHolder(View itemView, int AdapterPos) {
            super(itemView);
            edt_url = itemView.findViewById(R.id.create_cardURL);
            img_utube = itemView.findViewById(R.id.image_youtube_link);
            edt_url.addTextChangedListener(new customTextWatcher(edt_url, CreateMultimediaCard.enum_MultipleViewType.UTUBE.ordinal(), AdapterPos, 1));
        }
    }

    class ContactHolder extends RecyclerView.ViewHolder {

        EditText edt_title, edt_comp_name, edt_mob_no, edt_off_no, edt_email, edt_Name, edt_Description;

        public ContactHolder(View itemView, int AdapterPos) {
            super(itemView);
            edt_comp_name = itemView.findViewById(R.id.edt_comp_name);
            edt_title = itemView.findViewById(R.id.edt_title);
            edt_mob_no = itemView.findViewById(R.id.edt_mob_no);
            edt_off_no = itemView.findViewById(R.id.edt_off_no);
            edt_email = itemView.findViewById(R.id.edt_email);
            edt_Name = itemView.findViewById(R.id.edt_name);
            edt_Description = itemView.findViewById(R.id.create_cardDescription);
            edt_Name.addTextChangedListener(new customTextWatcher(edt_Name, CreateMultimediaCard.enum_MultipleViewType.CONTACT.ordinal(), AdapterPos, 1));
            edt_comp_name.addTextChangedListener(new customTextWatcher(edt_comp_name, CreateMultimediaCard.enum_MultipleViewType.CONTACT.ordinal(), AdapterPos, 2));
            edt_title.addTextChangedListener(new customTextWatcher(edt_title, CreateMultimediaCard.enum_MultipleViewType.CONTACT.ordinal(), AdapterPos, 3));
            edt_mob_no.addTextChangedListener(new customTextWatcher(edt_mob_no, CreateMultimediaCard.enum_MultipleViewType.CONTACT.ordinal(), AdapterPos, 4));
            edt_off_no.addTextChangedListener(new customTextWatcher(edt_off_no, CreateMultimediaCard.enum_MultipleViewType.CONTACT.ordinal(), AdapterPos, 5));
            edt_email.addTextChangedListener(new customTextWatcher(edt_email, CreateMultimediaCard.enum_MultipleViewType.CONTACT.ordinal(), AdapterPos, 6));
            edt_Description.addTextChangedListener(new customTextWatcher(edt_Description, CreateMultimediaCard.enum_MultipleViewType.CONTACT.ordinal(), AdapterPos, 7));
        }


    }

    class customTextWatcher implements TextWatcher {

        EditText edt_to_add_tw;
        int TYPE;
        int AdapterPos;
        int Cont_Edt_Type;

        public customTextWatcher(EditText editText, int enum_type, int Position, int Edit_Type) {
            edt_to_add_tw = editText;
            AdapterPos = Position;
            TYPE = enum_type;
            Cont_Edt_Type = Edit_Type;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TYPE != CreateMultimediaCard.enum_MultipleViewType.CONTACT.ordinal()) {
                JSONReqMVModule model_Obj = new JSONReqMVModule();
                model_Obj.value = edt_to_add_tw.getText().toString();
                CreateMultimediaCard.enum_MultipleViewType enum_multi_types = CreateMultimediaCard.enum_MultipleViewType.values()[TYPE];

                switch (enum_multi_types) {
                    case TITLE:
                        model_Obj.type = "title";
                        break;
                    case TEXT:
                        model_Obj.type = "text";
                        break;
                    case WEBLINK:
                        model_Obj.type = "weblink";
                        break;
                    case UTUBE:
                        model_Obj.type = "youtube";
                        break;
                }

                mListener.JsonReqModelUpdate(AdapterPos, model_Obj);
            } else {

                ContactHelperModule cont_hlep_model = new ContactHelperModule();

                cont_hlep_model.values = edt_to_add_tw.getText().toString();
                cont_hlep_model.type = String.valueOf(Cont_Edt_Type);

                mListener.contact_update(AdapterPos, cont_hlep_model);
            }

        }
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {

        EditText edt_title;
        int AdapterPos;

        public TitleViewHolder(View itemView, int Position) {
            super(itemView);
            AdapterPos = Position;
            edt_title = itemView.findViewById(R.id.edt_title);
            edt_title.addTextChangedListener(new customTextWatcher(edt_title, CreateMultimediaCard.enum_MultipleViewType.TITLE.ordinal(), AdapterPos, 1));
        }


    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        VideoView video_vw;
        ImageView play_pause;
        FrameLayout video_contr;
        ImageView img_video_upload;

        public VideoViewHolder(View itemView, int Position) {
            super(itemView);
            video_vw = itemView.findViewById(R.id.video_vw);
            play_pause = itemView.findViewById(R.id.play_pause);
            video_contr = itemView.findViewById(R.id.video_contr);
            img_video_upload = itemView.findViewById(R.id.img_video_upload);

        }
    }

    class ImageViewHolder extends RecyclerView.ViewHolder implements Thumbnail_ImageAdapter.Thumbnail_interface {

        SimpleDraweeView imageView;


        Thumbnail_ImageAdapter thumbnail_imageAdapter;
        //int Thumbnail_sel_position;
        RecyclerView recycler_list_thumbnail;
        List<MultipleImageModel> multi_image_obj;
        Context scrn_cntxt;

        public ImageViewHolder(View itemView, int Position) {
            super(itemView);
            scrn_cntxt = itemView.getContext();
            imageView = itemView.findViewById(R.id.image_createCard);
            set_card_image(itemView.getContext(), imageView, ((BrightlyNavigationActivity) _activity).CARD_DEF_IMAGE);
            //list_multi_image = new ArrayList<>();
            //  Thumbnail_sel_position = multipleViewCreationModelList.get(Position).Thumbnail_sel_pos;

            LinearLayoutManager L_manager = new LinearLayoutManager(itemView.getContext());
            L_manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            L_manager.setReverseLayout(true);
            recycler_list_thumbnail = (RecyclerView) itemView.findViewById(R.id.list_thumbnail);
            recycler_list_thumbnail.setLayoutManager(L_manager);


        }

        public void set_thumbnail_adapter(List<MultipleImageModel> temp_multi_image_obj, int sel_pos) {
            multi_image_obj = temp_multi_image_obj;
            if (thumbnail_imageAdapter == null) {
                thumbnail_imageAdapter = new Thumbnail_ImageAdapter();
                thumbnail_imageAdapter.adapter_multi_View_sel_pos = sel_pos;
                thumbnail_imageAdapter.mListener = this;


            } else {

            }

            thumbnail_imageAdapter.multipleImageModelList = multi_image_obj;

            recycler_list_thumbnail.setAdapter(thumbnail_imageAdapter);
            thumbnail_imageAdapter.adapter_Thumbnail_sel_position = multipleViewCreationModelList.get(sel_pos).Thumbnail_sel_pos;
            thumbnail_imageAdapter.notifyItemChanged(multipleViewCreationModelList.get(sel_pos).Thumbnail_sel_pos);
        }

        @Override
        public void Add_Thumbnail() {


            if (multi_image_obj.size() - 1 <= 10) {
          /*  imgImageChooser_crop = new ImageChooser_Crop(getActivity(), "Img_0001" + img_count);
            Intent intent = imgImageChooser_crop.getPickImageChooserIntent();
            if (intent == null) {
                //PermissionUtil.
            } else {
                startActivityForResult(intent, PICK_IMAGE_REQ);
            }*/
                mListener.call_action(thumbnail_imageAdapter.adapter_multi_View_sel_pos, CreateMultimediaCard.enum_MultipleViewType.IMAGE.ordinal());
            }
        }

        @Override
        public void onSelect(int Position, MultipleImageModel model) {
            int Thumbnail_sel_position = 1;
            if (Position >= 1)
                Thumbnail_sel_position = Position;
            set_card_image(scrn_cntxt, imageView, model.getImg_url());
            thumbnail_imageAdapter.adapter_Thumbnail_sel_position = Thumbnail_sel_position;
            thumbnail_imageAdapter.notifyDataSetChanged();
            set_card_image(scrn_cntxt, imageView, model.getImg_url());
            mListener.update_image_thumbnail_pos(thumbnail_imageAdapter.adapter_multi_View_sel_pos, Thumbnail_sel_position);
        }
    }

    public void set_card_image(Context context, SimpleDraweeView imageView, String URI_value) {
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(context.getResources());
        builder.setProgressBarImage(R.drawable.loader);

        builder.setProgressBarImage(
                new AutoRotateDrawable(builder.getProgressBarImage(), 1000, true));
        builder.setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(100)
                .build();

        imageView.setHierarchy(hierarchy);


        final ImageRequest imageRequest =
                ImageRequestBuilder.newBuilderWithSource(Uri.parse(URI_value))
                        .setResizeOptions(CreateMultimediaCard.mResizeOptions)

                        .build();
        imageView.setImageRequest(imageRequest);
    }

    public interface MultiviewAdapterListener {


        public void on_type_choosed(int position_to_add, int TYPE);

        public void call_action(int position, int TYPE);


        public void call_image_dialog(int Position);

        public void update_image_thumbnail_pos(int AdapterPos, int Thumbnail_pos);

        public void del_view(int Position);

        public void contact_update(int Position, ContactHelperModule modelObj);


        public void JsonReqModelUpdate(int Position, Object modelOj);
    }

    /*class AudioViewHolder extends RecyclerView.ViewHolder {
        TextView text_audioFile;
        ImageView upload_audioFile;
        CountDownTimer countDownTimer = null;
        File tempMp3File = null;
        SeekBar audio_seek_bar;


        ImageView img_play_stop;
        TextView txt_PlayProgTime;
        boolean isPlayBtnClicked = false;
        RelativeLayout rl_audio_player;


        MediaPlayer mediaPlayer;
        LinearLayout rec_contr;
        LinearLayout crt_contr;
        boolean isCreateScreen;
        RealmModel user_obj;
        String cur_audio_Pos;
        int mediaFileLengthInMilliseconds;
        String TotalTime;
        private final Handler handler = new Handler();
        Context scrn_context;
        int AdapterPos;

        public AudioViewHolder(View itemView, int Position) {
            super(itemView);
            AdapterPos = Position;
            scrn_context = itemView.getContext();
            img_play_stop = itemView.findViewById(R.id.img_play_stop);
            audio_seek_bar = itemView.findViewById(R.id.seek_audio_rec);
            txt_PlayProgTime = itemView.findViewById(R.id.txt_PlayProgTime);
            text_audioFile = itemView.findViewById(R.id.text_audioFile);
            upload_audioFile = itemView.findViewById(R.id.upload_audioFile);

            crt_contr = (LinearLayout) itemView.findViewById(R.id.ll_cr_contr);


            rl_audio_player = itemView.findViewById(R.id.rl_audio_player);
*//*
            setSeek_Bar();
            setImg_play_stop_Listener();
*//*
            mListener.audio_init(audio_seek_bar, txt_PlayProgTime, img_play_stop);

            upload_audioFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mListener.call_action(AdapterPos, CreateMultimediaCard.enum_MultipleViewType.AUDIO.ordinal());
                }
            });

        }


    }*/

    public class AudioViewHolder extends RecyclerView.ViewHolder implements MediaPlayer.OnBufferingUpdateListener {

        SeekBar audio_seek_bar;


        public ImageView img_play_stop;
        TextView txt_PlayProgTime;
        String TotalTime;
        public boolean isAudioPlay;
        int mediaFileLengthInMilliseconds;
        String cur_audio_Pos;

        public MediaPlayer mediaPlayer;
        TextView text_audioFile;
        RelativeLayout rl_audio_player;
        ImageView upload_audioFile;
        Context scrn_context;
        Handler handler = new Handler();
        int AdapterPos;

        public AudioViewHolder(View itemView, int Position) {
            super(itemView);
            AdapterPos = Position;
            scrn_context = itemView.getContext();
            img_play_stop = itemView.findViewById(R.id.img_play_stop);
            audio_seek_bar = itemView.findViewById(R.id.seek_audio_rec);
            upload_audioFile = itemView.findViewById(R.id.upload_audioFile);
            text_audioFile = itemView.findViewById(R.id.text_audioFile);
            rl_audio_player = itemView.findViewById(R.id.rl_audio_player);
            txt_PlayProgTime = itemView.findViewById(R.id.txt_PlayProgTime);
            upload_audioFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.call_action(AdapterPos, CreateMultimediaCard.enum_MultipleViewType.AUDIO.ordinal());
                }
            });
            img_play_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isAudioPlay) {
                        mediaPlayer.pause();
                        img_play_stop.setImageResource(R.drawable.play_rec);
                        isAudioPlay = false;
                        //mediaPlayer.prepareAsync();
                    } else {

                        img_play_stop.setImageResource(R.drawable.stop_rec);

                        mediaPlayer.start();
                        isAudioPlay = true;
                        primarySeekBarProgressUpdater();

                    }
                }
            });

/*
            setSeek_Bar();
            setImg_play_stop_Listener();
*/


        }

        private void primarySeekBarProgressUpdater() {
            if (isAudioPlay) {
                audio_seek_bar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100)); // This math construction give a percentage of "was playing"/"song length"
                cur_audio_Pos = TimeFormat.formateMilliSeccond(mediaPlayer.getCurrentPosition());
                txt_PlayProgTime.setText(cur_audio_Pos + "/" + TotalTime);
                if (mediaPlayer.isPlaying()) {
                    Runnable notification = new Runnable() {
                        public void run() {
                            primarySeekBarProgressUpdater();
                        }
                    };
                    handler.postDelayed(notification, 1000);
                }
            } else {
                //   cur_audio_Pos = TotalTime;
            }
        }

        public void setAudioProgText() {
            txt_PlayProgTime.setText(cur_audio_Pos + "/" + TotalTime);
        }

        public MediaPlayer setMediaPlayer(Uri audio_uri, String filePath, boolean isCreateCard) {
            mediaPlayer = new MediaPlayer();


            try {

                AudioManager am = (AudioManager) scrn_context.getSystemService(Context.AUDIO_SERVICE);
                am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);


                if (audio_uri != null) {
                    mediaPlayer.setDataSource(scrn_context, audio_uri);
                } else {
                    mediaPlayer.setDataSource(filePath);
                }
                if (!isCreateCard) {
                    txt_PlayProgTime.setVisibility(View.INVISIBLE);
                    mediaPlayer.setOnBufferingUpdateListener(this);
                } else
                    audio_seek_bar.setSecondaryProgress(audio_seek_bar.getMax());
                mediaPlayer.prepareAsync();
                mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // gets the song length in milliseconds from URL

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // mp.stop();
                        cur_audio_Pos = TotalTime;
                        setAudioProgText();
                        img_play_stop.setImageResource(R.drawable.play_rec);
                        isAudioPlay = false;
                        audio_seek_bar.setProgress(100);
                        mediaPlayer = mp;
                        //   mp.prepareAsync();
                    }
                });
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // to get total duration in milliseconds
                        mediaPlayer = mp;
                        long currentDuration = mediaPlayer.getCurrentPosition();
                        TotalTime = TimeFormat.formateMilliSeccond(mediaFileLengthInMilliseconds);
                        cur_audio_Pos = TimeFormat.formateMilliSeccond(currentDuration);
                        txt_PlayProgTime.setText(cur_audio_Pos + "/" + TotalTime);

                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
            return mediaPlayer;

        }


        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            audio_seek_bar.setSecondaryProgress(percent);
            if (percent == 100) {
                txt_PlayProgTime.setVisibility(View.VISIBLE);
            }
        }
    }

}
