package com.oksisi213.systemuicontroller

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.RadioGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_preset.view.*

/**
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 * Created by Charles on 16/01/2018.
 */
class PresetFragment : Fragment(), MyWindowFocusChangeListener {
	companion object {
		val TAG = Companion::class.java.simpleName

		fun newInstance(): PresetFragment {
			return PresetFragment()
		}
	}

	lateinit var radioGroup: RadioGroup
	lateinit var listView: ListView

	val adapter by lazy { SimpleTextAdapter() }


	val flagList by lazy {
		HashMap<Int, String>().apply {
			put(View.SYSTEM_UI_FLAG_FULLSCREEN, "SYSTEM_UI_FLAG_FULLSCREEN")
			put(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION, "SYSTEM_UI_FLAG_HIDE_NAVIGATION")
			put(View.SYSTEM_UI_FLAG_IMMERSIVE, "SYSTEM_UI_FLAG_IMMERSIVE")
			put(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY, "SYSTEM_UI_FLAG_IMMERSIVE_STICKY")
			put(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN, "SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN")
			put(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION, "SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION")
			put(View.SYSTEM_UI_FLAG_LAYOUT_STABLE, "SYSTEM_UI_FLAG_LAYOUT_STABLE")
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				put(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR, "SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR")
				put(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR, "SYSTEM_UI_FLAG_LIGHT_STATUS_BAR")
			}
			put(View.SYSTEM_UI_FLAG_LOW_PROFILE, "SYSTEM_UI_FLAG_LOW_PROFILE")
		}
	}


	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_preset, container, false)
		radioGroup = view.radio_group
		listView = view.listView
		listView.adapter = adapter


		radioGroup.setOnCheckedChangeListener { _: RadioGroup?, id: Int ->
			Log.e(TAG, "setOnCheckedChangeListener")
			when (id) {
				R.id.radio_dim -> dimSystemBars()
				R.id.radio_hiding_status_bar -> hideStatusBar()
				R.id.radio_hiding_navigation_bar -> hideNavigationBar()
				R.id.radio_immersive -> setImmersiveMode()
				R.id.radio_immersive_sticky -> setStickyImmersiveMode()
				R.id.radio_reveal_the_bars -> setDefault()
				else -> setDefault()
			}
		}

		activity?.window?.decorView?.setOnSystemUiVisibilityChangeListener { visibility: Int ->
			Log.e(TAG, "setOnSystemUiVisibilityChangeListener")
			adapter.add(flagList[visibility])
		}
		return view
	}

	fun setDefault() {
		activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
	}

	fun hideStatusBar() {
		activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
	}

	fun hideNavigationBar() {
		activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
	}

	fun dimSystemBars() {
		activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE
	}

	fun setStickyImmersiveMode() {
		activity?.window?.decorView?.systemUiVisibility = (
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
						or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
						or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
				)
	}

	fun setImmersiveMode() {
		activity?.window?.decorView?.systemUiVisibility = (
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
						or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
						or View.SYSTEM_UI_FLAG_IMMERSIVE
				)
	}

	override fun setUserVisibleHint(isVisibleToUser: Boolean) {
		super.setUserVisibleHint(isVisibleToUser)
		Log.e(TAG, "setUserVisibleHint")
		if (isVisibleToUser) {
			adapter.clear()
		} else {

		}
	}


	override fun onWindowFocusChanged(hasFocus: Boolean) {
		if (isVisible && radioGroup.checkedRadioButtonId == R.id.radio_immersive_sticky) {
			setStickyImmersiveMode()
		}
	}

	inner class SimpleTextAdapter : BaseAdapter() {

		val items = ArrayList<String>()

		fun add(str: String?) {
			if (!str.isNullOrBlank()) {
				items.add(str!!)
				notifyDataSetChanged()
			}
		}

		fun clear() {
			items.clear()
			notifyDataSetChanged()
		}

		override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
			val tv: TextView = if (p1 == null) {
				TextView(p2?.context)
			} else {
				p1 as TextView
			}
			tv.text = items[p0]
			return tv
		}

		override fun getItem(p0: Int): String = items[p0]

		override fun getItemId(p0: Int): Long = p0.toLong()

		override fun getCount(): Int {
			return items.size
		}

	}
}