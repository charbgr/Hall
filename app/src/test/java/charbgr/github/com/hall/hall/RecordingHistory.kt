package charbgr.github.com.hall.hall

import charbgr.github.com.hall.RenderRecorder
import charbgr.github.com.hall.hall.history.HistoryResult
import charbgr.github.com.hall.hall.history.HistoryTape

class RecordingHistory(
    val actualTape: HistoryTape = HistoryTape.NOOP
) : HistoryTape {

  val pushEvents = RenderRecorder.create<Screen>()
  val switchedTabEvents = RenderRecorder.create<Tab>()
  val replayEvents = RenderRecorder.create<Unit>()
  var overrideHistoryResult: HistoryResult? = null

  override fun push(screen: Screen) {
    pushEvents.add(screen)
    actualTape.push(screen)
  }

  override fun switchedTab(tab: Tab) {
    switchedTabEvents.add(tab)
    actualTape.switchedTab(tab)
  }

  override fun replay(): HistoryResult {
    replayEvents.add(Unit)
    if (overrideHistoryResult != null) {
      return overrideHistoryResult!!
    }
    return actualTape.replay()
  }

  fun reset() {
    pushEvents.reset()
    switchedTabEvents.reset()
    replayEvents.reset()
  }
}
