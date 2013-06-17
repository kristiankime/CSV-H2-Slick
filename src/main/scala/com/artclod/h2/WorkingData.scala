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
import scala.slick.jdbc.SetParameter

object WorkingData {
	val workingDataURL = "jdbc:h2:mem:working_data_" + UUID.randomUUID().toString() + ";DB_CLOSE_DELAY=-1"

	implicit val defaultColumnTypes = Vector(ColumnBoolean, ColumnDate_yyy_MM_dd, ColumnTimestamp, ColumnInt, ColumnLong, ColumnDouble, ColumnString)
	implicit val defaultCSVOptions = H2CSVOptions()

	private val inferredColumnData = "inferredColumnData"
	private val firstColumnAsOptionString = GetResult(r => r.nextStringOption)

	def run[T](code: => T) = {
		Database.forURL(workingDataURL, driver = classOf[org.h2.Driver].getCanonicalName().toString) withSession { code }
	}

	def loadCSVColumnsAllString(csvFile: String, tableName: String)(implicit options: H2CSVOptions) = {
		run { Q.updateNA("CREATE TABLE \"" + tName(tableName) + "\" AS " + csvReadCommand(csvFile, options) + ";").execute }
	}
	private def csvReadCommand(csvFile: String, options: H2CSVOptions) = "SELECT * FROM CSVREAD('" + csvFile + "', null" + options.sqlString + ")"
	
	def guessColumnTypes(tableName: String)(implicit columnTypes: Vector[ColumnType[_]]) = {
		if (tableName == null) { throw new IllegalArgumentException("name was null") }
		run {
			val columnsMetaData = MTable.getTables(tName(tableName)).first.getColumns
			val inferedColumnTypes = ArrayBuffer[InferredColumn]()
			for (columnMetaData <- columnsMetaData) {
				val columnTypeInferer = ColumnTypeInferer(columnMetaData.column, columnTypes: _*)
				Q.queryNA("SELECT \"" + columnMetaData.column + "\" FROM \"" + tName(tableName) + "\"")(firstColumnAsOptionString).foreach(columnTypeInferer.sample(_))
				inferedColumnTypes += columnTypeInferer.inferedColumnType
			}

			Vector(inferedColumnTypes: _*)
		}
	}

	// charset=UTF-8 escape=\" fieldDelimiter=\" fieldSeparator=, ' || 'lineComment=# lineSeparator=\n null= rowSeparator=
	def loadCSV(csvFile: String, table: Table[_] { def inferredColumnData: Vector[InferredColumn] }, csvOptions: String = null)(implicit options: H2CSVOptions): String = {
		loadCSV(csvFile, table.tableName, table.inferredColumnData: _*)(options)
	}

	def loadCSV(csvFile: String, tableName: String, columns: InferredColumn*)(implicit options: H2CSVOptions) = {
		run {
			Q.updateNA("CREATE TABLE " + tName(tableName) + "(" + columns.map(_.sqlColumn).mkString(", ") + ") AS " + csvReadCommand(csvFile, options)  + ";").execute
		}
		tableName
	}

	def scalaCodeFor(scalaName: String, tableName: String, columns: InferredColumn*) = {
		val b = new StringBuilder("object " + scalaName + " extends Table[(" + columns.map(cType(_)).mkString(", ") + ")](\"" + tName(tableName) + "\") {\n")
		for (column <- columns) {
			b ++= "\tdef " + cName(column.name) + " = column[" + cType(column) + "](\"" + column.name + "\")\n"
		}

		b ++= "\tdef * = " + columns.map(_.name).mkString(" ~ ") + "\n"

		b ++= "\tdef " + inferredColumnData + " = Vector(" + columns.map(_.asScalaCode).mkString(", ") + ")\n"

		b ++= "}"
		b.toString
	}
	private def cName(name: String) = """[\s]+""".r.replaceAllIn(name, "_")
	private def cType(ict: InferredColumn) = {
		val t = ict.columnType.scalaTypeName
		if (ict.canBeNull) { "Option[" + t + "]" } else { t }
	}
	private def tName(name: String) = name.toUpperCase

	def dropTable(tableName: String) = {
		run { Q.updateNA("DROP TABLE IF EXISTS \"" + tName(tableName) + "\";").execute }
	}

	def scalaCodeFromCSV(scalaName: String, csvFile: String) = {
		val tempCSVTable = "temp_table_" + UUID.randomUUID.toString
		try {
			loadCSVColumnsAllString(csvFile, tempCSVTable)
			val columns = guessColumnTypes(tempCSVTable)
			loadCSV(csvFile, scalaName, columns: _*)
			scalaCodeFor(scalaName, scalaName, columns: _*)
		} finally {
			dropTable(tempCSVTable)
		}
	}

}