package com.example.mlivefirerepo_kotlin.`interface`

import com.example.mlivefirerepo_kotlin.Response
import com.example.mlivefirerepo_kotlin.model.PostModel

interface FirebaseCallBack {
    fun onResponse(response: Response)
    fun onSuccess(data: List<PostModel>)
    fun onError(message: String)
}