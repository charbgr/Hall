package charbgr.github.com.hall.hall

data class DummyScreen(
  private val tag: String,
  var handleBack: Boolean
) : Screen {
  override fun screenTag(): String = tag
  override fun handleBack(): Boolean = handleBack
}
