package dependencies.dependencies

import dependencies.Versions

object AndroidTestDependencies{

    val test_ext = "androidx.test.ext:junit:${Versions.test_ext}"
    val kotlin_test = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"
    val espresso_core = "androidx.test.espresso:espresso-core:${Versions.espresso_core}"
    val espresso_contrib = "androidx.test.espresso:espresso-contrib:${Versions.espresso_core}"
    val compose_ui_test = "androidx.compose.ui:ui-test-junit4:${Versions.compose_version}"
    val compose_ui_tooling = "androidx.compose.ui:ui-tooling:${Versions.compose_version}"
}