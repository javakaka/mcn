package com.ezcloud.framework.util;

import java.security.Identity;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.StringUtil;

public class ServletUtil {
	public static String _SOFT_TAG = "_JUTS_";
	public static String _ERROR_TAG = "_ERROR_";
	public static String _DEFAULT_TABLE_STYLE = "6699cc";
	public static String _WEB_PATH = "/juts";
	public static String _WEB_URL = "http://www";
	public static String _WEB_PHYSICAL_PATH = null;

	public static String getDefaultPage(
			HttpServletRequest paramHttpServletRequest) {
		String str = "";
		try {
			str = ConfigUtil.getParam("DEFAULT_MAIN_PAGE");
		} catch (Exception localException) {
			localException.printStackTrace();
		}

		return str;
	}

	/**
	 * 获取当前登录人员的staff_no
	 * @param request
	 * @return
	 */
	public static String getLoginStaffNo(
			HttpServletRequest request) {
		String staff_no = null;
		HttpSession httpSession = request.getSession(true);
		if (httpSession.getAttribute("staff") != null) {
			Row staffRow = (Row) httpSession.getAttribute("staff");
			staff_no = staffRow.get("staff_no").toString();
		}
		return staff_no;
	}

	/**
	 * 获取当前登录人员的真实姓名
	 * @param request
	 * @return
	 */
	public static String getLoginStaffName(
			HttpServletRequest request) {
		String staff_real_name = null;
		HttpSession httpSession = request.getSession(true);
		if (httpSession.getAttribute("staff") != null) {
			Row staffRow = (Row) httpSession.getAttribute("staff");
			staff_real_name = staffRow.get("real_name").toString();
		}
		return staff_real_name;
	}

	
	public static String getLoginStaffSiteName(
			HttpServletRequest request) {
		String site_name = "";
		
		return site_name;
	}

	public static String getLoginStaffSiteNo(
			HttpServletRequest request) {
		String site_no = null;
		HttpSession httpSession = request.getSession(true);
		if (httpSession.getAttribute("staff") != null) {
			Row staffRow = (Row) httpSession.getAttribute("staff");
			site_no = staffRow.get("site_no").toString();
		}
		return site_no;
	}

	public static Object getLoginStaff(
			HttpServletRequest request, String paramString) {
		Row staffRow = null;
		HttpSession httpSession = request.getSession(true);
		if (httpSession.getAttribute("staff") != null) {
		    staffRow = (Row) httpSession.getAttribute("staff");
		}
		return staffRow;
	}

	/**
	 * 从request中获取参数
	 * @param httpServletRequest
	 * @param paramName
	 * @param defaultParamValue
	 * @return
	 */
	public static String get(HttpServletRequest httpServletRequest,
			String paramName, String defaultParamValue) {
		return get(httpServletRequest, paramName, defaultParamValue, false);
	}

	public static String get(HttpServletRequest httpServletRequest,
			String paramName, String defaultParamValue, boolean boolIsoToGBK) {
		String paramValue = httpServletRequest.getParameter(paramName);
		if (paramValue == null) {
			paramValue = httpServletRequest.getParameter(paramName.toUpperCase());
			if (paramValue == null)
				paramValue = defaultParamValue;
		}
		if (boolIsoToGBK)
			paramValue = StringUtil.isoToGBK(paramValue, boolIsoToGBK);
		return paramValue;
	}

	public static String get(HttpServletRequest httpServletRequest,
			String paramName) {
		return get(httpServletRequest, paramName, "");
	}

	public static String get(HttpServletRequest httpServletRequest,
			String paramName, boolean paramBoolean) {
		return get(httpServletRequest, paramName, "", paramBoolean);
	}

	/**
	 * 检查request中是否包含指定参数名的参数
	 * @param httpServletRequest
	 * @param paramName
	 * @return
	 */
	public static boolean contain(HttpServletRequest httpServletRequest,
			String paramName) {
		Enumeration enumeration = httpServletRequest.getParameterNames();
		boolean bool = false;
		while (enumeration.hasMoreElements()) {
			if (enumeration.nextElement().toString().toUpperCase().equals(
					paramName.toUpperCase())) {
				bool = true;
			}
		}
		return bool;
	}

	/**
	 * 取DataSet参数值
	 * @param httpServletRequest
	 * @param paramName
	 * @return
	 */
	public static DataSet getResult(HttpServletRequest httpServletRequest,
			String paramName) {
		return (DataSet) httpServletRequest.getAttribute(_SOFT_TAG+ paramName.toUpperCase());
	}

	/**
	 * 移除DataSet参数值
	 * @param httpServletRequest
	 * @param paramName
	 * @return
	 */
	public static void removeResult(HttpServletRequest httpServletRequest,String paramName) {
		httpServletRequest.removeAttribute(_SOFT_TAG+ paramName.toUpperCase());
	}

	/**
	 * 取默认DataSet 参数
	 * @param httpServletRequest
	 * @param paramName
	 * @return
	 */
	public static String getDefaultParam(
			HttpServletRequest httpServletRequest, String paramName) {
		String str = "";
		DataSet localDataSet = getResult(httpServletRequest, "DATA");
		if ((localDataSet != null) && (localDataSet.size() == 1)) {
			Row localRow = (Row) localDataSet.get(0);
			str = localRow.getString(paramName);
		}
		return str;
	}

	/**
	 * 授权
	 * @param httpServletRequest
	 * @param paramString
	 * @return
	 */
	public static boolean permit(HttpServletRequest httpServletRequest,
			String paramName) {
		boolean bool = false;
		HttpSession localHttpSession = httpServletRequest.getSession(true);
		if (localHttpSession.getAttribute("staff") != null) {
			Row staffRow = (Row) localHttpSession.getAttribute("staff");
			if (paramName != null) {
				String[] arrayOfString = StringUtil.toArray(paramName, ",");
				HashMap localHashMap = (HashMap) staffRow.get("staff-sub-fun");
				if (localHashMap != null) {
					for (int i = 0; i < arrayOfString.length; i++) {
						if (localHashMap.containsKey(arrayOfString[i])) {
							bool = true;
							break;
						}
					}
				}
			}
		}
		return bool;
	}
}