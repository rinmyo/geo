package enums.contexts

enum class SettingZoneContext {
    SETTING_ZONE_NAME,
    CONFIRM_ZONE_NAME,
    SETTING_ZONE_TYPE,
    CONFIRM_ZONE_TYPE,
    SETTING_ZONE_DATA,
    CONFIRM_ZONE_DATA,
    SETTING_HEIGHT,
    CONFIRM_HEIGHT,
    SETTING_ZONE_DESCRIPTION,
    CONFIRM_ZONE_DESCRIPTION;

    operator fun inc() = values()[this.ordinal + 1]

    operator fun dec() = values()[this.ordinal - 1]
}