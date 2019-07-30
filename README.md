# springcloud-demo
## springcloud 官网简介以及maven版本依赖　　
https://spring.io/projects/spring-cloud#learn
https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-dependencies

## 服务注册发现
### eureka server
1.springcloud-server为例<br></br>
pom.xml中引入<br></br>
spring-cloud-starter-netflix-eureka-server<br></br>
2.启动类上加上@EnableEurekaServer注解,声明为服务注册中心<br></br>
3.注册中心yml配置说明  
&emsp;详见springcloud-server-registered的application.ym<br></br>　
### eureka client
1.其他服务注册到注册中心  
&emsp;1.1 springcloud-client为例  
&emsp;&emsp;引入spring-cloud-starter-netflix-eureka-client,spring-cloud-starter-openfeign依赖.服务启动类上加入@EnableEurekaClient注解声明为eureka服务的客户端.@EnableFeignClients,声明为feign的客户端.后面讲解用处.  
&emsp;1.2 yml配置  
&emsp;&emsp;详见springcloud-client的application.yml配置  　　
&emsp;&emsp;notice:默认注册使用主机名注册　　
![avatar](picture/1564456113(1).png)　　
会取/etc/hosts里面的主机名(windows system32 driver etc hosts文件)　　
### feign client
feign的原理以及熔断使用和异常处理详细内容见demo-client 注释　　
feign默认是集成了熔断和负载的.熔断相关看配置文件,默认负载开启的,只需要２个服务示例即可,都是客户端负载.　　

如果使用restTemplate调用不使用feign需要配置　　　
　
  　@LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
 就可实现负载.　　
 当使用restTemplate调用第三方服务的时候需要另外注入一个Bean,取别名,并且不能添加　@LoadBalanced注解　　
 



