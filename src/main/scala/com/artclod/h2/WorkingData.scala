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
	private val columnTypes = Vector(ColumnBoolean, ColumnInt, ColumnLong, ColumnDate_yyy_MM_dd, ColumnTimestamp, ColumnString)

	def run[T](code: => T) = {
		Database.forURL(workingDataURL, driver = "org.h2.Driver") withSession { code }
	}

	def loadCSVColumnsAllString(csvFile: String, tableName: String) = {
		run {
			Q.updateNA("CREATE TABLE \"" + tableName + "\" AS SELECT * FROM CSVREAD('" + csvFile + "');").execute
		}
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

	def loadCSV(csvFile: String, tableName: String, columns: InferedColumnType*) = {
		run {
			Q.updateNA("CREATE TABLE " + tableName + "(" + columns.map(_.sqlColumn).mkString(", ") + ") AS SELECT * FROM CSVREAD('" + csvFile + "');").execute
		}
		tableName
	}

	def scalaCodeFor(scalaName: String, tableName: String, columns: InferedColumnType*) = {
		val b = new StringBuilder("object " + scalaName + " extends Table[(" + columns.map(cType(_)).mkString(", ") + ")](\"" + tableName + "\") {\n")
		for (column <- columns) {
			b ++= "\tdef " + cName(column.name) + " = column[" + cType(column) + "](\"" + column.name + "\")\n"
		}

		b ++= "\tdef * = " + columns.map(_.name).mkString(" ~ ") + "\n"

		b ++= "}"
		b.toString
	}
	private def cName(name: String) = """[\s]+""".r.replaceAllIn(name, "_")
	private def cType(ict: InferedColumnType) = {
		val t = ict.columnType.scalaTypeName
		if (ict.canBeNull) { "Option[" + t + "]" } else { t }
	}

}