package de.vaplus.api.pojo;

import java.util.List;

import de.vaplus.api.ImportLine;

public class CsvFileReadResult {
	public List<? extends ImportLine> importLines;
	public int csvLines;
	public int cancelationLines;
}
