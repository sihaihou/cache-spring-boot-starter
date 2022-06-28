# 有什么作用?
  可以非常友好的支持cache.
  
# 安装
  下载或编译
  可选 1 - 从这里下载预构建包：https://github.com/sihaihou/cache-spring-boot-starter/releases
  
# 快速开始
  开始您的第一个项目非常容易。
  
### 第一步: 引入依赖

<pre>
&#60;dependency&#62;
   &#60;groupId&#62;com.housihai&#60;/groupId&#62;
   &#60;artifactId&#62;cache-spring-boot-starter&#60;/artifactId&#62;
   &#60;version&#62;0.0.1.RELEASE&#60;/version&#62;
&#60;/dependency&#62;
</pre>

### 第二步: 在启动类加上@EnableReycoCache注解开启cache
<pre>
@EnableBinlog
@SpringBootApplication
public class TestApplication {
	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}
}
</pre>

### 第三步: 在service的方法上加上@ReycoCacheable注解并进行简单的配置
<pre>
@Service("test1ServiceImpl")
public class Test1ServiceImpl implements TestService{
    @Override
    @ReycoCacheable(cacheName="user",key="#user.username",expireTime=10)
    public String test(User user) {
        System.out.println(Thread.currentThread().getName()+",Test1ServiceImpl:"+user);
        return user.getUsername();
    }
}
</pre>

# 文档
所有最新和长期的通知也可以从Github 通知问题这里找到。

# 贡献
欢迎贡献者加入 cache-spring-boot-starter 项目。请联系18307200213@163.com 以了解如何为此项目做出贡献。
