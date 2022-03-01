package com.tirmizee;

import com.tirmizee.utils.AESUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootBodyDecryptionApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootBodyDecryptionApplication.class, args);
		String originalString = "{\"name\":\"revise\"}";
		String encryptedString = AESUtils.encrypt(originalString);
		String decryptedString = AESUtils.decrypt(encryptedString);

		System.out.println(originalString);
		System.out.println(encryptedString);
		System.out.println(decryptedString);
	}

}
