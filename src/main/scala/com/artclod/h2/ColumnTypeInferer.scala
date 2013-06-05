package com.artclod.h2

case class ColumnTypeInferer(name: String, types: ColumnType[_]*) {
	val canParse = Array.fill(types.size)(true)
	var canBeNull = false;

	def sample(value: Option[String]) {
		value match {
			case None => canBeNull = true
			case Some(v) => {
				for (i <- 0 until types.size) {
					if (canParse(i) && !types(i).isValidSQL(v.trim)) {
						canParse(i) = false
					}
				}
			}
		}
	}

	private def firstTypeThatCanParse = {
		val anyCanParse = Vector(canParse: _*).foldLeft(false)(_ || _)
		if (!anyCanParse) {
			throw new IllegalStateException("Was unable to find a type for column [" + name + "]")
		}

		var i = 0
		var currentCanParse = canParse(i)
		var currentType = types(i)
		while (!currentCanParse && i < types.size) {
			i += 1
			currentCanParse = canParse(i)
			currentType = types(i)
		}
		currentType
	}

	def inferedColumnType = InferedColumnType(name, canBeNull, firstTypeThatCanParse)
}

case class InferedColumnType(name: String, canBeNull: Boolean, columnType: ColumnType[_]) {
	def sqlColumn = { "" + name + " " + columnType.sqlTypeName + "" + (if (canBeNull) { "" } else { " NOT NULL" }) }
}