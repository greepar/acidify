@file:JvmName("Main")

package org.ntqqrev.yogurt

import com.github.ajalt.mordant.terminal.Terminal
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.ntqqrev.milky.milkyPackageVersion
import org.ntqqrev.milky.milkyVersion
import org.ntqqrev.yogurt.util.addSigIntHandler
import org.ntqqrev.yogurt.util.checkSecurity
import kotlin.jvm.JvmName

val t = Terminal()

fun main() {
    t.println("""
        | Starting ${BuildKonfig.name} v${BuildKonfig.version}
        | .--------------------------------------.
        | |   __  __                       __    |
        | |   \ \/ /___  ____ ___  _______/ /_   |
        | |    \  / __ \/ __ `/ / / / ___/ __/   |
        | |    / / /_/ / /_/ / /_/ / /  / /_     |
        | |   /_/\____/\__, /\__,_/_/   \__/     |
        | |           /____/   Acidify + Milky   |
        | '--------------------------------------'
        | Commit Hash:    ${BuildKonfig.commitHash}
        | Milky Version:  $milkyVersion+@saltify/milky-types@$milkyPackageVersion
        | Build Time:     ${BuildKonfig.buildTime}
        | Data Directory: ${SystemFileSystem.resolve(Path("."))}
    """.trimMargin()
    )
    checkSecurity()
    YogurtApp.createServer()
        .addSigIntHandler()
        .start(wait = true)
}