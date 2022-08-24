package com.hz_apps.timebasedlocker.Adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hz_apps.timebasedlocker.R;

import java.io.File;
import java.util.ArrayList;

public class FilesListAdapter extends RecyclerView.Adapter<FilesListAdapter.myViewHolder> {

    private final Context context;
    private final ArrayList<File> imagesFilesList;
    // Just making program fast: I take a array of in which all elements are in false state
    // if user select any of the item then that particular item will select to true state
    // At the end of this i will make a new array that contain elements on the basis of true and false
    private final boolean[] filesSelectedState;
    public int numberOfItemsSelected = 0;
    private final ImageButton nextBtn;

    public FilesListAdapter(Context context, ArrayList<File> imagesFilesList, ImageButton nextBtn) {
        this.context = context;
        this.imagesFilesList = imagesFilesList;
        this.filesSelectedState = new boolean[imagesFilesList.size()];
        this.nextBtn = nextBtn;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_list_view, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        File file = imagesFilesList.get(holder.getBindingAdapterPosition());

        Glide.with(context).load(file)
                .into(holder.imageView);

        if (filesSelectedState[holder.getBindingAdapterPosition()]){
            holder.imageView.setColorFilter(ContextCompat.getColor(context, R.color.black_opacity_25),
                    PorterDuff.Mode.SRC_OVER
            );
            holder.checkBox.setVisibility(View.VISIBLE);
        }else{
            holder.imageView.clearColorFilter();
            holder.checkBox.setVisibility(View.GONE);
        }

        holder.itemView.setOnLongClickListener(v -> {
            if (filesSelectedState[holder.getBindingAdapterPosition()]) {
                unselectItem(holder);
            }
            else{
                selectItem(holder);
            }

            /*
             * if selected files are equal to 0 then disable nextBtn
             * if it is not 0 then enable nextBtn
             */
            if (numberOfItemsSelected == 0)
                nextBtn.setBackgroundResource(R.drawable.round_button_disabled);
            else nextBtn.setBackgroundResource(R.drawable.round_button_enabled);
            return true;
        });

        holder.itemView.setOnClickListener(v -> {
            if (filesSelectedState[holder.getBindingAdapterPosition()]) {
                unselectItem(holder);
            }
            else{
                selectItem(holder);
            }

            /*
             * if selected files are equal to 0 then disable nextBtn
             * if it is not 0 then enable nextBtn
             */
            if (numberOfItemsSelected == 0)
                nextBtn.setBackgroundResource(R.drawable.round_button_disabled);
            else nextBtn.setBackgroundResource(R.drawable.round_button_enabled);
        });
    }

    @Override
    public int getItemCount() {
        return imagesFilesList.size();
    }

    /*
     * This function return all selected files
     * It check if filesSelectedState array is true then it will add that file to selectedFiles arrayList
     */
    public ArrayList<File> getAllSelectedFiles() {
        ArrayList<File> filesList = new ArrayList<>();
        for (int i = 0; i < imagesFilesList.size(); i++) {
            if (filesSelectedState[i]) {
                filesList.add(imagesFilesList.get(i));
            }
        }
        return filesList;
    }

    // This function will select item and also set true in filesSelectedState array
    private void selectItem(myViewHolder holder) {
        holder.checkBox.setVisibility(View.VISIBLE);
        holder.imageView.setColorFilter(ContextCompat.getColor(context, R.color.black_opacity_25),
                PorterDuff.Mode.SRC_OVER
        );
        filesSelectedState[holder.getBindingAdapterPosition()] = true;
        numberOfItemsSelected += 1;
    }

    // This function unselect item and also set false in filesSelectedState array
    private void unselectItem(myViewHolder holder) {
        holder.checkBox.setVisibility(View.GONE);
        filesSelectedState[holder.getBindingAdapterPosition()] = false;
        holder.imageView.clearColorFilter();
        numberOfItemsSelected -= 1;
    }

    public void setAllItemsSelected(){
        for (int i=0; i<filesSelectedState.length; i++){
            if (!filesSelectedState[i]){
                filesSelectedState[i] = true;
                numberOfItemsSelected += 1;
                notifyItemChanged(i);
            }
        }
        nextBtn.setBackgroundResource(R.drawable.round_button_enabled);
    }

    public void setAllItemsUnselected(){
        for (int i=0; i<filesSelectedState.length; i++){
            if (filesSelectedState[i]){
                filesSelectedState[i] = false;
                numberOfItemsSelected -= 1;
                notifyItemChanged(i);
            }
        }
        nextBtn.setBackgroundResource(R.drawable.round_button_disabled);
    }

    public boolean isAllItemsSelected() {
        return numberOfItemsSelected == imagesFilesList.size();
    }

    public boolean isAllItemsUnselected() {
        return numberOfItemsSelected == 0;
    }

    public boolean[] getFilesSelectedState() {
        return filesSelectedState;
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final ImageView checkBox;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView_images_list);
            checkBox = itemView.findViewById(R.id.check_box_images_list);
        }
    }
}