package br.com.frequencia.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	/**
	 * @param date
	 * @return
	 */
	public static String getDataHora(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);

		return formatter.format(date);
	}

	/**
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date getDataHora(String date, String format) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.parse(date);
	}

	public static Date getDateSemHora(Date date) throws ParseException {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String dtString = format.format(date);
		return (Date) format.parse(dtString);
	}
	
	public static Calendar getCalendarHora(String dataHora, String format) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			cal.setTime(sdf.parse(dataHora));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return cal;
	}

}
