package com.example.searchcollectapp.main

import com.example.searchcollectapp.Document
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

// sealed class 내의 data class에 맞춰서 직렬화와 역직렬화하기 위한 클래스
class DocumentTypeAdapter : JsonDeserializer<Document> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Document {
        val jsonObject = json.asJsonObject
        // ImageDocument에만 있는 image_url 유무를 기준으로 삼음
        return if (jsonObject.has("image_url")) {
            context.deserialize(json, Document.ImageDocument::class.java)
        } else {
            context.deserialize(json, Document.VideoDocument::class.java)
        }
    }
}