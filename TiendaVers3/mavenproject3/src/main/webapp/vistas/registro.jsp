<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrarse</title>
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
            max-width: 450px;
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
            transition: border-color 0.3s;
        }

        input:focus {
            border-color: #2A1E13;
            outline: none;
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
            transition: background-color 0.3s;
        }

        button:hover {
            background-color: #3a2a1a;
        }

        .form-box p {
            margin-top: 15px;
            text-align: center;
            font-size: 14px;
        }

        .form-box a {
            color: #c2955d;
            text-decoration: none;
            font-weight: bold;
        }

        .form-box a:hover {
            text-decoration: underline;
        }

        .success-msg {
            color: #2ecc71;
            background-color: #eafaf1;
            border: 1px solid #2ecc71;
            padding: 10px;
            text-align: center;
            border-radius: 6px;
            margin-top: 15px;
            font-weight: bold;
        }

        .error-msg {
            color: #e74c3c;
            background-color: #fceaea;
            border: 1px solid #e74c3c;
            padding: 10px;
            text-align: center;
            border-radius: 6px;
            margin-top: 15px;
            font-weight: bold;
        }
    </style>
</head>
<body>

    <div class="login-container">
        <div class="form-box">
            <h2>Registrarse</h2>

            <form action="<%= request.getContextPath() %>/RegistroController" method="post">
                <input type="text" name="nombre_completo" placeholder="Nombre completo" required>
                <input type="text" name="telefono" placeholder="Teléfono" required>
                <input type="email" name="email" placeholder="Correo electrónico" required>
                <input type="text" name="direccion" placeholder="Dirección" required>
                <button type="submit">Registrar</button>
            </form>

            <% if (request.getAttribute("exito") != null) { %>
                <p class="success-msg">✅ <%= request.getAttribute("exito") %></p>
            <% } %>

            <% if (request.getAttribute("error") != null) { %>
                <p class="error-msg">❌ <%= request.getAttribute("error") %></p>
            <% } %>

            <p>¿Ya tienes cuenta?
                <a href="<%= request.getContextPath() %>/vistas/login.jsp">Inicia sesión</a>
            </p>
        </div>
    </div>

</body>
</html>
