package com.artclod.h2

import java.lang.Long
import java.sql.Date
import java.sql.Timestamp
import java.text.ParseException
import java.text.SimpleDateFormat
import scala.util.matching.Regex
import java.util.TimeZone
import java.text.NumberFormat
import java.util.Locale
import java.text.DecimalFormat
import java.math.BigDecimal
import java.text.ParsePosition

trait ColumnType[T] {
	val scalaType: Class[T]
	def scalaTypeName = scalaType.getSimpleName.capitalize

	val sqlTypeName: String
	def sqlTypeNameExtra(size: Int): String = ""
	def isValidSQL(s: String): Boolean

	def asScalaCode = getClass.getSimpleName.replace("$", "")

	override def toString() = "SQL[" + sqlTypeName + "]"
}

object ColumnBoolean extends ColumnType[Boolean] {
	val scalaType = classOf[Boolean]

	val sqlTypeName = "boolean"

	private val booleanStrings = Set("True", "TRUE", "T", "true", "False", "FALSE", "F", "false")
	def isValidSQL(s: String) = booleanStrings(s)
}

object ColumnInt extends ColumnType[Int] {
	val scalaType = classOf[Int]

	val sqlTypeName = "int"

	private def parser = { val p = NumberFormat.getInstance(Locale.US).asInstanceOf[DecimalFormat]; p.setParseBigDecimal(true); p }
	def isValidSQL(s: String) = {
		try {
			val pos = new ParsePosition(0)
			parser.parse(s, pos).asInstanceOf[BigDecimal].intValueExact
			s.length() == pos.getIndex()
		} catch {
			case _: ParseException => false
			case _: ArithmeticException => false
			case _: NullPointerException => false
		}
	}
}

object ColumnLong extends ColumnType[Long] {
	val scalaType = classOf[Long]

	val sqlTypeName = "bigint"

	private def parser = { val p = NumberFormat.getInstance(Locale.US).asInstanceOf[DecimalFormat]; p.setParseBigDecimal(true); p }
	def isValidSQL(s: String) = {
		try {
			val pos = new ParsePosition(0)
			parser.parse(s, pos).asInstanceOf[BigDecimal].longValueExact
			s.length() == pos.getIndex()
		} catch {
			case _: ParseException => false
			case _: ArithmeticException => false
			case _: NullPointerException => false
		}
	}
}

object ColumnDouble extends ColumnType[Double] {
	val scalaType = classOf[Double]

	val sqlTypeName = "double"

	private def parser = { val p = NumberFormat.getInstance(Locale.US).asInstanceOf[DecimalFormat]; p.setParseBigDecimal(true); p }
	def isValidSQL(s: String) = {
		try {
			val pos = new ParsePosition(0)
			val d = parser.parse(s, pos).asInstanceOf[BigDecimal].doubleValue
			!(d == Double.NegativeInfinity || d == Double.PositiveInfinity) && s.length() == pos.getIndex()
		} catch {
			case _: ParseException => false
			case _: ArithmeticException => false
			case _: NullPointerException => false
		}
	}
}

object ColumnDate_yyy_MM_dd extends ColumnType[Date] {
	val scalaType = classOf[Date]

	val sqlTypeName = "date"

	private def dateFormat = new SimpleDateFormat("yyyy-MM-dd")
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