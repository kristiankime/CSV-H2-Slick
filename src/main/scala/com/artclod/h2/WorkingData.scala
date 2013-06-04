package com.artclod.h2

import java.sql.Connection
import java.sql.DriverManager
import java.util.Properties
import scala.slick.session.Database
import Database.threadLocalSession
import scala.slick.jdbc.{ GetResult, StaticQuery => Q }
import java.util.UUID
import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.meta.DatabaseMeta
import scala.slick.jdbc.meta.MTable
import java.sql.DatabaseMetaData
import scala.slick.jdbc.meta.MColumn
import scala.collection.mutable.ArrayBuffer
import scala.collection.immutable.Vector

object WorkingData {
	val workingDataURL = "jdbc:h2:mem:working_data_" + UUID.randomUUID().toString() + ";DB_CLOSE_DELAY=-1"

	private val firstColumnAsOptionString = GetResult(r => r.nextStringOption)
	private val columnTypes = Vector(ColumnBoolean, ColumnByte, ColumnInt, ColumnLong, ColumnDate_yyy_MM_dd, ColumnTimestamp, ColumnString)

	def run[T](code: => T) = {
		Database.forURL(workingDataURL, driver = "org.h2.Driver") withSession { code }
	}

	def loadCSVColumnsAllString(csvFile: String, tableName: String) = {
		run { Q.updateNA("CREATE TABLE \"" + tableName + "\" AS SELECT * FROM CSVREAD('" + csvFile + "');").execute }
	}

	def guessColumnTypes(tableName: String) = {
		run {
			val columnsMetaData = MTable.getTables(tableName).first.getColumns
			val inferedColumnTypes = ArrayBuffer[InferedColumnType]()
			for (columnMetaData <- columnsMetaData) {
				val columnTypeInferer = ColumnTypeInferer(columnMetaData.column, columnTypes: _*)
				Q.queryNA("SELECT \"" + columnMetaData.column + "\" FROM \"" + tableName + "\"")(firstColumnAsOptionString).foreach(columnTypeInferer.sample(_))
				inferedColumnTypes += columnTypeInferer.inferedColumnType
			}

			Vector(inferedColumnTypes: _*)
		}
	}

}