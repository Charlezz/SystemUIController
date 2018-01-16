package com.oksisi213.systemuicontroller

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 * Created by Charles on 16/01/2018.
 */
class ChatFragment : Fragment(), MyWindowFocusChangeListener {

	companion object {
		val TAG = ChatFragment::class.java.simpleName

		fun newInstance(): ChatFragment = ChatFragment()
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_chat, container, false)
		view.setOnClickListener {
			activity?.startActivity(Intent(activity, ChatActivity::class.java))
		}
		return view
	}

	override fun onWindowFocusChanged(hasFocus: Boolean) {
		Log.e(TAG, "onWindowFocusChanged")
	}

}

