package com.cats.server.db

import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlPreparedStatement
import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import com.cats.HockeyPlayerQueries
import com.cats.Transactions
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.engine.*
import java.lang.Exception

public fun SqlDriver.execStatement(statement: String, vararg primitiveParams: Any?) {
    this.execute(null, statement, primitiveParams.size, primitiveParams.createSqlParamBinder())
}

public fun <R> SqlDriver.execQuery(
    query: String,
    mapper: (SqlCursor) -> QueryResult<R>,
    vararg primitiveParams: Any?
): QueryResult<R> {
    return this.executeQuery(null, query, mapper, primitiveParams.size, primitiveParams.createSqlParamBinder())
}

private fun Array<out Any?>.createSqlParamBinder(): (SqlPreparedStatement.() -> Unit) = {
    this@createSqlParamBinder.forEachIndexed { index, param ->
        when (param) {
            null -> bindString(index, param) // NB: Under the hood, this will call `bindNull` which is type-agnostic
//            is UIDStringConvertible -> bindString(index, param.uidString)
            is String -> bindString(index, param)
            is Char -> bindString(index, param.toString())
            is Byte -> bindLong(index, param.toLong())
            is Short -> bindLong(index, param.toLong())
            is Int -> bindLong(index, param.toLong())
            is Long -> bindLong(index, param)
            is Float -> bindDouble(index, param.toDouble())
            is Double -> bindDouble(index, param)
            is ByteArray -> bindBytes(index, param)
            else -> throw IllegalArgumentException("Invalid parameter type: $param for $index")
        }
    }
}

object TransactionsDB {

    private val config = HikariConfig().apply {
        jdbcUrl = "jdbc:mysql://localhost:3306/Transactions"
        username = "transacter"
        password = "transacter"
        driverClassName = "com.mysql.cj.jdbc.Driver"
        addDataSourceProperty("cachePrepStmts", "true")
        addDataSourceProperty("prepStmtCacheSize", "250")
        addDataSourceProperty("prepStmtCacheSqlLimit", "2048")

    }
    private val ds = HikariDataSource(config)
    private val driver: SqlDriver = ds.asJdbcDriver()

    val asds = object : TransacterImpl(driver) {}.also { transacter ->
        transacter.transaction {
            val version = tryOrNull {
                driver.execQuery("SELECT version FROM MigrationVersion LIMIT 1;", {
                    it.next()
                    QueryResult.Value(it.getLong(0))
                }).value
            } ?: 0L
            val schema = Transactions.Schema
            if (version == 0L) {
                schema.create(driver)
            } else {
                schema.migrate(driver, version, schema.version)
            }
            driver.execStatement("DELETE FROM MigrationVersion;")
            driver.execStatement("INSERT INTO MigrationVersion VALUES(${schema.version});")
        }

    }

    private fun <T> tryOrNull(expression: () -> T): T? {
        return try {
            expression()
        } catch (ex: Exception) {
            println(ex.message.orEmpty())
            null
        }
    }

    private val database = Transactions(driver)

    private val playerQueries: HockeyPlayerQueries = database.hockeyPlayerQueries

    val playerRepository: HockeyPlayerRepository = HockeyPlayerRepositoryImpl(playerQueries)
}

