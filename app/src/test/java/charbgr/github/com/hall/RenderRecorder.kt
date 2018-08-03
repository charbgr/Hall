package charbgr.github.com.hall

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import java.util.concurrent.CopyOnWriteArrayList

/**
 * RenderRecorder is a useful object for recording renders during tests.
 * It eliminates the boilerplate on Robot classes.
 *
 * Instead of using Mockito and make all Kotlin classes open, you can use the [RenderRecorder] to
 * record easily renders.
 *
 * e.g.
 * class FooRobot(val fooPresenter: FooPresenter) {
 *
 *   val viewModelRecorder: RenderRecorder<FooViewModel> = RenderRecorder.create()
 *
 *   val view: FooView = object: FooView {
 *      fun render(fooViewModel: FooViewModel) {
 *        viewModelRecorder.add(fooViewModel)
 *      }
 *   }
 *
 * }
 *
 * @Test
 * public test_foo_renders() {
 *  ...
 *
 *  val fooRobot = FooRobot(fooPresenter)
 *  fooRobot.viewModelRecorder.assertNoRenders()
 *  fooRobot.viewModelRecorder.assertRenders(fooViewModel1, fooViewModel2, ...)
 *
 *  ...
 * }
 *
 */
class RenderRecorder<T : Any?> {

  companion object {
    fun <T : Any> create(): RenderRecorder<T> = RenderRecorder()
    fun <T : Any> createDefault(defaultRender: T): RenderRecorder<T> {
      return RenderRecorder<T>().apply { add(defaultRender) }
    }
  }

  val renders: CopyOnWriteArrayList<T> = CopyOnWriteArrayList()

  fun add(render: T) {
    renders.add(render)
  }

  fun assertRenders(vararg expectedList: T) = apply {
    assertEquals(expectedList.size, renders.size)

    expectedList.forEachIndexed { index, expectedItem ->
      val actualItem = renders[index]
      assertEquals(expectedItem, actualItem)
    }
  }

  fun assertRenderedOnce() {
    assertEquals(1, renders.size)
  }

  fun assertRenderedTimes(times: Int) {
    assertEquals(times, renders.size)
  }

  fun assertRenderedAtLeastOnce() {
    assertTrue(renders.size >= 1)
  }

  fun assertNoRenders() = apply {
    assertEquals(0, renders.size)
  }

  fun reset() = apply {
    renders.clear()
  }
}
