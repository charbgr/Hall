package charbgr.github.com.hall.hall

import charbgr.github.com.hall.RenderRecorder

class RecordingRootScreenRequester(
    val screens: MutableMap<Tab, DummyScreen>
) : RootScreenRequester<DummyScreen> {

  val rootScreenForEvents = RenderRecorder.create<Tab>()

  override fun rootScreenFor(tab: Tab): DummyScreen {
    rootScreenForEvents.add(tab)
    return screens[tab]!!
  }

  fun reset() {
    rootScreenForEvents.reset()
  }
}
