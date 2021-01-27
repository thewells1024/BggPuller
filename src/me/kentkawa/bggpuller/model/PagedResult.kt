package me.kentkawa.bggpuller.model

interface PagedResult<T> {
    val totalItems: Int
    val items: List<T>
}
