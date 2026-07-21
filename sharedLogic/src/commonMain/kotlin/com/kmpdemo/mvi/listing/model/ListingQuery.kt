package com.kmpdemo.mvi.listing.model

/**
 * Default query parameters for `/apiv4/listing`.
 * Pagination uses 1-based `pgnum` as required by the backend.
 */
object ListingQuery {
    const val ENTITIES = "2,4,172"
    const val OTHER_ENT = ""
    const val EXCL_ENT = "7"
    const val ITEM_NUMBER = 10
    const val DEFAULT_PAGE_SIZE = 10
    const val LISTING_PATH = "/apiv4/listing"

    fun buildPath(pageOneBased: Int, pageSize: Int = DEFAULT_PAGE_SIZE): String =
        "$LISTING_PATH?entities=$ENTITIES&otherent=$OTHER_ENT&exclent=$EXCL_ENT" +
            "&pgnum=$pageOneBased&inum=$ITEM_NUMBER&pgsize=$pageSize"
}
