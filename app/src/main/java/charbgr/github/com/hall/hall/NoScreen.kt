package charbgr.github.com.hall.hall

object NoScreen: Screen {
  override fun screenTag(): String = "no-screen"
  override fun handleBack(): Boolean = false
}
