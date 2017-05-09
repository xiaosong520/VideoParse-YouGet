package xyz.yhsj.parse.intfc

import xyz.yhsj.parse.entity.ParseResult

/**
 * 接口解析
 * Created by zengsong on 2017-5-9
 */
interface Parse {
    fun parseResult(sourceUrl: String): ParseResult
}