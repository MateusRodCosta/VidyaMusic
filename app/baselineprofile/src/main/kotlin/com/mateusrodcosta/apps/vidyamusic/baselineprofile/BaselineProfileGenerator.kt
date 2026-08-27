package com.mateusrodcosta.apps.vidyamusic.baselineprofile

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This test class generates a basic startup baseline profile for the target package.
 *
 * We recommend you start with this but add important user flows to the profile to improve their performance.
 * Refer to the [baseline profile documentation](https://d.android.com/topic/performance/baselineprofiles)
 * for more information.
 *
 * You can run the generator with the "Generate Baseline Profile" run configuration in Android Studio or
 * the equivalent `generateBaselineProfile` gradle task:
 * ```
 * ./gradlew :app:generateReleaseBaselineProfile
 * ```
 * The run configuration runs the Gradle task and applies filtering to run only the generators.
 *
 * Check [documentation](https://d.android.com/topic/performance/benchmarking/macrobenchmark-instrumentation-args)
 * for more information about available instrumentation arguments.
 *
 * After you run the generator, you can verify the improvements running the [StartupBenchmarks] benchmark.
 *
 * When using this class to generate a baseline profile, only API 33+ or rooted API 28+ are supported.
 *
 * The minimum required version of androidx.benchmark to generate a baseline profile is 1.2.0.
 **/
@RunWith(AndroidJUnit4::class)
@LargeTest
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate() {
        // The application id for the running build variant is read from the instrumentation arguments.
        rule.collect(
            packageName = InstrumentationRegistry.getArguments().getString("targetAppId")
                ?: throw Exception("targetAppId not passed as instrumentation runner arg"),

            // See: https://d.android.com/topic/performance/baselineprofiles/dex-layout-optimizations
            includeInStartupProfile = true
        ) {
            // Allow notification permission via ADB
            device.executeShellCommand("pm grant $packageName android.permission.POST_NOTIFICATIONS")

            // This block defines the app's critical user journey. Here we are interested in
            // optimizing for app startup. But you can also navigate and scroll through your most important UI.

            // Start default activity for your app
            pressHome()
            startActivityAndWait()

            // 1. Wait until the content is asynchronously loaded
            waitForAsyncContent()
            // 2. Scroll the roster content
            scrollRosterJourney()
            // 3. Play random track
            playRandomTrackJourney()
            // 4. Interact with miniplayer
            interactWithMiniplayerJourney()
            // 5. Expand miniplayer to full player
            miniToFullPlayerJourney()
            // 6. Interact with full player (BUGGY)
            // interactWithFullPlayerJourney()
            // 7. Collapse full player
            collapseFullPlayerJourney()

            // Check UiAutomator documentation for more information how to interact with the app.
            // https://d.android.com/training/testing/other-components/ui-automator
        }
    }

    fun MacrobenchmarkScope.waitForAsyncContent() {
        device.wait(Until.hasObject(By.res("roster")), 5_000)
        val roster = device.findObject(By.res("roster"))
        roster.wait(Until.hasObject(By.res("track_item")), 5_000)
    }

    fun MacrobenchmarkScope.scrollRosterJourney() {
        device.wait(Until.hasObject(By.res("roster")), 5_000)
        val roster = device.findObject(By.res("roster"))
        roster.setGestureMargin(device.displayWidth / 5)

        roster.fling(Direction.DOWN)
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        roster.fling(Direction.UP)
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun MacrobenchmarkScope.playRandomTrackJourney() {
        device.wait(Until.hasObject(By.res("roster")), 5_000)
        val roster = device.findObject(By.res("roster"))
        val trackItems = roster.findObjects(By.res("track_item"))

        val index = (iteration ?: 0) % trackItems.size
        trackItems[index].click()
        try {
            Thread.sleep(5000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun MacrobenchmarkScope.interactWithMiniplayerJourney() {
        device.wait(Until.hasObject(By.res("miniplayer")), 5_000)
        val miniplayer = device.findObject(By.res("miniplayer"))
        val miniplayerContent = miniplayer.findObject(By.res("miniplayer_content"))

        val playPauseButton = miniplayerContent.findObject(By.res("miniplayer_playpause"))
        val nextButton = miniplayerContent.findObject(By.res("miniplayer_next"))

        playPauseButton.click()
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        playPauseButton.click()
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        nextButton.click()
        try {
            Thread.sleep(5000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun MacrobenchmarkScope.miniToFullPlayerJourney() {
        val miniplayer = device.findObject(By.res("miniplayer"))
        val miniplayerContent = miniplayer.findObject(By.res("miniplayer_content"))

        miniplayerContent.swipe(Direction.UP, 1.0f)
        device.wait(Until.hasObject(By.res("full_player")), 5_000)
    }

    /*
    fun MacrobenchmarkScope.interactWithFullPlayerJourney() {
        device.wait(Until.hasObject(By.res("full_player")), 5_000)
        val fullPlayer = device.findObject(By.res("full_player"))
        val fullPlayerControls = fullPlayer.findObject(By.res("full_player_controls"))

        val playPauseButton = fullPlayerControls.findObject(By.res("full_player_playpause"))

        playPauseButton.click()
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
     */

    fun MacrobenchmarkScope.collapseFullPlayerJourney() {
        device.wait(Until.hasObject(By.res("full_player")), 5_000)
        val fullPlayer = device.findObject(By.res("full_player"))
        val fullPlayerHeader = fullPlayer.findObject(By.res("full_player_header"))

        val collapseButton = fullPlayerHeader.findObject(By.res("full_player_collapse"))

        collapseButton.click()
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        device.wait(Until.gone(By.res("full_player")), 5_000)
    }
}