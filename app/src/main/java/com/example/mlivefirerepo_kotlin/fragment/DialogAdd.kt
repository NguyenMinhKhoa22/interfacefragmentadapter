package com.example.mlivefirerepo_kotlin.fragment

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mlivefirerepo_kotlin.R
import com.example.mlivefirerepo_kotlin.viewmodel.PostViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.box_add_post.*
import java.text.SimpleDateFormat
import java.util.*


class DialogAdd : DialogFragment() {
    private val GALLERYImage = 2
    var getStringImagePost : String = ""
    var getStringAvatarPost : String = ""
    var getStringNameUSerPost : String =""
    var dateString : String =""
    private val TAG = "DialogAdd"
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mFire : FirebaseFirestore
    private lateinit var mPostViewModel: PostViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAuth = FirebaseAuth.getInstance()
        mFire = FirebaseFirestore.getInstance()

        val androidFactory =
            ViewModelProvider.AndroidViewModelFactory(context!!.applicationContext as Application)
        mPostViewModel = androidFactory.create(PostViewModel::class.java)

        val view =inflater.inflate(R.layout.box_add_post, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        uploadInfoUserToPost()

        // choose Image from Post Dialog
        ImgPost_Dialog.setOnClickListener {
            chooseImageFromGallery()
        }

        btnSendPost_Dialog.setOnClickListener {
            if (getStringImagePost != null) {
                mPostViewModel.uploadPost(getStringImagePost, getStringAvatarPost, getStringNameUSerPost, dateString)
                dismiss()
            }
            return@setOnClickListener

        }

        super.onViewCreated(view, savedInstanceState)
    }

    // show Info Current User Signed In to Dialog Post
    private fun uploadInfoUserToPost() {
        val currentUserId = mAuth.currentUser!!.uid
        val docRef = mFire.collection("Account_").document(currentUserId)
        docRef.get()
            .addOnSuccessListener { document->

                val formatter = SimpleDateFormat("dd/MM/yyyy" + " HH:mm")
               dateString = formatter.format(Date(System.currentTimeMillis()))
                getStringAvatarPost = document.getString("eAvatar").toString()
                getStringNameUSerPost = document.getString("eName").toString()
                Glide.with(requireActivity().applicationContext).load(getStringAvatarPost).into(imgUserPostDialog)
            }
    }

    private fun chooseImageFromGallery() {
        val imageIntent = Intent()
        imageIntent.type = "image/*"
        imageIntent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(imageIntent,"Select image"), GALLERYImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==GALLERYImage && resultCode == Activity.RESULT_OK && data != null) {
            ImgPost_Dialog!!.setImageURI(data.data)
            Toast.makeText(activity, "Please For Loading Image...", Toast.LENGTH_SHORT).show()
            uploadImagePost(data.data!!)
        }
    }

    private fun uploadImagePost(imageURI: Uri) {
        val mStorageRef = FirebaseStorage.getInstance().reference
        // path at storage
        val imageFileNameImage = "POST_Images/images_Uploaded${System.currentTimeMillis()}.png"
        val uploadTask = mStorageRef.child(imageFileNameImage).putFile(imageURI)
        uploadTask.addOnSuccessListener {
            Toast.makeText(activity, "Please For Loading Image...", Toast.LENGTH_SHORT).show()
            Log.e(TAG, " Upload image ok")
            val downloadURLTask = mStorageRef.child(imageFileNameImage).downloadUrl
            downloadURLTask.addOnSuccessListener {it ->
                Log.e(TAG, "image path : $it")
                getStringImagePost = it.toString()
                Toast.makeText(activity, "Please For Loading Image...", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "test image path : "+ getStringImagePost)
                Toast.makeText(activity, "Waitting for 2 seconds...", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
            }
        }.addOnFailureListener{
            Toast.makeText(activity, "Loading Image Failed", Toast.LENGTH_SHORT).show()
            Log.e(TAG, " Upload image failed ${it.printStackTrace()}")
        }
    }
}