package misc;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Utils {

	
	public static final SimpleDateFormat dateFormatDDMonYYYYhm = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
	
	public static void setTimeZone(){
		dateFormatDDMonYYYYhm.setTimeZone(TimeZone.getTimeZone("IST"));
		TimeZone.setDefault(TimeZone.getTimeZone("IST"));
	}
	public static Calendar getCalender(){
		Calendar cal = new GregorianCalendar();
		cal.setTimeZone(TimeZone.getTimeZone("IST"));
		return cal;
	}
}
