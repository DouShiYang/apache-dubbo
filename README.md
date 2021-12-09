# Apache Dubbo Project

[![Build Status](https://github.com/apache/dubbo/workflows/Build%20and%20Test%20For%20Dubbo%203/badge.svg?branch=3.0)](https://github.com/apache/dubbo/actions/workflows/build-and-test-3.yml?query=branch%3A3.0)
[![Build Status](https://travis-ci.com/apache/dubbo.svg?branch=master)](https://travis-ci.com/apache/dubbo)
[![Codecov](https://codecov.io/gh/apache/dubbo/branch/3.0/graph/badge.svg)](https://codecov.io/gh/apache/dubbo)
![Maven](https://img.shields.io/maven-central/v/org.apache.dubbo/dubbo.svg)
![License](https://img.shields.io/github/license/alibaba/dubbo.svg)
[![Average time to resolve an issue](http://isitmaintained.com/badge/resolution/apache/dubbo.svg)](http://isitmaintained.com/project/apache/dubbo "Average time to resolve an issue")
[![Percentage of issues still open](http://isitmaintained.com/badge/open/apache/dubbo.svg)](http://isitmaintained.com/project/apache/dubbo "Percentage of issues still open")
[![Tweet](https://img.shields.io/twitter/url/http/shields.io.svg?style=social)](https://twitter.com/intent/tweet?text=Apache%20Dubbo%20is%20a%20high-performance%2C%20java%20based%2C%20open%20source%20RPC%20framework.&url=http://dubbo.apache.org/&via=ApacheDubbo&hashtags=rpc,java,dubbo,micro-service)
[![Twitter Follow](https://img.shields.io/twitter/follow/ApacheDubbo.svg?label=Follow&style=social&logoWidth=0)](https://twitter.com/intent/follow?screen_name=ApacheDubbo)
[![Gitter](https://badges.gitter.im/alibaba/dubbo.svg)](https://gitter.im/alibaba/dubbo?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

Apache Dubbo 是一个高性能的、基于 Java 的开源 RPC 框架。请访问 [官方网站](http:dubbo.apache.org) 获取快速入门指南和文档，以及 [wiki](https:github.comapachedubbowiki) 获取新闻、常见问题解答和发行说明。

我们正在收集 Dubbo 用户信息，以帮助我们进一步改进 Dubbo。请通过在 [issue1012: Wanted: who's using dubbo](https:github.comapachedubboissues1012) 上提供您的使用信息来支持我们，谢谢:)
## 结构

![Architecture](https://dubbo.apache.org/imgs/architecture.png)

## 功能特征

* 透明式的RPC调用
* 智能负载均衡
* 服务的自动注册与发现
* 高扩展性
* 运行时流量路由
* 可视化服务治理

## 快速开始

The following code snippet comes from [Dubbo Samples](https://github.com/apache/dubbo-samples/tree/master/dubbo-samples-api). You may clone the sample project and step into the `dubbo-samples-api` subdirectory before proceeding.

```bash
# git clone https://github.com/apache/dubbo-samples.git
# cd dubbo-samples/dubbo-samples-api
```

There's a [README](https://github.com/apache/dubbo-samples/tree/master/dubbo-samples-api/README.md) file under `dubbo-samples-api` directory. We recommend referencing the samples in that directory by following the below-mentioned instructions: 

### Maven dependency

```xml
<properties>
    <dubbo.version>3.0.4</dubbo.version>
</properties>

<dependencies>
    <dependency>
        <groupId>org.apache.dubbo</groupId>
        <artifactId>dubbo</artifactId>
        <version>${dubbo.version}</version>
    </dependency>
    <dependency>
        <groupId>org.apache.dubbo</groupId>
        <artifactId>dubbo-dependencies-zookeeper</artifactId>
        <version>${dubbo.version}</version>
        <type>pom</type>
    </dependency>
</dependencies>
```

### 定义服务接口

```java
package org.apache.dubbo.samples.api;

public interface GreetingsService {
    String sayHi(String name);
}
```

*See [api/GreetingsService.java](https://github.com/apache/dubbo-samples/blob/master/dubbo-samples-api/src/main/java/org/apache/dubbo/samples/api/GreetingsService.java) on GitHub.*

### 为提供者实现服务接口

```java
package org.apache.dubbo.samples.provider;

import org.apache.dubbo.samples.api.GreetingsService;

public class GreetingsServiceImpl implements GreetingsService {
    @Override
    public String sayHi(String name) {
        return "hi, " + name;
    }
}
```

*See [provider/GreetingsServiceImpl.java](https://github.com/apache/dubbo-samples/blob/master/dubbo-samples-api/src/main/java/org/apache/dubbo/samples/provider/GreetingsServiceImpl.java) on GitHub.*

### Start service provider

```java
package org.apache.dubbo.samples.provider;


import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.samples.api.GreetingsService;

import java.util.concurrent.CountDownLatch;

public class Application {
    private static String zookeeperHost = System.getProperty("zookeeper.address", "127.0.0.1");

    public static void main(String[] args) throws Exception {
        ServiceConfig<GreetingsService> service = new ServiceConfig<>();
        service.setApplication(new ApplicationConfig("first-dubbo-provider"));
        service.setRegistry(new RegistryConfig("zookeeper://" + zookeeperHost + ":2181"));
        service.setInterface(GreetingsService.class);
        service.setRef(new GreetingsServiceImpl());
        service.export();

        System.out.println("dubbo service started");
        new CountDownLatch(1).await();
    }
}
```

*See [provider/Application.java](https://github.com/apache/dubbo-samples/blob/master/dubbo-samples-api/src/main/java/org/apache/dubbo/samples/provider/Application.java) on GitHub.*

### 编译运行提供者

```bash
# mvn clean package
# mvn -Djava.net.preferIPv4Stack=true -Dexec.mainClass=org.apache.dubbo.samples.provider.Application exec:java
```

### 在消费者中调用远程服务

```java
package org.apache.dubbo.samples.client;


import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.samples.api.GreetingsService;

public class Application {
    private static String zookeeperHost = System.getProperty("zookeeper.address", "127.0.0.1");

    public static void main(String[] args) {
        ReferenceConfig<GreetingsService> reference = new ReferenceConfig<>();
        reference.setApplication(new ApplicationConfig("first-dubbo-consumer"));
        reference.setRegistry(new RegistryConfig("zookeeper://" + zookeeperHost + ":2181"));
        reference.setInterface(GreetingsService.class);
        GreetingsService service = reference.get();
        String message = service.sayHi("dubbo");
        System.out.println(message);
    }
}
```
*See [consumer/Application.java](https://github.com/apache/dubbo-samples/blob/master/dubbo-samples-api/src/main/java/org/apache/dubbo/samples/client/Application.java) on GitHub.*

### 编译运行消费者

```bash
# mvn clean package
# mvn -Djava.net.preferIPv4Stack=true -Dexec.mainClass=org.apache.dubbo.samples.client.Application exec:java
```

The consumer will print out `hi, dubbo` on the screen.


### 下一步

* [你的第一个dubbo应用](https://dubbo.apache.org/blog/2018/08/07/dubbo-101/) - A 101 tutorial to reveal more details, with the same code above.
* [用户手册](https://dubbo.apache.org/docs/v2.7/user/preface/background/) - How to use Dubbo and all its features.
* [dubbo开发者手册](https://dubbo.apache.org/docs/v2.7/dev/build/) - How to involve in Dubbo development.
* [dubbo管理员手册](https://dubbo.apache.org/docs/v2.7/admin/install/provider-demo/) - How to admin and manage Dubbo services.

## 构建

如果您想尝试尖端功能，可以使用以下命令进行构建。 （构建主分支需要Java 1.8）
```
  mvn clean install
```

## Contact

* Mailing list:
  * dev list: for dev/user discussion. [subscribe](mailto:dev-subscribe@dubbo.apache.org), [unsubscribe](mailto:dev-unsubscribe@dubbo.apache.org), [archive](https://lists.apache.org/list.html?dev@dubbo.apache.org),  [guide](https://github.com/apache/dubbo/wiki/Mailing-list-subscription-guide)

* Bugs: [Issues](https://github.com/apache/dubbo/issues/new?template=dubbo-issue-report-template.md)
* Gitter: [Gitter channel](https://gitter.im/alibaba/dubbo)
* Twitter: [@ApacheDubbo](https://twitter.com/ApacheDubbo)

## 贡献者

See [CONTRIBUTING](https://github.com/apache/dubbo/blob/master/CONTRIBUTING.md) for details on submitting patches and the contribution workflow.

### 如何参与贡献?

* Take a look at issues with tags marked [`Good first issue`](https://github.com/apache/dubbo/issues?q=is%3Aopen+is%3Aissue+label%3A%22good+first+issue%22) or [`Help wanted`](https://github.com/apache/dubbo/issues?q=is%3Aopen+is%3Aissue+label%3A%22help+wanted%22).
* Join the discussion on the mailing list, subscription [guide](https://github.com/apache/dubbo/wiki/Mailing-list-subscription-guide).
* Answer questions on [issues](https://github.com/apache/dubbo/issues).
* Fix bugs reported on [issues](https://github.com/apache/dubbo/issues), and send us a pull request.
* Review the existing [pull request](https://github.com/apache/dubbo/pulls).
* Improve the [website](https://github.com/apache/dubbo-website), typically we need
  * blog post
  * translation on documentation
  * use cases around the integration of Dubbo in enterprise systems.
* Improve the [dubbo-admin/dubbo-monitor](https://github.com/apache/dubbo-admin).
* Contribute to the projects listed in [ecosystem](https://github.com/dubbo).
* Other forms of contribution not explicitly enumerated above.
* If you would like to contribute, please send an email to dev@dubbo.apache.org to let us know!

## 报告错误

Please follow the [template](https://github.com/apache/dubbo/issues/new?template=dubbo-issue-report-template.md) for reporting any issues.

## 报告安全漏洞

Please report security vulnerabilities to [us](mailto:security@dubbo.apache.org) privately.

## Dubbo生态

* [Dubbo生态入口](https://github.com/apache?utf8=%E2%9C%93&q=dubbo&type=&language=) - A GitHub group `dubbo` to gather all Dubbo relevant projects not appropriate in [apache](https://github.com/apache) group yet
* [Dubbo网站入口](https://github.com/apache/dubbo-website) - Apache Dubbo official website
* [Dubbo样品](https://github.com/apache/dubbo-samples) - samples for Apache Dubbo
* [Dubbo Spring Boot](https://github.com/apache/dubbo-spring-boot-project) - Spring Boot Project for Dubbo
* [Dubbo Admin](https://github.com/apache/dubbo-admin) - The reference implementation for Dubbo admin
* [Dubbo Awesome](https://github.com/apache/dubbo-awesome) - Dubbo's slides and video links in Meetup

#### 语言

* [Go](https://github.com/dubbo/dubbo-go) (recommended)
* [Node.js](https://github.com/apache/dubbo-js)
* [Python](https://github.com/dubbo/py-client-for-apache-dubbo)
* [PHP](https://github.com/apache/dubbo-php-framework)
* [Erlang](https://github.com/apache/dubbo-erlang)

## License

Apache Dubbo software is licenced under the Apache License Version 2.0. See the [LICENSE](https://github.com/apache/dubbo/blob/master/LICENSE) file for details.
