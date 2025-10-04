<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, model.Pedido" %>
<%
    List<Pedido>[] secciones = (List<Pedido>[]) request.getAttribute("secciones");
    String[] titulos = (String[]) request.getAttribute("titulos");
    String[] clases = (String[]) request.getAttribute("clases");
    Pedido pedidoBuscado = (Pedido) request.getAttribute("pedidoBuscado");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestión de Pedidos - Tosta Coffee</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #fdfaf6;
        }
        header, footer {
            background-color: #4e342e;
            color: white;
        }
        .btn-volver {
            background-color: #6d4c41;
            color: white;
        }
        .btn-volver:hover {
            background-color: #5d4037;
        }
        .seccion {
            margin-bottom: 50px;
        }
        .pedido-card {
            background-color: #ffffff;
            border-radius: 12px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            padding: 15px;
        }
        .estado-titulo {
            font-size: 1.3rem;
            font-weight: bold;
        }
    </style>
</head>
<body>

<header class="p-3 mb-4">
    <div class="container d-flex justify-content-between align-items-center">
        <h1 class="h3 mb-0">☕ Tosta Coffee - Gestión de Pedidos</h1>
        <a href="vistas/trabajador/panelControl.jsp" class="btn btn-volver">Volver al Panel</a>
    </div>
</header>

<div class="container">

    <!-- Buscador por ID -->
    <form class="mb-4 d-flex" method="get" action="GestionPedidosServlet">
        <input type="number" class="form-control me-2" name="idPedido" placeholder="Buscar pedido por ID..." required>
        <button type="submit" class="btn btn-dark">Buscar</button>
    </form>

    <!-- Resultado del buscador -->
    <% if (pedidoBuscado != null) { %>
        <div class="card mb-4 border-warning pedido-card">
            <div class="card-body">
                <h5 class="card-title">Pedido encontrado: #<%= pedidoBuscado.getIdPedido() %></h5>
                <p><strong>Cliente:</strong> <%= pedidoBuscado.getNombreCliente() %></p>
                <p><strong>Fecha:</strong> <%= pedidoBuscado.getFechaPedido() %></p>
                <p><strong>Monto:</strong> S/ <%= pedidoBuscado.getMontoTotal() %></p>
                <p><strong>Estado:</strong> <%= pedidoBuscado.getEstado() %></p>

                <!-- Botón de cambio al siguiente estado -->
                <form action="EstadoPedidoController" method="post">
                    <input type="hidden" name="idPedido" value="<%= pedidoBuscado.getIdPedido() %>">
                    <div class="btn-group mt-2">
                        <% String estado = pedidoBuscado.getEstado(); %>
                        <% if ("RECIBIDO".equals(estado)) { %>
                            <button type="submit" name="nuevoEstado" value="EN_PREPARACION" class="btn btn-warning btn-sm">Pasar a PREPARACIÓN</button>
                        <% } else if ("EN_PREPARACION".equals(estado)) { %>
                            <button type="submit" name="nuevoEstado" value="EN_CAMINO" class="btn btn-info btn-sm">Pasar a EN CAMINO</button>
                        <% } else if ("EN_CAMINO".equals(estado)) { %>
                            <button type="submit" name="nuevoEstado" value="ENTREGADO" class="btn btn-success btn-sm">Marcar como ENTREGADO</button>
                        <% } else { %>
                            <span class="text-success">Pedido entregado ✅</span>
                        <% } %>
                    </div>
                </form>
            </div>
        </div>
    <% } %>

    <!-- Secciones por estado -->
    <div class="row">
        <% for (int i = 0; i < secciones.length; i++) { %>
            <div class="col-12 seccion">
                <h2 class="estado-titulo text-<%= clases[i].replace("border-", "") %>"><%= titulos[i] %></h2>
                <div class="row">
                    <% List<Pedido> pedidos = secciones[i]; %>
                    <% if (pedidos.isEmpty()) { %>
                        <p class="text-muted">No hay pedidos en esta sección.</p>
                    <% } else { %>
                        <% for (Pedido p : pedidos) { %>
                            <div class="col-md-6 col-lg-4 mb-4">
                                <div class="pedido-card border <%= clases[i] %>">
                                    <p><strong>ID:</strong> <%= p.getIdPedido() %></p>
                                    <p><strong>Cliente:</strong> <%= p.getNombreCliente() %></p>
                                    <p><strong>Fecha:</strong> <%= p.getFechaPedido() %></p>
                                    <p><strong>Monto:</strong> S/ <%= p.getMontoTotal() %></p>
                                    <p><strong>Estado:</strong> <%= p.getEstado() %></p>

                                    <form action="EstadoPedidoController" method="post">
                                        <input type="hidden" name="idPedido" value="<%= p.getIdPedido() %>">
                                        <% String estado = p.getEstado(); %>
                                        <% if ("RECIBIDO".equals(estado)) { %>
                                            <button type="submit" name="nuevoEstado" value="EN_PREPARACION" class="btn btn-warning btn-sm">Pasar a PREPARACIÓN</button>
                                        <% } else if ("EN_PREPARACION".equals(estado)) { %>
                                            <button type="submit" name="nuevoEstado" value="EN_CAMINO" class="btn btn-info btn-sm">Pasar a EN CAMINO</button>
                                        <% } else if ("EN_CAMINO".equals(estado)) { %>
                                            <button type="submit" name="nuevoEstado" value="ENTREGADO" class="btn btn-success btn-sm">Marcar como ENTREGADO</button>
                                        <% } else { %>
                                            <span class="text-success">✅ Entregado</span>
                                        <% } %>
                                    </form>
                                </div>
                            </div>
                        <% } %>
                    <% } %>
                </div>
            </div>
        <% } %>
    </div>

</div>

<footer class="text-center p-3 mt-5">
    <p class="mb-0">&copy; 2025 Tosta Coffee. Todos los derechos reservados.</p>
</footer>

</body>
</html>
