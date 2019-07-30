GroovyRoute getRoutes() {
    new GroovyRoute()

}
//通过在指定的路径下，读取groovy脚本转换为动态路由的ZuulProperties属性。
class GroovyRoute {
    // 自定义路由属性id
    def id = "1";
    def path = "/demo-client/**";
    def serviceId = "demo-client";
    def url;
    def stripPrefix = true;
    def retryable;
    def customSensitiveHeaders = false;
    def sensitiveHeaders = new LinkedHashSet();

}