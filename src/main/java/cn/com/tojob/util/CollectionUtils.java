package cn.com.tojob.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionUtils {

	public static boolean isNullOrEmpty(Object obj) {
		if (obj == null)
			return true;
		if (obj instanceof CharSequence)
			return ((CharSequence) obj).length() == 0;

		if (obj instanceof Collection)
			return ((Collection) obj).isEmpty();

		if (obj instanceof Map)
			return ((Map) obj).isEmpty();

		if (obj instanceof Object[]) {
			Object[] object = (Object[]) obj;
			if (object.length == 0) {
				return true;
			}
			boolean empty = true;
			for (int i = 0; i < object.length; i++) {
				if (!isNullOrEmpty(object[i])) {
					empty = false;
					break;
				}
			}
			return empty;
		}
		return false;
	}

	/**
	 * @author liuluamos
	 * @Date 2015-08-21
	 * 
	 *       将list转化为字符串
	 * @param list
	 *            转化的list
	 * @param divide
	 *            分割符
	 * @return
	 */
	public static String listToString(List<String> list, String divide) {
		StringBuffer result = new StringBuffer();
		boolean flag = false;
		for (String string : list) {
			if (flag) {
				result.append(divide);
			} else {
				flag = true;
			}
			result.append(string);
		}

		return result.toString();
	}

	/**
	 * @author liuluamos
	 * @Date 2015-09-22
	 * 
	 *       去除list重复值
	 * @param list
	 * @return
	 */
	public static List removeDuplicateWithOrder(List list) {
		Set set = new HashSet();
		List newList = new ArrayList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (set.add(element))
				newList.add(element);
		}
		return newList;
	}

	/**
	 * @author liuluamos
	 * @Date 2015-11-20
	 * 
	 *       转变为字符串，转化后格式为:'x','x'
	 * @param list
	 * @return
	 */
	public static String covertSqlIn(String para) {
		String[] branchArr = para.split(",");
		String branchNo = "";
		for (int i = 0; i < branchArr.length; i++) {
			if (i == 0) {
				branchNo += "\'" + branchArr[0] + "\'";
			} else {
				branchNo += ",\'" + branchArr[i] + "\'";
			}
		}
		return branchNo;
	}

	public static void main(String[] args) {
	}

	/**
	 * oracle.sql.Clob类型转换成String类型
	 * 
	 * @param clob
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 * @Date 2015年11月29日
	 * @author liuluamos
	 */
	public static String ClobToString(Clob clob) throws SQLException, IOException {

		String reString = "";
		
		// 得到流
		Reader is = clob.getCharacterStream();
		BufferedReader br = new BufferedReader(is);
		String s = br.readLine();
		StringBuffer sb = new StringBuffer();
		while (s != null) {
			
			// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
			sb.append(s);
			s = br.readLine();
		}
		reString = sb.toString();
		return reString;
	}
	
	/**
	 * 判断是否为空或者X或者-
	* @author  liuluamos
	* @date 创建时间：2017年2月24日 下午3:32:07 
	* @version 1.0 
	* @parameter    
	* @return  boolean
	 */
	public static boolean isLegal(Object obj) {
		if (obj == null)
			return true;
		if (obj.equals("X")) {
			return true;
		}
		if (obj.equals("-")) {
			return true;
		}
		if (obj instanceof CharSequence)
			return ((CharSequence) obj).length() == 0;

		if (obj instanceof Collection)
			return ((Collection) obj).isEmpty();

		if (obj instanceof Map)
			return ((Map) obj).isEmpty();

		if (obj instanceof Object[]) {
			Object[] object = (Object[]) obj;
			if (object.length == 0) {
				return true;
			}
			boolean empty = true;
			for (int i = 0; i < object.length; i++) {
				if (!isNullOrEmpty(object[i])) {
					empty = false;
					break;
				}
			}
			return empty;
		}
		return false;
	}
}
