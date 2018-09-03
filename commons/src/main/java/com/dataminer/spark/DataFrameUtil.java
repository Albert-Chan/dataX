package com.dataminer.spark;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Types;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.spark.annotation.Experimental;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.jdbc.JdbcDialect;
import org.apache.spark.sql.jdbc.JdbcDialects;
import org.apache.spark.sql.jdbc.JdbcType;
import org.apache.spark.sql.types.BooleanType;
import org.apache.spark.sql.types.ByteType;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DecimalType;
import org.apache.spark.sql.types.DoubleType;
import org.apache.spark.sql.types.FloatType;
import org.apache.spark.sql.types.IntegerType;
import org.apache.spark.sql.types.IntegerType$;
import org.apache.spark.sql.types.LongType;
import org.apache.spark.sql.types.MetadataBuilder;
import org.apache.spark.sql.types.ShortType;
import org.apache.spark.sql.types.StringType;

import scala.Option;;

@Experimental
public class DataFrameUtil {

	public static final IntegerType INTEGER = IntegerType$.MODULE$;

	static {
		JdbcDialects.registerDialect(new OracleDialect());
	}

	/**
	 * @param sqlc
	 *            SQLContext
	 * @param tableName
	 *            table to insert table, default mode is APPEND
	 * @param dbProps
	 *            database connection relevant props val dbProps: Properties = new
	 *            Properties dbProps.setProperty("driver",
	 *            "oracle.jdbc.driver.OracleDriver") dbProps.setProperty("url",
	 *            "jdbc:oracle:thin:@127.0.0.1:1521:orcl")
	 *            dbProps.setProperty("user", "xxxx")
	 *            dbProps.setProperty("password", "xxxx")
	 * @return DataFrame contains records from table
	 */
	public static DataFrame readFromTable(SQLContext sqlc, String tableName, Properties dbProps) {
		return sqlc.read().jdbc(dbProps.getProperty("url"), tableName, dbProps);
	}

	/**
	 * this one is to support table with query instead of fetching all like the
	 * previous one
	 *
	 * @param sqlc
	 * @param optionMap
	 *            Map( "driver" -> "oracle.jdbc.driver.OracleDriver" "url" ->
	 *            "jdbc:oracle:thin:@127.0.0.1:1521:orcl", "dbtable" -> table name
	 *            or any sql sentence in format ( "(select cast(grid as int) as
	 *            grid, cast(tazid as int) as tazid, perc from TBL_GRID_TAZ_PERC
	 *            where version = 20161022) grid_temp" ) "user"-> "xxxx" "password"
	 *            -> "xxxx" )
	 * @return DataFrame contains records from table or table with query
	 */
	public static DataFrame fromJDBC(SQLContext sqlc, Map<String, String> optionMap) {
		return sqlc.read().format("jdbc").options(optionMap).load();
	}

	/**
	 * Writes a data frame into a given database table.
	 * 
	 * @param dataFrame
	 *            data frame as the input
	 * @param tableName
	 *            table as the destination, default mode is APPEND
	 * @param dbProps
	 *            database connection relevant props
	 */
	public static void toJDBC(DataFrame dataFrame, String tableName, Properties dbProps) {
		dataFrame.write().mode(SaveMode.Append).jdbc(dbProps.getProperty("url"), tableName, dbProps);
	}

	private static final Pattern ROW_BRACKET_PATTERN = Pattern.compile("\\[(.*)\\]");

	/**
	 * Writes a data frame into HDFS as plain text files.
	 * 
	 * @param dataFrame
	 * @param hdfsPath
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static void toHDFSAsText(DataFrame dataFrame, String hdfsPath) throws IOException, URISyntaxException {
		RDDUtil.toHDFSAsText(dataFrame.javaRDD().map(row -> {
			// remove the '[]' from row.toString().
			// e.g. [1,2,3] ===> 1,2,3
			Matcher matcher = ROW_BRACKET_PATTERN.matcher(row.toString());
			if (matcher.find()) {
				return matcher.group(1);
			} else {
				throw new Exception("JavaRDD<Row> row format error");
			}
		}), hdfsPath);
	}

	/**
	 * Reads from parquet files from the HDFS paths, return a data frame.
	 * 
	 * @param sqlc
	 * @param parquetPath
	 * @return the data frame
	 */
	public static DataFrame fromParquet(SQLContext sqlc, String... parquetPath) {
		return sqlc.read().parquet(parquetPath);
	}

	/**
	 * Restores the data frame to the parquet file.
	 * 
	 * @param dataFrame
	 * @param parquetPath
	 */
	public static void toParquet(DataFrame dataFrame, String parquetPath) {
		dataFrame.write().parquet(parquetPath);
	}

}

// spark 1.6.2 has some oracle type issue, so temporarily "borrow" some piece
// from spark 2.0
class OracleDialect extends JdbcDialect {
	private static final long serialVersionUID = -3939581388693222000L;

	@Override
	public boolean canHandle(String url) {
		return url.startsWith("jdbc:oracle");
	}

	@Override
	public Option<DataType> getCatalystType(int sqlType, String typeName, int size, MetadataBuilder md) {
		if (sqlType == Types.NUMERIC && size == 0) {
			return Option.apply(new DecimalType(DecimalType.MAX_PRECISION(), 10));
		} else {
			return Option.empty();
		}
		/*
		 * if (sqlType == Types.NUMERIC) { long scale = (null != md) ?
		 * md.build().getLong("scale") : 0L;
		 * 
		 * if (size == 0) { return Option.apply(new
		 * DecimalType(DecimalType.MAX_PRECISION(), 10)); } else if (size == 1) { return
		 * Option.apply(DataTypes.BooleanType); } else if (size == 3 || size == 5 ||
		 * size == 10) { return Option.apply(DataTypes.IntegerType); } else if (size ==
		 * 19 && scale == 0L) { return Option.apply(DataTypes.LongType); } else if (size
		 * == 19 && scale == 4L) { return Option.apply(DataTypes.FloatType); } else if
		 * (scale == -127L) { return Option.apply(new
		 * DecimalType(DecimalType.MAX_PRECISION(), 10)); } else { return
		 * Option.empty(); } } else { return Option.empty(); }
		 */
	}

	@Override
	public Option<JdbcType> getJDBCType(DataType dt) {
		if (dt instanceof BooleanType) {
			return Option.apply(new JdbcType("NUMBER(1)", java.sql.Types.BOOLEAN));
		} else if (dt instanceof IntegerType) {
			return Option.apply(new JdbcType("NUMBER(10)", java.sql.Types.INTEGER));
		} else if (dt instanceof LongType) {
			return Option.apply(new JdbcType("NUMBER(19)", java.sql.Types.BIGINT));
		} else if (dt instanceof FloatType) {
			return Option.apply(new JdbcType("NUMBER(19, 4)", java.sql.Types.FLOAT));
		} else if (dt instanceof DoubleType) {
			return Option.apply(new JdbcType("NUMBER(19, 4)", java.sql.Types.DOUBLE));
		} else if (dt instanceof ByteType) {
			return Option.apply(new JdbcType("NUMBER(3)", java.sql.Types.SMALLINT));
		} else if (dt instanceof ShortType) {
			return Option.apply(new JdbcType("NUMBER(5)", java.sql.Types.SMALLINT));
		} else if (dt instanceof StringType) {
			return Option.apply(new JdbcType("VARCHAR2(255)", java.sql.Types.VARCHAR));
		} else {
			return Option.empty();
		}
	}

}
