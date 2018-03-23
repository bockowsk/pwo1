package pl.edu.agh.mwo2017.bockowsk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Main {

	Session session;

	public static void main(String[] args) {
		Main main = new Main();
		// main.printSchools();
		// main.jdbcTest();
		// main.printSchools();
		// main.addNewData();
		main.executeQueries();
		main.executeQuery1();
		// 1. Chcemy znaleźć tylko szkoły, których nazwa to UE. (Podpowiedź: użyj
		// składni
		// WHERE tabela.kolumna='wartosc')
		
		// 2. Wykorzystując funkcję session.delete() i analogię do tworzenia obiektów,
		// usuń wszystkie odnalezione w powyższym punkcie szkoły.
		
		// 3. Napisz zapytanie, które zwraca ilość szkół w bazie (Podpowiedź: użyj
		// funkcji
		// COUNT())
		
		// 4. Napisz zapytanie, które zwraca ilość studentów w bazie.
		
		// 5. Napisz zapytanie, które zwraca wszystkie szkoły o liczbie klas większej
		// lub równej 2.
		
		// 6. Poniższe zapytanie wyszukuje szkołę, w której występuje klasa o profilu
		// “biol-chem”. Bazując na tym zapytaniu napisz nowe zapytanie, które wyszukuje
		// szkołę z klasą o profilu mat-fiz oraz obecnym roku większym bądź równym 2
		main.close();

	}

	public Main() {
		session = HibernateUtil.getSessionFactory().openSession();
	}

	public void close() {
		session.close();
		HibernateUtil.shutdown();
	}

	private void printSchools() {
		Criteria crit = session.createCriteria(School.class);
		List<School> schools = crit.list();

		System.out.println("### Schools and classes");
		for (School s : schools) {
			System.out.println(s);
			for (SchoolClass c : s.getClasses()) {
				System.out.println("\t" + c);
				// students
				for (Student student : c.getStudents()) {
					System.out.println("\t\t" + student);
				}
			}
		}
	}

	private void addNewData() {
		Student student1 = new Student("Pawel", "Smieszny", "12023001345");
		Student student2 = new Student("Pawel", "Grzes", "12023345001");
		Student student3 = new Student("Andrzej", "Smakolyk", "12023050134");
		SchoolClass klasa1 = new SchoolClass(2016, 2018, "Matematyka");
		klasa1.addStudent(student1);
		klasa1.addStudent(student2);
		klasa1.addStudent(student3);
		School szkola = new School("Politechnika Krakowska", "Warszawska 100");
		szkola.addClasses(klasa1);
		Transaction transaction = session.beginTransaction();
		session.save(szkola);
		transaction.commit();

	}

	private void executeQueries() {
		String hql = "FROM School";
		Query query = session.createQuery(hql);
		List results = query.list();
		System.out.println(results);

		
	}

	private void executeQuery1() {
		String hql = "FROM School WHERE School='UE'";
		Query query = session.createQuery(hql);
		List<School> results = query.list();
		for (School s: results) {			
			System.out.println(s);
		}

		
	}

	private void jdbcTest() {
		Connection conn = null;
		Statement stmt = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("org.sqlite.JDBC");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection("jdbc:sqlite:./src/main/resources/school.db", "", "");

			// STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM schools";
			ResultSet rs = stmt.executeQuery(sql);

			// STEP 5: Extract data from result set
			while (rs.next()) {
				// Retrieve by column name
				String name = rs.getString("name");
				String address = rs.getString("address");

				// Display values
				System.out.println("Name: " + name);
				System.out.println(" address: " + address);
			}
			// STEP 6: Clean-up environment
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		} // end try
		System.out.println("Goodbye!");
	}// end jdbcTest

}
