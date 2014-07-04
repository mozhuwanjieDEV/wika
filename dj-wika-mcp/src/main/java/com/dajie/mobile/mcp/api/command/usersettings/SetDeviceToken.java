/**
 * $Id$
 * Copyright 2009-2013 DAJIE-INC. All rights reserved.
 */

package com.dajie.mobile.mcp.api.command.usersettings;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.dajie.mobile.mcp.api.command.AbstractApiCommand;
import com.dajie.mobile.mcp.api.entity.ApiCommandContext;
import com.dajie.mobile.mcp.api.entity.ApiResult;
import com.dajie.mobile.mcp.api.entity.ApiResultCode;
import com.dajie.mobile.mcp.utils.McpUtils;
import com.dajie.wika.constants.DeviceType;
import com.dajie.wika.service.UserSettingsService;

/**
 * @author <a href="mailto:chengwei.hust@gmail.com">程伟</a>
 * @version Dec 16, 2013 12:07:07 PM
 */

public class SetDeviceToken extends AbstractApiCommand implements
		InitializingBean {

	private static final Logger logger = LoggerFactory
			.getLogger(SetDeviceToken.class);

	private UserSettingsService userSettingsService;

	private final int DEFAULT_DEVICE_TYPE = DeviceType.IOS;

	public void setUserSettingsService(UserSettingsService userSettingsService) {
		this.userSettingsService = userSettingsService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(userSettingsService, "userSettingsService is required");
	}

	@Override
	public ApiResult onExecute(ApiCommandContext context) {
		// 取参数
		int userId = context.getUserId();
		Map<String, String> stringParams = context.getStringParams();
		String deviceToken = stringParams.get("deviceToken");
		
		int deviceType = DEFAULT_DEVICE_TYPE;
		try {
			deviceType = Integer.parseInt(stringParams.get("deviceType"));
		} catch (Exception e) {
			logger.info("no device type input,use default");
		}
		
		Object result = null;
		ApiResult apiResult = null;
		// 执行RPC调用
		try {
			long t = System.currentTimeMillis();
			result = userSettingsService.setDeviceToken(userId, deviceToken, deviceType);
			McpUtils.rpcTimeCost(t, "usersettings.setDeviceToken");
		} catch (Exception e) {
			// 异常记录日志， 返回错误信息
			logger.error("RPC error ", e);
			apiResult = new ApiResult(ApiResultCode.E_SYS_RPC_ERROR);
			return apiResult;
		}

		// 正常返回接口数据
		apiResult = new ApiResult(ApiResultCode.SUCCESS, result);
		return apiResult;
	}

}
