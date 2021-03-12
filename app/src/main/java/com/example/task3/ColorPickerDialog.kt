package com.example.task3

import android.R.color
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment


class ColorPickerDialog: DialogFragment() {

    lateinit var buttons: ArrayList<View>;
    var onInputListener: OnInputListener? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.color_picker, container, false)

        buttons = view.findViewById<LinearLayout>(R.id.color_buttons).touchables
        buttons.forEach { it.setOnClickListener{returnColorCode(it as Button)}}
        return view
    }

    private fun returnColorCode(colorButton: Button){
        val background = colorButton.currentHintTextColor
        onInputListener!!.sendColor(background)
    }

    interface OnInputListener {
        fun sendColor(color: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onInputListener = activity as OnInputListener?
        }

        catch (e: ClassCastException) {
            Log.e("Color Picker", "onAttach: ClassCastException: " + e.message)
        }
    }



}