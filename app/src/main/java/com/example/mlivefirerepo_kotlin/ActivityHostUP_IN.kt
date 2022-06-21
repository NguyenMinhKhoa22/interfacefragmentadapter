package com.example.mlivefirerepo_kotlin

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.mlivefirerepo_kotlin.databinding.ActivityHostUpInBinding
import com.example.mlivefirerepo_kotlin.fragment.MainFragment
import com.example.mlivefirerepo_kotlin.fragment.SignInFrag
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_host_up_in.*

class ActivityHostUP_IN : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host_up_in)



    }





}