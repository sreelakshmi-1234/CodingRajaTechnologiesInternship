import java.sql.SQLException;
public class LibraryManagementSystem {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		System.out.println("******************** Welcome to the College Library! ********************");

		System.out.println("Please do login first for accessing menu.");

		LoginService loginService = new LoginService();
		loginService.doLogin();
	}
}
