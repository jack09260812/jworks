package com.github.utils.spring;


import com.github.utils.string.StringUtil;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 用户名密码加密
 * 
 * @author jinwei.li@baifendian.com
 * 
 */
public class DefinePropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	private String[] encryptPropNames = { "jdbc.username", "jdbc.password" };

	@Override
	protected String convertProperty(String propertyName, String propertyValue) {
		if (isEncryptProp(propertyName)) {
			String decryptValue = null;
			try {
				decryptValue = StringUtil.decrypt(propertyValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return decryptValue;
		} else {
			return propertyValue;
		}
	}

	/**
	 * 判断是否是加密的属性
	 * 
	 * @param propertyName 属性名称
	 * @return boolean
	 */
	private boolean isEncryptProp(String propertyName) {
		for (String encryptpropertyName : encryptPropNames) {
			if (encryptpropertyName.equals(propertyName))
				return true;
		}
		return false;
	}
}