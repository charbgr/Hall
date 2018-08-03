package charbgr.github.com.hall.hall

import android.os.Bundle
import charbgr.github.com.hall.hall.history.HistoryResult.Pop
import charbgr.github.com.hall.hall.history.HistoryResult.SwitchTab
import charbgr.github.com.hall.hall.history.HistoryTape

abstract class Hall<T : Screen, O : Any>(
  private val rootScreenRequester: RootScreenRequester<T>
) {

  protected lateinit var currentTab: Tab
  private val screenStack: MutableMap<Tab, MutableList<T>> = mutableMapOf()
  var historyTape: HistoryTape = HistoryTape.NOOP
  var switchTabListener: SwitchTabListener = SwitchTabListener.NOOP
  private var blockRecord = false

  fun initTabs(currentTab: Tab, vararg tabs: Tab) {
    this.currentTab = currentTab
    if (!tabs.contains(currentTab)) {
      screenStack[currentTab] = mutableListOf()
    }

    for (tab in tabs) {
      screenStack[tab] = mutableListOf()
    }

    runWithoutRecording {
      val rootScreen = rootScreenRequester.rootScreenFor(currentTab)
      push(rootScreen)
    }
  }

  fun switch(tab: Tab, options: O? = null) {
    if (tab == currentTab) {
      return
    }

    recordSwitchTab(tab)

    val toStack = stackFrom(tab)
    if (toStack.isEmpty()) {
      currentTab = tab
      val rootScreen = rootScreenRequester.rootScreenFor(tab)
      push(rootScreen, options)
    } else {
      val previousTab = currentTab
      currentTab = tab
      val previousScreen = topScreen(previousTab) ?: return
      val toScreen = topScreen(currentTab) ?: return
      switchTabInternal(tab, previousScreen, toScreen)
    }
  }

  fun push(screen: T, options: O? = null) {
    currentStack().add(screen)
    actualPush(screen, options)
    recordPush(screen)
  }

  fun pop(options: O? = null) {
    val screen = topScreen() ?: return
    pop(screen, options)
  }

  fun pop(screen: T, options: O? = null) {
    currentStack().remove(screen)
    actualPop(screen, options)
  }

  protected abstract fun switchTabInternal(tab: Tab, fromScreen: T, toScreen: T)
  abstract fun actualPush(screen: T, options: O?)
  abstract fun actualPop(screen: T, options: O?)
  abstract fun restoreIfNeeded(savedInstanceState: Bundle?): Boolean

  fun handleBack(options: O? = null): Boolean {
    val topScreen = topScreen() ?: return false
    val consumed = topScreen.handleBack()

    if (!consumed) {
      val result = historyTape.replay()
      when (result) {
        Pop -> pop(options)
        is SwitchTab -> {
          runWithoutRecording {
            switchTabListener.switchedToTab(result.tab)
            switch(result.tab, options)
          }
        }
      }

      return true
    }

    return consumed
  }

  private fun recordPush(screen: T) {
    if (!blockRecord) {
      historyTape.push(screen)
    }
  }

  private fun recordSwitchTab(tab: Tab) {
    if (!blockRecord) {
      historyTape.switchedTab(tab)
    }
  }

  private fun topScreen(tab: Tab = currentTab): T? = stackFrom(tab).lastOrNull()
  protected fun currentStack(): MutableList<T> = stackFrom(currentTab)
  protected fun stackFrom(tab: Tab): MutableList<T> = screenStack[tab]
      ?: throw IllegalArgumentException("Can't get a tab that hasn't been initialized")

  private infix fun runWithoutRecording(func: () -> Unit) {
    blockRecord = true
    func()
    blockRecord = false
  }

  override fun toString(): String = "Hall(screenStack=$screenStack, currentTab=$currentTab)"
}
