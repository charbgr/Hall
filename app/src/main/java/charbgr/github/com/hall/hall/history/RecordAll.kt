package charbgr.github.com.hall.hall.history

import charbgr.github.com.hall.hall.Screen
import charbgr.github.com.hall.hall.Tab
import charbgr.github.com.hall.hall.history.RecordAll.HistoryAction.Push
import charbgr.github.com.hall.hall.history.RecordAll.HistoryAction.SwitchTab
import charbgr.github.com.hall.log

class RecordAll : HistoryTape {

  private val history = mutableListOf<HistoryAction>()

  override fun push(screen: Screen) {
    history.add(Push(screen))
    printHistory()
  }

  override fun switchedTab(tab: Tab) {
    history.add(SwitchTab(tab))
    printHistory()
  }

  override fun replay(): HistoryResult {
    val actionsSize = history.size
    val prevPrevAction = getHistoryActionOrNull(actionsSize - 2)
    return if (prevPrevAction == null) {
      removeLastHistoryAction()
      HistoryResult.Pop
    } else {
      return if (prevPrevAction is SwitchTab) {
        removeLastHistoryAction()
        removeLastHistoryAction()
        HistoryResult.SwitchTab(prevPrevAction.tab)
      } else {
        removeLastHistoryAction()
        HistoryResult.Pop
      }
    }
  }

  private fun getHistoryActionOrNull(idx: Int): HistoryAction? = try {
    history[idx]
  } catch (e: IndexOutOfBoundsException) {
    null
  }

  private fun removeLastHistoryAction() {
    val size = history.size - 1
    if (size >= 0) {
      history.removeAt(size)
    }
  }

  private fun printHistory() {
    log("History(${history.size}) : $history")
  }

  private sealed class HistoryAction {
    class Push(val screen: Screen) : HistoryAction() {
      override fun toString(): String = "Screen(${screen.screenTag()})"
    }

    class SwitchTab(val tab: Tab) : HistoryAction() {
      override fun toString(): String = "Tab(${tab.title()})"
    }
  }
}
