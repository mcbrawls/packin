package net.mcbrawls.packin.lang

/**
 * A utility class for all of Minecraft's languages.
 */
object LanguageList {
    /**
     * The store of languages.
     */
    private val languages: List<String> = listOf(
        "af_za",
        "ar_sa",
        "ast_es",
        "az_az",
        "ba_ru",
        "bar",
        "be_by",
        "bg_bg",
        "br_fr",
        "brb",
        "bs_ba",
        "ca_es",
        "cs_cz",
        "cy_gb",
        "da_dk",
        "de_at",
        "de_ch",
        "de_de",
        "el_gr",
        "en_au",
        "en_ca",
        "en_gb",
        "en_nz",
        "en_pt",
        "en_ud",
        "en_us",
        "enp",
        "enws",
        "eo_uy",
        "es_ar",
        "es_cl",
        "es_ec",
        "es_es",
        "es_mx",
        "es_uy",
        "es_ve",
        "esan",
        "et_ee",
        "eu_es",
        "fa_ir",
        "fi_fi",
        "fil_ph",
        "fo_fo",
        "fr_ca",
        "fr_fr",
        "fra_de",
        "fur_it",
        "fy_nl",
        "ga_ie",
        "gd_gb",
        "gl_es",
        "haw_us",
        "he_il",
        "hi_in",
        "hr_hr",
        "hu_hu",
        "hy_am",
        "id_id",
        "ig_ng",
        "io_en",
        "is_is",
        "isv",
        "it_it",
        "ja_jp",
        "jbo_en",
        "ka_ge",
        "kk_kz",
        "kn_in",
        "ko_kr",
        "ksh",
        "kw_gb",
        "la_la",
        "lb_lu",
        "li_li",
        "lmo",
        "lol_us",
        "lt_lt",
        "lv_lv",
        "lzh",
        "mk_mk",
        "mn_mn",
        "ms_my",
        "mt_mt",
        "nah",
        "nds_de",
        "nl_be",
        "nl_nl",
        "nn_no",
        "no_no",
        "oc_fr",
        "ovd",
        "pl_pl",
        "pt_br",
        "pt_pt",
        "qya_aa",
        "ro_ro",
        "rpr",
        "ru_ru",
        "ry_ua",
        "se_no",
        "sk_sk",
        "sl_si",
        "so_so",
        "sq_al",
        "sr_sp",
        "sv_se",
        "sxu",
        "szl",
        "ta_in",
        "th_th",
        "tl_ph",
        "tlh_aa",
        "tok",
        "tr_tr",
        "tt_ru",
        "uk_ua",
        "val_es",
        "vec_it",
        "vi_vn",
        "yi_de",
        "yo_ng",
        "zh_cn",
        "zh_hk",
        "zh_tw",
        "zlm_arab",
    )

    val containers = listOf(
        "barrel",
        "blast_furnace",
        "brewing",
        "cartography_table",
        "chest",
        "chestDouble",
        "crafting",
        "dispenser",
        "dropper",
        "enderchest",
        "furnace",
        "grindstone_title",
        "hopper",
        "inventory",
        "loom",
        "repair",
        "shulkerBox",
        "smoker",
        "stonecutter",
        "merchant.trades",
    ).map { "container.$it" }

    fun forEachLanguage(action: (String) -> Unit) {
        languages.forEach(action)
    }

    fun forEachContainer(action: (String) -> Unit) {
        containers.forEach(action)
    }
}
