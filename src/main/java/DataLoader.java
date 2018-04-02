import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {
	private List<DataContainer> loadedData;
	
	public DataLoader(String filePath, DataInterpreter interpreter) throws IOException {
		loadedData = new ArrayList<DataContainer>();
		load(filePath, interpreter);
	}
	
	public List<DataContainer> getLoadedData() {
		return loadedData;
	}
	
	public void load(String filePath, DataInterpreter interpreter) throws IOException {
		try (
			FileReader fileReader = new FileReader(filePath);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
		) {
			
			String lineWithData;
			while ((lineWithData = bufferedReader.readLine()) != null) {
				loadedData.add(interpreter.interpretString(lineWithData));
			}
			
			fileReader.close();
			bufferedReader.close();
		}
	}
}
