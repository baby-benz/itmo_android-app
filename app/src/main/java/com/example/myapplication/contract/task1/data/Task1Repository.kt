package com.example.myapplication.contract.task1.data

import com.example.myapplication.contract.task1.Task1Contract
import com.example.myapplication.contract.task1.Task1Contract.Model.DetailedScreenData

class Task1Repository(language: String) : Task1Contract.Model {
    private val data: Array<DetailedScreenData> = when (language) {
        "english" -> dataForEnglish()
        "русский" -> dataForRussian()
        else -> dataForEnglish()
    }

    override fun detailedScreenData(detailedScreenNum: Int): DetailedScreenData {
        return data[detailedScreenNum]
    }

    private fun dataForEnglish(): Array<DetailedScreenData> {
        return arrayOf(
            DetailedScreenData(
                "Cannon",
                "Large-caliber gun classified as a type of artillery, which usually launches a projectile using explosive chemical propellant"
            ),
            DetailedScreenData(
                "Howitzer",
                "Long-ranged weapon, falling between a cannon, which fires shells at flat trajectories, and a mortar, which fires at high angles of ascent and descent"
            ),
            DetailedScreenData(
                "Mortar",
                "Simple, lightweight, man-portable, muzzle-loaded weapon, consisting of a smooth-bore metal tube fixed to a base plate with a lightweight bipod mount and a sight"
            ),
            DetailedScreenData(
                "Rocket artillery",
                "Artillery that uses rocket explosives as the projectile"
            )
        )
    }

    private fun dataForRussian(): Array<DetailedScreenData> {
        return arrayOf(
            DetailedScreenData(
                "Пушка",
                "Основным назначением является стрельба по настильной траектории, а также по воздушным и отдалённым целям"
            ),
            DetailedScreenData(
                "Гаубица",
                "Предназначена преимущественно для навесной стрельбы с закрытых огневых позиций, вне прямой видимости цели"
            ),
            DetailedScreenData(
                "Мортира",
                "Орудие с коротким стволом (обычно длиной канала менее 15 калибров) для навесной стрельбы"
            ),
            DetailedScreenData(
                "Реактивная артиллерия",
                "Применяет реактивные снаряды, т.е. доставляющий снаряд к цели, используя реактивный двигатель, установленный на самом снаряде и за счёт действия реактивной тяги сообщающий снаряду требуемую скорость полёта"
            )
        )
    }
}