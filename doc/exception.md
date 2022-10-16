### 添加异常拦截器简要步骤
1、自定义异常处理类MyCustomException
2、自定义统一异常拦截器GraceExceptionHandler
   * 指定要拦截的异常类@ExceptionHandler(MyCustomException.class)
   * 在拦截器处，直接将异常返回 return GraceJSONResult.exception(e.getResponseStatusEnum());
