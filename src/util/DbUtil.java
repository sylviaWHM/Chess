package util;

import model.Rank;
import model.User;

import java.sql.*;
import java.util.ArrayList;


/**
 * 数据库操作类Dao层
 */
public class DbUtil {
	private Connection con;
	private PreparedStatement ps;
	private ResultSet rs;
	String user = "root";
	String password = "root";
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/db_chess";

	//Loading configuration drivers
	public DbUtil(){
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	//Connecting to the database
	public void connect(){
		try {
			con = DriverManager.getConnection(url, user, password);
			System.out.println("Connection to MySQL successful!");
		} catch (SQLException e) {
			System.out.println("Failed to connect, wrong account or password!");
		}
	}

	//Add user
	public boolean addUser(String name,String password){
		try {
			String sql = "insert into t_user values(?,?,?)";
			ps = con.prepareStatement(sql);
			ps.setString(1,name);
			ps.setString(2,password);
			ps.setDouble(3,0);
			if (ps.executeUpdate() != 0){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	//User login, return the user entity class.
	public User loginUser(String name,String password){
		try {
			String sql = "select * from t_user where name=? and password=?";
			ps = con.prepareStatement(sql);
			ps.setString(1,name);
			ps.setString(2,password);
			rs = ps.executeQuery();
			if (rs.next()){
				return new User(rs.getString(1),rs.getDouble(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	//Update user win rate
	public void updateWinRate(String name){
		try {
			int rankSum;//Total number of matches played
			int winSum = 0;//Number of wins
			ArrayList<Rank> ranks = getAllRank(name);
			rankSum = ranks.size();
			for (Rank rank:ranks){
				if (rank.result.equals("victory")){
					winSum++;
				}
			}

			String sql = "update t_user set winRate=? where name=?";
			ps = con.prepareStatement(sql);
			double winRate;
			if (rankSum == 0){
				winRate = 0;
			}else{
				winRate = (double) winSum/(double) rankSum;
			}
			ps.setDouble(1,winRate);
			ps.setString(2,name);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//Add a record
	public void addRank(Rank rank){
		try {
			String sql = "insert into t_rank values(?,?,?,?)";
			ps = con.prepareStatement(sql);
			ps.setString(1,rank.name);
			ps.setString(2,rank.otherName);
			ps.setString(3,rank.result);
			ps.setString(4,rank.date);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//Get the player's full record
	public ArrayList<Rank> getAllRank(String name){
		ArrayList<Rank> ranks = new ArrayList<>();
		try {
			String sql = "select * from t_rank where name=?";
			ps = con.prepareStatement(sql);
			ps.setString(1,name);
			rs = ps.executeQuery();
			while (rs.next()){
				Rank rank = new Rank(rs.getString(1),
				rs.getString(2), rs.getString(3),rs.getString(4));
				ranks.add(rank);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ranks;
	}

	//Get user rankings
	public ArrayList<String[]> getRankLevel() {
		ArrayList<String[]> ranks = new ArrayList<>();
		try {
			String sql = "select name,winRate from t_user order by winRate desc";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				ranks.add(new String[]{rs.getString(1),rs.getString(2)});
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ranks;
	}

}

