/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.config.annotation;


import org.apache.dubbo.common.constants.ClusterRules;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于声明 Dubbo 服务的类级注解。
 * 1. 与java config bean一起使用：
 * 建议使用此用法。 它在 bean 方法上比在实现类上更灵活，并且与 Spring 更兼容。
 *    @author jianyang
 * @Configuration
 *    class ProviderConfiguration {
 *        @Bean
 *        @DubboService(group="demo")
 *        public DemoService demoServiceImpl() {
 *            return new DemoServiceImpl();
 *        }
 *    }
 *
 * 2.在服务的实现类上使用：
 *    @DubboService(group="demo")
 *    public class DemoServiceImpl implements DemoService {
 *        ...
 *    }
 *
 * 这种用法导致实现类依赖 Dubbo 模块
 *
 *
 * @since 2.7.7
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
public @interface DubboService {

    /**
     * 接口类，默认值为void.class
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 接口类名，默认值为空字符串
     */
    String interfaceName() default "";

    /**
     * 服务版本，默认值为空字符串
     */
    String version() default "";

    /**
     * 服务组，默认值为空字符串
     */
    String group() default "";

    /**
     * 服务路径，默认值为空字符串
     */
    String path() default "";

    /**
     * 是否导出服务，默认为true
     */
    boolean export() default true;

    /**
     * 服务令牌，默认值为false
     */
    String token() default "";

    /**
     * 服务是否被弃用，默认值为false
     */
    boolean deprecated() default false;

    /**
     * 服务是否动态，默认值为true
     */
    boolean dynamic() default true;

    /**
     * 服务的访问日志，默认值为“”
     */
    String accesslog() default "";

    /**
     * 服务的最大并发执行次数，默认值为 0 - 无限制
     */
    int executes() default -1;

    /**
     * 是否将服务注册到注册中心，默认值为true
     */
    boolean register() default true;

    /**
     * 服务权重值，默认为0
     */
    int weight() default -1;

    /**
     * 服务文档，默认值为“”
     */
    String document() default "";

    /**
     * 服务注册延迟时间，默认为0
     */
    int delay() default -1;

    /**
     * @see DubboService#stub()
     * @deprecated
     */
    String local() default "";

    /**
     * 服务存根名称，如果未设置则使用接口名称+本地
     */
    String stub() default "";

    /**
     * 集群策略，合法值包括：failover、failfast、failsafe、failback、fork 可以使用 {@link org.apache.dubbo.common.constants.ClusterRulesFAIL_FAST}
     */
    String cluster() default ClusterRules.EMPTY;

    /**
     * 代理是如何生成的，合法值包括：jdk、javassist
     */
    String proxy() default "";

    /**
     * 服务提供者可以接受的最大连接数，默认值为 0 - 连接是共享的
     */
    int connections() default -1;

    /**
     * T回调实例限制对等连接
     * <p>
     * see org.apache.dubbo.common.constants.CommonConstants.DEFAULT_CALLBACK_INSTANCES
     */
    int callbacks() default -1;

    /**
     * C连接时的回调方法名称，默认值为空字符串
     */
    String onconnect() default "";

    /**
     * 断开连接时的回调方法名称，默认值为空字符串
     */
    String ondisconnect() default "";

    /**
     * 服务所有者，默认值为空字符串
     */
    String owner() default "";

    /**
     * 服务层，默认值为空字符串
     */
    String layer() default "";

    /**
     * 服务调用重试次数
     *
     * @see org.apache.dubbo.common.constants.CommonConstants#DEFAULT_RETRIES
     */
    int retries() default -1;

    /**
     * Load balance strategy, legal values include: random, roundrobin, leastactive
     *
     * you can use {@link org.apache.dubbo.common.constants.LoadbalanceRules#RANDOM} ……
     */
    String loadbalance() default ClusterRules.EMPTY;

    /**
     * 是否开启异步调用，默认为false
     */
    boolean async() default false;

    /**
     * 允许的最大活动请求数，默认值为 0
     */
    int actives() default -1;

    /**
     * 异步请求是否已经发送，默认值为false
     */
    boolean sent() default false;

    /**
     * 服务模拟名称，如果未设置，则使用接口名称 + Mock
     */
    String mock() default "";

    /**
     * 是否使用JSR303验证，合法值为：true、false
     */
    String validation() default "";

    /**
     * 服务调用超时值，默认为0
     */
    int timeout() default -1;

    /**
     * 指定服务调用的缓存实现，合法值包括：lru、threadlocal、jcache
     */
    String cache() default "";

    /**
     * 服务调用过滤器
     *
     * @see Filter
     */
    String[] filter() default {};

    /**
     * 用于服务导出和取消导出的侦听器
     * @see ExporterListener
     */
    String[] listener() default {};

    /**
     * 自定义参数键值对，例如：{key1, value1, key2, value2}
     */
    String[] parameters() default {};

    /**
     * 应用spring bean名称
     * @deprecated 不要设置它并使用全局应用程序配置
     */
    @Deprecated
    String application() default "";

    /**
     * 模块 spring bean 名称
     */
    String module() default "";

    /**
     * 提供者 spring bean 名称
     */
    String provider() default "";

    /**
     * 协议 spring bean 名称
     */
    String[] protocol() default {};

    /**
     * 监控 spring bean 名称
     */
    String monitor() default "";

    /**
     * 注册表 spring bean 名称
     */
    String[] registry() default {};

    /**
     * 服务标签名称
     */
    String tag() default "";

    /**
     * 方法支持
     *
     * @return
     */
    Method[] methods() default {};

    /**
     * 引用导出服务的范围，如果是本地的，则表示仅在当前 JVM 中搜索。
     * @see org.apache.dubbo.rpc.Constants#SCOPE_LOCAL
     * @see org.apache.dubbo.rpc.Constants#SCOPE_REMOTE
     */
    String scope() default "";

    /**
     * Weather the service is export asynchronously
     */
    boolean exportAsync() default false;
}
