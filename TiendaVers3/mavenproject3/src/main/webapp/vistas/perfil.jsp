<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="model.Cliente" %>
<%@ page import="java.util.List" %>
<%@ page import="dao.PedidoDAO, model.Pedido, model.DetallePedido" %>
<%@ page session="true" %>

<%
    Cliente cliente = (Cliente) session.getAttribute("cliente");
    if (cliente == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    PedidoDAO pedidoDAO = new PedidoDAO();
    List<Pedido> pedidosEnProceso = pedidoDAO.obtenerPedidosEnProceso(cliente.getIdCliente());
    List<Pedido> historial = pedidoDAO.obtenerHistorialPedidos(cliente.getIdCliente());

    java.util.Collections.reverse(pedidosEnProceso);
    java.util.Collections.reverse(historial);
%>

<!DOCTYPE html>
<html lang="es">
    <%@ include file="head.jsp" %>
<head>
    <meta charset="UTF-8">
    <title>Perfil de Usuario - Tosta Café</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../css/header.css"/>
    <link rel="stylesheet" href="../css/footer.css"/>
    <style>
        html, body {
            height: 100%;
            margin: 0;
            font-family: "Montserrat", sans-serif;
            background-color: #f4f1ec;
            display: flex;
            flex-direction: column;
        }

        body {
            overflow-y: auto;
        }

        header {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100px;
            background-color: white;
            z-index: 10;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        main {
            flex: 1;
            padding-top: 110px; /* espacio debajo del header */
            padding-bottom: 150px; /* espacio para footer */
        }

        .perfil-container {
            display: flex;
            flex-wrap: wrap;
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
            background-color: #fffefc;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            border-radius: 12px;
        }

        .perfil-menu {
            width: 100%;
            max-width: 250px;
            border-right: 1px solid #ddd;
        }

        .perfil-menu ul {
            list-style: none;
            padding: 0;
            margin: 0;
        }

        .perfil-menu li {
            margin-bottom: 1rem;
        }

        .perfil-menu a {
            text-decoration: none;
            color: #333;
            font-weight: bold;
            display: block;
            padding: 10px;
            border-radius: 6px;
            transition: background-color 0.3s;
        }

        .perfil-menu a:hover, .perfil-menu a.active {
            background-color: #f0e4d6;
        }

        .perfil-content {
            flex: 1;
            padding: 20px;
        }

        .seccion-perfil {
            display: none;
        }

        .seccion-perfil.active {
            display: block;
        }

        .perfil-label {
            font-weight: bold;
            color: #3c2b1c;
        }

        .pedido {
            background-color: #fff;
            border-left: 6px solid #865c38;
            padding: 20px;
            margin-bottom: 20px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(60, 43, 28, 0.1);
            transition: transform 0.2s ease, box-shadow 0.3s ease;
        }

        .pedido:hover {
            transform: scale(1.01);
            box-shadow: 0 6px 18px rgba(60, 43, 28, 0.25);
        }

        .pedido p, .pedido ul li {
            margin: 6px 0;
            font-size: 16px;
            color: #3c2b1c;
        }

        .pedido strong {
            color: #865c38;
        }

        .pedido ul {
            list-style: none;
            padding-left: 0;
            margin-top: 10px;
        }

        .pedido ul li {
            background-color: #F5E6D8;
            padding: 10px;
            margin-bottom: 6px;
            border-left: 4px solid #3c2b1c;
            border-radius: 6px;
        }

        .estado-etiqueta {
            padding: 6px 10px;
            border-radius: 6px;
            font-weight: bold;
            color: white;
            font-size: 0.9rem;
            display: inline-block;
        }

        .estado-RECIBIDO       { background-color: #444; }
        .estado-EN_PREPARACION { background-color: #ff9800; }
        .estado-EN_CAMINO      { background-color: #2196f3; }
        .estado-ENTREGADO      { background-color: #4caf50; }
        .estado-esperando      { background-color: #ccc; color: black; }
        .estado-pasado         { background-color: #9e9e9e; }

        .form-modificar {
            display: flex;
            flex-direction: column;
            gap: 15px;
            max-width: 400px;
        }

        .form-modificar input {
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 8px;
            font-size: 16px;
        }

        .btn-guardar {
            background-color: #3c2b1c;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-weight: bold;
            transition: background-color 0.3s;
        }

        .btn-guardar:hover {
            background-color: #2a1e13;
        }

        .footer {
            background-color: #F5E6D8;
            padding: 2rem;
            color: #000;
            font-family: "Montserrat", sans-serif;
            box-shadow: 0 -2px 10px #2a1e13;
            text-align: center;
        }

        @media (max-width: 768px) {
            .perfil-container {
                flex-direction: column;
            }

            .perfil-menu {
                width: 100%;
                border-right: none;
                margin-bottom: 20px;
            }
        }
    </style>
</head>
<body>

<%@ include file="header.jsp" %>

<main>
    <div class="perfil-container">
        <div class="perfil-menu">
            <ul>
                <li><a href="#" onclick="mostrarSeccion('info')">👤 Ver perfil</a></li>
                <li><a href="#" onclick="mostrarSeccion('modificar')">✏️ Modificar datos</a></li>
                <li><a href="#" onclick="mostrarSeccion('procesando')">🚚 Pedidos en proceso</a></li>
                <li><a href="#" onclick="mostrarSeccion('historial')">🛒 Historial de compras</a></li>
            </ul>
        </div>

        <div class="perfil-content">
            <div id="info" class="seccion-perfil active">
                <h2>👤 Mi perfil</h2>
                <p><span class="perfil-label">Nombre:</span> <%= cliente.getNombreCompleto() %></p>
                <p><span class="perfil-label">Correo:</span> <%= cliente.getEmail() %></p>
            </div>

            <div id="modificar" class="seccion-perfil">
                <h2>✏️ Modificar datos</h2>
                <form class="form-modificar" action="../ClienteController" method="post">
                    <input type="hidden" name="accion" value="actualizar">
                    <input type="text" name="nombre" value="<%= cliente.getNombreCompleto() %>" placeholder="Nombre completo">
                    <input type="email" name="correo" value="<%= cliente.getEmail() %>" placeholder="Correo electrónico">
                    <input type="text" name="telefono" value="<%= cliente.getTelefono() %>" placeholder="Teléfono">
                    <input type="text" name="direccion" value="<%= cliente.getDireccion() %>" placeholder="Dirección">
                    <button type="submit" class="btn-guardar">Guardar cambios</button>
                </form>
            </div>

            <div id="procesando" class="seccion-perfil">
                <h2>📦 Pedidos en proceso</h2>
                <% if (!pedidosEnProceso.isEmpty()) {
                    for (Pedido pedido : pedidosEnProceso) { %>
                        <div class="pedido">
                            <p><strong>Pedido N°:</strong> <%= pedido.getIdPedido() %></p>
                            <p><strong>Fecha:</strong> <%= pedido.getFechaPedido() %></p>
                            <p><strong>Total:</strong> S/ <%= pedido.getMontoTotal() %></p>
                            <ul>
                                <% for (DetallePedido detalle : pedido.getDetalles()) { %>
                                    <li><%= detalle.getCantidad() %> x <%= detalle.getNombreProducto() %> - S/ <%= detalle.getSubtotal() %></li>
                                <% } %>
                            </ul>
                            <div style="margin-top: 1rem;">
                                <strong>Seguimiento del Pedido:</strong><br>
                                <span class="estado-etiqueta <%= "RECIBIDO".equals(pedido.getEstado()) ? "estado-RECIBIDO" : "estado-pasado" %>">📥 Recibido</span> ➡
                                <span class="estado-etiqueta <%= "EN_PREPARACION".equals(pedido.getEstado()) ? "estado-EN_PREPARACION" : ("EN_PREPARACION".compareTo(pedido.getEstado()) < 0 ? "estado-pasado" : "") %>">🧑‍🍳 En preparación</span>
                                <% if ("DELIVERY".equals(pedido.getModalidad())) { %>
                                    ➡ <span class="estado-etiqueta <%= "EN_CAMINO".equals(pedido.getEstado()) ? "estado-EN_CAMINO" : ("EN_CAMINO".compareTo(pedido.getEstado()) < 0 ? "estado-pasado" : "") %>">🚚 En camino</span>
                                <% } %>
                                ➡ <span class="estado-etiqueta estado-esperando">✅ Entregado</span>
                            </div>
                        </div>
                <% }} else { %>
                    <p>No tienes pedidos en proceso.</p>
                <% } %>
            </div>

            <div id="historial" class="seccion-perfil">
                <h2>🛒 Historial de compras</h2>
                <% if (!historial.isEmpty()) {
                    for (Pedido pedido : historial) { %>
                        <div class="pedido">
                            <p><strong>Pedido N°:</strong> <%= pedido.getIdPedido() %></p>
                            <p><strong>Fecha:</strong> <%= pedido.getFechaPedido() %></p>
                            <p><strong>Total:</strong> S/ <%= pedido.getMontoTotal() %></p>
                            <ul>
                                <% for (DetallePedido detalle : pedido.getDetalles()) { %>
                                    <li><%= detalle.getCantidad() %> x <%= detalle.getNombreProducto() %> - S/ <%= detalle.getSubtotal() %></li>
                                <% } %>
                            </ul>
                            <p><strong>Estado:</strong> <span class="estado-etiqueta estado-ENTREGADO">✅ ENTREGADO</span></p>
                        </div>
                <% }} else { %>
                    <p>No tienes historial de compras.</p>
                <% } %>
            </div>
        </div>
    </div>
</main>

<%@ include file="footer.jsp" %>

<script>
    function mostrarSeccion(id) {
        document.querySelectorAll('.seccion-perfil').forEach(div => div.classList.remove('active'));
        document.getElementById(id).classList.add('active');
    }
</script>
</body>
</html>
