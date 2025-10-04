package dao;

import model.Producto;
import config.Conexion;

import java.sql.*;
import java.util.*;

public class ProductoDAO {

    public List<Producto> listar() {
    List<Producto> lista = new ArrayList<>();
    String sql = "SELECT p.*, c.nombre AS categoria_nombre " +
                 "FROM producto p " +
                 "JOIN categoria c ON p.id_categoria = c.id_categoria";
    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            Producto p = new Producto();
            p.setIdProducto(rs.getInt("id_producto"));
            p.setNombre(rs.getString("nombre"));
            p.setPrecio(rs.getDouble("precio"));
            p.setUrlImagen(rs.getString("url_imagen"));
            p.setStock(rs.getInt("stock"));
            p.setIdCategoria(rs.getInt("id_categoria"));
            p.setCategoriaNombre(rs.getString("categoria_nombre")); // 👈 aquí
            lista.add(p);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return lista;
}


    public void agregar(Producto p) {
        String sql = "INSERT INTO producto (nombre, precio, url_imagen, stock, id_categoria) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecio());
            ps.setString(3, p.getUrlImagen());
            ps.setInt(4, p.getStock());
            ps.setInt(5, p.getIdCategoria());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Producto obtenerPorId(int id) {
        Producto p = null;
        String sql = "SELECT * FROM producto WHERE id_producto = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                p = new Producto();
                p.setIdProducto(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getDouble("precio"));
                p.setUrlImagen(rs.getString("url_imagen"));
                p.setStock(rs.getInt("stock"));
                p.setIdCategoria(rs.getInt("id_categoria"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return p;
    }

    public void actualizar(Producto p) {
        String sql = "UPDATE producto SET nombre=?, precio=?, url_imagen=?, stock=?, id_categoria=? WHERE id_producto=?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecio());
            ps.setString(3, p.getUrlImagen());
            ps.setInt(4, p.getStock());
            ps.setInt(5, p.getIdCategoria());
            ps.setInt(6, p.getIdProducto());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Producto> buscarPorNombre(String texto) {
    List<Producto> lista = new ArrayList<>();
    String sql = "SELECT p.*, c.nombre AS categoria_nombre " +
                 "FROM producto p " +
                 "JOIN categoria c ON p.id_categoria = c.id_categoria " +
                 "WHERE p.nombre LIKE ?";
    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, "%" + texto + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Producto p = new Producto();
            p.setIdProducto(rs.getInt("id_producto"));
            p.setNombre(rs.getString("nombre"));
            p.setPrecio(rs.getDouble("precio"));
            p.setUrlImagen(rs.getString("url_imagen"));
            p.setStock(rs.getInt("stock"));
            p.setIdCategoria(rs.getInt("id_categoria"));
            p.setCategoriaNombre(rs.getString("categoria_nombre"));
            lista.add(p);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return lista;
}


    public void eliminar(int id) {
        String sql = "DELETE FROM producto WHERE id_producto=?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
