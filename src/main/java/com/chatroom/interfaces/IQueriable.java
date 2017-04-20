package com.chatroom.interfaces;

import java.util.Map;

public interface IQueriable {
	public Map<Object, Object>[] select(Object destObj);
	public boolean update(Object oldObj, Object newObj);
	public int delete(Object destObj);
	public boolean insert(Object oringinObj);
}
