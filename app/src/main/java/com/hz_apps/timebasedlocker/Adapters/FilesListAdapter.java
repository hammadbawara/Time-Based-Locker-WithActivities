package com.hz_apps.timebasedlocker.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hz_apps.timebasedlocker.R;
import com.hz_apps.timebasedlocker.ui.LockFiles.LockFilesActivity;
import com.hz_apps.timebasedlocker.ui.selectfolder.selectfiles.SelectFilesActivity;

import java.io.File;
import java.util.ArrayList;

public class FilesListAdapter extends RecyclerView.Adapter<FilesListAdapter.myViewHolder>{

    private final Context context;
    private final ArrayList<File> imagesFilesList;
    private boolean allItemsSelectedFlag = false;
    private boolean allItemsUnselectedFlag = false;
    // Just making program fast: I take a array of in which all elements are in false state
    // if user select any of the item then that particular item will select to true state
    // At the end of this i will make a new array that contain elements on the basis of true and false
    private final boolean[] filesSelectedState;
    private final ImageButton nextBtn;
    int numberOfItemsSelected = 0;

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

        File file = imagesFilesList.get(position);

        Glide.with(context).load(file)
                .into(holder.imageView);


        // nextBtn on ClickListener
        nextBtn.setOnClickListener(v -> {
            if (numberOfItemsSelected == 0){
                Toast.makeText(context, "You have not selected any item", Toast.LENGTH_SHORT).show();
           }else{
               Intent intent = new Intent(context, LockFilesActivity.class);
               intent.putExtra("selected_files", getAllSelectedFiles());
               intent.putExtra("TYPES_OF_FILES", SelectFilesActivity.TYPES_OF_FILES);
               context.startActivity(intent);
            }
        });

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                selectItem(holder);
            }
            if (!isChecked) unselectItem(holder);

            /**
             * if selected files are equal to 0 then disable nextBtn
             * if it is not 0 then enable nextBtn
             */
            if (numberOfItemsSelected == 0) nextBtn.setBackgroundResource(R.drawable.round_button_disabled);
            else nextBtn.setBackgroundResource(R.drawable.round_button_enabled);
        });

        // If user selected all items selected then it check if item not selected then select that item
        if (allItemsSelectedFlag){
            holder.checkBox.setChecked(true);
        }
        if (allItemsUnselectedFlag){
            holder.checkBox.setChecked(false);
            unselectItem(holder);
        }
    }

    @Override
    public int getItemCount() {
        return imagesFilesList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final CheckBox checkBox;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView_images_list);
            checkBox = itemView.findViewById(R.id.checkbox_images_list);
        }
    }


    public void setAllItemsSelected(boolean b){
        allItemsSelectedFlag = b;
    }

    public void setAllItemsUnSelectedFlag(boolean allItemsUnselectedFlag) {
        nextBtn.setBackgroundResource(R.drawable.round_button_disabled);
        this.allItemsUnselectedFlag = allItemsUnselectedFlag;
    }

    /*
    * This function return all selected files
    * It check if filesSelectedState array is true then it will add that file to selectedFiles arrayList
     */
    private ArrayList<File> getAllSelectedFiles(){
        ArrayList<File> filesList = new ArrayList<>();
        for (int i=0; i<imagesFilesList.size(); i++){
            if (filesSelectedState[i]){
                filesList.add(imagesFilesList.get(i));
            }
        }
        return filesList;
    }

    // This function will select item and also set true in filesSelectedState array
    private void selectItem(myViewHolder holder){
        holder.imageView.setColorFilter(ContextCompat.getColor(context, R.color.black_opacity_25),
                PorterDuff.Mode.SRC_OVER
        );
        filesSelectedState[holder.getAdapterPosition()] = true;
        numberOfItemsSelected += 1;
    }

    // This function unselect item and also set false in filesSelectedState array
    private void unselectItem(myViewHolder holder){
        filesSelectedState[holder.getAdapterPosition()] = false;
        holder.imageView.clearColorFilter();
        numberOfItemsSelected -= 1;
    }

    public boolean isAllItemsSelected(){
        return numberOfItemsSelected == imagesFilesList.size();
    }

    public boolean isAllItemsUnselected(){
        return numberOfItemsSelected == 0;
    }

    public boolean[] getFilesSelectedState() {
        return filesSelectedState;
    }
}