package dev.glycine.geo.enums

/**
 * 不同的用地類型
 *
 * @param properties exception之外的玩家的權限
 */
enum class CanType(vararg val properties: PropertyType) {
    /**
     * 建築用地
     * 傷害免疫（無例外）， 禁止入內（建築師例外），禁止操作（建築師例外）
     */
    BUILDING(PropertyType.DENY_PLAYER_INJURE, PropertyType.DENY_PLAYER_ENTRY, PropertyType.DENY_BLOCK_OPERATION),

    /**
     * 村莊
     * 免疫傷害（外村人例外） 禁止入內（本村人例外） 禁止操作（非管理員例外）
     *
     */
    VILLAGE(PropertyType.DENY_PLAYER_INJURE, PropertyType.DENY_PLAYER_ENTRY, PropertyType.DENY_BLOCK_OPERATION),

    /**
     * 道路用地
     * 禁止操作(沒有例外)
     */
    ROAD(PropertyType.DENY_BLOCK_OPERATION),

    /**
     * 公路用地
     * 禁止操作（無例外）
     */
    HIGHWAYS(PropertyType.DENY_BLOCK_OPERATION),

}