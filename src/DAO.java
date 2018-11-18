import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

abstract public class DAO {
	public static String EDIT_RESULT="Edit finish";
	public static String DELETE_RESULT="Delete finish";
	abstract public void addAnEntry(String name, String number);
	abstract public String searchPhoneNumber(String name);
	abstract public String editData(String name,String number);
	abstract public String deleteData(String name);
	
}
//HashTable
class HashTableDataBase extends DAO
{
	private Hashtable<String, String> phoneBook;
	
	HashTableDataBase() {
		phoneBook=new Hashtable<>();
	}
	public void addAnEntry(String name, String number) {
		phoneBook.put(name, number);
	}
	public String searchPhoneNumber(String name) {
		return (String) phoneBook.get(name);
	}
	public String editData(String name,String number) {
		if((String)phoneBook.get(name)==null)
			return null;
		phoneBook.put(name, number);
		return EDIT_RESULT;
	}
	public String deleteData(String name) {
		if((String)phoneBook.get(name)==null)
			return null;
		phoneBook.remove(name);
		return DELETE_RESULT;
	}
}
//Text
class TextDataBase extends DAO
{
	private String fileName;
	public TextDataBase() {
		fileName="Data.txt";
		File file=new File(fileName);
		if(!file.exists())
		{
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public TextDataBase(String name)
	{
		fileName=name;
		File file=new File(fileName);
		if(!file.exists())
		{
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	@Override
	public void addAnEntry(String name, String number) {
		FileWriter fw;
		try {
			fw = new FileWriter(fileName,true);
			fw.write(name+" "+number+System.getProperty("line.separator"));
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public String searchPhoneNumber(String name) {
		FileReader fr;
		try {
			fr = new FileReader(fileName);
			BufferedReader br=new BufferedReader(fr);
			while(br.ready())
			{
				String[] data=br.readLine().split(" ");
				if(data[0].equals(name))
					return data[1];
			}
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String editData(String name, String number) {
		String result=null;
		//Read
		FileReader fr;
		String allData="";
		try {
			fr = new FileReader(fileName);
			BufferedReader br=new BufferedReader(fr);
			while(br.ready())
			{
				String dataLine=br.readLine();
				String[] data=dataLine.split(" ");
				if(data[0].equals(name))
				{
					data[1]=number;
					result=EDIT_RESULT;
				}
				allData+=(data[0]+" "+data[1]+"\r\n");
			}
			
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Write
		FileWriter fw;
		try {
			fw = new FileWriter(fileName);
			fw.write(allData);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public String deleteData(String name) {
		String result=null;
		//Read
		FileReader fr;
		String allData="";
		try {
			fr = new FileReader(fileName);
			BufferedReader br=new BufferedReader(fr);
			while(br.ready())
			{
				String dataLine=br.readLine();
				String[] data=dataLine.split(" ");
				if(data[0].equals(name))
				{
					result=DELETE_RESULT;
				}
				else
				{
					allData+=(data[0]+" "+data[1]+"\r\n");
				}
			}
			
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Write
		FileWriter fw;
		try {
			fw = new FileWriter(fileName);
			fw.write(allData);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
}
//Microsoft Access
class AccessDataBase extends DAO
{
	private String fileName;
	private Connection connection;
	private Statement statement;
	public AccessDataBase(String name) {
		fileName=name;
		
		try {
			//建立驅動程式，連結odbc至Microsoft Access
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			String dataSource = "jdbc:ucanaccess://"+fileName;	 
			//連結資料庫
			File file=new File(fileName);
			//不存在檔案
			if(!file.exists())
			{
				connection=DriverManager.getConnection(dataSource+";newdatabaseversion=V2010");
				statement = connection.createStatement();
				statement.executeUpdate("CREATE TABLE PhoneBook (Name TEXT,Number TEXT)");
			}
			else
			{
				connection=DriverManager.getConnection(dataSource);
				statement = connection.createStatement();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	@Override
	public void addAnEntry(String name, String number) {
		try {
			statement.executeUpdate("INSERT INTO PhoneBook (Name, Number) VALUES ('"+name+"','"+number+"')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public String searchPhoneNumber(String name) {
		try {
			ResultSet resultSet=statement.executeQuery("select * from PhoneBook where Name = '"+name+"'");
			while(resultSet.next())
			{
				if(resultSet.getString("Name").equals(name))
					return resultSet.getString("Number");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String editData(String name, String number) {
		try {
			if(statement.executeUpdate("update PhoneBook set Number = '"+number+"' where Name = '"+name+"'")>0)
				return EDIT_RESULT;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String deleteData(String name) {
		try {
			if(statement.executeUpdate("delete from PhoneBook where Name = '"+name+"'")>0)
				return DELETE_RESULT;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
