package enums

enum class ContextType {
    SETTING_NAME,
    CONFIRM_NAME,
    SETTING_TYPE,
    CONFIRM_TYPE,
    SETTING_DATA,
    CONFIRM_DATA;

    operator fun plus(int: Int) = enumValues<ContextType>()[this.ordinal + int]

    operator fun minus(int: Int) = enumValues<ContextType>()[this.ordinal - int]
}