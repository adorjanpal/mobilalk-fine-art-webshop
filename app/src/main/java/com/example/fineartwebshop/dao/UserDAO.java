package com.example.fineartwebshop.dao;

import com.example.fineartwebshop.model.ProductModel;
import com.example.fineartwebshop.model.UserModel;
import com.example.fineartwebshop.type.FirestoreCallback;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDAO {
    private final static String COLLECTION_NAME = "users";

    public static void getCartByEmail(String email, FirestoreCallback callback) {
        CollectionReference usersRef = FirebaseFirestore.getInstance().collection(COLLECTION_NAME);

        usersRef.whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);

                            List<Map<String, Object>> cartMaps = (List<Map<String, Object>>) document.get("cart");

                            List<ProductModel> cartItems = new ArrayList<>();

                            if (cartMaps != null) {
                                for (Map<String, Object> itemMap : cartMaps) {
                                    ProductModel product = new ProductModel();

                                    product.setId((String) itemMap.get("id"));
                                    product.setName((String) itemMap.get("name"));
                                    product.setPrice(itemMap.get("price") != null ? ((Long) itemMap.get("price")).intValue() : 0);                                    product.setDescription((String) itemMap.get("description"));
                                    product.setImgUrl((String) itemMap.get("imgUrl"));
                                    product.setSeller((String) itemMap.get("seller"));
                                    product.setDescription((String) itemMap.get("description"));

                                    cartItems.add(product);
                                }
                            }

                            callback.onSuccess(cartItems);

                        } else {
                            callback.onFailure(new Exception("No user found with email: " + email));
                        }
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    public static void addToCart(String email, ProductModel product, FirestoreCallback callback) {
        CollectionReference usersRef = FirebaseFirestore.getInstance().collection(COLLECTION_NAME);

        usersRef.whereEqualTo("email", email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    DocumentSnapshot userDoc = querySnapshot.getDocuments().get(0);
                    String userId = userDoc.getId();

                    usersRef.document(userId)
                            .update("cart", FieldValue.arrayUnion(product))

                            .addOnFailureListener(e -> {
                                callback.onFailure(e);
                            });

                } else {
                    callback.onFailure(new Exception("No user found with email: " + email));
                }
            } else {
                callback.onFailure(task.getException());
            }
        });
    }

    public static void save(String email) {
        CollectionReference usersRef = FirebaseFirestore.getInstance().collection(COLLECTION_NAME);

        usersRef.add(new UserModel(email, new ArrayList<>(), new ArrayList<>()));
    }
}
