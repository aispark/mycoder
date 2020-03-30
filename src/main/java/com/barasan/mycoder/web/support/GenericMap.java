package com.barasan.mycoder.web.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;

import com.barasan.mycoder.generator.helper.StringUtil;

import org.apache.commons.collections.map.ListOrderedMap;

public class GenericMap extends ListOrderedMap {
	private static final long serialVersionUID = 7357036537071533435L;

	public Object put(Object key, Object value) {
		// clob 타입일 경우 String으로 변환 합니다.
		if (value instanceof Clob) {
			Clob c1 = (Clob) value;
			BufferedReader br = null;
			try {
				br = new BufferedReader(c1.getCharacterStream());
				StringBuffer sb = new StringBuffer();
				char[] buf = new char[1024];
				int readcnt;
				while ((readcnt = br.read(buf, 0, 1024)) != -1) {
					sb.append(buf, 0, readcnt);
				}
				value = sb.toString();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null)
						br.close();
				} catch (IOException e) {
				}
			}
		} else if (value instanceof Boolean) {
			value = Boolean.toString((Boolean) value);
		} else if (value instanceof Integer) {
			value = Integer.toString((Integer) value);
		} else if (value instanceof Long) {
			value = Long.toString((Long) value);
		} else if (value instanceof BigDecimal) {
			value = value.toString();
		}

		// key를 Camel표기법으로 변환합니다.
		return super.put(StringUtil.camelCase((String) key), value);
	}
}