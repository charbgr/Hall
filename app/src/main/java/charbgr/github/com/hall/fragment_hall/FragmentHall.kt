package charbgr.github.com.hall.fragment_hall

import android.os.Bundle
import android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import android.support.v7.app.AppCompatActivity
import charbgr.github.com.hall.hall.Hall
import charbgr.github.com.hall.hall.RootScreenRequester
import charbgr.github.com.hall.hall.Tab
import charbgr.github.com.hall.log

class FragmentHall(
    private val activity: AppCompatActivity,
    private val containerId: Int,
    rootScreenRequester: RootScreenRequester<BaseFragment>
) : Hall<BaseFragment, FragmentTransactionOptions>(rootScreenRequester) {

  override fun actualPush(screen: BaseFragment, options: FragmentTransactionOptions?) {
    log("actual PUSH | detach + add ")
    val stack = currentStack()
    val detachedScreen = try {
      stack[stack.size - 2]
    } catch (e: IndexOutOfBoundsException) {
      null
    }
    val ft = activity.supportFragmentManager.beginTransaction()
    if (detachedScreen != null) {
      ft.detach(detachedScreen)
    }

    ft.add(containerId, screen, screen.screenTag())
        .addToBackStack(screen.screenTag())
        .commit()
  }

  override fun actualPop(screen: BaseFragment, options: FragmentTransactionOptions?) {
    log("actual POP: ${screen.screenTag()}")
    activity.supportFragmentManager.popBackStack(screen.tag, POP_BACK_STACK_INCLUSIVE)
  }

  override fun switchTabInternal(tab: Tab, fromScreen: BaseFragment, toScreen: BaseFragment) {
    log("switchTabInternal | hide + show ")
    activity.supportFragmentManager.beginTransaction()
        .hide(fromScreen)
        .show(toScreen)
        .commit()
  }

  override fun restoreIfNeeded(savedInstanceState: Bundle?): Boolean = false
}
