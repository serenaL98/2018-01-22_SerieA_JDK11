package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.seriea.model.AnnataPunteggio;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {

	public List<Season> listAllSeasons() {
		String sql = "SELECT season, description FROM seasons";
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Season(res.getInt("season"), res.getString("description")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Team> listTeams() {
		String sql = "SELECT team FROM teams";
		List<Team> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Team(res.getString("team")));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<AnnataPunteggio> prendiPareggi(String squadra){
		String sql = "SELECT m.Season anno, m.HomeTeam, m.AwayTeam, count(m.FTR) punti, s.description des" + 
				" FROM matches m, seasons s" + 
				" WHERE m.FTR = 'D' AND (m.HomeTeam = ? OR m.AwayTeam = ? ) AND m.Season=s.season " + 
				" GROUP BY m.Season";
		List<AnnataPunteggio> lista = new ArrayList<>();
		
		try {
			Connection con = DBConnect.getConnection();
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, squadra);
			st.setString(2, squadra);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				Integer anno = res.getInt("anno");
				Integer punti = res.getInt("punti");
				String sta = res.getString("des");
				
				AnnataPunteggio a = new AnnataPunteggio(anno, punti, sta);
				
				lista.add(a);
			}
			
			con.close();
			
		}catch(SQLException e) {
			throw new RuntimeException("ERRORE DB: impossibile prendere i punteggi.\n", e);
		}
		
		return lista;
	}
	
	public List<AnnataPunteggio> prendiVittorie(String squadra){
		String sql = "SELECT m.Season anno, m.HomeTeam, m.AwayTeam, 3*Count(m.FTR) punti, s.description des" + 
				" FROM matches m, seasons s" + 
				" WHERE (m.HomeTeam = ? AND m.FTR = 'H'  and m.Season= s.season) OR(m.AwayTeam = ? AND m.FTR = 'A'  and m.Season= s.season) " + 
				" GROUP BY m.Season";
		List<AnnataPunteggio> lista = new ArrayList<>();
		
		try {
			Connection con = DBConnect.getConnection();
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, squadra);
			st.setString(2, squadra);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				Integer anno = res.getInt("anno");
				Integer punti = res.getInt("punti");
				String sta = res.getString("des");
				
				AnnataPunteggio a = new AnnataPunteggio(anno, punti, sta);
				
				lista.add(a);
			}
			
			con.close();
			
		}catch(SQLException e) {
			throw new RuntimeException("ERRORE DB: impossibile prendere i punteggi.\n", e);
		}
		
		return lista;
	}
	
	public List<String> prendiStagioni(String squadra){
		String sql = "SELECT s.description descr" + 
				" from seasons s" + 
				" WHERE s.season IN (SELECT DISTINCT m.Season" + 
				"							FROM matches m" + 
				"							WHERE m.HomeTeam = ? OR m.AwayTeam = ? )";
		List<String> lista = new ArrayList<>();
		
		try {
			Connection con = DBConnect.getConnection();
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, squadra);
			st.setString(2, squadra);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				String i = res.getString("descr");
				lista.add(i);
			}
			
			con.close();
			
		}catch(SQLException e) {
			throw new RuntimeException("ERRORE DB: impossibile prendere i valori.", e);
		}
		
		return lista;
	}
}

