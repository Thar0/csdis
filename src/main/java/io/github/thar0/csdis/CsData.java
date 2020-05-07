package io.github.thar0.csdis;

import static io.github.thar0.csdis.Util.*;

public class CsData {

    public enum OcarinaSongActionIDs {
        /* 0x00 */ OCARINA_ACTION_UNK_0,
        /* 0x01 */ OCARINA_ACTION_FREE_PLAY_OCARINA,
        /* 0x02 */ OCARINA_ACTION_MINUET,
        /* 0x03 */ OCARINA_ACTION_BOLERO,
        /* 0x04 */ OCARINA_ACTION_SERENADE,
        /* 0x05 */ OCARINA_ACTION_REQUIEM,
        /* 0x06 */ OCARINA_ACTION_NOCTURNE,
        /* 0x07 */ OCARINA_ACTION_PRELUDE,
        /* 0x08 */ OCARINA_ACTION_SARIAS_SONG,
        /* 0x09 */ OCARINA_ACTION_EPONAS_SONG,
        /* 0x0A */ OCARINA_ACTION_ZELDAS_LULLABY,
        /* 0x0B */ OCARINA_ACTION_SUNS_SONG,
        /* 0x0C */ OCARINA_ACTION_SONG_OF_TIME,
        /* 0x0D */ OCARINA_ACTION_SONG_OF_STORMS,
        /* 0x0E */ OCARINA_ACTION_OCARINA_ACTION_UNK_E,
        /* 0x0F */ OCARINA_ACTION_MINUET_PLAYBACK,
        /* 0x10 */ OCARINA_ACTION_BOLERO_PLAYBACK,
        /* 0x11 */ OCARINA_ACTION_SERENADE_PLAYBACK,
        /* 0x12 */ OCARINA_ACTION_REQUIEM_PLAYBACK,
        /* 0013 */ OCARINA_ACTION_NOCTURNE_PLAYBACK,
        /* 0x14 */ OCARINA_ACTION_PRELUDE_PLAYBACK,
        /* 0x15 */ OCARINA_ACTION_SARIAS_SONG_PLAYBACK,
        /* 0x16 */ OCARINA_ACTION_EPONAS_SONG_PLAYBACK,
        /* 0x17 */ OCARINA_ACTION_ZELDAS_LULLABY_PLAYBACK,
        /* 0x18 */ OCARINA_ACTION_SUNS_SONG_PLAYBACK,
        /* 0x19 */ OCARINA_ACTION_SONG_OF_TIME_PLAYBACK,
        /* 0x1A */ OCARINA_ACTION_SONG_OF_STORMS_PLAYBACK
    }

