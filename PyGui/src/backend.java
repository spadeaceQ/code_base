import java.io.BufferedReader;
import java.io.InputStreamReader;

public class backend {

    public static void main(String[] args) {

    // sending args and executing the python script at the terminal and reading its output	
    try{	
        String command = "python ./python_scripts/draw.py";

        Process proc = Runtime.getRuntime().exec(command);

        // Read the output

        BufferedReader reader =  
              new BufferedReader(new InputStreamReader(proc.getInputStream()));

        String line = "";
        while((line = reader.readLine()) != null) {
            System.out.print(line + "\n");
        }


        proc.waitFor();   
    }catch (Exception e){};
    }
} 