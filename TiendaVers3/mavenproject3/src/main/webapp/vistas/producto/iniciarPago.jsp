<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<%@ page import="java.text.DecimalFormat" %>

<html>
    <head>
        <title>Pagar con PayPal - Tosta Café</title>
        <style>
            body {
                font-family: "Montserrat", sans-serif;
                background-color: #F5E6D8;
                display: flex;
                flex-direction: column;
                align-items: center;
                padding: 60px 20px;
            }

            h2 {
                color: #3c2b1c;
                font-size: 30px;
                margin-bottom: 20px;
            }

            p {
                font-size: 22px;
                color: #865c38;
                margin-bottom: 40px;
            }

            .mensaje {
                background-color: #fff;
                padding: 20px 30px;
                border-left: 6px solid #865c38;
                border-radius: 12px;
                margin-bottom: 30px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                font-size: 18px;
            }

            .btn-pago {
                background-color: #3c2b1c;
                color: #fff;
                padding: 14px 24px;
                font-size: 16px;
                border: none;
                border-radius: 50px;
                cursor: pointer;
                transition: background-color 0.3s ease;
                width: 100%;
                max-width: 300px;
            }

            .btn-pago:hover {
                background-color: #2a1e13;
            }
        </style>
    </head>
    <body>

        <%
            String modalidad = request.getParameter("modalidad");
            String totalParam = request.getParameter("total");
            double total = 0.0;

            try {
                total = Double.parseDouble(totalParam);
            } catch (NumberFormatException e) {
                response.sendRedirect("errorPago.jsp");
                return;
            }

            session.setAttribute("modalidad", modalidad);
            session.setAttribute("monto_total", total);

            DecimalFormat df = new DecimalFormat("0.00");
        %>

        <h2>Pagar con PayPal</h2>

        <div class="mensaje">
            Modalidad de entrega: <strong><%= modalidad.equals("DELIVERY") ? "Delivery" : "Recojo en local"%></strong><br>
            Monto a pagar: <strong>S/ <%= df.format(total)%></strong>
        </div>

        <form action="<%= request.getContextPath()%>/CrearOrdenPayPalServlet" method="post">
            <input type="hidden" name="monto" value="<%= total%>">
            <input type="hidden" name="modalidad" value="${sessionScope.modalidad}">
            <button type="submit" class="btn-pago">Pagar con PayPal</button>
        </form>

    </body>
</html>
