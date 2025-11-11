package org.ntqqrev.acidify.js

@RequiresOptIn(
    "This API is intended to wrap its Kotlin counterpart for JavaScript export only. " +
            "In Kotlin/JS, please use the original Kotlin API directly.",
    level = RequiresOptIn.Level.ERROR
)
internal annotation class AcidifyJsWrapper
