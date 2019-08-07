package sorter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MultimediaSorter {

	public static void main(String[] args) throws Exception {
		MultimediaSorter renamer = new MultimediaSorter();
		renamer.rename();
	}

	final File workingDir;

	final File targetDir;

	final NameComparator nameComparator = new NameComparator();

	private MultimediaSorter() throws Exception {
		workingDir = new File(MultimediaSorter.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
		targetDir = new File(workingDir.getPath() + "/output");
		targetDir.mkdir();
		System.out.println("Working directory: " + workingDir);
		System.out.println("Target directory: " + targetDir);
	}

	public void rename() {
		final List<File> sortedFiles = Arrays.asList(workingDir.listFiles()).stream().filter(f -> f.isFile() && (f.getName().endsWith(SupportedExtension.JPG.getExtension()) || f.getName().endsWith(SupportedExtension.MP4.getExtension()))).sorted(nameComparator).collect(Collectors.toList());
		if (sortedFiles.isEmpty()){
			System.out.println("No files to process");
			return;
		}

		for (int i = 0; i < sortedFiles.size(); i++) {
			System.out.println((i + 1) + "/" + sortedFiles.size()); 
			File file = sortedFiles.get(i);
			try {
				Files.copy(file.toPath(),
						Paths.get(targetDir.getAbsolutePath() + "/" + (i + 1)
								+ file.getName().substring(file.getName().lastIndexOf("."))),
						StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				System.err.println("Failed to copy file: " + file.getName());
				e.printStackTrace();
			}
		}
	}
}
