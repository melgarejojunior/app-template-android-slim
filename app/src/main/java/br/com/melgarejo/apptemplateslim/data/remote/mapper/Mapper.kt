package br.com.melgarejo.apptemplateslim.data.remote.mapper

interface Mapper<in I, out O> {
    fun transform(t: I): O
    fun transform(items: List<I>?): List<O>?
}