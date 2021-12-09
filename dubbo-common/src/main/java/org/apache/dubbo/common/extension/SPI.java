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

package org.apache.dubbo.common.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 扩展接口标记
 *
 * 扩展配置文件的更改 以Protocol为例，其配置文件'META-INF/dubbo/com.xxx.Protocol'由：
 *        com.foo.XxxProtocol
 *        com.foo.YyyProtocol
 *
 * 键值对
 *        xxx=com.foo.XxxProtocol
 *        yyy=com.foo.YyyProtocol
 *
 *  这种变化的原因是：
 * 如果静态字段或扩展实现中的方法引用了第三方库，如果第三方库不存在，则其类将无法初始化。 在这种情况下，dubbo 无法找出扩展的 id，因此如果使用以前的格式，则无法将异常信息与扩展映射。
 *
 * 例如：
 * 无法加载扩展（“mina”）。 当用户配置使用mina时，dubbo会报错无法加载扩展，而不是报告哪个extract扩展执行失败以及extract原因
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPI {

    /**
     * default extension name
     */
    String value() default "";

    /**
     * scope of SPI, default value is application scope.
     */
    ExtensionScope scope() default ExtensionScope.APPLICATION;
}
