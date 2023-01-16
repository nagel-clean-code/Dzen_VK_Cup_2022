package com.example.dzen_vk_cup_2022.task_1.repositories

import com.example.dzen_vk_cup_2022.R


class CategoriesRepository {
    val mapCategories = mapOf(
        "modeOfLifeList" to listOf("Прогулки", "Путешествия", "Спорт", "Хобби", "Медиа", "Хозяйство", "Финансы"),
        "beautyAndHealth" to listOf("Медицина", "Косметика и уход", "Бьюти услуги", "Психология"),
        "interests" to listOf("Политика", "Новости", "Наука и образование", "История", "Литература", "Изотерика", "Искусство", "Автомобили", "Хобби"),
        "kitchen" to listOf("Еда", "Рестораны", "Рецепты", "Приборы"),
        "recreationAndEntertainment" to listOf("Юмор", "Кино и музыка", "Путешествия"),
        "family" to listOf("Дети", "Отношения", "Животные", "Уют"),
    )

    val mapPictures = mapOf(
        "Юмор" to R.drawable.humor,
        "Кино и музыка" to R.drawable.movie,
        "Путешествия" to R.drawable.travel,

        "Дети" to R.drawable.kids,
        "Отношения" to R.drawable.relationships,
        "Животные" to R.drawable.animals,
        "Уют" to R.drawable.couch,

        "Еда" to R.drawable.food,
        "Рестораны" to R.drawable.restaurants,
        "Рецепты" to R.drawable.recipes,
        "Приборы" to R.drawable.cutlery,

        "Политика" to R.drawable.politics,
        "Новости" to R.drawable.news,
        "Наука и образование" to R.drawable.science_and_tech,
        "История" to R.drawable.history,
        "Литература" to R.drawable.literature,
        "Изотерика" to R.drawable.esoteric,
        "Искусство" to R.drawable.art,
        "Автомобили" to R.drawable.cars,
        "Хобби" to R.drawable.hobbie,

        "Прогулки" to R.drawable.hiking,
        "Спорт" to R.drawable.sport,
        "Медиа" to R.drawable.media,
        "Хозяйство" to R.drawable.agriculture,
        "Финансы" to R.drawable.finance,

        "Медицина" to R.drawable.medicine,
        "Косметика и уход" to R.drawable.beauty_treatment,
        "Бьюти услуги" to R.drawable.beauty,
        "Психология" to R.drawable.psychology
    )
}