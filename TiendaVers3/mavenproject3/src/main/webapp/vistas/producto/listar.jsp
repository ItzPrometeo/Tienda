<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, model.Producto, dao.ProductoDAO, model.Cliente" %>
<%@ page session="true" %>

<%
    List<Producto> lista = (List<Producto>) request.getAttribute("lista");
    Map<Integer, Integer> carrito = (Map<Integer, Integer>) session.getAttribute("carrito");

    Map<String, List<Producto>> productosPorCategoria = new LinkedHashMap<>();
    if (lista != null) {
        for (Producto p : lista) {
            String categoria = p.getCategoriaNombre();
            productosPorCategoria.computeIfAbsent(categoria, k -> new ArrayList<>()).add(p);
        }
    }
%>

<!DOCTYPE html>
<html lang="es">
    <%@ include file="../head.jsp" %>
<head>
    <meta charset="UTF-8">
    <title>Tosta Café - Productos</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/header.css">
</head>
<body>

<!-- Incluir header -->
<%@ include file="/vistas/header.jsp" %>

<div class="main-container">
    <div class="productos">
        <% for (String categoria : productosPorCategoria.keySet()) { %>
        <div class="categoria-bloque">
            <h2 class="categoria-titulo"><%= categoria %></h2>
            <div class="categoria-productos">
                <% for (Producto p : productosPorCategoria.get(categoria)) { %>
                <div class="producto-card" data-nombre="<%= p.getNombre().toLowerCase() %>">
                    <img class="producto-img" src="<%= request.getContextPath() %>/<%= p.getUrlImagen() %>" alt="<%= p.getNombre() %>">
                    <h3><%= p.getNombre() %></h3>
                    <p>Precio: S/ <%= p.getPrecio() %></p>
                    <form action="<%= request.getContextPath() %>/CarritoControlle" method="post">
                        <input type="hidden" name="accion" value="agregar">
                        <input type="hidden" name="id" value="<%= p.getIdProducto() %>">
                        <input type="number" name="cantidad" value="1" min="1" max="<%= p.getStock() %>">
                        <br>
                        <button type="submit" class="btn">Agregar al carrito</button>
                    </form>
                </div>
                <% } %>
            </div>
        </div>
        <% } %>
    </div>

    <!-- Carrito -->
    <div class="carrito">
        <h2>🛒 Carrito</h2>

        <%
            double total = 0;
            if (carrito != null && !carrito.isEmpty()) {
                ProductoDAO dao = new ProductoDAO();
        %>
        <div class="carrito-scroll">
            <%
                for (Map.Entry<Integer, Integer> entry : carrito.entrySet()) {
                    int idProd = entry.getKey();
                    int cantidad = entry.getValue();
                    Producto prod = dao.obtenerPorId(idProd);
                    if (prod != null) {
                        double subtotal = prod.getPrecio() * cantidad;
                        total += subtotal;
            %>
            <div class="card-carrito">
                <strong><%= prod.getNombre() %></strong><br>
                Cantidad: <%= cantidad %><br>
                Subtotal: S/ <%= subtotal %><br>
                <form action="<%= request.getContextPath() %>/CarritoControlle" method="post">
                    <input type="hidden" name="accion" value="eliminar">
                    <input type="hidden" name="id" value="<%= prod.getIdProducto() %>">
                    <button class="btn btn-red" type="submit">Eliminar</button>
                </form>
            </div>
            <%
                    }
                }
            %>
        </div>

        <div class="carrito-total-fijo">
            <p><strong>Total:</strong> S/ <%= total %></p>
            <%
                Cliente clienteSesion = (Cliente) session.getAttribute("cliente");
                if (clienteSesion != null) {
            %>
            <form action="<%= request.getContextPath() %>/vistas/producto/finalizar.jsp">
                <button class="btn">Finalizar compra</button>
            </form>
            <% } else { %>
            <form action="<%= request.getContextPath() %>/vistas/login.jsp" method="get">
                <button class="btn">Inicia sesión para comprar</button>
            </form>
            <% } %>
        </div>

        <% } else { %>
        <p>El carrito está vacío.</p>
        <% } %>
    </div>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const inputBuscador = document.getElementById("input-buscador");
        const tarjetas = document.querySelectorAll(".producto-card");

        if (inputBuscador) {
            inputBuscador.addEventListener("input", function () {
                const texto = inputBuscador.value.toLowerCase();
                tarjetas.forEach((card) => {
                    const nombre = card.getAttribute("data-nombre");
                    card.style.display = nombre.includes(texto) ? "block" : "none";
                });
            });
        }
    });
</script>

<!-- Incluir footer -->
<%@ include file="/vistas/footer.jsp" %>
</body>
</html>
