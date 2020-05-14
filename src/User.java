import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class User { // User Class to Login and make operations in the system
	DatabaseConnection db = new DatabaseConnection(); // DatabaseConnection Object
	private long userID;
	private String userPass;
	Scanner input = new Scanner(System.in); // Scanner Object

	public User() { // User Constructor for database connection
		String url = "jdbc:mysql://" + db.getHost() + ":" + db.getPort() + "/" + db.getName()
				+ "?useUnicode=true&characterEncoding=utf8";

		try {
			Class.forName("com.mysql.jdbc.Driver");

		} catch (ClassNotFoundException ex) {
			System.out.println("Driver not found");
		}
		try {
			db.setCon(DriverManager.getConnection(url, db.getUserName(), db.getPassword()));
			System.out.println("Connection successful.");
		} catch (SQLException ex) {
			System.out.println("Connection fail.");
		}
	}
	
	public boolean userLogin(long tcNum, String uPass) {  // User Login takes TC and password
		boolean loginSuccess = false; // login status
		int length = String.valueOf(tcNum).length();
		if (length > 11 || length < 11 || length < 0) { // input validation
			System.out.println("Wrong input!!!!");
			return false;
		}
		String search = "Select * From user"; // Query for user
		try {
			db.setStatement(db.getCon().createStatement()); // Database statement
			ResultSet rs = db.getStatement().executeQuery(search);

			while (rs.next()) { // get user information one by one from database
				userID = rs.getLong("userID");
				userPass = rs.getString("userPass");
				if (tcNum == getUserID() && uPass.equals(getUserPass())) { // if user information matches with database
					System.out.print("Login Successfull!");
					loginSuccess = true; // makes true to loginSucces
				}
			}
			if (loginSuccess == false) { // if login is not successful
				System.out.print("Wrong TC.Kimlik number or password!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return loginSuccess; // returns the status
	}

	// This method is used for register the system as new user
	public void userRegister() {
		try {
			db.setStatement(db.getCon().createStatement());
			System.out.println("Please enter your TC.Kimlik number: ");
			long userID = input.nextLong();
			input.nextLine();
			System.out.println("Please enter your password: ");
			String userPass = input.nextLine();
			System.out.println("Please re-enter your password: ");
			String uPass2 = input.nextLine();
			if (userPass.equals(uPass2)) { // insert the data to the database
				String search = "Insert Into user(userID,userPass) VALUES(" + "'" + userID + "'," + "'" + userPass
						+ "')";
				db.getStatement().executeUpdate(search);
			} else {
				System.out.print("Passwords are not matching!");
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

	}
	// This method used for adding a crime file
	public void addfile(CrimeFile cfile) {
		System.out.println("Enter name");
		String username = input.nextLine();
		while (cfile.setUserName(username)) { // input validation
			System.out.println("Please use only letters!");
			System.out.println("Enter name");
			username = input.nextLine();
			cfile.setUserName(username);
		}

		System.out.println("Enter address");
		String address = input.nextLine();
		cfile.setUserAddress(address);

		do {
			try {
				System.out.println("Enter phone");
				long phone = input.nextLong();
				input.nextLine();
				while (cfile.setUserPhone(phone)) { // input validation
					System.out.println("Phone number must be 11 digit!");
					System.out.println("Enter phone again: ");
					phone = input.nextLong();
					cfile.setUserPhone(phone);
				}
				break;
			} catch (InputMismatchException e) {
				System.out.println("Please only use numbers!");
			}
			input.nextLine();
		} while (true);
		input.nextLine();

		System.out.println("Enter complaint type");
		String complainttype = input.nextLine();
		cfile.setComplaintType(complainttype);
		input.nextLine();

		System.out.println("Enter complaint description");
		String complaintdesc = input.nextLine();
		cfile.setComplaintDescription(complaintdesc);

		System.out.println("Enter crime location");
		String crimeloc = input.nextLine();
		cfile.setCrimeLocation(crimeloc);

		do {
			try {
				System.out.println("Enter crime time");
				int crimeTime = input.nextInt();
				while (cfile.setCrimeTime(crimeTime)) { // input validation
					System.out.println("Invalid time!");
					System.out.println("Enter Time again: ");
					crimeTime = input.nextInt();
					cfile.setCrimeTime(crimeTime);
				}
				break;
			} catch (InputMismatchException e) {
				System.out.println("Wrong format!");
			}
			input.nextLine();
		} while (true);

		do {
			try {
				System.out.println("Enter crime date");
				int crimeDate = input.nextInt();
				while (cfile.setCrimeDate(crimeDate)) { // input validation
					System.out.println("Invalid Date!");
					System.out.println("Enter Date again: ");
					crimeDate = input.nextInt();
					cfile.setCrimeDate(crimeDate);
				}
				break;
			} catch (InputMismatchException e) {
				System.out.println("Wrong format!");
			}
			input.nextLine();
		} while (true);

		try { // insert the data to the database
			db.setStatement(db.getCon().createStatement());
			String query = "Insert into crimefile(userName,userAddress,userPhone,complaintType,complaintDescription,crimeTime,crimeDate,crimeLocation) "
					+ "VALUES(" + "'" + cfile.getUserName() + "'," + "'" + cfile.getUserAddress() + "'," + "'"
					+ cfile.getUserPhone() + "'," + "'" + cfile.getComplaintType() + "'," + "'"
					+ cfile.getComplaintDescription() + "'," + "'" + cfile.getCrimeTime() + "'," + "'"
					+ cfile.getCrimeDate() + "'," + "'" + cfile.getCrimeLocation() + "')";
			db.getStatement().executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addmissingperson(MissingPerson mperson) {

		System.out.println("Enter name");
		String personname = input.nextLine();
		while (mperson.setName(personname)) { // input validation
			System.out.println("Please use only letters!");
			System.out.println("Enter name");
			personname = input.nextLine();
			mperson.setName(personname);
		}

		System.out.println("Enter surname");
		String personsurname = input.nextLine();
		while (mperson.setSurname(personsurname)) { // input validation
			System.out.println("Please use only letters!");
			System.out.println("Enter surname");
			personsurname = input.nextLine();
			mperson.setSurname(personsurname);
		}

		System.out.println("Enter gender");
		String persongender = input.nextLine();
		while (mperson.setGender(persongender)) { // input validation
			System.out.println("Please use only letters!");
			System.out.println("Enter gender");
			persongender = input.nextLine();
			mperson.setGender(persongender);
		}

		System.out.println("Enter place of birth");
		String personplaceofbirth = input.nextLine();
		while (mperson.setPlaceOfbirth(personplaceofbirth)) { // input validation
			System.out.println("Please use only letters!");
			System.out.println("Enter place of birth again: ");
			personplaceofbirth = input.nextLine();
			mperson.setPlaceOfbirth(personplaceofbirth);
		}

		do {
			try {
				System.out.println("Enter date of birth");
				int persondateofbirth = input.nextInt();
				while (mperson.setDateOfbirth(persondateofbirth)) { // input validation
					System.out.println("Invalid Date!");
					System.out.println("Enter Date again: ");
					persondateofbirth = input.nextInt();
					mperson.setDateOfbirth(persondateofbirth);
				}
				break;
			} catch (InputMismatchException e) {
				System.out.println("Wrong format!");
			}
			input.nextLine();
		} while (true);

		System.out.println("Enter weight");
		double personweight = input.nextDouble();
		mperson.setWeight(personweight);

		System.out.println("Enter height");
		double personheight = input.nextDouble();
		mperson.setHeight(personheight);
		input.nextLine();
		System.out.println("Enter hair color");
		String personhaircolor = input.nextLine();
		while (mperson.setHairColor(personhaircolor)) { // input validation
			System.out.println("Please use only letters!");
			System.out.println("Enter hair color again");
			personhaircolor = input.nextLine();
			mperson.setHairColor(personhaircolor);
		}

		System.out.println("Enter skin color");
		String personskincolor = input.nextLine();
		while (mperson.setSkinColor(personskincolor)) { // input validation
			System.out.println("Please use only letters!");
			System.out.println("Enter skin color again");
			personskincolor = input.nextLine();
			mperson.setSkinColor(personskincolor);
		}

		System.out.println("Enter eye color");
		String personeyecolor = input.nextLine();
		while (mperson.setEyeColor(personeyecolor)) { // input validation
			System.out.println("Please use only letters!");
			System.out.println("Enter eye color again");
			personeyecolor = input.nextLine();
			mperson.setEyeColor(personeyecolor);
		}

		do {
			try {
				System.out.println("Enter date missing");
				int personmissingdate = input.nextInt();
				while (mperson.setDateMissing(personmissingdate)) { // input validation
					System.out.println("Invalid Date!");
					System.out.println("Enter Date again: ");
					personmissingdate = input.nextInt();
					mperson.setDateMissing(personmissingdate);
				}
				break;
			} catch (InputMismatchException e) {
				System.out.println("Wrong format!");
			}
			input.nextLine();
		} while (true);

		try { // insert the data to the database
			db.setStatement(db.getCon().createStatement());
			String query = "Insert into missingperson(name, surname, gender, placeOfbirth, dateOfbirth, weight, height, dateMissing, skinColor, hairColor, eyeColor) "
					+ "VALUES(" + "'" + mperson.getName() + "'," + "'" + mperson.getSurname() + "'," + "'"
					+ mperson.getGender() + "'," + "'" + mperson.getPlaceOfbirth() + "'," + "'"
					+ mperson.getDateOfbirth() + "'," + "'" + mperson.getWeight() + "'," + "'" + mperson.getHeight()
					+ "'," + "'" + mperson.getDateMissing() + "'," + "'" + mperson.getSkinColor() + "'," + "'"
					+ mperson.getHairColor() + "'," + "'" + mperson.getEyeColor() + "')";
			db.getStatement().executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void viewhotnews() {
		String query = "select * from hotnews order by newsdate DESC"; //Query for criminal report

		try {
			db.setStatement(db.getCon().createStatement()); //database statement
			ResultSet rs = db.getStatement().executeQuery(query);

			while (rs.next()) { // gets the data from database and prints it to the screen
				String title = rs.getString("title");
				String text = rs.getString("text");
				Date newsDate = rs.getDate("newsdate");
				
				System.out.println("Title: " + title);
				System.out.println("Text: " + text);
				System.out.println("NewsDate: " + newsDate);
				System.out.println();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addComplaint(UserComplaint ucomp) {
		do {
			try {
				System.out.println("Enter complaint date");
				int compdate = input.nextInt();
				while (ucomp.setComplaintDate(compdate)) { // input validation
					System.out.println("Invalid Date!");
					System.out.println("Enter Complaint Date again: ");
					compdate = input.nextInt();
					ucomp.setComplaintDate(compdate);
				}
				break;
			} catch (InputMismatchException e) {
				System.out.println("Wrong format!");
			}
			input.nextLine();
		} while (true);
		input.nextLine();
		
		System.out.println("Enter complaint title: ");
		String comptitle = input.nextLine();
		ucomp.setTitle(comptitle);
		
		System.out.println("Enter subject: ");
		String subject = input.nextLine();
		ucomp.setSubject(subject);
		
		System.out.println("Enter complaint: ");
		String complaint = input.nextLine();
		ucomp.setComplaint(complaint);
		
		try { // insert the data to the database
			db.setStatement(db.getCon().createStatement());
			String query = "Insert into usercomplaint(comptitle,usercomplaint, compsubject, compdate)"
					+ "VALUES(" + "'" + ucomp.getTitle() + "'," + "'" + ucomp.getComplaint() + "'," + "'" + ucomp.getSubject() + "'," + "'"
					+ ucomp.getComplaintDate() + "')";
			db.getStatement().executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//Buraya bak�n s�k�nt� var
	public void addFeedback(Feedback feedback) {
		System.out.println("Enter name: ");
		String name = input.nextLine();
		feedback.setUsername(name);
		while (feedback.setUsername(name)) { // input validation
			System.out.println("Please use only letters!");
			System.out.println("Enter name again: ");
			name = input.nextLine();
			feedback.setUsername(name);
		}
		
		System.out.println("Enter surname: ");
		String surname = input.nextLine();
		feedback.setSurName(surname);
		while (feedback.setSurName(surname)) { // input validation
			System.out.println("Please use only letters!");
			System.out.println("Enter surname again: ");
			surname = input.nextLine();
			feedback.setSurName(surname);
		}
		
		System.out.println("Enter your feedback: ");
		String userf = input.nextLine();
		feedback.setFeedback(userf);
		
		
		do {
			try {
				System.out.println("Enter feedback date");
				int feedbackdate = input.nextInt();
				while (feedback.setFeedbackdate(feedbackdate)) { // input validation
					System.out.println("Invalid Date!");
					System.out.println("Enter Feedback Date again: ");
					feedbackdate = input.nextInt();
					feedback.setFeedbackdate(feedbackdate);
				}
				break;
			} catch (InputMismatchException e) {
				System.out.println("Wrong format!");
			}
			input.nextLine();
		} while (true);
		
		try { // insert the data to the database
			db.setStatement(db.getCon().createStatement());
			String query = "Insert into feedback(name,surname,feedback,feedbackdate)"
					+ "VALUES(" + "'" + feedback.getUsername() + "'," + "'" + feedback.getUsersurname() + "'," + "'" + feedback.getFeedback() + "'," + "'"
					+ feedback.getFeedbackdate() + "')";
			db.getStatement().executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public long getUserID() {
		return userID;
	}

	public String getUserPass() {
		return userPass;
	}
	public void userdisplay() { // Displays the User menu
		System.out.println("\nPress 1 for add a crime file...");
		System.out.println("Press 2 for add a missing person file...");
		System.out.println("Press 3 for display hot news...");
		System.out.println("Press 4 for add user complaint...");
		System.out.println("Press 5 for adding feedback...");
	}
}
