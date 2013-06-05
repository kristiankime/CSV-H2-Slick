package com.artclod.h2

import java.lang.Long
import java.sql.Date
import java.sql.Timestamp
import java.text.ParseException
import java.text.SimpleDateFormat
import scala.util.matching.Regex
import java.util.TimeZone

trait ColumnType[T] {
	def isValidSQL(s: String): Boolean
	val scalaType: Class[T]
	val sqlTypeName: String
	def sqlTypeNameExtra(length: Int): String = ""
	val sqlTypeInt: Int
	override def toString() = "SQL[" + sqlTypeName + "]"
}

object ColumnBoolean extends ColumnType[Boolean] {
	val scalaType = classOf[Boolean]
	val sqlTypeName = "boolean"
	val sqlTypeInt = java.sql.Types.BOOLEAN

	private val booleanStrings = Set("True", "TRUE", "T", "true", "False", "FALSE", "F", "false")
	def isValidSQL(s: String) = booleanStrings(s)
}

object ColumnByte extends ColumnType[Byte] {
	val scalaType = classOf[Byte]
	val sqlTypeName = "tinyint"
	val sqlTypeInt = java.sql.Types.TINYINT
	
	private val regex = """^([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])$""".r
	def isValidSQL(s: String) = regex.pattern.matcher(s).matches
}

object ColumnInt extends ColumnType[Int] {
	val scalaType = classOf[Int]
	val sqlTypeName = "int"
	val sqlTypeInt = java.sql.Types.INTEGER

	def isValidSQL(s: String) = {
		try {
			Integer.parseInt(s)
			true
		} catch {
			case _: NumberFormatException => false
		}
	}
}

object ColumnLong extends ColumnType[Long] {
	val scalaType = classOf[Long]
	val sqlTypeName = "bigint"
	val sqlTypeInt = java.sql.Types.BIGINT

	def isValidSQL(s: String) = {
		try {
			Long.parseLong(s)
			true
		} catch {
			case _: NumberFormatException => false
		}
	}
}

object ColumnDate_yyy_MM_dd extends ColumnType[Date] {
	val scalaType = classOf[Date]
	val sqlTypeName = "date"
	val sqlTypeInt = java.sql.Types.DATE

	private val dateFormat = new SimpleDateFormat("yyyy-MM-dd")
	
	def isValidSQL(s: String) = {
		try {
			dateFormat.parse(s)
			true
		} catch {
			case _: ParseException => false
		}
	}
}

object ColumnTimestamp extends ColumnType[Timestamp] {
	val scalaType = classOf[Timestamp]
	val sqlTypeName = "timestamp"
	val sqlTypeInt = java.sql.Types.TIMESTAMP

	def isValidSQL(s: String) = {
		try {
			Timestamp.valueOf(s)
			true
		} catch {
			case _: IllegalArgumentException => false
		}
	}
}

object ColumnString extends ColumnType[String] {
	val scalaType = classOf[String]
	val sqlTypeName = "varchar"
	val sqlTypeInt = java.sql.Types.VARCHAR

	override def sqlTypeNameExtra(length: Int): String = "(" + length + ")"
	def isValidSQL(s: String) = { true }
}

//Java/JDBC	Oracle	PostgreSql	DB2	MySql	H2	MS SQL	Derby
//int	 number	 integer	 int	 int	 int	 int	 integer
//long	 number	 bigint	 bigint	 bigint	 bigint	 bigint	 bigint
//float	 float	 real	 real	 float	 real	 real	 real
//double	 real	 double precision	 double	 double	 double	 float	 double
//BigDecimal	 decimal	 numeric	 decimal	 decimal	 decimal	 decimal	 decimal
//String	 varchar2(x)	 varchar(x)	 varchar(x)	 varchar(x)	 varchar(x)	 varchar(x)	 varchar(x)
//Date	 date	 date	 date	 date	 date	 date	 date
//Timestamp	 date	 timestamp	 timestamp	 datetime	 timestamp	 datetime	 timestamp
//byte[]	 blob	 bytea	 blob	 blob	 binary	 varbinary	 blob(1M)
//boolean	 number(1)	 boolean	 char(1)	 boolean	 boolean	 bit	 char(1)
//UUID	 char(36)	 uuid	 char(36)	 char(36)	 uuid	 char(36)	 char(36)