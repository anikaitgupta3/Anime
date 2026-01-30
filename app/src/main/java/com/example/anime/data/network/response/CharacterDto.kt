package com.example.anime.data.network.response

data class CharactersResponse(val data: List<CharacterDataDto>)
data class CharacterDataDto(val character: CharacterDetailDto?)
data class CharacterDetailDto(val name: String?)