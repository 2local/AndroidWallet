object AndroidConfig {
    const val COMPILE_SDK = 30
    const val MIN_SDK = 21
    const val TARGET_SDK = 30
    const val BUILD_TOOLS_VERSION = "30.0.2"

    const val ID = "two.local.io"
    const val VERSION_CODE = 87
    const val VERSION_NAME = "2.5.4"
}


interface BuildType {
    companion object {
        const val RELEASE = "release"
        const val DEBUG = "debug"
        const val STAGING = "staging"
    }

    val isMinifyEnabled: Boolean
    val isDebuggable: Boolean
    val versionNameSuffix: String
}

object BuildTypeDebug: BuildType {
    const val APPLICATION_ID_SUFFIX = ".dev"
    override val isMinifyEnabled = false
    override val isDebuggable = true
    override val versionNameSuffix = "-dev"
}

object BuildTypeStage: BuildType {
    const val APPLICATION_ID_SUFFIX = ".stage"
    override val isMinifyEnabled = false
    override val isDebuggable = true
    override val versionNameSuffix = "-stage"
}

object BuildTypeRelease: BuildType {
    override val isMinifyEnabled = true
    override val isDebuggable = false
    override val versionNameSuffix = ""
}

