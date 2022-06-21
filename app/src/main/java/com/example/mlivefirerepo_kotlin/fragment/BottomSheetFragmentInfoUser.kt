package com.example.mlivefirerepo_kotlin.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.mlivefirerepo_kotlin.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.botsheet_info_user.*

class BottomSheetFragmentInfoUser : BottomSheetDialogFragment() {
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mFire : FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAuth = FirebaseAuth.getInstance()
        mFire = FirebaseFirestore.getInstance()
        val view =inflater.inflate(R.layout.botsheet_info_user, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uploadInfoUser()
        (getView()!!.parent as View).setBackgroundColor(Color.TRANSPARENT)
        btnOk.setOnClickListener {
            dismiss()
        }
    }

    private fun uploadInfoUser() {
        val currentUserId = mAuth.currentUser!!.uid
        val docRef = mFire.collection("Account_").document(currentUserId)
        docRef.get()
            .addOnSuccessListener { document->
               Glide.with(requireActivity().applicationContext).load(document.getString("eAvatar")).into(avatarUserBS)
                gmailUserBS.text = document.getString("eEmail")
            }
    }
}