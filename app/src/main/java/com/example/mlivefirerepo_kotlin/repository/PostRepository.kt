package com.example.mlivefirerepo_kotlin.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.mlivefirerepo_kotlin.`interface`.FirebaseCallBack
import com.example.mlivefirerepo_kotlin.Response
import com.example.mlivefirerepo_kotlin.model.PostModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class PostRepository {
    lateinit var dataPost : String
    private var mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private var mFire : FirebaseFirestore = FirebaseFirestore.getInstance()
    private val dataPostRef : CollectionReference = mFire.collection("Posts")
    private val dataLikePostRef = mFire.collection("Liked")

    fun uploadPost(StringImgPost : String, StringAvatarPost: String, StringNameUserPost : String, StringTimePost : String){
        val stringImgPost = StringImgPost
        val userId = mAuth.currentUser!!.uid
        val keyIdPost : DocumentReference
        keyIdPost = mFire.collection("Posts").document()
        val uniqueIdPost = keyIdPost.id
        val PostRef = mFire.collection("Posts").document(uniqueIdPost)
        val postInfo = hashMapOf(
            "eImgPost" to stringImgPost,
            "eUserIdPost" to userId,
            "eIdOfPost" to uniqueIdPost,
            "eAvatarUserPost" to StringAvatarPost,
            "eNameUserPost" to StringNameUserPost,
            "eTimePost" to StringTimePost,
            "eLikedPost" to 0
        )
        PostRef.set(postInfo)
            .addOnCompleteListener{
                Log.e("PostRepository", "Added post ok")
            }
            .addOnFailureListener{
                Log.e("PostRepository", "Added post Error")
            }
    }

    fun eGetPost(callback: FirebaseCallBack) {
        dataPostRef.get()
            .addOnCompleteListener() { task ->
                val response = Response()
                if (task.isSuccessful) {
                    val result = task.result
                    result?.let {
                        response.posts = result.documents.mapNotNull { snapshot ->
                            snapshot.toObject(PostModel::class.java)

                        }
                    }
                    callback.onSuccess(response.posts.orEmpty())
                } else {
                    response.exception = task.exception
                    callback.onError(task.exception?.message.orEmpty())
                }

            }
    }

    fun uploadLike(stringUserLiked : String) {
        val likeInfo = hashMapOf(
            "eLiked" to stringUserLiked
        )
        dataLikePostRef.add(likeInfo)
    }

}