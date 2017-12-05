package de.vaplus.api;

import java.io.Serializable;
import java.util.List;

import de.vaplus.api.entity.SearchResult;

public interface SearchControllerInterface extends Serializable {

	List<? extends SearchResult> getResults(String query);

}
