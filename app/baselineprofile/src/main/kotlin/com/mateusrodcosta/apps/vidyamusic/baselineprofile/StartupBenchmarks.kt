package com.mateusrodcosta.apps.vidyamusic.baselineprofile

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
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
 * This test class benchmarks the speed of app startup.
 * Run this benchmark to verify how effective a Baseline Profile is.
 * It does this by comparing [CompilationMode.None], which represents the app with no Baseline
 * Profiles optimizations, and [CompilationMode.Partial], which uses Baseline Profiles.
 *
 * Run this benchmark to see startup measurements and captured system traces for verifying
 * the effectiveness of your Baseline Profiles. You can run it directly from Android
 * Studio as an instrumentation test, or run all benchmarks for a variant, for example benchmarkRelease,
 * with this Gradle task:
 * ```
 * ./gradlew :app:baselineprofile:connectedBenchmarkReleaseAndroidTest
 * ```
 *
 * You should run the benchmarks on a physical device, not an Android emulator, because the
 * emulator doesn't represent real world performance and shares system resources with its host.
 *
 * For more information, see the [Macrobenchmark documentation](https://d.android.com/macrobenchmark#create-macrobenchmark)
 * and the [instrumentation arguments documentation](https://d.android.com/topic/performance/benchmarking/macrobenchmark-instrumentation-args).
 **/
@RunWith(AndroidJUnit4::class)
@LargeTest
class StartupBenchmarks {

    @get:Rule
    val rule = MacrobenchmarkRule()

    @Test
    fun startupCompilationNone() =
        benchmark(CompilationMode.None())

    @Test
    fun startupCompilationBaselineProfiles() =
        benchmark(CompilationMode.Partial(BaselineProfileMode.Require))

    private fun benchmark(compilationMode: CompilationMode) {
        // The application id for the running build variant is read from the instrumentation arguments.
        rule.measureRepeated(
            packageName = InstrumentationRegistry.getArguments().getString("targetAppId")
                ?: throw Exception("targetAppId not passed as instrumentation runner arg"),
            metrics = listOf(StartupTimingMetric()),
            compilationMode = compilationMode,
            startupMode = StartupMode.COLD,
            iterations = 10,
            setupBlock = {
                pressHome()
            },
            measureBlock = {
                // Allow notification permission via ADB
                device.executeShellCommand("pm grant $packageName android.permission.POST_NOTIFICATIONS")

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

                // Check the UiAutomator documentation for more information on how to
                // interact with the app.
                // https://d.android.com/training/testing/other-components/ui-automator
            }
        )
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