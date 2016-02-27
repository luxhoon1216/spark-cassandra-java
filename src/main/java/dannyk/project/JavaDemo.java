package dannyk.project;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;

import com.datastax.driver.core.Session;
import com.datastax.spark.connector.cql.CassandraConnector;
import com.datastax.spark.connector.japi.CassandraJavaUtil;

import dannyk.project.model.Product;

public class JavaDemo implements Serializable {
  private transient SparkConf conf;

  private JavaDemo(SparkConf conf) {
    this.conf = conf;
  }

  private void run() {
    JavaSparkContext sc = new JavaSparkContext(conf);
    generateData(sc);
    //compute(sc);
    //showResults(sc);
    sc.stop();
  }

  private void generateData(JavaSparkContext sc) {
    CassandraConnector connector = CassandraConnector.apply(sc.getConf());

    // Prepare the schema
    Session session = null;
    try {
      session = connector.openSession();
      session.execute("DROP KEYSPACE IF EXISTS java_api");
      session
          .execute("CREATE KEYSPACE java_api WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1}");
      session.execute("CREATE TABLE java_api.products (id INT, name TEXT, age INT, PRIMARY KEY (age))");
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    finally {
      if (session != null) {
        try {
          session.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    // Prepare the products hierarchy
    Random random = new Random();
    List<Product> products = new ArrayList<Product>();
    for(int i = 1; i < 10000; i++) {
    	Product p = new Product(i, i + "", random.nextInt(100));
    	products.add(p);
    }

    JavaRDD<Product> productsRDD = sc.parallelize(products);
    CassandraJavaUtil.javaFunctions(productsRDD)
        .writerBuilder("java_api", "products", CassandraJavaUtil.mapToRow(Product.class)).saveToCassandra();

//    JavaRDD<Sale> salesRDD = productsRDD.filter(new Function<Product, Boolean>() {
//      public Boolean call(Product product) throws Exception {
//        return product.getParents().size() == 2;
//      }
//    }).flatMap(new FlatMapFunction<Product, Sale>() {
//      public Iterable<Sale> call(Product product) throws Exception {
//        Random random = new Random();
//        List<Sale> sales = new ArrayList<Sale>(1000);
//        for (int i = 0; i < 1000; i++) {
//          sales.add(new Sale(UUID.randomUUID(), product.getId(), BigDecimal.valueOf(random.nextDouble())));
//        }
//        return sales;
//      }
//    });
//
//    CassandraJavaUtil.javaFunctions(salesRDD).writerBuilder("java_api", "sales", CassandraJavaUtil.mapToRow(Sale.class))
//        .saveToCassandra();
  }

  public static void main(String[] args) {
    if (args.length != 2) {
      System.err.println("Syntax: com.datastax.spark.demo.JavaDemo <Spark Master URL> <Cassandra contact point>");
      System.exit(1);
    }

    SparkConf conf = new SparkConf();
    conf.setAppName("Java API demo");
    conf.setMaster(args[0]);
    conf.set("spark.cassandra.connection.host", args[1]);

    JavaDemo app = new JavaDemo(conf);
    app.run();
  }
}
