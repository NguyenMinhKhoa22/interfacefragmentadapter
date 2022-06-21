package com.example.mlivefirerepo_kotlin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mlivefirerepo_kotlin.Response
import com.example.mlivefirerepo_kotlin.`interface`.FirebaseCallBack
import com.example.mlivefirerepo_kotlin.model.PostModel
import com.example.mlivefirerepo_kotlin.repository.PostRepository

class PostViewModel(application: Application) : AndroidViewModel(application) {
     var postRepository : PostRepository = PostRepository()

     var mPostListLiveData = MutableLiveData<List<PostModel>>()
     var mPostArrayList =ArrayList<PostModel>()


    fun uploadPost(StringImgPost : String, StringAvatarPost : String, StringNameUserPost : String, dateString : String) {
        postRepository.uploadPost(StringImgPost, StringAvatarPost, StringNameUserPost,dateString)
    }

    fun uploadLikedPost(StringUserLikedPost : String) {
        postRepository.uploadLike(StringUserLikedPost)
    }

    fun getResponeseCallBackPost(callBack: FirebaseCallBack) = postRepository.eGetPost(callBack)


    fun getDataPost(){
        postRepository.eGetPost(object : FirebaseCallBack{
            override fun onResponse(response: Response) {
            }

            override fun onSuccess(data: List<PostModel>) {
                mPostListLiveData.postValue(data)
            }

            override fun onError(message: String) {
                TODO("Not yet implemented")
            }

        })
//        mPostListLiveData.postValue(mPostArrayList)
    }
}