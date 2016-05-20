package com.chatroom.persiststorage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ExcuteQueryObject {
	@SuppressWarnings("unchecked")
	protected Map<Object, Object>[] select(PreparedStatement preparestatement, Object[] destObjParams)throws SQLException {
		if (null == preparestatement) {
			return null;
		}
		this.setPreparement(preparestatement, destObjParams);
		ResultSet results = null;;
		try {
			results = preparestatement.executeQuery();
			List<Map<Object, Object>> allResult = new LinkedList<>();
			while (results.next()) {
				allResult.add(setToMap(results));
			}
			return allResult.toArray((Map<Object, Object>[])new Map[]{});
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally {
			if (null != preparestatement) {
				preparestatement.close();
			}
			if (null != results) {
				results.close();
			}
		}
	}
	protected boolean update(PreparedStatement preparestatement, Object []oldObjParams, Object newObj) throws SQLException {
		if (null == preparestatement) {
			return false;
		}
		// 设置参数
		try {
			List<Object> allParams = new LinkedList<>();
			this.addFieldsParams(allParams, newObj);// 添加参数
			allParams.addAll(allParams);
			this.setPreparement(preparestatement, allParams.toArray());
			// 参数设置完毕
			preparestatement.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			preparestatement.close();
		}
	}
	protected int delete(PreparedStatement preparestatement, Object[] destObjParams) throws SQLException {
		if (null == preparestatement) {
			return -1;
		}
		try {
			this.setPreparement(preparestatement, destObjParams);// 设置参数
			return preparestatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			preparestatement.close();
		}
	}
	protected boolean insert(PreparedStatement preparestatement, Object oringinObj) throws SQLException {
		if (null == preparestatement) {
			return false;
		}
		try {
			//将对象参数获取到
			List<Object> params = new LinkedList<>();
			this.addFieldsParams(params, oringinObj);
			this.setPreparement(preparestatement, params.toArray());// 设置参数
			//执行
			preparestatement.execute();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			preparestatement.close();
		}
	}
	protected Map<Object, Object> setToMap(ResultSet set) {
		try {
			int col = set.getMetaData().getColumnCount();
			Map<Object, Object> dstObj = new HashMap<>();
			for (int i = 0; i < col; i++) {
				dstObj.put(i, set.getObject(i + 1));// 获取值
			}
			return dstObj;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	protected void setPreparement(PreparedStatement statement, Object []params) {
		for (int i = 0;i < params.length; i++) {
			try {
				statement.setObject(i + 1, params[i]);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	protected void addFieldsParams(List<Object>params, Object obj) {
		java.lang.reflect.Field[]allFields = obj.getClass().getDeclaredFields();
		// 获取所有属性
		for (int i = 0; i < allFields.length; i++) {
			String fieldName = allFields[i].getName();
			String getterFunction = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			Method getterMethod = null;
			try {
				getterMethod = obj.getClass().getMethod(getterFunction);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				continue;
			} catch (SecurityException e) {
				e.printStackTrace();
				continue;
			}
			Object value = null;
			try {
				value = getterMethod.invoke(obj, new Object[]{});
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				continue;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				continue;
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				continue;
			}
			// 属性值
			params.add(value);
		}
	}
}
