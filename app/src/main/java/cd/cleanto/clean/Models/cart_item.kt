package cd.cleanto.clean.Models

import android.os.Parcel
import android.os.Parcelable

data class cart_item(
    val id: Int,
    val name: String,
    val price: Double,
    var quantity: Int = 1,
    val image: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeDouble(price)
        parcel.writeInt(quantity)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<cart_item> {
        override fun createFromParcel(parcel: Parcel): cart_item {
            return cart_item(parcel)
        }

        override fun newArray(size: Int): Array<cart_item?> {
            return arrayOfNulls(size)
        }
    }
}