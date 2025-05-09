package com.example.fineartwebshop.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.fineartwebshop.R;
import com.example.fineartwebshop.databinding.FragmentProfileBinding;
import com.example.fineartwebshop.ui.product_form.ProductFormFragment;
import com.google.firebase.auth.FirebaseAuth;

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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}