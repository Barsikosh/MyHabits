package com.example.task3.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.task3.MainActivity
import com.example.task3.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.image_selector.*

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
                .with(MainActivity.context)
                .load(str)
                .circleCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .into(userImage)
            dismiss()
        }
    }
}