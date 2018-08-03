package charbgr.github.com.hall.hall.history

import charbgr.github.com.hall.hall.Tab

sealed class HistoryResult {
  class SwitchTab(val tab: Tab) : HistoryResult()
  object Pop : HistoryResult()
}
