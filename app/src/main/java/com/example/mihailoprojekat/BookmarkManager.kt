package com.example.mihailoprojekat

import com.example.mihailoprojekat.modules.Info
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.security.MessageDigest

object BookmarkManager {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun sanitizeUrl(url: String): String {
        return url.replace("[^a-zA-Z0-9]".toRegex(), "_")
    }

    fun saveBookmark(article: Info, onComplete: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid ?: return

        val bookmarkData = hashMapOf(
            "title" to article.title,
            "description" to article.description,
            "url" to article.url,
            "image" to article.image,
            "publishedAt" to article.publishedAt,
            "sourceName" to article.source.name,
            "content" to article.content
        )

        firestore.collection("users")
            .document(userId)
            .collection("bookmarks")
            .document(sanitizeUrl(article.url))
            .set(bookmarkData)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun removeBookmark(articleUrl: String, onComplete: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("users")
            .document(userId)
            .collection("bookmarks")
            .document(sanitizeUrl(articleUrl))
            .delete()
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun isBookmarked(articleUrl: String, onResult: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("users")
            .document(userId)
            .collection("bookmarks")
            .document(sanitizeUrl(articleUrl))
            .get()
            .addOnSuccessListener { document ->
                onResult(document.exists())
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    fun getBookmarks(onResult: (List<Info>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("users")
            .document(userId)
            .collection("bookmarks")
            .get()
            .addOnSuccessListener { documents ->
                val bookmarks = documents.mapNotNull { doc ->
                    try {
                        Info(
                            title = doc.getString("title") ?: "",
                            description = doc.getString("description"),
                            content = doc.getString("content"),
                            url = doc.getString("url") ?: "",
                            image = doc.getString("image"),
                            publishedAt = doc.getString("publishedAt") ?: "",
                            source = com.example.mihailoprojekat.modules.Source(
                                name = doc.getString("sourceName") ?: "",
                                url = ""
                            )
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                onResult(bookmarks)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }
}