package hello.hellospring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

@SpringBootTest
class HelloSpringApplicationTests {

	@Test
	void contextLoads() {
		Random rand = new Random();
		char[] chars = new char[16];
		for(int i=0;i<chars.length;i++) {
			chars[i] = (char) rand.nextInt(65536);
			if (!Character.isValidCodePoint(chars[i]))
				i--;
		}
		String s = new String(chars);
		System.out.println("s=" + s);
	}

}
