package com.example.task3.Fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.task3.MainActivity
import com.example.task3.R
import com.google.android.material.navigation.NavigationView
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.image_selector.*
import kotlinx.android.synthetic.main.nav_header.*

class UserImageSelector : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.image_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navView: NavigationView? = activity?.findViewById(R.id.nav_view)
        val userImage = navView!!.getHeaderView(0).findViewById<ImageView>(R.id.user_image)
        userImage.setBackgroundColor(0)
        setup_image.setOnClickListener {
            val str = image_reference.text.toString()
            Glide
                .with(MainActivity.CONTEXT)
                .load(str)
                .circleCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .into(userImage)
            dismiss()
        }
    }
}