package com.oksisi213.systemuicontroller

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import kotlinx.android.synthetic.main.fragment_check_box.view.*

/**
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 * Created by Charles on 15/01/2018.
 */

class CheckBoxFragment : Fragment(), MyWindowFocusChangeListener {


	val TAG = CheckBoxFragment::class.java.simpleName

	companion object {
		fun newInstance(): CheckBoxFragment {
			return CheckBoxFragment()
		}
	}

	lateinit var checkFullScreen: CheckBox
	lateinit var checkHideNavigation: CheckBox
	lateinit var checkImmersive: CheckBox
	lateinit var checkImmersiveSticky: CheckBox
	lateinit var checkLayoutFullscreen: CheckBox

	lateinit var checkLayoutHideNavigation: CheckBox
	lateinit var checkLayoutStable: CheckBox
	lateinit var checkLightNavigationBar: CheckBox
	lateinit var checkLightStatusBar: CheckBox
	lateinit var checkLowProfile: CheckBox

	val flagList by lazy {
		ArrayList<Pair<Int, CheckBox>>().apply {
			add(Pair(View.SYSTEM_UI_FLAG_FULLSCREEN, checkFullScreen))
			add(Pair(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION, checkHideNavigation))
			add(Pair(View.SYSTEM_UI_FLAG_IMMERSIVE, checkImmersive))
			add(Pair(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY, checkImmersiveSticky))
			add(Pair(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN, checkLayoutFullscreen))

			add(Pair(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION, checkLayoutHideNavigation))
			add(Pair(View.SYSTEM_UI_FLAG_LAYOUT_STABLE, checkLayoutStable))

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				add(Pair(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR, checkLightNavigationBar))
				add(Pair(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR, checkLightStatusBar))
			}
			add(Pair(View.SYSTEM_UI_FLAG_LOW_PROFILE, checkLowProfile))
		}
	}


	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_check_box, container, false)

		checkFullScreen = view.check_fullscreen
		checkHideNavigation = view.check_hide_navigation
		checkImmersive = view.check_immersive
		checkImmersiveSticky = view.check_immersive_sticky
		checkLayoutFullscreen = view.check_layout_fullscreen
		checkLayoutHideNavigation = view.check_layout_hide_navigation
		checkLayoutStable = view.check_layout_stable
		checkLightNavigationBar = view.check_light_navigation_bar
		checkLightStatusBar = view.check_light_status_bar
		checkLowProfile = view.check_low_profile

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
			checkLightNavigationBar.isEnabled = false
			checkLightStatusBar.isEnabled = false
		}

		flagList.forEach { pair: Pair<Int, CheckBox> ->
			pair.second.setOnCheckedChangeListener(checkedChangeListener)
		}

		activity?.window?.decorView?.setOnSystemUiVisibilityChangeListener { visibility: Int ->
			Log.e(TAG, "setOnSystemUiVisibilityChangeListener=$visibility")
			flagList.forEach { pair: Pair<Int, CheckBox> ->
				Log.e(TAG, "visibility=$visibility pair.first=${pair.first}")

				if (((pair.first and visibility) == pair.first)) {
					pair.second.setOnCheckedChangeListener(null)
					pair.second.isChecked = ((pair.first and visibility) == pair.first)
					pair.second.setOnCheckedChangeListener(checkedChangeListener)
				}
			}
		}
		return view
	}

	val checkedChangeListener = object : CompoundButton.OnCheckedChangeListener {
		override fun onCheckedChanged(button: CompoundButton?, isChecked: Boolean) {
			var flags = View.SYSTEM_UI_FLAG_VISIBLE

			if (checkFullScreen.isChecked) {
				flags = flags or View.SYSTEM_UI_FLAG_FULLSCREEN
			}
			if (checkHideNavigation.isChecked) {
				flags = flags or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
			}
			if (checkImmersive.isChecked) {
				flags = flags or View.SYSTEM_UI_FLAG_IMMERSIVE
			}
			if (checkImmersiveSticky.isChecked) {
				flags = flags or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
			}
			if (checkLayoutFullscreen.isChecked) {
				flags = flags or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
			}
			if (checkLayoutHideNavigation.isChecked) {
				flags = flags or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
			}
			if (checkLayoutStable.isChecked) {
				flags = flags or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
			}
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				if (checkLightNavigationBar.isChecked) {
					flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
				}
				if (checkLightStatusBar.isChecked) {
					flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
				}
			}
			if (checkLowProfile.isChecked) {
				flags = flags or View.SYSTEM_UI_FLAG_LOW_PROFILE
			}

			activity?.window?.decorView?.systemUiVisibility = flags

		}
	}

	override fun onWindowFocusChanged(hasFocus: Boolean) {
	}
}