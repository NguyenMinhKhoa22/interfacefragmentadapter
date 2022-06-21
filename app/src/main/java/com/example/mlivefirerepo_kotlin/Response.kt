package com.example.mlivefirerepo_kotlin

import com.example.mlivefirerepo_kotlin.model.PostModel

data class Response( var posts: List<PostModel>? = null,
                     var exception: Exception? = null)
