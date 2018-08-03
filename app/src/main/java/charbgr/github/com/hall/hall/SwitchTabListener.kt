package charbgr.github.com.hall.hall

interface SwitchTabListener {
  fun switchedToTab(tab: Tab)

  companion object {
    val NOOP = object : SwitchTabListener {
      override fun switchedToTab(tab: Tab) {
      }
    }
  }
}
