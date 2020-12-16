package io.kimmking.rpcfx.demo.consumer;

import io.kimmking.rpcfx.client.RpcfxByteBuddy;
import io.kimmking.rpcfx.demo.api.Order;
import io.kimmking.rpcfx.demo.api.OrderService;
import io.kimmking.rpcfx.demo.api.User;
import io.kimmking.rpcfx.demo.api.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class RpcfxClientApplication {

	// 二方库
	// 三方库 lib
	// nexus, userserivce -> userdao -> user
	//

	public static void main(String[] args) {

		// UserService service = new xxx();
		// service.findById

//		UserService userService = Rpcfx.create(UserService.class, "http://localhost:8080/");
		UserService userService = RpcfxByteBuddy.create(UserService.class, "http://localhost:8080/");
		if (userService != null) {
			User user = userService.findById(1);
			System.out.println("find user id=1 from server: " + user.getName());
		}

//		OrderService orderService = Rpcfx.create(OrderService.class, "http://localhost:8080/");
		OrderService orderService = RpcfxByteBuddy.create(OrderService.class, "http://localhost:8080/");
		if (orderService != null) {
			Order order = orderService.findOrderById(1992129);
			System.out.println(String.format("find order name=%s, amount=%f", order.getName(), order.getAmount()));
		}
		// 新加一个OrderService

        // 使用 xstream 序列化方式
        userService = RpcfxByteBuddy.xstreamCreate(UserService.class, "http://localhost:8080/xstream");
        if (userService != null) {
            User user = userService.findById(1);
            System.out.println("find user id=1 from server: " + user.getName());
        }

//		SpringApplication.run(RpcfxClientApplication.class, args);
	}

}
