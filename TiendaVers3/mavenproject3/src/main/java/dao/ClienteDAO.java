package dao;

import model.Cliente;
import config.Conexion;

import java.sql.*;

public class ClienteDAO {

    public Cliente obtenerPorEmailYTelefono(String email, String telefono) {
        Cliente cliente = null;

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM cliente WHERE email = ? AND telefono = ?")
        ) {
            ps.setString(1, email);
            ps.setString(2, telefono);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("id_cliente"));
                cliente.setNombreCompleto(rs.getString("nombre_completo"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setEmail(rs.getString("email"));
                cliente.setDireccion(rs.getString("direccion"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cliente;
    }
    
    public boolean actualizar(Cliente cliente) {
    String sql = "UPDATE cliente SET nombre_completo=?, telefono=?, email=?, direccion=? WHERE id_cliente=?";
    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, cliente.getNombreCompleto());
        ps.setString(2, cliente.getTelefono());
        ps.setString(3, cliente.getEmail());
        ps.setString(4, cliente.getDireccion());
        ps.setInt(5, cliente.getIdCliente());

        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
    
    public boolean registrar(Cliente cliente) {
    String sql = "INSERT INTO cliente (nombre_completo, telefono, email, direccion) VALUES (?, ?, ?, ?)";
    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, cliente.getNombreCompleto());
        ps.setString(2, cliente.getTelefono());
        ps.setString(3, cliente.getEmail());
        ps.setString(4, cliente.getDireccion());
        return ps.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
}
