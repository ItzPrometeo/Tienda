<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*,model.Producto, dao.ProductoDAO" %>
<%@ page session="true" %>

<html>
<head>
    <title>Confirmar Compra - Tosta Café</title>
    <style>
    body {
        font-family: "Montserrat", sans-serif;
        background-color: #F5E6D8;
        padding: 40px 20px;
        margin: 0;
    }

    h2 {
        text-align: center;
        color: #3c2b1c;
        margin-bottom: 30px;
        font-size: 32px;
    }

    .card {
        background-color: #fff;
        border-left: 6px solid #865c38;
        margin: 15px auto;
        padding: 20px;
        border-radius: 12px;
        box-shadow: 0 4px 12px rgba(60, 43, 28, 0.1);
        max-width: 500px;
        transition: transform 0.2s ease, box-shadow 0.3s ease;
    }

    .card:hover {
        transform: scale(1.01);
        box-shadow: 0 6px 18px rgba(60, 43, 28, 0.25);
    }

    .card strong {
        font-size: 20px;
        color: #3c2b1c;
    }

    .card p, .card span {
        margin-top: 8px;
        font-size: 16px;
        color: #3c2b1c;
    }

    h3 {
        text-align: center;
        margin-top: 30px;
        color: #865c38;
        font-size: 24px;
    }

    form {
        background-color: #D4C4A8;
        max-width: 500px;
        margin: 30px auto;
        padding: 20px;
        border-radius: 15px;
        box-shadow: 0 4px 10px rgba(0,0,0,0.1);
    }

    label {
        font-weight: bold;
        color: #3c2b1c;
        display: block;
        margin-bottom: 10px;
        font-size: 16px;
    }

    select {
        width: 100%;
        padding: 10px;
        border-radius: 8px;
        border: 1px solid #865c38;
        font-size: 16px;
        background-color: #fff;
        margin-bottom: 20px;
    }

    .btn {
        background-color: #3c2b1c;
        color: white;
        padding: 12px 20px;
        border: none;
        border-radius: 50px;
        font-size: 16px;
        cursor: pointer;
        width: 100%;
        transition: background-color 0.3s ease;
    }

    .btn:hover {
        background-color: #2a1e13;
    }

    .mensaje-vacio {
        text-align: center;
        font-size: 18px;
        color: #865c38;
        margin-top: 50px;
    }
</style>

</head>
<body>

<h2>Resumen del Pedido</h2>

<%
    Map<Integer, Integer> carrito = (Map<Integer, Integer>) session.getAttribute("carrito");
    if (carrito != null && !carrito.isEmpty()) {
        ProductoDAO dao = new ProductoDAO();
        double total = 0;

        for (Map.Entry<Integer, Integer> entry : carrito.entrySet()) {
            int idProd = entry.getKey();
            int cantidad = entry.getValue();
            Producto prod = dao.obtenerPorId(idProd);

            if (prod != null) {
                double subtotal = prod.getPrecio() * cantidad;
                total += subtotal;
%>
        <div class="card">
            <strong><%= prod.getNombre() %></strong><br>
            Cantidad: <%= cantidad %><br>
            Subtotal: S/. <%= subtotal %>
        </div>
<%
            }
        }
%>
    <h3>Total a pagar: S/. <%= total %></h3>

    <!-- Formulario de modalidad y botón de pago -->
    <form action="iniciarPago.jsp" method="post">
        <label for="modalidad">Modalidad de entrega:</label><br>
        <select name="modalidad" required>
            <option value="LOCAL">Recojo en local (15 min)</option>
            <option value="DELIVERY">Delivery (30 - 45 min)</option>
        </select><br><br>
        <input type="hidden" name="total" value="<%= total %>">
        <button type="submit" class="btn">Pagar con Izipay</button>
    </form>

<%
    } else {
        out.print("<p>No hay productos en el carrito.</p>");
    }
%>

</body>
</html>
