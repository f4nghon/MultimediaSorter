package sorter;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class NameComparator implements Comparator<File> {

	final DateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

	public NameComparator() {
		// empty
	}

	@Override
	public int compare(File o1, File o2) {
		try {
			String file1Name = o1.getName().substring(0, o1.getName().lastIndexOf("."));
			String file2Name = o2.getName().substring(0, o2.getName().lastIndexOf("."));
			Date d1 = extractDateFromFileName(file1Name);
			Date d2 = extractDateFromFileName(file2Name);
			if (d1.equals(d2)) {
				// Same date, check for suffix
				int s1 = extractSuffixFromFileName(file1Name);
				int s2 = extractSuffixFromFileName(file2Name);
				return Integer.valueOf(s1).compareTo(Integer.valueOf(s2));
			}
			return d1.compareTo(d2);
		} catch (Exception ex) {
			System.err.println("Failed to parse date: " + o1.getName() + ", " + o2.getName());
			ex.printStackTrace();
		}
		return 0;
	}

	private Date extractDateFromFileName(String fileName) throws ParseException {
		if (fileName.startsWith("IMG_") || fileName.startsWith("VID_")) {
			if (fileName.length() < 18) {
				throw new ParseException("Incorrect file name: " + fileName, 0);
			}
			return fileNameDateFormat.parse(fileName.substring(4, 19));
		} else if (fileName.startsWith("PANO_")) {
			if (fileName.length() < 19) {
				throw new ParseException("Incorrect file name: " + fileName, 0);
			}
			return fileNameDateFormat.parse(fileName.substring(5, 20));
		} else {
			// no prefix might be available
			if (fileName.length() < 14) {
				throw new ParseException("Incorrect file name: " + fileName, 0);
			}
			return fileNameDateFormat.parse(fileName.substring(0, 15));
		}
	}

	private int extractSuffixFromFileName(String fileName) {
		// 2 photos takes at the same second, check suffix
		int suffixIndex = fileName.lastIndexOf("_") + 1;
		if ((fileName.startsWith("IMG_") || fileName.startsWith("PANO_") || fileName.startsWith("VID_"))
				&& suffixIndex > 16) {
			String suffixStr = fileName.substring(suffixIndex);
			try {
				return Integer.parseInt(suffixStr);
			} catch (NumberFormatException ex) {
				System.err.println("Failed to extract suffix from file: " + fileName);
			}
		}

		return 0;
	}

}
