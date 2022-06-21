package com.example.mlivefirerepo_kotlin.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.mlivefirerepo_kotlin.MainActivity
import com.example.mlivefirerepo_kotlin.R
import com.example.mlivefirerepo_kotlin.databinding.FragmentSignInBinding
import com.example.mlivefirerepo_kotlin.repository.AccountRepository
import com.example.mlivefirerepo_kotlin.viewmodel.AccountViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_host_up_in.*
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignInFrag : Fragment() {

    lateinit var mAccountViewModel: AccountViewModel
    lateinit var  navController : NavController
    var mAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mUser = mAuth.currentUser
        if (mUser!= null) {
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction().add(R.id.fragment, MainFragment())
                .commit()

        }
        mAccountViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application))[AccountViewModel::class.java]
        mAccountViewModel.getAccData().observe(this, object : Observer<FirebaseUser> {
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

        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        switchToSignUp.setOnClickListener{
            navController.popBackStack(R.id.signInFrag, true)
            navController.navigate(R.id.signUpFrag)
        }

        signInBtn.setOnClickListener {
            val email : String = emailSignIn.text.toString()
            val password : String = passwordSignIn.text.toString()

            if (!email.isEmpty()&& !password.isEmpty()) {
                mAccountViewModel.signIn(email, password)

            }
        }
    }
}