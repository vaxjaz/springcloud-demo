GroovyRoute getRoutes() {
    new GroovyRoute()

}
//通过在指定的路径下，读取groovy脚本转换为动态路由的ZuulProperties属性。
class GroovyRoute {
    // 自定义路由属性id
    def id;
    def path;
    def serviceId;
    def url;
    def stripPrefix = true;
    def retryable;
    def customSensitiveHeaders = false;
}