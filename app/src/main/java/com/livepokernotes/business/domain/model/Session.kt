package com.livepokernotes.business.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Session(
    val id: String,
    val comment: String,
    val updated_at: String,
    val created_at: String
): Parcelable
