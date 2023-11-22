package com.morpion.taximeter.data.local.entity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.morpion.taximeter.domain.model.ui.TaximeterHistoryUIModel
import org.jetbrains.annotations.NotNull

@Entity(tableName = "taximeter_history")
data class TaximeterHistoryLocalData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") @NotNull var id: Int,
    @ColumnInfo(name = "paid") val paid: String?,
    @ColumnInfo(name = "distance") val distance: String?,
    @ColumnInfo(name = "time") val time: String?,
    @ColumnInfo(name = "date") val date: Long?,
    @ColumnInfo(name = "image") var img: Bitmap?
)

fun TaximeterHistoryLocalData.toUiModel() : TaximeterHistoryUIModel{
    return TaximeterHistoryUIModel(
        id = id,
        paid = paid?: "",
        distance = distance?: "",
        time = time?: "",
        date = date?: 0,
        img = img
    )
}