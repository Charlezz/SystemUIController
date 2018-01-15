package com.oksisi213.systemuicontroller

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
	val TAG = MainActivity::class.java.simpleName

	val adapter: MyPagerAdapter by lazy {
		MyPagerAdapter(supportFragmentManager)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		viewPager.adapter = adapter

		adapter.addPage(CheckBoxFragment.newInstance())

	}

	inner class MyPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

		var items = ArrayList<Fragment>()

		fun setPages(items: ArrayList<Fragment>) {
			this.items = items
			notifyDataSetChanged()
		}

		fun addPage(f: Fragment) {
			items.add(f)
			notifyDataSetChanged()
		}

		override fun getItem(position: Int): Fragment {
			return items.get(position)
		}

		override fun getCount(): Int {
			return items.size
		}

		override fun getPageTitle(position: Int): CharSequence? {
			return "Checked"
		}
	}

	override fun onWindowFocusChanged(hasFocus: Boolean) {
		super.onWindowFocusChanged(hasFocus)
		if (hasFocus) {
			Log.e(TAG, "onWindowFocusChanged1:${window.decorView.systemUiVisibility}")
			window.decorView.systemUiVisibility = (
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE
							or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
							or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
							or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
							or View.SYSTEM_UI_FLAG_FULLSCREEN
							or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
			Log.e(TAG, "onWindowFocusChanged2:${window.decorView.systemUiVisibility}")

		}
	}
}
