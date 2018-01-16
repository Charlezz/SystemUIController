package com.oksisi213.systemuicontroller

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.item_chat.view.*


/**
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 * Created by Charles on 16/01/2018.
 */
class ChatActivity : AppCompatActivity() {
	companion object {
		val TAG = Companion::class.java.simpleName
	}

	val adapter by lazy {
		ChatAdapter()
	}


	var isKeyboardOpen: Boolean = false
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_chat)

		window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
		window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)

		background.layoutParams = background.layoutParams.apply {
			width = resources.displayMetrics.widthPixels
			height = resources.displayMetrics.heightPixels
		}



		supportActionBar?.hide()
		recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
		recyclerView.adapter = adapter

		for (i in 0 until 100) {
			adapter.add("$i")
		}

		send.setOnClickListener {
			adapter.add(input.text.toString().also {
				input.text.clear()
			}) {
				recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
			}
		}

		rootView.viewTreeObserver.addOnGlobalLayoutListener {
			Log.e(TAG, "onGlobalLayout")

			val r = Rect()
			//r will be populated with the coordinates of your view that area still visible.
			rootView.getWindowVisibleDisplayFrame(r)


			val heightDiff = rootView.rootView.height - (r.bottom - r.top)
			Log.e(TAG, "heightDiff = ${heightDiff}")

			if (heightDiff > 100f * resources.displayMetrics.scaledDensity) {

				if (isKeyboardOpen) {
					return@addOnGlobalLayoutListener
				}
				Log.e(TAG, "keyboard open")
				isKeyboardOpen = true

				window.decorView.systemUiVisibility = 0
				input_layout.translationY = -heightDiff.toFloat() + input_layout.height / 2f
				recyclerView.translationY = -heightDiff.toFloat() + input_layout.height / 2f
			} else {

				if (!isKeyboardOpen) {
					return@addOnGlobalLayoutListener
				}
				Log.e(TAG, "keyboard close")
				isKeyboardOpen = false
				setImmersive()
				input_layout.translationY = 0f
				recyclerView.translationY = 0f
			}
		}
		setImmersive()
	}

	override fun onWindowFocusChanged(hasFocus: Boolean) {
		super.onWindowFocusChanged(hasFocus)
		if (hasFocus) {
			setImmersive()
		}
	}

	fun setImmersive() {
		window.decorView.systemUiVisibility =
				View.SYSTEM_UI_FLAG_FULLSCREEN or
						View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
						View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
						View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
						View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
						View.SYSTEM_UI_FLAG_LAYOUT_STABLE
	}
}

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
	val items = ArrayList<String>()

	fun add(str: String, onItemChanged: () -> Unit) {
		items.add(str)
		notifyItemChanged(items.size - 1)
		onItemChanged()
	}

	fun add(str: String) {
		items.add(str)
		notifyItemChanged(items.size - 1)
	}

	override fun getItemCount(): Int = items.size

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
		(holder as ChatViewHolder).tv.text = items[position]
	}

	override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
		val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_chat, parent, false)
		return ChatViewHolder(view)
	}

}

class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
	val tv: TextView

	init {
		tv = itemView.tv
	}
}