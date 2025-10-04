package dao;

import model.Producto;
import config.Conexion;
import model.DetallePedido;
import model.Pedido;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class PedidoDAO {

    public void registrarPedido(int idCliente, String modalidad, double total, Map<Integer, Integer> carrito, String metodoPago) {

        Connection con = null;
        PreparedStatement psPedido = null, psDetalle = null, psPago = null, psMov = null;
        ResultSet rsPedido = null;

        try {
            con = Conexion.getConexion();
            con.setAutoCommit(false);

            // 1. Insertar pedido
            String sqlPedido = "INSERT INTO pedido (modalidad, estado, monto_total, metodo_pago, id_cliente) VALUES (?, 'RECIBIDO', ?, ?, ?)";
            

            psPedido = con.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            psPedido.setString(1, modalidad);
            psPedido.setDouble(2, total);
            psPedido.setString(3, metodoPago);
            psPedido.setInt(4, idCliente);

            psPedido.executeUpdate();
            rsPedido = psPedido.getGeneratedKeys();
            rsPedido.next();
            int idPedido = rsPedido.getInt(1);

            ProductoDAO productoDAO = new ProductoDAO();

            for (Map.Entry<Integer, Integer> entry : carrito.entrySet()) {
                int idProd = entry.getKey();
                int cantidad = entry.getValue();
                Producto p = productoDAO.obtenerPorId(idProd);

                double precio = p.getPrecio();
                double subtotal = precio * cantidad;

                // 2. Insertar detalle_pedido
                String sqlDetalle = "INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
                psDetalle = con.prepareStatement(sqlDetalle);
                psDetalle.setInt(1, idPedido);
                psDetalle.setInt(2, idProd);
                psDetalle.setInt(3, cantidad);
                psDetalle.setDouble(4, precio);
                psDetalle.setDouble(5, subtotal);
                psDetalle.executeUpdate();

                // 3. Insertar movimiento_inventario (salida)
                String sqlMov = "INSERT INTO movimiento_inventario (id_producto, tipo_movimiento, cantidad, id_pedido) VALUES (?, 'SALIDA', ?, ?)";
                psMov = con.prepareStatement(sqlMov);
                psMov.setInt(1, idProd);
                psMov.setInt(2, cantidad);
                psMov.setInt(3, idPedido);
                psMov.executeUpdate();
            }

            // 4. Insertar pago
            String sqlPago = "INSERT INTO pago (id_pedido, monto) VALUES (?, ?)";
            psPago = con.prepareStatement(sqlPago);
            psPago.setInt(1, idPedido);
            psPago.setDouble(2, total);
            psPago.executeUpdate();

            con.commit();

        } catch (Exception e) {
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            Conexion.cerrar(rsPedido);
            Conexion.cerrar(psPedido);
            Conexion.cerrar(psDetalle);
            Conexion.cerrar(psPago);
            Conexion.cerrar(psMov);
            Conexion.cerrar(con);
        }
    }

    public List<Pedido> obtenerPedidosPorEstado(int idCliente, String estado) {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedido WHERE id_cliente = ? AND estado = ?";

        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            ps.setString(2, estado);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Pedido p = new Pedido();
                p.setIdPedido(rs.getInt("id_pedido"));
                p.setFechaPedido(rs.getDate("fecha_pedido"));
                p.setModalidad(rs.getString("modalidad"));
                p.setEstado(rs.getString("estado"));
                p.setMontoTotal(rs.getDouble("monto_total"));
                p.setMetodoPago(rs.getString("metodo_pago"));
                p.setIdCliente(rs.getInt("id_cliente"));

                p.setDetalles(obtenerDetallesPorPedido(p.getIdPedido())); // ⭐ aquí cargamos los detalles

                lista.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Pedido> obtenerHistorialPedidos(int idCliente) {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedido WHERE id_cliente = ? AND estado IN ('ENTREGADO', 'CANCELADO')";

        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Pedido p = new Pedido();
                p.setIdPedido(rs.getInt("id_pedido"));
                p.setFechaPedido(rs.getDate("fecha_pedido"));
                p.setModalidad(rs.getString("modalidad"));
                p.setEstado(rs.getString("estado"));
                p.setMontoTotal(rs.getDouble("monto_total"));
                p.setMetodoPago(rs.getString("metodo_pago"));
                p.setIdCliente(rs.getInt("id_cliente"));

                p.setDetalles(obtenerDetallesPorPedido(p.getIdPedido())); // ⭐ aquí también

                lista.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<DetallePedido> obtenerDetallesPorPedido(int idPedido) {
        List<DetallePedido> detalles = new ArrayList<>();
        String sql = "SELECT dp.*, p.nombre FROM detalle_pedido dp JOIN producto p ON dp.id_producto = p.id_producto WHERE id_pedido = ?";

        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPedido);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DetallePedido d = new DetallePedido();
                d.setIdPedido(rs.getInt("id_pedido"));
                d.setIdProducto(rs.getInt("id_producto"));
                d.setCantidad(rs.getInt("cantidad"));
                d.setPrecioUnitario(rs.getDouble("precio_unitario"));
                d.setSubtotal(rs.getDouble("subtotal"));
                d.setNombreProducto(rs.getString("nombre")); // Asegúrate que el atributo esté en tu modelo

                detalles.add(d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return detalles;
    }

        public boolean actualizarEstadoPedido(int idPedido, String nuevoEstado) { 
        String sql = "UPDATE pedido SET estado = ? WHERE id_pedido = ?";
        try (Connection con = Conexion.getConexion(); 
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idPedido);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0; // ✅ Devuelve true si al menos una fila fue actualizada

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // ❌ Devuelve false si hay error
        }
    }


    public List<Pedido> listarTodos() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT p.id_pedido, p.fecha_pedido, p.modalidad, p.estado, p.monto_total, p.id_cliente, c.nombre_completo "
                + "FROM pedido p "
                + "JOIN cliente c ON p.id_cliente = c.id_cliente "
                + "ORDER BY p.id_pedido DESC";

        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setIdPedido(rs.getInt("id_pedido"));
                pedido.setFechaPedido(rs.getTimestamp("fecha_pedido"));
                pedido.setModalidad(rs.getString("modalidad"));
                pedido.setEstado(rs.getString("estado"));
                pedido.setMontoTotal(rs.getDouble("monto_total"));
                pedido.setIdCliente(rs.getInt("id_cliente"));

                // Puedes guardar el nombre del cliente si tu modelo Pedido lo tiene
                try {
                    pedido.setNombreCliente(rs.getString("nombre_completo"));
                } catch (Exception e) {
                    // Ignora si no tienes ese atributo
                }

                lista.add(pedido);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Pedido obtenerPorId(int id) {
        Pedido pedido = null;
        String sql = "SELECT * FROM pedido WHERE id_pedido = ?";

        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pedido = new Pedido();
                    pedido.setIdPedido(rs.getInt("id_pedido"));
                    pedido.setFechaPedido(rs.getTimestamp("fecha_pedido"));
                    pedido.setModalidad(rs.getString("modalidad"));
                    pedido.setEstado(rs.getString("estado"));
                    pedido.setMontoTotal(rs.getDouble("monto_total"));
                    pedido.setIdCliente(rs.getInt("id_cliente"));
                    pedido.setMetodoPago(rs.getString("metodo_pago")); // si usas este campo
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedido;
    }

    public List<Pedido> obtenerPedidosEnProceso(int idCliente) {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedido WHERE id_cliente = ? AND estado NOT IN ('ENTREGADO', 'CANCELADO')";

        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Pedido p = new Pedido();
                p.setIdPedido(rs.getInt("id_pedido"));
                p.setFechaPedido(rs.getDate("fecha_pedido"));
                p.setModalidad(rs.getString("modalidad"));
                p.setEstado(rs.getString("estado"));
                p.setMontoTotal(rs.getDouble("monto_total"));
                p.setMetodoPago(rs.getString("metodo_pago"));
                p.setIdCliente(rs.getInt("id_cliente"));

                // Detalles del pedido
                p.setDetalles(obtenerDetallesPorPedido(p.getIdPedido()));

                lista.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Pedido> listarPorEstado(String estado) {
    List<Pedido> lista = new ArrayList<>();
    String sql = "SELECT p.id_pedido, p.fecha_pedido, p.modalidad, p.estado, p.monto_total, p.id_cliente, c.nombre_completo "
               + "FROM pedido p "
               + "JOIN cliente c ON p.id_cliente = c.id_cliente "
               + "WHERE p.estado = ? "
               + "ORDER BY p.id_pedido DESC";

    try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, estado);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Pedido pedido = new Pedido();
            pedido.setIdPedido(rs.getInt("id_pedido"));
            pedido.setFechaPedido(rs.getTimestamp("fecha_pedido"));
            pedido.setModalidad(rs.getString("modalidad"));
            pedido.setEstado(rs.getString("estado"));
            pedido.setMontoTotal(rs.getDouble("monto_total"));
            pedido.setIdCliente(rs.getInt("id_cliente"));
            try {
                pedido.setNombreCliente(rs.getString("nombre_completo"));
            } catch (Exception e) {
                // Ignora si tu modelo no lo tiene
            }
            lista.add(pedido);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return lista;
}
    
   public Pedido buscarPorId(int id) {
    Pedido pedido = null;
    String sql = "SELECT p.id_pedido, p.fecha_pedido, p.modalidad, p.estado, p.monto_total, p.metodo_pago, p.id_cliente, c.nombre_completo " +
                 "FROM pedido p " +
                 "JOIN cliente c ON p.id_cliente = c.id_cliente " +
                 "WHERE p.id_pedido = ?";
    try (Connection conn = Conexion.getConexion();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            pedido = construirPedido(rs);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return pedido;
}

    
    private Pedido construirPedido(ResultSet rs) throws SQLException {
    Pedido pedido = new Pedido();
    pedido.setIdPedido(rs.getInt("id_pedido"));
    pedido.setFechaPedido(rs.getTimestamp("fecha_pedido"));
    pedido.setModalidad(rs.getString("modalidad"));
    pedido.setEstado(rs.getString("estado"));
    pedido.setMontoTotal(rs.getDouble("monto_total"));
    pedido.setMetodoPago(rs.getString("metodo_pago"));
    pedido.setIdCliente(rs.getInt("id_cliente"));
    try {
        pedido.setNombreCliente(rs.getString("nombre_completo")); // si tu modelo lo tiene
    } catch (Exception e) {
        // si no lo tiene, ignora
    }
    return pedido;
}



    
}
