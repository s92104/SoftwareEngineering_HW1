import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;

/*
 * Model data for the phone book application.  
 */

public class PhoneBookModel {

	private PhoneBookView phonebookview;

	// The following are various states captured by the model
	public static String ADD_NAME_STATE = "ADD_NAME";
	public static String ADD_NUMBER_STATE = "ADD_NUMBER";
	public static String SEARCH_STATE = "SEARCH";
	public static String IDLE_STATE = "IDLE";
	public static String SEARCH_RESULT_STATE = "SEARCH_RESULT";
	public static String ERROR_STATE = "ERROR";
	public static String EXIT_STATE = "EXIT";
	//Edit
	public static String EDIT_NAME_STATE = "EDIT_NAME";
	public static String EDIT_NUMBER_STATE = "EDIT_NUMBER";
	public static String EDIT_RESULT_STATE = "EDIT_RESULT";
	//Delete
	public static String DELETE_STATE = "DELETE_NAME";
	public static String DELETE_RESULT_STATE = "DELETE_RESULT";
	// Private fields used to track various model data
	private String state = IDLE_STATE;
	private String searchResult = null;
	private DAO dao;
	private String editResult=null;
	private String deleteResult=null;

	/**
	 * set the state
	 * @param aState
	 */
	public void setState(String aState) {
		state = aState;
		phonebookview.stateHasChanged(this, state);
	}
	
	/**
	 * add a phone entry
	 * @param name
	 * @param number
	 */
	public void addAnEntry(String name, String number) {
		dao.addAnEntry(name, number);
	}

	/**
	 * search the phone number and set the searchResult field
	 * @param name
	 */
	public void searchPhoneNumber(String name) {
		searchResult = dao.searchPhoneNumber(name);
	}

	/**
	 * return the search result
	 */
	public String getSearchResult() {
		return searchResult;
	}

	/**
	 * get the state
	 */
	public String getState() {
		return state;
	}

	//edit the data
	public void editData(String name,String number) {
		editResult=dao.editData(name, number);
	}
	public String getEditResult() {
		return editResult;
	}

	//delete the data
	public void deleteData(String name)
	{
		deleteResult=dao.deleteData(name);
	}
	public String getDeleteResult() {
		return deleteResult;
	}
	
	/**
	 * constructor
	 * @param view
	 */
	public PhoneBookModel(PhoneBookView view) {
		phonebookview = view;
		dao=new AccessDataBase("Data.accdb");
		//dao=new TextDataBase("Data.txt");
	}
	
}
