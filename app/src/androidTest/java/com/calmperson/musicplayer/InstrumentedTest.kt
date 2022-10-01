package com.calmperson.musicplayer

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class InstrumentedTest {

    private lateinit var device: UiDevice
    private lateinit var context: Context

    @Before
    fun before() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.pressHome()

        context = ApplicationProvider.getApplicationContext()
        val intent: Intent = context.packageManager.getLaunchIntentForPackage(context.packageName)!!
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)

        device.wait(Until.hasObject(By.pkg(context.packageName).depth(0)), 1000)
    }

    @Test
    fun test1() {
        device.findObject(findSelectorById("first_start")).click()
        device.findObject(UiSelector().text("Sounds")).click()
        device.findObject(findSelectorById("back_button")).apply { click() }.click()
        device.findObject(findSelectorById("first_start")).click()
        device.findObject(UiSelector().text("Sounds")).click()
        device.findObject(findSelectorById("confirm_button")).click()

        device.findObject(
            findSelectorById("table_scroll")
                .childSelector(findSelectorById("playlist_table")
                    .childSelector(UiSelector().className("android.widget.TableRow")
                        .childSelector(findSelectorById("play_button"))))
        ).click()
        Thread.sleep(2000)
        device.findObject(UiSelector().text("Arman Cekin - Hold On ft. BR_VE.mp3")).click()
        device.findObject(findSelectorById("pause_button")).click()
        device.findObject(findSelectorById("play_button")).click()
        device.findObject(findSelectorById("rewind_left_button")).click()
        Thread.sleep(1000)
        device.findObject(findSelectorById("rewind_right_button")).click()
    }

    private fun findSelectorById(id: String): UiSelector = UiSelector().resourceId("com.calmperson.musicplayer:id/$id")
}