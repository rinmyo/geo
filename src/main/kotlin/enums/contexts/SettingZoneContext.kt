package enums.contexts

enum class SettingZoneContext {
    SETTING_ZONE_NAME,
    CONFIRM_ZONE_NAME,
    SETTING_ZONE_TYPE,
    CONFIRM_ZONE_TYPE,
    SETTING_ZONE_DATA,
    CONFIRM_ZONE_DATA,
    SETTING_ZONE_NOTE,
    CONFIRM_ZONE_NOTE,
    SETTING_ZONE_DONE;

    operator fun plus(int: Int) = enumValues<SettingZoneContext>()[this.ordinal + int]

    operator fun minus(int: Int) = enumValues<SettingZoneContext>()[this.ordinal - int]
}