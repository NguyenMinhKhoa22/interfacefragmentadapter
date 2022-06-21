package com.example.mlivefirerepo_kotlin.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager
import com.example.mlivefirerepo_kotlin.R
import com.example.mlivefirerepo_kotlin.Response
import com.example.mlivefirerepo_kotlin.`interface`.FirebaseCallBack
import com.example.mlivefirerepo_kotlin.adapter.PostAdapter
import com.example.mlivefirerepo_kotlin.model.PostModel
import com.example.mlivefirerepo_kotlin.viewmodel.AccountViewModel
import com.example.mlivefirerepo_kotlin.viewmodel.PostViewModel
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.row_post.view.*


class MainFragment : Fragment(), PostAdapter.OnClickCmtInterface {
    private var mFire: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var mAccountViewModel: AccountViewModel
    private lateinit var mPostViewModel: PostViewModel
    private lateinit var mPostArrayList: ArrayList<PostModel>
    private lateinit var mAdapterPost: PostAdapter
    lateinit var  navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPostViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application))[PostViewModel::class.java]
        getResponeseUsingcallback()

        mAccountViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application))[AccountViewModel::class.java]
        mAccountViewModel.getLoggedData().observe(this, object : Observer<Boolean>{
            override fun onChanged(boolean: Boolean?) {
                if (boolean!!) {
                navController.navigate(R.id.signInFrag)
                }
            }
        })
        mPostViewModel.mPostListLiveData.observe(this) {
            rec_viewpage_2.apply {
                mAdapterPost = PostAdapter(context, it)
                adapter = mAdapterPost
                offscreenPageLimit = mPostArrayList.size
                setPadding(100, 0, 100, 0)
            }
        }

        mPostViewModel.getDataPost()

    }

    private fun getResponeseUsingcallback() {
       mPostViewModel.getResponeseCallBackPost(object : FirebaseCallBack {
           override fun onResponse(response: Response) {
                Log.e("TestLog", "ok: $response")
           }
           override fun onSuccess(data: List<PostModel>) {
           }
           override fun onError(message: String) {
           }
       })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        view.rec_viewpage.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }
            override fun onPageSelected(position: Int) {

            }
            override fun onPageScrollStateChanged(state: Int) {
            }
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        mPostArrayList = ArrayList()
        mAdapterPost = PostAdapter(activity!!, mPostArrayList)


        getSizeArrayList()

        // button Sign Out
        SignOutbtn.setOnClickListener {
            mAccountViewModel.signOut()
        }

        // call Bottom Sheet Fragment
        val botSheetInfoUser = BottomSheetFragmentInfoUser()
        btnShowInfoUser.setOnClickListener {
            botSheetInfoUser.show(parentFragmentManager, "BottomSheetDialog")
        }

        // call Dialog Fragment
        val dialogAddPost = DialogAdd()
        btnAddPost.setOnClickListener {
            dialogAddPost.show(parentFragmentManager, "DialogFragment")
        }


    }

    // test viewpager
    private fun getSizeArrayList() {
        mFire.collection("Posts").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("firestore error", error.message.toString())
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        mPostArrayList.add(dc.document.toObject(PostModel::class.java))
                    }
                }

            }
        })
    }

    //// interface
    override fun onItemClick(position: String?) {
         Log.e("Clicked", ""+position)

    }


}