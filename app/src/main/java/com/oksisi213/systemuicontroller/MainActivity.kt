package com.oksisi213.systemuicontroller

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

	companion object {
		val TAG = MainActivity::class.java.simpleName

		val ID_DEFAULT_GROUP = 0
		val ID_HIDE_ACTION_BAR = 0
		val ID_SHOW_ACTION_BAR = 1
	}

	val adapter: MyPagerAdapter by lazy {
		MyPagerAdapter(supportFragmentManager)
	}

	val focusListenerList = ArrayList<MyWindowFocusChangeListener>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		supportActionBar?.hide()
		viewPager.adapter = adapter

		adapter.addPage(CheckBoxFragment.newInstance().also { checkBoxFragment ->
			focusListenerList.add(checkBoxFragment)
		}, "CheckBox")
		adapter.addPage(PresetFragment.newInstance().also { presetFragment ->
			focusListenerList.add(presetFragment)
		}, "Preset")

		adapter.addPage(ChatFragment.newInstance().also { chatFragment ->
			focusListenerList.add(chatFragment)
		}, "Chat demo")

	}

	inner class MyPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

		var items = ArrayList<Pair<Fragment, String>>()

		fun setPages(items: ArrayList<Pair<Fragment, String>>) {
			this.items = items
			notifyDataSetChanged()
		}

		fun addPage(f: Fragment, s: String) {
			items.add(Pair(f, s))
			notifyDataSetChanged()
		}

		override fun getItem(position: Int): Fragment {
			return items[position].first
		}

		override fun getCount(): Int {
			return items.size
		}

		override fun getPageTitle(position: Int): CharSequence? {
			return items[position].second
		}
	}

	override fun onWindowFocusChanged(hasFocus: Boolean) {
		super.onWindowFocusChanged(hasFocus)
		focusListenerList.forEach { listener: MyWindowFocusChangeListener? ->
			listener?.onWindowFocusChanged(hasFocus)
		}
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menu?.add(ID_DEFAULT_GROUP, ID_HIDE_ACTION_BAR, ID_HIDE_ACTION_BAR, "Hide Actionbar")
		menu?.add(ID_DEFAULT_GROUP, ID_SHOW_ACTION_BAR, ID_SHOW_ACTION_BAR, "Show Actionbar")
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		when (item?.itemId) {
			ID_HIDE_ACTION_BAR -> supportActionBar?.hide()
			ID_SHOW_ACTION_BAR -> supportActionBar?.show()
		}
		return super.onOptionsItemSelected(item)
	}
}

