package top.lanscarlos.vulpecular.utils

import taboolib.library.kether.QuestContext

/**
 * Vulpecular
 * top.lanscarlos.vulpecular.utils
 *
 * @author Lanscarlos
 * @since 2022-02-23 17:35
 */

fun QuestContext.Frame.variable(key: String): Any? {
    return variables().get<Any?>(key).let { if (it.isPresent) it.get() else null }
}

fun QuestContext.Frame.variable(key: String, value: Any) {
    variables()[key] = value
}

fun QuestContext.Frame.iterator(): Any? {
    return variable("@Iterator")
}

fun QuestContext.Frame.iterator(iterator: Any) {
    variable("@Iterator", iterator)
}

fun QuestContext.Frame.run(): Any? {
    return variable("@Iterator")
}