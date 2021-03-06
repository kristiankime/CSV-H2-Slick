package com.artclod.h2

case class ColumnTypeInferer(name: String, types: ColumnType[_]*) {
	val canParse = Array.fill(types.size)(true)
	var canBeNull = false;
	var maxLength = 0;
	
	def sample(value: Option[String]) {
		value match {
			case None => canBeNull = true
			case Some(v) => {
				maxLength = Math.max(maxLength, v.size)
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

	def inferedColumnType = InferredColumn(name, canBeNull, maxLength, firstTypeThatCanParse)
}

case class InferredColumn(name: String, canBeNull: Boolean, maxLength : Int, columnType: ColumnType[_]) {
	
	def sqlColumn = "\"" + name + "\" " + columnType.sqlTypeName + columnType.sqlTypeNameExtra(maxLength)  + (if (canBeNull) { "" } else { " NOT NULL" })
	
	def asScalaCode = InferredColumn.getClass.getSimpleName.replace("$", "") +"(\"" + name + "\", " + canBeNull + ", " + maxLength + ", " + columnType.asScalaCode + ")" 

}