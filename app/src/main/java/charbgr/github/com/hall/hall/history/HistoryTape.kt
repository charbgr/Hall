package charbgr.github.com.hall.hall.history

import charbgr.github.com.hall.hall.Screen
import charbgr.github.com.hall.hall.Tab

interface HistoryTape {
  fun push(screen: Screen)
  fun switchedTab(tab: Tab)
  fun replay(): HistoryResult

  companion object {
    val NOOP = object : HistoryTape {
      override fun push(screen: Screen) {
      }

      override fun switchedTab(tab: Tab) {
      }

      override fun replay(): HistoryResult = HistoryResult.Pop
    }
  }
}
