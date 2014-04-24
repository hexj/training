package forwe.hexj.learn.j8;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

public class J8LearnTest {

	@Test
	public void arrayTest() {
		try {
			Files.list(Paths.get(".")).forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void dirTest() {
		try {
			Files.list(Paths.get(".")).filter(Files::isDirectory)
					.forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void hidden() {
		System.out.println(new File(".").listFiles(File::isHidden));
	}
}
