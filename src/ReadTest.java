import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.Gson;

public class ReadTest {

    public static void main(String[] args) {

        Gson gson = new Gson();
        Waiter w = new Waiter();

        try {
            BufferedReader br = new BufferedReader(
                new FileReader("test/t0.json"));
            Data obj = gson.fromJson(br,Data.class);

            System.out.println(obj);

            for (Tutor t : obj.tutors) {
                for (Slot s : obj.slots) {
                    System.out.println("weighting (Tutor " + t + ", Slot " + s + "): " + w.weight(t,s));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

