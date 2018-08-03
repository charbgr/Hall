package charbgr.github.com.hall.hall

import junit.framework.Assert.assertTrue
import org.junit.Test

class HallTest {

  private val TAB1 = tab("TAB2")
  private val TAB2 = tab("TAB3")

  private val SCREEN1 = DummyScreen("screen1", false)
  private val SCREEN2 = DummyScreen("screen2", false)

  @Test
  fun test_init_tabs_requests_correct_tab() {
    val hall = hall()
    hall.initTabs(TAB1, TAB1, TAB2)

    hall.assertScreenRequestedFromListener(TAB1)
    hall.assertCurrentTab(TAB1)
    hall.assertCurrentStack(SCREEN1)
  }

  @Test
  fun test_init_tabs_requests_correct_tab_without_passing_all_tabs() {
    val hall = hall()
    hall.initTabs(TAB1, TAB2)

    hall.assertScreenRequestedFromListener(TAB1)
    hall.assertCurrentTab(TAB1)
    hall.assertCurrentStack(SCREEN1)
  }

  @Test
  fun test_init_tabs_pushes_screens() {
    val hall = hall()
    hall.initTabs(TAB1, TAB2)

    hall.assertPush(SCREEN1, null)
  }

  @Test
  fun test_init_tabs_does_not_record() {
    val hall = hall()
    hall.initTabs(TAB1, TAB2)

    hall.historyTapeRecorder.pushEvents.assertNoRenders()
    hall.historyTapeRecorder.switchedTabEvents.assertNoRenders()
    hall.historyTapeRecorder.replayEvents.assertNoRenders()
  }

  @Test
  fun test_push() {
    val hall = hall()
    hall.initTabs(TAB1, TAB2)
    hall.reset()

    hall.push(DummyScreen("foo", false))
    hall.assertCurrentStack(SCREEN1, DummyScreen("foo", false))
    hall.assertPush(DummyScreen("foo", false), null)
    hall.assertHistoryPushRecorded(DummyScreen("foo", false))
    hall.reset()

    hall.push(DummyScreen("foo2", false), DummyOptions(1))
    hall.assertCurrentStack(SCREEN1, DummyScreen("foo", false), DummyScreen("foo2", false))
    hall.assertPush(DummyScreen("foo2", false), DummyOptions(1))
    hall.assertHistoryPushRecorded(DummyScreen("foo2", false))
  }

  @Test
  fun test_pop_top_screen() {
    val hall = hall()
    hall.initTabs(TAB1, TAB2)
    hall.push(DummyScreen("bar", false))
    hall.reset()

    hall.pop()
    hall.assertPop(DummyScreen("bar", false))
    hall.assertCurrentStack(SCREEN1)
  }

  @Test
  fun test_pop_screen() {
    val hall = hall()
    hall.initTabs(TAB1, TAB2)
    hall.push(DummyScreen("foo", false))
    hall.push(DummyScreen("bar", false))
    hall.reset()

    hall.pop(DummyScreen("foo", false))
    hall.assertPop(DummyScreen("foo", false), null)
    hall.assertCurrentStack(SCREEN1, DummyScreen("bar", false))
  }

  @Test
  fun test_switch_to_current_tab() {
    val hall = hall()
    hall.initTabs(TAB1, TAB2)
    hall.reset()

    hall.switch(TAB1)

    hall.assertNoSwitchtabInternalCalled()
    hall.assertNoScreenRequestedFromListener()
  }

  @Test
  fun test_switch_tab_requests_tab() {
    val hall = hall()
    hall.initTabs(TAB1, TAB2)
    hall.reset()

    hall.switch(TAB2)
    hall.assertScreenRequestedFromListener(TAB2)
    hall.assertCurrentStack(SCREEN2)
  }

  @Test
  fun test_switch_tab_with_already_screen_in_stack() {
    val hall = hall()
    hall.initTabs(TAB1, TAB2)
    hall.switch(TAB2)
    hall.switch(TAB1)
    hall.reset()

    hall.switch(TAB2)
    hall.assertCurrentTab(TAB2)
    hall.assertSwitchTabCalled(TAB2, SCREEN1, SCREEN2)
    hall.assertNoScreenRequestedFromListener()
    hall.reset()

    hall.switch(TAB1)
    hall.assertCurrentTab(TAB1)
    hall.assertSwitchTabCalled(TAB1, SCREEN2, SCREEN1)
    hall.assertNoScreenRequestedFromListener()
  }

  @Test
  fun test_top_screen_consumes_back() {
    val hall = hall(rootScreenRequester = oneTab(screen = DummyScreen("foo", true)))
    hall.initTabs(TAB1)
    hall.reset()

    assertTrue(hall.handleBack())
    hall.assertPopNotCalled()
    hall.assertNoSwitchtabInternalCalled()
  }

  @Test
  fun test_pops_screen_from_history() {

  }

  @Test
  fun test_switches_tab_from_history() {

  }

  private fun hall(
      rootScreenRequester: RecordingRootScreenRequester = defaultRootScreenRequester()
  ): RecordingHall = RecordingHall(rootScreenRequester)

  private fun defaultRootScreenRequester() = RecordingRootScreenRequester(mutableMapOf(
      TAB1 to SCREEN1,
      TAB2 to SCREEN2
  ))

  private fun oneTab(
      tab: Tab = TAB1,
      screen: DummyScreen
  ) = RecordingRootScreenRequester(mutableMapOf(tab to screen))

  private fun tab(title: CharSequence): Tab = object : Tab {
    override fun title(): CharSequence = title
  }

}
