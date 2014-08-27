package controler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateFile{
	File file;
	FileWriter filewriter;
	public CreateFile(){
		File old = new File("tmp.csv");
		old.delete();
		file = new File("./tmp.csv");

	}

	public void addData(int[] data){
		String os = System.getProperty("os.name");
		try {
			filewriter = new FileWriter(file, true);
			for(int i = 0; i < data.length; i++){
				filewriter.write(String.valueOf(data[i]));
				filewriter.write(",");
			}
			if(os.contains("Mac")){
				filewriter.write("\r");
			}else{
				filewriter.write("\r\n");
			}


			filewriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
