package nl.hr.project3_4.straalbetaal.encryption;

import java.util.*;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class BlackBox {

	public static void main(String args[]) {

	}
	//////////////// FIELDS/////////////////

	long[] Key;
	long SecondaryKey;

	public static BlackBox b = new BlackBox("Costa Monday", 3459);

	///////////// CONSTRUCTORS///////////////

	public BlackBox(String s) {
		char[] mix = s.toCharArray();
		Key = new long[mix.length];
		for (int i = 0; i < mix.length; i++) {
			Key[i] = (long) mix[i];
		}
		SecondaryKey = 2321; // SecondaryKey < 10000
	}

	public BlackBox(String s, long l) {
		char[] mix = s.toCharArray();
		Key = new long[mix.length];
		for (int i = 0; i < mix.length; i++) {
			Key[i] = (long) mix[i];
		}
		SecondaryKey = l;
	}

	///////////// METHODS///////////////

	public String encrypt(String s) {
		String converted = "";
		int i = 0;
		for (char c : s.toCharArray()) {
			long tijdelijk = ((long) c) * SecondaryKey * Key[i] * UTC();
			converted += (tijdelijk + "#");
			i++;
			if (i == Key.length) {
				i = 0;
			}
		}
		return converted;
	}

	public String decrypt(String s) {
		String convert = "";
		String decoded = "";
		int i = 0;
		for (char c : s.toCharArray()) {
			if (c != '#') {
				convert += c;
			} else {
				long tijdelijk = Long.parseLong(convert);
				tijdelijk /= SecondaryKey;
				tijdelijk /= Key[i];
				tijdelijk /= UTC();
				decoded += (char) (tijdelijk);
				convert = "";
				i++;
			}
			if (i == Key.length) {
				i = 0;
			}
		}
		return decoded;
	}

	public long UTC() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddHHmm");
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		long UTC = Long.parseLong(simpleDateFormat.format(calendar.getTime()));
		return UTC;
	}

}