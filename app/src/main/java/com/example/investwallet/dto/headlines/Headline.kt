package com.example.investwallet.dto.headlines

data class Headline(
    val astDescription: AstDescription,
    val id: String,
    val permission: String,
    val published: Int,
    val relatedSymbols: List<RelatedSymbol>,
    val shortDescription: String,
    val source: String,
    val title: String,
    val urgency: Int,
    val sourceLogoId: String,
)