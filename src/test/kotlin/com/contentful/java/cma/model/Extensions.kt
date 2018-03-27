package com.contentful.java.cma.model

fun CMASystem.setCreatedBy(link: CMALink): CMASystem {
    this.createdBy = link
    return this
}
