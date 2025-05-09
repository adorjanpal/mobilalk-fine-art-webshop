package com.example.fineartwebshop.ui.product_form;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.fineartwebshop.R;
import com.example.fineartwebshop.dao.ProductDAO;
import com.example.fineartwebshop.databinding.FragmentProductFormBinding;
import com.example.fineartwebshop.model.ProductModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import validator.Validator;

public class ProductFormFragment extends Fragment {

    private FragmentProductFormBinding binding;
    private ImageButton closeButton;
    private ImageView productImageView;
    private MaterialButton selectImageButton;
    private MaterialButton submitButton;
    private Uri selectedImageUri;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> selectImageLauncher;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProductFormBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        closeButton = binding.getRoot().findViewById(R.id.closeButton);
        productImageView = binding.getRoot().findViewById(R.id.productImageView);
        selectImageButton = binding.getRoot().findViewById(R.id.selectImageButton);
        submitButton = binding.getRoot().findViewById(R.id.submitButton);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        openImagePicker();
                    } else {
                        Toast.makeText(getContext(), "Permission to access images is required to select an image.", Toast.LENGTH_LONG).show();
                    }
                });

        selectImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImageUri = data.getData();
                            try {
                                Bitmap bitmap;
                                if (Build.VERSION.SDK_INT < 28) {
                                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                                } else {
                                    ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), selectedImageUri);
                                    bitmap = ImageDecoder.decodeBitmap(source);
                                }
                                productImageView.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                    } else {
                        openImagePicker();
                    }
                } else {
                    openImagePicker();
                }
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImageUri != null) {
                    uploadImageToFirebase(selectedImageUri);
                } else {
                    Toast.makeText(getContext(), "Please select an image.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectImageLauncher.launch(intent);
    }

    private void uploadImageToFirebase(Uri imageUri) {
        if (imageUri != null) {
            try {
                Bitmap bitmap;
                if (Build.VERSION.SDK_INT < 28) {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                } else {
                    ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), imageUri);
                    bitmap = ImageDecoder.decodeBitmap(source);
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                byte[] data = baos.toByteArray();

                String filename = UUID.randomUUID().toString() + ".jpg";
                StorageReference imageRef = storageReference.child("images/" + filename);

                UploadTask uploadTask = imageRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getContext(), "Upload failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("ProductFormFragment", "Upload failed", exception);
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        Log.d("ProductFormFragment", "Image uploaded successfully");
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                EditText productNameEditText = binding.getRoot().findViewById(R.id.productNameEditText);
                                EditText productPriceEditText = binding.getRoot().findViewById(R.id.productPriceEditText);
                                EditText productAuthorEditText = binding.getRoot().findViewById(R.id.authorEditText);

                                String productName, productPrice, productAuthor;
                                Validator validator = new Validator();
                                productName = String.valueOf(productNameEditText.getText());
                                productPrice = String.valueOf(productPriceEditText.getText());
                                productAuthor = String.valueOf(productAuthorEditText.getText());
                                ProductDAO.save(new ProductModel(productAuthor, filename, productName, Integer.parseInt(productPrice), FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                                Log.d("ProductFormFragment", "Download URL: " + downloadUri.toString());
                            }
                        });
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error preparing image for upload.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}