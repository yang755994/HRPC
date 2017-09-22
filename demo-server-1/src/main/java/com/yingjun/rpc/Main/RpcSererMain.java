package com.yingjun.rpc.Main;


import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RpcSererMain {

	private static final String[] CONFIG_RESOUCES = new String[] { "spring-server.xml" };

	private static ClassPathXmlApplicationContext springContext = null;

	private static volatile boolean running = true;

	public static void main(String[] args) {

		startSpringContainer();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				if (springContext != null) {
					springContext.stop();
					springContext.close();
					springContext = null;
				}
				synchronized (RpcSererMain.class) {
					running = false;
					RpcSererMain.class.notify();
				}
			}
		});

		synchronized (RpcSererMain.class) {
			while (running) {
				try {
					RpcSererMain.class.wait();
				} catch (Throwable e) {
				}
			}
		}

	}

	private static void startSpringContainer() {
		springContext = new ClassPathXmlApplicationContext(CONFIG_RESOUCES);
		springContext.start();
		System.out.println("application start success......");
	}
}
