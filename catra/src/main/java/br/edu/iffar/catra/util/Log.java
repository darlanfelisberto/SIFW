package br.edu.iffar.catra.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import br.edu.iffar.fw.classBag.db.Model;

public class Log {

	private static String pattern = "[%s] [%s] [%s]";
	private static String patternEntity = "[%s] [%s] [%s] [%s]";
	private static String patternEntityMenssagem = "[%s] [%s] [%s] [%s] [%s]";
	private static DateTimeFormatter fdtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:MM:ss");
	
	private static BufferedWriter writer;

	public static Path getPath() throws IOException {
		String today = "log " + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

		Path path = Paths.get("log/" + today);
		if (!Files.exists(path)) {
			Files.createFile(path);
		}
		return path;
	}
	
	public static void init() {
		try {
			if (!Files.exists(Paths.get("log"))) {
				Files.createDirectories(Paths.get("log"));
			}

			writer = Files.newBufferedWriter(getPath(),StandardOpenOption.APPEND);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String readFile() {
		try {
			return  Files.readString(getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void close() {
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void info(String pattern) {
		String log = String.format(Log.pattern, LocalDateTime.now().format(fdtFormatter),"info",pattern);
		try {
			writer.write(log);
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void info(String pattern,Model<?> entity) {
		String log = String.format(patternEntity, LocalDateTime.now().format(fdtFormatter),"info",pattern,(entity.getClass().getName() + Model.SEPARATIOR_KEY + entity.getMMId().toString()));
		try {
			writer.write(log);
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void info(String pattern,Model<?> entity,String msg) {
		String log = String.format(patternEntity, LocalDateTime.now().format(fdtFormatter),"info",pattern,msg,(entity.getClass().getName() + Model.SEPARATIOR_KEY + entity.getMMId().toString()));
		try {
			writer.write(log);
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printError(String pattern) {
		String log = String.format(Log.pattern, LocalDateTime.now().format(fdtFormatter), "error", pattern);
		System.out.println(log);
		try {
			writer.write(log);
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
