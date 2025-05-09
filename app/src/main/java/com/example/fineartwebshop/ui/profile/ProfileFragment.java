package com.example.fineartwebshop.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.fineartwebshop.R;
import com.example.fineartwebshop.dao.UserDAO;
import com.example.fineartwebshop.databinding.FragmentProfileBinding;
import com.example.fineartwebshop.model.ProductModel;
import com.example.fineartwebshop.type.FirestoreCallback;
import com.example.fineartwebshop.ui.login.LoginActivity;
import com.example.fineartwebshop.ui.product_form.ProductFormFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.auth.User;

import java.util.List;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel notificationsViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView usernameTextView = binding.usernameTextView;
        Button sellButton = binding.sellButton;

        Glide.with(binding.getRoot().getContext())
                .load(R.drawable.account_circle_24px)
                .into(binding.profilePicture);

        usernameTextView.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.nav_host_fragment_activity_main, new ProductFormFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        binding.deleteProfileButton.setOnClickListener(v -> {
            UserDAO.deleteByEmail(usernameTextView.getText().toString(), new FirestoreCallback() {
                @Override
                public void onSuccess(List<ProductModel> productList) {
                    Toast.makeText(getContext(), "Profile deleted", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getContext(), "Failed to delete profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}