package com.example.vinam.musicgo.spotifymodule.models
import com.google.gson.annotations.SerializedName



data class SpotifyUser(

    @SerializedName("country")
    var country: String,
    @SerializedName("display_name")
    var displayName: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("explicit_content")
    var explicitContent: ExplicitContent,
    @SerializedName("external_urls")
    var externalUrls: ExternalUrls,
    @SerializedName("followers")
    var followers: Followers,
    @SerializedName("href")
    var href: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("images")
    var images: List<Image>,
    @SerializedName("product")
    var product: String,
    @SerializedName("type")
    var type: String,
    @SerializedName("uri")
    var uri: String
)

data class ExplicitContent(
    @SerializedName("filter_enabled")
    var filterEnabled: Boolean,
    @SerializedName("filter_locked")
    var filterLocked: Boolean
)

data class ExternalUrls(
    @SerializedName("spotify")
    var spotify: String
)

data class Followers(
    @SerializedName("href")
    var href: Any?,
    @SerializedName("total")
    var total: Int
)

data class Image(
    @SerializedName("height")
    var height: Any?,
    @SerializedName("url")
    var url: String,
    @SerializedName("width")
    var width: Any?
)