package net.javacogito.jdbcconnector.controller;

import net.javacogito.jdbcconnector.entity.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation for table 'product' controller.
 * Contains basic CRUD operations for table 'product'.
 */

public class ProductController extends AbstractController<Product, Integer> {
  private static final String SELECT_ALL_PRODUCTS = "SELECT * FROM product";
  private static final String INSERT_PRODUCT = "INSERT INTO product VALUES (?, ?, ?, ?)";
  private static final String DELETE_PRODUCT = "DELETE FROM product WHERE id = ?";
  private static final String SELECT_PRODUCT_BY_ID = "SELECT * FROM product WHERE id = ?";
  private static final String UPDATE_PRODUCT_BY_ID = "UPDATE product SET name = ?, product_type_id = ?, price = ? WHERE id = ?";
  private static final String CREATE_PRODUCT = "CREATE TABLE product (id INT PRIMARY KEY, name VARCHAR(100), product_type_id INT, price FLOAT)";
  private static final String DROP_PRODUCT = "DROP TABLE IF EXISTS product";
  private static final String SELECT_MAX_ID = "SELECT max(id) FROM product";
  private static final ProductController PRODUCT_CONTROLLER = new ProductController();
  private static final Logger LOG = LogManager.getLogger(ProductController.class);
  private ProductController() {}

  /**
   * Gets Product Controller
   *
   * @return Product Controller
   */
  public static ProductController getProductController() {
    return PRODUCT_CONTROLLER;
  }

  /**
   * Returns all products as list. Executes SELECT * FROM product.
   *
   * @return list of all elements
   */
  @Override public List<Product> getAll() {
    List<Product> products = new LinkedList<>();
    PreparedStatement ps = getPrepareStatement(SELECT_ALL_PRODUCTS);
    try {
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        Product product = new Product();
        product.setId(rs.getInt(1));
        product.setName(rs.getString(2));
        product.setProductTypeId(rs.getInt(3));
        product.setPrice(rs.getFloat(4));
        products.add(product);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closePrepareStatement(ps);
    }
    return products;
  }

  /**
   * Updates an product.
   *
   * @param entity product to update.
   */
  @Override public boolean update(Product entity) {
    PreparedStatement ps = getPrepareStatement(UPDATE_PRODUCT_BY_ID);
    try {
      ps.setString(1, entity.getName());
      ps.setInt(2, entity.getProductTypeId());
      ps.setFloat(3, entity.getPrice());
      ps.setInt(4, entity.getId());
      ps.executeUpdate();
    } catch (SQLException e) {
      return false;
    } finally {
      closePrepareStatement(ps);
    }
    LOG.info(String.format("Entity with id = %d updated to new value %s.", entity.getId(), entity));
    return true;
  }

  /**
   * Returns product by it's id, usually primary key.
   *
   * @param id Id of product
   * @return result product.
   */
  @Override public Product getEntityById(Integer id) {
    PreparedStatement ps = getPrepareStatement(SELECT_PRODUCT_BY_ID);
    Product product = new Product();
    try {
      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        product.setId(rs.getInt(1));
        product.setName(rs.getString(2));
        product.setProductTypeId(rs.getInt(3));
        product.setPrice(rs.getFloat(4));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closePrepareStatement(ps);
    }
    LOG.info(String.format("Found entity %s.", product));
    return product;
  }

  /**
   * Deletes product by id.
   *
   * @param id Id of product to delete
   * @return true of deletion completes successfully.
   */
  @Override public boolean delete(Integer id) {
    PreparedStatement ps = getPrepareStatement(DELETE_PRODUCT);
    try {
      ps.setInt(1, id);
      ps.executeUpdate();
    } catch (SQLException e) {
      return false;
    } finally {
      closePrepareStatement(ps);
    }
    LOG.info(String.format("Entity with id = %d deleted", id));
    return true;
  }

  /**
   * Inserts new product.
   *
   * @param entity product to create.
   * @return true of creation completes successfully.
   */
  @Override public boolean insert(Product entity) {
    PreparedStatement ps = getPrepareStatement(INSERT_PRODUCT);
    try {
      ps.setInt(1, entity.getId());
      ps.setString(2, entity.getName());
      ps.setInt(3, entity.getProductTypeId());
      ps.setFloat(4, entity.getPrice());
      ps.executeUpdate();
    } catch (SQLException e) {
      return false;
    } finally {
      closePrepareStatement(ps);
    }
    LOG.info(String.format("Entity %s inserted", entity));
    return true;
  }

  /**
   * Creates table for product.
   *
   * @return true if creation is successful
   */
  @Override public boolean create() {
    PreparedStatement ps = getPrepareStatement(CREATE_PRODUCT);
    try {
      ps.executeUpdate();
    } catch (SQLException e) {
      return false;
    } finally {
      closePrepareStatement(ps);
    }
    LOG.info("Table for entity is created");
    return true;
  }

  /**
   * Drops table with product.
   *
   * @return true if deletion is successful
   */
  @Override public boolean drop() {
    PreparedStatement ps = getPrepareStatement(DROP_PRODUCT);
    try {
      ps.executeUpdate();
    } catch (SQLException e) {
      return false;
    } finally {
      closePrepareStatement(ps);
    }
    LOG.info("Table for entity is dropped");
    return true;
  }
  /**
   * Returns last id of entity in database.
   *
   * @return last id of entity in database
   */
  @Override public Integer getLastId() {
    return getLastIdAsInteger();
  }

  /**
   * Returns next available id of entity in database.
   *
   * @return next available id of entity in database
   */
  @Override public Integer getNextId() {
    return getNextIdAsInteger();
  }

  /**
   * Gets query for selecting of id if an entity.
   *
   * @return query for selecting of id if an entity
   */
  @Override protected String getMaxIdQuery(){
    return SELECT_MAX_ID;
  }
}
