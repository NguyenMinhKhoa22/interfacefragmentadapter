package com.example.mlivefirerepo_kotlin.repository

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.mlivefirerepo_kotlin.`interface`.ShowToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class AccountRepository   {
    private var mAccountLiveData : MutableLiveData<FirebaseUser> = MutableLiveData()
     private var mLoggedLiveData : MutableLiveData<Boolean> = MutableLiveData()
    private var mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private var mFire : FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getAccountLiveData(): MutableLiveData<FirebaseUser> { return mAccountLiveData }

    fun getLoggedLiveData(): MutableLiveData<Boolean> { return mLoggedLiveData }

    fun AccountRepository() {
        mAccountLiveData = MutableLiveData()
        mLoggedLiveData = MutableLiveData()
        mAuth = FirebaseAuth.getInstance()
        if (mAuth.currentUser!= null) {
            mAccountLiveData.postValue(mAuth.currentUser)
        }
    }

    fun registerAcc(getStringImage : String ,name : String,email : String, password : String) {

        mAuth.createUserWithEmailAndPassword( email, password)
            .addOnCompleteListener{task->
                if (task.isSuccessful) {
                    val userId = mAuth.currentUser!!.uid
                    mAccountLiveData.postValue(mAuth.currentUser)
                    val eEmail : String = email
                    val eName : String = name
                    val accInfo = hashMapOf(
                          "eEmail" to eEmail,
                          "eName" to eName,
                          "eAvatar" to getStringImage
        )
                 mFire.collection("Account_").document(userId).set(accInfo)
                         Log.e("regis", ""+email)
                } else {
                         Log.e("regis", "error : "+task.exception!!.message)
                }
            }
    }

    fun loginAcc(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{task->
                if (task.isSuccessful) {
                    mAccountLiveData.postValue(mAuth.currentUser)
                    Log.e("regis", "Login OK :V ")
                } else {
                    Log.e("regis", "error : "+task.exception!!.message)

                    /// error when write wrong login failed
//                   Toast.makeText(context, task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
    }
    fun signOutAcc(){
        mAuth.signOut()
        mLoggedLiveData.postValue(true)
    }
}