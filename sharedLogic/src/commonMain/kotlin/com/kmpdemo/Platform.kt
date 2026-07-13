package com.kmpdemo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform