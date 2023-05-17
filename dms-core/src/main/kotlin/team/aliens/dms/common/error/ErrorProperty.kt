package team.aliens.dms.common.error

interface ErrorProperty {

    fun status(): Int

    fun message(): String

    fun code(): String

    fun formatMessage(vararg datas: String): ErrorProperty {
        return object : ErrorProperty {
            override fun status(): Int = this.status()
            override fun message(): String = this.message().format(datas)
            override fun code(): String = this.code()
        }
    }
}
