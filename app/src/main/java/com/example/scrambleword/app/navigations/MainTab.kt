package com.example.scrambleword.app.navigations

enum class MainTab(
    val route: String,
    val labelFa: String
) {
    HOME(Routes.MAIN_HOME, "خانه"),
    PRACTICE(Routes.MAIN_PRACTICE, "تمرین"),
    LEARN(Routes.MAIN_LEARN, "یادگیری"),
    WORDS(Routes.MAIN_WORDS, "کلمات");

    companion object {
        fun fromRoute(route: String?): MainTab {
            if (route == null) return HOME
            return entries.find { route.startsWith(it.route) } ?: HOME
        }
    }


}