package com.example.mlivefirerepo_kotlin.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.mlivefirerepo_kotlin.*
import com.example.mlivefirerepo_kotlin.model.PostModel
import com.example.mlivefirerepo_kotlin.viewmodel.PostViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.row_post.view.*
import java.text.SimpleDateFormat
import java.util.*


 class PostAdapter(private val context: Context, private val mListPost: List<PostModel>)
    : PagerAdapter(){
    private lateinit var mPostViewModel: PostViewModel
    private var mAuth : FirebaseAuth= FirebaseAuth.getInstance()
    private var mFire : FirebaseFirestore = FirebaseFirestore.getInstance()

     // create interface
     interface OnClickCmtInterface{
         fun onItemClick(position: String?)
     }

     // set method interface @_@
     private var cmtClickInterface: OnClickCmtInterface ? = null
     fun setCmtItemInterface(cmtItemInterface: OnClickCmtInterface) {
        this.cmtClickInterface = cmtItemInterface
    }


    private val CurrentUserLiked = mAuth.currentUser!!.uid
      private var getCountLike : Long = 0
    override fun getCount(): Int {
       return mListPost.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return  view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val view = LayoutInflater.from(context).inflate(R.layout.row_post, container, false)
        val postL = mListPost[position]

        val LimgPostP = postL.eImgPost
        val LtimeUserP = postL.eTimePost
        val LavatarUserP = postL.eAvatarUserPost
        val LnameUserP = postL.eNameUserPost
        val LlikeP = postL.eLikedPost

//        view.imgPost.setOnClickListener(object : View.OnClickListener{
//            override fun onClick(p0: View?) {
//                if (cmtClickInterface!= null) {
//                    cmtClickInterface!!.onItemClick(position)
//                }
//            }
//
//        })

        view.imgPost.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                if (cmtClickInterface != null) {
                    cmtClickInterface?.onItemClick(postL.eIdOfPost.toString())
                }
            }

        })


        var isLike = false
        var countLike : Int = 0

        val postRef : DocumentReference = mFire.collection("Posts").document(postL.eIdOfPost!!)
        val likeRef : DocumentReference = mFire.collection("Posts").document(postL.eIdOfPost!!).collection("Liked").document(CurrentUserLiked)
        mFire.collection("Posts").document(postL.eIdOfPost!!).collection("Liked").get().addOnSuccessListener{document ->
            getCountLike = document.size().toLong()
            Log.e("testLike" , " $getCountLike")
            postRef.update("eLikedPost", getCountLike)
        }

        view.likePost.setOnClickListener{
            Toast.makeText(context, "Click like" + postL.eIdOfPost , Toast.LENGTH_SHORT).show()

            isLike = true
            countLike = (getCountLike + 1).toInt()
            view.likePost.setBackgroundResource(R.drawable.ic_liked)
            val formatter = SimpleDateFormat("dd/MM/yyyy" + " HH:mm")
            val getTimelike = formatter.format(Date(System.currentTimeMillis()))

            val likedPostInfo = hashMapOf(
                "eLiked" to CurrentUserLiked ,
                "eTimeLiked" to getTimelike ,
                "eLikedCheck" to isLike
            )

            postRef.update("eLikedPost", countLike)
            likeRef.set(likedPostInfo)

        }

        view.likePost.setOnLongClickListener{

            getCountLike = (getCountLike - 1)

            likeRef.delete()

            view.likePost.setBackgroundResource(R.drawable.ic_unlike)
            isLike = false
            postRef.update("eLikedPost", getCountLike)
            true
        }



        // set value to each row Post


        view.likeCount.text = LlikeP.toString()
        Glide.with(context).load(LavatarUserP).into(view.imgAvatarUserPost)
        view.nameUserPost.text = LnameUserP
        Glide.with(context).load(LimgPostP).into(view.imgPost)
        view.timeUserPost.text = LtimeUserP

        container.addView(view, POSITION_UNCHANGED)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeAllViews()
    }



}