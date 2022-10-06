package br.edu.iffar.catra.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author qwerty
 */
public class Factory implements Runnable{
    private static EntityManagerFactory emf = null;
//    protected static EntityManager current = null;
    
    public static void init() throws Exception{
    	if(emf != null) {
    		throw new Exception("Entity factory já foi inicializada.");
    	}
        emf = Persistence.createEntityManagerFactory("catracaUnit");
//        current = Factory.emf.createEntityManager();
    }
    
//    protected EntityManager getCurrentEntityManager(){
//        return current;
//    }
//    
//    protected void closeCurrentEntityManager(){
//        current.clear();
//        current.flush();
//        current = Factory.emf.createEntityManager();
//    }
    
    protected EntityManager getEntityManager(){
        return Factory.emf.createEntityManager();
    }
    
    public void run() {
    	try {
			Factory.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
//    @SuppressWarnings("unused")
//	private Session getCurrentSession() {
//        Map<String, String> settings = new HashMap<>();
//        settings.put("connection.driver_class", "org.sqlite.JDBC");
//        settings.put("hibernate.dialect", "org.hibernate.dialect.SQLiteDialect");
//        settings.put("hibernate.connection.url","jdbc:sqlite:/home/qwerty/git/iffar_java/catraca.db");
//        settings.put("hibernate.connection.username", "");
//        settings.put("hibernate.connection.password", "");
////        settings.put("hibernate.current_session_context_class", "thread");
//        settings.put("hibernate.show_sql", "true");
//        settings.put("hibernate.format_sql", "true");
//
//        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(settings).build();
//
//        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
//        // metadataSources.addAnnotatedClass(Player.class);
//        Metadata metadata = metadataSources.buildMetadata();
//
//        // here we build the SessionFactory (Hibernate 5.4)
//        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
//        Session session = sessionFactory.getCurrentSession();
//        return session;
//    }
//    
//    private Connection getConection() throws SQLException {
//        return DriverManager.getConnection("jdbc:sqlite:/home/qwerty/git/iffar_java/catraca.db");
//    }
//
//    private Statement getStatement() throws SQLException {
//        Statement statement = this.getConection().createStatement();
//        statement.setQueryTimeout(30);
//        return statement;
//    }
//    
//    private void closeConnection(Connection c) {
//        try {
//            if (c != null && !c.isClosed()) {
//                c.close();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    
}
