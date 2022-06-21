package com.example.mlivefirerepo_kotlin.viewmodel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mlivefirerepo_kotlin.repository.AccountRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

 class AccountViewModel : AndroidViewModel {
     var repository: AccountRepository
     var mData : MutableLiveData<FirebaseUser>
     var mLogged : MutableLiveData<Boolean>


    public constructor(application: Application): super(application){
        repository = AccountRepository()
        mData = repository.getAccountLiveData()
        mLogged = repository.getLoggedLiveData()
    }

    fun getAccData() : MutableLiveData<FirebaseUser> {
        return mData
    }
    fun getLoggedData() : MutableLiveData<Boolean> {
        return mLogged
    }

    fun register(getStringImage : String,name : String,email: String, password: String) {
        repository.registerAcc(getStringImage,name,email, password)
    }
    fun signIn(email: String, password: String) {
        repository.loginAcc(email, password)
    }
    fun signOut() {
        repository.signOutAcc()
    }

}


