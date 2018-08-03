package charbgr.github.com.hall.hall

import android.os.Bundle
import charbgr.github.com.hall.RenderRecorder
import junit.framework.Assert.assertEquals

class RecordingHall(
    private val recordingRootScreenRequester: RecordingRootScreenRequester
) : Hall<DummyScreen, DummyOptions>(recordingRootScreenRequester) {

  private val switchTabInternalEvents = RenderRecorder.create<SwitchTabInternal>()
  private val actualPushEvents = RenderRecorder.create<ActualPush>()
  private val actualPopEvents = RenderRecorder.create<ActualPop>()
  val historyTapeRecorder = RecordingHistory(historyTape)

  init {
    historyTape = historyTapeRecorder
  }

  override fun switchTabInternal(tab: Tab, fromScreen: DummyScreen, toScreen: DummyScreen) {
    switchTabInternalEvents.add(SwitchTabInternal(tab, fromScreen, toScreen))
  }

  override fun actualPush(screen: DummyScreen, options: DummyOptions?) {
    actualPushEvents.add(ActualPush(screen, options))
  }

  override fun actualPop(screen: DummyScreen, options: DummyOptions?) {
    actualPopEvents.add(ActualPop(screen, options))
  }

  override fun restoreIfNeeded(savedInstanceState: Bundle?): Boolean = false

  fun assertCurrentTab(tab: Tab) = assertEquals(tab, currentTab)
  fun assertCurrentStack(vararg screens: DummyScreen) =
      assertEquals(screens.toList(), currentStack().toList())

  fun assertPush(screen: DummyScreen, options: DummyOptions? = null) {
    actualPushEvents.assertRenders(ActualPush(screen, options))
  }

  fun assertPop(screen: DummyScreen, options: DummyOptions? = null) {
    actualPopEvents.assertRenders(ActualPop(screen, options))
  }

  fun assertPopNotCalled() {
    actualPopEvents.assertNoRenders()
  }

  fun assertNoScreenRequestedFromListener() {
    recordingRootScreenRequester.rootScreenForEvents.assertNoRenders()
  }

  fun assertScreenRequestedFromListener(tab: Tab) {
    recordingRootScreenRequester.rootScreenForEvents.assertRenders(tab)
  }

  fun assertHistoryPushRecorded(dummyScreen: DummyScreen) {
    historyTapeRecorder.pushEvents.assertRenders(dummyScreen)
  }

  fun assertHistoryPeplayRecorded() {
    historyTapeRecorder.replayEvents.assertRenderedAtLeastOnce()
  }

  fun reset() {
    switchTabInternalEvents.reset()
    actualPushEvents.reset()
    actualPopEvents.reset()
    historyTapeRecorder.reset()
    recordingRootScreenRequester.reset()
  }

  fun assertNoSwitchtabInternalCalled() {
    switchTabInternalEvents.assertNoRenders()
  }

  fun assertSwitchTabCalled(tab: Tab, fromScreen: DummyScreen, toScreen: DummyScreen) {
    switchTabInternalEvents.assertRenders(SwitchTabInternal(tab, fromScreen, toScreen))
  }
}

data class SwitchTabInternal(
    val tab: Tab,
    val fromScreen: DummyScreen,
    val toScreen: DummyScreen
)

data class ActualPop(
    val screen: DummyScreen,
    val options: DummyOptions?
)

data class ActualPush(
    val screen: DummyScreen,
    val options: DummyOptions?
)
