package top.lanscarlos.ranales.action.target.filter

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.info
import taboolib.library.kether.ArgTypes
import taboolib.library.kether.ParsedAction
import taboolib.library.kether.QuestReader
import taboolib.module.kether.ScriptFrame
import taboolib.platform.type.BukkitPlayer
import java.util.concurrent.CompletableFuture

object FilterEntityType: Filter() {

    override fun parse(reader: QuestReader): Any {
//        val types = reader.next(ArgTypes.listOf(ArgTypes.ACTION)).toMutableList()
//        args["filter-types"] = types
        return reader.next(ArgTypes.listOf(ArgTypes.ACTION)).toMutableList()
    }

    override fun call(frame: ScriptFrame, arg: Any, targets: Collection<Any>): Collection<Any> {
        val actions = (arg as? List<*>)?.map { it }?.toMutableList() ?: error("Illegal Filter Data!")
        val legalTypes = mutableSetOf<String>()
        val illegalTypes = mutableSetOf<String>()
        val future = CompletableFuture<Collection<Any>>()
        fun process() {
            if (actions.isNotEmpty()) {
                val action = actions.removeFirst() as? ParsedAction<*> ?: return
                frame.newFrame(action).run<String>().thenApply { type ->
                    if (type.startsWith("!")) {
                        illegalTypes += type.substring(1).lowercase()
                    }else {
                        legalTypes += type.lowercase()
                    }
                    process()
                }
            }else {
                future.complete(targets.filter {
                    val type = when(it) {
                        is ProxyPlayer -> "ProxyPlayer".lowercase()
                        is Entity -> it.type.name.lowercase()
                        else -> it::class.simpleName?.lowercase() ?: return@filter false
                    }
                    type !in illegalTypes && ( if (legalTypes.isNotEmpty()) type in legalTypes else true )
                })
            }
        }
        process()
        return future.get()
    }
}