package com.searchengine.webportal.dto;

/**
 * 复制Server端的状态码
 */
public class ResultCode {

    public static final int SESSION_ERROR = -2;		//Session过期
    public static final int LOGIN_FAILED = -1;		//用户名或密码错误

    public static final int STATUS_OK = 200; // 通用200响应码
    public static final int NOT_FOUND = 404; // 未找到
    public static final int SERVER_ERROR = 500; // 应用程序错误

    public static final int ERROR = 0;				//业务逻辑请求出错
    public static final int SUCCESS = 1;			//业务逻辑请求成功

    public static final int NEEDUPDATE = 2;			//需要更新
    public static final int DONOTNEEDUPDATE = 3;	//不需要更新


    //UserInfo
    public static final int USER_OK = 200;              // 期望的成功结果
    public static final int USER_FAILURE = 400;         // 失败
    public static final int USER_NOT_EXIST = 404;       // 用户不存在

    //Task
    public static final int TASK_OK = 200;              // 期望的成功结果
    public static final int TASK_FAILURE = 400;         // 失败
    public static final int TASK_NOT_EXIST = 404;       // 任务不存在

    //TeachingPlan
    public static final int TEACHING_PLAN_OK = 200;              // 期望的成功结果
    public static final int TEACHING_PLAN_FAILURE = 400;         // 失败
}
