<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Cliente" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Confirmación de Pago</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
    <style>
        body {
            font-family: "Montserrat", sans-serif;
            background-color: #F5E6D8;
            padding: 100px 20px 40px;
            color: #2a1e13;
            text-align: center;
        }
        .container {
            max-width: 700px;
            margin: auto;
            background-color: #fff;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0px 4px 10px rgba(42, 30, 19, 0.3);
        }
        .btn {
            display: inline-block;
            margin-top: 20px;
            background-color: #3c2b1c;
            color: #fff;
            padding: 12px 24px;
            border: none;
            border-radius: 50px;
            text-decoration: none;
            transition: background-color 0.3s ease;
        }
        .btn:hover {
            background-color: #2a1e13;
        }
        .resumen {
            margin-top: 20px;
            font-size: 18px;
            line-height: 1.6;
        }
    </style>
</head>
<body>

<%
    String estado = request.getParameter("estado_pago");
    String modalidad = request.getParameter("modalidad");
    String totalStr = request.getParameter("total");

    double total = 0.0;
    try {
        total = Double.parseDouble(totalStr);
    } catch (Exception e) {
        total = 0.0;
    }

    Cliente cliente = (Cliente) session.getAttribute("cliente");
%>

<% if (cliente == null) { %>
    <div class="container">
        <h2>Error: Sesión inválida</h2>
        <p>Por favor, inicia sesión y vuelve a realizar la compra.</p>
        <a class="btn" href="<%= request.getContextPath() %>/login.jsp">Ir a Login</a>
    </div>
<% } else if ("ok".equals(estado)) { %>
    <div class="container">
        <h2>¡Gracias por tu compra, <%= cliente.getNombreCompleto() %>!</h2>
        <div class="resumen">
            <p>Tu pedido fue registrado por <strong>S/. <%= total %></strong>.</p>
            <% if ("LOCAL".equalsIgnoreCase(modalidad)) { %>
                <p>📍 Estará listo para recojo en 15 minutos en nuestra tienda.</p>
            <% } else { %>
                <p>🚚 Tu pedido llegará entre 30 a 45 minutos vía delivery.</p>
            <% } %>
        </div>
        <a class="btn" href="<%= request.getContextPath() %>/ProductoControlle">Volver a comprar</a>
    </div>
<% } else { %>
    <div class="container">
        <h2>⚠️ Error en el pago</h2>
        <p>Tu transacción no fue confirmada. Inténtalo de nuevo.</p>
        <a class="btn" href="<%= request.getContextPath() %>/ProductoControlle">Volver</a>
    </div>
<% } %>

</body>
</html>
