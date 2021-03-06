// SPDX-License-Identifier: MIT OR Apache-2.0

object Publish {
    const val group = "com.github.msink"
    const val user = "msink"
    object pom {
        const val url = "https://github.com/msink/kotlin-libui"
        const val connection = "scm:git:https://github.com/msink/kotlin-libui.git"
        const val devConnection = "scm:git:git@github.com:msink/kotlin-libui.git"
    }
}

object Kotlin {
    const val version = "1.3.31"
    const val repo = "https://dl.bintray.com/kotlin/kotlin-dev"
}

object Libui {
    const val version = "alpha4.1-openfolder"
    const val repo = "https://github.com/msink/libui"
}

object Dokka {
    const val version = "0.9.17"
}

object Download {
    const val version = "3.4.3"
}

object Bintray {
    const val version = "1.8.4-jetbrains-5"
    const val repo = "https://dl.bintray.com/jetbrains/kotlin-native-dependencies"
}
