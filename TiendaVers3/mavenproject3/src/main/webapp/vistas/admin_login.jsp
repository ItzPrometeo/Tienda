<%@ page contentType="text/html;charset=UTF-8" %> 
<!DOCTYPE html>
<html>
<head>
    <title>Acceso Administrador</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
    <style>
        body {
            margin: 0;
            padding: 0;
            background-color: #fff;
            font-family: 'Montserrat', sans-serif;
        }

        .login-container {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background: linear-gradient(to right, #F5E6D8, #FFECD2);
        }

        .form-box {
            background-color: #fff;
            padding: 3rem;
            border-radius: 15px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
            max-width: 400px;
            width: 100%;
            text-align: center;
        }

        .form-box h2 {
            color: #2a1e13;
            margin-bottom: 1.5rem;
        }

        .form-box input[type="email"],
        .form-box input[type="password"] {
            width: 100%;
            padding: 12px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 10px;
            font-size: 16px;
        }

        .form-box button {
            background-color: #2a1e13;
            color: white;
            padding: 12px 25px;
            font-size: 16px;
            border: none;
            border-radius: 10px;
            cursor: pointer;
            transition: background 0.3s;
        }

        .form-box button:hover {
            background-color: #5a3c25;
        }

        .error {
            background-color: #fceaea;
            border: 1px solid #e74c3c;
            color: #e74c3c;
            padding: 10px;
            border-radius: 6px;
            margin-top: 15px;
            font-weight: bold;
        }

        .volver {
            display: block;
            margin-top: 20px;
            color: #2a1e13;
            text-decoration: none;
        }

        .volver:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <div class="form-box">
            <h2>Acceso Administrador</h2>

            <form action="<%= request.getContextPath() %>/LoginController" method="post">
                <input type="hidden" name="tipo" value="admin">
                <input type="email" name="email" placeholder="Correo electrónico" required>
                <input type="password" name="contrasena" placeholder="Contraseña" required>
                <button type="submit">Ingresar</button>
            </form>

            <% if (request.getAttribute("error") != null) { %>
                <div class="error">❌ <%= request.getAttribute("error") %></div>
            <% } %>

            <a class="volver" href="<%= request.getContextPath() %>/vistas/login.jsp">← Volver al login de cliente</a>
        </div>
    </div>
</body>
</html>
