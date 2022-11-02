import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Loader {	
	public Process load(String exeName) {
		try {
			File file = new File("data" + "/" + exeName);
			Scanner scanner = new Scanner(file);
			Process process = new Process();
			process.parse(scanner);
			scanner.close();
			return process;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
