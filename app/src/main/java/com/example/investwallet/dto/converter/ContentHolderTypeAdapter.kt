package com.example.investwallet.dto.converter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class ContentHolderTypeAdapter : TypeAdapter<Content>() {
    private val gson: Gson = Gson()

    override fun write(out: JsonWriter?, value: Content?) { throw NotImplementedError() }

    override fun read(reader: JsonReader?): Content {

        reader?.apply {
            return getContent(reader)
        }

        return Content.ErrorContent
    }

    private fun getContent(reader: JsonReader):Content{
        if (reader.peek() == JsonToken.STRING)
            return Content.StringContent(reader.nextString())

        if (reader.peek() == JsonToken.BEGIN_OBJECT)
            return Content.ChildrenContent(getChildren(reader))

        return Content.ErrorContent
    }


    private fun getChildren(reader: JsonReader): Children {

        reader.beginObject()

        var chType = ""
        var chParams: Params? = null
        val chChildren:MutableList<Content> = mutableListOf()

        while (reader.hasNext())
        {
            val fieldName:String = reader.nextName()

            if(fieldName == "type")
                chType = reader.nextString()

            if(fieldName == "params")
                chParams = gson.fromJson(reader, Params::class.java)

            if(fieldName == "children")
            {
                reader.beginArray()

                while (reader.hasNext())
                    chChildren.add(getContent(reader))

                reader.endArray()
            }
        }

        reader.endObject()

        return Children(
            children = chChildren.toList(),
            type = chType,
            params = chParams
        )
    }
}

data class Children(
    val children: List<Content>,
    val type: String,
    val params: Params? = null
)

data class Params(
    val symbol: String?,
    val text: String
){
    fun getURLImg(): String{
        return if (symbol != null)
            "https://s3-symbol-logo.tradingview.com/$symbol--big.svg"
        else{
            ""
        }
    }
}

sealed class Content{
    data class ChildrenContent(val content: Children): Content()
    data class StringContent(val content:String): Content()
    object ErrorContent: Content()
}

data class newsDtoItem(
    val astDescription: Children,
    val id: String,
    val link: String,
    val permission: String,
    val published: Int,
    var relatedSymbols: List<RelatedSymbol>,
    val shortDescription: String,
    val source: String,
    val title: String
)

data class RelatedSymbol(
    @SerializedName("logoid")
    val logoid: String?,
    @SerializedName("currency-logoid")
    val currencyLogoid: String?,
    val symbol: String
): IUTag{
    override fun getURLImg(): String{
        return if (logoid != null)
            "https://s3-symbol-logo.tradingview.com/$logoid--big.svg"
        else{
            ""
        }
    }

    override fun getTag(): String {
        return symbol
    }

    override fun getDescriptions(): String {
        return symbol
    }
}