package charbgr.github.com.hall

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import charbgr.github.com.hall.fragment_hall.BaseFragment
import charbgr.github.com.hall.fragment_hall.FragmentHall
import charbgr.github.com.hall.hall.RootScreenRequester
import charbgr.github.com.hall.hall.SwitchTabListener
import charbgr.github.com.hall.hall.Tab
import charbgr.github.com.hall.hall.history.RecordAll
import kotlinx.android.synthetic.main.activity_main.navigation

class MainActivity : AppCompatActivity(), RootScreenRequester<BaseFragment>, SwitchTabListener {

  private val homeTab = object : Tab {
    override fun title(): CharSequence = "Home"
  }
  private val dashTab = object : Tab {
    override fun title(): CharSequence = "Dashboard"
  }
  private val notiTab = object : Tab {
    override fun title(): CharSequence = "Notification"
  }

  val hall = FragmentHall(this, R.id.hall_container, this)

  private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
    when (item.itemId) {
      R.id.navigation_home -> {
        hall.switch(homeTab)
        return@OnNavigationItemSelectedListener true
      }
      R.id.navigation_dashboard -> {
        hall.switch(dashTab)
        return@OnNavigationItemSelectedListener true
      }
      R.id.navigation_notifications -> {
        hall.switch(notiTab)
        return@OnNavigationItemSelectedListener true
      }
    }
    false
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    hall.historyTape = RecordAll()
    hall.switchTabListener = this
    hall.initTabs(homeTab, dashTab, notiTab)
    hall.restoreIfNeeded(savedInstanceState)

    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
  }

  override fun onBackPressed() {
    if (!hall.handleBack()) {
      super.onBackPressed()
    }
  }

  override fun rootScreenFor(tab: Tab): BaseFragment = when (tab) {
    homeTab -> BlankFragment.newInstance("Home", 0)
    dashTab -> BlankFragment.newInstance("Dashboard", 0)
    notiTab -> BlankFragment.newInstance("Notification", 0)
    else -> throw IllegalArgumentException("oooops")
  }

  override fun switchedToTab(tab: Tab) = when (tab) {
    homeTab -> navigation.selectedItemId = R.id.navigation_home
    dashTab -> navigation.selectedItemId = R.id.navigation_dashboard
    notiTab -> navigation.selectedItemId = R.id.navigation_notifications
    else -> {
    }
  }
}
