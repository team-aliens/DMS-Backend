package team.aliens.dms.common.error

interface ErrorProperty {

    fun status(): Int

    fun message(): String

    fun code(): String

    fun formatMessage(vararg datas: String): ErrorProperty {
        return this.let {
            object : ErrorProperty {
                override fun status(): Int = it.status()
                override fun message(): String = it.message().format(*datas)
                override fun code(): String = it.code()
            }
        }
    }
}