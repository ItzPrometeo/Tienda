<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.Usuario" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null || !"ADMIN".equals(usuario.getRol())) {
        response.sendRedirect(request.getContextPath() + "/vistas/login_admin.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Dashboard Admin | Tosta Coffee</title>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700&display=swap" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        html, body {
            height: 100%;
            font-family: 'Montserrat', sans-serif;
            display: flex;
            flex-direction: column;
            background-color: #f9f5f1;
        }

        header {
            background-color: #3c2b1c;
            color: white;
            padding: 15px 40px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 6px rgba(0,0,0,0.2);
            position: sticky;
            top: 0;
            z-index: 100;
        }

        header h1 {
            font-size: 22px;
            font-weight: 700;
            margin: 0;
        }

        header a {
            color: #fff;
            text-decoration: none;
            font-weight: 600;
            background-color: #8d6748;
            padding: 10px 18px;
            border-radius: 8px;
            transition: background-color 0.3s ease;
        }

        header a:hover {
            background-color: #a47b56;
        }

        .container {
            flex: 1;
            padding: 30px 40px;
            max-width: 1400px;
            width: 100%;
            margin: auto;
        }

        .dashboard-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
            gap: 30px;
            justify-content: center;
        }

        .card {
            background-color: #fff;
            padding: 30px 20px;
            border-radius: 18px;
            text-align: center;
            box-shadow: 0 6px 12px rgba(0,0,0,0.1);
            transition: transform 0.2s ease, box-shadow 0.2s ease;
            text-decoration: none;
            color: #3c2b1c;
        }

        .card:hover {
            transform: translateY(-4px);
            box-shadow: 0 12px 20px rgba(0,0,0,0.15);
            background-color: #fef9f6;
        }

        .card i {
            font-size: 38px;
            margin-bottom: 12px;
            display: block;
        }

        .card h2 {
            font-size: 20px;
            margin-bottom: 10px;
        }

        .card p {
            font-size: 14px;
            color: #5e4c3c;
        }

        footer {
            background-color: #F5E6D8;
            text-align: center;
            padding: 1rem;
            font-size: 14px;
            color: #3c2b1c;
            box-shadow: 0 -2px 8px rgba(0,0,0,0.1);
            margin-top: auto;
        }

        @media (max-width: 768px) {
            header h1 {
                font-size: 18px;
            }

            header a {
                padding: 8px 14px;
                font-size: 14px;
            }

            .card {
                padding: 25px 15px;
            }

            .card h2 {
                font-size: 18px;
            }

            .card i {
                font-size: 32px;
            }
        }
    </style>
</head>
<body>

<header>
    <h1>☕ Tosta Coffee</h1>
    <a href="${pageContext.request.contextPath}/LogoutController">Cerrar sesión</a>
</header>

<main class="container">
    <div class="dashboard-grid">
        <a href="${pageContext.request.contextPath}/GestionPedidosServlet" class="card">
            <i>📦</i>
            <h2>Gestión de Pedidos</h2>
            <p>Administra pedidos activos y entregados.</p>
        </a>
        <a href="gestion_boletas.jsp" class="card">
            <i>🧾</i>
            <h2>Gestión de Boletas</h2>
            <p>Genera y revisa comprobantes de pago.</p>
        </a>
        <a href="gestion_productos.jsp" class="card">
            <i>🍰</i>
            <h2>Gestión de Productos</h2>
            <p>Administra el menú de productos disponibles.</p>
        </a>
        <a href="gestion_clientes.jsp" class="card">
            <i>👤</i>
            <h2>Gestión de Clientes</h2>
            <p>Consulta, edita o elimina datos de clientes.</p>
        </a>
        <a href="gestion_empleados.jsp" class="card">
            <i>👥</i>
            <h2>Gestión de Empleados</h2>
            <p>Gestiona personal administrativo y operario.</p>
        </a>
        <a href="gestion_inventario.jsp" class="card">
            <i>📦</i>
            <h2>Gestión de Inventario</h2>
            <p>Controla insumos y stock de productos.</p>
        </a>
        <a href="estadisticas.jsp" class="card">
            <i>📊</i>
            <h2>Estadísticas</h2>
            <p>Analiza gráficas de ventas y productividad.</p>
        </a>
    </div>
</main>

<footer>
    &copy; 2025 Tosta Coffee. Todos los derechos reservados.
</footer>

</body>
</html>
