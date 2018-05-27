package example.inc.trence.tafa

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Tafa on 27/05/2018.
 * CopyRight
 */
@Parcelize
data class detail(val id: Int,val title: String, val body:String,
                  val name: String, val img: String, val username: String):Parcelable