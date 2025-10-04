<%@ page contentType="text/html;charset=UTF-8" %> 
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Iniciar Sesión</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600&display=swap" rel="stylesheet">

    <style>
        body {
            margin: 0;
            font-family: 'Montserrat', sans-serif;
            background: #F5E6D8;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .login-container {
            background: white;
            border-radius: 15px;
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
            padding: 40px 35px;
            max-width: 420px;
            width: 100%;
        }

        .form-box h2 {
            margin-bottom: 25px;
            color: #2A1E13;
            text-align: center;
        }

        input[type="text"],
        input[type="email"] {
            width: 100%;
            padding: 12px 15px;
            margin-bottom: 18px;
            border: 1px solid #ccc;
            border-radius: 10px;
            font-size: 14px;
        }

        button {
            width: 100%;
            padding: 12px;
            background-color: #2A1E13;
            color: #fff;
            border: none;
            border-radius: 10px;
            font-size: 16px;
            cursor: pointer;
        }

        .form-box a {
            color: #c2955d;
            text-decoration: none;
            font-weight: bold;
        }

        .form-box a:hover {
            text-decoration: underline;
        }

        .error {
            color: #e74c3c;
            background-color: #fceaea;
            border: 1px solid #e74c3c;
            padding: 10px 15px;
            text-align: center;
            margin-top: 15px;
            border-radius: 6px;
            font-weight: bold;
        }
    </style>
</head>
<body>

    <div class="login-container">
        <div class="form-box">
            <h2>Iniciar Sesión</h2>
            <form action="<%= request.getContextPath() %>/LoginController" method="post">
                <input type="email" name="email" placeholder="Correo electrónico" required>
                <input type="text" name="telefono" placeholder="Teléfono" required>
                <button type="submit">Ingresar</button>
            </form>

            <% String errorParam = request.getParameter("error"); %>
            <% if ("1".equals(errorParam)) { %>
                <div class="error">❌ Credenciales incorrectas. Inténtalo de nuevo.</div>
            <% } %>

            <p style="margin-top: 20px;">
                ¿No tienes cuenta?
                <a href="<%= request.getContextPath() %>/vistas/registro.jsp">Regístrate aquí</a>
            </p>

            <p style="text-align: center; margin-top: 15px;">
                <a href="<%= request.getContextPath() %>/vistas/admin_login.jsp">🔐 Ingresar como Administrador</a>
            </p>
        </div>
    </div>

</body>
</html>
