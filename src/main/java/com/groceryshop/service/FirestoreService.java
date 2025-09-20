package com.groceryshop.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class FirestoreService {

    private Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }

    // ✅ Save with auto-generated ID
    public String save(String collection, Map<String, Object> data) {
        try {
            DocumentReference docRef = getFirestore().collection(collection).document();
            data.put("id", docRef.getId());
            docRef.set(data).get(); // Wait for operation
            return docRef.getId();
        } catch (Exception e) {
            throw new RuntimeException("Error saving document", e);
        }
    }

    // ✅ Save with custom ID
    public String save(String collection, String id, Map<String, Object> data) {
        try {
            DocumentReference docRef = getFirestore().collection(collection).document(id);
            data.put("id", id);
            docRef.set(data).get();
            return id;
        } catch (Exception e) {
            throw new RuntimeException("Error saving document with ID " + id, e);
        }
    }

    // ✅ Find by ID
    public Map<String, Object> findById(String collection, String id) {
        try {
            DocumentSnapshot document = getFirestore()
                    .collection(collection)
                    .document(id)
                    .get()
                    .get();
            return document.exists() ? document.getData() : null;
        } catch (Exception e) {
            throw new RuntimeException("Error finding document with ID " + id, e);
        }
    }

    // ✅ Find all documents
    public List<Map<String, Object>> findAll(String collection) {
        try {
            ApiFuture<QuerySnapshot> future = getFirestore().collection(collection).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            List<Map<String, Object>> results = new ArrayList<>();
            for (QueryDocumentSnapshot doc : documents) {
                results.add(doc.getData());
            }
            return results;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching all documents from " + collection, e);
        }
    }

    // ✅ Find documents by field
    public List<Map<String, Object>> findByField(String collection, String field, Object value) {
        try {
            ApiFuture<QuerySnapshot> future = getFirestore()
                    .collection(collection)
                    .whereEqualTo(field, value)
                    .get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            List<Map<String, Object>> results = new ArrayList<>();
            for (QueryDocumentSnapshot doc : documents) {
                results.add(doc.getData());
            }
            return results;
        } catch (Exception e) {
            throw new RuntimeException("Error querying documents in " + collection, e);
        }
    }

    // ✅ Update document fields
    public void update(String collection, String id, Map<String, Object> updates) {
        try {
            DocumentReference docRef = getFirestore().collection(collection).document(id);
            docRef.update(updates).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Error updating document with ID " + id, e);
        }
    }

    // ✅ Delete a document
    public boolean delete(String collection, String id) {
        try {
            getFirestore().collection(collection).document(id).delete().get();
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting document with ID " + id, e);
        }
    }
}
