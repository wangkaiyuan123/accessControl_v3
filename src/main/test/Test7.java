import java.text.SimpleDateFormat;
import java.util.Date;



public class Test7 {
	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		System.out.println(sdf.format(new Date()));
	}
}
