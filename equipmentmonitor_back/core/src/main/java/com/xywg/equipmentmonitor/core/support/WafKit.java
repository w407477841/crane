/**
 * Copyright (c) 2011-2014, hubin (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xywg.equipmentmonitor.core.support;

import java.util.regex.Pattern;

/**
 * Web防火墙工具类
 * <p>
 * @author   hubin
 * @date	 2014-5-8
 */
public class WafKit {

	/**
	 * @description 过滤XSS脚本内容
	 * @param value
	 * 				待处理内容
	 * @return
	 */
	public static String stripXSS(String value) {
		String rlt = null;

		if (null != value) {
			// NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
			// avoid encoded attacks.
			// value = ESAPI.encoder().canonicalize(value);

			// Avoid null characters
			rlt = value.replaceAll("", "");

			// Avoid anything between script tags
			String  reg = "<script>(.*?)</script>";
			Pattern scriptPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
			rlt = scriptPattern.matcher(rlt).replaceAll("");

			// Avoid anything in a src='...' type of expression
			/*scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE | Pattern.DOTALL);
			rlt = scriptPattern.matcher(rlt).replaceAll("");

			scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE | Pattern.DOTALL);
			rlt = scriptPattern.matcher(rlt).replaceAll("");*/

			// Remove any lonesome </script> tag
			  reg = "</script>";
			scriptPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
			rlt = scriptPattern.matcher(rlt).replaceAll("");

			// Remove any lonesome <script ...> tag
				reg = "<script(.*?)>";
			scriptPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE | Pattern.DOTALL);
			rlt = scriptPattern.matcher(rlt).replaceAll("");

			// Avoid eval(...) expressions
			reg = "eval\\((.*?)\\)";
			scriptPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE | Pattern.DOTALL);
			rlt = scriptPattern.matcher(rlt).replaceAll("");

			// Avoid expression(...) expressions
			reg = "expression\\((.*?)\\)";
			scriptPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE | Pattern.DOTALL);
			rlt = scriptPattern.matcher(rlt).replaceAll("");

			// Avoid javascript:... expressions
			reg= "javascript:";
			scriptPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
			rlt = scriptPattern.matcher(rlt).replaceAll("");

			// Avoid vbscript:... expressions
			reg = "vbscript:";
			scriptPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
			rlt = scriptPattern.matcher(rlt).replaceAll("");

			// Avoid onload= expressions
			reg  = "\"onload(.*?)=\"";
			scriptPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE | Pattern.DOTALL);
			rlt = scriptPattern.matcher(rlt).replaceAll("");
		}
		
		return rlt;
	}

	/**
	 * @Description 过滤SQL注入内容
	 * @param value
	 * 				待处理内容
	 * @return
	 */
	public static String stripSqlInjection(String value) {
		return (null == value) ? null : value.replaceAll("('.+--)|(--)|(%7C)", ""); //value.replaceAll("('.+--)|(--)|(\\|)|(%7C)", "");
	}

	/**
	 * @Description 过滤SQL/XSS注入内容
	 * @param value
	 * 				待处理内容
	 * @return
	 */
	public static String stripSqlXSS(String value) {
		return stripXSS(stripSqlInjection(value));
	}

}
