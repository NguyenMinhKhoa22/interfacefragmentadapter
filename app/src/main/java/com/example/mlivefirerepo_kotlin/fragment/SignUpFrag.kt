package com.example.mlivefirerepo_kotlin.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.mlivefirerepo_kotlin.R
import com.example.mlivefirerepo_kotlin.viewmodel.AccountViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignUpFrag : Fragment() {
    var getStringImage : String = ""
    private val TAG = "SignUpFrag"
    private val GALLERYImage = 2
    private lateinit var mAccountViewModel: AccountViewModel
    lateinit var  navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAccountViewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application))[AccountViewModel::class.java]
        mAccountViewModel.getAccData().observe(this, object : Observer<FirebaseUser>{
            override fun onChanged(firebaseUser: FirebaseUser?) {
                if (firebaseUser != null) {
                    navController.navigate(R.id.mainFragment)
                }
            }

        })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        switchToSignIn.setOnClickListener{
            navController.popBackStack(R.id.signUpFrag, true)
            navController.navigate(R.id.signInFrag)
        }
        avatarSignUp.setOnClickListener{
            chooseImageFromGallery()
        }

        signUpBtn.setOnClickListener {
            val name : String = nameSignUp.text.toString()
            val email : String = emailSignUp.text.toString()
            val password : String = passwordSignUp.text.toString()
            if (name.isEmpty()){
                nameSignUp.error = "Missing name"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailSignUp.error = "error format email"
            } else if (TextUtils.isEmpty(password)) {
                passwordSignUp.error = "Please enter password"
            } else if (password.length<6) {
                passwordSignUp.error = "Password at least 6 characters long"
            } else {
                mAccountViewModel.register(getStringImage,name, email, password)

            }
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
            avatarSignUp!!.setImageURI(data.data)
            uploadImage(data.data!!)
        }
    }
    private fun uploadImage(imageURI: Uri) {
        val mStorageRef = FirebaseStorage.getInstance().reference
        // path at storage
        val imageFileNameImage = "IMAGE/images_Uploaded${System.currentTimeMillis()}.png"
        val uploadTask = mStorageRef.child(imageFileNameImage).putFile(imageURI)
        uploadTask.addOnSuccessListener {
            Log.e(TAG, " Upload image ok")
            val downloadURLTask = mStorageRef.child(imageFileNameImage).downloadUrl
            downloadURLTask.addOnSuccessListener {it ->
                Log.e(TAG, "image path : $it")
               getStringImage = it.toString();
                Log.e(TAG, "test image path : "+ getStringImage)
            }.addOnFailureListener{
            }
        }.addOnFailureListener{
            Log.e(TAG, " Upload image failed ${it.printStackTrace()}")
        }
    }

}