    public enum CSTerminatorDestinations {
        /* 0x00 */ INVALID_DESTINATION_0,
        /* 0x01 */ CUTSCENE_MAP_GANON_HORSE,
        /* 0x02 */ CUTSCENE_MAP_THREE_GODESSES_POST_DEKU_TREE,
        /* 0x03 */ GERUDO_VALLEY_DIN,
        /* 0x04 */ DEATH_MOUNTAIN_TRAIL_NAYRU,
        /* 0x05 */ KOKIRI_FOREST_FARORE,
        /* 0x06 */ CUTSCENE_MAP_TRIFORCE_CREATION,
        /* 0x07 */ KOKIRI_FOREST_RECEIVE_KOKIRI_EMERALD,
        /* 0x08 */ TEMPLE_OF_TIME_AFTER_USE_MS,
        /* 0x09 */ GERUDO_VALLEY_DIN_2,
        /* 0x0A */ LINKS_HOUSE_INTRO,
        /* 0x0B */ KOKIRI_FOREST_INTRO,
        /* 0x0C */ DEATH_MOUNTAIN_TRAIL_AFTER_GORON_RUBY,
        /* 0x0D */ ZORAS_FOUNTAIN_AFTER_ZORAS_SAPPHIRE,
        /* 0x0E */ KOKIRI_FOREST_AFTER_KOKIRI_EMERALD,
        /* 0x0F */ TEMPLE_OF_TIME_KOKIRI_EMERALD, //unused
        /* 0x10 */ TEMPLE_OF_TIME_GORON_RUBY, //unused
        /* 0x11 */ TEMPLE_OF_TIME_ZORAS_SAPPHIRE, //unused
        /* 0x12 */ TEMPLE_OF_TIME_AFTER_USE_MS_FIRST,
        /* 0x13 */ DEATH_MOUNTAIN_TRAIL_AFTER_INTRO,
        /* 0x14 */ INVALID_DESTINATION_14,
        /* 0x15 */ LAKE_HYLIA_WATER_RISES,
        /* 0x16 */ DESERT_COLOSSUS_REQUIEM,
        /* 0x17 */ CUTSCENE_MAP_CURSE_YOU,
        /* 0x18 */ JABU_JABU_INTRO,
        /* 0x19 */ CHAMBER_OF_SAGES_LIGHT_MEDALLION,
        /* 0x1A */ TEMPLE_OF_TIME_KOKIRI_EMERALD_2, //duplicate of 0x000F
        /* 0x1B */ TEMPLE_OF_TIME_GORON_RUBY_2, //duplicate of 0x0010
        /* 0x1C */ TEMPLE_OF_TIME_ZORAS_SAPPHIRE_2, //duplicate of 0x0011
        /* 0x1D */ CHAMBER_OF_SAGES_FOREST_MEDALLION,
        /* 0x1E */ CHAMBER_OF_SAGES_FIRE_MEDALLION,
        /* 0x1F */ CHAMBER_OF_SAGES_WATER_MEDALLION,
        /* 0x20 */ HYRULE_FIELD_FLASHBACK, //lacs part 4
        /* 0x21 */ HYRULE_FIELD_AFTER_LAKE_HYLIA_OWL,
        /* 0x22 */ CUTSCENE_MAP_GANON_AFTER_USE_MS,
        /* 0x23 */ HYRULE_FIELD_INTRO_ZELDA_ESCAPE,
        /* 0x24 */ INVALID_DESTINATION_24,
        /* 0x25 */ INVALID_DESTINATION_25,
        /* 0x26 */ CUTSCENE_MAP_SHEIKAH_LEGEND, //lacs part 2
        /* 0x27 */ TEMPLE_OF_TIME_ZELDA_REVEAL, //lacs part 3
        /* 0x28 */ TEMPLE_OF_TIME_GET_LIGHT_ARROWS, //lacs part 5
        /* 0x29 */ LAKE_HYLIA_AFTER_BLUE_WARP,
        /* 0x2A */ KAKARIKO_VILLAGE_DRAIN_WELL,
        /* 0x2B */ WINDMILL_AFTER_DRAIN_WELL,
        /* 0x2C */ TEMPLE_OF_TIME_AFTER_DOOR_OF_TIME_OPENS,
        /* 0x2D */ INVALID_DESTINATION_2D,
        /* 0x2E */ TEMPLE_OF_TIME_AFTER_USE_MS_FIRST_2, // duplicate of 0x0012
        /* 0x2F */ KAKARIKO_VILLAGE_NOCTURNE_PART_2,
        /* 0x30 */ DESERT_COLOSSUS_AFTER_REQUIEM,
        /* 0x31 */ TEMPLE_OF_TIME_AFTER_LIGHT_ARROWS,
        /* 0x32 */ KAKARIKO_VILLAGE_AFTER_NOCTURNE,
        /* 0x33 */ HYRULE_FIELD_IMPA_ESCORT_CS,
        /* 0x34 */ TEMPLE_OF_TIME_SONG_OF_TIME,
        /* 0x35 */ HYRULE_FIELD_AFTER_SONG_OF_TIME,
        /* 0x36 */ GERUDO_VALLEY_CREDITS,
        /* 0x37 */ GERUDO_FORTRESS_CREDITS,
        /* 0x38 */ KAKARIKO_VILLAGE_CREDITS,
        /* 0x39 */ DEATH_MOUNTAIN_TRAIL_CREDITS_1,
        /* 0x3A */ GORON_CITY_CREDITS, // unused?
        /* 0x3B */ LAKE_HYLIA_CREDITS,
        /* 0x3C */ ZORAS_FOUNTAIN_CREDITS, // unused
        /* 0x3D */ ZORAS_DOMAIN_CREDITS,
        /* 0x3E */ KOKIRI_FOREST_CREDITS_1,
        /* 0x3F */ KOKIRI_FOREST_CREDITS_2,
        /* 0x40 */ HYRULE_FIELD_CREDITS,
        /* 0x41 */ LON_LON_RANCH_CREDITS_1,
        /* 0x42 */ KAKARIKO_VILLAGE_AFTER_TRAIL_OWL,
        /* 0x43 */ HTRULE_FIELD_UNUSED_ENTRANCE,
        /* 0x44 */ CUTSCENE_MAP_FIRE,
        /* 0x45 */ KOKIRI_FOREST_POST_FOREST_MEDALLION,
        /* 0x46 */ DEATH_MOUNTAIN_TRAIL_CREDITS_2,
        /* 0x47 */ TEMPLE_OF_TIME_CREDITS,
        /* 0x48 */ ZELDAS_COURTYARD_CREDITS,
        /* 0x49 */ LON_LON_RANCH_CREDITS_1_2, // duplicate of 0x0041
        /* 0x4A */ LON_LON_RANCH_CREDITS_2,
        /* 0x4B */ LON_LON_RANCH_CREDITS_3,
        /* 0x4C */ LON_LON_RANCH_CREDITS_4,
        /* 0x4D */ LON_LON_RANCH_CREDITS_5,
        /* 0x4E */ LON_LON_RANCH_CREDITS_6,
        /* 0x4F */ LON_LON_RANCH_NO_CS_1,
        /* 0x50 */ LON_LON_RANCH_NO_CS_2,
        /* 0x51 */ LON_LON_RANCH_NO_CS_3,
        /* 0x52 */ LON_LON_RANCH_NO_CS_4,
        /* 0x53 */ LON_LON_RANCH_NO_CS_5,
        /* 0x54 */ LON_LON_RANCH_NO_CS_6,
        /* 0x55 */ LON_LON_RANCH_NO_CS_7,
        /* 0x56 */ LON_LON_RANCH_NO_CS_8,
        /* 0x57 */ LON_LON_RANCH_NO_CS_9,
        /* 0x58 */ LON_LON_RANCH_NO_CS_10,
        /* 0x59 */ LON_LON_RANCH_NO_CS_11,
        /* 0x5A */ LON_LON_RANCH_NO_CS_12,
        /* 0x5B */ LON_LON_RANCH_NO_CS_13,
        /* 0x5C */ LON_LON_RANCH_NO_CS_14,
        /* 0x5D */ LON_LON_RANCH_NO_CS_15,
        /* 0x5E */ LON_LON_RANCH_NO_CS_EPONAS_SONG,
        /* 0x5F */ CONDITIONAL_DESTINATION, // TODO more descriptive name?
        /* 0x60 */ DESERT_COLOSSUS_SPIRIT_BLUE_WARP,
        /* 0x61 */ GRAVEYARD_AFTER_SHADOW_BLUE_WARP,
        /* 0x62 */ DEATH_MOUNTAIN_CRATER_AFTER_FIRE_BLUE_WARP,
        /* 0x63 */ SACRED_FOREST_MEADOW_AFTER_FOREST_BLUE_WARP,
        /* 0x64 */ KOKIRI_FOREST_AFTER_FOREST_BLUE_WARP,
        /* 0x65 */ DESERT_COLOSSUS_AFTER_SILVER_GAUNTLETS,
        /* 0x66 */ TEMPLE_OF_TIME_FRONT_OF_PEDESTAL,
        /* 0x67 */ HYRULE_FIELD_TITLE_SCREEN,
        /* 0x68 */ SPIRIT_TEMPLE_BOSS_TITLE_SCREEN,
        /* 0x69 */ GRAVEYARD_SUNS_SONG,
        /* 0x6A */ ROYAL_FAMILYS_TOMB_SUNS_SONG,
        /* 0x6B */ GANONS_CASTLE_AFTER_FOREST_TRIAL,
        /* 0x6C */ GANONS_CASTLE_AFTER_WATER_TRIAL,
        /* 0x6D */ GANONS_CASTLE_AFTER_SHADOW_TRIAL,
        /* 0x6E */ GANONS_CASTLE_AFTER_FIRE_TRIAL,
        /* 0x6F */ GANONS_CASTLE_AFTER_LIGHT_TRIAL,
        /* 0x70 */ GANONS_CASTLE_AFTER_SPIRIT_TRIAL,
        /* 0x71 */ GANONS_CASTLE_DISPEL_BARRIER_IF_CONDITIONS,
        /* 0x72 */ HYRULE_FIELD_INTRO,
        /* 0x73 */ HYRULE_FIELD_AFTER_IMPA_ESCORT,
        /* 0x74 */ DESERT_COLOSSUS_SPIRIT_BLUE_WARP_2,
        /* 0x75 */ HYRULE_FIELD_SKY,
        /* 0x76 */ GANON_BATTLE_TOWER_COLLAPSE,
        /* 0x77 */ ZELDAS_COURTYARD_RECEIVE_LETTER
    }

    public static String getOcarinaSongActionID(short value) {
        String name = enumNameByOrdinal(OcarinaSongActionIDs.class, value);
        return (name!=null) ? name : formatHex(value);
    }

    public static String getCSTerminatorDestination(short value) {
        String name = enumNameByOrdinal(CSTerminatorDestinations.class, value);
        return (name!=null) ? name : formatHex(value);
    }

    public static String commandContinueStop(byte value) {
        return (value == 0) ? "CS_CMD_CONTINUE" : ((value == -1) ? "CS_CMD_STOP" : formatHex(value));
    }
}
