package br.com.frequencia.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.com.frequencia.exception.PersistenceException;
import br.com.frequencia.model.Credenciamento;
import br.com.frequencia.model.Evento;
import br.com.frequencia.model.Frequencia;
import br.com.frequencia.model.Horario;
import br.com.frequencia.model.Instrutor;
import br.com.frequencia.model.Orgao;
import br.com.frequencia.model.Participante;
import br.com.frequencia.model.ParticipanteDestaque;
import br.com.frequencia.model.ParticipanteInscrito;
import br.com.frequencia.model.Sala;
import br.com.frequencia.model.Turma;
import br.com.frequencia.model.Usuario;

/**
 * Componente responsável por abrir e encerrar a conexão com o banco de dados.
 * 
 */
public class DatabaseCriation {

	// Informacões para conexão com banco de dados SQLite.
	private static final int BUFFER = 1 * 1024 * 1024;
	private static final File DATABASE = new File("./etc/db/frequencia.db");
	private static final String STR_DRIVER = "org.sqlite.JDBC";
	private static final String STR_CON = "jdbc:sqlite:";

	/**
	 * Se não existir o arquivo de banco de dados, o programa roda o método para criar um arquivo de banco de dados
	 */
	public static void checkDatabase() throws Exception {
		if (!DATABASE.exists()) {
			createNewDatabase();
		}
	}

	/**
	 * Irá rodar todos os comandos necessários para fazer a configuração inicial do banco: criação de tabelas, usuários (se o banco
	 * comportar esse recurso), inserção de registros iniciais, etc.
	 */
	public static void createNewDatabase() throws Exception {
		Connection conn = null;
		Statement stmt = null;
		try {

			// Cria os diretórios pai do arquivo (caso não existam)
			DATABASE.getParentFile().mkdirs();
			// Cria o arquivo do banco
			DATABASE.createNewFile();
			if (!DATABASE.exists()) {
				throw new PersistenceException("Erro ao gravar o arquivo de banco de dados.");
			}

			conn = getConnection();
			stmt = conn.createStatement();
			createTables(stmt);
		} catch (Exception ex) {
			closeAll(conn, stmt);
			throw new PersistenceException("Erro na criação do banco de dados\n" + ex.getMessage());
		}
	}

	/**
	 * 
	 * @param s
	 * @throws SQLException
	 */
	public static void createTables(Statement s){
		try {
			s.execute(Instrutor.montarDDL());
			s.execute(Credenciamento.montarDDL());
			s.execute(Evento.montarDDL());
			s.execute(Frequencia.montarDDL());
			s.execute(Horario.montarDDL());
			s.execute(Orgao.montarDDL());
			s.execute(Participante.montarDDL());
			s.execute(ParticipanteDestaque.montarDDL());
			s.execute(ParticipanteInscrito.montarDDL());
			s.execute(Sala.montarDDL());
			s.execute(Turma.montarDDL());
			s.execute(Usuario.montarDDL());
		} catch (SQLException e) {
			throw new PersistenceException("Erro ao criar as tabelas no banco de dados.\n" + e.getMessage());
		}
	}

	/**
	 * Cria conexões com o banco
	 */
	public static Connection getConnection() throws Exception {

		Class.forName(STR_DRIVER);
		Connection conn = DriverManager.getConnection(STR_CON + DATABASE.getPath());
		return conn;

	}

	public static void removeDataBase() throws Exception {
		DATABASE.delete();
	}

	public static void backupDatabase(File arquivoBkp) throws Exception {

		// Verificações iniciais
		if (!DATABASE.exists()) {
			throw new PersistenceException("Não foi possível fazer backup porquê o arquivo de dados não foi localizado!");
		}
		if (!arquivoBkp.isDirectory() && !arquivoBkp.getName().toLowerCase().endsWith(".db")) {
			arquivoBkp = new File(arquivoBkp.getPath() + ".db");
		}

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {

			bis = new BufferedInputStream(new FileInputStream(DATABASE), BUFFER);
			bos = new BufferedOutputStream(new FileOutputStream(arquivoBkp), BUFFER);

			int byteLido;
			while ((byteLido = bis.read()) != -1) {
				bos.write(byteLido);
			}

		} finally {
			if (bos != null) {
				bos.flush();
				bos.close();
			}
			if (bis != null) {
				bis.close();
			}
		}

	}

	public static void recoverBackupDatabase(File arquivoBkp) throws Exception {

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {

			bis = new BufferedInputStream(new FileInputStream(arquivoBkp), BUFFER);
			bos = new BufferedOutputStream(new FileOutputStream(DATABASE), BUFFER);

			int byteLido;
			while ((byteLido = bis.read()) != -1) {
				bos.write(byteLido);
			}

		} finally {
			if (bos != null) {
				bos.flush();
				bos.close();
			}
			if (bis != null) {
				bis.close();
			}
		}

	}

	public static void closeAll(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			throw new PersistenceException("Não foi possivel fechar a conexão com o banco de dados!\n" + e.getMessage());
		}
	}

	public static void closeAll(Connection conn, Statement stmt) {
		try {
			if (conn != null) {
				closeAll(conn);
			}
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception e) {
			throw new PersistenceException("Não foi possivel fechar o statement!\n" + e.getMessage());
		}
	}

	public static void closeAll(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if (conn != null || stmt != null) {
				closeAll(conn, stmt);
			}
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			throw new PersistenceException("Não foi possivel fechar o statement!\n" + e.getMessage());
		}
	}
}
