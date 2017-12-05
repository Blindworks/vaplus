package de.vaplus.api.controller;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import de.vaplus.api.ImportLine;
import de.vaplus.api.pojo.CsvFileReadResult;
import de.vaplus.api.pojo.ImportResult;

public interface ImportControllerInterface extends Serializable {

	CsvFileReadResult readPSWCube24Row(InputStream is) throws Exception;

	CsvFileReadResult readPSWCube45Row(InputStream is) throws Exception;

	List<String> getUnknownUserAliasList(List<? extends ImportLine> importLines);

	List<String> getUnknownShopAliasList(List<? extends ImportLine> importLines);

	List<String> getUnknownVOList(List<? extends ImportLine> importLines);

	void importData(List<? extends ImportLine> importLines);

	CsvFileReadResult readSortics_2_5_4(InputStream is) throws Exception;

	CsvFileReadResult readEposActivation(InputStream is) throws Exception;

	CsvFileReadResult readEposOther(InputStream is) throws Exception;

	boolean isImportWorkerRunning();

	ImportResult getImportResult();

}
