package charbgr.github.com.hall.hall

interface RootScreenRequester<T: Screen> {
  fun rootScreenFor(tab: Tab): T
}
