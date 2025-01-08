package com.sb.sbwap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 取り込む対象のsblibのパッケージ配下のパッケージを認識できるよう、SpringBootの設定を変更する
@SpringBootApplication(scanBasePackages = {
		"com.sb.sbwap",
		"com.sb.sblib",
})
public class SbwapApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbwapApplication.class, args);
	}
}
