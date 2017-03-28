package com.twj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

/**
 * Turbine是Hystrix Dashboard的聚合工具,只能监控使用Feign或Ribbon消费服务的app,不能监控纯服务提供方。
 * 		监控的是：服务消费的成功、失败、断路、超时、拒绝
 * 
 * Turbine是聚合服务器发送事件流数据的一个工具，hystrix只能监控单个节点，实际生产中都为集群，因此可以通过
 * Turbine来监控集群下hystrix的metrics情况，通过eureka来发现hystrix服务
 * 
 * 配置application.yml时
 * 	1.需要指定监控的集群列表，如：turbine.aggregator.clusterConfig: RM-C-Cluster，RM-B-Cluster
 * 	2.需要指定监控的服务应用列表，如：turbine.appConfig: Service-Client,Service-Client2
 * 	3.turbine.clusterNameExpression通常设置为metadata['cluster']即可，集群名称映射自turbine.aggregator.clusterConfig，即RM-C-Cluster，RM-B-Cluster
 * 
 * 多个app组成一个集群，设置方法eureka.instance.metadata-map.cluster=RM-C-Cluster
 * 
 * 本例中：
 * 	3个eureka集群	localhost:1001/localhost:1002/localhost:1003
 * 	ConfigServer	localhost:9001
 * 	ServiceA		localhost:3001	(纯服务提供方，不能被Hystrix/Turbine监控)
 * 	ServiceB		localhost:3002	(纯服务提供方，不能被Hystrix/Turbine监控)
 * 	Service-Client(RM-C-Cluster)	localhost:4001	使用Feign消费服务ServiceA和ServiceB(能被Hystrix/Turbine监控)
 * 	Service-Client2(RM-C-Cluster)	localhost:4002	使用Feign消费服务ServiceA和ServiceB(能被Hystrix/Turbine监控)
 * 	Cluster-Monitor	localhost:7001	监控集群RM-C-Cluster,RM-B-Cluster，监控应用Service-Client,Service-Client2
 * 
 * 对集群RM-C-Cluster的服务监控URL为
 * http://localhost:4001/hystrix/monitor?stream=http%3A%2F%2Flocalhost%3A7001%2Fturbine.stream%3Fcluster%3DRM-C-Cluster
 * 
 * 
 * @author ruihua.qin
 *
 */
@EnableTurbine
@SpringBootApplication
public class ClusterMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClusterMonitorApplication.class, args);
	}
}
