package com.itosoftware.inderandroid.Fragments;

/**
 * Created by itofelipeparra on 24/06/16.
 */

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import com.itosoftware.inderandroid.R;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.FilePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.FilePickerCallback;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.entity.ChosenImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


public class DialogFragmentPqrUploadSelector extends DialogFragment implements View.OnClickListener, ImagePickerCallback, FilePickerCallback {

    private View back;
    private View image;
    private View camera;
    private View file;
    private View container_detail;
    private ImagePicker imagePicker;
    private CameraImagePicker cameraPicker;
    private ProgressDialog progressDialog;
    private static String TAG = "upload selector";
    private String pickerPath;
    private FilePicker filePicker;

    public DialogFragmentPqrUploadSelector() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View view = inflater.inflate(R.layout.fragment_dialog_pqr_upload_selector, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        back = view.findViewById(R.id.back);
        back.setOnClickListener(this);
        image = view.findViewById(R.id.image);
        image.setOnClickListener(this);
        camera = view.findViewById(R.id.camera);
        camera.setOnClickListener(this);
        file = view.findViewById(R.id.file);
        file.setOnClickListener(this);
        container_detail = view.findViewById(R.id.container_detail);
        isStoragePermissionGranted();

        return view;
    }

    public void isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(getActivity(), "No hay permisos para subir archivos", Toast.LENGTH_LONG).show();
                    container_detail.setVisibility(View.GONE);
                }
                return;
            }
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();

    }

    @Override
    public void onClick(View v) {
        String tag = v.getTag().toString();
        if (!tag.equals("back")) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Procesando archivo");
            }
            progressDialog.show();
            image.setEnabled(false);
            camera.setEnabled(false);
            file.setEnabled(false);
        }

        if (tag.equals("back")) {
            dismiss();
        } else if (tag.equals("image")) {
            imagePicker = new ImagePicker(this);
            imagePicker.shouldGenerateMetadata(true);
            imagePicker.shouldGenerateThumbnails(true);
            imagePicker.setImagePickerCallback(this);
            imagePicker.pickImage();
        } else if (tag.equals("camera")) {
            cameraPicker = new CameraImagePicker(this);
            cameraPicker.shouldGenerateMetadata(true);
            cameraPicker.shouldGenerateThumbnails(true);
            cameraPicker.setImagePickerCallback(this);
            pickerPath = cameraPicker.pickImage();
        } else if (tag.equals("file")) {
            filePicker = new FilePicker(this);
            filePicker.setFilePickerCallback(this);
            filePicker.pickFile();
        }
    }


    @Override
    public void onFilesChosen(List<ChosenFile> files) {
        Log.d(TAG, "onFilesChosen: " + files.get(0));
        if (files.get(0).getMimeType().contains("image")) {
            ((PqrFragment) getParentFragment()).setIs_file(false);
        } else {
            ((PqrFragment) getParentFragment()).setIs_file(true);
        }
        String base64 = getBase64(files.get(0).getOriginalPath());
        System.gc();
        Log.w(TAG, "base 64:" + base64);
        dismiss();

    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Picker.PICK_IMAGE_DEVICE) {
                if (imagePicker == null) {
                    imagePicker = new ImagePicker(this);
                    imagePicker.setImagePickerCallback(this);
                }
                imagePicker.submit(data);
            } else if (requestCode == Picker.PICK_IMAGE_CAMERA) {
                if (cameraPicker == null) {
                    cameraPicker = new CameraImagePicker(this);
                    cameraPicker.setImagePickerCallback(this);
                    cameraPicker.reinitialize(pickerPath);
                }
                if (pickerPath != null) {
                    cameraPicker.submit(data);
                } else {
                    cameraPicker = new CameraImagePicker(this);
                    cameraPicker.shouldGenerateMetadata(true);
                    cameraPicker.shouldGenerateThumbnails(true);
                    cameraPicker.setImagePickerCallback(this);
                    pickerPath = cameraPicker.pickImage();
                }
            } else if (requestCode == Picker.PICK_FILE && resultCode == Activity.RESULT_OK) {
                if (filePicker == null) {
                    filePicker = new FilePicker(this);
                    filePicker.setFilePickerCallback(this);
                }
                filePicker.submit(data);
            }
        } else {
            dismissProgress();
        }
    }

    @Override
    public void onImagesChosen(List<ChosenImage> list) {
        Log.w(TAG, "file list" + list.toString());
        ((PqrFragment) getParentFragment()).setIs_file(false);
        String base64;
        if (list.get(0).getOriginalPath().contains(".png")) {
            base64 = getBase64(list.get(0).getOriginalPath());
        } else {
            base64 = getBase64(list.get(0).getThumbnailPath());
        }
        Log.w(TAG, "base 64:" + base64);
        dismiss();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // You have to save path in case your activity is killed.
        // In such a scenario, you will need to re-initialize the CameraImagePicker
        outState.putString("picker_path", pickerPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onError(String s) {
        Log.w(TAG, "error archivo:" + s);
        Toast.makeText(getActivity(), "Problema al cargar el archivo", Toast.LENGTH_LONG).show();
        dismissProgress();
    }

    public String getBase64(String path) {
        File originalFile = new File(path);
        String encodedBase64 = null;

        try {
            FileInputStream fileInputStreamReader = new FileInputStream(originalFile);
            byte[] bytes = new byte[(int) originalFile.length()];
            fileInputStreamReader.read(bytes);
            encodedBase64 = new String(Base64.encodeToString(bytes, 0));
            fileInputStreamReader.close();
            bytes = null;
            File file = new File(path);
            Uri selectedUri = Uri.fromFile(file);
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString());
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
            selectedUri = null;
            file = null;
            System.gc();
            encodedBase64 = "data:" + mimeType + ";base64," + encodedBase64;
            ((PqrFragment) getParentFragment()).setPath_file(path);
            ((PqrFragment) getParentFragment()).setUploadImage(path);
            ((PqrFragment) getParentFragment()).setBase_64(encodedBase64);
            dismissProgress();
            dismiss();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            dismissProgress();
            onError(e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            dismissProgress();
            onError(e.toString());
        }

        return encodedBase64;
    }

    public void dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        image.setEnabled(true);
        camera.setEnabled(true);
        file.setEnabled(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_path")) {
                pickerPath = savedInstanceState.getString("picker_path");
            }
        }
    }

}