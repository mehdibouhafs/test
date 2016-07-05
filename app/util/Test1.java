package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test1 {
	
	public Test1() throws IOException {
		String chemin = "C:\\Users\\MBS\\Desktop\\complete\\src\\main\\resources\\sample-data.csv";
		List<String> lines = firstLine(new File(chemin));
		
			System.out.println(lines);
		
	}
	
	
	public List<String> firstLine (File f) throws IOException {
        List<String> result = new ArrayList<>(); // !!!
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);

        /*for (String line = br.readLine(); line != null; line = br.readLine()) {
            result.add(line);
        }

        br.close();
        fr.close();*/
        String line = br.readLine();
        String[] cols =  line.split(",");
        for (String col: cols
             ) {
            result.add(col);
        }
        return result;
    }
	
	public static void main(String[] args) throws IOException {
		new Test1();
	}
	
	
}
